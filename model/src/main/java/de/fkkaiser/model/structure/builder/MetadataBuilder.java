package de.fkkaiser.model.structure.builder;

import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.structure.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Fluent builder for constructing {@link Metadata} instances with a clear, readable API.
 *
 * <p>The builder pattern provides an elegant way to create Metadata objects when multiple
 * properties need to be set. It offers better readability compared to constructor calls
 * with many parameters, and makes it clear which properties are being configured.</p>
 *
 * <p><b>Builder Pattern Benefits:</b></p>
 * <ul>
 *   <li><b>Readability:</b> Each property assignment is explicit and self-documenting</li>
 *   <li><b>Flexibility:</b> Properties can be set in any order</li>
 *   <li><b>Optional Parameters:</b> Only set the properties you need</li>
 *   <li><b>Immutability:</b> Build once, then use (though Metadata itself is mutable)</li>
 *   <li><b>Validation:</b> Required fields are enforced at construction time</li>
 * </ul>
 *
 * <p><b>Required vs. Optional Properties:</b></p>
 * <table>
 *   <caption>Builder Property Requirements</caption>
 *   <tr>
 *     <th>Property</th>
 *     <th>Required</th>
 *     <th>Set Via</th>
 *     <th>Default if Not Set</th>
 *   </tr>
 *   <tr>
 *     <td>title</td>
 *     <td>Yes</td>
 *     <td>Constructor</td>
 *     <td>N/A (required parameter)</td>
 *   </tr>
 *   <tr>
 *     <td>author</td>
 *     <td>No</td>
 *     <td>{@link #author(String)}</td>
 *     <td>null</td>
 *   </tr>
 *   <tr>
 *     <td>subject</td>
 *     <td>No</td>
 *     <td>{@link #subject(String)}</td>
 *     <td>null</td>
 *   </tr>
 *   <tr>
 *     <td>keywords</td>
 *     <td>No</td>
 *     <td>{@link #keywords(List)}</td>
 *     <td>Empty list</td>
 *   </tr>
 *   <tr>
 *     <td>language</td>
 *     <td>No</td>
 *     <td>{@link #language(String)}</td>
 *     <td>"de-DE"</td>
 *   </tr>
 *   <tr>
 *     <td>creationDate</td>
 *     <td>No</td>
 *     <td>{@link #creationDate(LocalDateTime)}</td>
 *     <td>Current timestamp</td>
 *   </tr>
 *   <tr>
 *     <td>displayDocTitle</td>
 *     <td>No</td>
 *     <td>{@link #displayDocTitle(Boolean)}</td>
 *     <td>true</td>
 *   </tr>
 * </table>
 *
 * <p><b>Usage Example 1 - Basic Document:</b></p>
 * <pre>{@code
 * Metadata metadata = Metadata.builder("My Document")
 *     .author("John Doe")
 *     .language("en-US")
 *     .build();
 * }</pre>
 *
 * <p><b>Usage Example 2 - Comprehensive Metadata:</b></p>
 * <pre>{@code
 * Metadata metadata = Metadata.builder("Annual Report 2024")
 *     .author("Finance Department")
 *     .subject("Financial Analysis")
 *     .keywords(List.of("finance", "annual", "report", "2024"))
 *     .language("en-US")
 *     .creationDate(LocalDateTime.of(2024, 12, 31, 23, 59))
 *     .displayDocTitle(true)
 *     .build();
 * }</pre>
 *
 * <p><b>Usage Example 3 - Minimal Metadata:</b></p>
 * <pre>{@code
 * // Only title is required - all other fields use defaults
 * Metadata metadata = Metadata.builder("Simple Document").build();
 * }</pre>
 *
 * <p><b>Usage Example 4 - Localized Documents:</b></p>
 * <pre>{@code
 * // German document
 * Metadata germanDoc = Metadata.builder("Technisches Handbuch")
 *     .author("Engineering Team")
 *     .language("de-DE")
 *     .keywords(List.of("Technik", "Handbuch", "Dokumentation"))
 *     .build();
 *
 * // French document
 * Metadata frenchDoc = Metadata.builder("Guide Technique")
 *     .author("Équipe d'ingénierie")
 *     .language("fr-FR")
 *     .keywords(List.of("technique", "guide", "documentation"))
 *     .build();
 * }</pre>
 *
 * <p><b>Usage Example 5 - Integration with Document Builder:</b></p>
 * <pre>{@code
 * // Create metadata and use directly in document construction
 * Document document = Document.builder(
 *     Metadata.builder("Project Proposal")
 *         .author("Product Team")
 *         .subject("Q1 2025 Initiatives")
 *         .language("en-US")
 *         .build()
 * )
 * .addPageSequence(...)
 * .build();
 * }</pre>
 *
 * <p><b>Language Code Examples:</b></p>
 * Common ISO 639-1 language codes with optional ISO 3166-1 country codes:
 * <ul>
 *   <li>{@code .language("en")} or {@code .language("en-US")} - English (United States)</li>
 *   <li>{@code .language("en-GB")} - English (United Kingdom)</li>
 *   <li>{@code .language("de")} or {@code .language("de-DE")} - German (Germany)</li>
 *   <li>{@code .language("fr")} or {@code .language("fr-FR")} - French (France)</li>
 *   <li>{@code .language("es")} or {@code .language("es-ES")} - Spanish (Spain)</li>
 *   <li>{@code .language("ja")} or {@code .language("ja-JP")} - Japanese (Japan)</li>
 * </ul>
 *
 * <p><b>Builder Lifecycle:</b></p>
 * <ol>
 *   <li><b>Construction:</b> Create builder via {@code Metadata.builder(title)}</li>
 *   <li><b>Configuration:</b> Chain property setters as needed</li>
 *   <li><b>Build:</b> Call {@link #build()} to create the Metadata instance</li>
 *   <li><b>Reuse:</b> Builder can be reused to create multiple similar instances</li>
 * </ol>
 *
 * <p><b>Validation:</b></p>
 * The builder performs validation at construction time:
 * <ul>
 *   <li>Title must not be {@code null} or empty (enforced in constructor)</li>
 *   <li>Warnings are logged for missing recommended fields (e.g., language)</li>
 * </ul>
 *
 * <p><b>Thread Safety:</b></p>
 * This builder is not thread-safe. Each builder instance should be used by a single
 * thread. However, multiple builders can be used concurrently in different threads.
 *
 * @author FK Kaiser
 * @version 1.0
 * @see Metadata
 */
public class MetadataBuilder {

    private static final Logger log = LoggerFactory.getLogger(MetadataBuilder.class);

    private final String title;
    private String author;
    private String subject;
    private List<String> keywords;
    private String language;
    private LocalDateTime creationDate;
    private Boolean displayDocTitle;

    /**
     * Creates a new MetadataBuilder with the required title.
     *
     * <p>The title is the only required field for document metadata, as it is essential
     * for PDF/A compliance and accessibility. Other properties can be set via the builder's
     * fluent methods.</p>
     *
     * <p><b>Note:</b> This constructor is typically not called directly. Instead, use
     * {@link Metadata#builder(String)} for better discoverability.</p>
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * // Preferred approach (via Metadata)
     * Metadata metadata = Metadata.builder("Document Title")
     *     .author("John Doe")
     *     .build();
     *
     * // Direct approach (also valid)
     * Metadata metadata = new MetadataBuilder("Document Title")
     *     .author("John Doe")
     *     .build();
     * }</pre>
     *
     * @param title the document title (required for PDF/A compliance and accessibility);
     *              must not be {@code null} or empty
     * @throws IllegalArgumentException if title is {@code null} or empty
     */
    @PublicAPI
    public MetadataBuilder(String title) {
        if (title == null || title.trim().isEmpty()) {
            log.error("Title cannot be null or empty");
            throw new IllegalArgumentException("Title is required for document metadata");
        }
        this.title = title;
    }

    /**
     * Sets the document author.
     *
     * <p>The author field typically contains the name of the person or organization
     * that created the document. This field is optional but recommended for proper
     * document attribution.</p>
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * metadata.builder("Report")
     *     .author("Jane Smith")
     *     .author("Engineering Department")  // Can be a person or organization
     *     .build();
     * }</pre>
     *
     * @param author the document author name; may be {@code null}
     * @return this builder instance for method chaining
     */
    @PublicAPI
    public MetadataBuilder author(String author) {
        this.author = author;
        return this;
    }

    /**
     * Sets the document subject.
     *
     * <p>The subject field describes the topic or purpose of the document in a few words.
     * This field is optional but helps with document categorization and search.</p>
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * metadata.builder("Q4 Report")
     *     .subject("Financial Analysis")
     *     .subject("Quarterly Performance Review")
     *     .build();
     * }</pre>
     *
     * @param subject the document subject or topic; may be {@code null}
     * @return this builder instance for method chaining
     */
    @PublicAPI
    public MetadataBuilder subject(String subject) {
        this.subject = subject;
        return this;
    }

    /**
     * Sets the document keywords.
     *
     * <p>Keywords are used for document search and categorization. They should be
     * relevant terms that describe the document's content. Multiple keywords improve
     * document discoverability in PDF management systems.</p>
     *
     * <p><b>Best Practices:</b></p>
     * <ul>
     *   <li>Use 3-10 relevant keywords</li>
     *   <li>Include both general and specific terms</li>
     *   <li>Use lowercase for consistency</li>
     *   <li>Avoid special characters</li>
     * </ul>
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * metadata.builder("User Guide")
     *     .keywords(List.of("manual", "documentation", "user guide", "help"))
     *     .keywords(List.of("tutorial", "instructions"))  // Replaces previous keywords
     *     .build();
     * }</pre>
     *
     * @param keywords the list of keywords; if {@code null}, will default to empty list in Metadata
     * @return this builder instance for method chaining
     */
    @PublicAPI
    public MetadataBuilder keywords(List<String> keywords) {
        this.keywords = keywords;
        return this;
    }

    /**
     * Sets the document language.
     *
     * <p>The language should be specified using ISO 639-1 language codes, optionally
     * with ISO 3166-1 country codes for regional variants (e.g., "en-US" for American
     * English vs. "en-GB" for British English).</p>
     *
     * <p><b>Common Language Codes:</b></p>
     * <ul>
     *   <li>"en" or "en-US" - English (United States)</li>
     *   <li>"de" or "de-DE" - German (Germany)</li>
     *   <li>"fr" or "fr-FR" - French (France)</li>
     *   <li>"es" or "es-ES" - Spanish (Spain)</li>
     *   <li>"it" or "it-IT" - Italian (Italy)</li>
     *   <li>"ja" or "ja-JP" - Japanese (Japan)</li>
     * </ul>
     *
     * <p><b>Default:</b> If not set, defaults to "de-DE" (German).</p>
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * // American English document
     * metadata.builder("User Manual")
     *     .language("en-US")
     *     .build();
     *
     * // German document
     * metadata.builder("Benutzerhandbuch")
     *     .language("de-DE")
     *     .build();
     * }</pre>
     *
     * @param language the ISO 639-1 language code, optionally with ISO 3166-1 country code;
     *                 may be {@code null} (defaults to "de-DE")
     * @return this builder instance for method chaining
     */
    @PublicAPI
    public MetadataBuilder language(String language) {
        this.language = language;
        return this;
    }

    /**
     * Sets the document creation timestamp.
     *
     * <p>The creation date indicates when the document was originally created. If not
     * explicitly set, it defaults to the current timestamp when the Metadata object
     * is built.</p>
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * // Use current time (default behavior)
     * metadata.builder("Report").build();
     *
     * // Set specific creation date
     * metadata.builder("Historical Document")
     *     .creationDate(LocalDateTime.of(2020, 1, 1, 0, 0))
     *     .build();
     *
     * // Use document's actual creation time
     * Path docPath = Paths.get("document.txt");
     * LocalDateTime created = LocalDateTime.ofInstant(
     *     Files.getLastModifiedTime(docPath).toInstant(),
     *     ZoneId.systemDefault()
     * );
     * metadata.builder("Document")
     *     .creationDate(created)
     *     .build();
     * }</pre>
     *
     * @param creationDate the document creation timestamp; if {@code null}, current time is used
     * @return this builder instance for method chaining
     */
    @PublicAPI
    public MetadataBuilder creationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    /**
     * Sets whether the document title should be displayed in the PDF viewer window.
     *
     * <p>When {@code true}, PDF readers will display the document title in the window
     * titlebar instead of the filename. This is recommended for better user experience
     * and accessibility.</p>
     *
     * <p><b>Default:</b> {@code true} (title is displayed)</p>
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * // Display title in PDF viewer (recommended)
     * metadata.builder("Annual Report 2024")
     *     .displayDocTitle(true)
     *     .build();
     *
     * // Display filename instead
     * metadata.builder("Technical Specs")
     *     .displayDocTitle(false)
     *     .build();
     * }</pre>
     *
     * @param displayDocTitle {@code true} to display the title in PDF viewer,
     *                        {@code false} to display filename;
     *                        if {@code null}, defaults to {@code true}
     * @return this builder instance for method chaining
     */
    @PublicAPI
    public MetadataBuilder displayDocTitle(Boolean displayDocTitle) {
        this.displayDocTitle = displayDocTitle;
        return this;
    }

    /**
     * Builds and returns the final {@link Metadata} instance with all configured properties.
     *
     * <p>This method creates a new Metadata object using all properties set through the
     * builder's fluent methods. Properties not explicitly set will use the defaults defined
     * in the Metadata class.</p>
     *
     * <p><b>Builder Reuse:</b></p>
     * After calling {@code build()}, the builder can be reused to create additional Metadata
     * instances with the same base configuration:
     * <pre>{@code
     * MetadataBuilder builder = Metadata.builder("Base Document")
     *     .author("John Doe")
     *     .language("en-US");
     *
     * Metadata doc1 = builder.subject("Part 1").build();
     * Metadata doc2 = builder.subject("Part 2").build();
     * }</pre>
     *
     * <p><b>Validation:</b></p>
     * The Metadata constructor performs additional validation and applies defaults
     * for any properties not set through the builder.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * Metadata metadata = Metadata.builder("Document Title")
     *     .author("Jane Smith")
     *     .subject("Important Report")
     *     .keywords(List.of("report", "analysis"))
     *     .language("en-US")
     *     .build();  // Creates the final Metadata instance
     * }</pre>
     *
     * @return a new Metadata instance with all configured properties
     */
    @PublicAPI
    public Metadata build() {
        log.debug("Building Metadata with title: '{}', author: '{}', language: '{}'",
                title, author, language);

        return new Metadata(
                this.title,
                this.author,
                this.subject,
                this.keywords,
                this.language,
                null,  // producer is always set by Metadata constructor
                this.creationDate,
                this.displayDocTitle
        );
    }
}