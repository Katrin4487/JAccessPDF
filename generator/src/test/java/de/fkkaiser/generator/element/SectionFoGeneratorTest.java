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

import de.fkkaiser.generator.ImageResolver;
import de.fkkaiser.generator.XslFoGenerator;
import de.fkkaiser.generator.element.SectionFoGenerator;
import de.fkkaiser.model.structure.Paragraph;
import de.fkkaiser.model.structure.Section;
import de.fkkaiser.model.structure.SectionVariant;
import de.fkkaiser.model.style.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SectionFoGenerator Tests")
class SectionFoGeneratorTest {

    private SectionFoGenerator generator;

    @Mock
    private XslFoGenerator mockMainGenerator;

    @Mock
    private ImageResolver mockResolver;

    private StyleSheet styleSheet;

    @BeforeEach
    void setUp() {
        generator = new SectionFoGenerator(mockMainGenerator);
        styleSheet = new StyleSheet(List.of(), List.of(), List.of());
    }

    @Test
    @DisplayName("should generate section with default Sect role")
    void shouldGenerateSectionWithDefaultRole() {
        Section section = new Section("test", null, null, List.of());
        SectionStyleProperties style = new SectionStyleProperties();
        section.setResolvedStyle(style);

        StringBuilder builder = new StringBuilder();
        generator.generate(section, styleSheet, builder, new ArrayList<>(), mockResolver, false);

        String result = builder.toString();
        assertTrue(result.contains("role=\"Sect\""));
        assertTrue(result.contains("<fo:block"));
        assertTrue(result.contains("</fo:block>"));
    }

    @Test
    @DisplayName("should generate section with Div role for NOTE variant")
    void shouldGenerateSectionWithNoteRole() {
        Section section = new Section("warning", SectionVariant.NOTE, null, List.of());
        SectionStyleProperties style = new SectionStyleProperties();
        section.setResolvedStyle(style);

        StringBuilder builder = new StringBuilder();
        generator.generate(section, styleSheet, builder, new ArrayList<>(), mockResolver, false);

        String result = builder.toString();
        assertTrue(result.contains("role=\"Div\""));
    }

    @Test
    @DisplayName("should generate section with Aside role for ASIDE variant")
    void shouldGenerateSectionWithAsideRole() {
        Section section = new Section("example", SectionVariant.ASIDE, null, List.of());
        SectionStyleProperties style = new SectionStyleProperties();
        section.setResolvedStyle(style);

        StringBuilder builder = new StringBuilder();
        generator.generate(section, styleSheet, builder, new ArrayList<>(), mockResolver, false);

        String result = builder.toString();
        assertTrue(result.contains("role=\"Aside\""));
    }

    @Test
    @DisplayName("should generate alt-text attribute when provided")
    void shouldGenerateAltText() {
        Section section = new Section("test", SectionVariant.NOTE, "Important warning", List.of());
        SectionStyleProperties style = new SectionStyleProperties();
        section.setResolvedStyle(style);

        StringBuilder builder = new StringBuilder();
        generator.generate(section, styleSheet, builder, new ArrayList<>(), mockResolver, false);

        String result = builder.toString();
        assertTrue(result.contains("fox:alt-text=\"Important warning\""));
    }

    @Test
    @DisplayName("should escape XML characters in alt-text")
    void shouldEscapeXmlInAltText() {
        Section section = new Section("test", null, "Warning: <dangerous> & \"quoted\"", List.of());
        SectionStyleProperties style = new SectionStyleProperties();
        section.setResolvedStyle(style);

        StringBuilder builder = new StringBuilder();
        generator.generate(section, styleSheet, builder, new ArrayList<>(), mockResolver, false);

        String result = builder.toString();
        assertTrue(result.contains("&lt;dangerous&gt;"));
        assertTrue(result.contains("&amp;"));
        assertTrue(result.contains("&quot;"));
    }

    @Test
    @DisplayName("should not generate alt-text when null")
    void shouldNotGenerateAltTextWhenNull() {
        Section section = new Section("test", null, null, List.of());
        SectionStyleProperties style = new SectionStyleProperties();
        section.setResolvedStyle(style);

        StringBuilder builder = new StringBuilder();
        generator.generate(section, styleSheet, builder, new ArrayList<>(), mockResolver, false);

        String result = builder.toString();
        assertFalse(result.contains("fox:alt-text"));
    }

