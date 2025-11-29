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
import de.fkkaiser.model.structure.builder.PageSequenceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a sequence of pages in a document with a consistent layout and styling.
 *
 * <p>A PageSequence defines a section of the document where all pages share the same
 * style class and can have optional header, body, and footer content areas. Documents
 * typically contain one or more page sequences, each representing a distinct section
 * with its own layout characteristics.</p>
 *
 * <p><b>Purpose and Design:</b></p>
 * Page sequences provide structural flexibility by allowing different sections of a
 * document to have different layouts. For example:
 * <ul>
 *   <li><b>Title Page:</b> Special formatting with no headers or footers</li>
 *   <li><b>Table of Contents:</b> Different page numbering or layout</li>
 *   <li><b>Main Content:</b> Standard layout with consistent headers and footers</li>
 *   <li><b>Appendices:</b> Different styling or page numbering scheme</li>
 * </ul>
 *
 * <p><b>Core Components:</b></p>
 * <table>
 *   <caption>PageSequence Components</caption>
 *   <tr>
 *     <th>Component</th>
 *     <th>Required</th>
 *     <th>Purpose</th>
 *     <th>Appears On</th>
 *   </tr>
 *   <tr>
 *     <td>styleClass</td>
 *     <td>Yes</td>
 *     <td>References the page layout style</td>
 *     <td>N/A (configuration)</td>
 *   </tr>
 *   <tr>
 *     <td>header</td>
 *     <td>No</td>
 *     <td>Content at the top of each page</td>
 *     <td>Every page in sequence</td>
 *   </tr>
 *   <tr>
 *     <td>body</td>
 *     <td>No*</td>
 *     <td>Main page content</td>
 *     <td>Every page in sequence</td>
 *   </tr>
 *   <tr>
 *     <td>footer</td>
 *     <td>No</td>
 *     <td>Content at the bottom of each page</td>
 *     <td>Every page in sequence</td>
 *   </tr>
 * </table>
 * <p>* While body is optional, having a null or empty body produces blank content pages
 * (though headers and footers will still render if provided).</p>
 *
 * <p><b>Style Class:</b></p>
 * The style class is the only required field. It references a page style definition that
 * controls:
 * <ul>
 *   <li>Page size (A4, Letter, custom)</li>
 *   <li>Page orientation (portrait, landscape)</li>
 *   <li>Margins (top, bottom, left, right)</li>
 *   <li>Column layout (single, multi-column)</li>
 *   <li>Page numbering format and position</li>
 * </ul>
 *
 * <p><b>JSON Representation:</b></p>
 * <pre>{@code
 * {
 *   "style-class": "main-content",
 *   "header": {
 *     "elements": [
 *       {
 *         "type": "paragraph",
 *         "style-class": "header-text",
 *         "inline-elements": [...]
 *       }
 *     ]
 *   },
 *   "body": {
 *     "elements": [
 *       {
 *         "type": "headline",
 *         "level": 1,
 *         "inline-elements": [...]
 *       },
 *       {
 *         "type": "paragraph",
 *         "inline-elements": [...]
 *       }
 *     ]
 *   },
 *   "footer": {
 *     "elements": [
 *       {
 *         "type": "paragraph",
 *         "style-class": "footer-text",
 *         "inline-elements": [...]
 *       }
 *     ]
 *   }
 * }
 * }</pre>
 *
 * <p><b>Usage Example 1 - Simple Single-Section Document:</b></p>
 * <pre>{@code
 * // Create content
 * ContentArea body = new ContentArea();
 * body.addElement(new Headline("title", "Document Title", 1));
 * body.addElement(new Paragraph("body-text", "This is the content."));
 *
 * // Create page sequence (no header/footer)
 * PageSequence mainPage = new PageSequence(
 *     "main-page",  // style class
 *     body,         // body content
 *     null,         // no header
 *     null          // no footer
 * );
 *
 * // Add to document
 * Document doc = Document.builder(metadata)
 *     .addPageSequence(mainPage)
 *     .build();
 * }</pre>
 *
 * <p><b>Usage Example 2 - Page with Header and Footer:</b></p>
 * <pre>{@code
 * // Create header
 * ContentArea header = new ContentArea();
 * header.addElement(new Paragraph("header-text", "Company Name"));
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
 * // Create page sequence
 * PageSequence contentPage = new PageSequence(
 *     "standard-page",
 *     body,
 *     header,
 *     footer
 * );
 * }</pre>
 *
 * <p><b>Usage Example 3 - Using Builder (Recommended):</b></p>
 * <pre>{@code
 * PageSequence page = PageSequence.builder("main-content")
 *     .header(headerContent)
 *     .body(mainContent)
 *     .footer(footerContent)
 *     .build();
 * }</pre>
 *
 * <p><b>Usage Example 4 - Multi-Section Document:</b></p>
 * <pre>{@code
 * // Title page (no header/footer, special style)
 * PageSequence titlePage = PageSequence.builder("title-page")
 *     .body(titleContent)
 *     .build();
 *
 * // Table of contents (no header, basic footer)
 * PageSequence tocPage = PageSequence.builder("toc-page")
 *     .body(tocContent)
 *     .footer(basicFooter)
 *     .build();
 *
 * // Main content (full header and footer)
 * PageSequence mainPages = PageSequence.builder("main-content")
 *     .header(standardHeader)
 *     .body(mainContent)
 *     .footer(standardFooter)
 *     .build();
 *
 * // Appendix (different styling)
 * PageSequence appendix = PageSequence.builder("appendix-page")
 *     .header(appendixHeader)
 *     .body(appendixContent)
 *     .footer(appendixFooter)
 *     .build();
 *
 * // Assemble document
 * Document doc = Document.builder(metadata)
 *     .addPageSequence(titlePage)
 *     .addPageSequence(tocPage)
 *     .addPageSequence(mainPages)
 *     .addPageSequence(appendix)
 *     .build();
 * }</pre>
 *
 * <p><b>Usage Example 5 - Blank Separator Page:</b></p>
 * <pre>{@code
 * // Create a blank page between sections
 * PageSequence blankPage = PageSequence.builder("blank-page")
 *     .body(null)  // No content
 *     .build();
 *
 * Document doc = Document.builder(metadata)
 *     .addPageSequence(chapter1)
 *     .addPageSequence(blankPage)  // Separator
 *     .addPageSequence(chapter2)
 *     .build();
 * }</pre>
 *
 * <p><b>Common Page Sequence Patterns:</b></p>
 * <ul>
 *   <li><b>Title Page:</b>
 *       <ul>
 *         <li>Style: "title-page" (centered, large margins)</li>
 *         <li>Header: null</li>
 *         <li>Body: Title, subtitle, author, date</li>
 *         <li>Footer: null</li>
 *       </ul>
 *   </li>
 *   <li><b>Table of Contents:</b>
 *       <ul>
 *         <li>Style: "toc-page"</li>
 *         <li>Header: null or minimal</li>
 *         <li>Body: TOC list with page numbers</li>
 *         <li>Footer: Page number</li>
 *       </ul>
 *   </li>
 *   <li><b>Main Content:</b>
 *       <ul>
 *         <li>Style: "main-content" (standard margins)</li>
 *         <li>Header: Document title, chapter name</li>
 *         <li>Body: Paragraphs, images, tables</li>
 *         <li>Footer: Page number, copyright</li>
 *       </ul>
 *   </li>
 *   <li><b>Appendix:</b>
 *       <ul>
 *         <li>Style: "appendix-page"</li>
 *         <li>Header: "Appendix" marker</li>
 *         <li>Body: Supporting information</li>
 *         <li>Footer: Appendix page numbering (A-1, A-2, etc.)</li>
 *       </ul>
 *   </li>
 * </ul>
 *
 * <p><b>Header and Footer Behavior:</b></p>
 * Headers and footers, when provided, appear on every page within the sequence:
 * <ul>
 *   <li><b>Consistency:</b> Same content on all pages in the sequence</li>
 *   <li><b>Dynamic Elements:</b> Page numbers update automatically</li>
 *   <li><b>Styling:</b> Use appropriate style classes for header/footer elements</li>
 *   <li><b>Null Handling:</b> Null header/footer means no content in that area</li>
 * </ul>
 *
 * <p><b>Body Content Flow:</b></p>
 * The body content flows across multiple pages as needed:
 * <ul>
 *   <li>Content automatically breaks across page boundaries</li>
 *   <li>Headers and footers repeat on each new page</li>
 *   <li>Page style determines available content area</li>
 *   <li>Empty or null body creates blank content area (headers/footers still show)</li>
 * </ul>
 *
 * <p><b>Page Numbering:</b></p>
 * Each page sequence can have its own numbering scheme:
 * <pre>{@code
 * // Roman numerals for front matter
 * PageSequence frontMatter = PageSequence.builder("toc-page")
 *     .body(tocContent)
 *     .footer(createFooterWithPageNumber("roman"))  // i, ii, iii, iv
 *     .build();
 *
 * // Arabic numerals for main content
 * PageSequence mainContent = PageSequence.builder("main-content")
 *     .body(content)
 *     .footer(createFooterWithPageNumber("arabic"))  // 1, 2, 3, 4
 *     .build();
 * }</pre>
 *
 * <p><b>Style Class Selection:</b></p>
 * Choose style classes based on the section's requirements:
 * <ul>
 *   <li><b>"title-page":</b> Large margins, centered content, no page numbers</li>
 *   <li><b>"toc-page":</b> Standard margins, page numbers in footer</li>
 *   <li><b>"main-content":</b> Standard layout with headers and footers</li>
 *   <li><b>"landscape-page":</b> Landscape orientation for wide tables/charts</li>
 *   <li><b>"two-column":</b> Two-column layout for dense content</li>
 * </ul>
 *
 * <p><b>Validation:</b></p>
 * The compact constructor validates:
 * <ul>
 *   <li><b>styleClass:</b> Must not be {@code null} or empty</li>
 *   <li><b>header, body, footer:</b> May be {@code null} (no validation)</li>
 * </ul>
 *
 * If the style class is invalid, an {@code IllegalArgumentException} is thrown and
 * an error is logged.
 *
 * <p><b>Immutability:</b></p>
 * As a record, PageSequence instances are immutable after construction. To create
 * variations, construct new PageSequence instances with the desired differences.
 *
 * <p><b>Thread Safety:</b></p>
 * PageSequence instances are immutable and thread-safe. However, the mutable
 * {@link ContentArea} objects they contain may not be thread-safe.
 *
 * <p><b>Best Practices:</b></p>
 * <ul>
 *   <li>Use descriptive style class names ("title-page", not "page1")</li>
 *   <li>Keep headers and footers simple and consistent</li>
 *   <li>Use PageNumber elements for dynamic page numbering</li>
 *   <li>Group related content into logical page sequences</li>
 *   <li>Test page breaks with realistic content volumes</li>
 * </ul>
 *
 * @param styleClass the CSS-like style class name that references a page style definition;
 *                   must not be {@code null} or empty
 * @param body       the main content area for the page sequence;
 *                   may be {@code null} (produces blank content area)
 * @param header     the header content that appears at the top of each page;
 *                   may be {@code null} (no header)
 * @param footer     the footer content that appears at the bottom of each page;
 *                   may be {@code null} (no footer)
 *
 * @author FK Kaiser
 * @version 1.0
 * @see Document
 * @see ContentArea
 * @see PageSequenceBuilder
 */
