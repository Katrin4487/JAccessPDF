package de.fkkaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fkkaiser.model.style.ElementBlockStyleProperties;
import de.fkkaiser.model.style.ElementStyle;
import de.fkkaiser.model.style.LayoutTableStyleProperties;
import de.fkkaiser.model.style.StyleResolverContext;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class LayoutTable implements Element{

    private final String styleClass;
    private final Element elementLeft;
    private final Element elementRight;

    private LayoutTableStyleProperties resolvedStyle;


    public LayoutTable(
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("element-left") Element elementLeft,
            @JsonProperty("element-right") Element elementRight) {

        this.styleClass = styleClass;
        this.elementLeft = elementLeft;
        this.elementRight = elementRight;

    }

    public Element getElementLeft() {
        return elementLeft;
    }

    public Element getElementRight() {
        return elementRight;
    }

    public LayoutTableStyleProperties getResolvedStyle() {
        return resolvedStyle;
    }

    void setResolvedStyle(LayoutTableStyleProperties resolvedStyle) {
        this.resolvedStyle = resolvedStyle;
    }

    @Override
    public String getType() {
        return ElementTypes.LAYOUT_TABLE;
    }

    @Override
    public String getStyleClass() {
        return styleClass;
    }

    /**
     * Resolves styles for the given element using the provided style resolver context.
     *
     * @param context The style resolver context containing style map and default text style properties
     */
    @Override
    public void resolveStyles(StyleResolverContext context) {

        ElementBlockStyleProperties parentStyle = context.parentBlockStyle();
        ElementStyle specificElementStyle = context.styleMap().get(this.getStyleClass());

        LayoutTableStyleProperties specificStyle = Optional.ofNullable(specificElementStyle)
                .map(ElementStyle::properties)
                .filter(LayoutTableStyleProperties.class::isInstance)
                .map(LayoutTableStyleProperties.class::cast)
                .orElse(new LayoutTableStyleProperties()); // Standard-Style, wenn nichts gefunden wurde

        LayoutTableStyleProperties finalStyle = specificStyle.copy();
        finalStyle.mergeWith(parentStyle);

        this.resolvedStyle = finalStyle;

        StyleResolverContext childContext = context.createChildContext(this.getResolvedStyle());
        finalStyle.mergeWith(parentStyle);
        this.setResolvedStyle(finalStyle);

        Stream.of(elementLeft,elementRight)
                .filter(Objects::nonNull)
                .forEach(elem -> elem.resolveStyles(childContext));
    }
}
