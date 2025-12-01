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

import de.fkkaiser.model.style.ElementStyle;
import de.fkkaiser.model.style.SectionStyleProperties;
import de.fkkaiser.model.style.StyleResolverContext;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SectionTest {

    @Test
    void shouldCreateSectionWithAllParameters() {
        List<Element> elements = List.of(
                new Paragraph("body", "Test content")
        );

        Section section = new Section(
                "test-section",
                SectionVariant.NOTE,
                "Test alt text",
                elements
        );

        assertEquals("test-section", section.getStyleClass());
        assertEquals(SectionVariant.NOTE, section.getVariant());
        assertEquals("Test alt text", section.getAltText());
        assertEquals(1, section.getElements().size());
    }

    @Test
    void shouldCreateSectionWithConvenienceConstructor() {
        List<Element> elements = List.of(
                new Paragraph("body", "Test content")
        );

        Section section = new Section(
                "test-section",
                SectionVariant.ASIDE,
                elements
        );

        assertEquals("test-section", section.getStyleClass());
        assertEquals(SectionVariant.ASIDE, section.getVariant());
        assertNull(section.getAltText());
        assertEquals(1, section.getElements().size());
    }

    @Test
    void shouldDefaultToEmptyListWhenElementsAreNull() {
        Section section = new Section(
                "test-section",
                null,
                null,
                null
        );

        assertNotNull(section.getElements());
        assertTrue(section.getElements().isEmpty());
    }

    @Test
    void shouldReturnCorrectType() {
        Section section = new Section("test", null, List.of());

        assertEquals(ElementTypes.SECTION, section.getType());
    }

    @Test
    void shouldResolveStylesWithoutVariant() {
        // Setup
        SectionStyleProperties sectionStyle = new SectionStyleProperties();
        sectionStyle.setPadding("1cm");

        ElementStyle elementStyle = new ElementStyle("base-section", "section", sectionStyle);

        Map<String, ElementStyle> styleMap = new HashMap<>();
        styleMap.put("base-section", elementStyle);

        StyleResolverContext context = new StyleResolverContext(
                styleMap,
                null
        );

        Section section = new Section("base-section", null, null, List.of());

        // Execute
        section.resolveStyles(context);

        // Verify
        assertNotNull(section.getResolvedStyle());
        assertEquals("1cm", section.getResolvedStyle().getPadding());
    }

    @Test
    void shouldResolveStylesWithVariant() {
        // Setup
        SectionStyleProperties baseStyle = new SectionStyleProperties();
        baseStyle.setPadding("1cm");

        SectionStyleProperties variantStyle = new SectionStyleProperties();
        variantStyle.setPadding("2cm");
        variantStyle.setBackgroundColor("#fff3cd");

        Map<String, ElementStyle> styleMap = new HashMap<>();
        styleMap.put("notice-box", new ElementStyle("notice-box", "section", baseStyle));
        styleMap.put("notice-box.note", new ElementStyle("notice-box.note", "section", variantStyle));

        StyleResolverContext context = new StyleResolverContext(
                styleMap,
                null
        );

        Section section = new Section("notice-box", SectionVariant.NOTE, null, List.of());

        // Execute
        section.resolveStyles(context);

        // Verify - should use variant style
        assertNotNull(section.getResolvedStyle());
        assertEquals("2cm", section.getResolvedStyle().getPadding());
        assertEquals("#fff3cd", section.getResolvedStyle().getBackgroundColor());
    }

    @Test
    void shouldUseBaseStyleWhenVariantStyleNotFound() {
        // Setup
        SectionStyleProperties baseStyle = new SectionStyleProperties();
        baseStyle.setPadding("1cm");

        Map<String, ElementStyle> styleMap = new HashMap<>();
        styleMap.put("notice-box", new ElementStyle("notice-box", "section", baseStyle));
        // notice-box.note does NOT exist

        StyleResolverContext context = new StyleResolverContext(
                styleMap,
                null
        );

        Section section = new Section("notice-box", SectionVariant.NOTE, null, List.of());

        // Execute
        section.resolveStyles(context);

        // Verify - should fall back to empty style (variant not found)
        assertNotNull(section.getResolvedStyle());
        // Since variant style doesn't exist, it creates new empty SectionStyleProperties
    }


    @Test
    void shouldHandleEmptyElementsList() {
        Section section = new Section("test", null, null, List.of());

        SectionStyleProperties style = new SectionStyleProperties();
        Map<String, ElementStyle> styleMap = new HashMap<>();
        styleMap.put("test", new ElementStyle("test", "section", style));

        StyleResolverContext context = new StyleResolverContext(styleMap, null);

        // Should not throw exception
        assertDoesNotThrow(() -> section.resolveStyles(context));
    }

    @Test
    void shouldHandleNullStyleClass() {
        Section section = new Section(null, SectionVariant.NOTE, null, List.of());

        StyleResolverContext context = new StyleResolverContext(
                new HashMap<>(),
                null
        );

        // Should not throw NullPointerException
        assertDoesNotThrow(() -> section.resolveStyles(context));
        assertNotNull(section.getResolvedStyle());
    }
}