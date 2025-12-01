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

import de.fkkaiser.api.utils.EResourceProvider;
import de.fkkaiser.model.annotation.PublicAPI;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Service for encrypting PDF documents with password protection and access permissions.
 *
 * <p>This class provides a fluent API for applying encryption to PDF documents, supporting
 * both user passwords (for opening the document) and owner passwords (for modifying permissions).
 * The encryptor uses 256-bit AES encryption for maximum security.</p>
 *
 * <p><b>Usage Example - Basic Encryption:</b></p>
 * <pre>{@code
 * ByteArrayOutputStream encrypted = PdfEncryptor.builder()
 *     .withUserPassword("user123")
 *     .withOwnerPassword("owner456")
 *     .encrypt(new File("input.pdf"));
 *
 * // Save to file
 * Files.write(Path.of("encrypted.pdf"), encrypted.toByteArray());
 * }</pre>
 *
 * <p><b>Usage Example - With Permissions:</b></p>
 * <pre>{@code
 * PdfEncryptor.builder()
 *     .withUserPassword("view123")
 *     .withOwnerPassword("admin456")
 *     .allowPrinting(true)
 *     .allowCopying(false)
 *     .allowModification(false)
 *     .encryptToFile(inputPdf, outputPdf);
 * }</pre>
 *
 * <p><b>Usage Example - With ResourceProvider:</b></p>
 * <pre>{@code
 * EResourceProvider provider = new EClasspathResourceProvider();
 *
 * ByteArrayOutputStream encrypted = PdfEncryptor.builder()
 *     .withUserPassword("secret")
 *     .encrypt(provider, "templates/document.pdf");
 * }</pre>
 *
 * <p><b>Security Notes:</b></p>
 * <ul>
 *   <li>Uses 256-bit AES encryption (industry standard)</li>
 *   <li>User password is required to open the document</li>
 *   <li>Owner password is required to change permissions</li>
 *   <li>If only user password is set, owner password defaults to user password</li>
 * </ul>
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 * @since 0.10.0
 */
@PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
public class PDFEncryptor {

    private static final Logger logger = LoggerFactory.getLogger(PDFEncryptor.class);

    /** Default encryption key length: 256-bit AES */
    private static final int DEFAULT_KEY_LENGTH = 256;

    private PDFEncryptor() {
        // Private constructor - use builder()
    }

    /**
     * Creates a new builder for configuring and executing PDF encryption operations.
     *
     * @return a new Builder instance
     */
    @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for configuring PDF encryption operations.
     */
    @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
    public static class Builder {
        private String userPassword;
        private String ownerPassword;
        private int keyLength = DEFAULT_KEY_LENGTH;

        // Access permissions (default: all restricted)
        private boolean allowPrinting = false;
        private boolean allowModification = false;
        private boolean allowCopying = false;
        private boolean allowAnnotations = false;
        private boolean allowFormFilling = false;
        private boolean allowAccessibilityExtraction = true; // Screen readers should work
        private boolean allowAssembly = false;

        private Builder() {
        }

