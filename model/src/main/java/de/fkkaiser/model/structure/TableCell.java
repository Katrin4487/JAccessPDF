package de.fkkaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.fkkaiser.model.style.ElementStyle;
import de.fkkaiser.model.style.StyleResolverContext;
import de.fkkaiser.model.style.TableCellStyleProperties;
import de.fkkaiser.model.style.ElementStyle;
import de.fkkaiser.model.style.StyleResolverContext;
import de.fkkaiser.model.style.TableCellStyleProperties;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a table cell, which acts as a container for other block-level elements.
 * It is not a full EElement itself but participates in style resolution.
 */
public class TableCell {

    private final String styleClass;
    private final List<Element> elements;
    // NEW: Support for spanning columns and rows
    private final int colspan;
    private final int rowspan;

    @JsonIgnore
    private TableCellStyleProperties resolvedStyle;

    @JsonCreator
    public TableCell(
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("elements") List<Element> elements,
            @JsonProperty("colspan") Integer colspan,
            @JsonProperty("rowspan") Integer rowspan) {
        this.styleClass = styleClass;
        this.elements = Objects.requireNonNullElse(elements, List.of());
        this.colspan = Optional.ofNullable(colspan).orElse(1);
        this.rowspan = Optional.ofNullable(rowspan).orElse(1);
    }


    // --- Getters ---
    public String getStyleClass() { return styleClass; }
    public List<Element> getElements() { return elements; }
    public int getColspan() { return colspan; }
    public int getRowspan() { return rowspan; }
    public TableCellStyleProperties getResolvedStyle() { return resolvedStyle; }
    public void setResolvedStyle(TableCellStyleProperties resolvedStyle) { this.resolvedStyle = resolvedStyle; }

    /**
     * Resolves styles for the cell and its contained elements.
     * @param context The current style context.
     */
    public void resolveStyles(StyleResolverContext context) {

        Optional.ofNullable(context.styleMap().get(this.getStyleClass()))
                .map(ElementStyle::properties)
                .filter(TableCellStyleProperties.class::isInstance)
                .map(TableCellStyleProperties.class::cast)
                .ifPresent(this::setResolvedStyle);

        StyleResolverContext childContext = context.createChildContext(null);
        elements.forEach(element -> element.resolveStyles(childContext));
    }
}
