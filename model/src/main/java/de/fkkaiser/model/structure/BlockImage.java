package de.fkkaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.fkkaiser.model.style.BlockImageStyleProperties;
import de.fkkaiser.model.style.ElementBlockStyleProperties;
import de.fkkaiser.model.style.ElementStyle;
import de.fkkaiser.model.style.StyleResolverContext;
import java.util.Optional;

@JsonTypeName(ElementTypes.BLOCK_IMAGE)
public final class BlockImage implements Element {

    private final String styleClass;
    private final String path;
    private final String altText;

    @JsonIgnore
    private BlockImageStyleProperties resolvedStyle;

    @JsonCreator
    public BlockImage(
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("path") String path,
            @JsonProperty("alt-text") String altText
    ) {
        this.styleClass = styleClass;
        this.path = path;
        this.altText = altText;
    }

    // --- Getters ---
    @Override
    public String getStyleClass() {
        return styleClass;
    }

    public String getPath() {
        return path;
    }

    public String getAltText() {return altText;}

    public BlockImageStyleProperties getResolvedStyle() {
        return resolvedStyle;
    }

    @Override
    public String getType() {
        return ElementTypes.BLOCK_IMAGE;
    }

    @Override
    public void resolveStyles(StyleResolverContext context) {
        ElementBlockStyleProperties parentStyle = context.parentBlockStyle();

        BlockImageStyleProperties specificStyle = Optional.ofNullable(context.styleMap().get(this.getStyleClass()))
                .map(ElementStyle::properties)
                .filter(BlockImageStyleProperties.class::isInstance)
                .map(BlockImageStyleProperties.class::cast)
                .orElse(new BlockImageStyleProperties());

        BlockImageStyleProperties finalStyle = specificStyle.copy();
        finalStyle.mergeWith(parentStyle);

        this.resolvedStyle = finalStyle;
    }
}