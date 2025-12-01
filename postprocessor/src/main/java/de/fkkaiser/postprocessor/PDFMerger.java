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
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Utility class for merging multiple PDF documents into a single PDF.
 * <p>
 * This class provides a builder pattern to add PDF documents from various sources
 * (files, byte arrays, input streams, PDDocument instances) and merge them into one.
 * </p>
 *
 * <h2>Usage Example:</h2>
 * <pre>{@code
 * PDFMerger.Builder builder = new PDFMerger.Builder();
 * builder.addDocument(new File("doc1.pdf"));
 * builder.addDocument(new File("doc2.pdf"));
 * ByteArrayOutputStream mergedPdf = builder.merge();
 * }</pre>
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
@PublicAPI
public class PDFMerger {

    private static final Logger logger = LoggerFactory.getLogger(PDFMerger.class);

    private PDFMerger() {
        // Private constructor to prevent instantiation --> use builder
    }

    /**
     * Creates a new PDFMerger builder instance.
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }


    /**
     * Builder class for constructing and merging PDF documents.
     */
    public static class Builder {

        private final List<PDDocument> documents = new ArrayList<>();
        public Builder() {
            // Initialize builder
        }

        /**
         * Adds a PDF document from a file.
         *
         * @param document the PDF file to add
         * @return the builder instance for chaining
         * @throws IOException if an error occurs while loading the PDF
         * @throws IllegalArgumentException if the file does not exist or is not a valid file
         * @throws NullPointerException if the document is null
         */
        @PublicAPI
        public Builder addDocument(File document) throws IOException {
            Objects.requireNonNull(document, "Document file must not be null");

            if(!document.exists()) {
                throw new IllegalArgumentException("Document file does not exist: " + document.getAbsolutePath());
            }
            if(!document.isFile()) {
                throw new IllegalArgumentException("Provided path is not a file: " + document.getAbsolutePath());
            }

            PDDocument pdDocument = Loader.loadPDF(document);
            this.documents.add(pdDocument);
            logger.debug("Added PDF document {}", document.getAbsolutePath());
            return this;
        }

        /**
         * Adds a PDF document from a Path.
         * @param document the Path to the PDF document
         * @return the builder instance for chaining
         * @throws IOException if an error occurs while loading the PDF
         * @throws NullPointerException if the document is null
         *
         */
        @PublicAPI
        public Builder addDocument(Path document) throws IOException {
            Objects.requireNonNull(Objects.requireNonNull(document, "Document file must not be null"));
            return  addDocument(document.toFile());
        }

        /**
         * Adds a PDF document from a file path.
         * @param documentPath the file path to the PDF document
         * @return the builder instance for chaining
         * @throws IOException if an error occurs while loading the PDF
         * @throws NullPointerException if the documentPath is null
         */
        @PublicAPI
        public Builder addDocument(String documentPath) throws IOException {
            Objects.requireNonNull(documentPath, "Document path must not be null");
            return addDocument(new File(documentPath));
        }

        /**
         * Adds a PDF document from an InputStream.
         * @param inputStream the InputStream containing the PDF data
         * @return the builder instance for chaining
         * @throws IOException if an error occurs while reading or loading the PDF
         * @throws NullPointerException if the inputStream is null
         */
        @PublicAPI
        public Builder addDocument(InputStream inputStream) throws IOException {
            Objects.requireNonNull(Objects.requireNonNull(inputStream, "Input stream must not be null"));

            try (inputStream) {
               return addDocument(inputStream.readAllBytes());
            }
        }

        /**
         * Adds a PDF document from a byte array.
         * @param pdfBytes the byte array containing the PDF data
         * @return the builder instance for chaining
         * @throws IOException if an error occurs while loading the PDF
         * @throws NullPointerException if the pdfBytes is null
         */
        @PublicAPI
        public Builder addDocument(byte[] pdfBytes) throws IOException {
            Objects.requireNonNull(pdfBytes, "Document byte array must not be null");

            PDDocument pdDocument = Loader.loadPDF(pdfBytes);
            this.documents.add(pdDocument);
            logger.debug("Added PDF document from byte array");
            return this;
        }

