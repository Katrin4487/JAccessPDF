package de.fkkaiser.api.simplelayer;

import java.util.List;
import java.util.Objects;

/**
 * Internal representation of a table row.
 * Contains a list of cells that make up the row.
 *
 * @param cells the list of {@link SimpleTableCell} objects in this row
 */
record SimpleTableRow(List<SimpleTableCell> cells) {

    /**
     * Compact constructor with validation.
     *
     * @throws NullPointerException if cells is null
     */
    public SimpleTableRow {
        Objects.requireNonNull(cells, "cells cannot be null");
    }
}