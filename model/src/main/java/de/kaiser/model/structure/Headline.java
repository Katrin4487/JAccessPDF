package de.kaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * Represents a headline element, which is a specific type of TextBlock.
 * It inherits common properties like styleClass and variant from AbstractElement.
 */
@JsonTypeName(ElementTypes.HEADLINE)
public final class Headline extends TextBlock {

    private static final Logger log = LoggerFactory.getLogger(Headline.class);
    private static final int DEFAULT_LEVEL = 1;

    /**
     * The hierarchy level of the headline, corresponding to h1-h6 in HTML.
     */
    private final int level;

    /**
     * The primary constructor used by Jackson for deserialization from JSON.
     * It initializes all properties, including those from the parent classes.
     *
     * @param styleClass     The CSS-like class for styling.
     * @param inlineElements The list of inline elements (e.g., TextRun) forming the content.
     * @param variant        A semantic variant of the element (e.g., "warning").
     * @param level          The headline level (1-6).
     */
    @JsonCreator
    public Headline(
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("inline-elements") List<InlineElement> inlineElements,
            @JsonProperty("variant") String variant,
            @JsonProperty("level") Integer level
    ) {

        super(styleClass, inlineElements, variant);

        if (level == null) {
            log.warn("Headline 'level' is not defined. Using default value {}.", DEFAULT_LEVEL);
            this.level = DEFAULT_LEVEL;
        } else {
            this.level = level;
        }
    }

    public Headline(String styleClass,
                    String text, Integer level) {
        this(styleClass, Collections.singletonList(new TextRun(text)), null, level);

    }

    @Override
    public String getType() {
        return ElementTypes.HEADLINE;
    }

    public int getLevel() {
        return level;
    }
}
