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

/**
 * Concrete style properties for a headline element.
 * A headline typically represents a title or heading in a document.
 *
 * @author Katrin Kaiser
 * @version 1.0.1
 */
@Internal
@JsonTypeName(JsonPropertyName.HEADLINE)
public class HeadlineStyleProperties extends TextBlockStyleProperties {


    // --- Overrides ---

    /**
     * Creates a copy of the current HeadlineStyleProperties instance.
     * @return a new HeadlineStyleProperties instance with the same properties
     */
    @Internal
    @Override
    public HeadlineStyleProperties copy() {
        HeadlineStyleProperties newInstance = new HeadlineStyleProperties();
        applyPropertiesTo(newInstance);
        return newInstance;
    }
}
