package de.kaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.kaiser.model.style.StyleResolverContext;

import java.util.List;
import java.util.Objects;

/**
 * Represents a section of a table (header or body), containing multiple rows.
 */
public record TableSection(List<TableRow> rows) {

    @JsonCreator
    public TableSection(@JsonProperty("rows") List<TableRow> rows) {
        this.rows = Objects.requireNonNullElse(rows, List.of());
    }

    /**
     * Delegates style resolution to all rows in this section.
     *
     * @param context The current style context.
     */
    public void resolveStyles(StyleResolverContext context) {
        rows.forEach(row -> row.resolveStyles(context));
    }
}
