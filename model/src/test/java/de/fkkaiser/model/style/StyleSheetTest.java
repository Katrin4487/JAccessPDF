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
import org.junit.jupiter.api.Nested;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StyleSheet Tests")
class StyleSheetTest {

    // === TextStyle Finding Tests ===

    @Nested
    @DisplayName("findFontStyleByName()")
    class FindFontStyleByNameTests {

        @Test
        @DisplayName("Should find TextStyle by name when present")
        void shouldFindTextStyleByName() {
            TextStyle myStyle = new TextStyle("default-text-style",
                    "12px", "Arial", "400", "normal");
            StyleSheet styleSheet = new StyleSheet(
                    Collections.singletonList(myStyle), null, null, null);

            Optional<TextStyle> found = styleSheet.findFontStyleByName("default-text-style");

            assertTrue(found.isPresent(), "Should find a TextStyle if it is present");
            assertEquals("default-text-style", found.get().name());
        }

        @Test
        @DisplayName("Should return empty Optional when TextStyle not found")
        void shouldReturnEmptyWhenTextStyleNotFound() {
            TextStyle myStyle = new TextStyle("default-text-style",
                    "12px", "Arial", "400", "normal");
            StyleSheet styleSheet = new StyleSheet(
                    Collections.singletonList(myStyle), null, null, null);

            assertFalse(styleSheet.findFontStyleByName("non-existent-style").isPresent(),
                    "Should not find a TextStyle that is not present");
        }

        @Test
        @DisplayName("Should handle null TextStyle list")
        void shouldHandleNullTextStyleList() {
            StyleSheet styleSheet = new StyleSheet(null, null, null, null);

            assertFalse(styleSheet.findFontStyleByName("a-text-style").isPresent(),
                    "Should not find a TextStyle if textStyles list is null");
        }

        @Test
        @DisplayName("Should handle empty TextStyle list")
        void shouldHandleEmptyTextStyleList() {
            StyleSheet styleSheet = new StyleSheet(new ArrayList<>(), null, null, null);

            assertFalse(styleSheet.findFontStyleByName("a-text-style").isPresent(),
                    "Should not find a TextStyle if textStyles list is empty");
        }

        @Test
        @DisplayName("Should handle null name parameter")
        void shouldHandleNullName() {
            TextStyle myStyle = new TextStyle("default-text-style",
                    "12px", "Arial", "400", "normal");
            StyleSheet styleSheet = new StyleSheet(
                    Collections.singletonList(myStyle), null, null, null);

            assertFalse(styleSheet.findFontStyleByName(null).isPresent(),
                    "Should return empty Optional when name is null");
        }
    }

    // === ElementStyle Finding Tests ===

    @Nested
    @DisplayName("findElementStyleByName()")
    class FindElementStyleByNameTests {

        @Test
        @DisplayName("Should find ElementStyle by exact name")
        void shouldFindElementStyleByName() {
            TextStyle textStyle = new TextStyle("body-text", "11pt", "Arial", "400", "normal");
            ElementStyle elementStyle = ElementStyle.forParagraph("my-paragraph", textStyle);

            StyleSheet styleSheet = new StyleSheet(
                    Collections.singletonList(textStyle),
                    Collections.singletonList(elementStyle),
                    null,
                    null
            );

            Optional<ElementStyle> found = styleSheet.findElementStyleByName("my-paragraph");

            assertTrue(found.isPresent());
            assertEquals("my-paragraph", found.get().name());
        }

        @Test
        @DisplayName("Should return empty when ElementStyle not found")
        void shouldReturnEmptyWhenNotFound() {
            StyleSheet styleSheet = StyleSheet.builder().build();

            assertFalse(styleSheet.findElementStyleByName("non-existent").isPresent());
        }
    }

    @Nested
    @DisplayName("findElementStyle() with Defaults")
    class FindElementStyleWithDefaultsTests {

