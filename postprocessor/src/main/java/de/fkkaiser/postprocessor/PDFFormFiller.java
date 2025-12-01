package de.fkkaiser.postprocessor;

import de.fkkaiser.api.utils.EResourceProvider;
import de.fkkaiser.model.annotation.PublicAPI;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDCheckBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDChoice;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDRadioButton;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;

/**
 * Service for filling PDF form fields (AcroForms) with data.
 *
 * <p>This class provides a fluent API for populating PDF forms with values, supporting
 * various field types including text fields, checkboxes, radio buttons, and dropdown lists.
 * Forms can optionally be flattened after filling to make them non-editable.</p>
 *
 * <p><b>Usage Example - Basic Form Filling:</b></p>
 * <pre>{@code
 * ByteArrayOutputStream filled = PDFFormFiller.builder()
 *     .withField("name", "Max Mustermann")
 *     .withField("email", "max@example.com")
 *     .withField("newsletter", true)  // Checkbox
 *     .fill(new File("template.pdf"));
 * }</pre>
 *
 * <p><b>Usage Example - With Map:</b></p>
 * <pre>{@code
 * Map<String, Object> formData = Map.of(
 *     "applicant_name", "Katrin Kaiser",
 *     "date", "2025-01-15",
 *     "terms_accepted", true
 * );
 *
 * PDFFormFiller.builder()
 *     .withFields(formData)
 *     .flatten(true)  // Make form non-editable
 *     .fillToFile(template, output);
 * }</pre>
 *
 * <p><b>Usage Example - With ResourceProvider:</b></p>
 * <pre>{@code
 * EResourceProvider provider = new EClasspathResourceProvider();
 *
 * ByteArrayOutputStream filled = PDFFormFiller.builder()
 *     .withField("company", "Acme Corp")
 *     .withField("department", "IT")
 *     .fill(provider, "templates/form.pdf");
 * }</pre>
 *
 * <p><b>Supported Field Types:</b></p>
 * <ul>
 *   <li>Text fields: Use String values</li>
 *   <li>Checkboxes: Use Boolean values (true = checked, false = unchecked)</li>
 *   <li>Radio buttons: Use String values (the export value of the selected option)</li>
 *   <li>Dropdown/List boxes: Use String values (must match an available option)</li>
 * </ul>
 *
 * <p><b>Flattening:</b></p>
 * When flatten is set to {@code true}, the form fields are converted to static content
 * and can no longer be edited. This is useful for creating final, non-editable documents.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 * @since 0.10.0
 */
@PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
public class PDFFormFiller {

    private static final Logger logger = LoggerFactory.getLogger(PDFFormFiller.class);

    private PDFFormFiller() {
        // Private constructor - use builder()
    }

    /**
     * Creates a new builder for configuring and executing PDF form filling operations.
     *
     * @return a new Builder instance
     */
    @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for configuring PDF form filling operations.
     */
    @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
    public static class Builder {
        private final Map<String, Object> fieldValues = new LinkedHashMap<>();
        private boolean flatten = false;
        private boolean ignoreUnknownFields = false;
        private boolean ignoreReadOnlyFields = true;

        private Builder() {
        }

