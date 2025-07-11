package de.kaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    public static Builder builder(String styleClass) {
        return new Builder(styleClass);
    }

    /**
     * A builder for creating instances of {@link PageSequence}.
     */
    public static class Builder {
        private final String styleClass;
        private ContentArea body;
        private ContentArea header;
        private ContentArea footer;

        public Builder(String styleClass) {
            this.styleClass = styleClass;
        }

        public Builder body(ContentArea body) { this.body = body; return this; }
        public Builder header(ContentArea header) { this.header = header; return this; }
        public Builder footer(ContentArea footer) { this.footer = footer; return this; }

        public PageSequence build() {
            return new PageSequence(styleClass, body, header, footer);
        }
    }
}