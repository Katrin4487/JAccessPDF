package de.kaiser.api;

import de.kaiser.api.utils.EResourceProvider;
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
        SimpleDocument doc = SimpleDocument.create("Test Document");

        // Then
        assertNotNull(doc);
    }

    @Test
    @DisplayName("Should throw exception when title is null")
    void shouldThrowExceptionWhenTitleIsNull() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> SimpleDocument.create(null)
        );

        assertTrue(exception.getMessage().contains("required"));
    }

    @Test
    @DisplayName("Should throw exception when title is empty")
    void shouldThrowExceptionWhenTitleIsEmpty() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> SimpleDocument.create("   ")
        );

        assertTrue(exception.getMessage().contains("required"));
    }

    @Test
    @DisplayName("Should add paragraph with default style")
    void shouldAddParagraphWithDefaultStyle() {
        // Given
        SimpleDocument doc = SimpleDocument.create("Test");

        // When
        SimpleDocument result = doc.addParagraph("Hello World");

        // Then
        assertSame(doc, result, "Should return same instance for chaining");
    }

    @Test
    @DisplayName("Should throw exception when adding null paragraph")
    void shouldThrowExceptionWhenAddingNullParagraph() {
        // Given
        SimpleDocument doc = SimpleDocument.create("Test");

        // When & Then
        assertThrows(
                IllegalArgumentException.class,
                () -> doc.addParagraph(null)
        );
    }

    @Test
    @DisplayName("Should add paragraph with custom style")
    void shouldAddParagraphWithCustomStyle() {
        // Given
        SimpleDocument doc = SimpleDocument.create("Test");

        // When
        SimpleDocument result = doc.addParagraph("Text", "custom-style");

        // Then
        assertSame(doc, result);
    }

    @Test
    @DisplayName("Should add heading with valid level")
    void shouldAddHeadingWithValidLevel() {
        // Given
        SimpleDocument doc = SimpleDocument.create("Test");

        // When
        SimpleDocument result = doc.addHeading("Heading", 2);

        // Then
        assertSame(doc, result);
    }

    @Test
    @DisplayName("Should add heading level 1 by default")
    void shouldAddHeadingLevel1ByDefault() {
        // Given
        SimpleDocument doc = SimpleDocument.create("Test");

        // When
        SimpleDocument result = doc.addHeading("Main Heading");

        // Then
        assertSame(doc, result);
    }

    @Test
    @DisplayName("Should throw exception for invalid heading level - too low")
    void shouldThrowExceptionForHeadingLevelTooLow() {
        // Given
        SimpleDocument doc = SimpleDocument.create("Test");

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
        SimpleDocument doc = SimpleDocument.create("Test");

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
        SimpleDocument doc = SimpleDocument.create("Test");
        EResourceProvider mockProvider = path -> null; // Simple mock

        // When
        SimpleDocument result = doc.withResourceProvider(mockProvider);

        // Then
        assertSame(doc, result);
    }

    @Test
    @DisplayName("Should set language code")
    void shouldSetLanguageCode() {
        // Given
        SimpleDocument doc = SimpleDocument.create("Test");

        // When
        SimpleDocument result = doc.withLanguage("en-US");

        // Then
        assertSame(doc, result);
    }

    @Test
    @DisplayName("Should allow method chaining")
    void shouldAllowMethodChaining() {
        // When
        SimpleDocument doc = SimpleDocument.create("Test")
                .withLanguage("de-DE")
                .addHeading("Title")
                .addParagraph("Text 1")
                .addParagraph("Text 2")
                .addHeading("Section", 2);

        // Then
        assertNotNull(doc);
    }

    @Test
    @DisplayName("Should throw exception when calling saveAs before build")
    void shouldThrowExceptionWhenSavingBeforeBuild(@TempDir Path tempDir) {
        // Given
        SimpleDocument doc = SimpleDocument.create("Test")
                .addParagraph("Hello");
        Path outputPath = tempDir.resolve("test.pdf");

        // When & Then
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> doc.saveAs(outputPath.toString())
        );

        assertTrue(exception.getMessage().contains("not built yet"));
    }

    @Test
    @DisplayName("Should throw exception when calling toStream before build")
    void shouldThrowExceptionWhenStreamingBeforeBuild() {
        // Given
        SimpleDocument doc = SimpleDocument.create("Test")
                .addParagraph("Hello");

        // When & Then
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> doc.toStream()
        );

        assertTrue(exception.getMessage().contains("not built yet"));
    }

    @Test
    @DisplayName("Should build document successfully")
    void shouldBuildDocumentSuccessfully() throws Exception {
        // Given
        SimpleDocument doc = SimpleDocument.create("Test")
                .addParagraph("Hello World");

        // When
        SimpleDocument builtDoc = doc.build();

        // Then
        assertNotNull(builtDoc);
        assertSame(doc, builtDoc, "Build should return same instance");
    }

    @Test
    @DisplayName("Should generate PDF stream after build")
    void shouldGeneratePdfStreamAfterBuild() throws Exception {
        // Given
        SimpleDocument doc = SimpleDocument.create("Test Document")
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
        SimpleDocument doc = SimpleDocument.create("Test Document")
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
        SimpleDocument.create("Hello World PDF")
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
        SimpleDocument.create("Multi-Element Document")
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
        SimpleDocument.create("Special Characters Test")
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
        SimpleDocument.create("Long Text Document")
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
        SimpleDocument doc = SimpleDocument.create("Test")
                .addParagraph("Hello");

        // When
        doc.build();
        ByteArrayOutputStream stream1 = doc.toStream();

        // Build again
        doc.build();
        ByteArrayOutputStream stream2 = doc.toStream();

        // Then
        assertNotNull(stream1);
        assertNotNull(stream2);
        assertEquals(stream1.size(), stream2.size(), "Both streams should be identical");
    }
}