        @Test
        @DisplayName("Should find explicit style when provided")
        void shouldFindExplicitStyle() {
            TextStyle textStyle = new TextStyle("heading-text", "16pt", "Arial", "700", "normal");
            ElementStyle explicitStyle = ElementStyle.forHeadline("custom-heading", textStyle);

            StyleSheet styleSheet = new StyleSheet(
                    Collections.singletonList(textStyle),
                    Collections.singletonList(explicitStyle),
                    null,
                    null
            );

            Optional<ElementStyle> found = styleSheet.findElementStyle(
                    StandardElementType.H1,
                    "custom-heading"
            );

            assertTrue(found.isPresent());
            assertEquals("custom-heading", found.get().name());
        }

        @Test
        @DisplayName("Should find style via defaults dictionary")
        void shouldFindStyleViaDefaultsDictionary() {
            TextStyle textStyle = new TextStyle("heading-text", "16pt", "Arial", "700", "normal");
            ElementStyle h1Style = ElementStyle.forHeadline("corporate-h1", textStyle);

            DefaultStyles defaults = DefaultStyles.builder()
                    .set(StandardElementType.H1, "corporate-h1")
                    .build();

            StyleSheet styleSheet = new StyleSheet(
                    Collections.singletonList(textStyle),
                    Collections.singletonList(h1Style),
                    null,
                    defaults
            );

            Optional<ElementStyle> found = styleSheet.findElementStyle(StandardElementType.H1, null);

            assertTrue(found.isPresent());
            assertEquals("corporate-h1", found.get().name());
        }

        @Test
        @DisplayName("Should find style via naming convention")
        void shouldFindStyleViaNamingConvention() {
            TextStyle textStyle = new TextStyle("heading-text", "16pt", "Arial", "700", "normal");
            ElementStyle h1Style = ElementStyle.forHeadline("h1-default", textStyle);

            StyleSheet styleSheet = new StyleSheet(
                    Collections.singletonList(textStyle),
                    Collections.singletonList(h1Style),
                    null,
                    null  // No explicit defaults
            );

            Optional<ElementStyle> found = styleSheet.findElementStyle(StandardElementType.H1, null);

            assertTrue(found.isPresent());
            assertEquals("h1-default", found.get().name());
        }

        @Test
        @DisplayName("Should prioritize explicit style over defaults")
        void shouldPrioritizeExplicitStyleOverDefaults() {
            TextStyle textStyle = new TextStyle("heading-text", "16pt", "Arial", "700", "normal");
            ElementStyle explicitStyle = ElementStyle.forHeadline("custom-h1", textStyle);
            ElementStyle defaultStyle = ElementStyle.forHeadline("h1-default", textStyle);

            DefaultStyles defaults = DefaultStyles.builder()
                    .set(StandardElementType.H1, "h1-default")
                    .build();

            StyleSheet styleSheet = new StyleSheet(
                    Collections.singletonList(textStyle),
                    List.of(explicitStyle, defaultStyle),
                    null,
                    defaults
            );

            // Explicit style should be found
            Optional<ElementStyle> found = styleSheet.findElementStyle(
                    StandardElementType.H1,
                    "custom-h1"
            );

            assertTrue(found.isPresent());
            assertEquals("custom-h1", found.get().name());
        }

        @Test
        @DisplayName("Should prioritize defaults dictionary over naming convention")
        void shouldPrioritizeDefaultsDictionaryOverNamingConvention() {
            TextStyle textStyle = new TextStyle("heading-text", "16pt", "Arial", "700", "normal");
            ElementStyle dictionaryStyle = ElementStyle.forHeadline("corporate-h1", textStyle);
            ElementStyle conventionStyle = ElementStyle.forHeadline("h1-default", textStyle);

            DefaultStyles defaults = DefaultStyles.builder()
                    .set(StandardElementType.H1, "corporate-h1")
                    .build();

            StyleSheet styleSheet = new StyleSheet(
                    Collections.singletonList(textStyle),
                    List.of(dictionaryStyle, conventionStyle),
                    null,
                    defaults
            );

            Optional<ElementStyle> found = styleSheet.findElementStyle(StandardElementType.H1, null);

            assertTrue(found.isPresent());
            assertEquals("corporate-h1", found.get().name());
        }

