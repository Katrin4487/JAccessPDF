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
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
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
 * Unit tests for {@link PDFEncryptor}.
 */
class PDFEncryptorTest {

    @TempDir
    Path tempDir;

    private File testPdf;
    private static final String USER_PASSWORD = "user123";
    private static final String OWNER_PASSWORD = "owner456";

    @BeforeEach
    void setUp() throws IOException {
        testPdf = createTestPdf();
    }

    @Test
    void testEncryptWithUserPassword() throws IOException {
        ByteArrayOutputStream encrypted = PDFEncryptor.builder()
                .withUserPassword(USER_PASSWORD)
                .encrypt(testPdf);

        assertNotNull(encrypted);
        assertTrue(encrypted.size() > 0);

        // Verify document is encrypted (cannot open without password)
        assertThrows(IOException.class, () ->
                Loader.loadPDF(encrypted.toByteArray())
        );

        // Verify can open with password
        try (PDDocument doc = Loader.loadPDF(encrypted.toByteArray(), USER_PASSWORD)) {
            assertEquals(1, doc.getNumberOfPages());
            assertTrue(doc.isEncrypted());
        }
    }

    @Test
    void testEncryptWithUserAndOwnerPassword() throws IOException {
        ByteArrayOutputStream encrypted = PDFEncryptor.builder()
                .withUserPassword(USER_PASSWORD)
                .withOwnerPassword(OWNER_PASSWORD)
                .encrypt(testPdf);

        assertNotNull(encrypted);

        // Can open with user password
        try (PDDocument doc = Loader.loadPDF(encrypted.toByteArray(), USER_PASSWORD)) {
            assertTrue(doc.isEncrypted());
        }

        // Can open with owner password
        try (PDDocument doc = Loader.loadPDF(encrypted.toByteArray(), OWNER_PASSWORD)) {
            assertTrue(doc.isEncrypted());
        }
    }

    @Test
    void testEncryptWithPermissions() throws IOException {
        ByteArrayOutputStream encrypted = PDFEncryptor.builder()
                .withUserPassword(USER_PASSWORD)
                .withOwnerPassword(OWNER_PASSWORD)
                .allowPrinting(true)
                .allowCopying(false)
                .allowModification(false)
                .encrypt(testPdf);

        try (PDDocument doc = Loader.loadPDF(encrypted.toByteArray(), USER_PASSWORD)) {
            AccessPermission permissions = doc.getCurrentAccessPermission();
            assertTrue(permissions.canPrint());
            assertFalse(permissions.canExtractContent());
            assertFalse(permissions.canModify());
        }
    }

    @Test
    void testEncryptWithAllPermissionsAllowed() throws IOException {
        ByteArrayOutputStream encrypted = PDFEncryptor.builder()
                .withUserPassword(USER_PASSWORD)
                .allowAll()
                .encrypt(testPdf);

        try (PDDocument doc = Loader.loadPDF(encrypted.toByteArray(), USER_PASSWORD)) {
            AccessPermission permissions = doc.getCurrentAccessPermission();
            assertTrue(permissions.canPrint());
            assertTrue(permissions.canExtractContent());
            assertTrue(permissions.canModify());
            assertTrue(permissions.canModifyAnnotations());
        }
    }

    @Test
    void testEncryptWithAllPermissionsDenied() throws IOException {
        ByteArrayOutputStream encrypted = PDFEncryptor.builder()
                .withUserPassword(USER_PASSWORD)
                .denyAll()
                .encrypt(testPdf);

        try (PDDocument doc = Loader.loadPDF(encrypted.toByteArray(), USER_PASSWORD)) {
            AccessPermission permissions = doc.getCurrentAccessPermission();
            assertFalse(permissions.canPrint()," Printing should be denied");
            assertFalse(permissions.canExtractContent()," Content extraction should be denied");
            assertFalse(permissions.canModify()," Modification should be denied");
            // Accessibility should still be allowed
            assertTrue(permissions.canExtractForAccessibility()," Accessibility extraction should be allowed");
        }
    }

    @Test
    void testEncryptToFile() throws IOException {
        File outputFile = tempDir.resolve("encrypted.pdf").toFile();

        File result = PDFEncryptor.builder()
                .withUserPassword(USER_PASSWORD)
                .encryptToFile(testPdf, outputFile);

        assertEquals(outputFile, result);
        assertTrue(outputFile.exists());

        try (PDDocument doc = Loader.loadPDF(outputFile, USER_PASSWORD)) {
            assertTrue(doc.isEncrypted());
        }
    }

    @Test
    void testEncryptFromByteArray() throws IOException {
        byte[] pdfBytes = readPdfAsBytes(testPdf);

        ByteArrayOutputStream encrypted = PDFEncryptor.builder()
                .withUserPassword(USER_PASSWORD)
                .encrypt(pdfBytes);

        assertNotNull(encrypted);

        try (PDDocument doc = Loader.loadPDF(encrypted.toByteArray(), USER_PASSWORD)) {
            assertTrue(doc.isEncrypted());
        }
    }

    @Test
    void testEncryptWithoutPasswordThrowsException() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                PDFEncryptor.builder()
                        .encrypt(testPdf)
        );

        assertTrue(exception.getMessage().contains("User password must be set"));
    }

    @Test
    void testEncryptWithNullPasswordThrowsException() {
        assertThrows(NullPointerException.class, () ->
                PDFEncryptor.builder()
                        .withUserPassword(null)
        );
    }

    @Test
    void testEncryptWithEmptyPasswordThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                PDFEncryptor.builder()
                        .withUserPassword("")
        );
    }

    @Test
    void testEncryptWithInvalidKeyLength() {
        assertThrows(IllegalArgumentException.class, () ->
                PDFEncryptor.builder()
                        .withKeyLength(512) // Invalid key length
        );
    }

    @Test
    void testEncryptWithValidKeyLengths() throws IOException {
        // Test 128-bit
        ByteArrayOutputStream encrypted128 = PDFEncryptor.builder()
                .withUserPassword(USER_PASSWORD)
                .withKeyLength(128)
                .encrypt(testPdf);
        assertNotNull(encrypted128);

        // Test 256-bit (default)
        ByteArrayOutputStream encrypted256 = PDFEncryptor.builder()
                .withUserPassword(USER_PASSWORD)
                .withKeyLength(256)
                .encrypt(testPdf);
        assertNotNull(encrypted256);
    }

    // Helper methods

    private File createTestPdf() throws IOException {
        File file = tempDir.resolve("test-" + System.nanoTime() + ".pdf").toFile();

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("Test Content");
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