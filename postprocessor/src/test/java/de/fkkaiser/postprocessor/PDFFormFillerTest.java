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
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.form.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link PDFFormFiller}.
 */
class PDFFormFillerTest {

    @TempDir
    Path tempDir;

    private File formPdf;

    @BeforeEach
    void setUp() throws IOException {
        formPdf = createTestForm();
    }

    @Test
    void testFillTextField() throws IOException {
        ByteArrayOutputStream filled = PDFFormFiller.builder()
                .withField("name", "Max Mustermann")
                .withField("email", "max@example.com")
                .fill(formPdf);

        assertNotNull(filled);
        assertTrue(filled.size() > 0);

        // Verify field values
        try (PDDocument doc = Loader.loadPDF(filled.toByteArray())) {
            PDAcroForm form = doc.getDocumentCatalog().getAcroForm();
            assertNotNull(form);
            assertEquals("Max Mustermann", form.getField("name").getValueAsString());
            assertEquals("max@example.com", form.getField("email").getValueAsString());
        }
    }

    @Test
    void testFillCheckbox() throws IOException {
        ByteArrayOutputStream filled = PDFFormFiller.builder()
                .withField("newsletter", true)
                .fill(formPdf);

        try (PDDocument doc = Loader.loadPDF(filled.toByteArray())) {
            PDAcroForm form = doc.getDocumentCatalog().getAcroForm();
            PDCheckBox checkbox = (PDCheckBox) form.getField("newsletter");
            assertTrue(checkbox.isChecked());
        }
    }

    @Test
    void testFillCheckboxUnchecked() throws IOException {
        ByteArrayOutputStream filled = PDFFormFiller.builder()
                .withField("newsletter", false)
                .fill(formPdf);

        try (PDDocument doc = Loader.loadPDF(filled.toByteArray())) {
            PDAcroForm form = doc.getDocumentCatalog().getAcroForm();
            PDCheckBox checkbox = (PDCheckBox) form.getField("newsletter");
            assertFalse(checkbox.isChecked());
        }
    }

    @Test
    void testFillWithMap() throws IOException {
        Map<String, Object> formData = new HashMap<>();
        formData.put("name", "Katrin Kaiser");
        formData.put("email", "katrin@example.com");
        formData.put("newsletter", true);

        ByteArrayOutputStream filled = PDFFormFiller.builder()
                .withFields(formData)
                .fill(formPdf);

        try (PDDocument doc = Loader.loadPDF(filled.toByteArray())) {
            PDAcroForm form = doc.getDocumentCatalog().getAcroForm();
            assertEquals("Katrin Kaiser", form.getField("name").getValueAsString());
            assertEquals("katrin@example.com", form.getField("email").getValueAsString());
        }
    }

    @Test
    void testFillAndFlatten() throws IOException {
        ByteArrayOutputStream filled = PDFFormFiller.builder()
                .withField("name", "Test User")
                .flatten(true)
                .fill(formPdf);

        try (PDDocument doc = Loader.loadPDF(filled.toByteArray())) {
            PDAcroForm form = doc.getDocumentCatalog().getAcroForm();
            // After flattening, form fields should be null or have no fields
            assertTrue(form == null || form.getFields().isEmpty());
        }
    }

    @Test
    void testFillToFile() throws IOException {
        File outputFile = tempDir.resolve("filled-form.pdf").toFile();

        File result = PDFFormFiller.builder()
                .withField("name", "Test User")
                .withField("email", "test@example.com")
                .fillToFile(formPdf, outputFile);

        assertEquals(outputFile, result);
        assertTrue(outputFile.exists());

        try (PDDocument doc = Loader.loadPDF(outputFile)) {
            PDAcroForm form = doc.getDocumentCatalog().getAcroForm();
            assertEquals("Test User", form.getField("name").getValueAsString());
        }
    }

    @Test
    void testFillFromByteArray() throws IOException {
        byte[] formBytes = readPdfAsBytes(formPdf);

        ByteArrayOutputStream filled = PDFFormFiller.builder()
                .withField("name", "Byte Array Test")
                .fill(formBytes);

        assertNotNull(filled);

        try (PDDocument doc = Loader.loadPDF(filled.toByteArray())) {
            PDAcroForm form = doc.getDocumentCatalog().getAcroForm();
            assertEquals("Byte Array Test", form.getField("name").getValueAsString());
        }
    }

