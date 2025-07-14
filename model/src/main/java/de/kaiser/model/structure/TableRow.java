package de.kaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.kaiser.model.style.StyleResolverContext;

import java.util.List;
import java.util.Objects;

/**
 * Represents a row in a table, containing multiple cells.
 */
public record TableRow(List<TableCell> cells) {
    @JsonCreator
    public TableRow(@JsonProperty("cells") List<TableCell> cells) {
        this.cells = Objects.requireNonNullElse(cells, List.of());
    }

    /**
     * Delegates style resolution to all cells in this row.
     *
     * @param context The current style context.
     */
    public void resolveStyles(StyleResolverContext context) {
        cells.forEach(cell -> cell.resolveStyles(context));
    }
}