        /**
         * Sets a value for a form field.
         *
         * <p><b>Supported value types:</b></p>
         * <ul>
         *   <li>String: For text fields, radio buttons, and dropdown lists</li>
         *   <li>Boolean: For checkboxes (true = checked, false = unchecked)</li>
         *   <li>Number: For numeric text fields (will be converted to String)</li>
         * </ul>
         *
         * @param fieldName the name of the form field; must not be {@code null}
         * @param value the value to set; must not be {@code null}
         * @return this builder for method chaining
         * @throws NullPointerException if fieldName or value is {@code null}
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public Builder withField(String fieldName, Object value) {
            Objects.requireNonNull(fieldName, "fieldName must not be null");
            Objects.requireNonNull(value, "value must not be null");

            this.fieldValues.put(fieldName, value);
            logger.debug("Added field: {} = {}", fieldName, value);
            return this;
        }

        /**
         * Sets multiple field values from a map.
         *
         * @param fields a map of field names to values; must not be {@code null}
         * @return this builder for method chaining
         * @throws NullPointerException if fields is {@code null}
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public Builder withFields(Map<String, Object> fields) {
            Objects.requireNonNull(fields, "fields must not be null");
            fields.forEach(this::withField);
            return this;
        }

        /**
         * Sets whether to flatten the form after filling.
         * When flattened, form fields become static content and cannot be edited.
         * Default is {@code false}.
         *
         * @param flatten {@code true} to flatten the form
         * @return this builder for method chaining
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public Builder flatten(boolean flatten) {
            this.flatten = flatten;
            logger.debug("Flatten set to: {}", flatten);
            return this;
        }

        /**
         * Sets whether to ignore unknown field names.
         * If {@code false}, an exception will be thrown if a field name doesn't exist in the form.
         * If {@code true}, unknown fields will be logged as warnings but processing continues.
         * Default is {@code false}.
         *
         * @param ignore {@code true} to ignore unknown fields
         * @return this builder for method chaining
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public Builder ignoreUnknownFields(boolean ignore) {
            this.ignoreUnknownFields = ignore;
            logger.debug("Ignore unknown fields set to: {}", ignore);
            return this;
        }

        /**
         * Sets whether to ignore read-only fields.
         * If {@code true}, attempts to set read-only fields will be logged as warnings.
         * If {@code false}, an exception will be thrown when trying to set a read-only field.
         * Default is {@code true}.
         *
         * @param ignore {@code true} to ignore read-only fields
         * @return this builder for method chaining
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public Builder ignoreReadOnlyFields(boolean ignore) {
            this.ignoreReadOnlyFields = ignore;
            logger.debug("Ignore read-only fields set to: {}", ignore);
            return this;
        }

        /**
         * Fills a PDF form from a file and returns the result as a ByteArrayOutputStream.
         *
         * @param inputFile the PDF file with form fields; must not be {@code null} and must exist
         * @return a ByteArrayOutputStream containing the filled PDF
         * @throws NullPointerException if inputFile is {@code null}
         * @throws IllegalArgumentException if inputFile does not exist or is not a file, or if the PDF has no form
         * @throws IOException if an I/O error occurs during form filling
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public ByteArrayOutputStream fill(File inputFile) throws IOException {
            Objects.requireNonNull(inputFile, "inputFile must not be null");
            if (!inputFile.exists()) {
                throw new IllegalArgumentException("Input file does not exist: " + inputFile.getAbsolutePath());
            }
            if (!inputFile.isFile()) {
                throw new IllegalArgumentException("Input is not a file: " + inputFile.getAbsolutePath());
            }

            try (PDDocument document = Loader.loadPDF(inputFile)) {
                return fillDocument(document);
            }
        }

        /**
         * Fills a PDF form from a Path and returns the result as a ByteArrayOutputStream.
         *
         * @param inputPath the path to the PDF file with form fields; must not be {@code null}
         * @return a ByteArrayOutputStream containing the filled PDF
         * @throws NullPointerException if inputPath is {@code null}
         * @throws IllegalArgumentException if the PDF has no form
         * @throws IOException if an I/O error occurs during form filling
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public ByteArrayOutputStream fill(Path inputPath) throws IOException {
            Objects.requireNonNull(inputPath, "inputPath must not be null");
            return fill(inputPath.toFile());
        }

        /**
         * Fills a PDF form from a file path string and returns the result as a ByteArrayOutputStream.
         *
         * @param inputPath the path to the PDF file with form fields; must not be {@code null}
         * @return a ByteArrayOutputStream containing the filled PDF
         * @throws NullPointerException if inputPath is {@code null}
         * @throws IllegalArgumentException if the PDF has no form
         * @throws IOException if an I/O error occurs during form filling
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public ByteArrayOutputStream fill(String inputPath) throws IOException {
            Objects.requireNonNull(inputPath, "inputPath must not be null");
            return fill(new File(inputPath));
        }

        /**
         * Fills a PDF form from an InputStream and returns the result as a ByteArrayOutputStream.
         * The stream will be closed after reading.
         *
         * @param inputStream the PDF input stream with form fields; must not be {@code null}
         * @return a ByteArrayOutputStream containing the filled PDF
         * @throws NullPointerException if inputStream is {@code null}
         * @throws IllegalArgumentException if the PDF has no form
         * @throws IOException if an I/O error occurs during form filling
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public ByteArrayOutputStream fill(InputStream inputStream) throws IOException {
            Objects.requireNonNull(inputStream, "inputStream must not be null");

            try (inputStream; PDDocument document = Loader.loadPDF(inputStream.readAllBytes())) {
                return fillDocument(document);
            }
        }

        /**
         * Fills a PDF form from a byte array and returns the result as a ByteArrayOutputStream.
         *
         * @param pdfBytes the PDF content as byte array with form fields; must not be {@code null}
         * @return a ByteArrayOutputStream containing the filled PDF
         * @throws NullPointerException if pdfBytes is {@code null}
         * @throws IllegalArgumentException if the PDF has no form
         * @throws IOException if an I/O error occurs during form filling
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public ByteArrayOutputStream fill(byte[] pdfBytes) throws IOException {
            Objects.requireNonNull(pdfBytes, "pdfBytes must not be null");

            try (PDDocument document = Loader.loadPDF(pdfBytes)) {
                return fillDocument(document);
            }
        }

        /**
         * Fills a PDF form from a ByteArrayOutputStream and returns the result as a ByteArrayOutputStream.
         *
         * @param outputStream the ByteArrayOutputStream containing PDF data with form fields; must not be {@code null}
         * @return a ByteArrayOutputStream containing the filled PDF
         * @throws NullPointerException if outputStream is {@code null}
         * @throws IllegalArgumentException if the PDF has no form
         * @throws IOException if an I/O error occurs during form filling
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public ByteArrayOutputStream fill(ByteArrayOutputStream outputStream) throws IOException {
            Objects.requireNonNull(outputStream, "outputStream must not be null");
            return fill(outputStream.toByteArray());
        }

        /**
         * Fills a PDF form from a URL and returns the result as a ByteArrayOutputStream.
         *
         * @param url the URL pointing to the PDF document with form fields; must not be {@code null}
         * @return a ByteArrayOutputStream containing the filled PDF
         * @throws NullPointerException if url is {@code null}
         * @throws IllegalArgumentException if the PDF has no form
         * @throws IOException if an I/O error occurs during form filling
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public ByteArrayOutputStream fill(URL url) throws IOException {
            Objects.requireNonNull(url, "url must not be null");

            try (InputStream stream = url.openStream()) {
                return fill(stream);
            }
        }

        /**
         * Fills a PDF form using a ResourceProvider and returns the result as a ByteArrayOutputStream.
         *
         * @param resourceProvider the resource provider to use; must not be {@code null}
         * @param resourceName the name/path of the resource; must not be {@code null}
         * @return a ByteArrayOutputStream containing the filled PDF
         * @throws NullPointerException if resourceProvider or resourceName is {@code null}
         * @throws IllegalArgumentException if the resource cannot be found or the PDF has no form
         * @throws IOException if an I/O error occurs during form filling
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public ByteArrayOutputStream fill(EResourceProvider resourceProvider, String resourceName) throws IOException {
            Objects.requireNonNull(resourceProvider, "resourceProvider must not be null");
            Objects.requireNonNull(resourceName, "resourceName must not be null");

            URL resourceUrl = resourceProvider.getResource(resourceName);
            if (resourceUrl == null) {
                throw new IllegalArgumentException("Resource not found: " + resourceName);
            }

            logger.debug("Filling form from resource: {}", resourceName);
            return fill(resourceUrl);
        }

        /**
         * Fills a PDF form and writes the result directly to a file.
         * This is a convenience method equivalent to fill() + writing to file.
         *
         * @param inputFile the PDF file with form fields to fill; must not be {@code null}
         * @param outputFile the target file for the filled PDF; must not be {@code null}
         * @return the output file
         * @throws NullPointerException if inputFile or outputFile is {@code null}
         * @throws IllegalArgumentException if the PDF has no form
         * @throws IOException if an I/O error occurs during form filling or writing
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public File fillToFile(File inputFile, File outputFile) throws IOException {
            Objects.requireNonNull(outputFile, "outputFile must not be null");

            ByteArrayOutputStream filled = fill(inputFile);

            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                filled.writeTo(fos);
                logger.info("Filled PDF written to: {}", outputFile.getAbsolutePath());
                return outputFile;
            } catch (IOException e) {
                logger.error("Failed to write filled PDF to file: {}", e.getMessage(), e);
                throw new IOException("Failed to write filled PDF to file: " + e.getMessage(), e);
            }
        }

        /**
         * Fills a PDF form and writes the result directly to a file.
         *
         * @param inputPath the path to the PDF file with form fields; must not be {@code null}
         * @param outputPath the target path for the filled PDF; must not be {@code null}
         * @return the output file
         * @throws NullPointerException if inputPath or outputPath is {@code null}
         * @throws IllegalArgumentException if the PDF has no form
         * @throws IOException if an I/O error occurs during form filling or writing
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public File fillToFile(Path inputPath, Path outputPath) throws IOException {
            Objects.requireNonNull(inputPath, "inputPath must not be null");
            Objects.requireNonNull(outputPath, "outputPath must not be null");
            return fillToFile(inputPath.toFile(), outputPath.toFile());
        }

        /**
         * Fills a PDF form and writes the result directly to a file.
         *
         * @param inputPath the path to the PDF file with form fields; must not be {@code null}
         * @param outputPath the target path for the filled PDF; must not be {@code null}
         * @return the output file
         * @throws NullPointerException if inputPath or outputPath is {@code null}
         * @throws IllegalArgumentException if the PDF has no form
         * @throws IOException if an I/O error occurs during form filling or writing
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public File fillToFile(String inputPath, String outputPath) throws IOException {
            Objects.requireNonNull(inputPath, "inputPath must not be null");
            Objects.requireNonNull(outputPath, "outputPath must not be null");
            return fillToFile(new File(inputPath), new File(outputPath));
        }

        /**
         * Core form filling logic that populates fields in a PDDocument.
         *
         * @param document the document with form fields to fill
         * @return a ByteArrayOutputStream containing the filled PDF
         * @throws IOException if form filling fails
         */
        private ByteArrayOutputStream fillDocument(PDDocument document) throws IOException {
            validate();

            PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
            if (acroForm == null) {
                throw new IllegalArgumentException("PDF document does not contain a form (AcroForm)");
            }

            logger.info("Starting form filling with {} fields", fieldValues.size());

            try {
                // Get all available fields for validation
                Map<String, PDField> availableFields = new HashMap<>();
                for (PDField field : acroForm.getFields()) {
                    availableFields.put(field.getFullyQualifiedName(), field);
                }

                // Fill each field
                int filledCount = 0;
                for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
                    String fieldName = entry.getKey();
                    Object value = entry.getValue();

                    PDField field = availableFields.get(fieldName);

                    if (field == null) {
                        handleUnknownField(fieldName);
                        continue;
                    }

                    if (field.isReadOnly()) {
                        handleReadOnlyField(fieldName);
                        continue;
                    }

                    fillField(field, value);
                    filledCount++;
                }

                logger.info("Successfully filled {} out of {} fields", filledCount, fieldValues.size());

                // Flatten if requested
                if (flatten) {
                    logger.info("Flattening form");
                    acroForm.flatten();
                }

                // Save to output stream
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                document.save(outputStream);

                return outputStream;

            } catch (IOException e) {
                logger.error("Failed to fill form: {}", e.getMessage(), e);
                throw new IOException("Failed to fill PDF form: " + e.getMessage(), e);
            }
        }

