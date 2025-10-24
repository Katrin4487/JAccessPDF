package de.fkkaiser.model.font;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents a font family, which is a collection of font types (e.g., regular,
 * bold, italic versions of the same font).
 */
public record FontFamily(
        @JsonProperty("font-family") String fontFamily,
        @JsonProperty("types") List<FontType> fontTypes
) {

    public String getName(){
        return fontFamily;
    }
}
