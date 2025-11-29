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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a hyperlink element that can be embedded within text content.
 *
 * <p>Hyperlink extends {@link TextRun} to provide clickable text elements that navigate
 * to specified URLs or document destinations. In rendered PDFs, hyperlinks appear as
 * interactive elements that can be clicked to open web pages, email clients, or jump
 * to specific locations within the document.</p>
 *
 * <p><b>Purpose in PDF Generation:</b></p>
 * During PDF rendering, hyperlinks are converted to PDF annotations that provide
 * interactive functionality:
 * <ul>
 *   <li><b>External Links:</b> URLs starting with http://, https://, or mailto:</li>
 *   <li><b>Internal Links:</b> References to document destinations or named anchors</li>
 *   <li><b>Visual Styling:</b> Typically rendered with distinct styling (color, underline)</li>
 *   <li><b>Accessibility:</b> Tagged with alternative text for screen readers</li>
 * </ul>
 *
 * <p><b>Inheritance from TextRun:</b></p>
 * By extending {@link TextRun}, Hyperlink inherits all text content and styling
 * capabilities while adding link-specific functionality. This means hyperlinks
 * participate in the standard inline element system, support style resolution,
 * and can be mixed with other inline elements in paragraphs and text blocks.
 *
 * <p><b>Link Target (href):</b></p>
 * The href property specifies the link destination and supports various formats:
 * <ul>
 *   <li><b>HTTP/HTTPS URLs:</b> {@code https://example.com}</li>
 *   <li><b>Email addresses:</b> {@code mailto:info@example.com}</li>
 *   <li><b>Internal references:</b> {@code #section-1} (implementation-dependent)</li>
 * </ul>
 *
 * <p>If href is {@code null} or blank, an empty string is used and a warning is logged.
 * This ensures the hyperlink object is valid even without a destination, though such
 * links will not function as expected in the rendered PDF.</p>
 *
 * <p><b>Accessibility with Alternative Text:</b></p>
 * The altText property provides alternative text for accessibility compliance.
 * This text is used by screen readers when the link is encountered, helping
 * visually impaired users understand the link's purpose. If no alternative text
 * is provided, the visible text content is used as fallback, ensuring every
 * hyperlink has accessible text.
 *
 * <p><b>Text Content Handling:</b></p>
 * If the text content is {@code null} or blank, the href value can be displayed
 * as fallback text (implementation-dependent). This is common for "naked URLs"
 * where the URL itself serves as both the destination and visible text.
 *
 * <p><b>JSON Representation:</b></p>
 * <pre>{@code
 * {
 *   "type": "hyperlink",
 *   "text": "Visit our website",
 *   "href": "https://example.com",
 *   "alt-text": "Link to Example Company homepage",
 *   "style-class": "external-link",
 *   "variant": "primary"
 * }
 * }</pre>
 *
 * <p><b>Usage Example 1 - External Web Link:</b></p>
 * <pre>{@code
 * // Simple three-parameter constructor
 * Hyperlink websiteLink = new Hyperlink(
 *     "Visit Example.com",
 *     "external-link",
 *     "https://example.com"
 * );
 * }</pre>
 *
 * <p><b>Usage Example 2 - Email Link with Accessibility:</b></p>
 * <pre>{@code
 * // Constructor with alternative text for accessibility
 * Hyperlink emailLink = new Hyperlink(
 *     "Contact Us",
 *     "email-link",
 *     "mailto:support@example.com",
 *     "Send email to customer support"
 * );
 * }</pre>
 *
 * <p><b>Usage Example 3 - Full Configuration:</b></p>
 * <pre>{@code
 * // Complete constructor with all parameters including variant
 * Hyperlink advancedLink = new Hyperlink(
 *     "Read Documentation",
 *     "doc-link",
 *     "technical",
 *     "https://docs.example.com/guide",
 *     "Link to technical documentation and setup guide"
 * );
 * }</pre>
 *
 * <p><b>Validation and Logging:</b></p>
 * The constructor performs validation and logging for edge cases:
 * <ul>
 *   <li>Null/blank text → Info log, href may be used as display text</li>
 *   <li>Null/blank href → Warning log, empty string assigned</li>
 *   <li>Null/blank altText → Info log, text content used as fallback</li>
 * </ul>
 *
 * <p><b>Style Resolution:</b></p>
 * Hyperlink inherits style resolution from {@link TextRun}. Link-specific styling
 * (such as color, text decoration, or hover effects in interactive PDFs) can be
 * configured through the styleClass. Common styling includes blue or underlined
 * text to visually distinguish links from regular text.
 *
 * <p><b>Best Practices:</b></p>
 * <ul>
 *   <li>Always provide meaningful text content that describes the link destination</li>
 *   <li>Use descriptive alternative text for accessibility</li>
 *   <li>Validate href values to ensure they are well-formed URLs</li>
 *   <li>Use consistent styling for similar link types (external, internal, email)</li>
 *   <li>Consider mobile/tablet viewing when sizing clickable areas</li>
 * </ul>
 *
 * @author FK Kaiser
 * @version 1.0
 * @see TextRun
 * @see InlineElement
 * @see InlineElementTypes
 */
public class Hyperlink extends TextRun {

    private static final Logger log = LoggerFactory.getLogger(Hyperlink.class);

    private final String href;
    private final String altText;

    /**
     * Creates a Hyperlink element with full configuration.
     *
     * <p>This constructor is the primary constructor used by Jackson during JSON
     * deserialization. It provides complete control over all hyperlink properties
     * including the optional variant and alternative text.</p>
     *
     * <p><b>Parameter Validation:</b></p>
     * The constructor performs validation and applies fallback values:
     * <ul>
     *   <li><b>text:</b> If {@code null} or blank, logs info message. The href may
     *       be used as display text during rendering (implementation-dependent).</li>
     *   <li><b>href:</b> If {@code null} or blank, logs warning and assigns empty
     *       string. The hyperlink will not function without a valid destination.</li>
     *   <li><b>altText:</b> If {@code null} or blank, logs info message and uses
     *       the text parameter as fallback for accessibility.</li>
     * </ul>
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * Hyperlink link = new Hyperlink(
     *     "Download PDF",
     *     "download-link",
     *     "primary",
     *     "https://example.com/document.pdf",
     *     "Link to download the user manual in PDF format"
     * );
     * }</pre>
     *
     * @param text       the visible text content of the hyperlink; may be {@code null} or blank
     * @param styleClass the CSS-like style class for styling properties; may be {@code null}
     * @param variant    an optional variant identifier for style variations; may be {@code null}
     * @param href       the URL or destination of the hyperlink; if {@code null} or blank,
     *                   an empty string is assigned and a warning is logged
     * @param altText    the alternative text for accessibility; if {@code null} or blank,
     *                   the text parameter is used as fallback
     */
    @JsonCreator
    public Hyperlink(
            @JsonProperty("text") String text,
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("variant") String variant,
            @JsonProperty("href") String href,
            @JsonProperty("alt-text") String altText
    ) {
        super(text, styleClass, variant);

        if (text == null || text.isBlank()) {
            log.info("text is null; Setting href as text");
        }
        if (href == null || href.isBlank()) {
            log.warn("Hyperlink created with empty or null href");
            this.href = "";
        } else {
            this.href = href;
        }
        if (altText == null || altText.isBlank()) {
            log.info("altText is null; Setting text as altText");
            this.altText = text;
        } else {
            this.altText = altText;
        }
    }

    /**
     * Creates a Hyperlink with text, style class, and destination URL.
     *
     * <p>This convenience constructor is suitable for most common hyperlink scenarios
     * where variant and explicit alternative text are not needed. The variant is set
     * to {@code null}, and the alternative text defaults to the visible text content.</p>
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * Hyperlink simpleLink = new Hyperlink(
     *     "Visit our website",
     *     "external-link",
     *     "https://example.com"
     * );
     * }</pre>
     *
     * <p>This is the most commonly used constructor for straightforward hyperlinks
     * that don't require additional configuration.</p>
     *
     * @param text       the visible text content of the hyperlink; may be {@code null} or blank
     * @param styleClass the CSS-like style class for styling properties; may be {@code null}
     * @param href       the URL or destination of the hyperlink; if {@code null} or blank,
     *                   an empty string is assigned and a warning is logged
     */
    public Hyperlink(String text, String styleClass, String href) {
        this(text, styleClass, null, href, null);
    }

    /**
     * Creates a Hyperlink with text, style class, destination URL, and alternative text.
     *
     * <p>This constructor is useful when you need to provide explicit alternative text
     * for accessibility purposes but don't need to specify a variant. The variant is
     * set to {@code null}.</p>
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * Hyperlink accessibleLink = new Hyperlink(
     *     "Download",
     *     "download-link",
     *     "https://example.com/manual.pdf",
     *     "Download the complete user manual in PDF format (2.3 MB)"
     * );
     * }</pre>
     *
     * <p>Use this constructor when the alternative text should provide more context
     * than the visible text alone, such as file sizes, format information, or
     * detailed descriptions of the link destination.</p>
     *
     * @param text       the visible text content of the hyperlink; may be {@code null} or blank
     * @param styleClass the CSS-like style class for styling properties; may be {@code null}
     * @param href       the URL or destination of the hyperlink; if {@code null} or blank,
     *                   an empty string is assigned and a warning is logged
     * @param altText    the alternative text for accessibility; if {@code null} or blank,
     *                   the text parameter is used as fallback
     */
    @SuppressWarnings("unused")
    public Hyperlink(String text, String styleClass, String href, String altText) {
        this(text, styleClass, null, href, altText);
    }

    /**
     * Returns the URL or destination of this hyperlink.
     *
     * <p>The href value specifies where the link navigates when clicked. Common
     * formats include HTTP/HTTPS URLs, mailto links, and internal document references.
     * If the hyperlink was created with a {@code null} or blank href, this method
     * returns an empty string.</p>
     *
     * @return the hyperlink destination URL; never {@code null}, may be empty
     */
    public String getHref() {
        return href;
    }

    /**
     * Returns the alternative text for accessibility purposes.
     *
     * <p>The alternative text provides a description of the link for screen readers
     * and assistive technologies. If no explicit alternative text was provided during
     * construction, this returns the visible text content as fallback.</p>
     *
     * @return the alternative text; may be {@code null} if both altText and text were null
     */
    public String getAltText() {
        return altText;
    }

    /**
     * Returns the element type identifier.
     *
     * @return the constant {@link InlineElementTypes#HYPERLINK}
     */
    @Override
    public String getType() {
        return InlineElementTypes.HYPERLINK;
    }
}