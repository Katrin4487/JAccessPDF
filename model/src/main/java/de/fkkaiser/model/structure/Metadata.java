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
import de.fkkaiser.model.structure.builder.MetadataBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Represents metadata for a PDF document conforming to PDF/A and accessibility standards.
 *
 * <p>Metadata provides descriptive information about the document that is embedded in the
 * generated PDF file. This information is used by PDF readers, search engines, and
 * accessibility tools to provide better user experience and document management.</p>
 *
 * <p><b>PDF/A Compliance:</b></p>
 * For PDF/A compliance, at minimum the {@code title} should be provided. Other fields
 * enhance document discoverability and accessibility but are optional.
 *
 * <p><b>Mutability:</b></p>
 * This class is intentionally mutable to support both JSON deserialization and programmatic
 * configuration. All fields can be modified after construction through setter methods or
 * during JSON deserialization via Jackson.
 *
 * <p><b>Required vs. Optional Fields:</b></p>
 * <table>
 *   <caption>Metadata Field Requirements</caption>
 *   <tr>
 *     <th>Field</th>
 *     <th>Required</th>
 *     <th>Default Value</th>
 *     <th>Description</th>
 *   </tr>
 *   <tr>
 *     <td>title</td>
 *     <td>Yes</td>
 *     <td>"PDF Dokument"</td>
 *     <td>Document title (accessibility requirement)</td>
 *   </tr>
 *   <tr>
 *     <td>language</td>
 *     <td>Recommended</td>
 *     <td>"de-DE"</td>
 *     <td>Document language (ISO 639-1 format)</td>
 *   </tr>
 *   <tr>
 *     <td>author</td>
 *     <td>No</td>
 *     <td>null</td>
 *     <td>Document author or creator</td>
 *   </tr>
 *   <tr>
 *     <td>subject</td>
 *     <td>No</td>
 *     <td>null</td>
 *     <td>Document subject or topic</td>
 *   </tr>
 *   <tr>
 *     <td>keywords</td>
 *     <td>No</td>
 *     <td>Empty list</td>
 *     <td>Search keywords</td>
 *   </tr>
 *   <tr>
 *     <td>creationDate</td>
 *     <td>No</td>
 *     <td>Current time</td>
 *     <td>Document creation timestamp</td>
 *   </tr>
 *   <tr>
 *     <td>displayDocTitle</td>
 *     <td>No</td>
 *     <td>true</td>
 *     <td>Show title in PDF viewer window</td>
 *   </tr>
 *   <tr>
 *     <td>producer</td>
 *     <td>Auto-set</td>
 *     <td>"de.kaiser.JAccessPDF v1.0"</td>
 *     <td>PDF generation software (read-only)</td>
 *   </tr>
 * </table>
 *
 * <p><b>Usage Example 1 - Simple Construction:</b></p>
 * <pre>{@code
 * // Minimal metadata with just a title
 * Metadata metadata = new Metadata("My Document");
 *
 * // With title and language
 * Metadata metadata = new Metadata("Mein Dokument", "de-DE");
 * }</pre>
 *
 * <p><b>Usage Example 2 - Full Construction:</b></p>
 * <pre>{@code
 * Metadata metadata = new Metadata(
 *     "Annual Report 2024",                    // title
 *     "Jane Doe",                               // author
 *     "Financial Analysis",                     // subject
 *     List.of("finance", "annual", "report"),  // keywords
 *     "en-US",                                  // language
 *     null,                                     // producer (auto-set)
 *     LocalDateTime.now(),                      // creationDate
 *     true                                      // displayDocTitle
 * );
 * }</pre>
 *
 * <p><b>Usage Example 3 - Builder Pattern (Recommended):</b></p>
 * <pre>{@code
 * Metadata metadata = Metadata.builder("Project Proposal")
 *     .author("John Smith")
 *     .subject("Q1 2025 Initiatives")
 *     .keywords(List.of("proposal", "strategy", "2025"))
 *     .language("en-US")
 *     .creationDate(LocalDateTime.now())
 *     .displayDocTitle(true)
 *     .build();
 * }</pre>
 *
 * <p><b>Usage Example 4 - Programmatic Modification:</b></p>
 * <pre>{@code
 * // Start with basic metadata
 * Metadata metadata = new Metadata("Draft Document");
 *
 * // Update as information becomes available
 * metadata.setAuthor("Jane Doe");
 * metadata.setSubject("Project Planning");
 * metadata.setLanguage("en-US");
 * metadata.setKeywords(List.of("draft", "planning"));
 *
 * // Use in document
 * Document doc = Document.builder(metadata)
 *     .addPageSequence(...)
 *     .build();
 * }</pre>
 *
 * <p><b>Language Codes:</b></p>
 * The language field should use ISO 639-1 language codes with optional ISO 3166-1
 * country codes:
 * <ul>
 *   <li>"en" or "en-US" - English (United States)</li>
 *   <li>"en-GB" - English (United Kingdom)</li>
 *   <li>"de" or "de-DE" - German (Germany)</li>
 *   <li>"fr" or "fr-FR" - French (France)</li>
 *   <li>"es" or "es-ES" - Spanish (Spain)</li>
 * </ul>
 *
 * <p><b>Thread Safety:</b></p>
 * This class is not thread-safe. If shared across threads, external synchronization
 * is required. In typical usage, metadata is configured in a single thread before
 * document generation begins.
 *
 * @author FK Kaiser
 * @version 1.1
 * @see Document
 * @see MetadataBuilder
 */
