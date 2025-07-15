package de.kaiser.model.style;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.function.Consumer;

/**
 * Concrete style properties for a section element.
 * A section can be used to group content and apply specific background,
 * padding, or borders to it.
 */
@JsonTypeName(StyleTargetTypes.SECTION)
public class SectionStyleProperties extends ElementBlockStyleProperties {

    @JsonProperty("padding")
    private String padding; // e.g., "10pt" or "5pt 10pt"

    @JsonProperty("border")
    private String border; // e.g., "1pt solid black"

    @JsonProperty("background-color")
    private String backgroundColor; // e.g., "#F0F0F0"

    // --- Getters and Setters ---
    public String getPadding() { return padding; }
    public void setPadding(String padding) { this.padding = padding; }
    public String getBorder() { return border; }
    public void setBorder(String border) { this.border = border; }
    public String getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(String backgroundColor) { this.backgroundColor = backgroundColor; }

    // --- Overrides ---
    public void mergeWith(ElementBlockStyleProperties base) {
        super.mergeWith(base);

        if (base instanceof SectionStyleProperties baseSection) {
            mergeProperty(this.padding, baseSection.getPadding(), this::setPadding);
            mergeProperty(this.border, baseSection.getBorder(), this::setBorder);
            mergeProperty(this.backgroundColor, baseSection.getBackgroundColor(), this::setBackgroundColor);
        }
    }

    @Override
    public SectionStyleProperties copy() {
        SectionStyleProperties newInstance = new SectionStyleProperties();
        applyPropertiesTo(newInstance);
        return newInstance;
    }


    protected void applyPropertiesTo(SectionStyleProperties newInstance) {
        super.applyPropertiesTo(newInstance);
        newInstance.setPadding(this.padding);
        newInstance.setBorder(this.border);
        newInstance.setBackgroundColor(this.backgroundColor);
    }

    private <T> void mergeProperty(T current, T base, Consumer<T> setter) {
        if (current == null) {
            setter.accept(base);
        }
    }
}
