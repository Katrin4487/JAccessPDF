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
import de.fkkaiser.model.style.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Represents a list item, which can function as a standard list item
 * or as an entry in a definition list with a label and a body.
 *
 * @author Katrin Kaiser
 * @version 1.1.2
 */
@JsonTypeName(JsonPropertyName.LIST_ITEM)
public final class ListItem implements Element {

    private static final Logger log = LoggerFactory.getLogger(ListItem.class);

    private final String styleClass;

    // The label of the list item (e.g., a term in a definition list).
    // Can contain rich inline content.
    private final List<InlineElement> label;

    // The body of the list item (e.g., the definition of the term).
    // Can contain block-level elements, including nested lists.
    private final List<Element> elements;

    @JsonIgnore
    private ElementBlockStyleProperties resolvedStyle;

    /**
     * Constructs a ListItem with the specified style class, label, and elements.
     *
     * @param styleClass style class
     * @param label      label of the list item
     * @param elements   body elements of the list item
     */
    @JsonCreator
    public ListItem(
            @JsonProperty(JsonPropertyName.STYLE_CLASS) String styleClass,
            @JsonProperty(JsonPropertyName.LABEL) List<InlineElement> label,
            @JsonProperty(JsonPropertyName.ELEMENTS) List<Element> elements
    ) {
        this.styleClass = styleClass;
        this.label = label;
        this.elements = elements;
    }

    // --- Getters ---

    /**
     * Returns the style class of the list item.
     *
     * @return style class name of the style class
     */
    @Internal
    @Override
    public String getStyleClass() {
        return styleClass;
    }

    /**
     * Returns the label of the list item.
     *
     * @return label of the list item
     */
    @Internal
    public List<InlineElement> getLabel() {
        return label;
    }

    /**
     * Returns the body elements of the list item.
     *
     * @return body elements of the list item
     */
    @Internal
    public List<Element> getElements() {
        return elements;
    }

    /**
     * Returns the resolved style properties for the list item.
     *
     * @return resolved style properties
     */
    @Internal
    public ElementBlockStyleProperties getResolvedStyle() {
        return resolvedStyle;
    }

    /**
     * Sets the resolved style properties for the list item.
     *
     * @param resolvedStyle resolved style properties
     */
    @Internal
    public void setResolvedStyle(ElementBlockStyleProperties resolvedStyle) {
        this.resolvedStyle = resolvedStyle;
    }

    /**
     * Returns the type of the element.
     *
     * @return ElementTargetType representing a list item
     */
    @Internal
    @Override
    public ElementTargetType getType() {
        return ElementTargetType.LIST_ITEM;
    }

    /**
     * Resolves the styles for the list item and its child elements
     * based on the provided style resolver context.
     *
     * @param context the style resolver context
     */
    @Internal
    @Override
    public void resolveStyles(StyleResolverContext context) {
        ListItemStyleProperties finalStyle = null;

        System.out.println("LIS "+this.styleClass);

        // 1. Try to find style by explicit styleClass
        if (this.styleClass != null) {
            ElementStyle specificElementStyle = context.styleMap().get(this.styleClass);
            if (specificElementStyle != null &&
                    specificElementStyle.properties() instanceof ListItemStyleProperties specificStyle) {
                finalStyle = specificStyle.copy();
                log.debug("Using explicit style '{}' for list item", this.styleClass);
            } else {
                log.warn("Style '{}' not found in stylesheet", this.styleClass);
            }
        }

        // 2. If no style found, try to find default based on element type
        if (finalStyle == null) {
            StandardElementType docElement = getStandardElementType();
            if (docElement != null) {
                var defaultStyle = context.styleSheet().findElementStyle(docElement, null);

                if (defaultStyle.isPresent() &&
                        defaultStyle.get().properties() instanceof ListItemStyleProperties defaultListItemStyle) {
                    finalStyle = defaultListItemStyle.copy();
                    log.debug("Using default style '{}' for {}",
                            defaultStyle.get().name(), docElement.getJsonKey());
                }
            }
        }

        // 3. Final fallback: empty style
        if (finalStyle == null) {
            finalStyle = new ListItemStyleProperties();
            log.debug("No style found for list item, using empty default");
        }

        // 4. Merge with parent
        finalStyle.mergeWith(context.parentBlockStyle());
        this.resolvedStyle = finalStyle;

        // 5. Delegate to child elements
        StyleResolverContext childContext = context.createChildContext(this.resolvedStyle);

        if (label != null) {
            label.forEach(el -> el.resolveStyles(childContext));
        }
        if (elements != null) {
            elements.forEach(el -> el.resolveStyles(childContext));
        }
    }

    /**
     * Returns the standard element type for this list item.
     *
     * @return StandardElementType.LI
     */
    @Override
    public StandardElementType getStandardElementType() {
        return StandardElementType.LI;
    }


}

