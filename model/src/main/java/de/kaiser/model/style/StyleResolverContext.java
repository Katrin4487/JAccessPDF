package de.kaiser.model.style;

import java.util.Map;

/**
 * A context object that holds the state during the recursive style resolution process.
 * It is immutable; a new context is created for each level of the element tree.
 *
 * @param parentBlockStyle Holds the style of the direct parent element. Can be null for top-level elements.
 */
public record StyleResolverContext(Map<String, ElementStyle> styleMap, TextBlockStyleProperties parentBlockStyle) {

    /**
     * The main constructor for creating a style context.
     *
     * @param styleMap         The map of all available named styles.
     * @param parentBlockStyle The resolved style of the parent element.
     */
    public StyleResolverContext {
    }

    /**
     * Creates a new context for child elements.
     * This new context carries over the global style map but sets a new parent style.
     *
     * @param newParentBlockStyle The resolved style of the new parent element.
     * @return A new StyleResolverContext instance.
     */
    public StyleResolverContext createChildContext(TextBlockStyleProperties newParentBlockStyle) {
        return new StyleResolverContext(this.styleMap, newParentBlockStyle);
    }
}
