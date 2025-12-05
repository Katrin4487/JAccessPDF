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

import de.fkkaiser.generator.GenerateConst;
import de.fkkaiser.generator.TagBuilder;
import de.fkkaiser.generator.XslFoGenerator;
import de.fkkaiser.model.structure.Headline;
import de.fkkaiser.model.structure.TextBlock;
import de.fkkaiser.model.style.HeadlineStyleProperties;
import de.fkkaiser.model.style.TextBlockStyleProperties;

/**
 * Generates XSL-FO for Headline elements.
 *
 * @author Katrin Kaiser
 * @version 1.1.2
 */
public class HeadlineFoGenerator extends TextBlockFoGenerator {


    /**
     * Constructor for HeadlineFoGenerator.
     * @param mainGenerator The main generator for delegation.
     */
    public HeadlineFoGenerator(XslFoGenerator mainGenerator) {
        super(mainGenerator);
    }

    /**
     * Returns the accessibility role for the headline (e.g., "H1", "H2").
     * @param textBlock The headline element.
     * @return The corresponding role string.
     */
    @Override
    protected String getRole(TextBlock textBlock) {
        if (textBlock instanceof Headline) {
            return GenerateConst.ROLE_HEADLINE + ((Headline) textBlock).getLevel();
        }
        return GenerateConst.ROLE_PARAGRAPH;
    }

    /**
     * Appends headline-specific attributes to the FO block.
     * @param builder The StringBuilder to append to.
     * @param style The resolved style properties.
     */
    @Override
    protected void appendSpecificAttributes(TagBuilder builder, TextBlockStyleProperties style) {
        // No specific style
    }

}
