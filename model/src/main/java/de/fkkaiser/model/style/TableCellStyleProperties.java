package de.fkkaiser.model.style;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.function.Consumer;

/**
 * Concrete style properties for a table cell (fo:table-cell).
 */
@JsonTypeName(StyleTargetTypes.TABLE_CELL)
public class TableCellStyleProperties extends TextBlockStyleProperties {

    @JsonProperty("border")
    private String border; // e.g., "1pt solid black"

    @JsonProperty("padding")
    private String padding; // e.g., "5pt"

    @JsonProperty("background-color")
    private String backgroundColor;

    /**
     * Controls the vertical alignment of content within the cell.
     * e.g., "top", "middle", "bottom".
     */
    @JsonProperty("vertical-align")
    private String verticalAlign;

    // --- Getters and Setters ---
    public String getBorder() { return border; }
    public void setBorder(String border) { this.border = border; }
    public String getPadding() { return padding; }
    public void setPadding(String padding) { this.padding = padding; }
    public String getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(String backgroundColor) { this.backgroundColor = backgroundColor; }
    public String getVerticalAlign() { return verticalAlign; }
    public void setVerticalAlign(String verticalAlign) { this.verticalAlign = verticalAlign; }

    // --- Overrides ---
    @Override
    public void mergeWith(ElementStyleProperties base) {
        super.mergeWith(base);

        if (base instanceof TableCellStyleProperties baseCell) {
            mergeProperty(this.border, baseCell.getBorder(), this::setBorder);
            mergeProperty(this.padding, baseCell.getPadding(), this::setPadding);
            mergeProperty(this.backgroundColor, baseCell.getBackgroundColor(), this::setBackgroundColor);
            mergeProperty(this.verticalAlign, baseCell.getVerticalAlign(), this::setVerticalAlign);
        }
    }

    private <T> void mergeProperty(T current, T base, Consumer<T> setter) {
        if (current == null) {
            setter.accept(base);
        }
    }

    @Override
    public TableCellStyleProperties copy() {
        TableCellStyleProperties newInstance = new TableCellStyleProperties();
        applyPropertiesTo(newInstance);
        return newInstance;
    }

    public void applyPropertiesTo(TableCellStyleProperties target){
        super.applyPropertiesTo(target);
        target.setBorder(this.getBorder());
        target.setPadding(this.getPadding());
        target.setBackgroundColor(this.getBackgroundColor());
        target.setVerticalAlign(this.getVerticalAlign());
    }
}
