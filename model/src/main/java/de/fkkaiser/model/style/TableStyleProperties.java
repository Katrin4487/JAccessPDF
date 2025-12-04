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
import de.fkkaiser.model.JsonPropertyName;

/**
 * Represents the style properties for a table element (`fo:table`).
 * <p>
 * This class extends `TextBlockStyleProperties` and provides additional
 * properties specific to tables, such as border collapse and width.
 * It supports copying of properties to create new instances with the same styles.
 *
 * @author Katrin Kaiser
 * @version 1.1.1
 */
@JsonTypeName(JsonPropertyName.TABLE)
public class TableStyleProperties extends TextBlockStyleProperties {

    /**
     * Specifies the border model for the table.
     * Common values include "collapse".
     */
    @JsonProperty(JsonPropertyName.BORDER_COLLAPSE)
    private String borderCollapse;

    /**
     * Specifies the total width of the table.
     * Examples of valid values include "100%" or "15cm".
     */
    @JsonProperty("width")
    private String width;

    // --- Getters and Setters ---

    /**
     * Gets the border collapse property of the table.
     *
     * @return the border collapse property as a `String`
     */
    public String getBorderCollapse() { return borderCollapse; }

    /**
     * Sets the border collapse property of the table.
     *
     * @param borderCollapse the border collapse property to set
     */
    public void setBorderCollapse(String borderCollapse) { this.borderCollapse = borderCollapse; }

    /**
     * Gets the width of the table.
     *
     * @return the width of the table as a `String`
     */
    public String getWidth() { return width; }

    /**
     * Sets the width of the table.
     *
     * @param width the width of the table to set
     */
    public void setWidth(String width) { this.width = width; }

    /**
     * Creates a copy of the current `TableStyleProperties` object.
     * <p>
     * This method copies all properties of the current object to a new instance.
     *
     * @return a new `TableStyleProperties` instance with the same properties
     */
    @Override
    public TableStyleProperties copy() {
        TableStyleProperties newInstance = new TableStyleProperties();
        applyPropertiesTo(newInstance);
        newInstance.setBorderCollapse(this.borderCollapse);
        newInstance.setWidth(this.width);
        return newInstance;
    }
}