        @Test
        @DisplayName("Should return empty when no style found")
        void shouldReturnEmptyWhenNoStyleFound() {
            StyleSheet styleSheet = StyleSheet.builder().build();

            Optional<ElementStyle> found = styleSheet.findElementStyle(StandardElementType.H1, null);

            assertFalse(found.isPresent());
        }

        @Test
        @DisplayName("Should handle null defaults gracefully")
        void shouldHandleNullDefaults() {
            StyleSheet styleSheet = new StyleSheet(null, null, null, null);

            Optional<ElementStyle> found = styleSheet.findElementStyle(StandardElementType.H1, null);

            assertFalse(found.isPresent());
        }
    }

    // === StyleSheetBuilder Tests ===

    @Nested
    @DisplayName("StyleSheetBuilder")
    class StyleSheetBuilderTests {

        @Test
        @DisplayName("Should build StyleSheet with empty lists by default")
        void shouldBuildWithEmptyLists() {
            StyleSheet styleSheet = StyleSheet.builder().build();

            assertTrue(styleSheet.textStyles().isEmpty(),
                    "Default builder should have empty textStyles list");
            assertTrue(styleSheet.elementStyles().isEmpty(),
                    "Default builder should have empty elementStyles list");
            assertTrue(styleSheet.pageMasterStyles().isEmpty(),
                    "Default builder should have empty pageMasterStyles list");
            assertNull(styleSheet.defaults(),
                    "Default builder should have null defaults");
        }

        @Test
        @DisplayName("Should add TextStyles via builder")
        void shouldAddTextStyles() {
            TextStyle style = new TextStyle("body-text", "11pt", "Arial", "400", "normal");

            StyleSheet styleSheet = StyleSheet.builder()
                    .addTextStyle(style)
                    .build();

            assertEquals(1, styleSheet.textStyles().size());
            assertEquals("body-text", styleSheet.textStyles().get(0).name());
        }

        @Test
        @DisplayName("Should add ElementStyles via builder")
        void shouldAddElementStyles() {
            TextStyle textStyle = new TextStyle("body-text", "11pt", "Arial", "400", "normal");
            ElementStyle elementStyle = ElementStyle.forParagraph("my-paragraph", textStyle);

            StyleSheet styleSheet = StyleSheet.builder()
                    .addElementStyle(elementStyle)
                    .build();

            assertEquals(1, styleSheet.elementStyles().size());
            assertEquals("my-paragraph", styleSheet.elementStyles().get(0).name());
        }

        @Test
        @DisplayName("Should add PageMasterStyles via builder")
        void shouldAddPageMasterStyles() {
            StyleSheet styleSheet = StyleSheet.builder()
                    .addPageMasterStyle(new PageMasterStyle())
                    .build();

            assertEquals(1, styleSheet.pageMasterStyles().size());
        }

        @Test
        @DisplayName("Should set defaults via builder")
        void shouldSetDefaults() {
            DefaultStyles defaults = DefaultStyles.builder()
                    .set(StandardElementType.H1, "h1-style")
                    .set(StandardElementType.P, "p-style")
                    .build();

            StyleSheet styleSheet = StyleSheet.builder()
                    .withDefaults(defaults)
                    .build();

            assertNotNull(styleSheet.defaults());
            assertTrue(styleSheet.defaults().get(StandardElementType.H1).isPresent());
            assertEquals("h1-style", styleSheet.defaults().get(StandardElementType.H1).get());
        }

        @Test
        @DisplayName("Should set single default via builder")
        void shouldSetSingleDefault() {
            StyleSheet styleSheet = StyleSheet.builder()
                    .setDefault(StandardElementType.H1, "h1-style")
                    .build();

            assertNotNull(styleSheet.defaults());
            assertTrue(styleSheet.defaults().get(StandardElementType.H1).isPresent());
            assertEquals("h1-style", styleSheet.defaults().get(StandardElementType.H1).get());
        }

        @Test
        @DisplayName("Should handle null inputs gracefully")
        void shouldHandleNullInputs() {
            StyleSheet styleSheet = StyleSheet.builder()
                    .addTextStyle(null)
                    .addElementStyle(null)
                    .addPageMasterStyle(null)
                    .build();

            assertEquals(0, styleSheet.textStyles().size());
            assertEquals(0, styleSheet.elementStyles().size());
            assertEquals(0, styleSheet.pageMasterStyles().size());
        }

