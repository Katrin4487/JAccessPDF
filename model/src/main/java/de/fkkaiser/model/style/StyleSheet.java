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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents the root stylesheet, containing font definitions, font styles,
 * element styles, and page layouts. This class is immutable.
 */
public record StyleSheet(
        @JsonProperty("text-styles") List<TextStyle> textStyles,
        @JsonProperty("element-styles") List<ElementStyle> elementStyles,
        @JsonProperty("page-master-styles") List<PageMasterStyle> pageMasterStyles
) {

    private static final Logger log = LoggerFactory.getLogger(StyleSheet.class);
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
     * Entry point to create a new StyleSheet using the Builder pattern.
     * @return A new StyleSheetBuilder instance.
     */
    public static StyleSheetBuilder builder() {
        return new StyleSheetBuilder();
    }

    /**
     * A fluent builder for creating an immutable StyleSheet object.
     */
    @SuppressWarnings("unused")
    public static class StyleSheetBuilder {

        // Internal mutable lists for aggregation
        private final List<TextStyle> textStyles = new ArrayList<>();
        private final List<ElementStyle> elementStyles = new ArrayList<>();
        private final List<PageMasterStyle> pageMasterStyles = new ArrayList<>();

        /**
         * Private constructor to enforce usage via StyleSheet.builder()
         */
        private StyleSheetBuilder() {
        }

        /**
         * Adds a TextStyle to the stylesheet.
         * @param style The TextStyle to add.
         * @return This builder instance for fluent chaining.
         */
        public StyleSheetBuilder addTextStyle(TextStyle style) {
            if (style != null) {
                this.textStyles.add(style);
            }
            return this;
        }

        /**
         * Adds an ElementStyle to the stylesheet.
         * @param style The ElementStyle to add.
         * @return This builder instance for fluent chaining.
         */
        public StyleSheetBuilder addElementStyle(ElementStyle style) {
            if (style != null) {
                this.elementStyles.add(style);
            }
            return this;
        }

        /**
         * Adds a PageMasterStyle to the stylesheet.
         * @param style The PageMasterStyle to add.
         * @return This builder instance for fluent chaining.
         */
        public StyleSheetBuilder addPageMasterStyle(PageMasterStyle style) {
            if (style != null) {
                this.pageMasterStyles.add(style);
            }
            return this;
        }

        /**
         * Builds the final, immutable StyleSheet object.
         * @return A new StyleSheet record.
         */
        public StyleSheet build() {
            // Use List.copyOf to create immutable lists for the record

            StringBuilder txtStyles = new StringBuilder();
            for (TextStyle textStyle : this.textStyles) {
                txtStyles.append(textStyle.name()).append(",");
            }
            log.debug("Text styles: {}",txtStyles);
            StringBuilder elmeStyles = new StringBuilder();
            for (ElementStyle elementStyle : this.elementStyles) {
                elmeStyles.append(elementStyle.name()).append(",");
            }

            log.debug("Element styles: {}",elmeStyles);

            return new StyleSheet(
                    List.copyOf(this.textStyles),
                    List.copyOf(this.elementStyles),
                    List.copyOf(this.pageMasterStyles)
            );
        }
    }
}
