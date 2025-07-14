package de.kaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.kaiser.model.style.ElementStyle;
import de.kaiser.model.style.FootnoteStyleProperties;
import de.kaiser.model.style.StyleResolverContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

/**
 * Represents a footnote that can now be styled and inherits styles correctly.
 */
@JsonTypeName("footnote")
public final class Footnote extends AbstractInlineElement {

    private static final Logger log = LoggerFactory.getLogger(Footnote.class);
    private static final String DEFAULT_INDEX = "*";

    @JsonIgnore
    private final String id;
    private final String index;
    private final List<InlineElement> inlineElements;


    // NEU: Feld für den aufgelösten Stil des Fußnotentextes
    @JsonIgnore
    private FootnoteStyleProperties resolvedStyle;

    @JsonCreator
    public Footnote(
            @JsonProperty("index") String index,
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("variant") String variant,
            @JsonProperty("inline-elements") List<InlineElement> inlineElements
    ) {
        super(styleClass, variant);
        this.id = "footnote-" + UUID.randomUUID();
        if (index == null || index.isEmpty()) {
            log.warn("index is null or empty. Set default one {}", DEFAULT_INDEX);
            this.index = DEFAULT_INDEX;
        } else {
            this.index = index;
        }
        this.inlineElements = inlineElements;
    }

    // --- Getters und ein neuer Setter ---
    public String getId() { return id; }
    public String getIndex() { return index; }
    public List<InlineElement> getInlineElements() { return inlineElements; }
    public FootnoteStyleProperties getResolvedStyle() { return resolvedStyle; }
    public void setResolvedStyle(FootnoteStyleProperties resolvedStyle) { this.resolvedStyle = resolvedStyle; }


    @Override
    public String getType() {
        return InlineElementTypes.FOOTNOTE;
    }

    /**
     * Resolves the style for the footnote body and delegates the resolution
     * to its child elements with a new, more specific context.
     */
    @Override
    public void resolveStyles(StyleResolverContext context) {
        // 1. Resolve the style for the footnote body itself.
        ElementStyle specificElementStyle = context.styleMap().get(this.getStyleClass());
        if (specificElementStyle != null && specificElementStyle.properties() instanceof FootnoteStyleProperties specificStyle) {
            FootnoteStyleProperties finalStyle = specificStyle.copy();
            finalStyle.mergeWith(context.parentBlockStyle());
            this.setResolvedStyle(finalStyle);
        } else {
            // If no specific style, create a new one that just inherits from the parent.
            FootnoteStyleProperties defaultStyle = new FootnoteStyleProperties();
            defaultStyle.mergeWith(context.parentBlockStyle());
            this.setResolvedStyle(defaultStyle);
        }

        // 2. Create a new context for the children inside the footnote.
        // The resolved style of the footnote body becomes the new parent style.
        StyleResolverContext childContext = context.createChildContext(this.getResolvedStyle());

        // 3. Delegate to child elements.
        if (inlineElements != null) {
            for (InlineElement element : inlineElements) {
                element.resolveStyles(childContext);
            }
        }
    }
}
