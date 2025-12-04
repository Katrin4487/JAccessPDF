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
import de.fkkaiser.model.annotation.VisibleForTesting;
import de.fkkaiser.model.structure.builder.SimpleListBuilder;
import de.fkkaiser.model.style.ElementBlockStyleProperties;
import de.fkkaiser.model.style.ElementStyle;
import de.fkkaiser.model.style.ListStyleProperties;
import de.fkkaiser.model.style.StyleResolverContext;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a list element in a document with a specific style class,
 * ordering, and list items.
 *
 * @author Katrin Kaiser
 * @version 1.1.0
 */
@JsonTypeName(JsonPropertyName.LIST)
public final class SimpleList implements Element {

    private final String styleClass;
    private final ListOrdering ordering;
    private final List<ListItem> items;

    @JsonIgnore
    private ListStyleProperties resolvedStyle;

    /**
     * Constructs a SimpleList with the specified style class, ordering, and items.
     *
     * @param styleClass The style class of the list (can be null).
     * @param ordering   The ordering of the list (defaults to UNORDERED if null).
     * @param items      The list items (must not be null or empty).
     * @throws NullPointerException     if items is null.
     * @throws IllegalArgumentException if items is empty.
     */
    @PublicAPI
    @JsonCreator
    public SimpleList(
            @JsonProperty(JsonPropertyName.STYLE_CLASS) String styleClass,
            @JsonProperty(JsonPropertyName.ORDERING) ListOrdering ordering,
            @JsonProperty(JsonPropertyName.ITEMS) List<ListItem> items
    ) {

        Objects.requireNonNull(items, "List items cannot be null");
        if(items.isEmpty()) {
            throw new IllegalArgumentException("List must contain at least one item");
        }
        this.styleClass = styleClass;
        this.ordering = ordering != null ? ordering : ListOrdering.UNORDERED;
        this.items = items;
    }

    /**
     * Returns the type of the element.
     *
     * @return The ElementTargetType representing a list.
     */
    @Internal
    @Override
    public ElementTargetType getType() {
        return ElementTargetType.LIST;
    }

    /**
     * Returns the style class of the list
     * @return The style class as a string.
     */
    @Internal
    @Override
    public String getStyleClass() {
        return styleClass;
    }


    /**
     * Returns the ordering of the list.
     *
     * @return The list ordering.
     */
    @Internal
    public ListOrdering getOrdering() { return ordering; }
    /**
     * Returns the list items.
     *
     * @return The list of ListItem objects.
     */
    @Internal
    public List<ListItem> getItems() { return items; }

    /**
     * Returns the resolved style properties for the list.
     *
     * @return The resolved ListStyleProperties.
     */
    @Internal
    public ListStyleProperties getResolvedStyle() { return resolvedStyle; }


    @Override
    public void resolveStyles(StyleResolverContext context) {
          ElementBlockStyleProperties parentStyle = context.parentBlockStyle();

        ListStyleProperties specificStyle = Optional.ofNullable(context.styleMap().get(this.getStyleClass()))
                .map(ElementStyle::properties)
                .filter(ListStyleProperties.class::isInstance)
                .map(ListStyleProperties.class::cast)
                .orElse(new ListStyleProperties());

         ListStyleProperties finalStyle = specificStyle.copy();
        finalStyle.mergeWith(parentStyle);

         this.resolvedStyle = finalStyle;

         StyleResolverContext childContext = context.createChildContext(this.getResolvedStyle());

        if (items != null) {
            for (ListItem item : items) {
                item.resolveStyles(childContext);
            }
        }
    }

    public static SimpleListBuilder builder() {
        return new SimpleListBuilder();
    }

    @VisibleForTesting
    public void setResolvedStyle(ListStyleProperties styleProperties) {
        this.resolvedStyle = styleProperties;
    }
}