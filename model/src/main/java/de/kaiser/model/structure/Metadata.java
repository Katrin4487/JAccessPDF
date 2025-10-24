package de.kaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Represents metadata for a document.
 * <P>
 * Constructor parameters:
 * </P>
 * <ul>
 * <li>title: the title of the document</li>
 * <li>author: the author of the document</li>
 * <li>subject: the subject of the document</li>
 * <li>keywords: the keywords associated with the document</li>
 * <li>language: the language of the document</li>
 * <li>producer: the producer of the document</li>
 * <li>creationDate: the creation date of the document</li>
 * <li>displayDocTitle: flag indicating whether to display the document title </li>
 * </ul>
 * <p>
 * Default values:
 * </p>
 * <ul>
 * <li>DEFAULT_TITLE: Default title if the provided title is null</li>
 * <li>DEFAULT_LANGUAGE: Default language if the provided language is null</li>
 * <li>PRODUCER: Default producer information </li>
 * </ul>
 *
 * <p>
 * Constructor initializes metadata values with provided parameters and default values where necessary.
 * Contains additional constructors for variations of parameter combinations.
 * </p>
 *
 * Builder class: Provides a way to construct Metadata objects using a fluent builder pattern and
 * allows setting individual metadata properties before building the Metadata object.
 * <p>
 *    Example usage:
 * </p>
 * {@code Metadata metadata = Metadata.builder("Sample Document")
 *                            .author("John Doe")
 *                            .subject("Sample")
 *                            .keywords(List.of("sample", "document"))
 *                            .language("en-US")
 *                            .creationDate(LocalDateTime.now())
 *                            .displayDocTitle(true)
 *                            .build();}
 */
public class Metadata {

    private static final Logger log = LoggerFactory.getLogger(Metadata.class);

    private static final String DEFAULT_TITLE = "PDF Dokument";
    private static final String DEFAULT_LANGUAGE = "de-DE";
    private static final String PRODUCER = "de.kaiser.JAccessPDF v1.0";

    private  String title;
    private  String author;
    private  String subject;
    @JsonProperty("keywords")
    private  List<String> keywords;
    private String language;
    private  String producer;
    @JsonProperty("creation-date")
    private  LocalDateTime creationDate;
    @JsonProperty("display-doc-title")
    private  Boolean displayDocTitle;

    /**
     * Represents metadata for a document.
     *
     * @param title the title of the document
     * @param author the author of the document
     * @param subject the subject of the document
     * @param keywords the keywords associated with the document
     * @param language the language of the document
     * @param producer the producer of the document
     * @param creationDate the creation date of the document
     * @param displayDocTitle flag indicating whether to display the document title
     */
    public Metadata(String title, String author, String subject, List<String> keywords,
                    String language, String producer, LocalDateTime creationDate, Boolean displayDocTitle) {

        this.producer = PRODUCER;

        if (title == null) {
            log.warn("Title is null. Setting default value '{}'", DEFAULT_TITLE);
            this.title = DEFAULT_TITLE;
        } else {
            this.title = title;
        }

        if (language == null) {
            log.warn("Language is null. Setting default value '{}'", DEFAULT_LANGUAGE);
            this.language = DEFAULT_LANGUAGE;
        } else {
            this.language = language;
        }

        this.author = author;
        this.subject = subject;
        this.keywords = (keywords == null) ? List.of() : keywords;
        this.creationDate = (creationDate == null) ? LocalDateTime.now() : creationDate;
        this.displayDocTitle = displayDocTitle == null || displayDocTitle;
    }

    public Metadata() {}

    /**
     * Constructs Metadata with a title, using defaults for all other properties.
     * @param title the title of the document
     */
    public Metadata(String title) {
        this(title, null, null, null, null, null, null, null);
    }

    /**
     * Constructs Metadata with a title and a language, using defaults for all other properties.
     * @param title the title of the document
     * @param language the language of the document
     */
    public Metadata(String title, String language) {
        this(title, null, null, null, language, null, null, null);
    }

    // Getter methods
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getSubject() { return subject; }
    public List<String> getKeywords() { return keywords; }
    public String getLanguage() { return language; }
    public String getProducer() { return producer; }
    public LocalDateTime getCreationDate() { return creationDate; }
    public Boolean isDisplayDocTitle() { return displayDocTitle; }

    public void setLanguage(String language) {
        this.language = language;
    }

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

    public static Builder builder(String title) {
        return new Builder(title);
    }

    /**
     * A Builder class for creating Metadata objects that represent document metadata.
     */
    public static class Builder {
        private final String title;
        private String author;
        private String subject;
        private List<String> keywords;
        private String language;
        private LocalDateTime creationDate;
        private Boolean displayDocTitle;

        public Builder(String title) {
            this.title = title;
        }

        public Builder author(String author) { this.author = author; return this; }
        public Builder subject(String subject) { this.subject = subject; return this; }
        public Builder keywords(List<String> keywords) { this.keywords = keywords; return this; }
        public Builder language(String language) { this.language = language; return this; }
        public Builder creationDate(LocalDateTime creationDate) { this.creationDate = creationDate; return this; }
        public Builder displayDocTitle(Boolean displayDocTitle) { this.displayDocTitle = displayDocTitle; return this; }

        public Metadata build() {
            return new Metadata(
                    this.title,
                    this.author,
                    this.subject,
                    this.keywords,
                    this.language,
                    null,
                    this.creationDate,
                    this.displayDocTitle
            );
        }
    }
}