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
package de.fkkaiser.model.structure.builder;

import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.structure.Document;
import de.fkkaiser.model.structure.InternalAddresses;
import de.fkkaiser.model.structure.Metadata;
import de.fkkaiser.model.structure.PageSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Fluent builder for constructing {@link Document} instances with a clear, readable API.
 *
 * <p>The builder pattern provides an elegant way to create Document objects, especially
 * when multiple page sequences need to be added incrementally. It offers better readability
 * than direct constructor calls and makes the document construction process explicit and
 * self-documenting.</p>
 *
 * <p><b>Builder Pattern Benefits:</b></p>
 * <ul>
 *   <li><b>Readability:</b> Each operation is explicit and self-documenting</li>
 *   <li><b>Flexibility:</b> Add page sequences incrementally in any order</li>
 *   <li><b>Optional Components:</b> Internal addresses can be included or omitted easily</li>
 *   <li><b>Incremental Construction:</b> Build document structure step by step</li>
 *   <li><b>Immutable Result:</b> Produces an immutable Document record</li>
 * </ul>
 *
 * <p><b>Required vs. Optional Components:</b></p>
 * <table>
 *   <caption>Document Builder Components</caption>
 *   <tr>
 *     <th>Component</th>
 *     <th>Required</th>
 *     <th>Set Via</th>
 *     <th>Default</th>
 *   </tr>
 *   <tr>
 *     <td>Metadata</td>
 *     <td>Yes</td>
 *     <td>Constructor</td>
 *     <td>N/A (required parameter)</td>
 *   </tr>
 *   <tr>
 *     <td>PageSequences</td>
 *     <td>No*</td>
 *     <td>{@link #addPageSequence(PageSequence)}</td>
 *     <td>Empty list (produces empty PDF)</td>
 *   </tr>
 *   <tr>
 *     <td>InternalAddresses</td>
 *     <td>No</td>
 *     <td>{@link #withInternalAddresses(InternalAddresses)}</td>
 *     <td>null (no external resources)</td>
 *   </tr>
 * </table>
 * <p>* While not strictly required, at least one page sequence is needed for meaningful content.</p>
 *
 * <p><b>Typical Build Process:</b></p>
 * <ol>
 *   <li><b>Create Builder:</b> {@code Document.builder(metadata)}</li>
 *   <li><b>Add Resources:</b> {@code .withInternalAddresses(...)} (optional)</li>
 *   <li><b>Add Content:</b> {@code .addPageSequence(...)} (one or more times)</li>
 *   <li><b>Build Document:</b> {@code .build()}</li>
 * </ol>
 *
 * <p><b>Usage Example 1 - Simple Single-Page Document:</b></p>
 * <pre>{@code
 * // Create a basic document with one page
 * Metadata metadata = new Metadata("Simple Document");
 *
 * ContentArea content = new ContentArea();
 * content.addElement(new Headline("title", "Welcome", 1));
 * content.addElement(new Paragraph("body", "This is a simple document."));
 *
 * PageSequence mainPage = new PageSequence("main-page", content, null, null);
 *
 * Document document = Document.builder(metadata)
 *     .addPageSequence(mainPage)
 *     .build();
 * }</pre>
 *
 * <p><b>Usage Example 2 - Multi-Section Document:</b></p>
 * <pre>{@code
 * // Create a complex document with multiple sections
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
 * .addPageSequence(createTitlePage())
 * .addPageSequence(createTableOfContents())
 * .addPageSequence(createExecutiveSummary())
 * .addPageSequence(createFinancialData())
 * .addPageSequence(createAppendices())
 * .build();
 * }</pre>
 *
 * <p><b>Usage Example 3 - Incremental Construction:</b></p>
 * <pre>{@code
 * // Build document incrementally as content becomes available
 * DocumentBuilder builder = Document.builder(
 *     new Metadata("Generated Report", "en-US")
 * );
 *
 * // Add title page
 * builder.addPageSequence(
 *     PageSequence.builder("title-page")
 *         .body(titleContent)
 *         .build()
 * );
 *
 * // Generate and add content pages
 * for (Chapter chapter : book.getChapters()) {
 *     ContentArea chapterContent = generateChapterContent(chapter);
 *     PageSequence chapterPage = PageSequence.builder("chapter-page")
 *         .header(standardHeader)
 *         .body(chapterContent)
 *         .footer(standardFooter)
 *         .build();
 *
 *     builder.addPageSequence(chapterPage);
 * }
 *
 * // Add appendix
 * builder.addPageSequence(createAppendix());
 *
 * // Build final document
 * Document document = builder.build();
 * }</pre>
 *
 * <p><b>Usage Example 4 - Conditional Content:</b></p>
 * <pre>{@code
 * DocumentBuilder builder = Document.builder(metadata);
 *
 * // Always add title page
 * builder.addPageSequence(titlePage);
 *
 * // Conditionally add table of contents
 * if (includeTOC) {
 *     builder.addPageSequence(tocPage);
 * }
 *
 * // Add main content
 * builder.addPageSequence(mainContent);
 *
 * // Conditionally add appendices
 * if (hasAppendices) {
 *     builder.addPageSequence(appendixPage);
 * }
 *
 * Document document = builder.build();
 * }</pre>
 *
 * <p><b>Usage Example 5 - With Custom Fonts and Images:</b></p>
 * <pre>{@code
 * InternalAddresses addresses = new InternalAddresses(
 *     "resources/fonts",
 *     "resources/images"
 * );
 *
 * Document document = Document.builder(metadata)
 *     .withInternalAddresses(addresses)
 *     .addPageSequence(productCatalogPage)
 *     .build();
 * }</pre>
 *
 * <p><b>Page Sequence Management:</b></p>
 * Page sequences are added in the order they are provided and will appear in that order
 * in the final PDF. Common document structures include:
 * <ul>
 *   <li><b>Title Page:</b> First page with document title and author</li>
 *   <li><b>Table of Contents:</b> Navigation overview</li>
 *   <li><b>Executive Summary:</b> High-level overview</li>
 *   <li><b>Main Content:</b> Primary document body (may span multiple sequences)</li>
 *   <li><b>Appendices:</b> Supporting information</li>
 *   <li><b>Index:</b> Alphabetical reference</li>
 * </ul>
 *
 * <p><b>Internal Addresses:</b></p>
 * The internal addresses component is optional and only needed when:
 * <ul>
 *   <li>Using custom fonts (not default system fonts)</li>
 *   <li>Including images in the document</li>
 *   <li>Both require JSON dictionary files that map identifiers to file paths</li>
 * </ul>
 *
 * If not set (or set to {@code null}), the PDF generator will use default system fonts
 * and images will not be available.
 *
 * <p><b>Builder Reuse:</b></p>
 * The builder instance can be reused to create multiple similar documents:
 * <pre>{@code
 * DocumentBuilder template = Document.builder(baseMetadata)
 *     .withInternalAddresses(standardAddresses);
 *
 * Document report1 = template
 *     .addPageSequence(report1Content)
 *     .build();
 *
 * // Note: After build(), the builder retains all page sequences
 * // For a fresh start, create a new builder instance
 * }</pre>
 *
 * <p><b>Validation:</b></p>
 * Validation is minimal in the builder:
 * <ul>
 *   <li>Metadata is required at construction time (via constructor parameter)</li>
 *   <li>No validation is performed on page sequences (empty list is valid)</li>
 *   <li>No validation is performed on internal addresses (null is valid)</li>
 *   <li>The built Document performs its own validation in its compact constructor</li>
 * </ul>
 *
 * <p><b>Thread Safety:</b></p>
 * This builder is not thread-safe. Each builder instance should be used by a single
 * thread. However, multiple builders can be used concurrently in different threads.
 *
 * <p><b>Empty Documents:</b></p>
 * It is possible to build a document with no page sequences:
 * <pre>{@code
 * Document emptyDoc = Document.builder(metadata).build();
 * }</pre>
 * This is technically valid but will produce an empty PDF with no content pages.
 *
 * @author FK Kaiser
 * @version 1.0
 * @see Document
 * @see Metadata
 * @see PageSequence
 * @see InternalAddresses
 */
public class DocumentBuilder {

    private static final Logger log = LoggerFactory.getLogger(DocumentBuilder.class);

    private final Metadata metadata;
    private final List<PageSequence> pageSequences;
    private InternalAddresses internalAddresses;

    /**
     * Creates a new DocumentBuilder with the required metadata.
     *
     * <p>The metadata is the only required component for a Document, as it provides
     * essential information like the document title (required for PDF/A compliance
     * and accessibility).</p>
     *
     * <p><b>Note:</b> This constructor is typically not called directly. Instead, use
     * {@link Document#builder(Metadata)} for better discoverability and consistency
     * with the API design pattern.</p>
     *
     * <p><b>Initialization:</b></p>
     * <ul>
     *   <li>Metadata is stored as provided (not copied)</li>
     *   <li>Page sequences list is initialized as an empty mutable ArrayList</li>
     *   <li>Internal addresses is initialized to null</li>
     * </ul>
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * // Preferred approach (via Document.builder)
     * Document doc = Document.builder(metadata)
     *     .addPageSequence(page)
     *     .build();
     *
     * // Direct approach (also valid)
     * Document doc = new DocumentBuilder(metadata)
     *     .addPageSequence(page)
     *     .build();
     * }</pre>
     *
     * @param metadata the document metadata; should not be {@code null}
     *                 (though not validated at this stage)
     */
    @PublicAPI
    public DocumentBuilder(Metadata metadata) {
        this.metadata = metadata;
        this.pageSequences = new ArrayList<>();
    }

    /**
     * Sets the internal addresses for external resource dictionaries.
     *
     * <p>Internal addresses point to JSON files that define available fonts and images
     * for the document. This component is optional and only needed when using custom
     * resources that are not available by default.</p>
     *
     * <p><b>When to Use:</b></p>
     * Set internal addresses when your document:
     * <ul>
     *   <li>Uses custom fonts (not default system fonts)</li>
     *   <li>Includes images that need to be referenced by ID</li>
     *   <li>Requires centralized resource management</li>
     * </ul>
     *
     * <p><b>Dictionary File Format:</b></p>
     * The font and image dictionary files are JSON files that map identifiers to
     * resource paths. See the {@link InternalAddresses} documentation for details.
     *
     * <p><b>Usage Example - With Custom Resources:</b></p>
     * <pre>{@code
     * InternalAddresses addresses = new InternalAddresses(
     *     "resources/fonts/company-fonts.json",
     *     "resources/images/brand-images.json"
     * );
     *
     * Document doc = Document.builder(metadata)
     *     .withInternalAddresses(addresses)
     *     .addPageSequence(contentPage)
     *     .build();
     * }</pre>
     *
     * <p><b>Usage Example - Only Custom Fonts:</b></p>
     * <pre>{@code
     * InternalAddresses addresses = new InternalAddresses(
     *     "fonts/custom-fonts.json",
     *     null  // No custom images
     * );
     *
     * Document doc = Document.builder(metadata)
     *     .withInternalAddresses(addresses)
     *     .addPageSequence(page)
     *     .build();
     * }</pre>
     *
     * <p><b>Usage Example - Only Custom Images:</b></p>
     * <pre>{@code
     * InternalAddresses addresses = new InternalAddresses(
     *     null,  // Use default fonts
     *     "images"
     * );
     *
     * Document doc = Document.builder(metadata)
     *     .withInternalAddresses(addresses)
     *     .addPageSequence(catalogPage)
     *     .build();
     * }</pre>
     *
     * <p><b>Note:</b> If not set, the document will use default system fonts and
     * images will not be available. This is perfectly valid for text-only documents
     * that don't require custom typography.</p>
     *
     * @param internalAddresses the resource dictionary addresses; may be {@code null}
     *                          to indicate no external resources are used
     * @return this builder instance for method chaining
     * @see InternalAddresses
     */
    @PublicAPI
    public DocumentBuilder withInternalAddresses(InternalAddresses internalAddresses) {
        this.internalAddresses = internalAddresses;
        log.debug("Set internal addresses: fonts='{}', images='{}'",
                internalAddresses != null ? internalAddresses.fontDictionary() : "null",
                internalAddresses != null ? internalAddresses.imageDictionary() : "null");
        return this;
    }

    /**
     * Adds a page sequence to the document.
     *
     * <p>Page sequences are added in the order they are provided and will appear in that
     * order in the final PDF. Each page sequence can have its own layout style, header,
     * body, and footer configuration.</p>
     *
     * <p><b>Common Page Sequence Patterns:</b></p>
     * <ul>
     *   <li><b>Title Page:</b> Special formatting, no headers/footers</li>
     *   <li><b>Table of Contents:</b> TOC-specific styling</li>
     *   <li><b>Main Content:</b> Standard layout with headers and footers</li>
     *   <li><b>Appendices:</b> Different numbering or style</li>
     * </ul>
     *
     * <p><b>Usage Example - Simple Document:</b></p>
     * <pre>{@code
     * PageSequence mainPage = PageSequence.builder("main-content")
     *     .body(mainContent)
     *     .build();
     *
     * Document doc = Document.builder(metadata)
     *     .addPageSequence(mainPage)
     *     .build();
     * }</pre>
     *
     * <p><b>Usage Example - Multi-Section Document:</b></p>
     * <pre>{@code
     * Document doc = Document.builder(metadata)
     *     .addPageSequence(titlePage)
     *     .addPageSequence(tocPage)
     *     .addPageSequence(chapter1Page)
     *     .addPageSequence(chapter2Page)
     *     .addPageSequence(appendixPage)
     *     .build();
     * }</pre>
     *
     * <p><b>Usage Example - Incremental Addition:</b></p>
     * <pre>{@code
     * DocumentBuilder builder = Document.builder(metadata);
     *
     * // Add pages as they are generated
     * for (int i = 1; i <= chapterCount; i++) {
     *     PageSequence chapter = createChapterPage(i);
     *     builder.addPageSequence(chapter);
     * }
     *
     * Document doc = builder.build();
     * }</pre>
     *
     * <p><b>Usage Example - Different Page Styles:</b></p>
     * <pre>{@code
     * // Title page with no header/footer
     * PageSequence title = PageSequence.builder("title-page")
     *     .body(titleContent)
     *     .build();
     *
     * // Content pages with header and footer
     * PageSequence content = PageSequence.builder("main-content")
     *     .header(standardHeader)
     *     .body(mainContent)
     *     .footer(standardFooter)
     *     .build();
     *
     * Document doc = Document.builder(metadata)
     *     .addPageSequence(title)
     *     .addPageSequence(content)
     *     .build();
     * }</pre>
     *
     * <p><b>Order Matters:</b></p>
     * Page sequences appear in the PDF in the exact order they are added to the builder.
     * Plan your document structure accordingly:
     * <pre>{@code
     * builder
     *     .addPageSequence(coverPage)      // Page 1
     *     .addPageSequence(tocPage)        // Page 2
     *     .addPageSequence(introPage)      // Page 3
     *     .addPageSequence(chapterOnePage) // Page 4
     *     // ... etc.
     * }</pre>
     *
     * <p><b>Empty Body Pages:</b></p>
     * A page sequence with a {@code null} or empty body is valid and will produce
     * blank pages (though headers and footers will still render if provided).
     *
     * <p><b>Logging:</b></p>
     * A debug log message is generated each time a page sequence is added, including
     * the style class of the sequence for traceability.
     *
     * @param pageSequence the page sequence to add; should not be {@code null}
     *                     (though not validated at this stage)
     * @return this builder instance for method chaining
     * @see PageSequence
     */
    @PublicAPI
    public DocumentBuilder addPageSequence(PageSequence pageSequence) {
        this.pageSequences.add(pageSequence);
        log.debug("Added page sequence with style class: '{}'",
                pageSequence != null ? pageSequence.styleClass() : "null");
        return this;
    }

    /**
     * Builds and returns the final immutable {@link Document} instance.
     *
     * <p>This method creates a new Document record using all components configured through
     * the builder. The resulting Document is immutable - to create variations, construct
     * a new Document instance.</p>
     *
     * <p><b>Build Process:</b></p>
     * <ol>
     *   <li>Creates an immutable copy of the page sequences list</li>
     *   <li>Constructs a new Document record with all components</li>
     *   <li>Document's compact constructor performs final validation</li>
     *   <li>Logs the build operation with document details</li>
     * </ol>
     *
     * <p><b>Immutability:</b></p>
     * The page sequences list is copied using {@code List.copyOf()} to ensure the
     * resulting Document is truly immutable and cannot be modified through the
     * original builder instance.
     *
     * <p><b>Builder Reuse:</b></p>
     * After calling {@code build()}, the builder retains its state and can theoretically
     * be used again. However, be aware that:
     * <ul>
     *   <li>The page sequences list remains populated</li>
     *   <li>Adding more sequences will affect subsequent builds</li>
     *   <li>For independent documents, create a new builder instance</li>
     * </ul>
     *
     * <p><b>Usage Example - Standard Build:</b></p>
     * <pre>{@code
     * Document document = Document.builder(metadata)
     *     .addPageSequence(titlePage)
     *     .addPageSequence(contentPage)
     *     .build();
     *
     * // document is now immutable and ready for PDF generation
     * }</pre>
     *
     * <p><b>Usage Example - Empty Document:</b></p>
     * <pre>{@code
     * // Building with no page sequences is valid
     * Document emptyDoc = Document.builder(metadata).build();
     *
     * // This produces a valid Document, but the PDF will have no content pages
     * }</pre>
     *
     * <p><b>Usage Example - Conditional Build:</b></p>
     * <pre>{@code
     * DocumentBuilder builder = Document.builder(metadata);
     *
     * // Add pages conditionally
     * if (includeTitlePage) {
     *     builder.addPageSequence(titlePage);
     * }
     *
     * builder.addPageSequence(mainContent);
     *
     * if (includeAppendix) {
     *     builder.addPageSequence(appendixPage);
     * }
     *
     * // Build when ready
     * Document document = builder.build();
     * }</pre>
     *
     * <p><b>Logging:</b></p>
     * An info-level log message is generated when the document is built, including:
     * <ul>
     *   <li>Document title (from metadata)</li>
     *   <li>Number of page sequences</li>
     *   <li>Whether internal addresses are present</li>
     * </ul>
     *
     * <p><b>Next Steps:</b></p>
     * After building the Document, it can be:
     * <ul>
     *   <li>Passed to a PDF generation facade for rendering</li>
     *   <li>Serialized to JSON for storage or transmission</li>
     *   <li>Used as a template for creating similar documents</li>
     * </ul>
     *
     * @return a new immutable Document instance with all configured components
     * @see Document
     */
    @PublicAPI
    public Document build() {
        log.info("Building document: title='{}', page sequences={}, has internal addresses={}",
                metadata != null ? metadata.getTitle() : "null",
                pageSequences.size(),
                internalAddresses != null);

        return new Document(
                internalAddresses,
                metadata,
                List.copyOf(pageSequences)
        );
    }
}