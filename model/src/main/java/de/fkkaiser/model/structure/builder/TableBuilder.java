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
package de.fkkaiser.model.structure.builder;

import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.structure.*;

import java.util.ArrayList;
import java.util.List;

public class TableBuilder {

    private final String styleClass;
    private List<String> columns;
    private TableSection header;
    private TableSection body;
    private TableSection footer;

    private String headerStyleClass;
    private String bodyStyleClass;
    private String footerStyleClass;

    private TableRow currentRow;

    /**
     * Constructor for TableBuilder.
     * @param styleClass The style class to be applied to the table.
     */
    @Internal
    public TableBuilder(String styleClass) {
        this.styleClass = styleClass;
    }


    /**
     * Adds column-sizes as String to the table (e.g. "50px", "2cm", "50%").
     * The first size corresponds to the first column, the second to the second column, and so on.
     *
     * @param columns The column sizes.
     * @return The TableBuilder instance for method chaining.
     */
    @PublicAPI
    public TableBuilder addColumns(String ... columns) {
        this.columns = List.of(columns);
        return this;
    }

    /**
     * Adds a header section to the table.
     *
     * @param header The TableSection representing the header.
     * @return The TableBuilder instance for method chaining.
     */
    @PublicAPI
    public TableBuilder addHeader(TableSection header) {
        this.header = header;
        return this;
    }
    /**
     * Adds a body section to the table.
     *
     * @param body The TableSection representing the body.
     * @return The TableBuilder instance for method chaining.
     */
    @PublicAPI
    public TableBuilder addBody(TableSection body) {
        this.body = body;
        return this;
    }
    /**
     * Adds a footer section to the table.
     *
     * @param footer The TableSection representing the footer.
     * @return The TableBuilder instance for method chaining.
     */
    @PublicAPI
    public TableBuilder addFooter(TableSection footer) {
        this.footer = footer;
        return this;
    }

    /**
     * Adds a header row to the table.
     *
     * @param headerRow The {@link TableRow} representing the header row.
     * @return The TableBuilder instance for method chaining.
     */
    @PublicAPI
    public TableBuilder addHeaderRow(TableRow headerRow) {
        List<TableRow> rows = new ArrayList<>();
        rows.add(headerRow);
        this.header = new TableSection(rows);
        return this;
    }

    /**
     * Adds a footer row to the table.
     *
     * @param footerRow The {@link TableRow} representing the footer row.
     * @return The TableBuilder instance for method chaining.
     */
    @PublicAPI
    public TableBuilder addFooterRow(TableRow footerRow) {
        List<TableRow> rows = new ArrayList<>();
        rows.add(footerRow);
        this.footer = new TableSection(rows);
        return this;
    }

    /**
     * Adds a body row to the table.
     *
     * @param bodyRow The {@link TableRow} representing the body row.
     * @return The TableBuilder instance for method chaining.
     */
    @PublicAPI
    public TableBuilder addBodyRow(TableRow bodyRow) {
        if(this.body == null) {
            this.body = new TableSection(new ArrayList<>());
        }
        this.body.rows().add(bodyRow);
        return this;
    }

    /**
     * Adds header cells to the table.
     *
     * @param cells The {@link TableCell} instances representing the header cells.
     * @return The TableBuilder instance for method chaining.
     */
    @PublicAPI
    public TableBuilder addHeaderCells(TableCell ... cells) {
        TableRow row = new TableRow(List.of(cells));
        return addHeaderRow(row);
    }

    /**
     * Adds footer cells to the table.
     *
     * @param cells The {@link TableCell} instances representing the footer cells.
     * @return The TableBuilder instance for method chaining.
     */
    @PublicAPI
    public TableBuilder addFooterCells(TableCell ... cells) {
        TableRow row = new TableRow(List.of(cells));
        return addFooterRow(row);
    }

