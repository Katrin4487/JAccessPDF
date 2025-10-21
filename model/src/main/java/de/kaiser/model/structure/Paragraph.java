package de.kaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;


/**
 * Represents a paragraph element that extends TextBlock.
 */
@JsonTypeName(ElementTypes.PARAGRAPH)
public class Paragraph extends TextBlock {



    public Paragraph(
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("inline-elements") List<InlineElement> inlineElements,
            @JsonProperty("variant") String variant
            ) {
        super(styleClass,inlineElements, variant);
    }

    public Paragraph(String styleClass, String standAloneText, String variant) {
        super(styleClass,List.of(new TextRun(styleClass, standAloneText, variant)),variant);
    }

    public Paragraph(String styleClass, String standAloneText) {
        super(styleClass,List.of(new TextRun(standAloneText,styleClass,null)));
    }

    public Paragraph(String styleClass, InlineElement inlineElement, String variant) {
        super(styleClass,List.of(inlineElement),variant);
    }

    public Paragraph(String styleClass, InlineElement inlineElement) {
        super(styleClass,List.of(inlineElement));
    }

    public Paragraph(String styleClass, List<InlineElement> inlineElements) {
        super(styleClass,inlineElements);
    }

    public Paragraph(String styleClass) {
        super(styleClass);
    }

    @Override
    public String getType() {
        return ElementTypes.PARAGRAPH;
    }


}