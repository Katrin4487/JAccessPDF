package de.fkkaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.fkkaiser.model.style.ElementBlockStyleProperties;
import de.fkkaiser.model.style.ElementStyle;
import de.fkkaiser.model.style.PartStyleProperties;
import de.fkkaiser.model.style.StyleResolverContext;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@JsonTypeName(ElementTypes.PART)
public final class Part implements Element {

    private final String styleClass;
    private final List<Element> elements;

    @JsonIgnore
    private PartStyleProperties resolvedStyle;

    @JsonCreator
    public Part(
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("elements") List<Element> elements
    ) {

        this.styleClass = styleClass;
        this.elements = Objects.requireNonNullElse(elements, List.of());
    }

    // --- Getters ---
    @Override
    public String getStyleClass() { return styleClass; }
    public List<Element> getElements() { return elements; }
    public PartStyleProperties getResolvedStyle() { return resolvedStyle; }

    @Override
    public String getType() {
        return ElementTypes.PART;
    }

    @Override
    public void resolveStyles(StyleResolverContext context) {
        ElementBlockStyleProperties parentStyle = context.parentBlockStyle();

        PartStyleProperties specificStyle = Optional.ofNullable(context.styleMap().get(this.getStyleClass()))
                .map(ElementStyle::properties)
                .filter(PartStyleProperties.class::isInstance)
                .map(PartStyleProperties.class::cast)
                .orElse(new PartStyleProperties());

        PartStyleProperties finalStyle = specificStyle.copy();
        finalStyle.mergeWith(parentStyle);

        this.resolvedStyle = finalStyle;

        StyleResolverContext childContext = context.createChildContext(this.getResolvedStyle());
        elements.forEach(element -> element.resolveStyles(childContext));
    }
}