        /**
         * Fills a specific field based on its type.
         *
         * @param field the field to fill
         * @param value the value to set
         * @throws IOException if setting the value fails
         */
        private void fillField(PDField field, Object value) throws IOException {
            String fieldName = field.getFullyQualifiedName();

            try {
                switch (field) {
                    case PDTextField pdTextField -> fillTextField(pdTextField, value);
                    case PDCheckBox pdCheckBox -> fillCheckBox(pdCheckBox, value);
                    case PDRadioButton pdRadioButton -> fillRadioButton(pdRadioButton, value);
                    case PDChoice pdChoice -> fillChoiceField(pdChoice, value);
                    default -> logger.warn("Unsupported field type for field '{}': {}",
                            fieldName, field.getClass().getSimpleName());
                }

                logger.debug("Filled field '{}' with value: {}", fieldName, value);

            } catch (IOException e) {
                logger.error("Failed to fill field '{}': {}", fieldName, e.getMessage());
                throw new IOException("Failed to fill field '" + fieldName + "': " + e.getMessage(), e);
            }
        }

        /**
         * Fills a text field.
         */
        private void fillTextField(PDTextField field, Object value) throws IOException {
            String textValue = convertToString(value);
            field.setValue(textValue);
        }

        /**
         * Fills a checkbox.
         */
        private void fillCheckBox(PDCheckBox field, Object value) throws IOException {
            if (value instanceof Boolean) {
                if ((Boolean) value) {
                    field.check();
                } else {
                    field.unCheck();
                }
            } else {
                throw new IllegalArgumentException(
                        "Checkbox field '" + field.getFullyQualifiedName() +
                                "' requires Boolean value, but got: " + value.getClass().getSimpleName()
                );
            }
        }

