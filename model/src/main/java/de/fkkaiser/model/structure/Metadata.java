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
import de.fkkaiser.model.JsonPropertyName;
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.annotation.VisibleForTesting;
import de.fkkaiser.model.structure.builder.MetadataBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Represents the metadata of a PDF document.
 *
 * <p>This class encapsulates various metadata properties that can be associated
 * with a PDF document, such as title, author, subject, keywords, language,
 * producer, creation date, and display options.</p>
 *
 * <p>Metadata can be specified using multiple constructors for convenience,
 * or via the {@link MetadataBuilder} for a fluent API.</p>
 *
 * @author Katrin Kaiser
 * @version 1.0.1
 */
public class Metadata {

    private static final Logger log = LoggerFactory.getLogger(Metadata.class);

    private static final String DEFAULT_TITLE = "PDF Dokument";
    private static final String DEFAULT_LANGUAGE = "de-DE";
    private static final String PRODUCER = "de.kaiser.JAccessPDF v1.0";

    @JsonProperty(JsonPropertyName.TITLE)
    private String title;
    @JsonProperty(JsonPropertyName.AUTHOR)
    private String author;
    @JsonProperty(JsonPropertyName.SUBJECT)
    private String subject;
    @JsonProperty(JsonPropertyName.KEYWORDS)
    private List<String> keywords;
    @JsonProperty(JsonPropertyName.LANGUAGE)
    private String language;
    @JsonProperty(JsonPropertyName.PRODUCER)
    private String producer;
    @JsonProperty(JsonPropertyName.CREATION_DATE)
    private LocalDateTime creationDate;
    @JsonProperty(JsonPropertyName.DISPLAY_DOC_TITLE)
    private Boolean displayDocTitle;

    /**
     * Creates a Metadata instance with the specified properties.
     * @param title title of the document
     * @param author author of the document
     * @param subject subject of the document
     * @param keywords list of keywords associated with the document
     * @param language language of the document (e.g., "en-US", "de-DE")
     * @param producer PDF producer identifier
     * @param creationDate creation timestamp of the document
     * @param displayDocTitle whether to display the document title in the PDF viewer
     * @throws NullPointerException if {@code title} is {@code null}
     */
    @PublicAPI
    public Metadata(String title, String author, String subject, List<String> keywords,
                    String language, String producer, LocalDateTime creationDate, Boolean displayDocTitle) {

        Objects.requireNonNull(title, "Title must not be null");
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
    @Internal
    public Metadata() {
        // Empty constructor for Jackson
    }

    /**
     * Creates metadata with only a title, using defaults for all other properties.
     *
     * <p>This is a convenience constructor for documents where only the title
     * is required.</p>
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * Metadata metadata = new Metadata("My Document Title");
     * }</pre>
     *
     * @param title the document title; must not be {@code null}
     */
    @PublicAPI
    public Metadata(String title) {
        this(title, null, null, null, null, null, null, null);
    }

    /**
     * Creates metadata with a title and language, using defaults for other properties.
     *
     * <p>This constructor allows specifying the document language along with the title.</p>
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * Metadata metadata = new Metadata("My Document Title", "en-US");
     * }</pre>
     *
     * @param title the document title; must not be {@code null}
     * @param language the document language in ISO 639-1 format; must not be {@code null}
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
    @Internal
    public String getTitle() {
        return title;
    }

    /**
     * Returns the document author.
     *
     * @return the author, or {@code null} if not set
     */
    @Internal
    public String getAuthor() {
        return author;
    }

    /**
     * Returns the document subject.
     *
     * @return the subject, or {@code null} if not set
     */
    @Internal
    public String getSubject() {
        return subject;
    }

    /**
     * Returns the document keywords.
     *
     * @return an immutable list of keywords, never {@code null} (empty list if not set)
     */
    @Internal
    public List<String> getKeywords() {
        return keywords;
    }

    /**
     * Returns the document language.
     *
     * @return the language in ISO 639-1 format, never {@code null}
     *         (defaults to "de-DE" if not set)
     */
    @Internal
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
    @Internal
    public String getProducer() {
        return producer;
    }

    /**
     * Returns the document creation timestamp.
     *
     * @return the creation date and time, never {@code null}
     *         (defaults to construction time if not set)
     */
    @Internal
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Returns whether the document title should be displayed in the PDF viewer window.
     *
     * @return {@code true} if the title should be displayed, {@code false} otherwise;
     *         never {@code null} (defaults to {@code true} if not set)
     */
    @Internal
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
     * @param title the new title; if {@code null}
     * @throws NullPointerException if {@code title} is {@code null}
     */
    @PublicAPI
    public void setTitle(String title) {
        Objects.requireNonNull(title, "Title must not be null");
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

    @VisibleForTesting
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

    /**
     * Creates a new {@link MetadataBuilder} for constructing Metadata instances.
     *
     * @param title The title of the document.
     * @return A new MetadataBuilder instance.
     */
    @PublicAPI
    public static MetadataBuilder builder(String title) {
        return new MetadataBuilder(title);
    }
}