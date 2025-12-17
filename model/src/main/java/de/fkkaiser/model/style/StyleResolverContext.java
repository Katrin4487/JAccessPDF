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

import java.util.Map;

/**
 * A context object that holds the state during the recursive style resolution process.
 * It is immutable; a new context is created for each level of the element tree.
 *
 * @param styleSheet       The complete stylesheet with all styles and defaults.
 * @param styleMap         The map of all available named styles (for quick lookup).
 * @param parentBlockStyle Holds the style of the direct parent element. Can be null for top-level elements.
 * @author Katrin Kaiser
 * @version 1.2.0
 */
public record StyleResolverContext(
        StyleSheet styleSheet,
        Map<String, ElementStyle> styleMap,
        ElementBlockStyleProperties parentBlockStyle
) {

    /**
     * The main constructor for creating a style context.
     *
     * @param styleSheet       The complete stylesheet.
     * @param styleMap         The map of all available named styles.
     * @param parentBlockStyle The resolved style of the parent element.
     */
    public StyleResolverContext {
    }

    /**
     * Creates a new context for child elements.
     * This new context carries over the stylesheet and global style map 
     * but sets a new parent style.
     *
     * @param newParentBlockStyle The resolved style of the new parent element.
     * @return A new StyleResolverContext instance.
     */
    public StyleResolverContext createChildContext(ElementBlockStyleProperties newParentBlockStyle) {
        return new StyleResolverContext(this.styleSheet, this.styleMap, newParentBlockStyle);
    }
}