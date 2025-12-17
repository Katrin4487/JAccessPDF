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

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fkkaiser.model.JsonPropertyName;
import de.fkkaiser.model.style.builder.StyleSheetBuilder;
import java.util.List;
import java.util.Optional;

/**
 * Represents the root stylesheet, containing font definitions, font styles,
 * element styles, and page layouts. This class is immutable.
 *
 * @author Katrin Kaiser
 * @version 1.0.1
 *
 */
public record StyleSheet(
        @JsonProperty(JsonPropertyName.TEXT_STYLES) List<TextStyle> textStyles,
        @JsonProperty(JsonPropertyName.ELEMENT_STYLES) List<ElementStyle> elementStyles,
        @JsonProperty(JsonPropertyName.PAGE_MASTER_STYLES) List<PageMasterStyle> pageMasterStyles,
        @JsonProperty(JsonPropertyName.DEFAULTS) DefaultStyles defaults
) {

    /**
     * Finds a text style by its name.
     *
     * @param name The name of the text style (e.g., "regular-text").
     * @return An Optional containing the found TextStyle, or empty if not found.
     */
    public Optional<TextStyle> findFontStyleByName(String name) {
        if (this.textStyles == null || name == null) {
            return Optional.empty();
        }
        return this.textStyles.stream()
                .filter(fs -> name.equals(fs.name()))
                .findFirst();
    }

    /**
     * Finds an element style by name, considering defaults and naming conventions.
     *
     * @param elementType The type of element (e.g., H1, P).
     * @param explicitStyleName Optional explicit style name, or null to use defaults.
     * @return Optional containing the found ElementStyle, or empty if not found.
     */
    public Optional<ElementStyle> findElementStyle(StandardElementType elementType, String explicitStyleName) {
        if (explicitStyleName != null) {
            Optional<ElementStyle> explicit = findElementStyleByName(explicitStyleName);
            if (explicit.isPresent()) {
                return explicit;
            }
        }

        if (defaults != null) {
            Optional<String> defaultStyleName = defaults.get(elementType);
            if (defaultStyleName.isPresent()) {
                Optional<ElementStyle> style = findElementStyleByName(defaultStyleName.get());
                if (style.isPresent()) {
                    return style;
                }
            }
        }

        // 3. Check naming convention (e.g., "h1-default")
        String conventionName = elementType.getDefaultStyleName();
        return findElementStyleByName(conventionName);
    }

    /**
     * Finds an element style by its exact name.
     * @param name The name of the element style.
     * @return Optional containing the found ElementStyle, or empty if not found.
     */
    public Optional<ElementStyle> findElementStyleByName(String name) {
        if (this.elementStyles == null || name == null) {
            return Optional.empty();
        }
        return this.elementStyles.stream()
                .filter(es -> name.equals(es.name()))
                .findFirst();
    }

    /**
     * Entry point to create a new StyleSheet using the Builder pattern.
     *
     * @return A new StyleSheetBuilder instance.
     */
    public static StyleSheetBuilder builder() {
        return new StyleSheetBuilder();
    }

}