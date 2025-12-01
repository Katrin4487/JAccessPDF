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
package de.fkkaiser.model.structure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParagraphTest {

    private static final String PARAGRAPH_STYLE =  "Paragraph-Style";
    private static final String REGULAR_STYLE = "Regular-Style";
    private static final String BOLD_STYLE =  "Bold-Style";
    private static final String ITALIC_STYLE =  "Italic-Style";
    private static final String HYPERLINK_STYLE =  "Hyperlink-Style";


    @Test
    @DisplayName("Should create a normal Paragraph with chaining")
    public void testCreateParagraphWithChaining() {

        Paragraph paragraph = Paragraph.builder(PARAGRAPH_STYLE)
                .withStyleClassRegular(REGULAR_STYLE)
                .addInlineText("This is a text in my Paragraph")
                .addInlineText("This is a second text")
                .build();

        assertEquals(2,paragraph.inlineElements.size(),"Should contains all inline elements");
        assertEquals(REGULAR_STYLE, paragraph.inlineElements.getFirst().getStyleClass()
                ,"Inline elements have the correct style class");

    }

    @Test
    @DisplayName("Should set correct style classes with chaining")
    public void testSetStyleClassWithChaining() {

        Paragraph paragraph = Paragraph.builder(PARAGRAPH_STYLE)
                .withStyleClassRegular(REGULAR_STYLE)
                .withStyleClassBold(BOLD_STYLE)
                .withStyleClassItalic(ITALIC_STYLE)
                .withStyleClassHyperlink(HYPERLINK_STYLE)
                .addInlineText("Default Text")
                .addHyperlink("A Text","https://www.fkkaiser.de")
                .addInlineTextItalic("An italic Text")
                .addInlineTextBold("Bold Text")
                .build();

        assertEquals(BOLD_STYLE,paragraph.inlineElements.getLast().getStyleClass());
        assertEquals(REGULAR_STYLE,paragraph.inlineElements.getFirst().getStyleClass());
        assertEquals(ITALIC_STYLE,paragraph.inlineElements.get(2).getStyleClass());
        assertEquals(HYPERLINK_STYLE,paragraph.inlineElements.get(1).getStyleClass());
    }
}
