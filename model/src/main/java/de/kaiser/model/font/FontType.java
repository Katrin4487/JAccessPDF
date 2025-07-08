package de.kaiser.model.font;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a specific font type within a font family,
 * linking a font file to a specific style (e.g., "italic")
 * and weight (e.g., "700").
 *
 */
public record FontType(
        @JsonProperty("path") String path,
        @JsonProperty("style") String fontStyle,
        @JsonProperty("weight") String fontWeight
) {

}
