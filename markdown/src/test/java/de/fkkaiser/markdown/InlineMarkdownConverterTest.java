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
import de.fkkaiser.model.style.builder.StandardElementStyles;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("InlineMarkdownConverter Tests")
class InlineMarkdownConverterTest {

    @Test
    @DisplayName("Should convert plain text to a single unstyled TextRun")
    void shouldConvertPlainText() {
        List<InlineElement> result = InlineMarkdownConverter.convert("Hello World");

        assertEquals(1, result.size());
        TextRun run = (TextRun) result.getFirst();
        assertEquals("Hello World", run.getText());
        assertNull(run.getStyleClass());
    }

    @Test
    @DisplayName("Should convert **strong** to a styled TextRun")
    void shouldConvertStrong() {
        List<InlineElement> result = InlineMarkdownConverter.convert("**bold**");

        assertEquals(1, result.size());
        TextRun run = (TextRun) result.getFirst();
        assertEquals("bold", run.getText());
        assertEquals(StandardElementStyles.STRONG_STYLE_NAME, run.getStyleClass());
    }

    @Test
    @DisplayName("Should convert *em* to a styled TextRun")
    void shouldConvertEmphasis() {
        List<InlineElement> result = InlineMarkdownConverter.convert("*italic*");

        assertEquals(1, result.size());
        TextRun run = (TextRun) result.getFirst();
        assertEquals("italic", run.getText());
        assertEquals(StandardElementStyles.EM_STYLE_NAME, run.getStyleClass());
    }

    @Test
    @DisplayName("Should convert `code` to a styled TextRun")
    void shouldConvertCode() {
        List<InlineElement> result = InlineMarkdownConverter.convert("`code()`");

        assertEquals(1, result.size());
        TextRun run = (TextRun) result.getFirst();
        assertEquals("code()", run.getText());
        assertEquals(StandardElementStyles.CODE_STYLE_NAME, run.getStyleClass());
    }

    @Test
    @DisplayName("Should convert a link with distinct text and href to a Hyperlink")
    void shouldConvertLink() {
        List<InlineElement> result = InlineMarkdownConverter.convert("[JAccessPDF](https://example.com \"Title\")");

        assertEquals(1, result.size());
        Hyperlink link = (Hyperlink) result.getFirst();
        assertEquals("JAccessPDF", link.getText());
        assertEquals("https://example.com", link.getHref());
        assertEquals("Title", link.getAltText());
        // No explicit styleClass: relies on the "hyperlink-default" convention fallback.
        assertNull(link.getStyleClass());
    }

    @Test
    @DisplayName("Should convert mixed inline content in document order")
    void shouldConvertMixedContent() {
        List<InlineElement> result = InlineMarkdownConverter.convert(
                "Hello **world**, *you* are `great`! See [this](https://example.com).");

        assertEquals(9, result.size());
        assertEquals("Hello ", ((TextRun) result.get(0)).getText());
        assertEquals("world", ((TextRun) result.get(1)).getText());
        assertEquals(StandardElementStyles.STRONG_STYLE_NAME, result.get(1).getStyleClass());
        assertEquals(", ", ((TextRun) result.get(2)).getText());
        assertEquals("you", ((TextRun) result.get(3)).getText());
        assertEquals(StandardElementStyles.EM_STYLE_NAME, result.get(3).getStyleClass());
        assertEquals(" are ", ((TextRun) result.get(4)).getText());
        assertEquals("great", ((TextRun) result.get(5)).getText());
        assertEquals(StandardElementStyles.CODE_STYLE_NAME, result.get(5).getStyleClass());
        assertEquals("! See ", ((TextRun) result.get(6)).getText());
        assertInstanceOf(Hyperlink.class, result.get(7));
        assertEquals("this", ((Hyperlink) result.get(7)).getText());
        assertEquals(".", ((TextRun) result.get(8)).getText());
    }

    @Test
    @DisplayName("Should flatten nested formatting to plain text under the outer style")
    void shouldFlattenNestedFormatting() {
        List<InlineElement> result = InlineMarkdownConverter.convert("**bold with *nested* text**");

        assertEquals(1, result.size());
        TextRun run = (TextRun) result.getFirst();
        assertEquals("bold with nested text", run.getText());
        assertEquals(StandardElementStyles.STRONG_STYLE_NAME, run.getStyleClass());
    }

    @Test
    @DisplayName("Should join multiple paragraphs with a single space")
    void shouldJoinMultipleParagraphs() {
        List<InlineElement> result = InlineMarkdownConverter.convert("First paragraph.\n\nSecond paragraph.");

        assertEquals(3, result.size());
        assertEquals("First paragraph.", ((TextRun) result.get(0)).getText());
        assertEquals(" ", ((TextRun) result.get(1)).getText());
        assertEquals("Second paragraph.", ((TextRun) result.get(2)).getText());
    }

    @Test
    @DisplayName("Should respect a custom MarkdownStyleMapping")
    void shouldRespectCustomStyleMapping() {
        MarkdownStyleMapping customMapping = new MarkdownStyleMapping("my-strong", "my-em", "my-code");

        List<InlineElement> result = InlineMarkdownConverter.convert("**bold**", customMapping);

        assertEquals("my-strong", result.getFirst().getStyleClass());
    }

    @Test
    @DisplayName("Should reject headings")
    void shouldRejectHeadings() {
        assertThrows(MarkdownConversionException.class,
                () -> InlineMarkdownConverter.convert("# Heading"));
    }

    @Test
    @DisplayName("Should reject lists")
    void shouldRejectLists() {
        assertThrows(MarkdownConversionException.class,
                () -> InlineMarkdownConverter.convert("- item one\n- item two"));
    }

    @Test
    @DisplayName("Should reject images")
    void shouldRejectImages() {
        assertThrows(MarkdownConversionException.class,
                () -> InlineMarkdownConverter.convert("![alt text](image.png)"));
    }

    @Test
    @DisplayName("Should reject raw inline HTML")
    void shouldRejectRawHtml() {
        assertThrows(MarkdownConversionException.class,
                () -> InlineMarkdownConverter.convert("plain <b>raw html</b> text"));
    }

    @Test
    @DisplayName("Should reject block quotes")
    void shouldRejectBlockQuotes() {
        assertThrows(MarkdownConversionException.class,
                () -> InlineMarkdownConverter.convert("> quoted"));
    }

    @Test
    @DisplayName("Should reject fenced code blocks")
    void shouldRejectFencedCodeBlocks() {
        assertThrows(MarkdownConversionException.class,
                () -> InlineMarkdownConverter.convert("```\ncode block\n```"));
    }

    @Test
    @DisplayName("Should reject an image nested inside otherwise-supported formatting")
    void shouldRejectNestedImage() {
        assertThrows(MarkdownConversionException.class,
                () -> InlineMarkdownConverter.convert("**bold with ![an image](x.png) inside**"));
    }

    @Test
    @DisplayName("Should throw NullPointerException for null markdown")
    void shouldThrowOnNullMarkdown() {
        assertThrows(NullPointerException.class, () -> InlineMarkdownConverter.convert(null));
    }

    @Test
    @DisplayName("Should return an empty list for empty input")
    void shouldReturnEmptyListForEmptyInput() {
        assertTrue(InlineMarkdownConverter.convert("").isEmpty());
    }
}
