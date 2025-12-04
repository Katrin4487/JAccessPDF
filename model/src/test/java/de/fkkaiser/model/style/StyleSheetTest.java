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
package de.fkkaiser.model.style;

import de.fkkaiser.model.structure.ElementTargetType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class StyleSheetTest {

    @Test
    @DisplayName("Should find FontStyle by name")
    void shouldFindFontStyleByStyleName() {
        TextStyle myStyle = new TextStyle("default-text-style",
                "12px",
                "Arial",
                "400",
                "normal");
        StyleSheet styleSheet = new StyleSheet(Collections.singletonList(myStyle), null, null);

        assertTrue(styleSheet.findFontStyleByName("default-text-style").isPresent(), "Should find a font if it is present");
        assertFalse(styleSheet.findFontStyleByName("special-text-style").isPresent(), "Should not find a font that is not present");

    }

    @Test
    @DisplayName("Should handle an empty or null font list")
    void shouldHandleAnEmptyFontList() {

        StyleSheet styleSheet = new StyleSheet(null, null, null);

        assertFalse(styleSheet.findFontStyleByName("a-text-style").isPresent(), "Should not find a font if Text-Styles is null");

        styleSheet = new StyleSheet(new ArrayList<>(), null, null);

        assertFalse(styleSheet.findFontStyleByName("a-text-style").isPresent(), "Should not find a font if Text-Styles is empty");

    }

    @Test
    @DisplayName("StyleSheetBuilder should build empty lists")
    void StyleSheetBuilderShouldBuildEmptyLists() {

        StyleSheet styleSheet = StyleSheet.builder().build();
        assertTrue(styleSheet.textStyles().isEmpty(), "Default StyleSheetBuilder should have empty text-styles list");
        assertTrue(styleSheet.elementStyles().isEmpty(), "Default StyleSheetBuilder should have empty elements list");
        assertTrue(styleSheet.pageMasterStyles().isEmpty(), "Default StyleSheetBuilder should have empty page-master styles list");

    }

    @Test
    @DisplayName("StyleSheetBuilder should add styles")
    void StyleSheetBuilderShouldAddStyles() {

        ElementStyleProperties elementStyleProperties = new ElementBlockStyleProperties();
        StyleSheet styleSheet = StyleSheet.builder()
                .addTextStyle(new TextStyle("default-text-style", "12pt", "Arial", "700", "normal"))
                .addElementStyle(new ElementStyle("default-element-style", ElementTargetType.PARAGRAPH, elementStyleProperties))
                .addPageMasterStyle(new PageMasterStyle())
                .build();

        assertEquals(1, styleSheet.textStyles().size(), "TextStyle should have be added");
        assertEquals(1, styleSheet.elementStyles().size(), "ElementStyle should have be added");
        assertEquals(1, styleSheet.pageMasterStyles().size(), "PageMasterStyle should have be added");

    }

    @Test
    @DisplayName("StyleSheetBuilder should handle null input")
    void StyleSheetBuilderShouldHandleNullInput() {

        ElementStyleProperties elementStyleProperties = new ElementBlockStyleProperties();
        StyleSheet styleSheet = StyleSheet.builder()
                .addTextStyle(null)
                .addElementStyle(null)
                .addPageMasterStyle(null)
                .build();

        assertEquals(0, styleSheet.textStyles().size(), "TextStyle-List should be empty");
        assertEquals(0, styleSheet.elementStyles().size(), "Element-Style List should be empty");
        assertEquals(0, styleSheet.pageMasterStyles().size(), "Page Master-Style List should be empty");

    }
}
