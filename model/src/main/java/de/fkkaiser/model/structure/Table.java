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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.fkkaiser.model.JsonPropertyName;
import de.fkkaiser.model.style.ElementBlockStyleProperties;
import de.fkkaiser.model.style.ElementStyle;
import de.fkkaiser.model.style.StyleResolverContext;
import de.fkkaiser.model.style.TableStyleProperties;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@JsonTypeName(JsonPropertyName.TABLE)
public final class Table implements Element {

    private final String styleClass;
    private final List<String> columns;
    private final TableSection header;
    private final TableSection body;
    private final TableSection footer;

    @JsonIgnore
    private TableStyleProperties resolvedStyle;

    @JsonCreator
    public Table(
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("columns") List<String> columns,
            @JsonProperty("header") TableSection header,
            @JsonProperty("body") TableSection body,
            @JsonProperty("footer") TableSection footer
    ) {
        this.styleClass = styleClass;
        this.columns = Objects.requireNonNullElse(columns,List.of());
        this.header = header;
        this.body = body;
        this.footer = footer;
    }

    // --- Getters ---
    @Override
    public String getStyleClass() { return styleClass; }
    public List<String> getColumns() { return columns; }
    public TableSection getHeader() { return header; }
    public TableSection getBody() { return body; }
    public TableSection getFooter() { return footer; }
    public TableStyleProperties getResolvedStyle() { return resolvedStyle; }

    @Override
    public ElementTargetType getType() {
        return ElementTargetType.TABLE;
    }

    /**
     * Resolves styles for the table and all its children (sections, rows, cells).
     * It follows the established pattern:
     * 1. Resolve the style for the table container itself, merging with parent context.
     * 2. Create a new, more specific context for the children.
     * 3. Delegate the style resolution down the hierarchy.
     * @param context The current style context.
     */
    @Override
    public void resolveStyles(StyleResolverContext context) {

        ElementBlockStyleProperties parentStyle = context.parentBlockStyle();
        ElementStyle specificElementStyle = context.styleMap().get(this.getStyleClass());

        TableStyleProperties specificTableStyle = Optional.ofNullable(specificElementStyle)
                .map(ElementStyle::properties)
                .filter(TableStyleProperties.class::isInstance)
                .map(TableStyleProperties.class::cast)
                .orElse(new TableStyleProperties()); // Standard-Style, wenn nichts gefunden wurde

        TableStyleProperties finalStyle = specificTableStyle.copy();
        finalStyle.mergeWith(parentStyle);

        // KORREKTUR: Das Feld wird hier direkt gesetzt.
        this.resolvedStyle = finalStyle;

        StyleResolverContext childContext = context.createChildContext(this.getResolvedStyle());Stream.of(header, body, footer)
                .filter(Objects::nonNull)
                .forEach(section -> section.resolveStyles(childContext));
    }
}
