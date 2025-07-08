package de.kaiser.model.style;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Concrete style properties for a paragraph element.
 */
@JsonTypeName(StyleTargetTypes.PARAGRAPH)
public class ParagraphStyleProperties extends TextBlockStyleProperties {

    @JsonProperty("text-align")
    private String textAlign; // e.g., "left", "center", "right", "justify"

    @JsonProperty("text-indent")
    private String textIndent; // e.g., "20pt"

    @JsonProperty("line-height")
    private String lineHeight; // e.g., "1.5"

    // --- Getters and Setters ---

    public String getTextAlign() { return textAlign; }
    public void setTextAlign(String textAlign) { this.textAlign = textAlign; }
    public String getTextIndent() { return textIndent; }
    public void setTextIndent(String textIndent) { this.textIndent = textIndent; }
    public String getLineHeight() { return lineHeight; }
    public void setLineHeight(String lineHeight) { this.lineHeight = lineHeight; }

    // --- Overrides ---

    @Override
    public void mergeWith(ElementStyleProperties base) {
        super.mergeWith(base);
        if (base instanceof ParagraphStyleProperties baseParagraph) {
            if (this.textAlign == null) {
                this.textAlign = baseParagraph.getTextAlign();
            }
            if (this.textIndent == null) {
                this.textIndent = baseParagraph.getTextIndent();
            }
            if (this.lineHeight == null) {
                this.lineHeight = baseParagraph.getLineHeight();
            }
        }
    }

    @Override
    public ParagraphStyleProperties copy() {
        ParagraphStyleProperties newInstance = new ParagraphStyleProperties();
        applyPropertiesTo(newInstance);
        newInstance.setTextAlign(this.textAlign);
        newInstance.setTextIndent(this.textIndent);
        newInstance.setLineHeight(this.lineHeight);
        return newInstance;
    }
}
