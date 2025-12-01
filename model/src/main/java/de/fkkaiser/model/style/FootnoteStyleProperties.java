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
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;

/**
 * Concrete style properties for the body of a footnote element.
 * It inherits all properties from TextBlockStyleProperties.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
@PublicAPI
@JsonTypeName(StyleTargetTypes.FOOTNOTE) // You'll need to add FOOTNOTE to StyleTargetTypes
public class FootnoteStyleProperties extends TextBlockStyleProperties {


    /**
     * Merges the current style properties with the provided base properties.
     * @param base the base style properties to merge with
     */
    @Internal
    @Override
    public void mergeWith(ElementStyleProperties base) {
        super.mergeWith(base);

        // No specific properties to merge yet.
    }

    /**
     * Creates a copy of the current FootnoteStyleProperties instance.
     * @return a new FootnoteStyleProperties instance with the same properties
     */
    @Internal
    @Override
    public FootnoteStyleProperties copy() {
        FootnoteStyleProperties newInstance = new FootnoteStyleProperties();
        applyPropertiesTo(newInstance);
        return newInstance;
    }
}
