package de.kaiser.model.style;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

public abstract class InlineTextElementStyleProperties extends InlineElementStyleProperties{

    @JsonProperty("font-style-name")
    private String fontStyleName;

    @JsonProperty("text-decoration")
    private String textDecoration;

    @JsonProperty("text-color")
    private String textColor;


    @Override
    public void mergeWith(ElementStyleProperties elemBase) {

        if(elemBase instanceof TextBlockStyleProperties textBase){
            this.fontStyleName = Optional.ofNullable(this.fontStyleName).orElse(textBase.getFontStyleName());
            this.textColor = Optional.ofNullable(this.textColor).orElse(textBase.getTextColor());
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
            textTarget.fontStyleName = this.fontStyleName;
            textTarget.textDecoration = this.textDecoration;
            textTarget.textColor = this.textColor;
        }
    }
}
