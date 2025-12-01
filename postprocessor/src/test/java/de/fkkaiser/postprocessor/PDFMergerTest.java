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
package de.fkkaiser.postprocessor;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link PDFMerger}.
 */
class PDFMergerTest {

    @TempDir
    Path tempDir;

    private File pdf1;
    private File pdf2;
    private File pdf3;

    @BeforeEach
    void setUp() throws IOException {
        pdf1 = createTestPdf("Test PDF 1");
        pdf2 = createTestPdf("Test PDF 2");
        pdf3 = createTestPdf("Test PDF 3");
    }

    @Test
    void testMergeTwoDocuments() throws IOException {
        ByteArrayOutputStream result = PDFMerger.builder()
                .addDocument(pdf1)
                .addDocument(pdf2)
                .merge();

        assertNotNull(result);
        assertTrue(result.size() > 0);

        // Verify merged PDF has 2 pages
        try (PDDocument doc = Loader.loadPDF(result.toByteArray())) {
            assertEquals(2, doc.getNumberOfPages());
        }
    }

    @Test
    void testMergeThreeDocuments() throws IOException {
        ByteArrayOutputStream result = PDFMerger.builder()
                .addDocument(pdf1)
                .addDocument(pdf2)
                .addDocument(pdf3)
                .merge();

        assertNotNull(result);

        try (PDDocument doc = Loader.loadPDF(result.toByteArray())) {
            assertEquals(3, doc.getNumberOfPages());
        }
    }

    @Test
    void testMergeFromByteArray() throws IOException {
        byte[] pdf1Bytes = readPdfAsBytes(pdf1);
        byte[] pdf2Bytes = readPdfAsBytes(pdf2);

        ByteArrayOutputStream result = PDFMerger.builder()
                .addDocument(pdf1Bytes)
                .addDocument(pdf2Bytes)
                .merge();

        assertNotNull(result);

        try (PDDocument doc = Loader.loadPDF(result.toByteArray())) {
            assertEquals(2, doc.getNumberOfPages());
        }
    }

    @Test
    void testMergeToFile() throws IOException {
        File outputFile = tempDir.resolve("merged.pdf").toFile();

        File result = PDFMerger.builder()
                .addDocument(pdf1)
                .addDocument(pdf2)
                .mergeToFile(outputFile);

        assertEquals(outputFile, result);
        assertTrue(outputFile.exists());

        try (PDDocument doc = Loader.loadPDF(outputFile)) {
            assertEquals(2, doc.getNumberOfPages());
        }
    }

    @Test
    void testMergeWithLessThanTwoDocuments() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                PDFMerger.builder()
                        .addDocument(pdf1)
                        .merge()
        );

        assertTrue(exception.getMessage().contains("At least"));
    }

    @Test
    void testMergeWithNullDocument() {
        assertThrows(NullPointerException.class, () ->
                PDFMerger.builder()
                        .addDocument((File) null)
        );
    }

    @Test
    void testMergeWithNonExistentFile() {
        File nonExistent = new File("does-not-exist.pdf");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                PDFMerger.builder()
                        .addDocument(nonExistent)
        );

        assertTrue(exception.getMessage().contains("does not exist"));
    }

    // Helper methods

    private File createTestPdf(String text) throws IOException {
        File file = tempDir.resolve("test-" + System.nanoTime() + ".pdf").toFile();

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText(text);
                contentStream.endText();
            }

            document.save(file);
        }

        return file;
    }

    private byte[] readPdfAsBytes(File pdfFile) throws IOException {
        try (PDDocument doc = Loader.loadPDF(pdfFile);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            doc.save(baos);
            return baos.toByteArray();
        }
    }
}