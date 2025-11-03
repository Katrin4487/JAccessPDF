package de.fkkaiser.model.style;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Represents a set of style properties that can be applied to an element.
 * This abstract class provides common properties related to spacing, indentation, padding,
 * and borders.
 */
public class ElementBlockStyleProperties extends ElementStyleProperties {

    private static final Logger log = LoggerFactory.getLogger(ElementBlockStyleProperties.class);

    ElementBlockStyleProperties(){
        // prevent initializing
    }

    @JsonProperty("space-before")
    private String spaceBefore;

    @JsonProperty("space-after")
    private String spaceAfter;

    @JsonProperty("start-indent")
    private String startIndent;

    @JsonProperty("end-indent")
    private String endIndent;

    @JsonProperty("padding")
    private String padding;

    @JsonProperty("padding-left")
    private String paddingLeft;

    @JsonProperty("padding-right")
    private String paddingRight;

    @JsonProperty("padding-top")
    private String paddingTop;

    @JsonProperty("padding-bottom")
    private String paddingBottom;

    @JsonProperty("border")
    private String border;

    @JsonProperty("border-left")
    private String borderLeft;

    @JsonProperty("border-right")
    private String borderRight;

    @JsonProperty("border-top")
    private String borderTop;

    @JsonProperty("border-bottom")
    private String borderBottom;

    @JsonProperty("keep-with-next")
    private Boolean keepWithNext; //keep-with-next.within-page,within-column,always="always"

    @JsonProperty("page-break-before")
    private boolean pageBreakBefore; //break-before="page"

    @JsonProperty("background-color")
    private String backgroundColor;


    /**
     * Merges the properties from a base style into this style.
     * Typically, properties from the base style are only applied if they are
     * not already set in this style.
     *
     * @param elemBase The base style to inherit from. Can be null.
     */
     @Override
    public void mergeWith(ElementStyleProperties elemBase){
        if(!(elemBase instanceof ElementBlockStyleProperties base)) {
            log.info("Attempted to merge with an incompatible style type: {}. Merge will be skipped.",
                    elemBase==null ? "null" :elemBase.getClass().getName());
            return;
        }
        this.spaceAfter = Optional.ofNullable(this.spaceAfter).orElse(base.spaceAfter);
        this.spaceBefore = Optional.ofNullable(this.spaceBefore).orElse(base.spaceBefore);
        this.startIndent = Optional.ofNullable(this.startIndent).orElse(base.startIndent);
        this.endIndent = Optional.ofNullable(this.endIndent).orElse(base.endIndent);
        this.padding = Optional.ofNullable(this.padding).orElse(base.getPadding());
        this.paddingLeft = Optional.ofNullable(this.paddingLeft).orElse(base.getPaddingLeft());
        this.paddingRight = Optional.ofNullable(this.paddingRight).orElse(base.getPaddingRight());
        this.paddingTop = Optional.ofNullable(this.paddingTop).orElse(base.getPaddingTop());
        this.paddingBottom = Optional.ofNullable(this.paddingBottom).orElse(base.getPaddingBottom());
        this.border = Optional.ofNullable(this.border).orElse(base.getBorder());
        this.borderLeft = Optional.ofNullable(this.borderLeft).orElse(base.getBorderLeft());
        this.borderRight = Optional.ofNullable(this.borderRight).orElse(base.getBorderRight());
        this.borderTop = Optional.ofNullable(this.borderTop).orElse(base.getBorderTop());
        this.borderBottom = Optional.ofNullable(this.borderBottom).orElse(base.getBorderBottom());
        this.keepWithNext = Optional.ofNullable(this.keepWithNext).orElse(base.keepWithNext);
        this.backgroundColor = Optional.ofNullable(this.backgroundColor).orElse(base.backgroundColor);
    }


    // --- Getter & Setter ---

    public String getSpaceBefore() {
        return spaceBefore;
    }

    public void setSpaceBefore(String spaceBefore) {
        this.spaceBefore = spaceBefore;
    }

    public String getSpaceAfter() {
        return spaceAfter;
    }

    public void setSpaceAfter(String spaceAfter) {
        this.spaceAfter = spaceAfter;
    }

    public String getStartIndent() {
        return startIndent;
    }

    public void setStartIndent(String startIndent) {
        this.startIndent = startIndent;
    }

    public String getEndIndent() {
        return endIndent;
    }

    public void setEndIndent(String endIndent) {
        this.endIndent = endIndent;
    }

    public String getPadding() {
        return padding;
    }

    public void setPadding(String padding) {
        this.padding = padding;
    }

    public String getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(String paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public String getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(String paddingRight) {
        this.paddingRight = paddingRight;
    }

    public String getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(String paddingTop) {
        this.paddingTop = paddingTop;
    }

    public String getPaddingBottom() {
        return paddingBottom;
    }

    public void setPaddingBottom(String paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public String getBorder() {
        return border;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public String getBorderLeft() {
        return borderLeft;
    }

    public void setBorderLeft(String borderLeft) {
        this.borderLeft = borderLeft;
    }

    public String getBorderRight() {
        return borderRight;
    }

    public void setBorderRight(String borderRight) {
        this.borderRight = borderRight;
    }

    public String getBorderTop() {
        return borderTop;
    }

    public void setBorderTop(String borderTop) {
        this.borderTop = borderTop;
    }

    public String getBorderBottom() {
        return borderBottom;
    }

    public void setBorderBottom(String borderBottom) {
        this.borderBottom = borderBottom;
    }

    public Boolean getKeepWithNext() {
        return keepWithNext;
    }

    public void setKeepWithNext(Boolean keepWithNext) {
        this.keepWithNext = keepWithNext;
    }

    @SuppressWarnings("unused")
    public boolean isPageBreakBefore() {
        return pageBreakBefore;
    }

    public void setPageBreakBefore(boolean pageBreakBefore) {
        this.pageBreakBefore = pageBreakBefore;
    }

    public String getBackgroundColor() {
         return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
         this.backgroundColor = backgroundColor;
    }
    //  --- Other methods ---
    /**
     * Helper method to apply all properties from this object to another.
     * Used by the copy() method in concrete subclasses.
     *
     * @param target The object to apply the properties to.
     */
    protected void applyPropertiesTo(ElementBlockStyleProperties target) {
        target.setSpaceBefore(spaceBefore);
        target.setSpaceAfter(spaceAfter);
        target.setStartIndent(startIndent);
        target.setEndIndent(endIndent);
        target.setPadding(padding);
        target.setPaddingLeft(paddingLeft);
        target.setPaddingRight(paddingRight);
        target.setPaddingTop(paddingTop);
        target.setPaddingBottom(paddingBottom);
        target.setBorder(border);
        target.setBorderLeft(borderLeft);
        target.setBorderRight(borderRight);
        target.setBorderBottom(borderBottom);
        target.setBorderTop(borderTop);
        target.setKeepWithNext(keepWithNext);
        target.setPageBreakBefore(pageBreakBefore);
        target.setBackgroundColor(backgroundColor);

    }

    /**
     * Creates a deep copy of this style properties object.
     * This is crucial to prevent style modifications on one element
     * from affecting another.
     *
     * @return A new instance with the same property values as this object.
     */
    public ElementBlockStyleProperties copy(){
        ElementBlockStyleProperties copy = new ElementBlockStyleProperties() {
            @Override
            public ElementBlockStyleProperties copy() {
                ElementBlockStyleProperties copy = new ElementBlockStyleProperties();
                copy.mergeWith(this);
                return copy;
            }
        };
        copy.mergeWith(this);
        return copy;
    }

}
