package de.kaiser.model.style;

/**
 * Abstract base class for all style property sets.
 * It defines the essential contract for style inheritance and copying,
 * which all concrete style property classes must implement.
 */
public abstract class ElementStyleProperties {

    /**
     * Merges the properties from a base style into this style.
     * Typically, properties from the base style are only applied if they are
     * not already set in this style.
     *
     * @param base The base style to inherit from. Can be null.
     */
    public abstract void mergeWith(ElementStyleProperties base);

    /**
     * Creates a deep copy of this style properties object.
     * This is crucial to prevent style modifications on one element
     * from affecting another.
     *
     * @return A new instance with the same property values as this object.
     */
    public abstract ElementStyleProperties copy();

    public abstract String getFontStyleName();
}
