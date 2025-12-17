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

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fkkaiser.model.JsonPropertyName;
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.style.ElementBlockStyleProperties;
import de.fkkaiser.model.style.ElementStyle;
import de.fkkaiser.model.style.LayoutTableStyleProperties;
import de.fkkaiser.model.style.StyleResolverContext;
import de.fkkaiser.model.style.*;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Represents a layout table element in a document structure.
 * A layout table contains two elements: one on the left and one on the right.
 * It is used for arranging content side by side (structural layout).
 *
 * @author Katrin Kaiser
 * @version 1.0.1
 */
public class LayoutTable implements Element {

    private final String styleClass;
    private final Element elementLeft;
    private final Element elementRight;

    private LayoutTableStyleProperties resolvedStyle;


    /**
     * Constructor for creating a LayoutTable object.
     *
     * @param styleClass   The style class of the layout table.
     * @param elementLeft  The element to be placed on the left side.
     * @param elementRight The element to be placed on the right side.
     */
    public LayoutTable(
            @JsonProperty(JsonPropertyName.STYLE_CLASS) String styleClass,
            @JsonProperty(JsonPropertyName.ELEMENT_LEFT) Element elementLeft,
            @JsonProperty(JsonPropertyName.ELEMENT_RIGHT) Element elementRight) {

        this.styleClass = styleClass;
        this.elementLeft = elementLeft;
        this.elementRight = elementRight;

    }

    /**
     * Gets the left element of the layout table.
     *
     * @return The left element.
     */
    @Internal
    public Element getElementLeft() {
        return elementLeft;
    }

    /**
     * Gets the right element of the layout table.
     *
     * @return The right element.
     */
    @Internal
    public Element getElementRight() {
        return elementRight;
    }

    /**
     * Gets the resolved style properties for the layout table.
     *
     * @return The resolved layout table style properties.
     */
    @Internal
    public LayoutTableStyleProperties getResolvedStyle() {
        return resolvedStyle;
    }

    /**
     * Sets the resolved style properties for the layout table.
     *
     * @param resolvedStyle The resolved layout table style properties to set.
     */
    @Internal
    public void setResolvedStyle(LayoutTableStyleProperties resolvedStyle) {
        this.resolvedStyle = resolvedStyle;
    }

    /**
     * Gets the type of the element.
     *
     * @return The element target type.
     */
    @Internal
    @Override
    public ElementTargetType getType() {
        return ElementTargetType.LAYOUT_TABLE;
    }

    /**
     * Gets the style class of the layout table.
     *
     * @return The style class.
     */
    @Internal
    @Override
    public String getStyleClass() {
        return styleClass;
    }

    /**
     * Resolves styles for the given element using the provided style resolver context.
     *
     * @param context The style resolver context containing style map and default text style properties
     */
    @Internal
    @Override
    public void resolveStyles(StyleResolverContext context) {

        ElementBlockStyleProperties parentStyle = context.parentBlockStyle();
        ElementStyle specificElementStyle = context.styleMap().get(this.getStyleClass());

        LayoutTableStyleProperties specificStyle = Optional.ofNullable(specificElementStyle)
                .map(ElementStyle::properties)
                .filter(LayoutTableStyleProperties.class::isInstance)
                .map(LayoutTableStyleProperties.class::cast)
                .orElse(new LayoutTableStyleProperties()); // Standard-Style, wenn nichts gefunden wurde

        LayoutTableStyleProperties finalStyle = (LayoutTableStyleProperties) specificStyle.copy();
        if (finalStyle == null) {
            finalStyle = new LayoutTableStyleProperties();
        }
        finalStyle.mergeWith(parentStyle);
        this.setResolvedStyle(finalStyle);

        StyleResolverContext childContext = context.createChildContext(this.getResolvedStyle());
        Stream.of(elementLeft, elementRight)
                .filter(Objects::nonNull)
                .forEach(elem -> elem.resolveStyles(childContext));
    }

    /**
     * Gets the standard element type for the layout table.
     *
     * @return {@null}, because there is no default.
     */
    @Override
    public StandardElementType getStandardElementType() {
        return null;
    }
}
