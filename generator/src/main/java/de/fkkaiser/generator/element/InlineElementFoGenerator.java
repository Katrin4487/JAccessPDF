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
package de.fkkaiser.generator.element;

import de.fkkaiser.generator.GenerateUtils;
import de.fkkaiser.model.structure.InlineElement;
import de.fkkaiser.model.style.StyleSheet;

/**
 * Abstract base class for generating XSL-FO strings for specific inline elements.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
public abstract class InlineElementFoGenerator {

    /**
     * Generates the XSL-FO string for a specific inline element.
     * @param element    The inline element to be processed.
     * @param styleSheet The entire StyleSheet for accessing font information.
     * @param builder    The StringBuilder to which the generated string is appended.
     */
    public abstract void generate(InlineElement element, StyleSheet styleSheet, StringBuilder builder);

   
    /**
     * Helper to normalize texts in text elements.
     * Replaces '\n' with '\u2028'.
     * @param text text that should be normalized
     * @return normalized String
     */
    protected String normalizeText(String text) {
        if (text == null) return "";

        String normalized = text.replace("\n", "\u2028");

        return GenerateUtils.escapeXml(normalized);
    }
}
