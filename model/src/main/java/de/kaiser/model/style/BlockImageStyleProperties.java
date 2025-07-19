package de.kaiser.model.style;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.function.Consumer;

@JsonTypeName(StyleTargetTypes.BLOCK_IMAGE)
public class BlockImageStyleProperties extends ElementBlockStyleProperties {

    @JsonProperty("content-width")
    private String contentWidth;

    @JsonProperty("scaling")
    private String scaling;

    @JsonProperty("alignment")
    private String alignment; //center / left -> text-alignment

    // --- Getters and Setters ---


    public String getContentWidth() {
        return contentWidth;
    }

    public String getScaling() {
        return scaling;
    }

    public void setContentWidth(String contentWidth) {
        this.contentWidth = contentWidth;
    }

    public void setScaling(String scaling) {
        this.scaling = scaling;
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    // --- Overrides ---
    public void mergeWith(ElementBlockStyleProperties base) {
        super.mergeWith(base);
        if (base instanceof BlockImageStyleProperties basePart) {
            mergeProperty(this.contentWidth, basePart.contentWidth, this::setContentWidth);
            mergeProperty(this.scaling, basePart.scaling, this::setScaling);
            mergeProperty(this.alignment, basePart.alignment, this::setAlignment);
        }
    }

    @Override
    public BlockImageStyleProperties copy() {
        BlockImageStyleProperties copy = new BlockImageStyleProperties();
        applyPropertiesTo(copy);
        return copy;
    }

    @Override
    public void applyPropertiesTo(ElementBlockStyleProperties target) {
        super.applyPropertiesTo(target);
        if (target instanceof BlockImageStyleProperties partTarget) {
            partTarget.setContentWidth(contentWidth);
            partTarget.setScaling(scaling);
            partTarget.setAlignment(alignment);
        }
    }


    private <T> void mergeProperty(T current, T base, Consumer<T> setter) {
        if (current == null) {
            setter.accept(base);
        }
    }
}