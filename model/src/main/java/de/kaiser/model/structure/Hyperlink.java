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

    @JsonCreator
    public Hyperlink(
            @JsonProperty("text") String text,
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("variant") String variant,
            @JsonProperty("href") String href
    ) {
        super(text, styleClass, variant);

        if(text==null || text.isBlank()){
            log.info("text is null; Setting href as text");
        }
        if(href==null || href.isBlank()){
            log.warn("Hyperlink created with empty or null href");
            this.href = "";
        }else {
            this.href = href;
        }
    }

    public Hyperlink(String text, String styleClass,String href) {
        this(text,styleClass,href,null);
    }
    /**
     * @return The URL or destination of the hyperlink.
     */
    public String getHref() {
        return href;
    }

    @Override
    public String getType() {
        return InlineElementTypes.HYPERLINK;
    }
}

