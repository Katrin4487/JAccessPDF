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
import de.fkkaiser.generator.TagBuilder;
import de.fkkaiser.generator.XslFoGenerator;
import de.fkkaiser.model.structure.*;
import de.fkkaiser.model.style.StyleSheet;
import de.fkkaiser.model.style.TableCellStyleProperties;
import de.fkkaiser.model.style.TableStyleProperties;

import java.util.List;

/**
 * TableFoGenerator extends ElementFoGenerator and is responsible for
 * generating XSL-FO block representation of a table element.
 */
public class TableFoGenerator extends ElementFoGenerator {

    private final XslFoGenerator mainGenerator;

    /**
     * Constructor for creating a TableFoGenerator object.
     *
     * @param mainGenerator The main XSL-FO generator to be used by this TableFoGenerator.
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
     * @param resolver   The image resolver for handling images.
     * @param isExternalArtefact Whether this is an external artifact.
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
        TagBuilder containerBlock = GenerateUtils.tagBuilder("block")
                .addAttribute("fox:content-type", "external-artifact");

        if (style != null) {
            // Apply text properties from the table style to the container block
            setFontStyle(styleSheet, style, containerBlock);
        }

        // Build the table
        TagBuilder tableBuilder = GenerateUtils.tagBuilder("table")
                .addAttribute("table-layout", "fixed");

        if (style != null) {
            tableBuilder
                    .addAttribute("border-collapse", style.getBorderCollapse())
                    .addAttribute("width", style.getWidth());
        }

        // Add table columns
        for (String colWidth : table.getColumns()) {
            tableBuilder.addChild(
                    GenerateUtils.tagBuilder("table-column")
                            .addAttribute("column-width", colWidth)
            );
        }

        // Add table header
        if (table.getHeader() != null) {
            TagBuilder headerBuilder = GenerateUtils.tagBuilder("table-header");
            generateRows(table.getHeader(), styleSheet, headerBuilder, headlines, resolver);
            tableBuilder.addChild(headerBuilder);
        }

        // Add table footer
        if (table.getFooter() != null) {
            TagBuilder footerBuilder = GenerateUtils.tagBuilder("table-footer");
            generateRows(table.getFooter(), styleSheet, footerBuilder, headlines, resolver);
            tableBuilder.addChild(footerBuilder);
        }

        // Add table body
        if (table.getBody() != null) {
            TagBuilder bodyBuilder = GenerateUtils.tagBuilder("table-body");
            generateRows(table.getBody(), styleSheet, bodyBuilder, headlines, resolver);
            tableBuilder.addChild(bodyBuilder);
        }

        containerBlock.addChild(tableBuilder);
        containerBlock.buildInto(builder);
    }

    /**
     * Helper method: generates rows for a specific table section (body, header, footer).
     *
     * @param section    The table section to generate rows for.
     * @param styleSheet The stylesheet.
     * @param builder    The TagBuilder to add rows to.
     * @param headlines  The list of headlines.
     * @param resolver   The image resolver.
     */
    private void generateRows(TableSection section, StyleSheet styleSheet, TagBuilder builder,
                              List<Headline> headlines, ImageResolver resolver) {
        if (section == null || section.rows() == null) return;

        for (TableRow row : section.rows()) {
            TagBuilder rowBuilder = GenerateUtils.tagBuilder("table-row");

            if (row.cells() != null) {
                for (TableCell cell : row.cells()) {
                    TagBuilder cellBuilder = generateCell(cell, styleSheet, headlines, resolver);
                    rowBuilder.addChild(cellBuilder);
                }
            }

            builder.addChild(rowBuilder);
        }
    }

    /**
     * Helper method: creates a single cell with content.
     *
     * @param cell       The table cell to generate.
     * @param styleSheet The stylesheet.
     * @param headlines  The list of headlines.
     * @param resolver   The image resolver.
     * @return The TagBuilder for the cell.
     */
    private TagBuilder generateCell(TableCell cell, StyleSheet styleSheet,
                                    List<Headline> headlines, ImageResolver resolver) {
        TableCellStyleProperties style = cell.getResolvedStyle();

        TagBuilder cellBuilder = GenerateUtils.tagBuilder("table-cell");

        // Add colspan and rowspan attributes
        if (cell.getColspan() > 1) {
            cellBuilder.addAttribute("number-columns-spanned", String.valueOf(cell.getColspan()));
        }
        if (cell.getRowspan() > 1) {
            cellBuilder.addAttribute("number-rows-spanned", String.valueOf(cell.getRowspan()));
        }

        if (style != null) {
            // Apply styles directly to the cell. The content will inherit them.
            cellBuilder
                    .addAttribute("border", style.getBorder())
                    .addAttribute("padding", style.getPadding())
                    .addAttribute("background-color", style.getBackgroundColor())
                    .addAttribute("display-align", style.getVerticalAlign());

            // Apply inheritable font styles
            this.setFontStyle(styleSheet, style, cellBuilder);
        }

        // Generate cell content block
        StringBuilder cellContent = new StringBuilder();
        mainGenerator.generateBlockElements(cell.getElements(), styleSheet, cellContent, headlines, resolver, false);

        TagBuilder contentBlock = GenerateUtils.tagBuilder("block")
                .addNestedContent(cellContent.toString());

        cellBuilder.addChild(contentBlock);

        return cellBuilder;
    }
}