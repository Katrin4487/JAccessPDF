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
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.structure.builder.TableBuilder;
import de.fkkaiser.model.style.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Represents a table element in a document structure.
 *
 * @author Katrin Kaiser
 * @version 1.0.2
 */
@JsonTypeName(JsonPropertyName.TABLE)
public final class Table implements Element {

    private final String styleClass;
    private final List<String> columns;
    private final TableSection header;
    private final TableSection body;
    private final TableSection footer;

    @JsonIgnore
    private TableStyleProperties resolvedStyle;

    /**
     * Creates a new Table element with the specified properties.
     *
     * @param styleClass the style class for the table (may be null)
     * @param columns    the list of column identifiers (defaults to empty list if null)
     * @param header     the table header section (may be null)
     * @param body       the table body section (may be null)
     * @param footer     the table footer section (may be null)
     */
    @Internal
    @JsonCreator
    public Table(
            @JsonProperty(JsonPropertyName.STYLE_CLASS) String styleClass,
            @JsonProperty(JsonPropertyName.COLUMNS) List<String> columns,
            @JsonProperty(JsonPropertyName.HEADER) TableSection header,
            @JsonProperty(JsonPropertyName.BODY) TableSection body,
            @JsonProperty(JsonPropertyName.FOOTER) TableSection footer
    ) {
        this.styleClass = styleClass;
        this.columns = Objects.requireNonNullElse(columns, List.of());
        this.header = header;
        this.body = body;
        this.footer = footer;
    }


    /**
     * Gets the style class of the table.
     *
     * @return the style class string (may be null)
     */
    @Override
    public String getStyleClass() {
        return styleClass;
    }

    /**
     * Gets the list of column identifiers for the table.
     *
     * @return the list of column identifiers
     */
    @Internal
    public List<String> getColumns() {
        return columns;
    }

    /**
     * Gets the header section of the table.
     *
     * @return the table header section (may be null)
     */
    @Internal
    public TableSection getHeader() {
        return header;
    }

    /**
     * Gets the body section of the table.
     *
     * @return the table body section (may be null)
     */
    @Internal
    public TableSection getBody() {
        return body;
    }

    /**
     * Gets the footer section of the table.
     *
     * @return the table footer section (may be null)
     */
    @Internal
    public TableSection getFooter() {
        return footer;
    }

    /**
     * Gets the resolved style properties for the table after style resolution.
     *
     * @return the resolved TableStyleProperties
     */
    @Internal
    public TableStyleProperties getResolvedStyle() {
        return resolvedStyle;
    }

    /**
     * Fluent builder for creating Table elements.
     *
     * @param styleClass The style class to be applied to the table.
     * @return A TableBuilder instance for method chaining.
     */
    @PublicAPI
    public static TableBuilder builder(String styleClass) {
        return new TableBuilder(styleClass);
    }

    /**
     * Returns the type identifier for this element.
     *
     * @return The ElementTargetType representing a table.
     */
    @Internal
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
     *
     * @param context The current style context.
     */
    @Internal
    @Override
    public void resolveStyles(StyleResolverContext context) {

        ElementBlockStyleProperties parentStyle = context.parentBlockStyle();
        ElementStyle specificElementStyle = context.styleMap().get(this.getStyleClass());


        TableStyleProperties specificTableStyle = Optional.ofNullable(specificElementStyle)
                .map(ElementStyle::properties)
                .filter(TableStyleProperties.class::isInstance)
                .map(TableStyleProperties.class::cast)
                .orElse(null);

        if(specificTableStyle == null) {
            StandardElementType docElement = getStandardElementType();
            if (docElement != null) {
                var defaultStyle = context.styleSheet().findElementStyle(docElement, null);

                if (defaultStyle.isPresent() &&
                        defaultStyle.get().properties() instanceof TableStyleProperties defaultTableStyle) {
                    specificTableStyle = defaultTableStyle.copy();

                }
            }
        }

        TableStyleProperties finalStyle = specificTableStyle != null ? specificTableStyle.copy(): new TableStyleProperties();
        finalStyle.mergeWith(parentStyle);
        this.resolvedStyle = finalStyle;

        StyleResolverContext childContext = context.createChildContext(this.getResolvedStyle());
        Stream.of(header, body, footer)
                .filter(Objects::nonNull)
                .forEach(section -> section.resolveStyles(childContext));
    }

    /**
     * Returns the standard element type for this table.
     *
     * @return StandardElementType.TABLE
     */
    @Internal
    @Override
    public StandardElementType getStandardElementType() {
        return StandardElementType.TABLE;
    }
}
