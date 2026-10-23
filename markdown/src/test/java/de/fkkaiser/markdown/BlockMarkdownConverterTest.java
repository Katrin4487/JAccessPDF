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
import de.fkkaiser.model.structure.ListItem;
import de.fkkaiser.model.structure.ListOrdering;
import de.fkkaiser.model.structure.Paragraph;
import de.fkkaiser.model.structure.SimpleList;
import de.fkkaiser.model.structure.TextRun;
import de.fkkaiser.model.style.builder.StandardElementStyles;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BlockMarkdownConverter Tests")
class BlockMarkdownConverterTest {

    @Test
    @DisplayName("Should convert a heading to a Headline with correct level")
    void shouldConvertHeading() {
        List<Element> result = BlockMarkdownConverter.convert("## Section Title");

        assertEquals(1, result.size());
        Headline headline = (Headline) result.getFirst();
        assertEquals(2, headline.getLevel());
        assertEquals("Section Title", ((TextRun) headline.getInlineElements().getFirst()).getText());
        assertNull(headline.getStyleClass());
    }

    @Test
    @DisplayName("Should convert a paragraph with inline formatting")
    void shouldConvertParagraphWithInlineFormatting() {
        List<Element> result = BlockMarkdownConverter.convert("Hello **world**.");

        assertEquals(1, result.size());
        Paragraph paragraph = (Paragraph) result.getFirst();
        assertNull(paragraph.getStyleClass());
        List<de.fkkaiser.model.structure.InlineElement> inline = paragraph.getInlineElements();
        assertEquals(3, inline.size());
        assertEquals("world", ((TextRun) inline.get(1)).getText());
        assertEquals(StandardElementStyles.STRONG_STYLE_NAME, inline.get(1).getStyleClass());
    }

    @Test
    @DisplayName("Should convert multiple top-level blocks in document order")
    void shouldConvertMultipleBlocks() {
        List<Element> result = BlockMarkdownConverter.convert("# Title\n\nFirst paragraph.\n\nSecond paragraph.");

        assertEquals(3, result.size());
        assertInstanceOf(Headline.class, result.get(0));
        assertInstanceOf(Paragraph.class, result.get(1));
        assertInstanceOf(Paragraph.class, result.get(2));
    }

    @Test
    @DisplayName("Should convert a bullet list")
    void shouldConvertBulletList() {
        List<Element> result = BlockMarkdownConverter.convert("- one\n- two\n- three");

        assertEquals(1, result.size());
        SimpleList list = (SimpleList) result.getFirst();
        assertEquals(ListOrdering.UNORDERED, list.getOrdering());
        assertEquals(3, list.getItems().size());

        ListItem first = list.getItems().getFirst();
        assertEquals(1, first.getElements().size());
        Paragraph itemParagraph = (Paragraph) first.getElements().getFirst();
        assertEquals("one", ((TextRun) itemParagraph.getInlineElements().getFirst()).getText());
    }

    @Test
    @DisplayName("Should convert an ordered list")
    void shouldConvertOrderedList() {
        List<Element> result = BlockMarkdownConverter.convert("1. first\n2. second");

        SimpleList list = (SimpleList) result.getFirst();
        assertEquals(ListOrdering.ORDERED, list.getOrdering());
        assertEquals(2, list.getItems().size());
    }

    @Test
    @DisplayName("Should convert a nested list inside a list item")
    void shouldConvertNestedList() {
        List<Element> result = BlockMarkdownConverter.convert("- outer\n  - inner one\n  - inner two");

        SimpleList outerList = (SimpleList) result.getFirst();
        assertEquals(1, outerList.getItems().size());

        ListItem outerItem = outerList.getItems().getFirst();
        // outer item contains its own paragraph ("outer") plus a nested list
        assertEquals(2, outerItem.getElements().size());
        assertInstanceOf(Paragraph.class, outerItem.getElements().get(0));
        SimpleList innerList = (SimpleList) outerItem.getElements().get(1);
        assertEquals(2, innerList.getItems().size());
    }

    @Test
    @DisplayName("Should convert an image-only paragraph to a BlockImage")
    void shouldConvertImageOnlyParagraph() {
        List<Element> result = BlockMarkdownConverter.convert("![Company Logo](logo.png)");

        assertEquals(1, result.size());
        BlockImage image = (BlockImage) result.getFirst();
        assertEquals("logo.png", image.getPath());
        assertEquals("Company Logo", image.getAltText());
    }

    @Test
    @DisplayName("Should map an empty alt text to null (decorative artifact)")
    void shouldMapEmptyAltTextToNull() {
        List<Element> result = BlockMarkdownConverter.convert("![](decoration.png)");

        BlockImage image = (BlockImage) result.getFirst();
        assertNull(image.getAltText());
    }

    @Test
    @DisplayName("Should reject block quotes")
    void shouldRejectBlockQuotes() {
        assertThrows(MarkdownConversionException.class,
                () -> BlockMarkdownConverter.convert("> quoted text"));
    }

    @Test
    @DisplayName("Should reject thematic breaks")
    void shouldRejectThematicBreaks() {
        assertThrows(MarkdownConversionException.class,
                () -> BlockMarkdownConverter.convert("---"));
    }

    @Test
    @DisplayName("Should reject fenced code blocks")
    void shouldRejectFencedCodeBlocks() {
        assertThrows(MarkdownConversionException.class,
                () -> BlockMarkdownConverter.convert("```\ncode\n```"));
    }

    @Test
    @DisplayName("Should reject raw HTML blocks")
    void shouldRejectHtmlBlocks() {
        assertThrows(MarkdownConversionException.class,
                () -> BlockMarkdownConverter.convert("<div>raw html</div>"));
    }

    @Test
    @DisplayName("Should reject an image mixed with other inline content in the same paragraph")
    void shouldRejectMixedImageParagraph() {
        assertThrows(MarkdownConversionException.class,
                () -> BlockMarkdownConverter.convert("Text before ![alt](img.png) text after"));
    }

    @Test
    @DisplayName("Should throw NullPointerException for null markdown")
    void shouldThrowOnNullMarkdown() {
        assertThrows(NullPointerException.class, () -> BlockMarkdownConverter.convert(null));
    }

    @Test
    @DisplayName("Should return an empty list for empty input")
    void shouldReturnEmptyListForEmptyInput() {
        assertTrue(BlockMarkdownConverter.convert("").isEmpty());
    }

    @Test
    @DisplayName("Should respect a custom MarkdownStyleMapping")
    void shouldRespectCustomStyleMapping() {
        MarkdownStyleMapping customMapping = new MarkdownStyleMapping("my-strong", "my-em", "my-code");

        List<Element> result = BlockMarkdownConverter.convert("**bold**", customMapping);

        Paragraph paragraph = (Paragraph) result.getFirst();
        assertEquals("my-strong", paragraph.getInlineElements().getFirst().getStyleClass());
    }
}
