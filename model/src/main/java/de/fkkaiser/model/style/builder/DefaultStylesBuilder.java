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
package de.fkkaiser.model.style.builder;

import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.style.DefaultStyles;
import de.fkkaiser.model.style.StandardElementType;

import java.util.EnumMap;
import java.util.Map;

/**
 * Builder for creating StyleDefaults.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
@Internal
public class DefaultStylesBuilder {
    private final Map<StandardElementType, String> mappings = new EnumMap<>(StandardElementType.class);

    /**
     * Initializes a new DefaultStylesBuilder.
     */
    @Internal
    public DefaultStylesBuilder() {
    }

    /**
     * Sets the default style for an element type.
     *
     * @param type      The element type.
     * @param styleName The name of the ElementStyle to use.
     * @return This builder for fluent chaining.
     */
    public DefaultStylesBuilder set(StandardElementType type, String styleName) {
        if (type != null && styleName != null) {
            this.mappings.put(type, styleName);
        }
        return this;
    }


    /**
     * Builds the immutable StyleDefaults.
     */
    public DefaultStyles build() {
        return new DefaultStyles(Map.copyOf(mappings));
    }

}