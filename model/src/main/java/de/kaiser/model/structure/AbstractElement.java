package de.kaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.kaiser.model.style.StyleResolverContext;

/**
 * An abstract base class for all Element implementations.
 * It provides common fields like styleClass and variant,
 * reducing code duplication in concrete element classes.
 */
public abstract class AbstractElement implements Element {

    @JsonProperty("style-class")
    protected String styleClass;

    @JsonProperty("variant")
    protected String variant;

    /**
     * A no-argument constructor is needed for Jackson deserialization.
     * Jackson will create an instance of the class and then populate the fields.
     */
    public AbstractElement() {
    }

    /**
     * Creates a new instance of AbstractElement with the specified style class and variant.
     *
     * @param styleClass The style class to apply to the element.
     * @param variant The semantic variant of the element.
     */
    public AbstractElement(String styleClass, String variant) {
        this.styleClass = styleClass;
        this.variant = variant;
    }

    @Override
    public abstract String getType();

    @Override
    public String getStyleClass() {
        return this.styleClass;
    }

    public String getVariant() {
        return this.variant;
    }

    // The 'resolvedStyle' field and its methods are left to the concrete subclasses
    // (e.g., TextBlock, Table) because their style property types differ.

    /**
     * Resolves the styles for the given element using the provided style resolver context.
     * This method must be implemented by all concrete subclasses.
     *
     * @param context The style resolver context containing the style map and parent style.
     */
    @Override
    public abstract void resolveStyles(StyleResolverContext context);
}
