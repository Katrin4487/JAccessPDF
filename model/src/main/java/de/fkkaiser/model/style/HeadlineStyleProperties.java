package de.fkkaiser.model.style;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Concrete style properties for a headline element.
 */
@JsonTypeName(StyleTargetTypes.HEADLINE)
public class HeadlineStyleProperties extends TextBlockStyleProperties {


    // --- Overrides ---

    @Override
    public void mergeWith(ElementStyleProperties base) {
        super.mergeWith(base);
    }

    @Override
    public HeadlineStyleProperties copy() {
        HeadlineStyleProperties newInstance = new HeadlineStyleProperties();
        applyPropertiesTo(newInstance);
        return newInstance;
    }
}
