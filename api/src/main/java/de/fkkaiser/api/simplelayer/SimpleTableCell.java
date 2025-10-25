package de.fkkaiser.api.simplelayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Eine Helferklasse, die eine einzelne Tabellenzelle repräsentiert.
 * Sie kann komplexe Inhalte wie Absätze und Listen enthalten.
 */
public class SimpleTableCell {

    final List<SimpleDocument.ContentElement> elements = new ArrayList<>();
    int colspan = 1;
    int rowspan = 1;

    /**
     * Erstellt eine leere Zelle.
     */
    public SimpleTableCell() {
        // Leer, für komplexen Inhalt
    }

    /**
     * Erstellt eine Zelle, die nur einen einzelnen Textabsatz enthält.
     * @param text Der Text für den Absatz.
     * @return Eine neue SimpleTableCell.
     */
    public static SimpleTableCell of(String text) {
        SimpleTableCell cell = new SimpleTableCell();
        cell.addParagraph(text);
        return cell;
    }

    /**
     * Fügt einen Absatz zu dieser Zelle hinzu.
     */
    public SimpleTableCell addParagraph(String text) {
        this.elements.add(new SimpleDocument.ParagraphElement(text, "default"));
        return this;
    }

    /**
     * Fügt eine unsortierte Liste zu dieser Zelle hinzu.
     */
    public SimpleTableCell addUnorderedList(List<String> items) {
        this.elements.add(new SimpleDocument.ListElement(items, "list-style-unordered", false));
        return this;
    }

    /**
     * Fügt eine sortierte Liste zu dieser Zelle hinzu.
     */
    public SimpleTableCell addOrderedList(List<String> items) {
        this.elements.add(new SimpleDocument.ListElement(items, "list-style-ordered", true));
        return this;
    }

    /**
     * Fügt ein Bild zu dieser Zelle hinzu.
     * @param relativePath Der Pfad zum Bild (z.B. "logo.png").
     */
    public SimpleTableCell addImage(String relativePath) {
        // Nutzt die gleiche Normalisierungslogik wie der Builder
        String fullPath = SimpleDocumentBuilder.normalizeImagePath(relativePath);
        this.elements.add(new SimpleDocument.ImageElement(fullPath));
        return this;
    }

    /**
     * Setzt den Spaltenumfang (colspan) für diese Zelle.
     */
    public SimpleTableCell withColspan(int span) {
        this.colspan = Math.max(1, span);
        return this;
    }

    /**
     * Setzt den Zeilenumfang (rowspan) für diese Zelle.
     */
    public SimpleTableCell withRowspan(int span) {
        this.rowspan = Math.max(1, span);
        return this;
    }
}