        /**
         * Fills a radio button.
         */
        private void fillRadioButton(PDRadioButton field, Object value) throws IOException {
            String stringValue = convertToString(value);
            field.setValue(stringValue);
        }

        /**
         * Fills a choice field (dropdown or list).
         */
        private void fillChoiceField(PDChoice field, Object value) throws IOException {
            String stringValue = convertToString(value);

            // Verify the value is in the list of options
            List<String> options = field.getOptions();
            if (!options.contains(stringValue)) {
                logger.warn("Value '{}' is not in the list of available options for field '{}': {}",
                        stringValue, field.getFullyQualifiedName(), options);
            }

            field.setValue(stringValue);
        }

        /**
         * Converts a value to String for text-based fields.
         */
        private String convertToString(Object value) {
            if (value == null) {
                return "";
            }
            return value.toString();
        }

        /**
         * Handles an unknown field name.
         */
        private void handleUnknownField(String fieldName) throws IOException {
            String message = "Field '" + fieldName + "' does not exist in the PDF form";

            if (ignoreUnknownFields) {
                logger.warn(message);
            } else {
                throw new IllegalArgumentException(message);
            }
        }

        /**
         * Handles a read-only field.
         */
        private void handleReadOnlyField(String fieldName) throws IOException {
            String message = "Field '" + fieldName + "' is read-only and cannot be modified";

            if (ignoreReadOnlyFields) {
                logger.warn(message);
            } else {
                throw new IllegalStateException(message);
            }
        }

        /**
         * Validates the builder configuration before filling.
         */
        private void validate() {
            if (fieldValues.isEmpty()) {
                logger.warn("No field values provided - form will not be modified");
            }
        }
    }
}