public record PageSequence(
        @JsonProperty("style-class")
        String styleClass,

        @JsonProperty("body")
        ContentArea body,

        @JsonProperty("header")
        ContentArea header,

        @JsonProperty("footer")
        ContentArea footer
) {

    private static final Logger log = LoggerFactory.getLogger(PageSequence.class);

    /**
     * Compact constructor that validates the page sequence parameters.
     *
     * <p>This constructor is automatically invoked whenever a PageSequence instance
     * is created. It ensures that the style class is valid, as this is the only
     * required component of a page sequence.</p>
     *
     * <p><b>Validation Rules:</b></p>
     * <ul>
     *   <li><b>styleClass:</b> Must not be {@code null} or empty (after trimming)</li>
     *   <li><b>header:</b> No validation (null is valid)</li>
     *   <li><b>body:</b> No validation (null is valid, produces blank content)</li>
     *   <li><b>footer:</b> No validation (null is valid)</li>
     * </ul>
     *
     * <p><b>Error Handling:</b></p>
     * If the style class is null or empty:
     * <ul>
     *   <li>An error message is logged</li>
     *   <li>An {@code IllegalArgumentException} is thrown</li>
     *   <li>The PageSequence instance is not created</li>
     * </ul>
     *
     * <p><b>Example Valid Constructions:</b></p>
     * <pre>{@code
     * // All components provided
     * new PageSequence("main-page", body, header, footer);  // ✓ Valid
     *
     * // Only body
     * new PageSequence("main-page", body, null, null);      // ✓ Valid
     *
     * // Only header and footer (blank content)
     * new PageSequence("main-page", null, header, footer);  // ✓ Valid
     *
     * // Only style class (completely blank page)
     * new PageSequence("blank-page", null, null, null);     // ✓ Valid
     * }</pre>
     *
     * <p><b>Example Invalid Constructions:</b></p>
     * <pre>{@code
     * // Missing style class
     * new PageSequence(null, body, header, footer);         // ✗ IllegalArgumentException
     * new PageSequence("", body, header, footer);           // ✗ IllegalArgumentException
     * new PageSequence("   ", body, header, footer);        // ✗ IllegalArgumentException
     * }</pre>
     *
     * @throws IllegalArgumentException if styleClass is {@code null} or empty
     */
    public PageSequence {
        if (styleClass == null || styleClass.trim().isEmpty()) {
            log.error("PageSequence styleClass is empty or null");
            throw new IllegalArgumentException("styleClass cannot be null or empty.");
        }
    }

    /**
     * Creates and returns a new {@link PageSequenceBuilder} for fluent page sequence construction.
     *
     * <p>The builder pattern is the recommended way to create PageSequence instances, as it
     * provides a clear, readable API for constructing page sequences with optional components.</p>
     *
     * <p><b>Builder Benefits:</b></p>
     * <ul>
     *   <li><b>Readability:</b> Clear, self-documenting code</li>
     *   <li><b>Flexibility:</b> Easily include or omit optional components</li>
     *   <li><b>Validation:</b> Required fields enforced at builder creation</li>
     *   <li><b>Fluent API:</b> Method chaining for concise construction</li>
     * </ul>
     *
     * <p><b>Usage Example - Full Page Sequence:</b></p>
     * <pre>{@code
     * PageSequence page = PageSequence.builder("main-content")
     *     .header(headerContent)
     *     .body(mainContent)
     *     .footer(footerContent)
     *     .build();
     * }</pre>
     *
     * <p><b>Usage Example - Page with Only Body:</b></p>
     * <pre>{@code
     * PageSequence page = PageSequence.builder("title-page")
     *     .body(titleContent)
     *     .build();
     * }</pre>
     *
     * <p><b>Usage Example - Conditional Components:</b></p>
     * <pre>{@code
     * PageSequenceBuilder builder = PageSequence.builder("content-page");
     *
     * if (includeHeader) {
     *     builder.header(headerContent);
     * }
     *
     * builder.body(mainContent);
     *
     * if (includeFooter) {
     *     builder.footer(footerContent);
     * }
     *
     * PageSequence page = builder.build();
     * }</pre>
     *
     * <p><b>Usage Example - Building Multiple Similar Pages:</b></p>
     * <pre>{@code
     * // Create a template builder
     * PageSequenceBuilder template = PageSequence.builder("chapter-page")
     *     .header(standardHeader)
     *     .footer(standardFooter);
     *
     * // Build pages with different bodies
     * PageSequence chapter1 = template.body(chapter1Content).build();
     * PageSequence chapter2 = template.body(chapter2Content).build();
     * }</pre>
     *
     * @param styleClass the CSS-like style class name for page layout;
     *                   must not be {@code null} or empty
     * @return a new PageSequenceBuilder instance initialized with the style class
     * @throws IllegalArgumentException if styleClass is {@code null} or empty
     * @see PageSequenceBuilder
     */
    @PublicAPI
    public static PageSequenceBuilder builder(String styleClass) {
        return new PageSequenceBuilder(styleClass);
    }
}