        /**
         * Sets the user password required to open the document.
         *
         * @param password the user password; must not be {@code null} or empty
         * @return this builder for method chaining
         * @throws NullPointerException if password is {@code null}
         * @throws IllegalArgumentException if password is empty
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public Builder withUserPassword(String password) {
            Objects.requireNonNull(password, "userPassword must not be null");
            if (password.trim().isEmpty()) {
                throw new IllegalArgumentException("User password cannot be empty");
            }
            this.userPassword = password;
            logger.debug("User password set");
            return this;
        }

        /**
         * Sets the owner password required to change document permissions.
         * If not set, the user password will be used as owner password.
         *
         * @param password the owner password; must not be {@code null} or empty
         * @return this builder for method chaining
         * @throws NullPointerException if password is {@code null}
         * @throws IllegalArgumentException if password is empty
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public Builder withOwnerPassword(String password) {
            Objects.requireNonNull(password, "ownerPassword must not be null");
            if (password.trim().isEmpty()) {
                throw new IllegalArgumentException("Owner password cannot be empty");
            }
            this.ownerPassword = password;
            logger.debug("Owner password set");
            return this;
        }

        /**
         * Sets the encryption key length.
         * Default is 256-bit AES encryption.
         *
         * @param keyLength the key length in bits (40, 128, or 256)
         * @return this builder for method chaining
         * @throws IllegalArgumentException if keyLength is not 40, 128, or 256
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public Builder withKeyLength(int keyLength) {
            if (keyLength != 40 && keyLength != 128 && keyLength != 256) {
                throw new IllegalArgumentException(
                        "Key length must be 40, 128, or 256 bits, but was: " + keyLength
                );
            }
            this.keyLength = keyLength;
            logger.debug("Encryption key length set to: {} bits", keyLength);
            return this;
        }

        /**
         * Sets whether printing is allowed.
         * Default is {@code false}.
         *
         * @param allow {@code true} to allow printing
         * @return this builder for method chaining
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public Builder allowPrinting(boolean allow) {
            this.allowPrinting = allow;
            return this;
        }

        /**
         * Sets whether modification of the document is allowed.
         * Default is {@code false}.
         *
         * @param allow {@code true} to allow modification
         * @return this builder for method chaining
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public Builder allowModification(boolean allow) {
            this.allowModification = allow;
            return this;
        }

        /**
         * Sets whether copying text and graphics is allowed.
         * Default is {@code false}.
         *
         * @param allow {@code true} to allow copying
         * @return this builder for method chaining
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public Builder allowCopying(boolean allow) {
            this.allowCopying = allow;
            return this;
        }

        /**
         * Sets whether adding or modifying annotations is allowed.
         * Default is {@code false}.
         *
         * @param allow {@code true} to allow annotations
         * @return this builder for method chaining
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public Builder allowAnnotations(boolean allow) {
            this.allowAnnotations = allow;
            return this;
        }

        /**
         * Sets whether filling in form fields is allowed.
         * Default is {@code false}.
         *
         * @param allow {@code true} to allow form filling
         * @return this builder for method chaining
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public Builder allowFormFilling(boolean allow) {
            this.allowFormFilling = allow;
            return this;
        }

        /**
         * Sets whether content extraction for accessibility purposes is allowed.
         * This should typically be {@code true} to allow screen readers to function.
         * Default is {@code true}.
         *
         * @param allow {@code true} to allow accessibility extraction
         * @return this builder for method chaining
         */
        @PublicAPI
        public Builder allowAccessibilityExtraction(boolean allow) {
            this.allowAccessibilityExtraction = allow;
            return this;
        }

        /**
         * Sets whether document assembly (inserting, rotating, or deleting pages) is allowed.
         * Default is {@code false}.
         *
         * @param allow {@code true} to allow assembly
         * @return this builder for method chaining
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public Builder allowAssembly(boolean allow) {
            this.allowAssembly = allow;
            return this;
        }


        /**
         * Sets all permissions to allow everything.
         * This is a convenience method equivalent to calling all allow* methods with {@code true}.
         *
         * @return this builder for method chaining
         */
        @PublicAPI
        public Builder allowAll() {
            this.allowPrinting = true;
            this.allowModification = true;
            this.allowCopying = true;
            this.allowAnnotations = true;
            this.allowFormFilling = true;
            this.allowAccessibilityExtraction = true;
            this.allowAssembly = true;
            logger.debug("All permissions enabled");
            return this;
        }

        /**
         * Sets all permissions to deny everything (except accessibility extraction).
         * This is a convenience method equivalent to calling all allow* methods with {@code false}.
         * Note: Accessibility extraction remains {@code true} to support screen readers.
         *
         * @return this builder for method chaining
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public Builder denyAll() {
            this.allowPrinting = false;
            this.allowModification = false;
            this.allowCopying = false;
            this.allowAnnotations = false;
            this.allowFormFilling = false;
            this.allowAccessibilityExtraction = true; // Keep true for accessibility
            this.allowAssembly = false;
            logger.debug("All permissions disabled (except accessibility)");
            return this;
        }

        /**
         * Encrypts a PDF document from a file and returns the result as a ByteArrayOutputStream.
         *
         * @param inputFile the PDF file to encrypt; must not be {@code null} and must exist
         * @return a ByteArrayOutputStream containing the encrypted PDF
         * @throws NullPointerException if inputFile is {@code null}
         * @throws IllegalArgumentException if inputFile does not exist or is not a file
         * @throws IllegalStateException if no user password is set
         * @throws IOException if an I/O error occurs during encryption
         */
        @PublicAPI
        public ByteArrayOutputStream encrypt(File inputFile) throws IOException {
            Objects.requireNonNull(inputFile, "inputFile must not be null");
            if (!inputFile.exists()) {
                throw new IllegalArgumentException("Input file does not exist: " + inputFile.getAbsolutePath());
            }
            if (!inputFile.isFile()) {
                throw new IllegalArgumentException("Input is not a file: " + inputFile.getAbsolutePath());
            }

            try (PDDocument document = Loader.loadPDF(inputFile)) {
                return encryptDocument(document);
            }
        }

        /**
         * Encrypts a PDF document from a Path and returns the result as a ByteArrayOutputStream.
         *
         * @param inputPath the path to the PDF file; must not be {@code null}
         * @return a ByteArrayOutputStream containing the encrypted PDF
         * @throws NullPointerException if inputPath is {@code null}
         * @throws IllegalStateException if no user password is set
         * @throws IOException if an I/O error occurs during encryption
         */
        @PublicAPI
        public ByteArrayOutputStream encrypt(Path inputPath) throws IOException {
            Objects.requireNonNull(inputPath, "inputPath must not be null");
            return encrypt(inputPath.toFile());
        }

