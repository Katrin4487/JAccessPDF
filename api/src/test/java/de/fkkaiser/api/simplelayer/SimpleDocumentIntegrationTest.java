package de.fkkaiser.api.simplelayer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SimpleDocument Integration Tests")
class SimpleDocumentIntegrationTest {

    @Test
    @DisplayName("End-to-End: Create complete document")
    void endToEndCreateCompleteDocument(@TempDir Path tempDir) throws Exception {
        // Given
        Path outputPath = tempDir.resolve("complete-document.pdf");

        // When
        SimpleDocumentBuilder.create("Complete Test Document")
                .withLanguage("de-DE")
                .addHeading("Hauptüberschrift", 1)
                .addParagraph("Dies ist ein einleitender Absatz.")
                .addHeading("Erster Abschnitt", 2)
                .addParagraph("Inhalt des ersten Abschnitts.")
                .addParagraph("Noch mehr Inhalt.")
                .addHeading("Zweiter Abschnitt", 2)
                .addParagraph("Inhalt des zweiten Abschnitts.")
                .addHeading("Unterabschnitt", 3)
                .addParagraph("Detaillierter Inhalt.")
                .build()
                .saveAs(outputPath.toString());

        // Then
        assertTrue(Files.exists(outputPath));
        byte[] pdfBytes = Files.readAllBytes(outputPath);

        // Verify PDF structure
        assertTrue(pdfBytes.length > 1000, "PDF should have reasonable size");
        assertEquals('%', pdfBytes[0]);
        assertEquals('P', pdfBytes[1]);
        assertEquals('D', pdfBytes[2]);
        assertEquals('F', pdfBytes[3]);

        // Verify PDF contains metadata (title should be in PDF)
        String pdfContent = new String(pdfBytes);
        assertTrue(pdfContent.contains("Complete Test Document"),
                "PDF should contain document title");
    }

