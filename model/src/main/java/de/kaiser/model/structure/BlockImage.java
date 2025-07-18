package de.kaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.kaiser.model.style.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@JsonTypeName(ElementTypes.BLOCK_IMAGE)
public final class BlockImage implements Element {

    private static final Logger log = LoggerFactory.getLogger(Part.class);

    private final String styleClass;
    private final String path;

    @JsonIgnore
    private BlockImageStyleProperties resolvedStyle;

    @JsonCreator
    public BlockImage(
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("path") String path
    ) {
        this.styleClass = styleClass;
        this.path = path;
    }

    // --- Getters ---
    @Override
    public String getStyleClass() {
        return styleClass;
    }

    public String getPath() {
        return path;
    }

    public BlockImageStyleProperties getResolvedStyle() {
        return resolvedStyle;
    }

    public void setResolvedStyle(BlockImageStyleProperties resolvedStyle) {
        this.resolvedStyle = resolvedStyle;
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
        this.setResolvedStyle(finalStyle);
    }
}