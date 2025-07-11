package de.kaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * Class representing a headline element that extends ETextBlock.
 */
@JsonTypeName(ElementTypes.HEADLINE)
public class Headline extends TextBlock {

    private static final Logger log = LoggerFactory.getLogger(Headline.class);

    private static final int DEFAULT_LEVEL = 1;

    /**
     * Represents the level of a headline element.
     * Allowed is 1-6 corresponding to h1-h6 in html.
     */
    private final int level;

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



    public Headline(String styleClass, String variant,int level) {
        super(styleClass, variant);
        this.level = level;
    }

    public Headline(String styleClass,int level) {
        super(styleClass);
        this.level = level;
    }

    @Override
    public String getType() {
        return ElementTypes.HEADLINE;
    }

    public int getLevel() {
        return level;
    }
}

