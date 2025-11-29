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
 * Concrete style properties for a table element (fo:table).
 */
@JsonTypeName(StyleTargetTypes.TABLE)
public class TableStyleProperties extends TextBlockStyleProperties {

    /**
     * Defines the table's border model.
     * Typically, "collapse".
     */
    @JsonProperty("border-collapse")
    private String borderCollapse;

    /**
     * Defines the total width of the table, e.g., "100%" or "15cm".
     */
    @JsonProperty("width")
    private String width;

    // --- Getters and Setters ---
    public String getBorderCollapse() { return borderCollapse; }
    public void setBorderCollapse(String borderCollapse) { this.borderCollapse = borderCollapse; }
    public String getWidth() { return width; }
    public void setWidth(String width) { this.width = width; }

    // --- Overrides ---

    public void mergeWith(TextBlockStyleProperties base) {
         super.mergeWith(base);
         if (base instanceof TableStyleProperties baseTable) {
            if (this.borderCollapse == null) {
                this.borderCollapse = baseTable.getBorderCollapse();
            }
            if (this.width == null) {
                this.width = baseTable.getWidth();
            }
        }
    }

    @Override
    public TableStyleProperties copy() {
        TableStyleProperties newInstance = new TableStyleProperties();
         applyPropertiesTo(newInstance);
        newInstance.setBorderCollapse(this.borderCollapse);
        newInstance.setWidth(this.width);
        return newInstance;
    }
}
