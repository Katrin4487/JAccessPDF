package de.fkkaiser.model.style;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Optional;

/**
 * Represents the root stylesheet, containing font definitions, font styles,
 * element styles, and page layouts. This class is immutable.
 */
public record StyleSheet(
        @JsonProperty("text-styles") List<TextStyle> textStyles,
        @JsonProperty("element-styles") List<ElementStyle> elementStyles,
        @JsonProperty("page-master-styles") List<PageMasterStyle> pageMasterStyles
) {


    /**
     * Finds a text style by its name.
     *
     * @param name The name of the text style (e.g., "regular-text").
     * @return An Optional containing the found TextStyle, or empty if not found.
     */
    public Optional<TextStyle> findFontStyleByName(String name) {
        if (this.textStyles == null || name == null) {
            return Optional.empty();
        }
        return this.textStyles.stream()
                .filter(fs -> name.equals(fs.name()))
                .findFirst();
    }
}
