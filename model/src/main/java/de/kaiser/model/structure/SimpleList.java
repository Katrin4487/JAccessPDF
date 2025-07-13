package de.kaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.kaiser.model.structure.Element;
import de.kaiser.model.structure.ListItem;
import de.kaiser.model.structure.ListOrdering;
import de.kaiser.model.style.ElementStyle;
import de.kaiser.model.style.ListStyleProperties;
import de.kaiser.model.style.StyleResolverContext;


import java.util.List;

/**
 * Represents a list element in a document with a specific style class,
 * ordering, and list items.
 */
@JsonTypeName("list")
public final class SimpleList implements Element {

    private final String styleClass;
    private final ListOrdering ordering;
    private final List<ListItem> items;

    @JsonIgnore
    private ListStyleProperties resolvedStyle;

    @JsonCreator
    public SimpleList(
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("ordering") ListOrdering ordering,
            @JsonProperty("items") List<ListItem> items
    ) {
        this.styleClass = styleClass;
        this.ordering = ordering;
        this.items = items;
    }

    @Override
    public String getType() {
        return "list";
    }

    @Override
    public String getStyleClass() {
        return styleClass;
    }

    // Getter...
    public ListOrdering getOrdering() { return ordering; }
    public List<ListItem> getItems() { return items; }
    public ListStyleProperties getResolvedStyle() { return resolvedStyle; }
    public void setResolvedStyle(ListStyleProperties resolvedStyle) { this.resolvedStyle = resolvedStyle; }

    @Override
    public void resolveStyles(StyleResolverContext context) {
        ElementStyle style = context.styleMap().get(this.getStyleClass());
        if (style != null && style.properties() instanceof ListStyleProperties listStyleProperties) {
            this.setResolvedStyle(listStyleProperties);
        }

        StyleResolverContext childContext = context.createChildContext(this.getResolvedStyle());


        if (items != null) {
            for (ListItem item : items) {
                // Pass the new, more specific context to the children.
                item.resolveStyles(childContext);
            }
        }
    }
}
