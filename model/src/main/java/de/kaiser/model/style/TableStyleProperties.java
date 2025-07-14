package de.kaiser.model.style;

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
