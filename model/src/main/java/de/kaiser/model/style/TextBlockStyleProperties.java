package de.kaiser.model.style;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

/**
 * Abstract base class for all block-level text elements.
 * It contains common properties like font, color, and margins.
 */
public class TextBlockStyleProperties extends ElementBlockStyleProperties {

    @JsonProperty("text-style-name")
    private String textStyleName;

    @JsonProperty("text-color")
    private String textColor; //color in FOP!

    @JsonProperty("line-height")
    private String lineHeight;

    @JsonProperty("text-align")
    private String textAlign; //start, center, end, justify

    public TextBlockStyleProperties() {
        super();
        //prevent init from outside
    }

    public String getTextStyleName() {
        return textStyleName;
    }

    public void setTextStyleName(String textStyleName) {
        this.textStyleName = textStyleName;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getLineHeight() {
        return lineHeight;
    }

    public void setLineHeight(String lineHeight) {
        this.lineHeight = lineHeight;
    }

    public String getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(String textAlign) {
        this.textAlign = textAlign;
    }

    /**
     * Merges properties from a base style into this one.
     * Only properties that are null in this object will be set from the base.
     *
     * @param base The base style to inherit from.
     */
    @Override
    public void mergeWith(ElementStyleProperties base) {
        super.mergeWith(base);
        if (!(base instanceof TextBlockStyleProperties textBase)) {
            return;
        }

        this.textStyleName = Optional.ofNullable(this.textStyleName).orElse(textBase.getTextStyleName());
        this.textColor = Optional.ofNullable(this.textColor).orElse(textBase.getTextColor());
        this.lineHeight = Optional.ofNullable(this.lineHeight).orElse(textBase.getLineHeight());
        this.textAlign = Optional.ofNullable(this.textAlign).orElse(textBase.getTextAlign());

    }

    /**
     * Helper method to apply all properties from this object to another.
     * Used by the copy() method in concrete subclasses.
     *
     * @param target The object to apply the properties to.
     */
    protected void applyPropertiesTo(ElementBlockStyleProperties target) {
        super.applyPropertiesTo(target);
        if (target instanceof TextBlockStyleProperties textBase) {
            textBase.setTextStyleName(this.textStyleName);
            textBase.setTextColor(this.textColor);
            textBase.setLineHeight(this.lineHeight);
            textBase.setTextAlign(this.textAlign);

        }
    }

    /**
     * Should be implemented from the subclasses
     *
     * @return a copy of the subclass object
     */
    @Override
    public TextBlockStyleProperties copy() {
        TextBlockStyleProperties newInstance = new TextBlockStyleProperties();
        this.applyPropertiesTo(newInstance);
        return newInstance;
    }


}
