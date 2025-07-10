package de.kaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Contains the document's metadata.
 * This includes both standard information and important
 * settings for accessibility.
 */
public record Metadata(

        String title,
        String author,
        String subject,
        @JsonProperty("keywords")
        List<String> keywords,
        String language,
        String producer,
        @JsonProperty("creation-date")  LocalDateTime creationDate,
        @JsonProperty("display-doc-title") boolean displayDocTitle
) {
    /**
     * Sets the creation date of the EMetadata if it is not provided in the JSON.
     * If creation date is null, it uses the current date and time.
     *
     * @param creationDate The creation date of the metadata.
     */
    public Metadata {
        //
        //If no creation date is specified in the JSON, use the current date.
        if (creationDate == null) {
            creationDate = LocalDateTime.now();
        }
        // lists should not be null
        if (keywords == null) {
            keywords = List.of();
        }

        producer = "de.kaiser.JAccessPDF v1.0";

    }


}
