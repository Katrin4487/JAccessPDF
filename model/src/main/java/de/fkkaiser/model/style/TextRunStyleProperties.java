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
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Concrete style properties for an inline text run element.
 * It primarily references a font-style and can add text-specific decorations.
 */
@JsonTypeName(StyleTargetTypes.TEXT_RUN)
public class TextRunStyleProperties extends InlineTextElementStyleProperties{

     @JsonProperty("baseline-shift")
    private String baselineShift; //super

    public String getBaselineShift() {
        return baselineShift;
    }
    public void setBaselineShift(String baselineShift) {
        this.baselineShift = baselineShift;
    }

    /**
     * Factory method to create a new, resolved style by combining a parent
     * style with an optional, more specific style.
     *
     * @param parentStyle The style inherited from the parent block (can be null).
     * @param specificStyle The specific style for this text run (can be null).
     * @return A new, fully resolved TextRunStyleProperties object.
     */
    public static TextRunStyleProperties createResolved(ElementBlockStyleProperties parentStyle, TextRunStyleProperties specificStyle) {
        TextRunStyleProperties newResolvedStyle = new TextRunStyleProperties();


        if (specificStyle != null && specificStyle.getTextColor() != null) {
            newResolvedStyle.setTextColor(specificStyle.getTextColor());
        } else if (parentStyle instanceof TextBlockStyleProperties textParent) {
            newResolvedStyle.setTextColor(textParent.getTextColor());
        }

        if (specificStyle != null && specificStyle.getTextStyleName() != null) {
            newResolvedStyle.setTextStyleName(specificStyle.getTextStyleName());
        } else if (parentStyle instanceof TextBlockStyleProperties textParent) {
            newResolvedStyle.setTextStyleName(textParent.getTextStyleName());
        }
        if (specificStyle != null && specificStyle.getLineFeedTreatment() != null) {
            newResolvedStyle.setLineFeedTreatment(specificStyle.getLineFeedTreatment());
        } else if (parentStyle instanceof TextBlockStyleProperties textParent) {
            newResolvedStyle.setLineFeedTreatment(textParent.getLinefeedTreatment());
        }

        if (specificStyle != null) {
            if(specificStyle.getTextDecoration()!=null){
                newResolvedStyle.setTextDecoration(specificStyle.getTextDecoration());
            }
            if(specificStyle.getBaselineShift()!=null){
                newResolvedStyle.setBaselineShift(specificStyle.getBaselineShift());
            }
        }

        return newResolvedStyle;
    }

    @Override
    public void mergeWith(ElementStyleProperties base) {
        super.mergeWith(base);
    }

    @Override
    public TextRunStyleProperties copy() {
        TextRunStyleProperties newInstance = new TextRunStyleProperties();
        applyPropertiesTo(newInstance);
        return newInstance;
    }

    @Override
    protected void applyPropertiesTo(InlineElementStyleProperties target) {
        super.applyPropertiesTo(target);
        if (target instanceof TextRunStyleProperties textRunStyleProperties) {
            textRunStyleProperties.setBaselineShift(this.baselineShift);
        }

    }

}
