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
package de.fkkaiser.generator;

import de.fkkaiser.model.structure.*;
import de.fkkaiser.model.style.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit and integration tests for the XslFoGenerator class.
 * This class tests the correct orchestration of the XSL-FO generation.
 */
class XslFoGeneratorTest {

    private XslFoGenerator xslFoGenerator;
    private Document testDocument;
    private StyleSheet testStyleSheet;

    // Helper method to create clean test data before each test
    @BeforeEach
    void setUp() {
        // Initialize the generator to be tested
        xslFoGenerator = new XslFoGenerator();

        TextStyle textStyle = new TextStyle("Default Style","10pt","0pen Sans","400","normal");
        testStyleSheet = new StyleSheet(List.of(textStyle), Collections.emptyList(), Collections.emptyList());

        Metadata metadata = Metadata.builder("A Title").author("An author").build();

        List<InlineElement> elements = new ArrayList<>();
        elements.add(new TextRun("Hello World!", "default-text",null));
        Paragraph paragraph = new Paragraph("default-paragraph",elements);



        // Create a headline
        List<InlineElement> headlineElements = new ArrayList<>();
        headlineElements.add(new TextRun("Chapter 1", "default-text", null));
        Headline headline = new Headline("default-headline",headlineElements, null,1);

        // Create the body of the page
        ContentArea body = new ContentArea(List.of(headline,paragraph));

        // Create header and footer

        List<InlineElement> headerElements = new ArrayList<>();
        headerElements.add(new PageNumber());
        Paragraph headerParagraph = new Paragraph("header-style",headerElements,null);

        ContentArea header = new ContentArea(List.of(headerParagraph));


        List<InlineElement> footerElements = new ArrayList<>();
        footerElements.add(new TextRun("Copyright © 2025", "default-text",null));
        Paragraph footerParagraph = new Paragraph("footer-style",footerElements);
        ContentArea footer = new ContentArea(List.of(footerParagraph));

        // Create a Page Sequence that combines everything
        PageSequence pageSequence = new PageSequence("default", body, header, footer);

        // Create the final document
        testDocument = new Document(null,metadata, List.of(pageSequence));
    }

    @Test
    @DisplayName("Should return an empty string when the document is null")
    void generate_withNullDocument_shouldReturnEmptyString() {
        String result = xslFoGenerator.generate(null, testStyleSheet,null);
        assertEquals("", result, "An empty result was expected for a null document.");
    }

    @Test
    @DisplayName("Should return an empty string when the stylesheet is null")
    void generate_withNullStyleSheet_shouldReturnEmptyString() {
        String result = xslFoGenerator.generate(testDocument, null,null);
        assertEquals("", result, "An empty result was expected for a null stylesheet.");
    }

    @Test
    @DisplayName("Should generate a valid basic XSL-FO structure")
    void generate_withValidInputs_shouldProduceRootElement() {
        String result = xslFoGenerator.generate(testDocument, testStyleSheet,null);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"), "XML declaration is missing.");
        assertTrue(result.contains("<fo:root xmlns:fo=\"http://www.w3.org/1999/XSL/Format\""), "fo:root element is missing or incorrect.");
        assertTrue(result.endsWith("</fo:root>"), "fo:root element is not closed correctly.");
    }

    @Test
    @DisplayName("Should generate metadata (Declarations) correctly")
    void generate_withValidInputs_shouldGenerateDeclarations() {
        String result = xslFoGenerator.generate(testDocument, testStyleSheet,null);

        assertTrue(result.contains("<fo:declarations>"), "fo:declarations block is missing.");
        assertTrue(result.contains("<dc:title>A Title</dc:title>"), "Title in metadata is missing.");
    }

    // ToDo


    @Test
    @DisplayName("Should generate bookmarks for headlines and replace the placeholder")
    void generate_withHeadlines_shouldGenerateBookmarks() {
        String result = xslFoGenerator.generate(testDocument, testStyleSheet,null);

        // Checks if the placeholder is gone and bookmark code has been inserted
        assertFalse(result.contains("<§§BOOKMARK_TREE§§>"), "The bookmark placeholder was not replaced.");
        assertTrue(result.contains("<fo:bookmark-tree>"), "The fo:bookmark-tree is missing.");
        assertTrue(result.contains("<fo:bookmark-title>Chapter 1</fo:bookmark-title>"), "The title of the bookmark is missing.");
    }
}