    /**
     * Adds header cells to the table from the given elements.
     *
     * @param elements The {@link Element} instances to be added as header cells.
     * @return The TableBuilder instance for method chaining.
     */
    @PublicAPI
    public TableBuilder addHeaderCells(Element ... elements) {
        for(Element element : elements) {
            TableCell cell = new TableCell(this.headerStyleClass, List.of(element),0,0);
            if(this.header == null) {
                this.header = new TableSection(new ArrayList<>());
                List<TableCell> cells = new ArrayList<>();
                cells.add(cell);
                this.header.rows().add(new TableRow(cells));
            }else {
                this.header.rows().getFirst().cells().add(cell);
            }
        }
        return this;
    }

    /**
     * Adds footer cells to the table from the given elements.
     *
     * @param elements The {@link Element} instances to be added as footer cells.
     * @return The TableBuilder instance for method chaining.
     */
    @PublicAPI
    public TableBuilder addFooterCells(Element ... elements) {
        for(Element element : elements) {
            TableCell cell = new TableCell(this.footerStyleClass, List.of(element),0,0);
            if(this.footer == null) {
                this.footer = new TableSection(new ArrayList<>());
                List<TableCell> cells = new ArrayList<>();
                cells.add(cell);
                this.footer.rows().add(new TableRow(cells));
            }else {
                this.footer.rows().getFirst().cells().add(cell);
            }
        }
        return this;
    }



    /**
     * Adds a single header cell to the table from the given element.
     *
     * @param element The {@link Element} to be added as a header cell.
     * @return The TableBuilder instance for method chaining.
     */
    @PublicAPI
    public TableBuilder addHeaderCell(Element element) {
        TableCell cell = new TableCell(this.headerStyleClass, List.of(element),0,0);
        addHeaderCells(cell);
        return this;
    }

    /**
     * Adds a single footer cell to the table from the given element.
     *
     * @param element The {@link Element} to be added as a footer cell.
     * @return The TableBuilder instance for method chaining.
     */
    @PublicAPI
    public TableBuilder addFooterCell(Element element) {
        TableCell cell = new TableCell(this.headerStyleClass, List.of(element),0,0);
        addFooterCells(cell);
        return this;
    }

    /**
     * Starts a new body row for the table.
     *
     * @return The TableBuilder instance for method chaining.
     */
    public TableBuilder startBodyRow() {
        if(this.body == null) {
            this.body = new TableSection(new ArrayList<>());
        }
        this.currentRow = new TableRow(new ArrayList<>());
        return this;
    }

    /**
     * Ends the current body row for the table.
     *
     * @return The TableBuilder instance for method chaining.
     */
    public TableBuilder endBodyRow() {
        if(this.currentRow != null) {
            this.body.rows().add(this.currentRow);
            this.currentRow = null;
        }
        return this;
    }
    /**
     * Adds a body cell to the current body row of the table.
     * Please ensure that startBodyRow() has been called before adding body cells.
     *
     * @param element The {@link Element} to be added as a body cell.
     * @return The TableBuilder instance for method chaining.
     */
    public TableBuilder addBodyCell(Element element){
        if(this.currentRow != null) {
            TableCell cell = new TableCell(this.bodyStyleClass, List.of(element),0,0);
            this.currentRow.cells().add(cell);
        }
        return this;
    }

    /**
     * Adds a style class to the header section of the table.
     *
     * @param styleClass The style class to be applied.
     * @return The TableBuilder instance for method chaining.
     */
    public TableBuilder addHeaderStyle(String styleClass) {
        this.headerStyleClass = styleClass;
        return this;
    }
    /**
     * Adds a style class to the body section of the table.
     *
     * @param styleClass The style class to be applied.
     * @return The TableBuilder instance for method chaining.
     */
    public TableBuilder addBodyStyle(String styleClass) {
        this.bodyStyleClass = styleClass;
        return this;
    }
    /**
     * Adds a style class to the footer section of the table.
     *
     * @param styleClass The style class to be applied.
     * @return The TableBuilder instance for method chaining.
     */
    public TableBuilder addFooterStyle(String styleClass) {
        this.footerStyleClass = styleClass;
        return this;
    }

    /**
     * Builds and returns the Table instance.
     *
     * @return The constructed Table instance.
     */
    public Table build() {
        if(this.currentRow != null) {
            endBodyRow();
        }
        return new Table(styleClass, columns, header, body, footer);
    }
}
