package de.fkkaiser.model.style;

public class LayoutTableStyleProperties extends ElementBlockStyleProperties{

    /**
     * Creates a deep copy of this style properties object.
     * This is crucial to prevent style modifications on one element
     * from affecting another.
     *
     * @return A new instance with the same property values as this object.
     */
    @Override
    public LayoutTableStyleProperties copy() {
        LayoutTableStyleProperties newInstance = new LayoutTableStyleProperties();
         applyPropertiesTo(newInstance);
        // ToDO Hier künftige LayoutTable-spezifische Properties kopieren
        return newInstance;
    }

    @Override
    public void mergeWith(ElementStyleProperties elemBase) {
        super.mergeWith(elemBase); // WICHTIG: Den Parent-Merge aufrufen!
        if (elemBase instanceof LayoutTableStyleProperties baseLayout) {
            // ToDo Hier künftige LayoutTable-spezifische Properties mergen
        }
    }
}