        /**
         * Encrypts a PDF document from a file path string and returns the result as a ByteArrayOutputStream.
         *
         * @param inputPath the path to the PDF file; must not be {@code null}
         * @return a ByteArrayOutputStream containing the encrypted PDF
         * @throws NullPointerException if inputPath is {@code null}
         * @throws IllegalStateException if no user password is set
         * @throws IOException if an I/O error occurs during encryption
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public ByteArrayOutputStream encrypt(String inputPath) throws IOException {
            Objects.requireNonNull(inputPath, "inputPath must not be null");
            return encrypt(new File(inputPath));
        }

        /**
         * Encrypts a PDF document from an InputStream and returns the result as a ByteArrayOutputStream.
         * The stream will be closed after reading.
         *
         * @param inputStream the PDF input stream; must not be {@code null}
         * @return a ByteArrayOutputStream containing the encrypted PDF
         * @throws NullPointerException if inputStream is {@code null}
         * @throws IllegalStateException if no user password is set
         * @throws IOException if an I/O error occurs during encryption
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public ByteArrayOutputStream encrypt(InputStream inputStream) throws IOException {
            Objects.requireNonNull(inputStream, "inputStream must not be null");

            try (inputStream; PDDocument document = Loader.loadPDF(inputStream.readAllBytes())) {
                return encryptDocument(document);
            }
        }

        /**
         * Encrypts a PDF document from a byte array and returns the result as a ByteArrayOutputStream.
         *
         * @param pdfBytes the PDF content as byte array; must not be {@code null}
         * @return a ByteArrayOutputStream containing the encrypted PDF
         * @throws NullPointerException if pdfBytes is {@code null}
         * @throws IllegalStateException if no user password is set
         * @throws IOException if an I/O error occurs during encryption
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public ByteArrayOutputStream encrypt(byte[] pdfBytes) throws IOException {
            Objects.requireNonNull(pdfBytes, "pdfBytes must not be null");

            try (PDDocument document = Loader.loadPDF(pdfBytes)) {
                return encryptDocument(document);
            }
        }

        /**
         * Encrypts a PDF document from a ByteArrayOutputStream and returns the result as a ByteArrayOutputStream.
         *
         * @param outputStream the ByteArrayOutputStream containing PDF data; must not be {@code null}
         * @return a ByteArrayOutputStream containing the encrypted PDF
         * @throws NullPointerException if outputStream is {@code null}
         * @throws IllegalStateException if no user password is set
         * @throws IOException if an I/O error occurs during encryption
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public ByteArrayOutputStream encrypt(ByteArrayOutputStream outputStream) throws IOException {
            Objects.requireNonNull(outputStream, "outputStream must not be null");
            return encrypt(outputStream.toByteArray());
        }

        /**
         * Encrypts a PDF document from a URL and returns the result as a ByteArrayOutputStream.
         *
         * @param url the URL pointing to the PDF document; must not be {@code null}
         * @return a ByteArrayOutputStream containing the encrypted PDF
         * @throws NullPointerException if url is {@code null}
         * @throws IllegalStateException if no user password is set
         * @throws IOException if an I/O error occurs during encryption
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public ByteArrayOutputStream encrypt(URL url) throws IOException {
            Objects.requireNonNull(url, "url must not be null");

            try (InputStream stream = url.openStream()) {
                return encrypt(stream);
            }
        }

        /**
         * Encrypts a PDF document using a ResourceProvider and returns the result as a ByteArrayOutputStream.
         *
         * @param resourceProvider the resource provider to use; must not be {@code null}
         * @param resourceName the name/path of the resource; must not be {@code null}
         * @return a ByteArrayOutputStream containing the encrypted PDF
         * @throws NullPointerException if resourceProvider or resourceName is {@code null}
         * @throws IllegalArgumentException if the resource cannot be found
         * @throws IllegalStateException if no user password is set
         * @throws IOException if an I/O error occurs during encryption
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public ByteArrayOutputStream encrypt(EResourceProvider resourceProvider, String resourceName) throws IOException {
            Objects.requireNonNull(resourceProvider, "resourceProvider must not be null");
            Objects.requireNonNull(resourceName, "resourceName must not be null");

            URL resourceUrl = resourceProvider.getResource(resourceName);
            if (resourceUrl == null) {
                throw new IllegalArgumentException("Resource not found: " + resourceName);
            }

            logger.debug("Encrypting document from resource: {}", resourceName);
            return encrypt(resourceUrl);
        }

        /**
         * Encrypts a PDF document and writes the result directly to a file.
         * This is a convenience method equivalent to encrypt() + writing to file.
         *
         * @param inputFile the PDF file to encrypt; must not be {@code null}
         * @param outputFile the target file for the encrypted PDF; must not be {@code null}
         * @return the output file
         * @throws NullPointerException if inputFile or outputFile is {@code null}
         * @throws IllegalStateException if no user password is set
         * @throws IOException if an I/O error occurs during encryption or writing
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public File encryptToFile(File inputFile, File outputFile) throws IOException {
            Objects.requireNonNull(outputFile, "outputFile must not be null");

            ByteArrayOutputStream encrypted = encrypt(inputFile);

            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                encrypted.writeTo(fos);
                logger.info("Encrypted PDF written to: {}", outputFile.getAbsolutePath());
                return outputFile;
            } catch (IOException e) {
                logger.error("Failed to write encrypted PDF to file: {}", e.getMessage(), e);
                throw new IOException("Failed to write encrypted PDF to file: " + e.getMessage(), e);
            }
        }

        /**
         * Encrypts a PDF document and writes the result directly to a file.
         *
         * @param inputPath the path to the PDF file to encrypt; must not be {@code null}
         * @param outputPath the target path for the encrypted PDF; must not be {@code null}
         * @return the output file
         * @throws NullPointerException if inputPath or outputPath is {@code null}
         * @throws IllegalStateException if no user password is set
         * @throws IOException if an I/O error occurs during encryption or writing
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public File encryptToFile(Path inputPath, Path outputPath) throws IOException {
            Objects.requireNonNull(inputPath, "inputPath must not be null");
            Objects.requireNonNull(outputPath, "outputPath must not be null");
            return encryptToFile(inputPath.toFile(), outputPath.toFile());
        }

        /**
         * Encrypts a PDF document and writes the result directly to a file.
         *
         * @param inputPath the path to the PDF file to encrypt; must not be {@code null}
         * @param outputPath the target path for the encrypted PDF; must not be {@code null}
         * @return the output file
         * @throws NullPointerException if inputPath or outputPath is {@code null}
         * @throws IllegalStateException if no user password is set
         * @throws IOException if an I/O error occurs during encryption or writing
         */
        @PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
        public File encryptToFile(String inputPath, String outputPath) throws IOException {
            Objects.requireNonNull(inputPath, "inputPath must not be null");
            Objects.requireNonNull(outputPath, "outputPath must not be null");
            return encryptToFile(new File(inputPath), new File(outputPath));
        }

