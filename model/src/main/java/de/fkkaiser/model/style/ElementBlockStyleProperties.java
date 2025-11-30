/*
 * Copyright 2025 Katrin Kaiser
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.fkkaiser.model.style;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.util.BorderUtil;
import de.fkkaiser.model.util.DimensionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a set of style properties that can be applied to an element.
 * This abstract class provides common properties related to spacing, indentation, padding,
 * and borders.
 *
 * @author Katrin Kaiser
 * @version 1.1.0
 */
public class ElementBlockStyleProperties extends ElementStyleProperties {

    private static final Logger log = LoggerFactory.getLogger(ElementBlockStyleProperties.class);

    //for validation
    private static final String PARAM_SPACE_BEFORE = "space-before";
    private static final String PARAM_SPACE_AFTER = "space-after";
    private static final String PARAM_START_INDENT = "start-indent";
    private static final String PARAM_END_INDENT = "end-indent";
    private static final String PARAM_PADDING = "padding";
    private static final String PARAM_PADDING_LEFT = "padding-left";
    private static final String PARAM_PADDING_RIGHT = "padding-right";
    private static final String PARAM_PADDING_TOP = "padding-top";
    private static final String PARAM_PADDING_BOTTOM = "padding-bottom";

    /**
     * Constructor to prevent direct instantiation.
     */
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

    @JsonProperty("break-before")
    private PageBreakVariant breakBefore;
    @JsonProperty("break-after")
    private PageBreakVariant breakAfter;

    @JsonProperty("background-color")
    private String backgroundColor;


