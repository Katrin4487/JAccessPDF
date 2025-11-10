package de.fkkaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.fkkaiser.model.style.StyleResolverContext;



/**
 * Base interface for all content elements.
 * Uses Jackson's polymorphism mechanism based on the 'type' field in the JSON to instantiate the
 * correct element class.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value =
                Paragraph.class, name = ElementTypes.PARAGRAPH),
        @JsonSubTypes.Type(value = Headline.class, name = ElementTypes.HEADLINE),
        @JsonSubTypes.Type(value = SimpleList.class, name = ElementTypes.LIST),
        @JsonSubTypes.Type(value = Table.class, name = ElementTypes.TABLE),
        @JsonSubTypes.Type(value = Section.class, name = ElementTypes.SECTION),
        @JsonSubTypes.Type(value = BlockImage.class, name = ElementTypes.BLOCK_IMAGE),
        @JsonSubTypes.Type(value = LayoutTable.class, name = ElementTypes.LAYOUT_TABLE),
})
public interface Element {

    String getType();
    String getStyleClass();

    /**
     * Resolves styles for the given element using the provided style resolver context.
     *
     * @param context The style resolver context containing style map and default text style properties
     */
    void resolveStyles(StyleResolverContext context);

}
