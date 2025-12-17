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
import de.fkkaiser.model.JsonPropertyName;
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.style.ElementBlockStyleProperties;
import de.fkkaiser.model.style.ElementStyle;
import de.fkkaiser.model.style.StyleResolverContext;
import de.fkkaiser.model.style.TableCellStyleProperties;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a table cell, which acts as a container for other block-level elements.
 * It is not a full Element itself but participates in style resolution.
 *
 * @author Katrin Kaiser
 * @version 1.0.2
 */
public class TableCell {

    private final String styleClass;
    private final List<Element> elements;

    private final int colspan;
    private final int rowspan;

    @JsonIgnore
    private TableCellStyleProperties resolvedStyle;

    /**
     * Creates a new TableCell with the specified properties.
     *
     * <p>This constructor is used by Jackson during JSON deserialization.
     * All parameters are optional in the JSON structure.</p>
     *
     * @param styleClass the CSS-like style class for styling properties; may be {@code null}
     * @param elements   the list of child elements contained within the cell; may be {@code null}
     * @param colspan    the number of columns this cell spans; defaults to 1 if {@code null}
     * @param rowspan    the number of rows this cell spans; defaults to 1 if {@code null}
     */
    @PublicAPI
    @JsonCreator
    public TableCell(
            @JsonProperty(JsonPropertyName.STYLE_CLASS) String styleClass,
            @JsonProperty(JsonPropertyName.ELEMENTS) List<Element> elements,
            @JsonProperty(JsonPropertyName.COL_SPAN) Integer colspan,
            @JsonProperty(JsonPropertyName.ROW_SPAN) Integer rowspan) {
        this.styleClass = styleClass;
        this.elements = Objects.requireNonNullElse(elements, List.of());
        this.colspan = Optional.ofNullable(colspan).orElse(1);
        this.rowspan = Optional.ofNullable(rowspan).orElse(1);
    }


    /**
     * Gets the style class of the table cell.
     *
     * @return the style class
     */
    public String getStyleClass() {
        return styleClass;
    }

    /**
     * Gets the list of elements contained in the table cell.
     *
     * @return the list of elements
     */
    @Internal
    public List<Element> getElements() {
        return elements;
    }

    /**
     * Gets the number of columns this cell spans.
     *
     * @return the colspan value
     */
    @Internal
    public int getColspan() {
        return colspan;
    }

    /**
     * Gets the number of rows this cell spans.
     *
     * @return the rowspan value
     */
    @Internal
    public int getRowspan() {
        return rowspan;
    }

    /**
     * Gets the resolved style properties for this table cell.
     *
     * @return the resolved TableCellStyleProperties
     */
    @Internal
    public TableCellStyleProperties getResolvedStyle() {
        return resolvedStyle;
    }


    /**
     * Resolves styles for the cell and its contained elements.
     *
     * @param context The current style context.
     */
    @Internal
    public void resolveStyles(StyleResolverContext context) {

        ElementBlockStyleProperties parentStyle = context.parentBlockStyle();

        TableCellStyleProperties specificStyle = Optional.ofNullable(context.styleMap().get(this.getStyleClass()))
                .map(ElementStyle::properties)
                .filter(TableCellStyleProperties.class::isInstance)
                .map(TableCellStyleProperties.class::cast)
                .orElse(new TableCellStyleProperties());
        TableCellStyleProperties finalStyle = specificStyle.copy();
        finalStyle.mergeWith(parentStyle);


        this.resolvedStyle = finalStyle;
        StyleResolverContext childContext = context.createChildContext(this.getResolvedStyle());
        elements.forEach(element -> element.resolveStyles(childContext));
    }
}