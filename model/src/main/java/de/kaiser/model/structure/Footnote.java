package de.kaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.kaiser.model.style.StyleResolverContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

/**
 * Represents a footnote. A footnote consists of two parts:
 * 1. The reference in the text (the index, e.g., "1").
 * 2. The footnote body, which can contain rich content (a list of inline elements).
 * It now includes a unique ID for accessibility.
 */
@JsonTypeName("footnote")
public final class Footnote extends AbstractInlineElement {

    private static final Logger log = LoggerFactory.getLogger(Footnote.class);
    private static final String DEFAULT_INDEX = "*";

    // A unique ID, automatically generated, for accessibility linking.
    @JsonIgnore // This ID is not part of the input JSON.
    private final String id;
    private final String index;
    private final List<InlineElement> inlineElements;

    @JsonCreator
    public Footnote(
            @JsonProperty("index") String index,
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("variant") String variant,
            @JsonProperty("inline-elements") List<InlineElement> inlineElements
    ) {
        super(styleClass,variant);
        this.id = "footnote-" + UUID.randomUUID(); // Generate a unique ID
        if(index==null || index.isEmpty()){
            log.warn("index is null or empty. Set default one {}", DEFAULT_INDEX);
            this.index = DEFAULT_INDEX;
        }else {
            this.index = index;
        }
        this.inlineElements = inlineElements;
    }

    public Footnote(String index,String styleClass,List<InlineElement> inlineElements) {
        this(index,styleClass,null,inlineElements);
    }

    public Footnote(String index,List<InlineElement> inlineElements) {
        this(index,null,null,inlineElements);
    }


    public String getId() {
        return id;
    }

    public String getIndex() {
        return index;
    }

    public List<InlineElement> getInlineElements() {
        return inlineElements;
    }

    @Override
    public String getType() {
        return InlineElementTypes.FOOTNOTE;
    }


    public String getText() {
        return null; // A footnote container has no single text.
    }

    @Override
    public void resolveStyles(StyleResolverContext context) {
        if (inlineElements != null) {
            for (InlineElement element : inlineElements) {
                element.resolveStyles(context);
            }
        }
    }
}