    @Test
    @DisplayName("Performance: Generate 10 documents quickly")
    void performanceGenerate10DocumentsQuickly(@TempDir Path tempDir) throws Exception {
        // Given
        long startTime = System.currentTimeMillis();

        // When
        for (int i = 0; i < 10; i++) {
            Path outputPath = tempDir.resolve("doc-" + i + ".pdf");

            SimpleDocumentBuilder.create("Document " + i)
                    .addHeading("Title " + i)
                    .addParagraph("Content " + i)
                    .build()
                    .saveAs(outputPath.toString());
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // Then
        assertTrue(duration < 30000,
                "Should generate 10 documents in less than 30 seconds, took: " + duration + "ms");

        // Verify all files exist
        for (int i = 0; i < 10; i++) {
            Path outputPath = tempDir.resolve("doc-" + i + ".pdf");
            assertTrue(Files.exists(outputPath), "Document " + i + " should exist");
        }
    }

    @Test
    @DisplayName("Stress test: Very large document")
    void stressTestVeryLargeDocument(@TempDir Path tempDir) throws Exception {
        // Given
        Path outputPath = tempDir.resolve("large-document.pdf");
        SimpleDocumentBuilder doc = SimpleDocumentBuilder.create("Large Document");

        // Add many elements
        for (int i = 1; i <= 100; i++) {
            doc.addHeading("Section " + i );
            doc.addParagraph("Lorem ipsum dolor sit amet, consectetur adipiscing elit. ".repeat(10));
        }

        // When
        doc.build().saveAs(outputPath.toString());

        // Then
        assertTrue(Files.exists(outputPath));
        long fileSize = Files.size(outputPath);
        assertTrue(fileSize > 10000, "Large document should produce large PDF: " + fileSize + " bytes");
    }

    @Test
    @DisplayName("Comparison: toStream and saveAs produce similar output")
    void comparisonToStreamAndSaveAsProduceSimilarOutput(@TempDir Path tempDir) throws Exception {
        // Given
        SimpleDocument doc = SimpleDocumentBuilder.create("Comparison Test")
                .addParagraph("Test content")
                .build();

        Path filePath = tempDir.resolve("from-file.pdf");

        // When
        ByteArrayOutputStream stream = doc.toStream();
        doc.saveAs(filePath.toString());

        byte[] streamBytes = stream.toByteArray();
        byte[] fileBytes = Files.readAllBytes(filePath);

        // Then
        // Note: PDFs contain timestamps and IDs, so exact byte comparison won't work
        // Instead, we verify they are similar in size and structure
        assertTrue(Math.abs(streamBytes.length - fileBytes.length) < 100,
                "Stream and file should have similar size (within 100 bytes). " +
                        "Stream: " + streamBytes.length + ", File: " + fileBytes.length);

        // Verify both are valid PDFs
        assertTrue(streamBytes.length > 100, "Stream should produce valid PDF");
        assertTrue(fileBytes.length > 100, "File should produce valid PDF");

        // Check PDF signatures
        assertEquals('%', streamBytes[0]);
        assertEquals('P', streamBytes[1]);
        assertEquals('D', streamBytes[2]);
        assertEquals('F', streamBytes[3]);

        assertEquals('%', fileBytes[0]);
        assertEquals('P', fileBytes[1]);
        assertEquals('D', fileBytes[2]);
        assertEquals('F', fileBytes[3]);
    }

    @Test
    @DisplayName("Unicode support: Various languages")
    void unicodeSupportVariousLanguages(@TempDir Path tempDir) throws Exception {
        // Given
        Path outputPath = tempDir.resolve("unicode-test.pdf");

        // When
        SimpleDocumentBuilder.create("Unicode Test")
                .addHeading("Deutsch: Äöü ß")
                .addParagraph("Français: àéèêë")
                .addParagraph("Español: ñáéíóú")
                .addParagraph("Русский: Привет мир")
                .addParagraph("中文: 你好世界")
                .addParagraph("العربية: مرحبا بالعالم")
                .build()
                .saveAs(outputPath.toString());

        // Then
        assertTrue(Files.exists(outputPath));
        assertTrue(Files.size(outputPath) > 500);
    }

    @Test
    @DisplayName("Idempotency: Multiple builds produce consistent results")
    void idempotencyMultipleBuildsProduceConsistentResults() throws Exception {
        // Given
        SimpleDocumentBuilder docbuilder = SimpleDocumentBuilder.create("Idempotency Test")
                .addParagraph("Test content");

        // When
        SimpleDocument doc = docbuilder.build();
        ByteArrayOutputStream stream1 = doc.toStream();

        docbuilder.build();
        ByteArrayOutputStream stream2 = doc.toStream();

        docbuilder.build();
        ByteArrayOutputStream stream3 = doc.toStream();

        // Then
        byte[] bytes1 = stream1.toByteArray();
        byte[] bytes2 = stream2.toByteArray();
        byte[] bytes3 = stream3.toByteArray();

        // Note: PDFs contain timestamps, so exact byte comparison won't work
        // Instead, we verify consistent size and structure
        assertTrue(Math.abs(bytes1.length - bytes2.length) < 100,
                "Multiple builds should produce similar sizes. " +
                        "Build 1: " + bytes1.length + ", Build 2: " + bytes2.length);

        assertTrue(Math.abs(bytes2.length - bytes3.length) < 100,
                "Multiple builds should produce similar sizes. " +
                        "Build 2: " + bytes2.length + ", Build 3: " + bytes3.length);

        // All should be valid PDFs with same signature
        for (byte[] bytes : new byte[][]{bytes1, bytes2, bytes3}) {
            assertEquals('%', bytes[0]);
            assertEquals('P', bytes[1]);
            assertEquals('D', bytes[2]);
            assertEquals('F', bytes[3]);
        }

        // Verify all have reasonable content
        assertTrue(bytes1.length > 100, "Build 1 should produce valid PDF");
        assertTrue(bytes2.length > 100, "Build 2 should produce valid PDF");
        assertTrue(bytes3.length > 100, "Build 3 should produce valid PDF");
    }

    @Test
    @DisplayName("Should use Open Sans as default font")
    void shouldUseOpenSansAsDefaultFont(@TempDir Path tempDir) throws Exception {
        // Given
        Path outputPath = tempDir.resolve("default-font.pdf");

        // When
        SimpleDocumentBuilder.create("Default Font Test")
                .addParagraph("Text in default Open Sans font")
                .build()
                .saveAs(outputPath.toString());

        // Then
        assertTrue(Files.exists(outputPath));
        assertTrue(Files.size(outputPath) > 0);

        // Note: Actual font verification would require PDF parsing
        // Here we just verify the document generates successfully
    }

    @Test
    @DisplayName("Should work with custom language")
    void shouldWorkWithCustomLanguage(@TempDir Path tempDir) throws Exception {
        // Given
        Path outputPath = tempDir.resolve("english.pdf");

        // When
        SimpleDocumentBuilder.create("English Document")
                .withLanguage("en-US")
                .addHeading("Welcome")
                .addParagraph("This is an English document.")
                .build()
                .saveAs(outputPath.toString());

        // Then
        assertTrue(Files.exists(outputPath));
    }

    @Test
    @DisplayName("Should handle all heading levels")
    void shouldHandleAllHeadingLevels(@TempDir Path tempDir) throws Exception {
        // Given
        Path outputPath = tempDir.resolve("all-headings.pdf");

        // When
        SimpleDocumentBuilder docbuilder = SimpleDocumentBuilder.create("All Heading Levels");
        for (int level = 1; level <= 6; level++) {
            docbuilder.addHeading("Heading Level " + level, level);
            docbuilder.addParagraph("Content for level " + level);
        }
        docbuilder.build().saveAs(outputPath.toString());

        // Then
        assertTrue(Files.exists(outputPath));
        assertTrue(Files.size(outputPath) > 500);
    }

    @Test
    @DisplayName("Should handle document with only headings")
    void shouldHandleDocumentWithOnlyHeadings(@TempDir Path tempDir) throws Exception {
        // Given
        Path outputPath = tempDir.resolve("only-headings.pdf");

        // When
        SimpleDocumentBuilder.create("Only Headings")
                .addHeading("First Heading")
                .addHeading("Second Heading", 2)
                .addHeading("Third Heading", 3)
                .build()
                .saveAs(outputPath.toString());

        // Then
        assertTrue(Files.exists(outputPath));
    }

    @Test
    @DisplayName("Should handle document with only paragraphs")
    void shouldHandleDocumentWithOnlyParagraphs(@TempDir Path tempDir) throws Exception {
        // Given
        Path outputPath = tempDir.resolve("only-paragraphs.pdf");

        // When
        SimpleDocumentBuilder.create("Only Paragraphs")
                .addParagraph("First paragraph")
                .addParagraph("Second paragraph")
                .addParagraph("Third paragraph")
                .build()
                .saveAs(outputPath.toString());

        // Then
        assertTrue(Files.exists(outputPath));
    }

    @Test
    @DisplayName("Should handle document with ordered list")
    void shouldHandleDocumentWithOrderedList(@TempDir Path tempDir) throws Exception {
        // Given
        Path outputPath = tempDir.resolve("orderedlist.pdf");

        // When
        SimpleDocumentBuilder.create("Ordered List")
                .addOrderedList(List.of("Item 1","Item 2"))
                .build()
                .saveAs(outputPath.toString());

        // Then
        assertTrue(Files.exists(outputPath));
    }

    @Test
    @DisplayName("Should handle document with unordered list")
    void shouldHandleDocumentWithUnorderedList(@TempDir Path tempDir) throws Exception {
        // Given
        Path outputPath = tempDir.resolve("unorderedlist.pdf");

        // When
        SimpleDocumentBuilder.create("Unordered List")
                .addUnorderedList(List.of("Item 1","Item 2"))
                .build()
                .saveAs(outputPath.toString());

        // Then
        assertTrue(Files.exists(outputPath));
    }

    @Test
    @DisplayName("Should handle document with image without alt text")
    void shouldHandleDocumentWithImage(@TempDir Path tempDir) throws Exception {
        // Given
        Path outputPath = tempDir.resolve("image.pdf");

        // When
        SimpleDocumentBuilder.create("Image")
                .addImage("images/logo.png")
                .build()
                .saveAs(outputPath.toString());

        // Then
        assertTrue(Files.exists(outputPath));
    }

    @Test
    @DisplayName("Should handle document with table")
    void shouldHandleDocumentWithTable(@TempDir Path tempDir) throws Exception {
        // Given
        Path outputPath = tempDir.resolve("table.pdf");

        SimpleTable table = new SimpleTable()
                .setColumns("50%", "50%")
                .addHeaderRow("Header 1", "Header 2")
                .addBodyRow("Zelle 1", "Zelle 2")
                .addBodyRow(
                SimpleTableCell.of("Komplexe Zelle").addParagraph("Absatz..."),
 SimpleTableCell.of("Zelle 4")
                );

        // When
        SimpleDocumentBuilder.create("Table")
                .addTable(table)
                .build()
                .saveAs(outputPath.toString());

        // Then
        assertTrue(Files.exists(outputPath));
    }

    @Test
    @DisplayName("Should handle document with image with alt text")
    void shouldHandleDocumentWithImageWithAltText(@TempDir Path tempDir) throws Exception {
        // Given
        Path outputPath = tempDir.resolve("image.pdf");

        // When
        SimpleDocumentBuilder.create("Image")
                .addImage("images/logo.png","Alt Text")
                .build()
                .saveAs(outputPath.toString());

        // Then
        assertTrue(Files.exists(outputPath));
    }

    @Test
    @DisplayName("Consistency: Same input produces similar PDF size")
    void consistencySameInputProducesSimilarPdfSize(@TempDir Path tempDir) throws Exception {
        // Given
        String title = "Consistency Test";
        String content = "This is test content for consistency verification.";

        // When - Generate 3 PDFs with identical content
        Path pdf1 = tempDir.resolve("consistency-1.pdf");
        Path pdf2 = tempDir.resolve("consistency-2.pdf");
        Path pdf3 = tempDir.resolve("consistency-3.pdf");

        SimpleDocumentBuilder.create(title).addParagraph(content).build().saveAs(pdf1.toString());
        SimpleDocumentBuilder.create(title).addParagraph(content).build().saveAs(pdf2.toString());
        SimpleDocumentBuilder.create(title).addParagraph(content).build().saveAs(pdf3.toString());

        // Then - All should have similar size (within 100 bytes due to timestamps)
        long size1 = Files.size(pdf1);
        long size2 = Files.size(pdf2);
        long size3 = Files.size(pdf3);

        assertTrue(Math.abs(size1 - size2) < 100,
                "PDF 1 and 2 should have similar size. " +
                        "Size 1: " + size1 + ", Size 2: " + size2);

        assertTrue(Math.abs(size2 - size3) < 100,
                "PDF 2 and 3 should have similar size. " +
                        "Size 2: " + size2 + ", Size 3: " + size3);

        // All should be valid PDFs
        for (Path path : new Path[]{pdf1, pdf2, pdf3}) {
            byte[] bytes = Files.readAllBytes(path);
            assertEquals('%', bytes[0]);
            assertEquals('P', bytes[1]);
            assertEquals('D', bytes[2]);
            assertEquals('F', bytes[3]);
        }
    }

    @Test
    @DisplayName("Should generate valid PDF/UA structure")
    void shouldGenerateValidPdfUaStructure(@TempDir Path tempDir) throws Exception {
        // Given
        Path outputPath = tempDir.resolve("pdfua-test.pdf");

        // When
        SimpleDocumentBuilder.create("PDF/UA Test Document")
                .withLanguage("de-DE")
                .addHeading("Accessible Document")
                .addParagraph("This document should be PDF/UA compliant.")
                .build()
                .saveAs(outputPath.toString());

        // Then
        assertTrue(Files.exists(outputPath));
        byte[] pdfBytes = Files.readAllBytes(outputPath);

        // Basic PDF/UA checks
        String pdfContent = new String(pdfBytes);

        // Should contain PDF/UA identifier
        assertTrue(pdfContent.contains("PDF/UA") || pdfContent.contains("pdfaid"),
                "PDF should indicate UA compliance");

        // Should have document title in metadata
        assertTrue(pdfContent.contains("PDF/UA Test Document"),
                "PDF should contain document title");
    }
}