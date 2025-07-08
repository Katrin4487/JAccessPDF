package de.kaiser.model.font;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum FontStyleValue {
    @JsonProperty("normal")
    NORMAL,

    @JsonProperty("italic")
    ITALIC,

    @JsonProperty("oblique")
    OBLIQUE
}