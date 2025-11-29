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

import java.util.Optional;

/**
 * Abstract base class for all block-level text elements.
 * It contains common properties like font, color, and margins.
 */
public class TextBlockStyleProperties extends ElementBlockStyleProperties {

    @JsonProperty("text-style-name")
    private String textStyleName;

    @JsonProperty("text-color")
    private String textColor; //color in FOP!

    @JsonProperty("line-height")
    private String lineHeight;

    @JsonProperty("text-align")
    private String textAlign; //start, center, end, justify

    private String span; //all

    @JsonProperty("linefeed-treatment")
    private String linefeedTreatment; //"preserve"

    public TextBlockStyleProperties() {
        super();
        //prevent init from outside
    }

    public String getTextStyleName() {
        return textStyleName;
    }

    public void setTextStyleName(String textStyleName) {
        this.textStyleName = textStyleName;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public String getLineHeight() {
        return lineHeight;
    }

    public void setLineHeight(String lineHeight) {
        this.lineHeight = lineHeight;
    }

    public String getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(String textAlign) {
        this.textAlign = textAlign;
    }

    public String getSpan() {
        return span;
    }

    public void setSpan(String span) {
        this.span = span;
    }

    public String getLinefeedTreatment() {
        return linefeedTreatment;
    }

    public void setLinefeedTreatment(String linefeedTreatment) {
        this.linefeedTreatment = linefeedTreatment;
    }

    /**
     * Merges properties from a specific style into this one.
     * All properties are set from the specific style if they are available.
     *If they are not available this style is used
     *
     * @param specific The specific style.
     */
    @Override
    public void mergeWith(ElementStyleProperties specific) {

        super.mergeWith(specific);
        if (specific instanceof TextBlockStyleProperties textSpec){
            this.textStyleName = Optional.ofNullable(textSpec.getTextStyleName()).orElse(this.getTextStyleName());
            this.textColor = Optional.ofNullable(textSpec.getTextColor()).orElse(this.getTextColor());
            this.lineHeight = Optional.ofNullable(textSpec.getLineHeight()).orElse(this.getLineHeight());
            this.textAlign = Optional.ofNullable(textSpec.getTextAlign()).orElse(this.getTextAlign());
            this.span = Optional.ofNullable(textSpec.getSpan()).orElse(this.getSpan());
            this.linefeedTreatment = Optional.ofNullable(textSpec.getLinefeedTreatment()).orElse(this.getLinefeedTreatment());

        }
    }

    /**
     * Helper method to apply all properties from this object to another.
     * Used by the copy() method in concrete subclasses.
     *
     * @param target The object to apply the properties to.
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
     * Should be implemented from the subclasses
     *
     * @return a copy of the subclass object
     */
    @Override
    public TextBlockStyleProperties copy() {
        TextBlockStyleProperties newInstance = new TextBlockStyleProperties();
        this.applyPropertiesTo(newInstance);
        return newInstance;
    }


}
