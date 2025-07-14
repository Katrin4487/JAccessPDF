package de.kaiser.model.style;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Concrete style properties for the body of a footnote element.
 * It inherits all properties from TextBlockStyleProperties.
 */
@JsonTypeName(StyleTargetTypes.FOOTNOTE) // You'll need to add FOOTNOTE to StyleTargetTypes
public class FootnoteStyleProperties extends TextBlockStyleProperties {

    // This class could have footnote-specific properties in the future.
    // For now, it just provides a concrete implementation for styling.

    @Override
    public void mergeWith(ElementStyleProperties base) {
        // First, let the parent class merge all common properties.
        super.mergeWith(base);

        // Then, merge properties specific to this class (if any are added later).
        if (base instanceof FootnoteStyleProperties /* baseFootnote */) {
            // No specific properties to merge yet.
        }
    }

    @Override
    public FootnoteStyleProperties copy() {
        FootnoteStyleProperties newInstance = new FootnoteStyleProperties();
        // Use the helper method from the abstract parent to copy all properties.
        applyPropertiesTo(newInstance);
        return newInstance;
    }
}