    @Test
    void testFillPdfWithoutForm() {
        File pdfWithoutForm = tempDir.resolve("no-form.pdf").toFile();

        // Create PDF without form
        try (PDDocument doc = new PDDocument()) {
            doc.addPage(new PDPage());
            doc.save(pdfWithoutForm);
        } catch (IOException e) {
            fail("Failed to create test PDF");
        }

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                PDFFormFiller.builder()
                        .withField("name", "Test")
                        .fill(pdfWithoutForm)
        );

        assertTrue(exception.getMessage().contains("does not contain a form"));
    }

    @Test
    void testFillUnknownFieldWithoutIgnore() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                PDFFormFiller.builder()
                        .withField("name", "Test")
                        .withField("unknown_field", "This doesn't exist")
                        .ignoreUnknownFields(false)
                        .fill(formPdf)
        );

        assertTrue(exception.getMessage().contains("does not exist"));
    }

    @Test
    void testFillUnknownFieldWithIgnore() throws IOException {
        // Should not throw exception
        ByteArrayOutputStream filled = PDFFormFiller.builder()
                .withField("name", "Test")
                .withField("unknown_field", "This doesn't exist")
                .ignoreUnknownFields(true)
                .fill(formPdf);

        assertNotNull(filled);

        try (PDDocument doc = Loader.loadPDF(filled.toByteArray())) {
            PDAcroForm form = doc.getDocumentCatalog().getAcroForm();
            assertEquals("Test", form.getField("name").getValueAsString());
            assertNull(form.getField("unknown_field"));
        }
    }

    @Test
    void testFillCheckboxWithWrongType() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                PDFFormFiller.builder()
                        .withField("newsletter", "true")  // String instead of Boolean
                        .fill(formPdf)
        );

        assertTrue(exception.getMessage().contains("requires Boolean value"));
    }

    @Test
    void testFillWithNullFieldName() {
        assertThrows(NullPointerException.class, () ->
                PDFFormFiller.builder()
                        .withField(null, "value")
        );
    }

    @Test
    void testFillWithNullValue() {
        assertThrows(NullPointerException.class, () ->
                PDFFormFiller.builder()
                        .withField("name", null)
        );
    }

    @Test
    void testFillWithNumericValue() throws IOException {
        ByteArrayOutputStream filled = PDFFormFiller.builder()
                .withField("name", 12345)  // Number will be converted to String
                .fill(formPdf);

        try (PDDocument doc = Loader.loadPDF(filled.toByteArray())) {
            PDAcroForm form = doc.getDocumentCatalog().getAcroForm();
            assertEquals("12345", form.getField("name").getValueAsString());
        }
    }

    // Helper method to create a test form
    private File createTestForm() throws IOException {
        File file = tempDir.resolve("test-form-" + System.nanoTime() + ".pdf").toFile();

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            // Create AcroForm
            PDAcroForm acroForm = new PDAcroForm(document);
            document.getDocumentCatalog().setAcroForm(acroForm);

            // Set default resources
            PDResources resources = new PDResources();
            acroForm.setDefaultResources(resources);

            // Add default appearance string
            String defaultAppearance = "/Helv 12 Tf 0 g";
            acroForm.setDefaultAppearance(defaultAppearance);

            // Create text field: name
            PDTextField nameField = new PDTextField(acroForm);
            nameField.setPartialName("name");
            nameField.setDefaultAppearance(defaultAppearance);

            PDAnnotationWidget nameWidget = nameField.getWidgets().getFirst();
            nameWidget.setRectangle(new PDRectangle(50, 750, 200, 20));
            nameWidget.setPage(page);
            page.getAnnotations().add(nameWidget);

            acroForm.getFields().add(nameField);

            // Create text field: email
            PDTextField emailField = new PDTextField(acroForm);
            emailField.setPartialName("email");
            emailField.setDefaultAppearance(defaultAppearance);

            PDAnnotationWidget emailWidget = emailField.getWidgets().getFirst();
            emailWidget.setRectangle(new PDRectangle(50, 720, 200, 20));
            emailWidget.setPage(page);
            page.getAnnotations().add(emailWidget);

            acroForm.getFields().add(emailField);

            // Create checkbox: newsletter
            PDCheckBox checkBox = new PDCheckBox(acroForm);
            checkBox.setPartialName("newsletter");

            PDAnnotationWidget checkWidget = checkBox.getWidgets().getFirst();
            checkWidget.setRectangle(new PDRectangle(50, 690, 20, 20));
            checkWidget.setPage(page);
            page.getAnnotations().add(checkWidget);

            acroForm.getFields().add(checkBox);

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