package de.kaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a hyperlink element that extends from {@link TextRun}.
 * This class provides the functionality to create a hyperlink with a specified URL or destination.
 */
public class Hyperlink extends TextRun {

    private static final Logger log = LoggerFactory.getLogger(Hyperlink.class);
    private final String href;
    private final String altText;

    /**
     * Creates a Hyperlink element that extends from TextRun.
     * This class provides the functionality to create a hyperlink with a specified URL or destination.
     *
     * @param text The text content to display in the hyperlink.
     * @param styleClass The class specifying the style of the hyperlink.
     * @param variant The variant of the hyperlink element.
     * @param href The URL or destination of the hyperlink.
     * @param altText The alternative text in case the original text is not available or empty.
     */
    @JsonCreator
    public Hyperlink(
            @JsonProperty("text") String text,
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("variant") String variant,
            @JsonProperty("href") String href,
            @JsonProperty("alt-text") String altText
    ) {
        super(text, styleClass, variant);

        if (text == null || text.isBlank()) {
            log.info("text is null; Setting href as text");
        }
        if (href == null || href.isBlank()) {
            log.warn("Hyperlink created with empty or null href");
            this.href = "";
        } else {
            this.href = href;
        }
        if (altText == null || altText.isBlank()) {
            log.info("altText is null; Setting text as altText");
            this.altText = text;
        } else {
            this.altText = altText;
        }
    }

    /**
     * Constructs a Hyperlink with the provided text, style class, and href.
     * The variant is set to null and alt text is equal to the text.
     *
     * @param text The text content to display in the hyperlink.
     * @param styleClass The class specifying the style of the hyperlink.
     * @param href The URL or destination of the hyperlink.
     */
    public Hyperlink(String text, String styleClass, String href) {
        this(text, styleClass, null, href, null);
    }

    /**
     * Constructs a Hyperlink element with the specified text, style class, URL destination, and alternative text.
     *
     * @param text The text content to display in the hyperlink.
     * @param styleClass The class specifying the style of the hyperlink.
     * @param href The URL or destination of the hyperlink.
     * @param altText The alternative text in case the original text is not available or empty.
     */
    @SuppressWarnings("unused")
    public Hyperlink(String text, String styleClass, String href,String altText) {
        this(text, styleClass,null , href, altText);
    }

    /**
     * @return The URL or destination of the hyperlink.
     */
    public String getHref() {
        return href;
    }

    public String getAltText() {
        return altText;
    }

    @Override
    public String getType() {
        return InlineElementTypes.HYPERLINK;
    }
}

