package de.kaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;


/**
 * Represents a paragraph element that extends ETextBlock.
 */
@JsonTypeName(ElementTypes.PARAGRAPH)
public class Paragraph extends TextBlock {



    public Paragraph(String styleClass, List<InlineElement> inlineElements, String variant) {
        super(styleClass,inlineElements, variant);
    }

    public Paragraph(String styleClass, List<InlineElement> inlineElements) {
        super(styleClass,inlineElements);
    }


    public Paragraph(String styleClass, String variant) {
        super(styleClass, variant);
    }


    public Paragraph(String styleClass) {
        super(styleClass);
    }

    @Override
    public String getType() {
        return ElementTypes.PARAGRAPH;
    }


}