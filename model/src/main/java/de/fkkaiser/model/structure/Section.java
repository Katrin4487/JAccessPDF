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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a structural section element that acts as a container
 * for other block-level elements.
 *
 * <p>Sections are useful for:</p>
 * <ul>
 *   <li>Grouping related content</li>
 *   <li>Applying consistent styling (background, borders, padding) to multiple elements</li>
 *   <li>Creating semantic sections (warnings, notes, examples)</li>
 *   <li>Improving document accessibility through proper PDF/UA structure</li>
 * </ul>
 *
 * <p><b>Section Variants:</b></p>
 * Sections support three semantic variants via {@link SectionVariant}:
 * <ul>
 *   <li>{@link SectionVariant#SECTION SECTION} - Standard section (role="Sect")</li>
 *   <li>{@link SectionVariant#NOTE NOTE} - Important notices, warnings (role="Note")</li>
 *   <li>{@link SectionVariant#ASIDE ASIDE} - Examples, supplementary content (role="Aside")</li>
 * </ul>
 *
 * <p><b>Style Resolution:</b></p>
 * When a variant is specified, the style key is constructed as {@code styleClass.variant}.
 * For example:
 * <pre>
 * styleClass="notice-box", variant=NOTE → looks up "notice-box.note"
 * </pre>
 *
 * <p><b>Example Usage:</b></p>
 * <pre>{@code
 * // Warning section
 * Section warning = new Section(
 *     "notice-box",
 *     SectionVariant.NOTE,
 *     "This section contains important safety information",
 *     List.of(
 *         new Headline("h3", "⚠️ Warning", 3),
 *         new Paragraph("body-text", "Please read carefully!")
 *     )
 * );
 *
 * // Example section
 * Section example = new Section(
 *     "example-box",
 *     SectionVariant.ASIDE,
 *     "Code example demonstrating the concept",
 *     List.of(
 *         new Headline("h4", "Example", 4),
 *         new Paragraph("code", "int x = 42;")
 *     )
 * );
 *
 * // Standard section (no variant)
 * Section standard = new Section(
 *     "content-section",
 *     null,
 *     null,
 *     List.of(
 *         new Paragraph("body", "Regular content...")
 *     )
 * );
 * }</pre>
 *
 * @see SectionVariant
 * @see SectionStyleProperties
 * @see ElementBlockStyleProperties
 */
@JsonTypeName(ElementTypes.SECTION)
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
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("variant") SectionVariant variant,
            @JsonProperty("alt-text") String altText,
            @JsonProperty("elements") List<Element> elements
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
    public Section(String styleClass, SectionVariant variant, List<Element> elements) {
        this(styleClass, variant, null, elements);
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
    public String getType() {
        return ElementTypes.SECTION;
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