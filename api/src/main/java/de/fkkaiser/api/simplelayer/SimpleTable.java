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
package de.fkkaiser.api.simplelayer;

import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static de.fkkaiser.api.simplelayer.SimpleStyleManager.TABLE_STYLE_NAME;


/**
 * Helper class to define a table in the SimpleDocumentBuilder.
 * Encapsulates the complexity of table creation.
 *
 * <p>Example usage:
 * <pre>{@code
 * SimpleTable table = new SimpleTable()
 *     .setColumns("50%", "50%")
 *     .addHeaderRow("Header 1", "Header 2")
 *     .addBodyRow("Cell 1", "Cell 2")
 *     .addBodyRow(
 *         SimpleTableCell.of("complex cell").addParagraph("paragraph..."),
 *         SimpleTableCell.of("Cell 4").withColspan(2)
 *     );
 * builder.addTable(table);
 * }</pre>
 *
 * @author FK Kaiser
 * @version 1.0.0
 */
@PublicAPI
public class SimpleTable {

    private static final Logger log = LoggerFactory.getLogger(SimpleTable.class);

    final String styleClass;
    final List<String> columns;
    final List<SimpleTableRow> headerRows = new ArrayList<>();
    final List<SimpleTableRow> bodyRows = new ArrayList<>();


    /**
     * Creates table with standard style.
     */
    public SimpleTable() {
        this.styleClass = TABLE_STYLE_NAME;
        this.columns = new ArrayList<>();
    }


    /**
     * Sets the column widths for the table.
     * Calling this method multiple times will replace previous column definitions.
     * Supported units: percentages (%), absolute units (cm, mm, in, pt, pc), or fractional units (fr).
     *
     * @param widths column width specifications (e.g., "50%", "10cm", "1fr")
     * @return this table instance for method chaining
     * @throws IllegalArgumentException if widths is empty
     * @throws NullPointerException if widths is null
     */
    public SimpleTable setColumns(String... widths) {
        Objects.requireNonNull(widths, "widths cannot be null");
        if (widths.length == 0) {
            throw new IllegalArgumentException("widths cannot be null or empty");
        }
        this.columns.clear();
        this.columns.addAll(Arrays.asList(widths));
        return this;
    }

    /**
     * Adds a row to the table header with simple text cells.
     * Each text will be automatically styled as a header cell (bold).
     *
     * @param cellTexts text content for each header cell
     * @return this table instance for method chaining
     * @throws IllegalArgumentException if cellTexts is null or empty
     * @throws NullPointerException if cellTexts is null
     */
    public SimpleTable addHeaderRow(String... cellTexts) {
        Objects.requireNonNull(cellTexts, "cellTexts cannot be null");
        if (cellTexts.length == 0) {
            throw new IllegalArgumentException("cellTexts cannot empty");
        }

        if (!this.columns.isEmpty() && cellTexts.length != this.columns.size()) {
            log.warn("Adding header row with {} cells, but table has {} columns defined. " +
                            "This may cause rendering issues.",
                    cellTexts.length, this.columns.size());
        }

        final List<SimpleTableCell> cells = Arrays.stream(cellTexts)
                .map(SimpleTableCell::ofHeader)
                .toList();
        this.headerRows.add(new SimpleTableRow(cells));
        return this;
    }

    /**
     * Adds a row to the table body (with simple text cells).
     * @param cellTexts text-content of the cells
     */
    public SimpleTable addBodyRow(String... cellTexts) {
        if (cellTexts == null || cellTexts.length == 0) {
            throw new IllegalArgumentException("cellTexts cannot be null or empty");
        }
        final List<SimpleTableCell> cells = Arrays.stream(cellTexts)
                .map(SimpleTableCell::of)
                .toList();
        this.bodyRows.add(new SimpleTableRow(cells));
        return this;
    }

    /**
     * Adds a row to the table header (with more complex cells).
     * @param cells list of {@link SimpleTableCell} objects that fills the row
     */
    @PublicAPI
    public SimpleTable addHeaderRow(SimpleTableCell... cells) {
        if (cells == null || cells.length == 0) {
            throw new IllegalArgumentException("cells cannot be null or empty");
        }

        this.headerRows.add(new SimpleTableRow(Arrays.asList(cells)));
        return this;
    }

    /**
     * Adds a row to the table body (with more complex cells).
     * @param cells list of {@link SimpleTableCell} that fills the row
     */
    public SimpleTable addBodyRow(SimpleTableCell... cells) {
        this.bodyRows.add(new SimpleTableRow(Arrays.asList(cells)));
        return this;
    }

    /**
     * Validates that column counts in body rows, header rows, and column settings are consistent.
     * Checks for colspan support and warns about mismatches.
     *
     * @throws IllegalStateException if the validation fails
     */
    @Internal("Used in SimpleDocumentBuilder")
    void validate() {
        // If no columns defined, check for consistency between rows
        if (columns.isEmpty()) {
            validateConsistentRowLengths();
            return;
        }

        // if columns defined, strict validation
        validateAgainstDefinedColumns();
    }

    private void validateAgainstDefinedColumns() {
        final int expectedColumns = columns.size();

        // Validate all rows with colspan support
        for (int i = 0; i < headerRows.size(); i++) {
            validateRow("Header", i, headerRows.get(i).cells(), expectedColumns);
        }

        for (int i = 0; i < bodyRows.size(); i++) {
            validateRow("Body", i, bodyRows.get(i).cells(), expectedColumns);
        }
    }

    private void validateRow(String rowType, int rowIndex, List<SimpleTableCell> cells, int expectedColumns) {
        final int effectiveColumns = cells.stream().mapToInt(cell -> cell.colspan).sum();
        if (effectiveColumns != expectedColumns) {
            throw new IllegalStateException(
                    String.format("%s row %d spans %d columns (including colspan), but table has %d columns defined",
                            rowType, rowIndex, effectiveColumns, expectedColumns)
            );
        }
    }

    private void validateConsistentRowLengths() {
        // Checks if all rows have the same number of columns
        if (!headerRows.isEmpty() && !bodyRows.isEmpty()) {
            final int headerCols = headerRows.getFirst().cells().size();
            final int bodyCols = bodyRows.getFirst().cells().size();
            if (headerCols != bodyCols) {
                log.warn("Header has {} columns but body has {} columns. Consider using setColumns().",
                        headerCols, bodyCols);
            }
        }
    }
}

