package de.kaiser.model.style;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a named font style that can be referenced by elements.
 * It combines a font family with specific attributes like size, weight, and style,
 * creating a reusable style definition (e.g., "normal-text", "headline-font").
 *
 */
public record TextStyle(
        @JsonProperty("name") String name,
        @JsonProperty("font-size") String fontSize,
        @JsonProperty("font-family-name") String fontFamilyName,
        @JsonProperty("font-weight") String fontWeight,
        @JsonProperty("font-style") String fontStyle
) {


}
