package de.kaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.kaiser.model.style.ElementStyle;
import de.kaiser.model.style.StyleResolverContext;
import de.kaiser.model.style.TextBlockStyleProperties;
import de.kaiser.model.style.TextRunStyleProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents an inline element for displaying text within a document.
 * This class implements the {@link InlineElement} interface.
 */
@JsonTypeName("text-run")
public class TextRun implements InlineElement {

    private static final Logger log = LoggerFactory.getLogger(TextRun.class);

    private final String text;
    private final String styleClass;
    private final String variant;

    @JsonIgnore
    private TextRunStyleProperties resolvedStyle;

    @JsonCreator
    public TextRun(
            @JsonProperty("text") String text,
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("variant") String variant
    ) {
        this.text = text;
        this.styleClass = styleClass;
        this.variant = variant;
    }

    @Override
    public String getType() {
        return InlineElementTypes.TEXT_RUN;
    }

    @Override
    public String getStyleClass() {
        return styleClass;
    }

    public String getVariant() {
        return variant;
    }

    public TextRunStyleProperties getResolvedStyle() {
        return resolvedStyle;
    }

    public String getText() {
        return text;
    }

    /**
     * Resolves the styles for this text run based on the provided context.
     * The logic is now more explicit: it determines the final value for each property
     * by checking the specific style first, then falling back to the parent style.
     *
     * @param context The style resolver context containing necessary style information.
     */
    @Override
    public void resolveStyles(StyleResolverContext context) {
        TextBlockStyleProperties parentStyle = context.getParentBlockStyle();
        TextRunStyleProperties specificRunStyle = null;

        // Find the specific style if a styleClass is provided.
        if (styleClass != null && !styleClass.isEmpty()) {
            ElementStyle specificElementStyle = context.getStyleMap().get(styleClass);
            if (specificElementStyle != null && specificElementStyle.properties() instanceof TextRunStyleProperties) {
                specificRunStyle = (TextRunStyleProperties) specificElementStyle.properties();
            } else {
                log.warn("Style class '{}' for text run '{}' not found or has incorrect property type. Falling back to parent block style.",
                        this.styleClass, this.text);
            }
        }

        assert specificRunStyle != null;
        this.resolvedStyle = TextRunStyleProperties.createResolved(parentStyle, specificRunStyle);
    }


}
