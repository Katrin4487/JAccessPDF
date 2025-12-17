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
package de.fkkaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.fkkaiser.model.JsonPropertyName;
import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.style.StyleResolverContext;

import java.util.List;
import java.util.Objects;

/**
 * Represents a row in a table, containing multiple cells.
 *
 * @author Katrin Kaiser
 * @version 1.0.1
 */
public record TableRow(List<TableCell> cells) {

    /**
     * Creates a new TableRow with the specified list of cells.
     *
     * <p>This constructor is used by Jackson during JSON deserialization.
     * If the cells parameter is null, it defaults to an empty list.</p>
     *
     * @param cells the list of table cells in this row; may be {@code null}
     */
    @PublicAPI
    @JsonCreator
    public TableRow(@JsonProperty(JsonPropertyName.CELLS) List<TableCell> cells) {
        this.cells = Objects.requireNonNullElse(cells, List.of());
    }

    /**
     * Delegates style resolution to all cells in this row.
     *
     * @param context The current style context.
     */
    public void resolveStyles(StyleResolverContext context) {
        cells.forEach(cell -> cell.resolveStyles(context));
    }
}