public class Metadata {

    private static final Logger log = LoggerFactory.getLogger(Metadata.class);

    private static final String DEFAULT_TITLE = "PDF Dokument";
    private static final String DEFAULT_LANGUAGE = "de-DE";
    private static final String PRODUCER = "de.kaiser.JAccessPDF v1.0";

    private String title;
    private String author;
    private String subject;

    @JsonProperty("keywords")
    private List<String> keywords;

    private String language;
    private String producer;

    @JsonProperty("creation-date")
    private LocalDateTime creationDate;

    @JsonProperty("display-doc-title")
    private Boolean displayDocTitle;

    /**
     * Creates document metadata with all properties specified.
     *
     * <p>This is the primary constructor used for complete metadata specification.
     * Most parameters can be {@code null}, in which case sensible defaults are applied:</p>
     * <ul>
     *   <li><b>title:</b> Defaults to "PDF Dokument" (logged as warning)</li>
     *   <li><b>language:</b> Defaults to "de-DE" (logged as warning)</li>
     *   <li><b>producer:</b> Always set to library identifier (parameter ignored if provided)</li>
     *   <li><b>keywords:</b> Defaults to empty list</li>
     *   <li><b>creationDate:</b> Defaults to current timestamp</li>
     *   <li><b>displayDocTitle:</b> Defaults to true</li>
     *   <li><b>author, subject:</b> Remain null if not provided</li>
     * </ul>
     *
     * @param title           the document title; if {@code null}, uses default "PDF Dokument"
     * @param author          the document author; may be {@code null}
     * @param subject         the document subject or topic; may be {@code null}
     * @param keywords        the search keywords; if {@code null}, uses empty list
     * @param language        the document language in ISO 639-1 format (e.g., "en-US");
     *                        if {@code null}, uses default "de-DE"
     * @param producer        the PDF producer identifier; this parameter is ignored and
     *                        always set to the library identifier
     * @param creationDate    the document creation timestamp; if {@code null}, uses current time
     * @param displayDocTitle whether to display the title in PDF viewer window;
     *                        if {@code null}, defaults to true
     */
    @PublicAPI
    public Metadata(String title, String author, String subject, List<String> keywords,
                    String language, String producer, LocalDateTime creationDate, Boolean displayDocTitle) {

        // Producer is always set to library identifier
        this.producer = Objects.requireNonNullElse(producer, PRODUCER);

        // Title with validation and default
        if (title == null || title.trim().isEmpty()) {
            log.warn("Title is null or empty. Setting default value '{}'", DEFAULT_TITLE);
            this.title = DEFAULT_TITLE;
        } else {
            this.title = title;
        }

        // Language with validation and default
        if (language == null || language.trim().isEmpty()) {
            log.warn("Language is null or empty. Setting default value '{}'", DEFAULT_LANGUAGE);
            this.language = DEFAULT_LANGUAGE;
        } else {
            this.language = language;
        }

        // Optional fields
        this.author = author;
        this.subject = subject;
        this.keywords = (keywords == null) ? List.of() : List.copyOf(keywords);
        this.creationDate = (creationDate == null) ? LocalDateTime.now() : creationDate;
        this.displayDocTitle = displayDocTitle == null || displayDocTitle;
    }

    /**
     * No-argument constructor for Jackson deserialization.
     *
     * <p>Creates a Metadata instance with all fields uninitialized. Jackson will
     * populate fields from JSON. This constructor should not be used directly in
     * application code.</p>
     */
    @PublicAPI
    public Metadata() {
        // Empty constructor for Jackson
    }

    /**
     * Creates metadata with only a title, using defaults for all other properties.
     *
     * <p>This is a convenience constructor for simple documents where only the title
     * is known at construction time. Other properties can be set later using setter methods.</p>
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * Metadata metadata = new Metadata("My Document");
     * metadata.setAuthor("John Doe");
     * metadata.setLanguage("en-US");
     * }</pre>
     *
     * @param title the document title; must not be {@code null}
     */
    @PublicAPI
    public Metadata(String title) {
        this(title, null, null, null, null, null, null, null);
    }

    /**
     * Creates metadata with a title and language, using defaults for all other properties.
     *
     * <p>This is a convenience constructor for documents where title and language are
     * the primary metadata requirements.</p>
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * Metadata metadata = new Metadata("Technisches Handbuch", "de-DE");
     * }</pre>
     *
     * @param title    the document title; must not be {@code null}
     * @param language the document language in ISO 639-1 format (e.g., "en-US", "de-DE");
     *                 must not be {@code null}
     */
    @PublicAPI
    public Metadata(String title, String language) {
        this(title, null, null, null, language, null, null, null);
    }

