/*
 * Copyright 2025 Katrin Kaiser
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.fkkaiser.model.style;

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
