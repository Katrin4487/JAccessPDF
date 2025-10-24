package de.fkkaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.fkkaiser.model.style.StyleResolverContext;
import de.fkkaiser.model.style.StyleResolverContext;

/**
 * An empty "marker" element
 * (XSL-FO page number)
 */
@JsonTypeName("page-number")
public class PageNumber extends AbstractInlineElement {

    @JsonCreator
    public PageNumber(
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("variant") String variant) {
        super(styleClass, variant);
    }

    public PageNumber(){
        this(null, null);
    }

    public PageNumber(String styleClass){
        this(styleClass, null);
    }

    @Override
    public String getType() {
        return InlineElementTypes.PAGE_NUMBER;
    }

    @Override
    public void resolveStyles(StyleResolverContext context) {
        // PageNumber hat typischerweise keinen eigenen Stil,
        // erbt aber den Kontext. Wenn doch, könnte hier Logik stehen.
    }
}
