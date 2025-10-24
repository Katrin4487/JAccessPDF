package de.fkkaiser.model.style;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.function.Consumer;

/**
 * Concrete style properties for a section element.
 * A section can be used to group content and apply specific background,
 * padding, or borders to it.
 */
@JsonTypeName(StyleTargetTypes.SECTION)
public class SectionStyleProperties extends ElementBlockStyleProperties {


    // --- Overrides ---
    public void mergeWith(ElementBlockStyleProperties base) {
        super.mergeWith(base);

    }

    @Override
    public SectionStyleProperties copy() {
        SectionStyleProperties newInstance = new SectionStyleProperties();
        applyPropertiesTo(newInstance);
        return newInstance;
    }


    protected void applyPropertiesTo(SectionStyleProperties newInstance) {
        super.applyPropertiesTo(newInstance);

    }

    private <T> void mergeProperty(T current, T base, Consumer<T> setter) {
        if (current == null) {
            setter.accept(base);
        }
    }
}
