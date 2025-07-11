package de.kaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Basic interface for all inline elements within a block (for example, paragraph).
 * This enables mixed content like text and highlighted text
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextRun.class, name = InlineElementTypes.TEXT_RUN),
        @JsonSubTypes.Type(value = PageNumber.class, name = InlineElementTypes.PAGE_NUMBER),
        @JsonSubTypes.Type(value = Footnote.class, name = InlineElementTypes.FOOTNOTE),
        @JsonSubTypes.Type(value = Hyperlink.class, name = InlineElementTypes.HYPERLINK)
})
public interface InlineElement extends Element {

}
