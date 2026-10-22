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

import de.fkkaiser.model.structure.Hyperlink;
import de.fkkaiser.model.structure.InlineElement;
import de.fkkaiser.model.structure.TextRun;
import org.commonmark.node.*;

import java.util.List;

/**
 * Walks a Markdown AST and appends the corresponding {@link InlineElement}s to a
 * target list. Only plain text, {@code *em*}, {@code **strong**}, {@code `code`},
 * links, and (multiple) paragraphs are supported - anything block-level (headings,
 * lists, block quotes, code blocks, tables, ...), raw HTML, and images are rejected
 * with a {@link MarkdownConversionException} rather than silently dropped or
 * flattened into plain text, since all of those carry structure or meaning that a
 * flat {@code List<InlineElement>} cannot represent.
 * <p>
 * Nested inline formatting (e.g. {@code **bold with *nested italic***}) is
 * flattened to plain text under the outermost style - inline elements in this
 * model carry a single style name, so true nesting is not representable.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
class InlineMarkdownVisitor extends AbstractVisitor {

    private final MarkdownStyleMapping styleMapping;
    private final List<InlineElement> result;
    private boolean emittedAnyParagraph = false;

    InlineMarkdownVisitor(MarkdownStyleMapping styleMapping, List<InlineElement> result) {
        this.styleMapping = styleMapping;
        this.result = result;
    }

    // === Supported constructs ===

    @Override
    public void visit(Paragraph paragraph) {
        // Multiple paragraphs are flattened into one run of inline content,
        // separated by a single space - there is no block boundary to represent here.
        if (emittedAnyParagraph) {
            result.add(new TextRun(" "));
        }
        emittedAnyParagraph = true;
        visitChildren(paragraph);
    }

    @Override
    public void visit(Text text) {
        result.add(new TextRun(text.getLiteral()));
    }

    @Override
    public void visit(Emphasis emphasis) {
        result.add(new TextRun(plainText(emphasis), styleMapping.emphasisStyleName()));
    }

    @Override
    public void visit(StrongEmphasis strongEmphasis) {
        result.add(new TextRun(plainText(strongEmphasis), styleMapping.strongStyleName()));
    }

    @Override
    public void visit(Code code) {
        result.add(new TextRun(code.getLiteral(), styleMapping.codeStyleName()));
    }

    @Override
    public void visit(Link link) {
        // styleClass is left null on purpose: Hyperlink.getStandardElementType() resolves
        // to StandardElementType.HYPERLINK, so the convention-name fallback ("hyperlink-default")
        // already applies styling without the converter having to name it explicitly.
        result.add(new Hyperlink(plainText(link), null, link.getDestination(), link.getTitle()));
    }

    @Override
    public void visit(SoftLineBreak softLineBreak) {
        result.add(new TextRun(" "));
    }

    @Override
    public void visit(HardLineBreak hardLineBreak) {
        // Known limitation: a forced line break inside inline text is rendered as a
        // plain space, not an actual break - there is no inline-level line break
        // concept in the document model.
        result.add(new TextRun(" "));
    }

    // === Rejected constructs ===
    // Each of these carries structure or meaning a flat List<InlineElement> cannot
    // represent, so they fail loudly instead of being silently dropped or flattened.

    @Override
    public void visit(BlockQuote blockQuote) {
        throw unsupported(blockQuote);
    }

    @Override
    public void visit(BulletList bulletList) {
        throw unsupported(bulletList);
    }

    @Override
    public void visit(OrderedList orderedList) {
        throw unsupported(orderedList);
    }

    @Override
    public void visit(ListItem listItem) {
        throw unsupported(listItem);
    }

    @Override
    public void visit(Heading heading) {
        throw unsupported(heading);
    }

    @Override
    public void visit(ThematicBreak thematicBreak) {
        throw unsupported(thematicBreak);
    }

    @Override
    public void visit(FencedCodeBlock fencedCodeBlock) {
        throw unsupported(fencedCodeBlock);
    }

    @Override
    public void visit(IndentedCodeBlock indentedCodeBlock) {
        throw unsupported(indentedCodeBlock);
    }

    @Override
    public void visit(HtmlBlock htmlBlock) {
        throw unsupported(htmlBlock);
    }

    @Override
    public void visit(HtmlInline htmlInline) {
        throw unsupported(htmlInline);
    }

    @Override
    public void visit(Image image) {
        throw unsupported(image);
    }

    @Override
    public void visit(LinkReferenceDefinition linkReferenceDefinition) {
        throw unsupported(linkReferenceDefinition);
    }

    @Override
    public void visit(CustomBlock customBlock) {
        throw unsupported(customBlock);
    }

    @Override
    public void visit(CustomNode customNode) {
        throw unsupported(customNode);
    }

    // === Helpers ===

    /**
     * Flattens the plain text content of an inline node, descending into nested
     * emphasis/strong/link/line-break nodes (their own styling is discarded), and
     * rejecting anything else the same way the top-level visitor does.
     */
    private static String plainText(Node node) {
        StringBuilder sb = new StringBuilder();
        appendPlainText(node, sb);
        return sb.toString();
    }

    private static void appendPlainText(Node node, StringBuilder sb) {
        for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
            if (child instanceof Text text) {
                sb.append(text.getLiteral());
            } else if (child instanceof Code code) {
                sb.append(code.getLiteral());
            } else if (child instanceof SoftLineBreak || child instanceof HardLineBreak) {
                sb.append(' ');
            } else if (child instanceof Emphasis || child instanceof StrongEmphasis || child instanceof Link) {
                appendPlainText(child, sb);
            } else {
                throw unsupported(child);
            }
        }
    }

    private static MarkdownConversionException unsupported(Node node) {
        return new MarkdownConversionException(
                "Unsupported Markdown construct for inline conversion: " + node.getClass().getSimpleName()
                        + ". Inline Markdown only supports plain text, *emphasis*, **strong**, `code`, links, "
                        + "and multiple paragraphs (joined with a space) - not headings, lists, block quotes, "
                        + "code blocks, tables, images, or raw HTML."
        );
    }
}
