package de.kaiser.model.style;

import java.util.Map;

public class StyleResolverContext {

    private final Map<String, ElementStyle> styleMap;
    private final TextBlockStyleProperties defaultBlockStyle;
    private final TextBlockStyleProperties parentBlockStyle;

    // --- Constructor ---

    public StyleResolverContext(Map<String, ElementStyle> styleMap, TextBlockStyleProperties defaultBlockStyle) {
        this(styleMap, defaultBlockStyle, defaultBlockStyle); // parent style is default
    }

    private StyleResolverContext(Map<String, ElementStyle> styleMap, TextBlockStyleProperties defaultBlockStyle, TextBlockStyleProperties parentBlockStyle) {
        this.styleMap = styleMap;
        this.defaultBlockStyle = defaultBlockStyle;
        this.parentBlockStyle = parentBlockStyle;
    }

    /**
     * Creates a new context for the child element. The parent style will set on it
     */
    public StyleResolverContext createChildContext(TextBlockStyleProperties newParentBlockStyle) {
        return new StyleResolverContext(this.styleMap, this.defaultBlockStyle, newParentBlockStyle);
    }

    // --- Getter ---

    public Map<String, ElementStyle> getStyleMap() {
        return styleMap;
    }

    public TextBlockStyleProperties getParentBlockStyle() {
        return parentBlockStyle;
    }
}