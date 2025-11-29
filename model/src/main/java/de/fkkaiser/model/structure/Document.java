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
package de.fkkaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.structure.builder.DocumentBuilder;

import java.util.List;

/**
 * Represents the root object of a document structure for PDF generation.
 *
 * <p>The Document is the top-level container that holds all information needed to generate
 * a PDF file. It contains metadata about the document, optional references to external
 * resources (fonts, images), and one or more page sequences that define the document's
 * content and layout.</p>
 *
 * <p><b>Document Structure Hierarchy:</b></p>
 * <pre>
 * Document (root)
 *   ├── Metadata (required)
 *   │     └── Title, author, language, etc.
 *   ├── InternalAddresses (optional)
 *   │     └── Paths to font and image dictionaries
 *   └── PageSequences (1 or more)
 *         └── PageSequence
 *               ├── Header (optional)
 *               ├── Body (optional but recommended)
 *               └── Footer (optional)
 *                     └── ContentArea
 *                           └── Elements (Paragraphs, Headlines, Tables, etc.)
 * </pre>
 *
 * <p><b>Core Components:</b></p>
 * <table>
 *   <caption>Document Components</caption>
 *   <tr>
 *     <th>Component</th>
 *     <th>Required</th>
 *     <th>Purpose</th>
 *     <th>Default</th>
 *   </tr>
 *   <tr>
 *     <td>Metadata</td>
 *     <td>Yes</td>
 *     <td>Document properties (title, author, language)</td>
 *     <td>N/A (must be provided)</td>
 *   </tr>
 *   <tr>
 *     <td>InternalAddresses</td>
 *     <td>No</td>
 *     <td>Paths to font and image dictionaries</td>
 *     <td>null (no external resources)</td>
 *   </tr>
 *   <tr>
 *     <td>PageSequences</td>
 *     <td>Yes</td>
 *     <td>Content sections with different layouts</td>
 *     <td>Empty list (valid but produces empty PDF)</td>
 *   </tr>
 * </table>
 *
 * <p><b>JSON Representation:</b></p>
 * <pre>{@code
 * {
 *   "metadata": {
 *     "title": "My Document",
 *     "author": "John Doe",
 *     "language": "en-US"
 *   },
 *   "internal-addresses": {
 *     "font-dictionary": "fonts",
 *     "image-dictionary": "images"
 *   },
 *   "page-sequences": [
 *     {
 *       "style-class": "title-page",
 *       "body": {
 *         "elements": [...]
 *       }
 *     },
 *     {
 *       "style-class": "main-content",
 *       "header": {
 *         "elements": [...]
 *       },
 *       "body": {
 *         "elements": [...]
 *       },
 *       "footer": {
 *         "elements": [...]
 *       }
 *     }
 *   ]
 * }
 * }</pre>
 *
 * <p><b>Usage Example 1 - Simple Document:</b></p>
 * <pre>{@code
 * // Create a simple single-page document
 * Metadata metadata = new Metadata("My First Document");
 *
 * ContentArea body = new ContentArea();
 * body.addElement(new Headline("title", "Welcome", 1));
 * body.addElement(new Paragraph("body-text", "This is a simple document."));
 *
 * PageSequence mainPage = new PageSequence("main-page", body, null, null);
 *
 * Document document = new Document(null, metadata, List.of(mainPage));
 * }</pre>
 *
 * <p><b>Usage Example 2 - Document with Builder (Recommended):</b></p>
 * <pre>{@code
 * // Create document using the fluent builder API
 * Document document = Document.builder(
 *     Metadata.builder("Annual Report 2024")
 *         .author("Finance Department")
 *         .language("en-US")
 *         .build()
 * )
 * .withInternalAddresses(new InternalAddresses(
 *     "fonts",
 *     "images"
 * ))
 * .addPageSequence(
 *     PageSequence.builder("title-page")
 *         .body(titlePageBody)
 *         .build()
 * )
 * .addPageSequence(
 *     PageSequence.builder("main-content")
 *         .header(headerContent)
 *         .body(mainContent)
 *         .footer(footerContent)
 *         .build()
 * )
 * .build();
 * }</pre>
 *
 * <p><b>Usage Example 3 - Multi-Section Document:</b></p>
 * <pre>{@code
 * Metadata metadata = Metadata.builder("Technical Manual")
 *     .author("Engineering Team")
 *     .subject("Product Documentation")
 *     .language("en-US")
 *     .build();
 *
 * // Title page (no header/footer)
 * PageSequence titlePage = PageSequence.builder("title-page")
 *     .body(createTitlePage())
 *     .build();
 *
 * // Table of contents
 * PageSequence tocPage = PageSequence.builder("toc-page")
 *     .body(createTableOfContents())
 *     .build();
 *
 * // Main content (with header and footer)
 * PageSequence mainContent = PageSequence.builder("main-content")
 *     .header(createHeader())
 *     .body(createMainContent())
 *     .footer(createFooter())
 *     .build();
 *
 * // Assemble document
 * Document document = Document.builder(metadata)
 *     .addPageSequence(titlePage)
 *     .addPageSequence(tocPage)
 *     .addPageSequence(mainContent)
 *     .build();
 * }</pre>
 *
 * <p><b>Usage Example 4 - Document with Custom Fonts and Images:</b></p>
 * <pre>{@code
 * InternalAddresses addresses = new InternalAddresses(
 *     "resources/fonts",
 *     "resources/images"
 * );
 *
 * Document document = Document.builder(metadata)
 *     .withInternalAddresses(addresses)
 *     .addPageSequence(contentPage)
 *     .build();
 * }</pre>
 *
 * <p><b>Usage Example 5 - Minimal Valid Document:</b></p>
 * <pre>{@code
 * // Minimal document (technically valid but produces empty PDF)
 * Document document = new Document(
 *     null,                           // No external resources
 *     new Metadata("Empty Document"), // Required metadata
 *     List.of()                       // No page sequences (empty PDF)
 * );
 * }</pre>
 *
 * <p><b>Page Sequences:</b></p>
 * Documents can contain multiple page sequences, each with its own layout style.
 * Common use cases include:
 * <ul>
 *   <li><b>Title Page:</b> First page with special formatting, no headers/footers</li>
 *   <li><b>Table of Contents:</b> Separate page sequence with TOC-specific styling</li>
 *   <li><b>Main Content:</b> Primary content with consistent headers and footers</li>
 *   <li><b>Appendices:</b> Additional sections with different page numbering or layout</li>
 * </ul>
 *
 * <p><b>Internal Addresses:</b></p>
 * The {@code internalAddresses} field is optional and only needed when:
 * <ul>
 *   <li>Using custom fonts (not system fonts)</li>
 *   <li>Including images in the document</li>
 *   <li>Both fonts and images require external dictionary files</li>
 * </ul>
 *
 * If {@code null}, the PDF generator will use default system fonts and no images.
 *
 * <p><b>Validation:</b></p>
 * The compact constructor performs minimal validation:
 * <ul>
 *   <li>If {@code pageSequences} is {@code null}, it is replaced with an empty list</li>
 *   <li>No validation is performed on {@code metadata} or {@code internalAddresses}</li>
 *   <li>An empty {@code pageSequences} list is valid but produces an empty PDF</li>
 * </ul>
 *
 * <p><b>Immutability:</b></p>
 * As a record, Document instances are immutable after construction. The pageSequences
 * list is copied to prevent external modification. To create variations of a document,
 * construct a new Document instance with the modified components.
 *
 * <p><b>Integration with PDF Generation:</b></p>
 * <pre>{@code
 * // Create document structure
 * Document document = Document.builder(metadata)
 *     .addPageSequence(...)
 *     .build();
 *
 * // Generate PDF (using the API module)
 * PdfGenerationFacade pdfGenerator = new PdfGenerationFacade();
 * byte[] pdfBytes = pdfGenerator.generatePdf(document, styleMap, fontList);
 * }</pre>
 *
 * <p><b>Thread Safety:</b></p>
 * Document instances are immutable and thread-safe after construction. However,
 * mutable components (like ContentArea) within the document structure may not be
 * thread-safe, so care should be taken when sharing documents across threads.
 *
 * @param internalAddresses optional paths to external resource dictionaries (fonts, images);
 *                          may be {@code null} if no external resources are used
 * @param metadata          document metadata including title, author, and language;
 *                          must not be {@code null}
 * @param pageSequences     list of page sequences defining document content and layout;
 *                          may be {@code null} (converted to empty list) or empty
 *
 * @author FK Kaiser
 * @version 1.0
 * @see Metadata
 * @see InternalAddresses
 * @see PageSequence
 * @see DocumentBuilder
 */