        /**
         * Core encryption logic that applies the protection policy to a PDDocument.
         *
         * @param document the document to encrypt
         * @return a ByteArrayOutputStream containing the encrypted PDF
         * @throws IOException if encryption fails
         */
        private ByteArrayOutputStream encryptDocument(PDDocument document) throws IOException {
            validate();

            logger.info("Starting encryption with {}-bit key", keyLength);

            try {
                // Create access permissions
                StandardProtectionPolicy policy = getStandardProtectionPolicy();

                // Apply encryption
                document.protect(policy);

                // Save to output stream
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                document.save(outputStream);

                logger.info("Successfully encrypted document");
                return outputStream;

            } catch (IOException e) {
                logger.error("Failed to encrypt document: {}", e.getMessage(), e);
                throw new IOException("Failed to encrypt PDF document: " + e.getMessage(), e);
            }
        }

        private StandardProtectionPolicy getStandardProtectionPolicy() {
            AccessPermission accessPermission = getAccessPermission();

            // Use owner password if set, otherwise use user password
            String effectiveOwnerPassword = (ownerPassword != null) ? ownerPassword : userPassword;

            // Create protection policy
            StandardProtectionPolicy policy = new StandardProtectionPolicy(
                    effectiveOwnerPassword,
                    userPassword,
                    accessPermission
            );
            policy.setEncryptionKeyLength(keyLength);
            return policy;
        }

        private AccessPermission getAccessPermission() {
            AccessPermission accessPermission = new AccessPermission();
            accessPermission.setCanPrint(allowPrinting);
            accessPermission.setCanModify(allowModification);
            accessPermission.setCanExtractContent(allowCopying);
            accessPermission.setCanModifyAnnotations(allowAnnotations);
            accessPermission.setCanFillInForm(allowFormFilling);
            accessPermission.setCanExtractForAccessibility(allowAccessibilityExtraction);
            accessPermission.setCanAssembleDocument(allowAssembly);
            return accessPermission;
        }

        /**
         * Validates the builder configuration before encryption.
         *
         * @throws IllegalStateException if configuration is invalid
         */
        private void validate() {
            if (userPassword == null || userPassword.trim().isEmpty()) {
                throw new IllegalStateException("User password must be set using withUserPassword()");
            }
        }
    }
}