        @Test
        @DisplayName("Should build complete StyleSheet with all components")
        void shouldBuildCompleteStyleSheet() {
            TextStyle textStyle = new TextStyle("body-text", "11pt", "Arial", "400", "normal");
            ElementStyle paragraphStyle = ElementStyle.forParagraph("p-default", textStyle);
            ElementStyle h1Style = ElementStyle.forHeadline("h1-default", textStyle);
            PageMasterStyle pageMaster = new PageMasterStyle();

            DefaultStyles defaults = DefaultStyles.builder()
                    .set(StandardElementType.H1, "h1-default")
                    .set(StandardElementType.P, "p-default")
                    .build();

            StyleSheet styleSheet = StyleSheet.builder()
                    .addTextStyle(textStyle)
                    .addElementStyle(paragraphStyle)
                    .addElementStyle(h1Style)
                    .addPageMasterStyle(pageMaster)
                    .withDefaults(defaults)
                    .build();

            assertEquals(1, styleSheet.textStyles().size());
            assertEquals(2, styleSheet.elementStyles().size());
            assertEquals(1, styleSheet.pageMasterStyles().size());
            assertNotNull(styleSheet.defaults());
            assertEquals(2, styleSheet.defaults().mappings().size());
        }

        @Test
        @DisplayName("Should chain multiple setDefault calls")
        void shouldChainMultipleSetDefaultCalls() {
            StyleSheet styleSheet = StyleSheet.builder()
                    .setDefault(StandardElementType.H1, "h1-style")
                    .setDefault(StandardElementType.H2, "h2-style")
                    .setDefault(StandardElementType.P, "p-style")
                    .build();

            assertNotNull(styleSheet.defaults());
            assertEquals(3, styleSheet.defaults().mappings().size());
            assertEquals("h1-style", styleSheet.defaults().get(StandardElementType.H1).get());
            assertEquals("h2-style", styleSheet.defaults().get(StandardElementType.H2).get());
            assertEquals("p-style", styleSheet.defaults().get(StandardElementType.P).get());
        }
    }

    // === Integration Tests ===

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should resolve complete style hierarchy")
        void shouldResolveCompleteStyleHierarchy() {
            // Setup: Create a complete StyleSheet with defaults
            TextStyle bodyText = new TextStyle("body-text", "11pt", "Arial", "400", "normal");
            TextStyle headingText = new TextStyle("heading-text", "16pt", "Arial", "700", "normal");

            ElementStyle pDefault = ElementStyle.forParagraph("p-default", bodyText);
            ElementStyle h1Default = ElementStyle.forHeadline("h1-default", headingText);
            ElementStyle h1Custom = ElementStyle.forHeadline("h1-custom", headingText);

            DefaultStyles defaults = DefaultStyles.builder()
                    .set(StandardElementType.P, "p-default")
                    .set(StandardElementType.H1, "h1-default")
                    .build();

            StyleSheet styleSheet = StyleSheet.builder()
                    .addTextStyle(bodyText)
                    .addTextStyle(headingText)
                    .addElementStyle(pDefault)
                    .addElementStyle(h1Default)
                    .addElementStyle(h1Custom)
                    .withDefaults(defaults)
                    .build();

            // Test: Resolve via explicit name
            Optional<ElementStyle> explicitH1 = styleSheet.findElementStyle(
                    StandardElementType.H1, "h1-custom");
            assertTrue(explicitH1.isPresent());
            assertEquals("h1-custom", explicitH1.get().name());

            // Test: Resolve via defaults
            Optional<ElementStyle> defaultH1 = styleSheet.findElementStyle(
                    StandardElementType.H1, null);
            assertTrue(defaultH1.isPresent());
            assertEquals("h1-default", defaultH1.get().name());

            // Test: Resolve paragraph
            Optional<ElementStyle> defaultP = styleSheet.findElementStyle(
                    StandardElementType.P, null);
            assertTrue(defaultP.isPresent());
            assertEquals("p-default", defaultP.get().name());
        }
    }
}