public record Document(
        @JsonProperty("internal-addresses")
        InternalAddresses internalAddresses,

        @JsonProperty("metadata")
        Metadata metadata,

        @JsonProperty("page-sequences")
        List<PageSequence> pageSequences
) {

    /**
     * Compact constructor that validates and normalizes document parameters.
     *
     * <p>This constructor is automatically invoked whenever a Document instance is created.
     * It ensures that the {@code pageSequences} list is never {@code null} by replacing
     * {@code null} values with an empty immutable list.</p>
     *
     * <p><b>Validation Rules:</b></p>
     * <ul>
     *   <li>If {@code pageSequences} is {@code null}, it is replaced with {@code List.of()}</li>
     *   <li>{@code metadata} is not validated (may be {@code null}, though not recommended)</li>
     *   <li>{@code internalAddresses} is not validated (may be {@code null})</li>
     * </ul>
     *
     * <p><b>Note:</b> While the constructor allows {@code metadata} to be {@code null},
     * this is not recommended as it may cause issues during PDF generation. Consider
     * providing at least minimal metadata with a document title.</p>
     */
    public Document {
        if (pageSequences == null) {
            pageSequences = List.of();
        }
    }

    /**
     * Creates and returns a new {@link DocumentBuilder} for fluent document construction.
     *
     * <p>The builder pattern is the recommended way to create Document instances, as it
     * provides a clear, readable API for constructing complex document structures with
     * multiple page sequences and optional components.</p>
     *
     * <p><b>Builder Benefits:</b></p>
     * <ul>
     *   <li><b>Readability:</b> Clear, self-documenting code</li>
     *   <li><b>Flexibility:</b> Add page sequences incrementally</li>
     *   <li><b>Optional Components:</b> Easy to include or omit internal addresses</li>
     *   <li><b>Type Safety:</b> Compile-time verification of document structure</li>
     * </ul>
     *
     * <p><b>Usage Example - Simple Document:</b></p>
     * <pre>{@code
     * Document doc = Document.builder(new Metadata("My Document"))
     *     .addPageSequence(mainPage)
     *     .build();
     * }</pre>
     *
     * <p><b>Usage Example - Complex Document:</b></p>
     * <pre>{@code
     * Document doc = Document.builder(
     *     Metadata.builder("Annual Report")
     *         .author("Finance Team")
     *         .language("en-US")
     *         .build()
     * )
     * .withInternalAddresses(new InternalAddresses(
     *     "fonts",
     *     "images"
     * ))
     * .addPageSequence(titlePage)
     * .addPageSequence(tocPage)
     * .addPageSequence(contentPage)
     * .addPageSequence(appendixPage)
     * .build();
     * }</pre>
     *
     * <p><b>Usage Example - Building Incrementally:</b></p>
     * <pre>{@code
     * DocumentBuilder builder = Document.builder(metadata);
     *
     * // Add pages as they are generated
     * for (Chapter chapter : chapters) {
     *     PageSequence page = createChapterPage(chapter);
     *     builder.addPageSequence(page);
     * }
     *
     * Document document = builder.build();
     * }</pre>
     *
     * @param metadata the document metadata (title, author, language, etc.);
     *                 must not be {@code null} (recommended, though not enforced)
     * @return a new DocumentBuilder instance initialized with the provided metadata
     * @see DocumentBuilder
     * @see Metadata
     */
    @PublicAPI
    public static DocumentBuilder builder(Metadata metadata) {
        return new DocumentBuilder(metadata);
    }
}