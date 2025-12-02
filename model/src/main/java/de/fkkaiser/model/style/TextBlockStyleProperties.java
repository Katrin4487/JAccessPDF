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
import de.fkkaiser.model.annotation.Inheritable;
import de.fkkaiser.model.util.DimensionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for all block-level text elements.
 * It contains common properties like font, color, and margins.
 * This class provides methods to get and set these properties,
 * as well as to copy them to other instances.
 *<P>
 * Subclasses should implement the `copy` method to create a deep copy of their instances.
 *
 * @version 1.1.0
 */
public class TextBlockStyleProperties extends ElementBlockStyleProperties {

    private static final Logger log = LoggerFactory.getLogger(TextBlockStyleProperties.class);

    @Inheritable
    @JsonProperty("text-style-name")
    private String textStyleName; // The name of the text style.

    @Inheritable
    @JsonProperty("text-color")
    private String textColor; // The color of the text.

    @Inheritable
    @JsonProperty("line-height")
    private String lineHeight; // The height of the lines in the text block.

    @Inheritable
    @JsonProperty("text-align")
    private TextAlign textAlign; // The alignment of the text (e.g., start, center, end, justify).

    @Inheritable
    private Span span; // The span property for the text block.

    @Inheritable
    @JsonProperty("linefeed-treatment")
    private LinefeedTreatment linefeedTreatment; // The treatment of line feeds (e.g., "preserve").

    /**
     * Default constructor to prevent initialization from outside.
     */
    public TextBlockStyleProperties() {
        super();
    }

    /**
     * Gets the name of the text style.
     *
     * @return the text style name
     */
    public String getTextStyleName() {
        return textStyleName;
    }

    /**
     * Sets the name of the text style.
     *
     * @param textStyleName the text style name to set
     */
    public void setTextStyleName(String textStyleName) {
        this.textStyleName = textStyleName;
    }

    /**
     * Gets the color of the text.
     *
     * @return the text color
     */
    public String getTextColor() {
        return textColor;
    }

    /**
     * Sets the color of the text.
     *
     * @param textColor the text color to set
     */
    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    /**
     * Gets the line height of the text block.
     *
     * @return the line height
     */
    public String getLineHeight() {
        return lineHeight;
    }

    /**
     * Sets the line height of the text block.
     *
     * @param lineHeight the line height to set
     */
    public void setLineHeight(String lineHeight) {

        if(DimensionUtil.isValidLineHeight(lineHeight)) {
            this.lineHeight = lineHeight;
        }else{
            log.warn("Invalid line height value: {}. It must be a number, percentage, or 'normal'. Value not set.", lineHeight);
        }
    }

    /**
     * Gets the text alignment.
     *
     * @return the text alignment
     */
    public TextAlign getTextAlign() {
        return textAlign;
    }

    /**
     * Sets the text alignment.
     *
     * @param textAlign the text alignment to set
     */
    public void setTextAlign(TextAlign textAlign) {
        this.textAlign = textAlign;
    }

    /**
     * Gets the span property of the text block.
     *
     * @return the span property
     */
    public Span getSpan() {
        return span;
    }

    /**
     * Sets the span property of the text block.
     *
     * @param span the span property to set
     */
    public void setSpan(Span span) {
        this.span = span;
    }

    /**
     * Gets the linefeed treatment property.
     *
     * @return the linefeed treatment
     */
    public LinefeedTreatment getLinefeedTreatment() {
        return linefeedTreatment;
    }

    /**
     * Sets the linefeed treatment property.
     *
     * @param linefeedTreatment the linefeed treatment to set
     */
    public void setLinefeedTreatment(LinefeedTreatment linefeedTreatment) {
        this.linefeedTreatment = linefeedTreatment;
    }

    /**
     * Helper method to apply all properties from this object to another.
     * Used by the `copy` method in concrete subclasses.
     *
     * @param target the object to apply the properties to
     */
    protected void applyPropertiesTo(ElementBlockStyleProperties target) {
        super.applyPropertiesTo(target);
        if (target instanceof TextBlockStyleProperties textBase) {
            textBase.setTextStyleName(this.textStyleName);
            textBase.setTextColor(this.textColor);
            textBase.setLineHeight(this.lineHeight);
            textBase.setTextAlign(this.textAlign);
            textBase.setSpan(this.span);
            textBase.setLinefeedTreatment(this.linefeedTreatment);
        }
    }

    /**
     * Creates a copy of the current `TextBlockStyleProperties` object.
     * Subclasses should override this method to return a copy of their instances.
     *
     * @return a copy of the current object
     */
    @Override
    public TextBlockStyleProperties copy() {
        TextBlockStyleProperties newInstance = new TextBlockStyleProperties();
        this.applyPropertiesTo(newInstance);
        return newInstance;
    }

    @Override
    public List<String> validate() {
        List<String> errors = new ArrayList<>();

        if (lineHeight != null && !DimensionUtil.isValidLineHeight(lineHeight)) {
            errors.add("Invalid line height value: " + lineHeight + ". It must be a number, percentage, or 'normal'.");
        }

        return errors;
    }
}