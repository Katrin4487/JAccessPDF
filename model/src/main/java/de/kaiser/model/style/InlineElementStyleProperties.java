package de.kaiser.model.style;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * A class representing style properties for inline elements.
 * Extends ElementStyleProperties.
 * This class provides properties specific to inline elements like background color.
 */
public class InlineElementStyleProperties extends ElementStyleProperties {

    private static final Logger log = LoggerFactory.getLogger(InlineElementStyleProperties.class);

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
        }else{
            log.warn("Attempted to merge with an incompatible style type: {}. Merge will be skipped.",
                    elemBase==null ? "null" :elemBase.getClass().getName());
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
