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
import de.fkkaiser.model.style.SectionStyleProperties;
import de.fkkaiser.model.style.StyleResolverContext;

import java.util.*;

/**
 * Represents a section element in the document structure.
 * A section is a block-level container that can hold other elements
 * and may have a semantic variant (e.g., introduction, conclusion).
 *
 * @author Katrin Kaiser
 * @version 1.2.0
 */
@JsonTypeName(JsonPropertyName.SECTION)
public final class Section implements Element {

    private final String styleClass;
    private final SectionVariant variant;
    private final String altText;
    private final List<Element> elements;

    @JsonIgnore
    private SectionStyleProperties resolvedStyle;

    /**
     * Creates a new Section with the specified properties.
     *
     * @param styleClass the base style class name (required for styled sections)
     * @param variant optional semantic variant (null defaults to {@link SectionVariant#SECTION})
     * @param altText optional alternative text for accessibility (used in PDF/UA fox:alt-text attribute)
     * @param elements list of child elements contained in this section (defaults to empty list if null)
     */
    @JsonCreator
    public Section(
            @JsonProperty(JsonPropertyName.STYLE_CLASS) String styleClass,
            @JsonProperty(JsonPropertyName.VARIANT) SectionVariant variant,
            @JsonProperty(JsonPropertyName.ALT_TEXT) String altText,
            @JsonProperty(JsonPropertyName.ELEMENTS) List<Element> elements
    ) {
        this.styleClass = styleClass;
        this.variant = variant;
        this.altText = altText;
        this.elements = Objects.requireNonNullElse(elements, List.of());
    }

    /**
     * Convenience constructor without alt-text.
     *
     * @param styleClass the base style class name
     * @param variant optional semantic variant
     * @param elements list of child elements
     */
    public Section(String styleClass,SectionVariant variant, List<? extends Element> elements) {
        this(styleClass, variant, null, elements != null ? new ArrayList<>(elements) : List.of());
    }


    // --- Getters ---

    @Override
    public String getStyleClass() {
        return styleClass;
    }

    /**
     * Returns the semantic variant of this section.
     *
     * @return the section variant, or null if not specified (defaults to SECTION)
     */
    public SectionVariant getVariant() {
        return variant;
    }

    /**
     * Returns the alternative text for accessibility.
     * This text is used in the PDF/UA structure tree to provide additional
     * context for screen readers.
     *
     * @return the alternative text, or null if not specified
     */
    public String getAltText() {
        return altText;
    }

    /**
     * Returns the list of child elements in this section.
     *
     * @return non-null list of elements (may be empty)
     */
    public List<Element> getElements() {
        return elements;
    }

    /**
     * Returns the resolved style properties for this section.
     * This is set during style resolution and is null before that.
     *
     * @return the resolved style properties, or null if not yet resolved
     */
    public SectionStyleProperties getResolvedStyle() {
        return resolvedStyle;
    }

    /**
     * Sets the resolved style properties for this section.
     * This is called internally during style resolution.
     *
     * @param resolvedStyle the resolved style properties
     */
    public void setResolvedStyle(SectionStyleProperties resolvedStyle) {
        this.resolvedStyle = resolvedStyle;
    }

    @Override
    public ElementTargetType getType() {
        return ElementTargetType.SECTION;
    }

    @Override
    public void resolveStyles(StyleResolverContext context) {
        ElementBlockStyleProperties parentStyle = context.parentBlockStyle();

        // Determine effective style key based on variant
        String effectiveStyleKey = this.styleClass;
        if (variant != null) {
            effectiveStyleKey = this.styleClass + "." + variant.getStyleName();
        }

        // Get style from styleMap
        SectionStyleProperties specificStyle = Optional.ofNullable(context.styleMap().get(effectiveStyleKey))
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