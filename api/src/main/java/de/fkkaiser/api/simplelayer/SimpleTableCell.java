package de.fkkaiser.api.simplelayer;

import de.fkkaiser.model.annotation.PublicAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a simple table cell that can contain various content elements such as text, lists, and images.
 * Provides functionality to manage cell content, styling, and cell spanning (rowspan and colspan).
 */
@PublicAPI
public class SimpleTableCell {

    private static final String DEFAULT_STYLE = "default";
    private static final String HEADER_STYLE = "header-cell";
    private static final String FOOTER_STYLE = "footer-cell";
    private static final String LIST_STYLE_UNORDERED = "list-style-unordered";
    private static final String LIST_STYLE_ORDERED = "list-style-ordered";


    final List<SimpleDocument.ContentElement> elements = new ArrayList<>();
    int colspan = 1;
    int rowspan = 1;

    /**
     * Creates an empty row
     */
    @PublicAPI
    public SimpleTableCell() {
        // empty
    }

    /**
     * Creates a new {@code SimpleTableCell} containing the specified text as content.
     *
     * @param text the text content to be included in the table cell
     * @return a new instance of {@code SimpleTableCell} with the specified text
     */
    @PublicAPI
    public static SimpleTableCell of(String text) {

        SimpleTableCell cell = new SimpleTableCell();
        cell.addParagraph(normalizeCellText(text));
        return cell;
    }

    /**
     * Creates a new {@code SimpleTableCell} configured as a header cell
     * with the specified text content.
     *
     * @param text the text to be included as the content of the header cell
     * @return a new instance of {@code SimpleTableCell} styled as a header cell
     */
    @PublicAPI
    public static SimpleTableCell ofHeader(String text) {
        SimpleTableCell cell = new SimpleTableCell();
        cell.addParagraph(normalizeCellText(text),HEADER_STYLE);
        return cell;
    }

    /**
     * Creates a new {@code SimpleTableCell} configured as a footer cell
     * with the specified text content.
     *
     * @param text the text to be included as the content of the footer cell
     * @return a new instance of {@code SimpleTableCell} styled as a footer cell
     */
    @PublicAPI
    public static SimpleTableCell ofFooter(String text) {
        SimpleTableCell cell = new SimpleTableCell();
        cell.addParagraph(normalizeCellText(text),FOOTER_STYLE);
        return cell;
    }

    /**
     * Adds a paragraph with the specified text content to this table cell.
     *
     * @param text the text content of the paragraph to be added
     * @return the current instance of {@code SimpleTableCell}, allowing for method chaining
     */
    @PublicAPI
    public SimpleTableCell addParagraph(String text) {
        this.elements.add(new SimpleDocument.ParagraphElement(text, DEFAULT_STYLE));
        return this;
    }

    @PublicAPI
    private void addParagraph(String text, String styleClassName) {
        this.elements.add(new SimpleDocument.ParagraphElement(text, styleClassName));
    }

    /**
     * Adds an unordered list to this table cell using the provided list of items.
     *
     * @param items the list of strings to be included as items in the unordered list
     * @return the current instance of {@code SimpleTableCell}, allowing for method chaining
     */
    @PublicAPI
    public SimpleTableCell addUnorderedList(List<String> items) {
        this.elements.add(new SimpleDocument.ListElement(items, LIST_STYLE_UNORDERED, false));
        return this;
    }

    /**
     * Adds an ordered list to this table cell using the provided list of items.
     *
     * @param items the list of strings to be included as items in the ordered list
     * @return the current instance of {@code SimpleTableCell}, allowing for method chaining
     */
    @PublicAPI
    public SimpleTableCell addOrderedList(List<String> items) {
        this.elements.add(new SimpleDocument.ListElement(items, LIST_STYLE_ORDERED, true));
        return this;
    }

    /**
     * Adds an image to the table cell using the specified relative path.
     *
     * @param relativePath the relative path of the image to be added
     * @return the current instance of {@code SimpleTableCell}, allowing for method chaining
     */
    @PublicAPI
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
     @PublicAPI
    public SimpleTableCell withRowspan(int span) {
        this.rowspan = Math.max(1, span);
        return this;
    }

    private static String normalizeCellText(String text) {
        if (text == null || text.trim().isEmpty()) {
            // non-breaking space ensures a visible/accessible text node for PDF/UA
            return "\u00A0";
        }
        return text;
    }
}
