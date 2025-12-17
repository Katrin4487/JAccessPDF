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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.fkkaiser.model.JsonPropertyName;
import de.fkkaiser.model.style.ElementBlockStyleProperties;
import de.fkkaiser.model.style.ElementStyle;
import de.fkkaiser.model.style.StyleResolverContext;
import de.fkkaiser.model.style.TextBlockStyleProperties;
import de.fkkaiser.model.style.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Abstract class representing a block of text elements.
 * Inherits from Element interface. Implements methods to resolve styles and manage inline elements.
 *
 * @author Katrin Kaiser
 * @version 1.0.1
 */
public abstract class TextBlock extends AbstractElement {

    private static final Logger log = LoggerFactory.getLogger(TextBlock.class);

    @JsonProperty(JsonPropertyName.INLINE_ELEMENTS)
    protected final List<InlineElement> inlineElements;

    @JsonIgnore
    protected TextBlockStyleProperties resolvedStyle;

    // --- Constructor Injection ---

    /**
     * Constructs a TextBlock with the provided inline elements, style class, and variant.
     *
     * @param styleClass     The CSS style class to apply to the text block
     * @param inlineElements A list of InlineElement objects to be included in the text block
     * @throws IllegalArgumentException if styleClass is empty
     * @throws NullPointerException     if styleClass is null
     */
    public TextBlock(String styleClass, List<InlineElement> inlineElements) {
        if (styleClass != null && styleClass.isEmpty()) {
            log.error("styleClass must not be  empty");
            throw new IllegalArgumentException("Style class must not be empty");
        }
        this.styleClass = styleClass;
        this.inlineElements = (inlineElements != null) ? inlineElements : List.of();
    }


    /**
     * Constructs a TextBlock with the provided style class.
     *
     * @param styleClass The CSS style class to apply to the text block
     */
    public TextBlock(String styleClass) {
        this(styleClass, null);
    }

    /**
     * Returns the resolved style properties for this text block.
     *
     * @return The resolved TextBlockStyleProperties
     */
    public TextBlockStyleProperties getResolvedStyle() {
        return resolvedStyle;
    }

    /**
     * Returns the list of inline elements contained in this text block.
     *
     * @return A list of InlineElement objects
     */
    public List<InlineElement> getInlineElements() {
        return inlineElements;
    }


    /**
     * Resolves and applies styles for this text block using the provided context.
     *
     * @param context The StyleResolverContext containing style information
     */
    @Override
    public void resolveStyles(StyleResolverContext context) {
        ElementBlockStyleProperties baseStyle = context.parentBlockStyle();

        TextBlockStyleProperties finalStyle = null;

        if (this.styleClass != null) {
            ElementStyle specificElementStyle = context.styleMap().get(this.styleClass);
            if (specificElementStyle != null &&
                    specificElementStyle.properties() instanceof TextBlockStyleProperties specificStyle) {
                finalStyle = specificStyle.copy();
            } else {
                log.warn("Style '{}' not found in stylesheet", this.styleClass);
            }
        }

        // If no style found, try to find default based on element type
        if (finalStyle == null) {
            StandardElementType docElement = getStandardElementType();
            if (docElement != null && context.styleSheet() != null) {
                var defaultStyle = context.styleSheet().findElementStyle(docElement, null);

                if (defaultStyle.isPresent() &&
                        defaultStyle.get().properties() instanceof TextBlockStyleProperties defaultTextStyle) {
                    finalStyle = defaultTextStyle.copy();
                    log.debug("Using default style '{}' for {}",
                            defaultStyle.get().name(), docElement.getJsonKey());
                }
            }
        }

        // Final fallback: empty style
        if (finalStyle == null) {
            finalStyle = new TextBlockStyleProperties();
            log.debug("No style found, using empty default");
        }

        // Now merge with parent (if parent is a TextBlockStyleProperties)
        if (baseStyle instanceof TextBlockStyleProperties parentTextStyle) {

            finalStyle.mergeWith(parentTextStyle);
        }

        this.resolvedStyle = finalStyle;

        // Delegate to inline elements with the newly resolved context
        StyleResolverContext childContext = context.createChildContext(this.resolvedStyle);
        for (InlineElement inlineElement : this.inlineElements) {
            inlineElement.resolveStyles(childContext);
        }
    }

}