    @Test
    @DisplayName("should not generate alt-text when empty string")
    void shouldNotGenerateAltTextWhenEmpty() {
        Section section = new Section("test", null, "", List.of());
        SectionStyleProperties style = new SectionStyleProperties();
        section.setResolvedStyle(style);

        StringBuilder builder = new StringBuilder();
        generator.generate(section, styleSheet, builder, new ArrayList<>(), mockResolver, false);

        String result = builder.toString();
        assertFalse(result.contains("fox:alt-text"));
    }

    @Test
    @DisplayName("should generate section marker inline element")
    void shouldGenerateSectionMarker() {
        Section section = new Section("test", null, null, List.of());
        SectionStyleProperties style = new SectionStyleProperties();
        style.setTextStyleName("test-marker-style");
        style.setSectionMarker("⚠️");
        section.setResolvedStyle(style);

        StringBuilder builder = new StringBuilder();
        generator.generate(section, styleSheet, builder, new ArrayList<>(), mockResolver, false);

        String result = builder.toString();
        assertTrue(result.contains("<fo:inline"));
        assertTrue(result.contains("⚠️"));
        assertTrue(result.contains("</fo:inline>"));
    }

    @Test
    @DisplayName("should not generate section marker when null")
    void shouldNotGenerateSectionMarkerWhenNull() {
        Section section = new Section("test", null, null, List.of());
        SectionStyleProperties style = new SectionStyleProperties();
        section.setResolvedStyle(style);

        StringBuilder builder = new StringBuilder();
        generator.generate(section, styleSheet, builder, new ArrayList<>(), mockResolver, false);

        String result = builder.toString();
        int inlineCount = result.split("<fo:inline").length - 1;
        assertEquals(0, inlineCount);
    }

    @Test
    @DisplayName("should generate keep-together attribute when true")
    void shouldGenerateKeepTogetherAttribute() {
        Section section = new Section("test", null, null, List.of());
        SectionStyleProperties style = new SectionStyleProperties();
        style.setKeepTogether(true);
        section.setResolvedStyle(style);

        StringBuilder builder = new StringBuilder();
        generator.generate(section, styleSheet, builder, new ArrayList<>(), mockResolver, false);

        String result = builder.toString();
        assertTrue(result.contains("keep-together.within-page=\"always\""));
    }

    @Test
    @DisplayName("should not generate keep-together when false")
    void shouldNotGenerateKeepTogetherWhenFalse() {
        Section section = new Section("test", null, null, List.of());
        SectionStyleProperties style = new SectionStyleProperties();
        style.setKeepTogether(false);
        section.setResolvedStyle(style);

        StringBuilder builder = new StringBuilder();
        generator.generate(section, styleSheet, builder, new ArrayList<>(), mockResolver, false);

        String result = builder.toString();
        assertFalse(result.contains("keep-together"));
    }

    @Test
    @DisplayName("should generate break-before attribute")
    void shouldGenerateBreakBeforeAttribute() {
        Section section = new Section("test", null, null, List.of());
        SectionStyleProperties style = new SectionStyleProperties();
        style.setBreakBefore(PageBreakVariant.PAGE);
        section.setResolvedStyle(style);

        StringBuilder builder = new StringBuilder();
        generator.generate(section, styleSheet, builder, new ArrayList<>(), mockResolver, false);

        String result = builder.toString();
        assertTrue(result.contains("break-before=\"page\""));
    }

//    @Test
//    @DisplayName("should not generate break-before when AUTO")
//    void shouldNotGenerateBreakBeforeWhenAuto() {
//        Section section = new Section("test", null, null, List.of());
//        SectionStyleProperties style = new SectionStyleProperties();
//        style.setBreakBefore(PageBreakVariant.AUTO);
//        section.setResolvedStyle(style);
//
//        StringBuilder builder = new StringBuilder();
//        generator.generate(section, styleSheet, builder, new ArrayList<>(), mockResolver, false);
//
//        String result = builder.toString();
//        assertFalse(result.contains("break-before"));
//    }

    @Test
    @DisplayName("should generate break-after attribute")
    void shouldGenerateBreakAfterAttribute() {
        Section section = new Section("test", null, null, List.of());
        SectionStyleProperties style = new SectionStyleProperties();
        style.setBreakAfter(PageBreakVariant.PAGE);
        section.setResolvedStyle(style);

        StringBuilder builder = new StringBuilder();
        generator.generate(section, styleSheet, builder, new ArrayList<>(), mockResolver, false);

        String result = builder.toString();
        assertTrue(result.contains("break-after=\"page\""));
    }

