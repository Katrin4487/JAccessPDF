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
import de.fkkaiser.model.JsonPropertyName;
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;

import java.util.function.Consumer;

/**
 * Concrete style properties for a part element.
 * A part typically represents a major division in a document,
 * such as a book part or volume.
 *
 * @author Katrin Kaiser
 * @version 1.0.1
 */
@PublicAPI
@JsonTypeName(JsonPropertyName.PART)
public class PartStyleProperties extends ElementBlockStyleProperties {


    /**
     * Creates a copy of the current PartStyleProperties instance.
     * @return a new PartStyleProperties instance with the same properties
     */
    @Internal
    @Override
    public PartStyleProperties copy() {
        PartStyleProperties copy = new PartStyleProperties();
        applyPropertiesTo(copy);
        return copy;
    }

    /**
     * Applies the current properties to the target ElementBlockStyleProperties instance.
     * @param target The object to apply the properties to.
     */
    @Internal
    @Override
    public void applyPropertiesTo(ElementBlockStyleProperties target) {
        super.applyPropertiesTo(target);
    }

}