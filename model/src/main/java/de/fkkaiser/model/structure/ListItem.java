package de.fkkaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.fkkaiser.model.style.ElementBlockStyleProperties;
import de.fkkaiser.model.style.ElementStyle;
import de.fkkaiser.model.style.StyleResolverContext;
import de.fkkaiser.model.style.TextBlockStyleProperties;

import java.util.List;

/**
 * Represents a list item, which can function as a standard list item
 * or as an entry in a definition list with a label and a body.
 */
@JsonTypeName("list-item")
public final class ListItem implements Element {

    private final String styleClass;
    private final String variant;

    // The label of the list item (e.g., a term in a definition list).
    // Can contain rich inline content.
    private final List<InlineElement> label;

    // The body of the list item (e.g., the definition of the term).
    // Can contain block-level elements, including nested lists.
    private final List<Element> elements;

    @JsonIgnore
    private ElementBlockStyleProperties resolvedStyle;

    @JsonCreator
    public ListItem(
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("variant") String variant,
            @JsonProperty("label") List<InlineElement> label, // NEU
            @JsonProperty("elements") List<Element> elements
    ) {
        this.styleClass = styleClass;
        this.variant = variant;
        this.label = label;
        this.elements = elements;
    }

    // --- Getters ---
    @Override
    public String getStyleClass() { return styleClass; }
    public String getVariant() { return variant; }
    public List<InlineElement> getLabel() { return label; } // NEU
    public List<Element> getElements() { return elements; }
    public ElementBlockStyleProperties getResolvedStyle() { return resolvedStyle; }

    @Override
    public String getType() {
        return "list-item";
    }

    @Override
    public void resolveStyles(StyleResolverContext context) {
        // Resolve the style for the list item container itself
        ElementStyle specificElementStyle = context.styleMap().get(this.getStyleClass());
        ElementBlockStyleProperties newResolvedStyle; // KORREKTUR: Als Variable deklariert

        if (specificElementStyle != null && specificElementStyle.properties() instanceof TextBlockStyleProperties specificStyle) {
            newResolvedStyle = specificStyle.copy();
            newResolvedStyle.mergeWith(context.parentBlockStyle());
        } else {
               if (context.parentBlockStyle() != null) {
                newResolvedStyle = context.parentBlockStyle().copy();
            } else {
                // Fallback, wenn es keinen Parent-Stil gibt (z.B. Top-Level-Liste)
                newResolvedStyle = new TextBlockStyleProperties();
            }
        }

        this.resolvedStyle = newResolvedStyle;

        StyleResolverContext childContext = context.createChildContext(this.getResolvedStyle());
        if (label != null) {
            label.forEach(el -> el.resolveStyles(childContext));
        }
        if (elements != null) {
            elements.forEach(el -> el.resolveStyles(childContext));
        }
    }
}
