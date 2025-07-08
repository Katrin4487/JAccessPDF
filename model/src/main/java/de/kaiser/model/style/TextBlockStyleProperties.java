package de.kaiser.model.style;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Abstract base class for all block-level text elements.
 * It contains common properties like font, color, and margins.
 */
public abstract class TextBlockStyleProperties extends ElementStyleProperties {

    @JsonProperty("font-style-name")
    private String fontStyleName;

    @JsonProperty("text-color")
    private String textColor;

    @JsonProperty("margin-bottom")
    private String marginBottom;

    // --- Getters and Setters ---
    public String getFontStyleName() { return fontStyleName; }
    public void setFontStyleName(String fontStyleName) { this.fontStyleName = fontStyleName; }
    public String getTextColor() { return textColor; }
    public void setTextColor(String textColor) { this.textColor = textColor; }
    public String getMarginBottom() { return marginBottom; }
    public void setMarginBottom(String marginBottom) { this.marginBottom = marginBottom; }

    /**
     * Merges properties from a base style into this one.
     * Only properties that are null in this object will be set from the base.
     * @param base The base style to inherit from.
     */
    @Override
    public void mergeWith(ElementStyleProperties base) {
        if (!(base instanceof TextBlockStyleProperties textBase)) return;
        if (this.fontStyleName == null) {
            this.fontStyleName = textBase.getFontStyleName();
        }
        if (this.textColor == null) {
            this.textColor = textBase.getTextColor();
        }
        if (this.marginBottom == null) {
            this.marginBottom = textBase.getMarginBottom();
        }
    }

    /**
     * Helper method to apply all properties from this object to another.
     * Used by the copy() method in concrete subclasses.
     * @param target The object to apply the properties to.
     */
    protected void applyPropertiesTo(TextBlockStyleProperties target) {
        target.setFontStyleName(this.fontStyleName);
        target.setTextColor(this.textColor);
        target.setMarginBottom(this.marginBottom);
    }

    /**
     * Should be implemented from the subclasses
     * @return a copy of the subclass object
     */
    @Override
    public abstract TextBlockStyleProperties copy();
}
