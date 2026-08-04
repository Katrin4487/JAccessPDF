package de.fkkaiser.model.style;
import de.fkkaiser.model.JsonPropertyName;
import de.fkkaiser.model.annotation.PublicAPI;

/**
 * Enum for all available style list keys in the JSON representation of the document model.
 * This enum is used to map the JSON keys to their corresponding style list types.
 */
public enum StyleListKey {

    /**
     * Key for text styles in the JSON representation.
     */
    TEXT_STYLES(JsonPropertyName.TEXT_STYLES),
    /**
     * Key for element styles in the JSON representation
     */
    ELEMENT_STYLES(JsonPropertyName.ELEMENT_STYLES),
    /**
     * Key for page master styles in the JSON representation.
     */
    PAGE_MASTER_STYLES(JsonPropertyName.PAGE_MASTER_STYLES);

    private final String jsonKey;

    StyleListKey(String jsonKey) {
        this.jsonKey = jsonKey;
    }

    /**
     * Returns the JSON key associated with this style list key.
     * @return JSON key
     */
    @PublicAPI
    public String getJsonKey() {
        return jsonKey;
    }
}
