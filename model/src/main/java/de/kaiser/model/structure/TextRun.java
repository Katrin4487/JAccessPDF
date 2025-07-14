package de.kaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.kaiser.model.style.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents an inline element for displaying text within a document.
 * This class implements the {@link InlineElement} interface.
 */
@JsonTypeName("text-run")
public class TextRun extends AbstractInlineElement {

    private static final Logger log = LoggerFactory.getLogger(TextRun.class);

    private final String text;


    @JsonIgnore
    private TextRunStyleProperties resolvedStyle;

    @JsonCreator
    public TextRun(
            @JsonProperty("text") String text,
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("variant") String variant
    ) {
        super(styleClass, variant);
        this.text = text;
    }

    public TextRun(String text){
        this(text, null, null);
    }

    public TextRun(String text,String variant){
        this(text, variant, null);
    }

    @Override
    public String getType() {
        return InlineElementTypes.TEXT_RUN;
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
        ElementBlockStyleProperties parentStyle = context.parentBlockStyle();
        TextRunStyleProperties specificRunStyle = null;

        if (styleClass != null && !styleClass.isEmpty()) {
            ElementStyle specificElementStyle = context.styleMap().get(styleClass);
            if (specificElementStyle != null && specificElementStyle.properties() instanceof TextRunStyleProperties) {
                specificRunStyle = (TextRunStyleProperties) specificElementStyle.properties();
            } else {
                log.warn("Style class '{}' not found or has incorrect type.", this.styleClass);
            }
        }


        this.resolvedStyle = TextRunStyleProperties.createResolved(parentStyle, specificRunStyle);
    }
}
