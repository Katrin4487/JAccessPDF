package de.kaiser.model.style;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Concrete style properties for an inline text run element.
 * It primarily references a font-style and can add text-specific decorations.
 */
@JsonTypeName(StyleTargetTypes.TEXT_RUN)
public class TextRunStyleProperties extends InlineTextElementStyleProperties{

    @JsonProperty("baseline-shift")
    private String baselineShift; //super

    // --- Getters and Setters ---


    public String getBaselineShift() {
        return baselineShift;
    }
    public void setBaselineShift(String baselineShift) {
        this.baselineShift = baselineShift;
    }

    @Override
    public void mergeWith(ElementStyleProperties base) {
        super.mergeWith(base);
    }

    @Override
    public TextRunStyleProperties copy() {
        TextRunStyleProperties newInstance = new TextRunStyleProperties();
        applyPropertiesTo(newInstance);
        return newInstance;
    }

    @Override
    protected void applyPropertiesTo(InlineElementStyleProperties target) {
        super.applyPropertiesTo(target);
        if (target instanceof TextRunStyleProperties textRunStyleProperties) {
            textRunStyleProperties.setBaselineShift(this.baselineShift);
        }

    }
}
