package de.kaiser.generator.element;

import de.kaiser.generator.XslFoGenerator;
import de.kaiser.model.structure.*;
import de.kaiser.model.style.StyleSheet;
import de.kaiser.model.style.TableCellStyleProperties;
import de.kaiser.model.style.TableStyleProperties;

import java.net.URL;
import java.util.List;

/**
 * ETableFoGenerator extends EElementFoGenerator and is responsible for
 * generating XSL-FO block representation of a table element.
 */
public class TableFoGenerator extends ElementFoGenerator {

    private final XslFoGenerator mainGenerator;

    /**
     * Constructor for creating an ETableFoGenerator object.
     *
     * @param mainGenerator The main XSL-FO generator to be used by this ETableFoGenerator.
     */
    public TableFoGenerator(XslFoGenerator mainGenerator) {
        this.mainGenerator = mainGenerator;
    }

    /**
     * Generates the FO block representation of a table element based on the provided style information.
     * Appends the generated content to the StringBuilder.
     *
     * @param element    The table element to generate.
     * @param styleSheet The style sheet containing the necessary style information.
     * @param builder    The StringBuilder to which the generated content will be appended.
     * @param headlines  The list of headlines for bookmark generation.
     */
    @Override
    public void generate(Element element, StyleSheet styleSheet, StringBuilder builder, List<Headline> headlines, URL imageUrl) {
        Table table = (Table) element;
        TableStyleProperties style = table.getResolvedStyle();

        // The entire table is wrapped in a block to control spacing before/after.
        builder.append("      <fo:block");
        if (style != null) {
            // Apply text properties from the table style to the container block
            setFontStyle(styleSheet, style, builder);
        }
        builder.append(">\n");

        // Start the table and apply table-specific styles
        builder.append("        <fo:table");
        if (style != null) {
            if (style.getBorderCollapse() != null) {
                builder.append(" border-collapse=\"").append(escapeXml(style.getBorderCollapse())).append("\"");
            }
            if (style.getWidth() != null) {
                builder.append(" width=\"").append(escapeXml(style.getWidth())).append("\"");
            }
        }
        builder.append(" table-layout=\"fixed\">\n");

        // Define table columns
        if (table.getColumns() != null) {
            for (String colWidth : table.getColumns()) {
                builder.append("          <fo:table-column column-width=\"").append(escapeXml(colWidth)).append("\"/>\n");
            }
        }

        // Generate table header
        if (table.getHeader() != null) {
            builder.append("          <fo:table-header>\n");
            generateRows(table.getHeader(), styleSheet, builder, headlines,imageUrl);
            builder.append("          </fo:table-header>\n");
        }
        if (table.getFooter() != null) {
            builder.append("          <fo:table-footer>\n");
            generateRows(table.getFooter(), styleSheet, builder, headlines,imageUrl);
            builder.append("          </fo:table-footer>\n");
        }

        // Generate table body
        if (table.getBody() != null) {
            builder.append("          <fo:table-body>\n");
            generateRows(table.getBody(), styleSheet, builder, headlines,imageUrl);
            builder.append("          </fo:table-body>\n");
        }


        builder.append("        </fo:table>\n");
        builder.append("      </fo:block>\n");
    }


    /**
     * Helper method: generates rows for a specific table section (body, header, footer).
     */
    private void generateRows(TableSection section, StyleSheet styleSheet, StringBuilder builder, List<Headline> headlines,URL imageUrl) {
        if (section == null || section.rows() == null) return;
        for (TableRow row : section.rows()) {
            builder.append("            <fo:table-row>\n");
            if (row.cells() != null) {
                for (TableCell cell : row.cells()) {
                    generateCell(cell, styleSheet, builder, headlines,imageUrl);
                }
            }
            builder.append("            </fo:table-row>\n");
        }
    }

    /**
     * Helper method: creates a single cell with content.
     */
    private void generateCell(TableCell cell, StyleSheet styleSheet, StringBuilder builder, List<Headline> headlines,URL imageUrl) {
        TableCellStyleProperties style = cell.getResolvedStyle();

        builder.append("              <fo:table-cell");

        // NEW: Add colspan and rowspan attributes
        if (cell.getColspan() > 1) {
            builder.append(" number-columns-spanned=\"").append(cell.getColspan()).append("\"");
        }
        if (cell.getRowspan() > 1) {
            builder.append(" number-rows-spanned=\"").append(cell.getRowspan()).append("\"");
        }

        if (style != null) {
            // Apply styles directly to the cell. The content will inherit them.
            if (style.getBorder() != null) {
                builder.append(" border=\"").append(escapeXml(style.getBorder())).append("\"");
            }
            if (style.getPadding() != null) {
                builder.append(" padding=\"").append(escapeXml(style.getPadding())).append("\"");
            }
            if (style.getBackgroundColor() != null) {
                builder.append(" background-color=\"").append(escapeXml(style.getBackgroundColor())).append("\"");
            }
            if (style.getVerticalAlign() != null) {
                builder.append(" display-align=\"").append(escapeXml(style.getVerticalAlign())).append("\"");
            }
            // Apply inheritable font styles
            this.setFontStyle(styleSheet, style, builder);
        }

        builder.append(">\n");

        // The content of a cell is a block, so we need a block container.
        builder.append("                <fo:block>\n");
        // Delegate generation of the cell's content back to the main generator.
        mainGenerator.generateBlockElements(cell.getElements(), styleSheet, builder, headlines,imageUrl);
        builder.append("                </fo:block>\n");

        builder.append("              </fo:table-cell>\n");
    }
}
