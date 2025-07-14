package de.kaiser.model.style;

public class SectionStyleProperties extends ElementBlockStyleProperties {
    @Override
    public void mergeWith(ElementStyleProperties elemBase) {

    }

    /**
     * Creates a deep copy of this style properties object.
     * This is crucial to prevent style modifications on one element
     * from affecting another.
     *
     * @return A new instance with the same property values as this object.
     */
    @Override
    public SectionStyleProperties copy() {
        return null;
    }
}
