package de.fkkaiser.model.style;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Concrete style properties for a list element.
 */
@JsonTypeName(StyleTargetTypes.LIST)
public class ListStyleProperties extends TextBlockStyleProperties {

    @JsonProperty("provisional-distance-between-starts")
    private String provDistBetweenStarts;

    @JsonProperty("provisional-label-separation")
    private String provLabelSeparation;


    /**
     * Defines the type of the list item marker.
     * e.g., "disc", "circle", "square" for unordered lists;
     * "decimal", "lower-alpha", "upper-roman" for ordered lists.
     */
    @JsonProperty("list-style-type")
    private String listStyleType;

    /**
     * Defines an image to be used as the list item marker.
     * Expects a URL to the image.
     */
    @JsonProperty("list-style-image")
    private String listStyleImage;

    /**
     * Variable representing the position of the list item marker with
     * respect to the list item's principal block box. (inside / outside)
     */
    @JsonProperty("list-style-position")
    private String listStylePosition;

    // --- Getters and Setters ---
    public String getProvDistBetweenStarts() { return provDistBetweenStarts; }
    public void setProvDistBetweenStarts(String provDistBetweenStarts) { this.provDistBetweenStarts = provDistBetweenStarts; }
    public String getProvLabelSeparation() { return provLabelSeparation; }
    public void setProvLabelSeparation(String provLabelSeparation) { this.provLabelSeparation = provLabelSeparation; }
    public String getListStyleType() { return listStyleType; }
    public void setListStyleType(String listStyleType) { this.listStyleType = listStyleType; }
    public String getListStyleImage() { return listStyleImage; }
    public void setListStyleImage(String listStyleImage) { this.listStyleImage = listStyleImage; }

    public String getListStylePosition() {
        return listStylePosition;
    }

    public void setListStylePosition(String listStylePosition) {
        this.listStylePosition = listStylePosition;
    }
// --- Overrides ---

    @Override
    public void mergeWith(ElementStyleProperties base) {
        super.mergeWith(base);
        if (base instanceof ListStyleProperties baseList) {
            if (this.provDistBetweenStarts == null) {
                this.provDistBetweenStarts = baseList.getProvDistBetweenStarts();
            }
            if (this.provLabelSeparation == null) {
                this.provLabelSeparation = baseList.getProvLabelSeparation();
            }
            if (this.listStyleType == null) {
                this.listStyleType = baseList.getListStyleType();
            }
            if (this.listStyleImage == null) {
                this.listStyleImage = baseList.getListStyleImage();
            }
        }
    }

    @Override
    public ListStyleProperties copy() {
        ListStyleProperties newInstance = new ListStyleProperties();
        applyPropertiesTo(newInstance);
        newInstance.setProvDistBetweenStarts(this.provDistBetweenStarts);
        newInstance.setProvLabelSeparation(this.provLabelSeparation);
        newInstance.setListStyleType(this.listStyleType);
        newInstance.setListStyleImage(this.listStyleImage);
        newInstance.setListStylePosition(this.listStylePosition);
        return newInstance;
    }
}
