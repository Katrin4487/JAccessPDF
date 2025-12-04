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
import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.style.ElementBlockStyleProperties;
import de.fkkaiser.model.style.ElementStyle;
import de.fkkaiser.model.style.PartStyleProperties;
import de.fkkaiser.model.style.StyleResolverContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a part element in the document structure.
 * A part typically represents a major division in a document,
 * such as a book part or volume.
 *
 * @author Katrin Kaiser
 * @version 1.2.0
 */
@JsonTypeName(JsonPropertyName.PART)
public final class Part implements Element {

    private final String styleClass;
    private final List<Element> elements;
    private final PartVariant variant;

    @JsonIgnore
    private PartStyleProperties resolvedStyle;

    @JsonCreator
    public Part(
            @JsonProperty(JsonPropertyName.STYLE_CLASS) String styleClass,
            @JsonProperty(JsonPropertyName.ELEMENTS) List<Element> elements,
            @JsonProperty(JsonPropertyName.VARIANT) PartVariant variant
    ) {

        this.styleClass = styleClass;
        this.elements = Objects.requireNonNullElse(elements, List.of());
        this.variant = variant != null ? variant : PartVariant.PART;
    }

    /**
     * Creates a Part with the specified style class and elements.
     * @param styleClass the style class identifier
     * @param elements the list of child elements contained within the part
     */
    @PublicAPI
    public Part(String styleClass, List<? extends Element> elements) {

        this(styleClass, elements==null ? List.of() : new ArrayList<>(elements), PartVariant.PART);
    }

    /**
     * Creates a Part with the specified style class and elements.
     * @param styleClass the style class identifier
     * @param elements the list of child elements contained within the part
     */
    @PublicAPI
    public Part(String styleClass, PartVariant variant,List<? extends Element> elements) {
        this(styleClass, elements==null ? List.of() : new ArrayList<>(elements), variant);
    }

    // --- Getters ---
    @Override
    public String getStyleClass() { return styleClass; }
    public List<Element> getElements() { return elements; }
    public PartStyleProperties getResolvedStyle() { return resolvedStyle; }
    public PartVariant getVariant() {return variant;}

    @Override
    public ElementTargetType getType() {
        return ElementTargetType.PART;
    }

    @Override
    public void resolveStyles(StyleResolverContext context) {
        ElementBlockStyleProperties parentStyle = context.parentBlockStyle();

        PartStyleProperties specificStyle = Optional.ofNullable(context.styleMap().get(this.getStyleClass()))
                .map(ElementStyle::properties)
                .filter(PartStyleProperties.class::isInstance)
                .map(PartStyleProperties.class::cast)
                .orElse(new PartStyleProperties());

        PartStyleProperties finalStyle = specificStyle.copy();
        finalStyle.mergeWith(parentStyle);

        this.resolvedStyle = finalStyle;

        StyleResolverContext childContext = context.createChildContext(this.getResolvedStyle());
        elements.forEach(element -> element.resolveStyles(childContext));
    }
}