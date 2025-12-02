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
import de.fkkaiser.model.style.ElementBlockStyleProperties;
import de.fkkaiser.model.style.ElementStyle;
import de.fkkaiser.model.style.StyleResolverContext;
import de.fkkaiser.model.style.TextBlockStyleProperties;
import de.fkkaiser.model.style.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * Abstract class representing a block of text elements.
 * Inherits from Element interface. Implements methods to resolve styles and manage inline elements.
 *
 * @author Katrin Kaiser
 * @version 1.0.1
 */
public abstract class TextBlock extends AbstractElement {

    private static final Logger log = LoggerFactory.getLogger(TextBlock.class);

    @JsonProperty("inline-elements")
    protected final List<InlineElement> inlineElements;

    @JsonIgnore
    protected TextBlockStyleProperties resolvedStyle;

    // --- Constructor Injection ---

    /**
     * Constructs a TextBlock with the provided inline elements, style class, and variant.
     *
     * @param styleClass     The CSS style class to apply to the text block
     * @param inlineElements A list of InlineElement objects to be included in the text block
     *
     * @throws IllegalArgumentException if styleClass is empty
     * @throws NullPointerException     if styleClass is null
     */
    public TextBlock(String styleClass,List<InlineElement> inlineElements) {
        Objects.requireNonNull(styleClass, "styleClass must not be null");
        if(styleClass.isEmpty()) {
            log.error("styleClass is null or empty");
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
        this(styleClass,null);
    }

    // ... Getters for final fields ...
    public TextBlockStyleProperties getResolvedStyle() {
        return resolvedStyle;
    }
    public List<InlineElement> getInlineElements() {
        return inlineElements;
    }


   @Override
    public void resolveStyles(StyleResolverContext context) {
        ElementBlockStyleProperties baseStyle = context.parentBlockStyle();

        TextBlockStyleProperties finalStyle = null;

        if (this.styleClass != null) {
            ElementStyle specificElementStyle = context.styleMap().get(this.styleClass);
            if (specificElementStyle != null &&
                    specificElementStyle.properties() instanceof TextBlockStyleProperties specificStyle) {
                finalStyle = (TextBlockStyleProperties) specificStyle.copy();
            }
        }

        // If no specific style found, create a default
        if (finalStyle == null) {
            finalStyle = new TextBlockStyleProperties();
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