    /**
     * Merges the properties from a base style into this style.
     * Typically, properties from the base style are only applied if they are
     * not already set in this style.
     *
     * @param elemBase The base style to inherit from. Can be null.
     */
    @Internal
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
        this.breakBefore = Optional.ofNullable(this.breakBefore).orElse(base.breakBefore);
        this.breakAfter = Optional.ofNullable(this.breakAfter).orElse(base.breakAfter);
    }


    // --- Getter & Setter ---

    /**
     * Gets the space before the element.
     *
     * @return The space before the element.
     */
    @Internal
    public String getSpaceBefore() {
        return spaceBefore;
    }

    /**
     * Sets the space before the element.
     * @param spaceBefore The space before the element.
     */
    @PublicAPI
    public void setSpaceBefore(String spaceBefore) {
        this.spaceBefore = validatedDimension(spaceBefore,PARAM_SPACE_BEFORE);
    }

    /**
     * Gets the space after the element.
     * @return The space after the element.
     */
    @Internal
    public String getSpaceAfter() {
        return spaceAfter;
    }

    /**
     * Sets the space after the element.
     * @param spaceAfter The space after the element.
     */
    public void setSpaceAfter(String spaceAfter) {
        this.spaceAfter = validatedDimension(spaceAfter,PARAM_SPACE_AFTER);
    }

    /**
     * Gets the start indent.
     * @return The start indent.
     */
    public String getStartIndent() {
        return startIndent;
    }

    /**
     * Sets the start indent.
     * @param startIndent The start indent.
     */
    public void setStartIndent(String startIndent) {
        this.startIndent = validatedDimension(startIndent,PARAM_START_INDENT);
    }

    /**
     * Gets the end indent.
     * @return The end indent.
     */
    public String getEndIndent() {
        return endIndent;
    }

    /**
     * Sets the end indent.
     * @param endIndent The end indent.
     */
    public void setEndIndent(String endIndent) {
        this.endIndent = validatedDimension(endIndent,PARAM_END_INDENT);
    }

    /**
     * Gets the padding.
     * @return The padding.
     */
    public String getPadding() {
        return padding;
    }

    /**
     * Sets the padding.
     * @param padding The padding.
     */
    public void setPadding(String padding) {
        this.padding = validatedDimension(padding,PARAM_PADDING);
    }

    /**
     * Gets the left padding.
     * @return The left padding.
     */
    public String getPaddingLeft() {
        return paddingLeft;
    }

    /**
     * Sets the left padding.
     * @param paddingLeft The left padding.
     */
    public void setPaddingLeft(String paddingLeft) {
        this.paddingLeft = validatedDimension(paddingLeft,PARAM_PADDING_LEFT);
    }

    /**
     * Gets the right padding.
     * @return The right padding.
     */
    public String getPaddingRight() {
        return paddingRight;
    }

    /**
     * Sets the right padding.
     * @param paddingRight The right padding.
     */
    public void setPaddingRight(String paddingRight) {
        this.paddingRight = validatedDimension(paddingRight,PARAM_PADDING_RIGHT);
    }

    /**
     * Gets the top padding.
     * @return The top padding.
     */
    public String getPaddingTop() {
        return paddingTop;
    }

    /**
     * Sets the top padding.
     * @param paddingTop The top padding.
     */
    public void setPaddingTop(String paddingTop) {
        this.paddingTop = validatedDimension(paddingTop,PARAM_PADDING_TOP);
    }

    /**
     * Gets the bottom padding.
     * @return The bottom padding.
     */
    public String getPaddingBottom() {
        return paddingBottom;
    }

    /**
     * Sets the bottom padding.
     * @param paddingBottom The bottom padding.
     */
    public void setPaddingBottom(String paddingBottom) {
        this.paddingBottom = validatedDimension(paddingBottom,PARAM_PADDING_BOTTOM);
    }

    /**
     * Gets the border.
     * @return The border.
     */
    public String getBorder() {
        return border;
    }

    /**
     * Sets the border.
     * @param border The border.
     */
    public void setBorder(String border) {
        if(BorderUtil.isValidBorder(border)){
            this.border = border;
        }else{
            log.warn("ElementBlockStyle: border: Invalid border value '{}'. Border will not be set.",border);
            this.border = null;
        }

    }

    /**
     * Gets the left border.
     * @return The left border.
     */
    public String getBorderLeft() {
        return borderLeft;
    }

    /**
     * Sets the left border.
     * @param borderLeft The left border.
     */
    public void setBorderLeft(String borderLeft) {
        if(BorderUtil.isValidBorder(borderLeft)){
            this.borderLeft = borderLeft;
        }else{
            log.warn("ElementBlockStyle: border-left: Invalid border value '{}'. Border will not be set.",borderLeft);
            this.borderLeft = null;
        }
    }

    /**
     * Gets the right border.
     * @return The right border.
     */
    public String getBorderRight() {
        return borderRight;
    }

    /**
     * Sets the right border.
     * @param borderRight The right border.
     */
    public void setBorderRight(String borderRight) {
        this.borderRight = borderRight;
    }

    /**
     * Gets the top border.
     * @return The top border.
     */
    public String getBorderTop() {
        return borderTop;
    }

    /**
     * Sets the top border.
     * @param borderTop The top border.
     */
    public void setBorderTop(String borderTop) {
        if (BorderUtil.isValidBorder(borderTop)){
            this.borderTop = borderTop;
        }else{
            log.warn("ElementBlockStyle: border-top: Invalid border value '{}'. Border will not be set.",borderTop);
            this.borderTop = null;
        }
    }

    /**
     * Gets the bottom border.
     * @return The bottom border.
     */
    public String getBorderBottom() {
        return borderBottom;
    }

    /**
     * Sets the bottom border.
     * @param borderBottom The bottom border.
     */
    public void setBorderBottom(String borderBottom) {
        if(BorderUtil.isValidBorder(borderBottom)){
            this.borderBottom = borderBottom;
        }else {
            log.warn("ElementBlockStyle: border-bottom: Invalid border value '{}'. Border will not be set.",borderBottom);
            this.borderBottom = null;
        }
    }

    /**
     * Gets whether to keep the element together with the next element.
     *
     * @return true if the element should be kept with the next element, false otherwise
     */
    public Boolean getKeepWithNext() {
        return keepWithNext;
    }

    /**
     * Sets whether to keep the element together with the next element.
     *
     * @param keepWithNext true to keep the element with the next element, false otherwise
     */
    public void setKeepWithNext(Boolean keepWithNext) {
        this.keepWithNext = keepWithNext;
    }

    /**
     * Gets the page break variant before the element.
     *
     * @return The page break variant before the element.
     */
    public PageBreakVariant getBreakBefore() {
        return breakBefore;
    }

    /**
     * Sets the page break variant before the element.
     *
     * @param breakBefore The page break variant before the element.
     */
    public void setBreakBefore(PageBreakVariant breakBefore) {
        this.breakBefore = breakBefore;
    }

    /**
     * Gets the page break variant after the element.
     *
     * @return The page break variant after the element.
     */
    public PageBreakVariant getBreakAfter(){
        return this.breakAfter;}

    /**
     * Sets the page break variant after the element.
     *
     * @param breakAfter The page break variant after the element.
     */
    public void setBreakAfter(PageBreakVariant breakAfter){
        this.breakAfter = breakAfter;}

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
        target.setBreakBefore(breakBefore);
        target.setBreakAfter(breakAfter);
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


    // === private methods ===

    private String validatedDimension(String paramValue,String paramName) {
        if (paramValue == null) {
            return null;
        }
        DimensionUtil.ValidationResult result = DimensionUtil.validateAndNormalize(paramValue);
        if (result.hasWarning()) {
            log.warn("ElementBlockStyle: {}: {}", paramName, result.warningMessage());
        }
        return result.normalized();
    }

    @Override
    public List<String> validate() {
        return new ArrayList<>();
    }

}
