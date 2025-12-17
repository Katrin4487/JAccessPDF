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
import de.fkkaiser.model.style.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * @version 1.2.1
 */
@JsonTypeName(JsonPropertyName.PART)
public final class Part implements Element {

    private static final Logger log = LoggerFactory.getLogger(Part.class);

    private final String styleClass;
    private final List<Element> elements;
    private final PartVariant variant;

    @JsonIgnore
    private PartStyleProperties resolvedStyle;

    /**
     * Creates a new Part element.
     *
     * <p>This constructor is used by Jackson during JSON deserialization.
     * All parameters are optional in the JSON structure.</p>
     *
     * @param styleClass the CSS-like style class for styling properties; may be {@code null}
     * @param elements   the list of child elements contained within the part; may be {@code null}
     * @param variant    the variant of the part (e.g., PART, VOLUME); may be {@code null}
     */
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
     *
     * @param styleClass the style class identifier
     * @param elements   the list of child elements contained within the part
     */
    @PublicAPI
    public Part(String styleClass, List<? extends Element> elements) {

        this(styleClass, elements == null ? List.of() : new ArrayList<>(elements), PartVariant.PART);
    }

    /**
     * Creates a Part with the specified style class and elements.
     *
     * @param styleClass the style class identifier
     * @param elements   the list of child elements contained within the part
     */
    @PublicAPI
    public Part(String styleClass, PartVariant variant, List<? extends Element> elements) {
        this(styleClass, elements == null ? List.of() : new ArrayList<>(elements), variant);
    }

    // --- Getters ---

    /**
     * Returns the style class name used to look up styling properties.
     *
     * @return the style class name; may be {@code null}
     */
    @Internal
    @Override
    public String getStyleClass() {
        return styleClass;
    }

    /**
     * Returns the list of child elements contained within the part.
     *
     * @return the list of child elements; never {@code null}
     */
    @Internal
    public List<Element> getElements() {
        return elements;
    }

    /**
     * Returns the resolved style properties for the part element.
     *
     * @return resolved style properties
     */
    @Internal
    public PartStyleProperties getResolvedStyle() {
        return resolvedStyle;
    }

    /**
     * Returns the variant of the part.
     *
     * @return the part variant
     */
    @Internal
    public PartVariant getVariant() {
        return variant;
    }

    /**
     * Returns the type identifier for this element.
     *
     * @return ElementTargetType.PART
     */
    @Override
    public ElementTargetType getType() {
        return ElementTargetType.PART;
    }

    /**
     * Resolves styles for the part element and its child elements.
     *
     * <p>This method looks up the specific style properties for this part
     * based on its style class and merges them with the parent block style
     * from the provided context. It then sets the resolved style for this part
     * and recursively resolves styles for all child elements.</p>
     *
     * @param context the style resolver context containing parent styles and style mappings
     */
    @Override
    public void resolveStyles(StyleResolverContext context) {
        ElementBlockStyleProperties parentStyle = context.parentBlockStyle();

        PartStyleProperties specificStyle = Optional.ofNullable(context.styleMap().get(this.getStyleClass()))
                .map(ElementStyle::properties)
                .filter(PartStyleProperties.class::isInstance)
                .map(PartStyleProperties.class::cast)
                .orElse(null);
        if (specificStyle == null) {
            StandardElementType docElement = getStandardElementType();
            if (docElement != null) {
                var defaultStyle = context.styleSheet().findElementStyle(docElement, null);

                if (defaultStyle.isPresent() &&
                        defaultStyle.get().properties() instanceof PartStyleProperties defaultPartStyle) {
                    specificStyle = defaultPartStyle.copy();
                    log.debug("Using default style '{}' for {}",
                            defaultStyle.get().name(), docElement.getJsonKey());
                }
            }
        }
        PartStyleProperties finalStyle = specificStyle!=null ? specificStyle.copy(): new PartStyleProperties();
        finalStyle.mergeWith(parentStyle);

        this.resolvedStyle = finalStyle;

        StyleResolverContext childContext = context.createChildContext(this.getResolvedStyle());
        elements.forEach(element -> element.resolveStyles(childContext));
    }

    /**
     * Returns the standard element type for this part.
     *
     * @return StandardElementType.PART
     */
    @Internal
    @Override
    public StandardElementType getStandardElementType() {
        return StandardElementType.PART;
    }
}