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
import de.fkkaiser.model.JsonPropertyName;
import de.fkkaiser.model.annotation.Inheritable;
import de.fkkaiser.model.annotation.Internal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Abstract base class for inline text element style properties.
 *
 * @author Katrin Kaiser
 * @version 1.0.1
 */
public abstract class InlineTextElementStyleProperties extends InlineElementStyleProperties {

    @Inheritable
    @JsonProperty(JsonPropertyName.TEXT_STYLE_NAME)
    private String textStyleName;

    @Inheritable
    @JsonProperty(JsonPropertyName.TEXT_DECORATION)
    private String textDecoration;


    @Inheritable
    @JsonProperty(JsonPropertyName.TEXT_COLOR)
    private String textColor;

    @Inheritable
    @JsonProperty(JsonPropertyName.LINEFEED_TREATMENT)
    private LinefeedTreatment lineFeedTreatment;


    /**
     * Creates a deep copy of this style properties object.
     * All properties are copied to a new instance.
     *
     * <p><b>Note:</b></p>
     * This implementation returns an {@link InlineElementStyleProperties} instance.
     * Subclasses should override this method to return their specific type.
     *
     * @return a new style properties instance with identical values
     */
    @Internal
    @Override
    public ElementStyleProperties copy() {
        InlineElementStyleProperties copy = new InlineElementStyleProperties();
        applyPropertiesTo(copy);
        return copy;
    }

    /**
     * Applies the properties of this style to the given target style object.
     * This method copies all relevant properties from this instance to the target.
     *
     * @param target the target style properties object to which properties will be applied
     */
    @Internal
    @Override
    protected void applyPropertiesTo(InlineElementStyleProperties target) {
        super.applyPropertiesTo(target);
        if (target instanceof InlineTextElementStyleProperties textTarget) {
            textTarget.textStyleName = this.textStyleName;
            textTarget.textDecoration = this.textDecoration;
            textTarget.textColor = this.textColor;
            textTarget.lineFeedTreatment = this.lineFeedTreatment;
        }
    }

    // --- Getter & Setter ---

    /**
     * Gets the name of the referenced text style.
     *
     * @return the text style name, or {@code null} if not set
     * @see #textStyleName
     */
    public String getTextStyleName() {
        return textStyleName;
    }

    /**
     * Gets the text decoration specification.
     *
     * @return the text decoration (e.g., {@code "underline"}, {@code "line-through"}),
     *         or {@code null} if not set
     * @see #textDecoration
     */
    public String getTextDecoration() {
        return textDecoration;
    }

    /**
     * Gets the text color.
     *
     * @return the text color (e.g., {@code "#FF0000"}, {@code "red"}),
     *         or {@code null} if not set
     * @see #textColor
     */
    public String getTextColor() {
        return textColor;
    }

    /**
     * Sets the name of the text style to apply to this inline element.
     *
     * @param fontStyleName the text style name; may be {@code null} to unset or inherit
     * @see #textStyleName
     */
    public void setTextStyleName(String fontStyleName) {
        this.textStyleName = fontStyleName;
    }

    /**
     * Sets the text decoration for this inline element.
     *
     * @param textDecoration the decoration to apply (e.g., {@code "underline"}, {@code "line-through"});
     *                       may be {@code null} to use default (no decoration)
     * @see #textDecoration
     */
    public void setTextDecoration(String textDecoration) {
        this.textDecoration = textDecoration;
    }

    /**
     * Sets the text color for this inline element.
     *
     * @param textColor the color to apply (e.g., {@code "#FF0000"}, {@code "rgb(255,0,0)"});
     *                  may be {@code null} to inherit from parent block
     * @see #textColor
     */
    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    /**
     * Gets how line feed characters are handled in this inline element.
     *
     * @return the treatment mode (e.g., {@code "preserve"}, {@code "treat-as-space"});
     *         may be {@code null} if default behavior is used
     * @see #lineFeedTreatment
     */
    public LinefeedTreatment getLineFeedTreatment() {
        return lineFeedTreatment;
    }

    /**
     * Sets how line feed characters are handled in this inline element.
     *
     * @param lineFeedTreatment the treatment mode (e.g., {@code "preserve"}, {@code "treat-as-space"});
     *                          may be {@code null} to use default behavior
     * @see #lineFeedTreatment
     */
    public void setLineFeedTreatment(LinefeedTreatment lineFeedTreatment) {
        this.lineFeedTreatment = lineFeedTreatment;
    }
}