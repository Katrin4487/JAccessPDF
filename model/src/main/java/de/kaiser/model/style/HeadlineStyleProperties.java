package de.kaiser.model.style;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Concrete style properties for a headline element.
 */
@JsonTypeName(StyleTargetTypes.HEADLINE)
public class HeadlineStyleProperties extends TextBlockStyleProperties {

    /**
     * Prevents a page break directly after the headline.
     * In XSL-FO, this translates to 'keep-with-next.within-page="always"'.
     */
    @JsonProperty("keep-with-next")
    private Boolean keepWithNext;

    // --- Getters and Setters ---

    public Boolean getKeepWithNext() {
        return keepWithNext;
    }

    public void setKeepWithNext(Boolean keepWithNext) {
        this.keepWithNext = keepWithNext;
    }

    // --- Overrides ---

    @Override
    public void mergeWith(ElementStyleProperties base) {

        super.mergeWith(base);

        if (base instanceof HeadlineStyleProperties baseHeadline) {
            if (this.keepWithNext == null) {
                this.keepWithNext = baseHeadline.getKeepWithNext();
            }
        }
    }

    @Override
    public HeadlineStyleProperties copy() {
        HeadlineStyleProperties newInstance = new HeadlineStyleProperties();
        applyPropertiesTo(newInstance);
        newInstance.setKeepWithNext(this.keepWithNext);
        return newInstance;
    }
}
