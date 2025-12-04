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
import de.fkkaiser.model.annotation.Internal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        @JsonProperty(JsonPropertyName.PAGE_MASTER_STYLES) List<PageMasterStyle> pageMasterStyles
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

    /**
     * Validates the stylesheet contents.
     * Currently, it validates each PageMasterStyle.
     *
     * @throws IllegalStateException if validation fails.
     * @throws NullPointerException if required fields are null.
     */
    @Internal
    public void validate(){
        Objects.requireNonNull(this.pageMasterStyles, "pageMasterStyles must not be null");
        Objects.requireNonNull(this.textStyles, "textStyles must not be null");
        if(this.pageMasterStyles.isEmpty()){
            log.error("No page master styles defined in the stylesheet.");
            throw new IllegalStateException("No page master styles defined in the stylesheet.");
        }
        if(this.textStyles.isEmpty()){
            log.error("No text styles defined in the stylesheet.");
            throw new IllegalStateException("No text styles defined in the stylesheet.");
        }
        this.pageMasterStyles.forEach(PageMasterStyle::validate);
        this.elementStyles.forEach(ElementStyle::validate);
    }

}
