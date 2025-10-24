package de.fkkaiser.model.font;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enum representing different font style values: normal, italic, and oblique.
 */
public enum FontStyleValue {
    @JsonProperty("normal")
    NORMAL,

    @JsonProperty("italic")
    ITALIC,

    @JsonProperty("oblique")
    OBLIQUE;

    @Override
    public String toString() {
        return switch (this) {
            case NORMAL -> "normal";
            case ITALIC -> "italic";
            case OBLIQUE -> "oblique";
            default ->
                //Should not happen
                    "HAPPENS";
        };
    }
}