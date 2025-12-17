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

import de.fkkaiser.model.structure.Hyperlink;
import de.fkkaiser.model.style.StyleSheet;
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
        styleSheet = new StyleSheet(List.of(), List.of(), List.of(),null);
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