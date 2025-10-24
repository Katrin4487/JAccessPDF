package de.fkkaiser.model.style;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

/**
 * An abstract class representing style properties for inline text elements.
 * Extends InlineElementStyleProperties.
 * This class provides properties specific to inline text elements like font style, text decoration, and text color.
 */
public abstract class InlineTextElementStyleProperties extends InlineElementStyleProperties{

    @JsonProperty("text-style-name")
    private String textStyleName;

    @JsonProperty("text-decoration")
    private String textDecoration;

    @JsonProperty("text-color")
    private String textColor;

    @JsonProperty("linefeed-treatment")
    private String lineFeedTreatment;


    @Override
    public void mergeWith(ElementStyleProperties elemBase) {

        if(elemBase instanceof TextBlockStyleProperties textBase){
            System.out.println("Instance of TextBlockStyleProperties "+textStyleName);
            this.textStyleName = Optional.ofNullable(this.textStyleName).orElse(textBase.getTextStyleName());
            this.textColor = Optional.ofNullable(this.textColor).orElse(textBase.getTextColor());
            this.lineFeedTreatment = Optional.ofNullable(this.lineFeedTreatment).orElse(textBase.getLinefeedTreatment());
        }
    }


    @Override
    public ElementStyleProperties copy() {
        InlineElementStyleProperties copy = new InlineElementStyleProperties();
        applyPropertiesTo(copy);
        return copy;
    }

    @Override
    protected void applyPropertiesTo(InlineElementStyleProperties target) {
        super.applyPropertiesTo(target);
        if (target instanceof InlineTextElementStyleProperties textTarget) {
            textTarget.textStyleName = this.textStyleName;
            textTarget.textDecoration = this.textDecoration;
            textTarget.textColor = this.textColor;
            textTarget.lineFeedTreatment = this.lineFeedTreatment;
        }
    }

    // --- Getter & Setter ---


    public String getTextStyleName() {
        return textStyleName;
    }


    public String getTextDecoration() {
        return textDecoration;
    }


    public String getTextColor() {
        return textColor;
    }

    public void setTextStyleName(String fontStyleName) {
        this.textStyleName = fontStyleName;
    }

    public void setTextDecoration(String textDecoration) {
        this.textDecoration = textDecoration;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getLineFeedTreatment() {
        return lineFeedTreatment;
    }

    public void setLineFeedTreatment(String lineFeedTreatment) {
        this.lineFeedTreatment = lineFeedTreatment;
    }
}
