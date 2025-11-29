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
package de.fkkaiser.api.simplelayer;

import de.fkkaiser.api.utils.EResourceProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SimpleDocument API Tests")
class SimpleDocumentTest {

    @Test
    @DisplayName("Should create SimpleDocument with valid title")
    void shouldCreateSimpleDocumentWithValidTitle() {
        // When
        SimpleDocumentBuilder doc = SimpleDocumentBuilder.create("Test Document");

        // Then
        assertNotNull(doc);
    }

    @Test
    @DisplayName("Should throw exception when title is null")
    void shouldThrowExceptionWhenTitleIsNull() {
        // When & Then
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> SimpleDocumentBuilder.create(null)
        );

        assertTrue(exception.getMessage().contains("null"));
    }

    @Test
    @DisplayName("Should throw exception when title is empty")
    void shouldThrowExceptionWhenTitleIsEmpty() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> SimpleDocumentBuilder.create("   ")
        );

        assertTrue(exception.getMessage().contains("required"));
    }

    @Test
    @DisplayName("Should add paragraph with default style")
    void shouldAddParagraphWithDefaultStyle() {
        // Given
        SimpleDocumentBuilder doc = SimpleDocumentBuilder.create("Test");

        // When
        SimpleDocumentBuilder result = doc.addParagraph("Hello World");

        // Then
        assertSame(doc, result, "Should return same instance for chaining");
    }

    @Test
    @DisplayName("Should throw exception when adding null paragraph")
    void shouldThrowExceptionWhenAddingNullParagraph() {
        // Given
        SimpleDocumentBuilder doc = SimpleDocumentBuilder.create("Test");

        // When & Then
        assertThrows(
                NullPointerException.class,
                () -> doc.addParagraph(null)
        );
    }

    @Test
    @DisplayName("Should add paragraph with custom style")
    void shouldAddParagraphWithCustomStyle() {
        // Given
        SimpleDocumentBuilder doc = SimpleDocumentBuilder.create("Test");

        // When
        SimpleDocumentBuilder result = doc.addParagraph("Text", "custom-style");

        // Then
        assertSame(doc, result);
    }

    @Test
    @DisplayName("Should add heading with valid level")
    void shouldAddHeadingWithValidLevel() {
        // Given
        SimpleDocumentBuilder doc = SimpleDocumentBuilder.create("Test");

        // When
        SimpleDocumentBuilder result = doc.addHeading("Heading", 2);

        // Then
        assertSame(doc, result);
    }

    @Test
    @DisplayName("Should add heading level 1 by default")
    void shouldAddHeadingLevel1ByDefault() {
        // Given
        SimpleDocumentBuilder doc = SimpleDocumentBuilder.create("Test");

        // When
        SimpleDocumentBuilder result = doc.addHeading("Main Heading");

        // Then
        assertSame(doc, result);
    }

    @Test
    @DisplayName("Should throw exception for invalid heading level - too low")
    void shouldThrowExceptionForHeadingLevelTooLow() {
        // Given
        SimpleDocumentBuilder doc = SimpleDocumentBuilder.create("Test");

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> doc.addHeading("Invalid", 0)
        );

        assertTrue(exception.getMessage().contains("between 1 and 6"));
    }

    @Test
    @DisplayName("Should throw exception for invalid heading level - too high")
    void shouldThrowExceptionForHeadingLevelTooHigh() {
        // Given
        SimpleDocumentBuilder doc = SimpleDocumentBuilder.create("Test");

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> doc.addHeading("Invalid", 7)
        );

        assertTrue(exception.getMessage().contains("between 1 and 6"));
    }

    @Test
    @DisplayName("Should set custom resource provider")
    void shouldSetCustomResourceProvider() {
        // Given
        SimpleDocumentBuilder doc = SimpleDocumentBuilder.create("Test");
        EResourceProvider mockProvider = path -> null; // Simple mock

        // When
        SimpleDocumentBuilder result = doc.withResourceProvider(mockProvider);

        // Then
        assertSame(doc, result);
    }

    @Test
    @DisplayName("Should set language code")
    void shouldSetLanguageCode() {
        // Given
        SimpleDocumentBuilder doc = SimpleDocumentBuilder.create("Test");

        // When
        SimpleDocumentBuilder result = doc.withLanguage("en-US");

        // Then
        assertSame(doc, result);
    }

    @Test
    @DisplayName("Should allow method chaining")
    void shouldAllowMethodChaining() {
        // When
        SimpleDocumentBuilder doc = SimpleDocumentBuilder.create("Test")
                .withLanguage("de-DE")
                .addHeading("Title")
                .addParagraph("Text 1")
                .addParagraph("Text 2")
                .addHeading("Section", 2);

        // Then
        assertNotNull(doc);
    }


    @Test
    @DisplayName("Should generate PDF stream after build")
    void shouldGeneratePdfStreamAfterBuild() throws Exception {
        // Given
        SimpleDocument doc = SimpleDocumentBuilder.create("Test Document")
                .addParagraph("Hello World")
                .build();

        // When
        ByteArrayOutputStream stream = doc.toStream();

        // Then
        assertNotNull(stream);
        assertTrue(stream.size() > 0, "PDF stream should not be empty");

        // Check PDF magic number
        byte[] bytes = stream.toByteArray();
        assertTrue(bytes.length > 4);
        assertEquals('%', bytes[0]);
        assertEquals('P', bytes[1]);
        assertEquals('D', bytes[2]);
        assertEquals('F', bytes[3]);
    }

    @Test
    @DisplayName("Should save PDF to file after build")
    void shouldSavePdfToFileAfterBuild(@TempDir Path tempDir) throws Exception {
        // Given
        SimpleDocument doc = SimpleDocumentBuilder.create("Test Document")
                .addHeading("My Heading")
                .addParagraph("Hello World")
                .build();

        Path outputPath = tempDir.resolve("output.pdf");

        // When
        doc.saveAs(outputPath.toString());

        // Then
        assertTrue(Files.exists(outputPath), "Output file should exist");
        assertTrue(Files.size(outputPath) > 0, "File should not be empty");

        // Verify PDF signature
        byte[] bytes = Files.readAllBytes(outputPath);
        assertEquals('%', bytes[0]);
        assertEquals('P', bytes[1]);
        assertEquals('D', bytes[2]);
        assertEquals('F', bytes[3]);
    }

    @Test
    @DisplayName("Should create minimal hello world document")
    void shouldCreateMinimalHelloWorldDocument(@TempDir Path tempDir) throws Exception {
        // Given
        Path outputPath = tempDir.resolve("hello.pdf");

        // When
        SimpleDocumentBuilder.create("Hello World PDF")
                .addParagraph("Hello World")
                .build()
                .saveAs(outputPath.toString());

        // Then
        assertTrue(Files.exists(outputPath));
        assertTrue(Files.size(outputPath) > 100, "PDF should have reasonable size");
    }

    @Test
    @DisplayName("Should create document with multiple elements")
    void shouldCreateDocumentWithMultipleElements(@TempDir Path tempDir) throws Exception {
        // Given
        Path outputPath = tempDir.resolve("multi-element.pdf");

        // When
        SimpleDocumentBuilder.create("Multi-Element Document")
                .addHeading("Main Title", 1)
                .addParagraph("Introduction paragraph")
                .addHeading("Section 1", 2)
                .addParagraph("Content for section 1")
                .addHeading("Section 2", 2)
                .addParagraph("Content for section 2")
                .addHeading("Subsection", 3)
                .addParagraph("Detailed content")
                .build()
                .saveAs(outputPath.toString());

        // Then
        assertTrue(Files.exists(outputPath));
        long fileSize = Files.size(outputPath);
        assertTrue(fileSize > 500, "PDF with multiple elements should be larger");
    }

    @Test
    @DisplayName("Should handle special characters in text")
    void shouldHandleSpecialCharactersInText(@TempDir Path tempDir) throws Exception {
        // Given
        Path outputPath = tempDir.resolve("special-chars.pdf");
        String specialText = "Äöü ß €@#$%&* \"quotes\" 'apostrophe'";

        // When
        SimpleDocumentBuilder.create("Special Characters Test")
                .addParagraph(specialText)
                .build()
                .saveAs(outputPath.toString());

        // Then
        assertTrue(Files.exists(outputPath));
    }