    @Test
    @DisplayName("should generate keep-with-next attribute when true")
    void shouldGenerateKeepWithNextAttribute() {
        Section section = new Section("test", null, null, List.of());
        SectionStyleProperties style = new SectionStyleProperties();
        style.setKeepWithNext(true);
        section.setResolvedStyle(style);

        StringBuilder builder = new StringBuilder();
        generator.generate(section, styleSheet, builder, new ArrayList<>(), mockResolver, false);

        String result = builder.toString();
        assertTrue(result.contains("keep-with-next.within-page=\"always\""));
    }

    @Test
    @DisplayName("should generate orphans attribute")
    void shouldGenerateOrphansAttribute() {
        Section section = new Section("test", null, null, List.of());
        SectionStyleProperties style = new SectionStyleProperties();
        style.setOrphans(2);
        section.setResolvedStyle(style);

        StringBuilder builder = new StringBuilder();
        generator.generate(section, styleSheet, builder, new ArrayList<>(), mockResolver, false);

        String result = builder.toString();
        assertTrue(result.contains("orphans=\"2\""));
    }

    @Test
    @DisplayName("should generate widows attribute")
    void shouldGenerateWidowsAttribute() {
        Section section = new Section("test", null, null, List.of());
        SectionStyleProperties style = new SectionStyleProperties();
        style.setWidows(3);
        section.setResolvedStyle(style);

        StringBuilder builder = new StringBuilder();
        generator.generate(section, styleSheet, builder, new ArrayList<>(), mockResolver, false);

        String result = builder.toString();
        assertTrue(result.contains("widows=\"3\""));
    }

    @Test
    @DisplayName("should generate all standard style attributes")
    void shouldGenerateStandardStyleAttributes() {
        Section section = new Section("test", null, null, List.of());
        SectionStyleProperties style = new SectionStyleProperties();
        style.setPadding("1cm");
        style.setBorder("1pt solid black");
        style.setBackgroundColor("#ffffff");
        style.setStartIndent("2cm");
        style.setEndIndent("2cm");
        style.setSpaceBefore("0.5cm");
        style.setSpaceAfter("0.5cm");
        section.setResolvedStyle(style);

        StringBuilder builder = new StringBuilder();
        generator.generate(section, styleSheet, builder, new ArrayList<>(), mockResolver, false);

        String result = builder.toString();
        assertTrue(result.contains("padding=\"1cm\""));
        assertTrue(result.contains("border=\"1pt solid black\""));
        assertTrue(result.contains("background-color=\"#ffffff\""));
        assertTrue(result.contains("start-indent=\"2cm\""));
        assertTrue(result.contains("end-indent=\"2cm\""));
        assertTrue(result.contains("space-before=\"0.5cm\""));
        assertTrue(result.contains("space-after=\"0.5cm\""));

    }

    @Test
    @DisplayName("should delegate child elements generation to main generator")
    void shouldDelegateChildElementsGeneration() {
        Paragraph child = new Paragraph("body", "Test");
        Section section = new Section("test", null, null, List.of(child));
        SectionStyleProperties style = new SectionStyleProperties();
        section.setResolvedStyle(style);

        StringBuilder builder = new StringBuilder();
        generator.generate(section, styleSheet, builder, new ArrayList<>(), mockResolver, false);

        verify(mockMainGenerator, times(1)).generateBlockElements(
                eq(section.getElements()),
                eq(styleSheet),
                any(StringBuilder.class),
                any(List.class),
                eq(mockResolver),
                eq(false)
        );
    }

    @Test
    @DisplayName("should handle null style gracefully")
    void shouldHandleNullStyle() {
        Section section = new Section("test", null, null, List.of());
        section.setResolvedStyle(null);

        StringBuilder builder = new StringBuilder();

        assertDoesNotThrow(() ->
                generator.generate(section, styleSheet, builder, new ArrayList<>(), mockResolver, false)
        );

        String result = builder.toString();
        assertTrue(result.contains("<fo:block"));
        assertTrue(result.contains("role=\"Sect\""));
    }

    @Test
    @DisplayName("should escape XML in style attributes")
    void shouldEscapeXmlInStyleAttributes() {
        Section section = new Section("test", null, null, List.of());
        SectionStyleProperties style = new SectionStyleProperties();
        style.setBackgroundColor("<color>");
        section.setResolvedStyle(style);

        StringBuilder builder = new StringBuilder();
        generator.generate(section, styleSheet, builder, new ArrayList<>(), mockResolver, false);

        String result = builder.toString();
        assertTrue(result.contains("&lt;color&gt;"));
    }
}