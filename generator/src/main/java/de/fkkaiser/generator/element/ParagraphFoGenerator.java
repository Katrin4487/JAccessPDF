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

import de.fkkaiser.generator.TagBuilder;
import de.fkkaiser.generator.XslFoGenerator;
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.structure.TextBlock;
import de.fkkaiser.model.style.ParagraphStyleProperties;
import de.fkkaiser.model.style.TextBlockStyleProperties;

/**
 * Generates XSL-FO representation for Paragraph elements.
 * @author Katrin Kaiser
 * @version 1.1.0
 */
@Internal
public class ParagraphFoGenerator extends TextBlockFoGenerator {

    /**
     * Constructor for ParagraphFoGenerator.
     * @param mainGenerator The main generator for delegation.
     */
    @Internal
    public ParagraphFoGenerator(XslFoGenerator mainGenerator) {
        super(mainGenerator);
    }

    /**
     * Returns the accessibility role for a paragraph ("P").
     * @param textBlock The paragraph element.
     * @return The role string "P".
     */
    @Internal
    @Override
    protected String getRole(TextBlock textBlock) {
        return "P";
    }

    /**
     * Appends paragraph-specific attributes to the FO block.
     * @param builder The TagBuilder to add attributes to.
     * @param style The resolved style properties.
     */
    @Internal
    @Override
    protected void appendSpecificAttributes(TagBuilder builder, TextBlockStyleProperties style) {
        // This method handles properties that only exist in ParagraphStyleProperties.
        if (style instanceof ParagraphStyleProperties pStyle) {
            builder
                    .addAttribute("text-indent", pStyle.getTextIndent())
                    .addAttribute("text-align-last", pStyle.getTextAlignLast() != null ? pStyle.getTextAlignLast().getValue() : null)
                    .addAttribute("language", pStyle.getLanguage());

            // Hyphenate is a boolean
            if (pStyle.isHyphenate()) {
                builder.addAttribute("hyphenate", "true");
            }

            // The 'orphans' and 'widows' properties control the minimum number of lines
            // of a paragraph to be left at the top or bottom of a page.
            if (pStyle.getOrphans() != null) {
                builder.addAttribute("orphans", String.valueOf(pStyle.getOrphans()));
            }
            if (pStyle.getWidows() != null) {
                builder.addAttribute("widows", String.valueOf(pStyle.getWidows()));
            }
        }
    }
}