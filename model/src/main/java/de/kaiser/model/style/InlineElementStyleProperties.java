package de.kaiser.model.style;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

public class InlineElementStyleProperties extends ElementStyleProperties {

    @JsonProperty("background-color")
    private String backgroundColor;

    // --- Constructor ---

    InlineElementStyleProperties(){
        //preventing initialization from outside
    }


    // --- Getter and Setter ---
    public String getBackgroundColor() {
        return backgroundColor;
    }
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }


    @Override
    public void mergeWith(ElementStyleProperties elemBase) {

        if(elemBase instanceof ElementBlockStyleProperties blockBase){
            this.backgroundColor = Optional.ofNullable(this.backgroundColor).orElse(blockBase.getBackgroundColor());
        }
    }


    @Override
    public ElementStyleProperties copy() {
        InlineElementStyleProperties copy = new InlineElementStyleProperties();
        applyPropertiesTo(copy);
        return copy;
    }

    protected void applyPropertiesTo(InlineElementStyleProperties target) {
        target.setBackgroundColor(backgroundColor);
    }
}
