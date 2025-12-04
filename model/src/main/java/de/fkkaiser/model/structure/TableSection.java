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
import de.fkkaiser.model.style.StyleResolverContext;
import de.fkkaiser.model.style.StyleResolverContext;

import java.util.List;
import java.util.Objects;

/**
 * Represents a section of a table (header or body), containing multiple rows.
 * @author Katrin Kaiser
 * @version 1.0.1
 */
public record TableSection(List<TableRow> rows) {

    @JsonCreator
    public TableSection(@JsonProperty(JsonPropertyName.ROWS) List<TableRow> rows) {
        this.rows = Objects.requireNonNullElse(rows, List.of());
    }

    /**
     * Delegates style resolution to all rows in this section.
     *
     * @param context The current style context.
     */
    public void resolveStyles(StyleResolverContext context) {
        rows.forEach(row -> row.resolveStyles(context));
    }
}
