package de.kaiser.generator.element;

import de.kaiser.model.structure.Hyperlink;
import de.kaiser.model.style.StyleSheet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HyperlinkFoGeneratorTest {

    private HyperlinkFoGenerator generator;
    private StyleSheet styleSheet;

    @BeforeEach
    void setUp() {
        generator = new HyperlinkFoGenerator();
        styleSheet = new StyleSheet(List.of(), List.of(), List.of());
    }

    @Test
    @DisplayName("Should generate a simple hyperlink.")
    void shouldGenerateBasicLInk() {
        // 1. Arrange: Testdaten vorbereiten
        Hyperlink link = new Hyperlink(
                "Besuchen Sie unsere Webseite",
                null,
                "https://www.example.de",
                null
        );
        StringBuilder builder = new StringBuilder();
        String expectedXml = "<fo:basic-link external-destination=\"https://www.example.de\"" +
                " fox:alt-text=\"Besuchen Sie unsere Webseite\">" +
                "Besuchen Sie unsere Webseite" +
                "</fo:basic-link>";

        generator.generate(link, styleSheet, builder);
        assertEquals(expectedXml, builder.toString(), "the fo string is not correct");
    }


    @Test
    @DisplayName("Should return empty Strings correctly")
    void testGenerateWithEmptyValues() {
        Hyperlink link = new Hyperlink("", "", "");
        StringBuilder builder = new StringBuilder();
        String expectedXml = "<fo:basic-link external-destination=\"\"" +
                " fox:alt-text=\"\">" +
                "</fo:basic-link>";

        generator.generate(link, styleSheet, builder);

        assertEquals(expectedXml, builder.toString(), "Empty strings are not correct");
    }
}