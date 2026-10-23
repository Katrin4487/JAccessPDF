/*
 * Copyright 2025 Katrin Kaiser
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.fkkaiser.markdown;

import de.fkkaiser.model.structure.BlockImage;
import de.fkkaiser.model.structure.Element;
import de.fkkaiser.model.structure.Headline;
import de.fkkaiser.model.structure.ListOrdering;
import de.fkkaiser.model.structure.SimpleList;
import org.commonmark.node.BulletList;
import org.commonmark.node.Heading;
import org.commonmark.node.Image;
import org.commonmark.node.ListBlock;
import org.commonmark.node.Node;
import org.commonmark.node.OrderedList;
import org.commonmark.parser.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Converts a Markdown document (or fragment) into a {@code List<Element>}, suitable
 * for a {@code ContentArea}, a {@code Section}'s elements, or a {@code TableCell}.
 * <p>
 * Supports headings, paragraphs (with {@code *em*}/{@code **strong**}/{@code `code`}/
 * links via {@link InlineMarkdownConverter}'s underlying logic), bullet and ordered
 * lists (including nesting - a list item may contain further block content, such as a
 * nested list), and image-only paragraphs ({@code ![alt](src)} as the sole content of
 * a paragraph, the common Markdown idiom for a block image).
 * <p>
 * Deliberately <strong>not</strong> supported: block quotes, thematic breaks, code
 * blocks, tables, raw HTML, and images mixed with other inline content in the same
 * paragraph. Each of those either has no well-defined mapping onto the document model
 * yet, or (for block quotes specifically) a known accessibility gap in how FOP writes
 * such content to the PDF structure tree. Such input throws
 * {@link MarkdownConversionException} rather than being silently dropped.
 *
 * <pre>{@code
 * List<Element> elements = BlockMarkdownConverter.convert(
 *         "# Title\n\nSome **bold** text.\n\n- one\n- two");
 * ContentArea body = new ContentArea(elements);
 * }</pre>
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
public final class BlockMarkdownConverter {

    private static final Parser PARSER = Parser.builder().build();

    private BlockMarkdownConverter() {
        // Utility class
    }

    /**
     * Converts {@code markdown} using the default style mapping
     * ({@link MarkdownStyleMapping#defaults()}).
     *
     * @param markdown the Markdown-formatted source text
     * @return the resulting elements, in document order
     * @throws NullPointerException if markdown is null
     * @throws MarkdownConversionException if markdown contains an unsupported construct
     */
    public static List<Element> convert(String markdown) {
        return convert(markdown, MarkdownStyleMapping.defaults());
    }

    /**
     * Converts {@code markdown} into elements, using {@code styleMapping} to name the
     * styles applied to {@code **strong**}, {@code *em*}, and {@code `code`} text
     * inside paragraphs and headings.
     *
     * @param markdown the Markdown-formatted source text
     * @param styleMapping the style names to use for strong/emphasis/code text
     * @return the resulting elements, in document order
     * @throws NullPointerException if markdown or styleMapping is null
     * @throws MarkdownConversionException if markdown contains an unsupported construct
     */
    public static List<Element> convert(String markdown, MarkdownStyleMapping styleMapping) {
        Objects.requireNonNull(markdown, "markdown must not be null");
        Objects.requireNonNull(styleMapping, "styleMapping must not be null");

        Node document = PARSER.parse(markdown);
        return convertChildren(document, styleMapping);
    }

    private static List<Element> convertChildren(Node parent, MarkdownStyleMapping styleMapping) {
        List<Element> result = new ArrayList<>();
        for (Node child = parent.getFirstChild(); child != null; child = child.getNext()) {
            result.add(convertBlock(child, styleMapping));
        }
        return result;
    }

    private static Element convertBlock(Node node, MarkdownStyleMapping styleMapping) {
        if (node instanceof Heading heading) {
            return new Headline(null, InlineMarkdownVisitor.extractInlineContent(heading, styleMapping), heading.getLevel());
        }
        if (node instanceof org.commonmark.node.Paragraph paragraph) {
            Image soleImage = soleChildImage(paragraph);
            if (soleImage != null) {
                return convertImage(soleImage);
            }
            return new de.fkkaiser.model.structure.Paragraph(null,
                    InlineMarkdownVisitor.extractInlineContent(paragraph, styleMapping));
        }
        if (node instanceof BulletList bulletList) {
            return convertList(bulletList, ListOrdering.UNORDERED, styleMapping);
        }
        if (node instanceof OrderedList orderedList) {
            return convertList(orderedList, ListOrdering.ORDERED, styleMapping);
        }
        throw unsupported(node);
    }

    /**
     * A paragraph consisting of exactly one Image and nothing else is the standard
     * Markdown idiom for a block image ({@code ![alt](src)} on its own line/paragraph).
     * Returns null for every other paragraph, including one that mixes an image with
     * other inline content - that case falls through to the normal inline-content path,
     * which rejects the Image the same way {@link InlineMarkdownConverter} does.
     */
    private static Image soleChildImage(org.commonmark.node.Paragraph paragraph) {
        Node onlyChild = paragraph.getFirstChild();
        if (onlyChild instanceof Image image && onlyChild.getNext() == null) {
            return image;
        }
        return null;
    }

    private static BlockImage convertImage(Image image) {
        // Markdown's alt text is the image's bracketed label content, not getTitle()
        // (which is the separate, optional "title" tooltip attribute). An empty alt
        // text ("![](src)") is mapped to null, matching BlockImage's own convention
        // that a null altText marks the image as a decorative artifact.
        String altText = InlineMarkdownVisitor.plainTextOf(image);
        return new BlockImage(null, image.getDestination(), altText.isEmpty() ? null : altText, null, null);
    }

    private static SimpleList convertList(ListBlock listBlock, ListOrdering ordering, MarkdownStyleMapping styleMapping) {
        List<de.fkkaiser.model.structure.ListItem> items = new ArrayList<>();
        for (Node child = listBlock.getFirstChild(); child != null; child = child.getNext()) {
            if (!(child instanceof org.commonmark.node.ListItem commonmarkItem)) {
                throw unsupported(child);
            }
            // A list item's children are themselves block-level (Paragraph, nested
            // BulletList/OrderedList, ...), matching ListItem.elements: List<Element>.
            items.add(new de.fkkaiser.model.structure.ListItem(null, null,
                    convertChildren(commonmarkItem, styleMapping)));
        }
        return new SimpleList(null, ordering, items);
    }

    private static MarkdownConversionException unsupported(Node node) {
        return new MarkdownConversionException(
                "Unsupported Markdown construct for block conversion: " + node.getClass().getSimpleName()
                        + ". Block Markdown currently supports headings, paragraphs (including *em*/**strong**/"
                        + "`code`/links), bullet/ordered lists (with nesting), and image-only paragraphs - not "
                        + "block quotes, thematic breaks, code blocks, tables, raw HTML, or images mixed with "
                        + "other content in the same paragraph."
        );
    }
}
