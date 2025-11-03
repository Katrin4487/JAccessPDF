package de.fkkaiser.api.simplelayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
 */
public class SimpleTable {

    final String styleClass;
    final List<String> columns;
    final List<SimpleTableRow> headerRows = new ArrayList<>();
    final List<SimpleTableRow> bodyRows = new ArrayList<>();


    /**
     * Creates table with standard style.
     */
    @SuppressWarnings("unused")
    public SimpleTable() {
        this.styleClass = TABLE_STYLE_NAME;
        this.columns = new ArrayList<>();
    }


    /**
     * Sets the column widths.
     * @param widths widths (e.g. "50%", "10cm", "1fr")
     */
    @SuppressWarnings("unused")
    public SimpleTable setColumns(String... widths) {
        this.columns.clear();
        this.columns.addAll(Arrays.asList(widths));
        return this;
    }

    /**
     * Adds a row to the table header (with simple text-cells).
     */
    @SuppressWarnings("unused")
    public SimpleTable addHeaderRow(String... cellTexts) {
        List<SimpleTableCell> cells = Arrays.stream(cellTexts)
                .map(SimpleTableCell::ofHeader)
                .collect(Collectors.toList());
        this.headerRows.add(new SimpleTableRow(cells));
        return this;
    }

    /**
     * Adds a row to the table body (with simple text cells).
     */
    @SuppressWarnings("unused")
    public SimpleTable addBodyRow(String... cellTexts) {
        List<SimpleTableCell> cells = Arrays.stream(cellTexts)
                .map(SimpleTableCell::of)
                .collect(Collectors.toList());
        this.bodyRows.add(new SimpleTableRow(cells));
        return this;
    }

    /**
     * Adds a row to the table header (with more complex cells).
     */
    @SuppressWarnings("unused")
    public SimpleTable addHeaderRow(SimpleTableCell... cells) {
        this.headerRows.add(new SimpleTableRow(Arrays.asList(cells)));
        return this;
    }

    /**
     * Adds a row to the table body (with more complex cells).
     */
    @SuppressWarnings("unused")
    public SimpleTable addBodyRow(SimpleTableCell... cells) {
        this.bodyRows.add(new SimpleTableRow(Arrays.asList(cells)));
        return this;
    }
}

