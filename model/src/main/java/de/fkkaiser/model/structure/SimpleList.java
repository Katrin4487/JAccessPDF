package de.fkkaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.fkkaiser.model.style.ElementBlockStyleProperties;
import de.fkkaiser.model.style.ElementStyle;
import de.fkkaiser.model.style.ListStyleProperties;
import de.fkkaiser.model.style.StyleResolverContext;


import java.util.List;
import java.util.Optional;

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


    @Override
    public void resolveStyles(StyleResolverContext context) {
          ElementBlockStyleProperties parentStyle = context.parentBlockStyle();

        ListStyleProperties specificStyle = Optional.ofNullable(context.styleMap().get(this.getStyleClass()))
                .map(ElementStyle::properties)
                .filter(ListStyleProperties.class::isInstance)
                .map(ListStyleProperties.class::cast)
                .orElse(new ListStyleProperties()); // Leeres Objekt, falls kein Stil definiert ist.

         ListStyleProperties finalStyle = specificStyle.copy();
        finalStyle.mergeWith(parentStyle); // Vererbung vom Parent

         this.resolvedStyle = finalStyle;

         StyleResolverContext childContext = context.createChildContext(this.getResolvedStyle());

        if (items != null) {
            for (ListItem item : items) {
                // Pass the new, more specific context to the children.
                item.resolveStyles(childContext);
            }
        }
    }
}