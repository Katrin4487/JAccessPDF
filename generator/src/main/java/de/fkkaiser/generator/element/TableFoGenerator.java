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
package de.fkkaiser.generator.element;

import de.fkkaiser.generator.GenerateUtils;
import de.fkkaiser.generator.ImageResolver;
import de.fkkaiser.generator.XslFoGenerator;
import de.fkkaiser.model.structure.*;
import de.fkkaiser.model.style.StyleSheet;
import de.fkkaiser.model.style.TableCellStyleProperties;
import de.fkkaiser.model.style.TableStyleProperties;

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
    public void generate(Element element,
                         StyleSheet styleSheet,
                         StringBuilder builder,
                         List<Headline> headlines,
                         ImageResolver resolver,
                         boolean isExternalArtefact) {
        Table table = (Table) element;
        TableStyleProperties style = table.getResolvedStyle();

        // The entire table is wrapped in a block to control spacing before/after.
        builder.append("      <fo:block fox:content-type=\"external-artifact\"");
        if (style != null) {
            // Apply text properties from the table style to the container block
            setFontStyle(styleSheet, style, builder);
        }
        builder.append(">");

        // Start the table and apply table-specific styles
        builder.append("        <fo:table");
        if (style != null) {
            if (style.getBorderCollapse() != null) {
                builder.append(" border-collapse=\"").append(GenerateUtils.escapeXml(style.getBorderCollapse())).append("\"");
            }
            if (style.getWidth() != null) {
                builder.append(" width=\"").append(GenerateUtils.escapeXml(style.getWidth())).append("\"");
            }
        }
        builder.append(" table-layout=\"fixed\">");

        // Define table columns

        for (String colWidth : table.getColumns()) {
            builder.append("          <fo:table-column column-width=\"").append(GenerateUtils.escapeXml(colWidth)).append("\"/>\n");

        }

        // Generate table header
        if (table.getHeader() != null) {
            builder.append("          <fo:table-header>");
            generateRows(table.getHeader(), styleSheet, builder, headlines, resolver);
            builder.append("          </fo:table-header>");
        }
        if (table.getFooter() != null) {
            builder.append("          <fo:table-footer>");
            generateRows(table.getFooter(), styleSheet, builder, headlines, resolver);
            builder.append("          </fo:table-footer>");
        }

        // Generate table body
        if (table.getBody() != null) {
            builder.append("          <fo:table-body>");
            generateRows(table.getBody(), styleSheet, builder, headlines, resolver);
            builder.append("          </fo:table-body>");
        }


        builder.append("        </fo:table>");
        builder.append("      </fo:block>");
    }


    /**
     * Helper method: generates rows for a specific table section (body, header, footer).
     */
    private void generateRows(TableSection section, StyleSheet styleSheet, StringBuilder builder, List<Headline> headlines, ImageResolver resolver) {
        if (section == null || section.rows() == null) return;
        for (TableRow row : section.rows()) {
            builder.append("            <fo:table-row>");
            if (row.cells() != null) {
                for (TableCell cell : row.cells()) {
                    generateCell(cell, styleSheet, builder, headlines, resolver);
                }
            }
            builder.append("            </fo:table-row>");
        }
    }

    /**
     * Helper method: creates a single cell with content.
     */
    private void generateCell(TableCell cell, StyleSheet styleSheet, StringBuilder builder, List<Headline> headlines, ImageResolver resolver) {
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
                builder.append(" border=\"").append(GenerateUtils.escapeXml(style.getBorder())).append("\"");
            }
            if (style.getPadding() != null) {
                builder.append(" padding=\"").append(GenerateUtils.escapeXml(style.getPadding())).append("\"");
            }
            if (style.getBackgroundColor() != null) {
                builder.append(" background-color=\"").append(GenerateUtils.escapeXml(style.getBackgroundColor())).append("\"");
            }
            if (style.getVerticalAlign() != null) {
                builder.append(" display-align=\"").append(GenerateUtils.escapeXml(style.getVerticalAlign())).append("\"");
            }
            // Apply inheritable font styles
            this.setFontStyle(styleSheet, style, builder);
        }

        builder.append(">\n");

        // The content of a cell is a block, so we need a block container.
        builder.append("                <fo:block>");
        // Delegate generation of the cell's content back to the main generator.
        mainGenerator.generateBlockElements(cell.getElements(), styleSheet, builder, headlines, resolver, false);
        builder.append("                </fo:block>");

        builder.append("              </fo:table-cell>");
    }
}
