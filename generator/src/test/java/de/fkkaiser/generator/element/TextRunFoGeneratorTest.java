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
package de.fkkaiser.generator.element;

import de.fkkaiser.model.structure.TextRun;
import de.fkkaiser.model.style.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the TextRunFoGenerator class.
 */
class TextRunFoGeneratorTest {

    private TextRunFoGenerator generator;
    private StyleSheet styleSheet;
    private StringBuilder builder;
    private StyleResolverContext context;


    @BeforeEach
    void setUp() {
        generator = new TextRunFoGenerator();
        builder = new StringBuilder();

        // Create a StyleSheet with some predefined text styles for testing
        TextStyle boldStyle = new TextStyle("bold-font", "12pt", "Arial","bold", "normal");
        TextStyle italicStyle = new TextStyle("italic-font", "10pt", "Times New Roman","normal", "italic");

        ParagraphStyleProperties paragraphStyleProps = new ParagraphStyleProperties();
        paragraphStyleProps.setTextStyleName("italic-font");

        TextRunStyleProperties textRunStyleProps = new TextRunStyleProperties();
        textRunStyleProps.setTextStyleName("bold-font");
        ElementStyle paragraphStyle = new ElementStyle("paragraph-style", StyleTargetTypes.PARAGRAPH,paragraphStyleProps);
        ElementStyle textRunStyle = new ElementStyle("text-run-style",StyleTargetTypes.TEXT_RUN,textRunStyleProps);


        TextRunStyleProperties textRunStyleProps2 = new TextRunStyleProperties();
        textRunStyleProps2.setTextColor("red");
        textRunStyleProps2.setTextDecoration("underline");
        textRunStyleProps2.setBaselineShift("super");

        ElementStyle textRunStyle2 = new ElementStyle("text-run-style-2",StyleTargetTypes.TEXT_RUN,textRunStyleProps2);

        styleSheet = new StyleSheet(List.of(boldStyle, italicStyle), List.of(paragraphStyle,textRunStyle,textRunStyle2), Collections.emptyList());

        Map<String,ElementStyle> styleMap = new HashMap<>();
        styleMap.put("paragraph-style",paragraphStyle);
        styleMap.put("text-run-style",textRunStyle);
        styleMap.put("text-run-style-2",textRunStyle2);
        context = new StyleResolverContext(styleMap,paragraphStyleProps);
    }


    @Test
    @DisplayName("Should generate plain text when no styling is applied")
    void shouldGeneratePlainTextWhenNoStylingIsApplied() {

        TextRun textRun = new TextRun("Unstyled text");

        generator.generate(textRun, styleSheet, builder);
        String result = builder.toString();

        // ASSERT
        assertEquals("Unstyled text", result, "Should only output the plain text if no styling is present.");
        assertFalse(result.contains("<fo:inline>"), "Should not generate an fo:inline wrapper for empty styles.");
    }

    @Test
    @DisplayName("Should apply styles from a matched TextStyle in the StyleSheet")
    void shouldApplyStylesFromMatchedTextStyle() {
        TextRun textRun = new TextRun("Bold text","text-run-style");
        textRun.resolveStyles(context);

        generator.generate(textRun, styleSheet, builder);
        String result = builder.toString();

        assertTrue(result.startsWith("<fo:inline"), "Should start with an fo:inline tag.");
        assertTrue(result.contains(" font-family=\"Arial\""), "Font family from style sheet is missing.");
        assertTrue(result.contains(" font-size=\"12pt\""), "Font size from style sheet is missing.");
        assertTrue(result.contains(" font-weight=\"bold\""), "Font weight from style sheet is missing.");
        assertTrue(result.contains(" font-style=\"normal\""), "Font style from style sheet is missing.");
        assertTrue(result.endsWith(">Bold text</fo:inline>"), "Should contain the text and be properly closed.");
    }

    @Test
    @DisplayName("Should apply direct styles from TextRunStyleProperties")
    void generate_withDirectStyles_shouldApplyDirectStyles() {

        TextRun textRun = new TextRun("Red underlined text","text-run-style-2");
        textRun.resolveStyles(context);
        generator.generate(textRun, styleSheet, builder);
        String result = builder.toString();


        assertTrue(result.startsWith("<fo:inline"), "Should start with an fo:inline tag.");
        assertTrue(result.contains(" color=\"red\""), "Direct color style is missing.");
        assertTrue(result.contains(" text-decoration=\"underline\""), "Direct text-decoration style is missing.");
        assertTrue(result.contains(" baseline-shift=\"super\""), "Direct baseline-shift style is missing.");
        assertTrue(result.endsWith(">Red underlined text</fo:inline>"), "Should contain the text and be properly closed.");
    }


}