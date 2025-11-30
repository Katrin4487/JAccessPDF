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
import de.fkkaiser.generator.ImageResolver;
import de.fkkaiser.generator.XslFoGenerator;
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.structure.Element;
import de.fkkaiser.model.structure.Headline;
import de.fkkaiser.model.structure.Part;
import de.fkkaiser.model.style.PageBreakVariant;
import de.fkkaiser.model.style.PartStyleProperties;
import de.fkkaiser.model.style.StyleSheet;

import java.util.List;


/**
 * Generates the XSL-FO structure for a Section element.
 *
 * @author Katrin Kaiser
 * @version 1.1.0
 */
@Internal
public class PartFoGenerator extends BlockElementFoGenerator {

    /**
     * Constructor for PartFoGenerator.
     * @param mainGenerator The main XSL-FO generator for delegating content generation.
     */
    @Internal
    public PartFoGenerator(XslFoGenerator mainGenerator) {
        super(mainGenerator);
    }

    /**
     * Generates the XSL-FO string for a part element.
     * @param element The element to be processed.
     * @param styleSheet The entire StyleSheet for accessing, for example, Font information.
     * @param builder The StringBuilder to which the generated string is appended.
     * @param headlines List of headlines for TOC generation.
     * @param resolver Image resolver for handling images.
     * @param isExternalParagraph Indicates if the paragraph is external.
     */
    @Internal
    @Override
    public void generate(Element element,
                         StyleSheet styleSheet,
                         StringBuilder builder,
                         List<Headline> headlines,
                         ImageResolver resolver,
                         boolean isExternalParagraph) {
        Part part = (Part) element;
        PartStyleProperties style = part.getResolvedStyle();

        builder.append("      <fo:block role=\"")
                .append(part.getVariant().getPdfRole())
                .append("\"");

        // Common block attributes from parent class
        appendBlockAttributes(builder, style, styleSheet);

        builder.append(">");

        mainGenerator.generateBlockElements(part.getElements(), styleSheet, builder, headlines, resolver, false);

        builder.append("      </fo:block>");
    }

    private void appendPartSpecificAttributes(StringBuilder builder,PartStyleProperties style){
        // This method handles properties that only exist in PartStyleProperties.
        if (style != null) {
            if (style.getBreakBefore() != null && style.getBreakBefore() != PageBreakVariant.AUTO) {
                builder.append(" break-before=\"")
                        .append(GenerateUtils.escapeXml(style.getBreakBefore().getFoValue()))
                        .append("\"");
            }

            if (style.getBreakAfter() != null && style.getBreakAfter() != PageBreakVariant.AUTO) {
                builder.append(" break-after=\"")
                        .append(GenerateUtils.escapeXml(style.getBreakAfter().getFoValue()))
                        .append("\"");
            }
        }
    }
}