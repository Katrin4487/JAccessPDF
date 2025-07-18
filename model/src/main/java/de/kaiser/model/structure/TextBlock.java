package de.kaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.kaiser.model.style.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Abstract class representing a block of text elements.
 * Inherits from Element interface. Implements methods to resolve styles and manage inline elements.
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
     * @param variant        The variant of the text block
     */
    public TextBlock(String styleClass,List<InlineElement> inlineElements, String variant) {
        if(styleClass == null || styleClass.isEmpty()) {
            log.error("styleClass is null or empty");
            throw new IllegalArgumentException("Style class cannot be null or empty");
        }
        this.styleClass = styleClass;
        this.inlineElements = (inlineElements != null) ? inlineElements : List.of();
        this.variant = variant;
    }

    /**
     * Constructs a TextBlock with the provided style class and variant.
     *
     * @param styleClass The CSS style class to apply to the text block
     * @param variant    The variant of the text block
     */
    public TextBlock(String styleClass, String variant) {
        this(styleClass,null,variant);
    }

    public TextBlock(String styleClass, List<InlineElement> inlineElements) {
        this(styleClass,inlineElements,null);
    }

    /**
     * Constructs a TextBlock with the provided style class.
     *
     * @param styleClass The CSS style class to apply to the text block
     */
    public TextBlock(String styleClass) {
        this(styleClass,null,null);
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
        if (baseStyle == null) {
            baseStyle = new ParagraphStyleProperties();
        }

        TextBlockStyleProperties finalStyle = new TextBlockStyleProperties();
        // Start with a copy of the base style.
        if(baseStyle instanceof TextBlockStyleProperties) {
            finalStyle = (TextBlockStyleProperties) baseStyle.copy();
        }

            // If a specific style class is defined, find it and merge it.
            if (this.styleClass != null) {
                ElementStyle specificElementStyle = context.styleMap().get(this.styleClass);
                if (specificElementStyle != null && specificElementStyle.properties() instanceof TextBlockStyleProperties specificStyle) {
                    // The specific style's properties are merged into our final style.
                    finalStyle.mergeWith(specificStyle);
                }
            }

            this.resolvedStyle = finalStyle;


        // Delegate to inline elements with the newly resolved context.
        StyleResolverContext childContext = context.createChildContext(this.resolvedStyle);
        for (InlineElement inlineElement : this.inlineElements) {
            inlineElement.resolveStyles(childContext);
        }
    }
}