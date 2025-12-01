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
import de.fkkaiser.model.annotation.Inheritable;
import de.fkkaiser.model.annotation.Internal;

/**
 * Represents the style properties for a table cell (`fo:table-cell`).
 * <p>
 * This class extends `TextBlockStyleProperties` and provides additional
 * properties specific to table cells, such as border, padding, background color,
 * and vertical alignment. It supports inheritable properties for consistent styling.
 * <p>
 * The class also includes methods for copying and applying properties to other
 * `TableCellStyleProperties` instances.
 *
 * @version 1.1.0
 */
@JsonTypeName(StyleTargetTypes.TABLE_CELL)
public class TableCellStyleProperties extends TextBlockStyleProperties {

    @Inheritable
    @JsonProperty("border")
    private String border; // The border style of the table cell (e.g., "1pt solid black").

    @Inheritable
    @JsonProperty("padding")
    private String padding; // The padding inside the table cell (e.g., "5pt").

    @Inheritable
    @JsonProperty("background-color")
    private String backgroundColor; // The background color of the table cell.

    /**
     * Specifies the vertical alignment of content within the table cell.
     * Possible values include "top", "middle", and "bottom".
     */
    @Inheritable
    @JsonProperty("vertical-align")
    private String verticalAlign;

    // --- Getters and Setters ---

    /**
     * Gets the border style of the table cell.
     *
     * @return the border style as a `String`
     */
    public String getBorder() { return border; }

    /**
     * Sets the border style of the table cell.
     *
     * @param border the border style to set
     */
    public void setBorder(String border) { this.border = border; }

    /**
     * Gets the padding inside the table cell.
     *
     * @return the padding as a `String`
     */
    public String getPadding() { return padding; }

    /**
     * Sets the padding inside the table cell.
     *
     * @param padding the padding to set
     */
    public void setPadding(String padding) { this.padding = padding; }

    /**
     * Gets the background color of the table cell.
     *
     * @return the background color as a `String`
     */
    public String getBackgroundColor() { return backgroundColor; }

    /**
     * Sets the background color of the table cell.
     *
     * @param backgroundColor the background color to set
     */
    public void setBackgroundColor(String backgroundColor) { this.backgroundColor = backgroundColor; }

    /**
     * Gets the vertical alignment of content within the table cell.
     *
     * @return the vertical alignment as a `String`
     */
    public String getVerticalAlign() { return verticalAlign; }

    /**
     * Sets the vertical alignment of content within the table cell.
     *
     * @param verticalAlign the vertical alignment to set
     */
    public void setVerticalAlign(String verticalAlign) { this.verticalAlign = verticalAlign; }

    /**
     * Creates a copy of the current `TableCellStyleProperties` object.
     *
     * @return a new `TableCellStyleProperties` instance with the same properties
     */
    @Override
    public TableCellStyleProperties copy() {
        TableCellStyleProperties newInstance = new TableCellStyleProperties();
        applyPropertiesTo(newInstance);
        return newInstance;
    }

    /**
     * Applies the current properties to another `TableCellStyleProperties` object.
     * <p>
     * This method copies all table-cell-specific properties to the target object.
     *
     * @param target the target `TableCellStyleProperties` to apply properties to
     */
    @Internal
    public void applyPropertiesTo(TableCellStyleProperties target){
        super.applyPropertiesTo(target);
        target.setBorder(this.getBorder());
        target.setPadding(this.getPadding());
        target.setBackgroundColor(this.getBackgroundColor());
        target.setVerticalAlign(this.getVerticalAlign());
    }
}