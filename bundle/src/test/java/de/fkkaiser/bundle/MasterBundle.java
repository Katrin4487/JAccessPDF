package de.fkkaiser.bundle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.font.FontFamilyList;
import de.fkkaiser.model.style.StyleSheet;

/**
 * Obect that contains all the data needed to generate a PDF document with placeholders
 * @param documentTree JSON tree of the document
 * @param styleSheet Stylesheet
 * @param fonts Font families
 * @param textBundle Text bundle with content
 */
public record MasterBundle(
        JsonNode documentTree,
        StyleSheet styleSheet,
        FontFamilyList fonts,
        TextBundle textBundle
) {
    /**
     * Creates a MasterBundle from JSON
     * @param documentJson JSON tree of the document
     * @param stylesJson JSON tree of the styles
     * @param fontsJson JSON tree of the fonts
     * @param textJson JSON tree of the text
     * @param mapper Jackson mapper
     * @return MasterBundle
     * @throws JAccessParseException if a text entry is not found for a placeholder key
     */
    @PublicAPI
    public static MasterBundle fromJson(String documentJson, String stylesJson,
                                    String fontsJson, String textJson,
                                    ObjectMapper mapper) throws JAccessParseException {
        //...
        return null;
    }
}