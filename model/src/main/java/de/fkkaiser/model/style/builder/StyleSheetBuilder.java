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
import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.style.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A fluent builder for creating an immutable StyleSheet object.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
@PublicAPI
public class StyleSheetBuilder {

    private static final Logger log = LoggerFactory.getLogger(StyleSheetBuilder.class);

    // Internal mutable lists for aggregation
    private final List<TextStyle> textStyles = new ArrayList<>();
    private final List<ElementStyle> elementStyles = new ArrayList<>();
    private final List<PageMasterStyle> pageMasterStyles = new ArrayList<>();
    private DefaultStyles defaults;

    /**
     * Private constructor to enforce usage via StyleSheet.builder()
     */
    @Internal
    public StyleSheetBuilder() {
    }

    /**
     * Adds a TextStyle to the stylesheet.
     *
     * @param style The TextStyle to add.
     * @return This builder instance for fluent chaining.
     */
    @PublicAPI
    public StyleSheetBuilder addTextStyle(TextStyle style) {
        if (style != null) {
            this.textStyles.add(style);
        }
        return this;
    }

    /**
     * Adds an ElementStyle to the stylesheet.
     *
     * @param style The ElementStyle to add.
     * @return This builder instance for fluent chaining.
     */
    @PublicAPI
    public StyleSheetBuilder addElementStyle(ElementStyle style) {
        if (style != null) {
            this.elementStyles.add(style);
        }
        return this;
    }

    /**
     * Adds a PageMasterStyle to the stylesheet.
     *
     * @param style The PageMasterStyle to add.
     * @return This builder instance for fluent chaining.
     */
    @PublicAPI
    public StyleSheetBuilder addPageMasterStyle(PageMasterStyle style) {
        if (style != null) {
            this.pageMasterStyles.add(style);
        }
        return this;
    }

    /**
     * Sets the default style mappings.
     *
     * @param defaults The DefaultStyles to use.
     * @return This builder instance for fluent chaining.
     */
    @PublicAPI
    public StyleSheetBuilder withDefaults(DefaultStyles defaults) {
        this.defaults = defaults;
        return this;
    }

    /**
     * Sets a single default mapping.
     *
     * @param type      The element type.
     * @param styleName The element style name.
     * @return This builder instance for fluent chaining.
     */
    @PublicAPI
    public StyleSheetBuilder setDefault(StandardElementType type, String styleName) {
        if (this.defaults == null) {
            this.defaults = DefaultStyles.builder().build();
        }
        // Da DefaultStyles immutable ist, müssen wir neu bauen
        DefaultStylesBuilder builder = DefaultStyles.builder();
        this.defaults.mappings().forEach(builder::set);
        builder.set(type, styleName);
        this.defaults = builder.build();
        return this;
    }

    /**
     * Builds the final, immutable StyleSheet object.
     *
     * @return A new StyleSheet record.
     */
    @PublicAPI
    public StyleSheet build() {

        StringBuilder txtStyles = new StringBuilder();
        for (TextStyle textStyle : this.textStyles) {
            txtStyles.append(textStyle.name()).append(",");
        }
        log.debug("Text styles: {}", txtStyles);
        StringBuilder elmeStyles = new StringBuilder();
        for (ElementStyle elementStyle : this.elementStyles) {
            elmeStyles.append(elementStyle.name()).append(",");
        }

        log.debug("Element styles: {}", elmeStyles);

        return new StyleSheet(
                List.copyOf(this.textStyles),
                List.copyOf(this.elementStyles),
                List.copyOf(this.pageMasterStyles),
                this.defaults
        );
    }

    /**
     * Validates the stylesheet contents.
     * Currently, it validates each PageMasterStyle.
     *
     * @throws IllegalStateException if validation fails.
     * @throws NullPointerException  if required fields are null.
     */
    final
    @Internal
    public void validate() {
        Objects.requireNonNull(this.pageMasterStyles, "pageMasterStyles must not be null");
        Objects.requireNonNull(this.textStyles, "textStyles must not be null");
        if (this.pageMasterStyles.isEmpty()) {
            log.error("No page master styles defined in the stylesheet.");
            throw new IllegalStateException("No page master styles defined in the stylesheet.");
        }
        if (this.textStyles.isEmpty()) {
            log.error("No text styles defined in the stylesheet.");
            throw new IllegalStateException("No text styles defined in the stylesheet.");
        }
        this.pageMasterStyles.forEach(PageMasterStyle::validate);
        this.elementStyles.forEach(ElementStyle::validate);
    }
}





