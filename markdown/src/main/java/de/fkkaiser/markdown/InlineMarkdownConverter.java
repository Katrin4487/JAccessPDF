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

import de.fkkaiser.model.structure.InlineElement;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Converts a single Markdown-formatted text fragment into a
 * {@code List<InlineElement>}, for use inside one {@code Paragraph}, {@code Headline},
 * or other {@code TextBlock}.
 * <p>
 * Supports plain text, {@code *em*}/{@code _em_}, {@code **strong**}/{@code __strong__},
 * {@code `code`} spans and {@code [text](url "title")} links. Deliberately does
 * <strong>not</strong> support anything block-level (headings, lists, block quotes,
 * code blocks, tables, thematic breaks), raw HTML, or images - those carry structure
 * that a flat list of inline elements cannot represent, and converting a whole
 * Markdown document into block-level {@code Element}s is a separate concern. Such
 * input throws {@link MarkdownConversionException} rather than being silently
 * dropped or flattened.
 *
 * <pre>{@code
 * List<InlineElement> inline = InlineMarkdownConverter.convert("Hello **world**!");
 * Paragraph p = new Paragraph("p-default", inline);
 * }</pre>
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
public final class InlineMarkdownConverter {

    private static final Parser PARSER = Parser.builder().build();

    private InlineMarkdownConverter() {
        // Utility class
    }

    /**
     * Converts {@code markdown} using the default style mapping
     * ({@link MarkdownStyleMapping#defaults()}).
     *
     * @param markdown the Markdown-formatted source text
     * @return the resulting inline elements, in document order
     * @throws NullPointerException if markdown is null
     * @throws MarkdownConversionException if markdown contains an unsupported construct
     */
    public static List<InlineElement> convert(String markdown) {
        return convert(markdown, MarkdownStyleMapping.defaults());
    }

    /**
     * Converts {@code markdown} into inline elements, using {@code styleMapping} to name
     * the styles applied to {@code **strong**}, {@code *em*}, and {@code `code`} text.
     *
     * @param markdown the Markdown-formatted source text
     * @param styleMapping the style names to use for strong/emphasis/code text
     * @return the resulting inline elements, in document order
     * @throws NullPointerException if markdown or styleMapping is null
     * @throws MarkdownConversionException if markdown contains an unsupported construct
     */
    public static List<InlineElement> convert(String markdown, MarkdownStyleMapping styleMapping) {
        Objects.requireNonNull(markdown, "markdown must not be null");
        Objects.requireNonNull(styleMapping, "styleMapping must not be null");

        Node document = PARSER.parse(markdown);
        List<InlineElement> result = new ArrayList<>();
        document.accept(new InlineMarkdownVisitor(styleMapping, result));
        return result;
    }
}