//    @Test
//    @DisplayName("Should handle empty document")
//    void shouldHandleEmptyDocument(@TempDir Path tempDir) throws Exception {
//        // Given
//        Path outputPath = tempDir.resolve("empty.pdf");
//
//        // When
//        SimpleDocument.create("Empty Document")
//                .build()
//                .saveAs(outputPath.toString());
//
//        // Then
//        assertTrue(Files.exists(outputPath));
//        assertTrue(Files.size(outputPath) > 0, "Even empty doc should produce valid PDF");
//    }

    @Test
    @DisplayName("Should handle very long text")
    void shouldHandleVeryLongText(@TempDir Path tempDir) throws Exception {
        // Given
        Path outputPath = tempDir.resolve("long-text.pdf");
        String longText = "Lorem ipsum dolor sit amet. ".repeat(100);

        // When
        SimpleDocumentBuilder.create("Long Text Document")
                .addParagraph(longText)
                .build()
                .saveAs(outputPath.toString());

        // Then
        assertTrue(Files.exists(outputPath));
        assertTrue(Files.size(outputPath) > 1000, "Long text should produce larger PDF");
    }

    @Test
    @DisplayName("Should allow building same document multiple times")
    void shouldAllowBuildingSameDocumentMultipleTimes() throws Exception {
        // Given
        SimpleDocumentBuilder docBuild = SimpleDocumentBuilder.create("Test")
                .addParagraph("Hello");

        // When
        SimpleDocument doc =docBuild.build();
        ByteArrayOutputStream stream1 = doc.toStream();

        // Build again
        docBuild.build();
        ByteArrayOutputStream stream2 = doc.toStream();

        // Then
        assertNotNull(stream1);
        assertNotNull(stream2);
        assertEquals(stream1.size(), stream2.size(), "Both streams should be identical");
    }
}