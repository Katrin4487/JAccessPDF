package de.kaiser.api;

import de.kaiser.model.style.StyleSheet;
import de.kaiser.model.style.TextStyle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SimpleStyleManager Tests")
class SimpleStyleManagerTest {

    private SimpleStyleManager styleManager;

    @BeforeEach
    void setUp() {
        styleManager = new SimpleStyleManager();
    }

    @Test
    @DisplayName("Should return default page master name")
    void shouldReturnDefaultPageMasterName() {
        // When
        String name = styleManager.getDefaultPageMasterName();

        // Then
        assertNotNull(name);
        assertFalse(name.isEmpty());
    }

    @Test
    @DisplayName("Should map default paragraph style name")
    void shouldMapDefaultParagraphStyleName() {
        // When
        String styleName = styleManager.getParagraphStyleName("default");

        // Then
        assertEquals("paragraph-default", styleName);
    }

    @Test
    @DisplayName("Should map custom paragraph style name")
    void shouldMapCustomParagraphStyleName() {
        // When
        String styleName = styleManager.getParagraphStyleName("custom");

        // Then
        assertEquals("paragraph-custom", styleName);
    }

    @Test
    @DisplayName("Should map heading style names for all levels")
    void shouldMapHeadingStyleNamesForAllLevels() {
        // When & Then
        for (int level = 1; level <= 6; level++) {
            String styleName = styleManager.getHeadingStyleName(level);
            assertEquals("heading-" + level, styleName);
        }
    }

    @Test
    @DisplayName("Should set and use custom language")
    void shouldSetAndUseCustomLanguage() {
        // When
        styleManager.setLanguage("en-US");

        // Then
        // Language is used internally, can't directly test without building StyleSheet
        // But we verify no exception is thrown
        assertDoesNotThrow(() -> styleManager.setLanguage("fr-FR"));
    }

    @Test
    @DisplayName("Should set and use custom font family")
    void shouldSetAndUseCustomFontFamily() {
        // When
        styleManager.setFontFamily("Roboto");

        // Then
        assertDoesNotThrow(() -> styleManager.setFontFamily("Arial"));
    }

    @Test
    @DisplayName("Should build valid StyleSheet")
    void shouldBuildValidStyleSheet() {
        // When
        StyleSheet styleSheet = styleManager.buildStyleSheet();

        // Then
        assertNotNull(styleSheet);
        assertNotNull(styleSheet.textStyles());
        assertNotNull(styleSheet.elementStyles());
        assertNotNull(styleSheet.pageMasterStyles());
    }

    @Test
    @DisplayName("Should include default text style in StyleSheet")
    void shouldIncludeDefaultTextStyleInStyleSheet() {
        // When
        StyleSheet styleSheet = styleManager.buildStyleSheet();

        // Then
        assertTrue(
                styleSheet.textStyles().stream()
                        .anyMatch(ts -> "text-default".equals(ts.name())),
                "Should contain default text style"
        );
    }

    @Test
    @DisplayName("Should include all heading text styles in StyleSheet")
    void shouldIncludeAllHeadingTextStylesInStyleSheet() {
        // When
        StyleSheet styleSheet = styleManager.buildStyleSheet();

        // Then
        for (int level = 1; level <= 6; level++) {
            final int currentLevel = level;
            assertTrue(
                    styleSheet.textStyles().stream()
                            .anyMatch(ts -> ("text-heading-" + currentLevel).equals(ts.name())),
                    "Should contain heading style for level " + level
            );
        }
    }

    @Test
    @DisplayName("Should include default paragraph element style")
    void shouldIncludeDefaultParagraphElementStyle() {
        // When
        StyleSheet styleSheet = styleManager.buildStyleSheet();

        // Then
        assertTrue(
                styleSheet.elementStyles().stream()
                        .anyMatch(es -> "paragraph-default".equals(es.name())),
                "Should contain default paragraph element style"
        );
    }

    @Test
    @DisplayName("Should include all heading element styles")
    void shouldIncludeAllHeadingElementStyles() {
        // When
        StyleSheet styleSheet = styleManager.buildStyleSheet();

        // Then
        for (int level = 1; level <= 6; level++) {
            final int currentLevel = level;
            assertTrue(
                    styleSheet.elementStyles().stream()
                            .anyMatch(es -> ("heading-" + currentLevel).equals(es.name())),
                    "Should contain heading element style for level " + level
            );
        }
    }

    @Test
    @DisplayName("Should include default page master style")
    void shouldIncludeDefaultPageMasterStyle() {
        // When
        StyleSheet styleSheet = styleManager.buildStyleSheet();

        // Then
        assertFalse(styleSheet.pageMasterStyles().isEmpty());
        assertEquals(
                styleManager.getDefaultPageMasterName(),
                styleSheet.pageMasterStyles().getFirst().getName()
        );
    }

    @Test
    @DisplayName("Should create heading styles with decreasing font sizes")
    void shouldCreateHeadingStylesWithDecreasingFontSizes() {
        // When
        StyleSheet styleSheet = styleManager.buildStyleSheet();

        // Then
        TextStyle h1 = findTextStyle(styleSheet, "text-heading-1");
        TextStyle h2 = findTextStyle(styleSheet, "text-heading-2");
        TextStyle h6 = findTextStyle(styleSheet, "text-heading-6");

        assertNotNull(h1);
        assertNotNull(h2);
        assertNotNull(h6);

        // Extract font sizes (e.g., "24px" -> 24)
        int size1 = extractFontSize(h1.fontSize());
        int size2 = extractFontSize(h2.fontSize());
        int size6 = extractFontSize(h6.fontSize());

        assertTrue(size1 > size2, "H1 should be larger than H2");
        assertTrue(size2 > size6, "H2 should be larger than H6");
    }

    @Test
    @DisplayName("Should create bold heading styles")
    void shouldCreateBoldHeadingStyles() {
        // When
        StyleSheet styleSheet = styleManager.buildStyleSheet();

        // Then
        for (int level = 1; level <= 6; level++) {
            TextStyle heading = findTextStyle(styleSheet, "text-heading-" + level);
            assertNotNull(heading);
            assertEquals("700", heading.fontWeight(), "Headings should be bold");
        }
    }

    @Test
    @DisplayName("Should use consistent font family")
    void shouldUseConsistentFontFamily() {
        // When
        StyleSheet styleSheet = styleManager.buildStyleSheet();

        // Then
        String expectedFont = "Open Sans"; // Default embedded font

        for (TextStyle textStyle : styleSheet.textStyles()) {
            assertEquals(expectedFont, textStyle.getFontFamilyName(),
                    "All styles should use same font family: " + textStyle.name());
        }
    }

    @Test
    @DisplayName("Should use custom font family when set")
    void shouldUseCustomFontFamilyWhenSet() {
        // Given
        styleManager.setFontFamily("Roboto");

        // When
        StyleSheet styleSheet = styleManager.buildStyleSheet();

        // Then
        for (TextStyle textStyle : styleSheet.textStyles()) {
            assertEquals("Roboto", textStyle.getFontFamilyName(),
                    "All styles should use custom font: " + textStyle.name());
        }
    }

    // Helper methods

    private TextStyle findTextStyle(StyleSheet sheet, String name) {
        return sheet.textStyles().stream()
                .filter(ts -> name.equals(ts.name()))
                .findFirst()
                .orElse(null);
    }

    private int extractFontSize(String fontSize) {
        // "24px" -> 24
        return Integer.parseInt(fontSize.replace("px", ""));
    }
}