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
import de.fkkaiser.model.style.ElementBlockStyleProperties;
import de.fkkaiser.model.style.ElementStyle;
import de.fkkaiser.model.style.LayoutTableStyleProperties;
import de.fkkaiser.model.style.StyleResolverContext;
import de.fkkaiser.model.style.*;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class LayoutTable implements Element{

    private final String styleClass;
    private final Element elementLeft;
    private final Element elementRight;

    private LayoutTableStyleProperties resolvedStyle;


    public LayoutTable(
            @JsonProperty(JsonPropertyName.STYLE_CLASS) String styleClass,
            @JsonProperty(JsonPropertyName.ELEMENT_LEFT) Element elementLeft,
            @JsonProperty(JsonPropertyName.ELEMENT_RIGHT) Element elementRight) {

        this.styleClass = styleClass;
        this.elementLeft = elementLeft;
        this.elementRight = elementRight;

    }

    public Element getElementLeft() {
        return elementLeft;
    }

    public Element getElementRight() {
        return elementRight;
    }

    public LayoutTableStyleProperties getResolvedStyle() {
        return resolvedStyle;
    }

    public void setResolvedStyle(LayoutTableStyleProperties resolvedStyle) {
        this.resolvedStyle = resolvedStyle;
    }

    @Override
    public ElementTargetType getType() {
        return ElementTargetType.LAYOUT_TABLE;
    }

    @Override
    public String getStyleClass() {
        return styleClass;
    }

    /**
     * Resolves styles for the given element using the provided style resolver context.
     *
     * @param context The style resolver context containing style map and default text style properties
     */
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
        if(finalStyle==null){
            finalStyle = new LayoutTableStyleProperties();
        }
        finalStyle.mergeWith(parentStyle);
        this.setResolvedStyle(finalStyle);

        StyleResolverContext childContext = context.createChildContext(this.getResolvedStyle());
        Stream.of(elementLeft,elementRight)
                .filter(Objects::nonNull)
                .forEach(elem -> elem.resolveStyles(childContext));
    }
}
