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
import de.fkkaiser.model.style.PartStyleProperties;
import de.fkkaiser.model.style.StyleSheet;

import java.util.List;

/**
 * Generates XSL-FO for Part elements.
 *
 * @author Katrin Kaiser
 * @version 1.1.0
 */
@Internal
public class PartFoGenerator extends ElementFoGenerator {

    private final XslFoGenerator mainGenerator;

    /**
     * Constructor for PartFoGenerator.
     *
     * @param mainGenerator The main XSL-FO generator for delegating content generation.
     */
    @Internal
    public PartFoGenerator(XslFoGenerator mainGenerator) {
        this.mainGenerator = mainGenerator;
    }

    /**
     * Generates the XSL-FO representation of a Part element.
     *
     * @param element             The Part element to be processed.
     * @param styleSheet          The StyleSheet for style resolution.
     * @param builder             The StringBuilder to append the generated XSL-FO.
     * @param headlines           The list of headlines for bookmark generation.
     * @param resolver            The ImageResolver for handling images.
     * @param isExternalParagraph Flag indicating if the paragraph is external.
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

        builder.append("      <fo:block role=\"").append(part.getVariant().getPdfRole()).append("\"");
        appendPartAttributes(builder, style, styleSheet);
        builder.append(">");

        mainGenerator.generateBlockElements(part.getElements(), styleSheet, builder, headlines, resolver, false);

        builder.append("      </fo:block>");
    }

    private void appendPartAttributes(StringBuilder builder, PartStyleProperties style, StyleSheet styleSheet) {
        if (style == null) return;

        setFontStyle(styleSheet, style, builder);


        if (style.getPageBreakBefore() != null) {
            builder.append(" break-before=\"").append(GenerateUtils.escapeXml(style.getPageBreakBefore())).append("\"");
        }
    }
}

