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
import de.fkkaiser.generator.TagBuilder;
import de.fkkaiser.model.structure.InlineElement;
import de.fkkaiser.model.structure.TextRun;
import de.fkkaiser.model.style.StyleSheet;
import de.fkkaiser.model.style.TextRunStyleProperties;
import de.fkkaiser.model.style.TextStyle;
import java.util.Optional;

/**
 * Generates XSL-FO markup for TextRun inline elements.
 * <p>
 * This generator handles the creation of fo:inline elements with appropriate
 * text styling attributes. It resolves text style references from the stylesheet
 * and applies font properties, colors, decorations, and baseline shifts as needed.
 * For unstyled text runs, it outputs plain text without wrapper elements.
 * </p>
 *
 * @author Katrin Kaiser
 * @version 1.0.1
 */
public class TextRunFoGenerator extends InlineElementFoGenerator {

    /**
     * Generates XSL-FO markup for a TextRun element.
     * <p>
     * If the text run has no styling, the text is output directly without a
     * fo:inline wrapper. Otherwise, a fo:inline element is created with all
     * applicable style attributes:
     * <ul>
     *   <li>Font properties (family, size, weight, style) from referenced text style</li>
     *   <li>Text color</li>
     *   <li>Text decoration (underline, line-through, etc.)</li>
     *   <li>Baseline shift (for superscript/subscript)</li>
     *   <li>Linefeed treatment</li>
     * </ul>
     * The text content is normalized before output to ensure proper whitespace handling.
     * </p>
     *
     * @param element the TextRun element to generate
     * @param styleSheet the stylesheet for resolving text style references
     * @param builder the StringBuilder to append XSL-FO markup to
     */
    @Override
    public void generate(InlineElement element, StyleSheet styleSheet, StringBuilder builder) {
        TextRun textRun = (TextRun) element;
        TextRunStyleProperties style = textRun.getResolvedStyle();

        // Output plain text if no styling is defined
        if (style == null) {
            builder.append(GenerateUtils.escapeXml(textRun.getText()));
            return;
        }

        // Resolve the referenced text style if a style name is specified
        Optional<TextStyle> textStyleOpt = Optional.empty();
        if (style.getTextStyleName() != null) {
            textStyleOpt = styleSheet.findFontStyleByName(style.getTextStyleName());
        }

        // Check if any styling attributes are present
        boolean hasStyling = textStyleOpt.isPresent()
                || style.getTextColor() != null
                || style.getTextDecoration() != null
                || style.getBaselineShift() != null
                || style.getLineFeedTreatment() != null;

        // If no styling, output plain text
        if (!hasStyling) {
            builder.append(normalizeText(textRun.getText()));
            return;
        }

        TagBuilder inlineBuilder = GenerateUtils.tagBuilder("inline");

        // Apply font properties from the resolved text style
        textStyleOpt.ifPresent(ts -> GenerateUtils.appendTextStyleTags(inlineBuilder, ts));

        // Apply direct style properties
        inlineBuilder
                .addAttribute("color", style.getTextColor())
                .addAttribute("text-decoration", style.getTextDecoration())
                .addAttribute("baseline-shift", style.getBaselineShift())
                .addAttribute("linefeed-treatment", style.getLineFeedTreatment() != null ? style.getLineFeedTreatment().getValue() : null);

        // Add normalized text content
        inlineBuilder.addNestedContent(normalizeText(textRun.getText()));

        inlineBuilder.buildInto(builder);
    }
}