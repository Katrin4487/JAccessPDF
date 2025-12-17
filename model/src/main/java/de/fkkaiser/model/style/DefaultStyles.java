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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.fkkaiser.model.style.builder.DefaultStylesBuilder;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

/**
 * Defines default element style mappings for standard HTML-like elements.
 * Maps ElementType enums to ElementStyle names.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
@JsonSerialize(using = DefaultStylesSerializer.class)
@JsonDeserialize(using = DefaultStylesDeserializer.class)
public record DefaultStyles(Map<StandardElementType, String> mappings) {

    /**
     * Creates an empty StyleDefaults.
     */
    public DefaultStyles() {
        this(new EnumMap<>(StandardElementType.class));
    }

    /**
     * Gets the default style name for a given element type.
     * @param type The element type.
     * @return Optional containing the style name, or empty if not defined.
     */
    public Optional<String> get(StandardElementType type) {
        return Optional.ofNullable(mappings.get(type));
    }

    /**
     * Entry point to create a new StyleDefaults using the Builder pattern.
     */
    public static DefaultStylesBuilder builder() {
        return new DefaultStylesBuilder();
    }

    public void set(StandardElementType type, String text) {
        mappings.put(type,text);
    }

}