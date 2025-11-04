package de.fkkaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fkkaiser.model.structure.builder.PageSequenceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a sequence of pages in the document.
 * It contains styling information (required), body content, header content, and footer content for the pages.
 */
public record PageSequence(
        @JsonProperty("style-class")
        String styleClass,
        @JsonProperty("body")
        ContentArea body,
        @JsonProperty("header")
        ContentArea header,
        @JsonProperty("footer")
        ContentArea footer
) {

    private static final Logger log = LoggerFactory.getLogger(PageSequence.class);

    /**
     * Compact constructor for validation.
     * Ensures that 'styleClass' is never null.
     */
    public PageSequence {
        if (styleClass == null || styleClass.isEmpty()) {
            log.error("styleClass is empty or null");
            throw new IllegalArgumentException("styleClass cannot be null or empty.");
        }
    }

    /**
     * Creates a new instance of Builder for constructing a PageSequence object with the specified style class.
     *
     * @param styleClass the style class for the PageSequence (required)
     * @return a new instance of Builder with the provided style class
     */
    public static PageSequenceBuilder builder(String styleClass) {
        return new PageSequenceBuilder(styleClass);
    }


}