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

    @JsonProperty("baseline-shift")
    private String baselineShift; //super

    // --- Getters and Setters ---

    public String getTextDecoration() { return textDecoration; }
    public void setTextDecoration(String textDecoration) { this.textDecoration = textDecoration; }

    public String getBaselineShift() {
        return baselineShift;
    }

    public void setBaselineShift(String baselineShift) {
        this.baselineShift = baselineShift;
    }

    @Override
    public void mergeWith(ElementStyleProperties base) {

        super.mergeWith(base);

        if (base instanceof TextRunStyleProperties baseRun) {
            if (this.textDecoration == null) {
                this.textDecoration = baseRun.getTextDecoration();
                this.baselineShift = baseRun.getBaselineShift();
            }
        }
    }

    @Override
    public TextRunStyleProperties copy() {
        TextRunStyleProperties newInstance = new TextRunStyleProperties();
        applyPropertiesTo(newInstance);
        newInstance.setTextDecoration(this.textDecoration);
        newInstance.setBaselineShift(this.baselineShift);
        return newInstance;
    }
}
