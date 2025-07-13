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

    public String getBaselineShift() {
        return baselineShift;
    }
    public void setBaselineShift(String baselineShift) {
        this.baselineShift = baselineShift;
    }

    /**
     * Factory method to create a new, resolved style by combining a parent
     * style with an optional, more specific style.
     *
     * @param parentStyle The style inherited from the parent block (can be null).
     * @param specificStyle The specific style for this text run (can be null).
     * @return A new, fully resolved TextRunStyleProperties object.
     */
    public static TextRunStyleProperties createResolved(TextBlockStyleProperties parentStyle, TextRunStyleProperties specificStyle) {
        TextRunStyleProperties newResolvedStyle = new TextRunStyleProperties();

        if (specificStyle != null && specificStyle.getTextColor() != null) {
            newResolvedStyle.setTextColor(specificStyle.getTextColor());
        } else if (parentStyle != null && parentStyle.getTextColor() != null) {
            newResolvedStyle.setTextColor(parentStyle.getTextColor());
        }

        if (specificStyle != null && specificStyle.getTextStyleName() != null) {
            newResolvedStyle.setTextStyleName(specificStyle.getTextStyleName());
        } else if (parentStyle != null && parentStyle.getTextStyleName() != null) {
            newResolvedStyle.setTextStyleName(parentStyle.getTextStyleName());
        }

        if (specificStyle != null) {
            if(specificStyle.getTextDecoration()!=null){
                newResolvedStyle.setTextDecoration(specificStyle.getTextDecoration());
            }
            if(specificStyle.getBaselineShift()!=null){
                newResolvedStyle.setBaselineShift(specificStyle.getBaselineShift());
            }
        }

        return newResolvedStyle;
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