    // ==================== Getters ====================

    /**
     * Returns the document title.
     *
     * @return the title, never {@code null} (defaults to "PDF Dokument" if not set)
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the document author.
     *
     * @return the author, or {@code null} if not set
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Returns the document subject.
     *
     * @return the subject, or {@code null} if not set
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Returns the document keywords.
     *
     * @return an immutable list of keywords, never {@code null} (empty list if not set)
     */
    public List<String> getKeywords() {
        return keywords;
    }

    /**
     * Returns the document language.
     *
     * @return the language in ISO 639-1 format, never {@code null}
     *         (defaults to "de-DE" if not set)
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Returns the PDF producer identifier.
     *
     * <p>This value is automatically set to the library identifier and cannot be changed.</p>
     *
     * @return the producer string, never {@code null}
     */
    public String getProducer() {
        return producer;
    }

    /**
     * Returns the document creation timestamp.
     *
     * @return the creation date and time, never {@code null}
     *         (defaults to construction time if not set)
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Returns whether the document title should be displayed in the PDF viewer window.
     *
     * @return {@code true} if the title should be displayed, {@code false} otherwise;
     *         never {@code null} (defaults to {@code true} if not set)
     */
    public Boolean isDisplayDocTitle() {
        return displayDocTitle;
    }

    // ==================== Setters ====================

    /**
     * Sets the document title.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * metadata.setTitle("Updated Document Title");
     * }</pre>
     *
     * @param title the new title; if {@code null} or empty, a warning is logged
     */
    @PublicAPI
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            log.warn("Setting title to null or empty value");
        }
        this.title = title;
    }

    /**
     * Sets the document author.
     *
     * @param author the author name; may be {@code null}
     */
    @PublicAPI
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Sets the document subject.
     *
     * @param subject the subject or topic; may be {@code null}
     */
    @PublicAPI
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Sets the document keywords.
     *
     * <p>The provided list is copied to ensure immutability of the stored list.</p>
     *
     * @param keywords the list of keywords; if {@code null}, an empty list is stored
     */
    @PublicAPI
    public void setKeywords(List<String> keywords) {
        this.keywords = (keywords == null) ? List.of() : List.copyOf(keywords);
    }

    /**
     * Sets the document language.
     *
     * <p>The language should be specified in ISO 639-1 format, optionally with
     * an ISO 3166-1 country code (e.g., "en", "en-US", "de-DE").</p>
     *
     * @param language the language code; if {@code null} or empty, a warning is logged
     */
    @PublicAPI
    public void setLanguage(String language) {
        if (language == null || language.trim().isEmpty()) {
            log.warn("Setting language to null or empty value");
        }
        this.language = language;
    }

    /**
     * Sets the document creation timestamp.
     *
     * @param creationDate the creation date and time; if {@code null}, current time is used
     */
    @PublicAPI
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = (creationDate == null) ? LocalDateTime.now() : creationDate;
    }

    /**
     * Sets whether the document title should be displayed in the PDF viewer window.
     *
     * @param displayDocTitle {@code true} to display the title, {@code false} otherwise;
     *                        if {@code null}, defaults to {@code true}
     */
    @PublicAPI
    public void setDisplayDocTitle(Boolean displayDocTitle) {
        this.displayDocTitle = displayDocTitle == null || displayDocTitle;
    }

    // ==================== Object Methods ====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Metadata metadata = (Metadata) o;
        return Objects.equals(title, metadata.title) &&
                Objects.equals(author, metadata.author) &&
                Objects.equals(subject, metadata.subject) &&
                Objects.equals(keywords, metadata.keywords) &&
                Objects.equals(language, metadata.language) &&
                Objects.equals(producer, metadata.producer) &&
                Objects.equals(creationDate, metadata.creationDate) &&
                Objects.equals(displayDocTitle, metadata.displayDocTitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, subject, keywords, language, producer, creationDate, displayDocTitle);
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", subject='" + subject + '\'' +
                ", keywords=" + keywords +
                ", language='" + language + '\'' +
                ", producer='" + producer + '\'' +
                ", creationDate=" + creationDate +
                ", displayDocTitle=" + displayDocTitle +
                '}';
    }

    // ==================== Builder Factory ====================

    /**
     * Creates and returns a new {@link MetadataBuilder} for fluent metadata construction.
     *
     * <p>The builder pattern is the recommended way to create Metadata instances with
     * multiple properties, as it provides a clear, readable API.</p>
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * Metadata metadata = Metadata.builder("Annual Report 2024")
     *     .author("Finance Department")
     *     .subject("Financial Analysis")
     *     .keywords(List.of("finance", "annual", "report"))
     *     .language("en-US")
     *     .displayDocTitle(true)
     *     .build();
     * }</pre>
     *
     * @param title the document title (required); must not be {@code null} or empty
     * @return a new MetadataBuilder instance
     * @see MetadataBuilder
     */
    @PublicAPI
    public static MetadataBuilder builder(String title) {
        return new MetadataBuilder(title);
    }
}