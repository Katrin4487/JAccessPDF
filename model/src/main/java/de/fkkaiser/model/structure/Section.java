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
import de.fkkaiser.model.style.SectionStyleProperties;
import de.fkkaiser.model.style.StyleResolverContext;
import de.fkkaiser.model.style.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a structural section element, which acts as a container
 * for other block-level elements like paragraphs, lists, or other sections.
 */
@JsonTypeName(ElementTypes.SECTION)
public final class Section implements Element {

    private final String styleClass;
    private final String variant;
    private final List<Element> elements;

    @JsonIgnore
    private SectionStyleProperties resolvedStyle;

    @JsonCreator
    public Section(
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("variant") String variant,
            @JsonProperty("elements") List<Element> elements
    ) {
        this.styleClass = styleClass;
        this.variant = variant;
        this.elements = Objects.requireNonNullElse(elements, List.of());
    }

    // --- Getters ---
    @Override
    public String getStyleClass() { return styleClass; }
    public String getVariant() { return variant; }
    public List<Element> getElements() { return elements; }
    public SectionStyleProperties getResolvedStyle() { return resolvedStyle; }
    public void setResolvedStyle(SectionStyleProperties resolvedStyle) { this.resolvedStyle = resolvedStyle; }

    @Override
    public String getType() {
        return ElementTypes.SECTION;
    }

    @Override
    public void resolveStyles(StyleResolverContext context) {
        ElementBlockStyleProperties parentStyle = context.parentBlockStyle();

        SectionStyleProperties specificStyle = Optional.ofNullable(context.styleMap().get(this.getStyleClass()))
                .map(ElementStyle::properties)
                .filter(SectionStyleProperties.class::isInstance)
                .map(SectionStyleProperties.class::cast)
                .orElse(new SectionStyleProperties());

        SectionStyleProperties finalStyle = specificStyle.copy();
        finalStyle.mergeWith(parentStyle);
        this.setResolvedStyle(finalStyle);

        StyleResolverContext childContext = context.createChildContext(this.getResolvedStyle());
        elements.forEach(element -> element.resolveStyles(childContext));
    }
}
