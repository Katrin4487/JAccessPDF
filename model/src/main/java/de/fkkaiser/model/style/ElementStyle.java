package de.fkkaiser.model.style;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Represents a single, named style definition in the stylesheet.
 * It connects a style name (e.g., "important-paragraph") to a set of properties,
 * targeting a specific element type.
 */
public record ElementStyle(
        @JsonProperty("name") String name,
        @JsonProperty("target-element") String targetElement,
        @JsonProperty("properties") ElementStyleProperties properties
) {

    @Override
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "target-element")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = ParagraphStyleProperties.class, name = StyleTargetTypes.PARAGRAPH),
            @JsonSubTypes.Type(value = HeadlineStyleProperties.class, name = StyleTargetTypes.HEADLINE),
            @JsonSubTypes.Type(value = ListStyleProperties.class, name = StyleTargetTypes.LIST),
            @JsonSubTypes.Type(value = TableStyleProperties.class, name = StyleTargetTypes.TABLE),
            @JsonSubTypes.Type(value = TableCellStyleProperties.class, name = StyleTargetTypes.TABLE_CELL),
            @JsonSubTypes.Type(value = SectionStyleProperties.class, name = StyleTargetTypes.SECTION),
            @JsonSubTypes.Type(value = TextRunStyleProperties.class, name = StyleTargetTypes.TEXT_RUN),
            @JsonSubTypes.Type(value = FootnoteStyleProperties.class, name = StyleTargetTypes.FOOTNOTE),
            @JsonSubTypes.Type(value = BlockImageStyleProperties.class, name = StyleTargetTypes.BLOCK_IMAGE),
            @JsonSubTypes.Type(value = LayoutTableStyleProperties.class, name = StyleTargetTypes.LAYOUT_TABLE),

    })
    public ElementStyleProperties properties() {
        return properties;
    }

    /**
     * Creates an ElementStyle object for a paragarph
     * @param name name for this element style object (unique identifier)
     * @param textStyle TextStyle object that should be used for the paragraph
     * @return ElementStyle object used for paragraphs
     */
    @SuppressWarnings("unused")
    public static ElementStyle forParagraph(String name,TextStyle textStyle) {
        ParagraphStyleProperties paragraphStyleProperties = new ParagraphStyleProperties();
        paragraphStyleProperties.setTextStyleName(textStyle.name());
        return new ElementStyle(name,StyleTargetTypes.PARAGRAPH,paragraphStyleProperties);
    }

    /**
     * Creates an ElementStyle object for a headline
     * @param name name for this element style object (unique identifier)
     * @param textStyle TextStyle object that should be used for the headline
     * @return ElementStyle object used for headlines
     */
    @SuppressWarnings("unused")
    public static ElementStyle forHeadline(String name,TextStyle textStyle) {
        HeadlineStyleProperties headlineStyleProperties = new HeadlineStyleProperties();
        headlineStyleProperties.setTextStyleName(textStyle.name());
        return new ElementStyle(name,StyleTargetTypes.HEADLINE,headlineStyleProperties);
    }
}
