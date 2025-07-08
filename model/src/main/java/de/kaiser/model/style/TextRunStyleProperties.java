package de.kaiser.model.style;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Concrete style properties for an inline text run element.
 * It primarily references a font-style and can add text-specific decorations.
 */
@JsonTypeName(StyleTargetTypes.TEXT_RUN)
public class TextRunStyleProperties extends TextBlockStyleProperties {

    @JsonProperty("text-decoration")
    private String textDecoration; // e.g., "underline" or "line-through"

    // --- Getters and Setters ---

    public String getTextDecoration() { return textDecoration; }
    public void setTextDecoration(String textDecoration) { this.textDecoration = textDecoration; }

    /**
     * Merges properties from a base style into this one.
     * @param base The base style to inherit from.
     */
    @Override
    public void mergeWith(ElementStyleProperties base) {
        // First, let the parent class merge all common properties.
        super.mergeWith(base);

        // Then, merge the properties specific to this class.
        if (base instanceof TextRunStyleProperties baseRun) {
            if (this.textDecoration == null) {
                this.textDecoration = baseRun.getTextDecoration();
            }
        }
    }

    /**
     * Creates a deep copy of this style properties object.
     * @return A new instance of ETextRunStyleProperties with the same values.
     */
    @Override
    public TextRunStyleProperties copy() {
        TextRunStyleProperties newInstance = new TextRunStyleProperties();

        applyPropertiesTo(newInstance);

        newInstance.setTextDecoration(this.textDecoration);
        return newInstance;
    }
}
