package de.fkkaiser.api.simplelayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Eine Helferklasse, um eine Tabelle für den SimpleDocumentBuilder zu definieren.
 * Sie kapselt die Komplexität der Tabellenerstellung.
 *
 * Beispiel:
 * <pre>
 * SimpleTable table = new SimpleTable()
 * .setColumns("50%", "50%")
 * .addHeaderRow("Header 1", "Header 2")
 * .addBodyRow("Zelle 1", "Zelle 2")
 * .addBodyRow(
 * SimpleTableCell.of("Komplexe Zelle").addParagraph("Absatz..."),
 * SimpleTableCell.of("Zelle 4").withColspan(2)
 * );
 * builder.addTable(table);
 * </pre>
 */
public class SimpleTable {

    final String styleClass;
    final List<String> columns;
    final List<SimpleTableRow> headerRows = new ArrayList<>();
    final List<SimpleTableRow> bodyRows = new ArrayList<>();
    // Footer wird der Einfachheit halber weggelassen, kann aber leicht ergänzt werden

    /**
     * Erstellt eine Tabelle mit Standard-Style.
     */
    public SimpleTable() {
        this.styleClass = "table-default";
        this.columns = new ArrayList<>();
    }

    /**
     * Erstellt eine Tabelle mit einem spezifischen Style-Namen.
     * @param styleClass Der Name des Styles (definiert im SimpleStyleManager).
     */
    public SimpleTable(String styleClass) {
        this.styleClass = styleClass;
        this.columns = new ArrayList<>();
    }

    /**
     * Setzt die Spaltenbreiten.
     * @param widths Breitenangaben (z.B. "50%", "10cm", "1fr")
     */
    public SimpleTable setColumns(String... widths) {
        this.columns.clear();
        this.columns.addAll(Arrays.asList(widths));
        return this;
    }

    /**
     * Fügt eine Zeile zum Tabellenkopf hinzu (mit einfachen Text-Zellen).
     */
    public SimpleTable addHeaderRow(String... cellTexts) {
        List<SimpleTableCell> cells = Arrays.stream(cellTexts)
                .map(SimpleTableCell::of)
                .collect(Collectors.toList());
        this.headerRows.add(new SimpleTableRow(cells));
        return this;
    }

    /**
     * Fügt eine Zeile zum Tabellenkörper hinzu (mit einfachen Text-Zellen).
     */
    public SimpleTable addBodyRow(String... cellTexts) {
        List<SimpleTableCell> cells = Arrays.stream(cellTexts)
                .map(SimpleTableCell::of)
                .collect(Collectors.toList());
        this.bodyRows.add(new SimpleTableRow(cells));
        return this;
    }

    /**
     * Fügt eine Zeile zum Tabellenkopf hinzu (mit komplexen Zellen).
     */
    public SimpleTable addHeaderRow(SimpleTableCell... cells) {
        this.headerRows.add(new SimpleTableRow(Arrays.asList(cells)));
        return this;
    }

    /**
     * Fügt eine Zeile zum Tabellenkörper hinzu (mit komplexen Zellen).
     */
    public SimpleTable addBodyRow(SimpleTableCell... cells) {
        this.bodyRows.add(new SimpleTableRow(Arrays.asList(cells)));
        return this;
    }
}

/**
 * Interne Repräsentation einer Tabellenzeile.
 */
class SimpleTableRow {
    final List<SimpleTableCell> cells;
    SimpleTableRow(List<SimpleTableCell> cells) {
        this.cells = cells;
    }
}
