package de.fkkaiser.model.structure.builder;

import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.structure.ContentArea;
import de.fkkaiser.model.structure.Document;
import de.fkkaiser.model.structure.PageSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fluent builder for constructing {@link PageSequence} instances with a clear, readable API.
 *
 * <p>The builder pattern provides an elegant way to create PageSequence objects with optional
 * header, body, and footer components. It offers better readability compared to constructor
 * calls with multiple null parameters and makes the page structure explicit and self-documenting.</p>
 *
 * <p><b>Builder Pattern Benefits:</b></p>
 * <ul>
 *   <li><b>Readability:</b> Each component assignment is explicit and clear</li>
 *   <li><b>Flexibility:</b> Easily include or omit optional components</li>
 *   <li><b>Optional Parameters:</b> Only set the components you need</li>
 *   <li><b>Validation:</b> Required style class is enforced at construction time</li>
 *   <li><b>Immutable Result:</b> Produces an immutable PageSequence record</li>
 * </ul>
 *
 * <p><b>Required vs. Optional Components:</b></p>
 * <table>
 *   <caption>Builder Component Requirements</caption>
 *   <tr>
 *     <th>Component</th>
 *     <th>Required</th>
 *     <th>Set Via</th>
 *     <th>Default if Not Set</th>
 *   </tr>
 *   <tr>
 *     <td>styleClass</td>
 *     <td>Yes</td>
 *     <td>Constructor</td>
 *     <td>N/A (required parameter)</td>
 *   </tr>
 *   <tr>
 *     <td>header</td>
 *     <td>No</td>
 *     <td>{@link #header(ContentArea)}</td>
 *     <td>null (no header)</td>
 *   </tr>
 *   <tr>
 *     <td>body</td>
 *     <td>No*</td>
 *     <td>{@link #body(ContentArea)}</td>
 *     <td>null (blank content)</td>
 *   </tr>
 *   <tr>
 *     <td>footer</td>
 *     <td>No</td>
 *     <td>{@link #footer(ContentArea)}</td>
 *     <td>null (no footer)</td>
 *   </tr>
 * </table>
 * <p>* While body is optional, having a null body produces blank content area
 * (though headers and footers will still render if provided).</p>
 *
 * <p><b>Typical Build Process:</b></p>
 * <ol>
 *   <li><b>Create Builder:</b> {@code PageSequence.builder(styleClass)}</li>
 *   <li><b>Add Header:</b> {@code .header(headerContent)} (optional)</li>
 *   <li><b>Add Body:</b> {@code .body(mainContent)} (optional but recommended)</li>
 *   <li><b>Add Footer:</b> {@code .footer(footerContent)} (optional)</li>
 *   <li><b>Build:</b> {@code .build()}</li>
 * </ol>
 *
 * <p><b>Usage Example 1 - Simple Page with Body Only:</b></p>
 * <pre>{@code
 * // Create content
 * ContentArea content = new ContentArea();
 * content.addElement(new Headline("title", "Chapter 1", 1));
 * content.addElement(new Paragraph("body-text", "This is the content."));
 *
 * // Build page sequence
 * PageSequence page = PageSequence.builder("main-page")
 *     .body(content)
 *     .build();
 * }</pre>
 *
 * <p><b>Usage Example 2 - Page with Header, Body, and Footer:</b></p>
 * <pre>{@code
 * // Create header
 * ContentArea header = new ContentArea();
 * header.addElement(new Paragraph("header-text", "Document Title"));
 *
 * // Create body
 * ContentArea body = new ContentArea();
 * body.addElement(new Paragraph("body-text", "Main content here..."));
 *
 * // Create footer with page number
 * ContentArea footer = new ContentArea();
 * Paragraph footerPara = Paragraph.builder("footer-text")
 *     .addInlineText("Page ")
 *     .addInlineElement(new PageNumber())
 *     .build();
 * footer.addElement(footerPara);
 *
 * // Build complete page sequence
 * PageSequence page = PageSequence.builder("standard-page")
 *     .header(header)
 *     .body(body)
 *     .footer(footer)
 *     .build();
 * }</pre>
 *
 * <p><b>Usage Example 3 - Title Page (Body Only, No Header/Footer):</b></p>
 * <pre>{@code
 * ContentArea titleContent = new ContentArea();
 * titleContent.addElement(new Headline("main-title", "Annual Report 2024", 1));
 * titleContent.addElement(new Paragraph("subtitle", "Financial Overview"));
 * titleContent.addElement(new Paragraph("author", "Finance Department"));
 *
 * PageSequence titlePage = PageSequence.builder("title-page")
 *     .body(titleContent)
 *     .build();
 * }</pre>
 *
 * <p><b>Usage Example 4 - Conditional Components:</b></p>
 * <pre>{@code
 * PageSequenceBuilder builder = PageSequence.builder("content-page");
 *
 * // Always add body
 * builder.body(mainContent);
 *
 * // Conditionally add header
 * if (includeHeader) {
 *     builder.header(createHeader());
 * }
 *
 * // Conditionally add footer
 * if (includePageNumbers) {
 *     builder.footer(createFooterWithPageNumber());
 * }
 *
 * PageSequence page = builder.build();
 * }</pre>
 *
 * <p><b>Usage Example 5 - Building Multiple Similar Pages:</b></p>
 * <pre>{@code
 * // Create standard header and footer
 * ContentArea standardHeader = createStandardHeader();
 * ContentArea standardFooter = createStandardFooter();
 *
 * // Build multiple chapter pages with same layout
 * List<PageSequence> chapters = new ArrayList<>();
 * for (Chapter chapter : book.getChapters()) {
 *     PageSequence chapterPage = PageSequence.builder("chapter-page")
 *         .header(standardHeader)
 *         .body(generateChapterContent(chapter))
 *         .footer(standardFooter)
 *         .build();
 *
 *     chapters.add(chapterPage);
 * }
 * }</pre>
 *
 * <p><b>Usage Example 6 - Different Page Types:</b></p>
 * <pre>{@code
 * // Title page - no header/footer
 * PageSequence titlePage = PageSequence.builder("title-page")
 *     .body(titleContent)
 *     .build();
 *
 * // Table of contents - footer only
 * PageSequence tocPage = PageSequence.builder("toc-page")
 *     .body(tocContent)
 *     .footer(pageNumberFooter)
 *     .build();
 *
 * // Main content - full layout
 * PageSequence contentPage = PageSequence.builder("main-content")
 *     .header(standardHeader)
 *     .body(mainContent)
 *     .footer(standardFooter)
 *     .build();
 *
 * // Blank separator - no body
 * PageSequence blankPage = PageSequence.builder("blank-page")
 *     .build();
 * }</pre>
 *
 * <p><b>Usage Example 7 - Integration with Document Builder:</b></p>
 * <pre>{@code
 * Document document = Document.builder(metadata)
 *     .addPageSequence(
 *         PageSequence.builder("title-page")
 *             .body(titleContent)
 *             .build()
 *     )
 *     .addPageSequence(
 *         PageSequence.builder("main-content")
 *             .header(standardHeader)
 *             .body(mainContent)
 *             .footer(standardFooter)
 *             .build()
 *     )
 *     .build();
 * }</pre>
 *
 *
 * <p><b>Header and Footer Design:</b></p>
 * Best practices for headers and footers:
 * <ul>
 *   <li>Keep content simple and concise</li>
 *   <li>Use consistent styling across all pages in the sequence</li>
 *   <li>Include page numbers in footers using {@code PageNumber} element</li>
 *   <li>Consider document title or chapter name in headers</li>
 *   <li>Use appropriate style classes for header/footer text</li>
 * </ul>
 *
 * <p><b>Body Content:</b></p>
 * The body content flows across multiple pages as needed:
 * <ul>
 *   <li>Content automatically breaks at page boundaries</li>
 *   <li>Headers and footers repeat on each new page</li>
 *   <li>Use appropriate elements (Paragraph, Headline, Table, etc.)</li>
 *   <li>Null body creates blank content area (valid but uncommon)</li>
 * </ul>
 *
 * <p><b>Builder Reuse:</b></p>
 * The builder can be reused to create multiple similar page sequences:
 * <pre>{@code
 * PageSequenceBuilder template = PageSequence.builder("chapter-page")
 *     .header(standardHeader)
 *     .footer(standardFooter);
 *
 * // Reuse template with different bodies
 * PageSequence chapter1 = template.body(chapter1Content).build();
 * PageSequence chapter2 = template.body(chapter2Content).build();
 * }</pre>
 *
 * <p><b>Validation:</b></p>
 * The builder performs minimal validation:
 * <ul>
 *   <li>Style class is required and validated at construction time</li>
 *   <li>Header, body, and footer are optional (null is valid)</li>
 *   <li>The built PageSequence performs additional validation in its compact constructor</li>
 * </ul>
 *
 * <p><b>Thread Safety:</b></p>
 * This builder is not thread-safe. Each builder instance should be used by a single
 * thread. However, multiple builders can be used concurrently in different threads.
 *
 * <p><b>Blank Pages:</b></p>
 * It is valid to build a page sequence with no body content:
 * <pre>{@code
 * // Blank separator page between sections
 * PageSequence blankPage = PageSequence.builder("blank-page").build();
 * }</pre>
 * This produces a valid PageSequence with blank content (though headers and footers
 * will still render if provided).
 *
 * @author FK Kaiser
 * @version 1.0
 * @see PageSequence
 * @see ContentArea
 * @see Document
 * @see DocumentBuilder
 */
public class PageSequenceBuilder {

    private static final Logger log = LoggerFactory.getLogger(PageSequenceBuilder.class);

    private final String styleClass;
    private ContentArea header;
    private ContentArea body;
    private ContentArea footer;

    /**
     * Creates a new PageSequenceBuilder with the required style class.
     *
     * <p>The style class is the only required component for a PageSequence, as it defines
     * the page layout characteristics such as size, orientation, margins, and styling.</p>
     *
     * <p><b>Validation:</b></p>
     * The style class must not be {@code null} or empty. If validation fails:
     * <ul>
     *   <li>An error message is logged</li>
     *   <li>An {@code IllegalArgumentException} is thrown</li>
     *   <li>The builder instance is not created</li>
     * </ul>
     *
     * <p><b>Note:</b> This constructor is typically not called directly. Instead, use
     * {@link PageSequence#builder(String)} for better discoverability and consistency
     * with the API design pattern.</p>
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * // Preferred approach (via PageSequence)
     * PageSequence page = PageSequence.builder("main-content")
     *     .body(content)
     *     .build();
     *
     * // Direct approach (also valid)
     * PageSequence page = new PageSequenceBuilder("main-content")
     *     .body(content)
     *     .build();
     * }</pre>
     *
     * <p><b>Common Style Classes:</b></p>
     * <ul>
     *   <li>"title-page" - Special title page layout</li>
     *   <li>"toc-page" - Table of contents layout</li>
     *   <li>"main-content" - Standard body content layout</li>
     *   <li>"landscape-page" - Landscape orientation</li>
     *   <li>"appendix-page" - Appendix section layout</li>
     * </ul>
     *
     * @param styleClass the CSS-like style class name that references a page style definition;
     *                   must not be {@code null} or empty
     * @throws IllegalArgumentException if styleClass is {@code null} or empty
     */
    @PublicAPI
    public PageSequenceBuilder(String styleClass) {
        if (styleClass == null || styleClass.trim().isEmpty()) {
            log.error("PageSequenceBuilder styleClass is null or empty");
            throw new IllegalArgumentException("styleClass is required for PageSequence");
        }
        this.styleClass = styleClass;
        log.debug("Created PageSequenceBuilder with style class: '{}'", styleClass);
    }

    /**
     * Sets the header content for this page sequence.
     *
     * <p>The header appears at the top of every page within the sequence. It typically
     * contains information such as document title, chapter name, or company name that
     * provides context for the page content.</p>
     *
     * <p><b>Common Header Content:</b></p>
     * <ul>
     *   <li>Document title</li>
     *   <li>Chapter or section name</li>
     *   <li>Company name or logo</li>
     *   <li>Date or version information</li>
     * </ul>
     *
     * <p><b>Header Design Guidelines:</b></p>
     * <ul>
     *   <li>Keep it simple and concise (1-2 lines typically)</li>
     *   <li>Use consistent styling across all pages</li>
     *   <li>Ensure adequate spacing from body content</li>
     *   <li>Consider readability at normal reading distance</li>
     * </ul>
     *
     * <p><b>Usage Example - Simple Header:</b></p>
     * <pre>{@code
     * ContentArea header = new ContentArea();
     * header.addElement(new Paragraph("header-text", "Annual Report 2024"));
     *
     * PageSequence page = PageSequence.builder("main-content")
     *     .header(header)
     *     .body(content)
     *     .build();
     * }</pre>
     *
     * <p><b>Usage Example - Header with Multiple Elements:</b></p>
     * <pre>{@code
     * ContentArea header = new ContentArea();
     * header.addElement(new Paragraph("header-title", "Technical Manual"));
     * header.addElement(new Paragraph("header-section", "Chapter 1: Introduction"));
     *
     * PageSequence page = PageSequence.builder("chapter-page")
     *     .header(header)
     *     .body(chapterContent)
     *     .build();
     * }</pre>
     *
     * <p><b>Usage Example - No Header (Title Page):</b></p>
     * <pre>{@code
     * // Title pages typically don't have headers
     * PageSequence titlePage = PageSequence.builder("title-page")
     *     .body(titleContent)
     *     // No header call
     *     .build();
     * }</pre>
     *
     * <p><b>Note:</b> If not set, the header will be {@code null}, meaning no header
     * content will appear on the pages in this sequence.</p>
     *
     * @param header the content area for the page header;
     *               may be {@code null} to indicate no header
     * @return this builder instance for method chaining
     */
    @PublicAPI
    public PageSequenceBuilder header(ContentArea header) {
        this.header = header;
        log.debug("Set header for page sequence with style class '{}'", styleClass);
        return this;
    }

    /**
     * Sets the body content for this page sequence.
     *
     * <p>The body is the main content area of the page and typically contains the primary
     * information such as paragraphs, headlines, tables, images, and other document elements.
     * Body content automatically flows across multiple pages as needed.</p>
     *
     * <p><b>Common Body Content:</b></p>
     * <ul>
     *   <li>Text paragraphs</li>
     *   <li>Headlines and subheadings</li>
     *   <li>Tables and data</li>
     *   <li>Images and figures</li>
     *   <li>Lists and enumerations</li>
     *   <li>Sections and subsections</li>
     * </ul>
     *
     * <p><b>Body Content Guidelines:</b></p>
     * <ul>
     *   <li>Structure content logically with headlines and sections</li>
     *   <li>Use appropriate element types for different content</li>
     *   <li>Consider page breaks for long content</li>
     *   <li>Ensure adequate spacing between elements</li>
     *   <li>Test with realistic content volumes</li>
     * </ul>
     *
     * <p><b>Usage Example - Simple Body:</b></p>
     * <pre>{@code
     * ContentArea body = new ContentArea();
     * body.addElement(new Headline("title", "Introduction", 1));
     * body.addElement(new Paragraph("body-text", "This document describes..."));
     *
     * PageSequence page = PageSequence.builder("main-content")
     *     .body(body)
     *     .build();
     * }</pre>
     *
     * <p><b>Usage Example - Structured Body:</b></p>
     * <pre>{@code
     * ContentArea body = new ContentArea();
     *
     * // Add title
     * body.addElement(new Headline("chapter-title", "Chapter 1", 1));
     *
     * // Add introduction
     * body.addElement(new Paragraph("body-text", "Introduction paragraph..."));
     *
     * // Add section
     * body.addElement(new Headline("section-title", "Background", 2));
     * body.addElement(new Paragraph("body-text", "Background information..."));
     *
     * // Add table
     * body.addElement(createDataTable());
     *
     * PageSequence page = PageSequence.builder("chapter-page")
     *     .body(body)
     *     .build();
     * }</pre>
     *
     * <p><b>Usage Example - Blank Body:</b></p>
     * <pre>{@code
     * // Blank separator page between sections
     * PageSequence blankPage = PageSequence.builder("blank-page")
     *     .body(null)  // or omit .body() call entirely
     *     .build();
     * }</pre>
     *
     * <p><b>Page Flow:</b></p>
     * When body content exceeds the available space on a single page:
     * <ul>
     *   <li>Content automatically flows to the next page</li>
     *   <li>Headers and footers repeat on each new page</li>
     *   <li>The same style class applies to all pages in the sequence</li>
     * </ul>
     *
     * <p><b>Note:</b> If not set (or set to {@code null}), the body will be blank,
     * though headers and footers will still render if provided. This is valid but
     * uncommon except for intentional blank pages.</p>
     *
     * @param body the content area for the page body;
     *             may be {@code null} to create a blank content area
     * @return this builder instance for method chaining
     */
    @PublicAPI
    public PageSequenceBuilder body(ContentArea body) {
        this.body = body;
        log.debug("Set body for page sequence with style class '{}'", styleClass);
        return this;
    }

    /**
     * Sets the footer content for this page sequence.
     *
     * <p>The footer appears at the bottom of every page within the sequence. It typically
     * contains information such as page numbers, copyright notices, or document identification
     * that should appear consistently across all pages.</p>
     *
     * <p><b>Common Footer Content:</b></p>
     * <ul>
     *   <li>Page numbers</li>
     *   <li>Copyright notices</li>
     *   <li>Document ID or version</li>
     *   <li>Date of creation</li>
     *   <li>Company name or branding</li>
     * </ul>
     *
     * <p><b>Footer Design Guidelines:</b></p>
     * <ul>
     *   <li>Keep it simple and unobtrusive (1-2 lines typically)</li>
     *   <li>Use smaller font size than body text</li>
     *   <li>Include page numbers for multi-page documents</li>
     *   <li>Ensure consistent styling across all pages</li>
     *   <li>Consider alignment (centered, left, right)</li>
     * </ul>
     *
     * <p><b>Usage Example - Simple Page Number Footer:</b></p>
     * <pre>{@code
     * ContentArea footer = new ContentArea();
     * Paragraph footerPara = Paragraph.builder("footer-text")
     *     .addInlineText("Page ")
     *     .addInlineElement(new PageNumber())
     *     .build();
     * footer.addElement(footerPara);
     *
     * PageSequence page = PageSequence.builder("main-content")
     *     .body(content)
     *     .footer(footer)
     *     .build();
     * }</pre>
     *
     * <p><b>Usage Example - Footer with Copyright:</b></p>
     * <pre>{@code
     * ContentArea footer = new ContentArea();
     * footer.addElement(new Paragraph("footer-text", "© 2024 Company Name"));
     * footer.addElement(Paragraph.builder("footer-page-number")
     *     .addInlineText("Page ")
     *     .addInlineElement(new PageNumber())
     *     .build());
     *
     * PageSequence page = PageSequence.builder("main-content")
     *     .body(content)
     *     .footer(footer)
     *     .build();
     * }</pre>
     *
     * <p><b>Usage Example - No Footer (Title Page):</b></p>
     * <pre>{@code
     * // Title pages often don't have footers with page numbers
     * PageSequence titlePage = PageSequence.builder("title-page")
     *     .body(titleContent)
     *     // No footer call
     *     .build();
     * }</pre>
     *
     * <p><b>Dynamic Page Numbers:</b></p>
     * Use the {@code PageNumber} element for automatic page numbering:
     * <pre>{@code
     * Paragraph footer = Paragraph.builder("footer")
     *     .addInlineText("Page ")
     *     .addInlineElement(new PageNumber())
     *     .addInlineText(" of ")
     *     .addInlineElement(new TotalPageCount())  // If available
     *     .build();
     * }</pre>
     *
     * <p><b>Note:</b> If not set, the footer will be {@code null}, meaning no footer
     * content will appear on the pages in this sequence.</p>
     *
     * @param footer the content area for the page footer;
     *               may be {@code null} to indicate no footer
     * @return this builder instance for method chaining
     */
    @PublicAPI
    public PageSequenceBuilder footer(ContentArea footer) {
        this.footer = footer;
        log.debug("Set footer for page sequence with style class '{}'", styleClass);
        return this;
    }

    /**
     * Builds and returns the final immutable {@link PageSequence} instance.
     *
     * <p>This method creates a new PageSequence record using all components configured
     * through the builder. The resulting PageSequence is immutable - to create variations,
     * construct a new PageSequence instance.</p>
     *
     * <p><b>Build Process:</b></p>
     * <ol>
     *   <li>Validates that the style class is set (done in constructor)</li>
     *   <li>Constructs a new PageSequence with all components</li>
     *   <li>PageSequence's compact constructor performs final validation</li>
     *   <li>Logs the build operation with sequence details</li>
     * </ol>
     *
     * <p><b>Component States:</b></p>
     * The built PageSequence will contain:
     * <ul>
     *   <li><b>styleClass:</b> The value provided in the constructor (never null)</li>
     *   <li><b>header:</b> The header set via {@link #header(ContentArea)}, or null</li>
     *   <li><b>body:</b> The body set via {@link #body(ContentArea)}, or null</li>
     *   <li><b>footer:</b> The footer set via {@link #footer(ContentArea)}, or null</li>
     * </ul>
     *
     * <p><b>Builder Reuse:</b></p>
     * After calling {@code build()}, the builder retains its state and can be reused:
     * <pre>{@code
     * PageSequenceBuilder template = PageSequence.builder("chapter-page")
     *     .header(standardHeader)
     *     .footer(standardFooter);
     *
     * // Build multiple pages with different bodies
     * PageSequence chapter1 = template.body(chapter1Content).build();
     * PageSequence chapter2 = template.body(chapter2Content).build();
     * }</pre>
     *
     * <p><b>Usage Example - Standard Build:</b></p>
     * <pre>{@code
     * PageSequence page = PageSequence.builder("main-content")
     *     .header(headerContent)
     *     .body(mainContent)
     *     .footer(footerContent)
     *     .build();
     *
     * // page is now immutable and ready to be added to a document
     * }</pre>
     *
     * <p><b>Usage Example - Minimal Build:</b></p>
     * <pre>{@code
     * // Only body content
     * PageSequence page = PageSequence.builder("title-page")
     *     .body(titleContent)
     *     .build();
     * }</pre>
     *
     * <p><b>Usage Example - Blank Page:</b></p>
     * <pre>{@code
     * // Blank separator page
     * PageSequence blankPage = PageSequence.builder("blank-page").build();
     * }</pre>
     *
     * <p><b>Logging:</b></p>
     * An info-level log message is generated when the page sequence is built, including:
     * <ul>
     *   <li>Style class name</li>
     *   <li>Whether header is present</li>
     *   <li>Whether body is present</li>
     *   <li>Whether footer is present</li>
     * </ul>
     *
     * <p><b>Next Steps:</b></p>
     * After building the PageSequence, it can be:
     * <ul>
     *   <li>Added to a Document via DocumentBuilder</li>
     *   <li>Serialized to JSON for storage</li>
     *   <li>Used as a template for creating similar sequences</li>
     * </ul>
     *
     * @return a new immutable PageSequence instance with all configured components
     * @see PageSequence
     * @see Document
     * @see DocumentBuilder
     */
    @PublicAPI
    public PageSequence build() {
        log.info("Building PageSequence: style class='{}', has header={}, has body={}, has footer={}",
                styleClass,
                header != null,
                body != null,
                footer != null);

        return new PageSequence(styleClass, body, header, footer);
    }
}