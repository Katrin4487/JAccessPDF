package de.kaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.kaiser.model.structure.Element;
import de.kaiser.model.structure.ElementTypes;
import de.kaiser.model.style.*;

import java.util.List;

/**
 * Represents a structural section element, which acts as a container
 * for other block-level elements like paragraphs, lists, or other sections.
 */
@JsonTypeName(ElementTypes.SECTION)
public final class Section implements Element {

    private final String styleClass;
    private final String variant;
    private final List<Element> elements;

    @JsonIgnore
    private SectionStyleProperties resolvedStyle;

    @JsonCreator
    public Section(
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("variant") String variant,
            @JsonProperty("elements") List<Element> elements
    ) {
        this.styleClass = styleClass;
        this.variant = variant;
        this.elements = elements;
    }

    // --- Getters ---
    @Override
    public String getStyleClass() { return styleClass; }
    public String getVariant() { return variant; }
    public List<Element> getElements() { return elements; }
    public SectionStyleProperties getResolvedStyle() { return resolvedStyle; }
    public void setResolvedStyle(SectionStyleProperties resolvedStyle) { this.resolvedStyle = resolvedStyle; }

    @Override
    public String getType() {
        return ElementTypes.SECTION;
    }

    @Override
    public void resolveStyles(StyleResolverContext context) {
        ElementBlockStyleProperties parentStyle = context.parentBlockStyle();
        ElementStyle specificElementStyle = context.styleMap().get(this.getStyleClass());

        if (specificElementStyle != null && specificElementStyle.properties() instanceof SectionStyleProperties specificSectionStyle) {
            SectionStyleProperties finalStyle = specificSectionStyle.copy();
            finalStyle.mergeWith(parentStyle);
            this.setResolvedStyle(finalStyle);
        } else {
            SectionStyleProperties defaultStyle = new SectionStyleProperties();
            defaultStyle.mergeWith(parentStyle);
            this.setResolvedStyle(defaultStyle);
        }

        // 2. The resolved style of this section is the new parent context for its children.
        StyleResolverContext childContext = context.createChildContext(this.getResolvedStyle());

        // 3. Delegate to child elements.
        if (elements != null) {
            for (Element element : elements) {
                element.resolveStyles(childContext);
            }
        }
    }
}
