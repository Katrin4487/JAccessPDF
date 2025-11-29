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
import de.fkkaiser.generator.XslFoGenerator;
import de.fkkaiser.model.structure.TextBlock;
import de.fkkaiser.model.style.ParagraphStyleProperties;
import de.fkkaiser.model.style.TextBlockStyleProperties;

/**
 * Generates XSL-FO representation for Paragraph elements.
 */
public class ParagraphFoGenerator extends TextBlockFoGenerator {

    /**
     * Constructor for ParagraphFoGenerator.
     * @param mainGenerator The main generator for delegation.
     */
    public ParagraphFoGenerator(XslFoGenerator mainGenerator) {
        super(mainGenerator);
    }

    /**
     * Returns the accessibility role for a paragraph ("P").
     * @param textBlock The paragraph element.
     * @return The role string "P".
     */
    @Override
    protected String getRole(TextBlock textBlock) {
        return "P";
    }

    /**
     * Appends paragraph-specific attributes to the FO block.
     * @param builder The StringBuilder to append to.
     * @param style The resolved style properties.
     */
    @Override
    protected void appendSpecificAttributes(StringBuilder builder, TextBlockStyleProperties style) {
        // This method handles properties that only exist in ParagraphStyleProperties.
        if (style instanceof ParagraphStyleProperties pStyle) {
            if (pStyle.getTextIndent() != null) {
                builder.append(" text-indent=\"").append(GenerateUtils.escapeXml(pStyle.getTextIndent())).append("\"");
            }
            if (pStyle.getTextAlignLast() != null) {
                builder.append(" text-align-last=\"").append(GenerateUtils.escapeXml(pStyle.getTextAlignLast())).append("\"");
            }
            if (pStyle.isHyphenate()) {
                builder.append(" hyphenate=\"true\"");
            }
            if (pStyle.getLanguage() != null) {
                builder.append(" language=\"").append(GenerateUtils.escapeXml(pStyle.getLanguage())).append("\"");
            }
            // The 'orphans' and 'widows' properties control the minimum number of lines
            // of a paragraph to be left at the top or bottom of a page.
            if (pStyle.isOrphans()) {
                builder.append(" orphans=\"2\""); // Default to 2 if true
            }
            if (pStyle.isWidows()) {
                builder.append(" widows=\"2\""); // Default to 2 if true
            }
        }
    }
}
