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
import de.fkkaiser.model.style.ElementBlockStyleProperties;
import de.fkkaiser.model.style.ElementStyle;
import de.fkkaiser.model.style.StyleResolverContext;
import de.fkkaiser.model.style.TextBlockStyleProperties;
import de.fkkaiser.model.style.ElementBlockStyleProperties;
import de.fkkaiser.model.style.ElementStyle;
import de.fkkaiser.model.style.StyleResolverContext;
import de.fkkaiser.model.style.TextBlockStyleProperties;

import java.util.List;

/**
 * Represents a list item, which can function as a standard list item
 * or as an entry in a definition list with a label and a body.
 */
@JsonTypeName("list-item")
public final class ListItem implements Element {

    private final String styleClass;
    private final String variant;

    // The label of the list item (e.g., a term in a definition list).
    // Can contain rich inline content.
    private final List<InlineElement> label;

    // The body of the list item (e.g., the definition of the term).
    // Can contain block-level elements, including nested lists.
    private final List<Element> elements;

    @JsonIgnore
    private ElementBlockStyleProperties resolvedStyle;

    @JsonCreator
    public ListItem(
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("variant") String variant,
            @JsonProperty("label") List<InlineElement> label, // NEU
            @JsonProperty("elements") List<Element> elements
    ) {
        this.styleClass = styleClass;
        this.variant = variant;
        this.label = label;
        this.elements = elements;
    }

    // --- Getters ---
    @Override
    public String getStyleClass() { return styleClass; }
    public String getVariant() { return variant; }
    public List<InlineElement> getLabel() { return label; } // NEU
    public List<Element> getElements() { return elements; }
    public ElementBlockStyleProperties getResolvedStyle() { return resolvedStyle; }
    public void setResolvedStyle(ElementBlockStyleProperties resolvedStyle) { this.resolvedStyle = resolvedStyle; }

    @Override
    public String getType() {
        return "list-item";
    }

    @Override
    public void resolveStyles(StyleResolverContext context) {
        // Resolve the style for the list item container itself
        ElementStyle specificElementStyle = context.styleMap().get(this.getStyleClass());
        if (specificElementStyle != null && specificElementStyle.properties() instanceof TextBlockStyleProperties specificStyle) {
            TextBlockStyleProperties newResolvedStyle = specificStyle.copy();
            newResolvedStyle.mergeWith(context.parentBlockStyle());
            this.setResolvedStyle(newResolvedStyle);
        } else {
            this.setResolvedStyle(context.parentBlockStyle() != null ? context.parentBlockStyle().copy() : null);
        }


        StyleResolverContext childContext = context.createChildContext(this.getResolvedStyle());

        if (label != null) {
            label.forEach(el -> el.resolveStyles(childContext));
        }
        if (elements != null) {
            elements.forEach(el -> el.resolveStyles(childContext));
        }
    }
}