        /**
         * Adds a PDF document from a ByteArrayInputStream.
         * @param outputStream the ByteArrayInputStream containing the PDF data
         * @return the builder instance for chaining
         * @throws IOException if an error occurs while reading or loading the PDF
         * @throws NullPointerException if the outputStream is null
         */
        @PublicAPI
        public Builder addDocument(ByteArrayInputStream outputStream) throws IOException {
            Objects.requireNonNull(Objects.requireNonNull(outputStream, "ByteArrayInputStream must not be null"));
            return addDocument(outputStream.readAllBytes());
        }

        /**
         * Adds a PDF document from an existing PDDocument instance.
         * @param document the PDDocument instance to add
         * @return the builder instance for chaining
         * @throws NullPointerException if the document is null
         */
        @PublicAPI
        public Builder addDocument(PDDocument document) {
            Objects.requireNonNull(document, "PDDocument must not be null");
            this.documents.add(document);
            logger.debug("Added PDDocument instance");
            return this;
        }

        /**
         * Merges the added PDF documents into a single PDF.
         * @return a ByteArrayOutputStream containing the merged PDF data
         * @throws IOException if an error occurs during the merge process
         * @throws IllegalStateException if less than two documents have been added
         */
        @PublicAPI
        public ByteArrayOutputStream merge() throws IOException {

            validate();

            logger.info("Merging {} PDF documents.", this.documents.size());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (PDDocument document = new PDDocument()) {
                try {
                    PDFMergerUtility merger = new PDFMergerUtility();

                    for (PDDocument doc : this.documents) {
                        merger.appendDocument(document, doc);
                    }

                    document.save(baos);
                    logger.info("Successfully merged PDF documents with size {} bytes.", baos.size());
                    return baos;
                } catch (IOException e) {
                    logger.error("Failed to merge documents: {}", e.getMessage());
                    throw new IOException("Failed to merge PDF documents", e);
                }
            } finally {

                for (PDDocument doc : this.documents) {
                    try {
                        doc.close();
                    } catch (IOException e) {
                        logger.warn("Failed to close PDDocument: {}", e.getMessage());
                    }
                }
                documents.clear();
            }
        }

        public File mergeToFile(File outputFile) throws IOException {
            Objects.requireNonNull(outputFile, "Output file must not be null");

            ByteArrayOutputStream baos = this.merge();

            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                baos.writeTo(fos);
                logger.info("Successfully wrote merged PDF to file: {}", outputFile.getAbsolutePath());
                return outputFile;
            } catch (IOException e) {
                logger.error("Failed to write merged PDF to file: {}", e.getMessage());
                throw new IOException("Failed to write merged PDF to file", e);
            }
        }

        /**
         * Merges the added PDF documents and writes the result to the specified file path.
         * @param outputFilePath the file path where the merged PDF should be saved
         * @return the File object representing the merged PDF
         * @throws IOException if an error occurs during the merge or file writing process
         * @throws NullPointerException if the outputFilePath is null
         */
        @PublicAPI
        public File mergeToFile(String outputFilePath) throws IOException {
            Objects.requireNonNull(outputFilePath, "Output file path must not be null");
            return mergeToFile(new File(outputFilePath));
        }

        /**
         * Adds a PDF document from a resource provided by an EResourceProvider.
         *
         * @param resourceProvider the resource provider to load the PDF from
         * @param resourceName     the name/path of the resource
         * @return the builder instance for chaining
         * @throws IOException              if an error occurs while loading the PDF
         * @throws IllegalArgumentException if the resource is not found
         * @throws NullPointerException     if the resourceProvider or resourceName is null
         */
        @PublicAPI
        public Builder addDocument(EResourceProvider resourceProvider, String resourceName) throws IOException {
            Objects.requireNonNull(resourceProvider, "resourceProvider must not be null");
            Objects.requireNonNull(resourceName, "resourceName must not be null");

            URL resourceUrl = resourceProvider.getResource(resourceName);
            if (resourceUrl == null) {
                throw new IllegalArgumentException("Resource not found: " + resourceName);
            }

            logger.debug("Adding document from resource: {}", resourceName);
            return addDocument(resourceUrl.openStream());
        }



        private void validate() {
            if(this.documents.size() < 2) {
                throw new IllegalStateException("At least two documents are required to perform a merge.");
            }
        }

    }

}
