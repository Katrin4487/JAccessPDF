package de.fkkaiser.api.simplelayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a simple table cell that can contain various content elements such as text, lists, and images.
 * Provides functionality to manage cell content, styling, and cell spanning (rowspan and colspan).
 */
public class SimpleTableCell {

    final List<SimpleDocument.ContentElement> elements = new ArrayList<>();
    int colspan = 1;
    int rowspan = 1;

    /**
     * Creates an empty row
     */
    public SimpleTableCell() {
        // empty
    }

    /**
     * Creates a new {@code SimpleTableCell} containing the specified text as content.
     *
     * @param text the text content to be included in the table cell
     * @return a new instance of {@code SimpleTableCell} with the specified text
     */
    public static SimpleTableCell of(String text) {
        SimpleTableCell cell = new SimpleTableCell();
        cell.addParagraph(text);
        return cell;
    }

    /**
     * Creates a new {@code SimpleTableCell} configured as a header cell
     * with the specified text content.
     *
     * @param text the text to be included as the content of the header cell
     * @return a new instance of {@code SimpleTableCell} styled as a header cell
     */
    public static SimpleTableCell ofHeader(String text) {
        SimpleTableCell cell = new SimpleTableCell();
        cell.addParagraph(text,"header-cell");
        return cell;
    }

    /**
     * Creates a new {@code SimpleTableCell} configured as a footer cell
     * with the specified text content.
     *
     * @param text the text to be included as the content of the footer cell
     * @return a new instance of {@code SimpleTableCell} styled as a footer cell
     */
    @SuppressWarnings("unused")
    public static SimpleTableCell ofFooter(String text) {
        SimpleTableCell cell = new SimpleTableCell();
        cell.addParagraph(text,"footer-cell");
        return cell;
    }

    /**
     * Adds a paragraph with the specified text content to this table cell.
     *
     * @param text the text content of the paragraph to be added
     * @return the current instance of {@code SimpleTableCell}, allowing for method chaining
     */
    public SimpleTableCell addParagraph(String text) {
        this.elements.add(new SimpleDocument.ParagraphElement(text, "default"));
        return this;
    }

    private void addParagraph(String text, String styleClassName) {
        this.elements.add(new SimpleDocument.ParagraphElement(text, styleClassName));
    }

    /**
     * Adds an unordered list to this table cell using the provided list of items.
     *
     * @param items the list of strings to be included as items in the unordered list
     * @return the current instance of {@code SimpleTableCell}, allowing for method chaining
     */
    @SuppressWarnings("unused")
    public SimpleTableCell addUnorderedList(List<String> items) {
        this.elements.add(new SimpleDocument.ListElement(items, "list-style-unordered", false));
        return this;
    }

    /**
     * Adds an ordered list to this table cell using the provided list of items.
     *
     * @param items the list of strings to be included as items in the ordered list
     * @return the current instance of {@code SimpleTableCell}, allowing for method chaining
     */
    @SuppressWarnings("unused")
    public SimpleTableCell addOrderedList(List<String> items) {
        this.elements.add(new SimpleDocument.ListElement(items, "list-style-ordered", true));
        return this;
    }

    /**
     * Adds an image to the table cell using the specified relative path.
     *
     * @param relativePath the relative path of the image to be added
     * @return the current instance of {@code SimpleTableCell}, allowing for method chaining
     */
    @SuppressWarnings("unused")
    public SimpleTableCell addImage(String relativePath) {
        String fullPath = SimpleDocumentBuilder.normalizeImagePath(relativePath);
        this.elements.add(new SimpleDocument.ImageElement(fullPath));
        return this;
    }

    /**
     * Sets the column span for this table cell. The column span defines how many columns
     * the cell should occupy. If the provided value is less than 1, it defaults to 1.
     *
     * @param span the number of columns the cell should span
     * @return the current instance of {@code SimpleTableCell}, allowing for method chaining
     */
    public SimpleTableCell withColspan(int span) {
        this.colspan = Math.max(1, span);
        return this;
    }

    /**
     * Sets the row span for this table cell. The row span defines how many rows
     * the cell should occupy. If the provided value is less than 1, it defaults to 1.
     *
     * @param span the number of rows the cell should span
     * @return the current instance of {@code SimpleTableCell}, allowing for method chaining
     */
    @SuppressWarnings("unused")
    public SimpleTableCell withRowspan(int span) {
        this.rowspan = Math.max(1, span);
        return this;
    }
}
