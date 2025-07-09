package de.kaiser.model.style;

public abstract class ElementStyleProperties {


    public abstract void mergeWith(ElementStyleProperties elemBase);

    /**
     * Creates a deep copy of this style properties object.
     * This is crucial to prevent style modifications on one element
     * from affecting another.
     *
     * @return A new instance with the same property values as this object.
     */
    public abstract ElementStyleProperties copy();
}
