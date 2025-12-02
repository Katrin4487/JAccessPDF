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
import de.fkkaiser.model.structure.Element;
import de.fkkaiser.model.structure.Headline;
import de.fkkaiser.model.structure.InlineElement;
import de.fkkaiser.model.structure.TextBlock;
import de.fkkaiser.model.style.StyleSheet;
import de.fkkaiser.model.style.TextBlockStyleProperties;

import java.util.List;
import java.util.UUID;

/**
 * Abstract base class for all block-level text elements (e.g., paragraphs, headlines).
 * Extends BlockElementFoGenerator to inherit common block properties,
 * and adds text-specific properties (color, line-height, text-align, etc.).
 *
 * @author Katrin Kaiser
 * @version 1.1.0
 */
public abstract class TextBlockFoGenerator extends BlockElementFoGenerator {

    protected TextBlockFoGenerator(XslFoGenerator mainGenerator) {
        super(mainGenerator);
    }

    @Override
    public void generate(Element element,
                         StyleSheet styleSheet,
                         StringBuilder builder,
                         List<Headline> headlines,
                         ImageResolver resolver,
                         boolean isExternalArtefact) {
        TextBlock textBlock = (TextBlock) element;
        TextBlockStyleProperties style = textBlock.getResolvedStyle();

        // Generate unique ID for headlines
        String headlineId = "";
        if (element instanceof Headline headline) {

            String theId =  "headline-" + UUID.randomUUID();
            headlineId = " id=\"" + theId + "\"";
            headline.setId(theId);
            headlines.add(headline);

        }

        builder.append("      <fo:block")
                .append(headlineId)
                .append(" role=\"").append(getRole(textBlock)).append("\"");


        // Append common block attributes (from BlockElementFoGenerator)
        appendBlockAttributes(builder, style, styleSheet);

        // Append text-specific attributes
        appendTextBlockAttributes(builder, style);

        // Append element-specific attributes (e.g., headline level-specific styles)
        appendSpecificAttributes(builder, style);

        if (isExternalArtefact) {
            builder.append(" fox:content-type=\"external-artifact\"");
        }

        builder.append(">");

        // Generate inline content
        if (textBlock.getInlineElements() != null) {
            for (InlineElement inlineElement : textBlock.getInlineElements()) {
                mainGenerator.generateInlineElement(inlineElement, styleSheet, builder);
            }
        }

        builder.append("      </fo:block>");
    }

    /**
     * Appends text-specific attributes that are unique to TextBlock elements.
     * These properties are in addition to the common block properties.
     *
     * @param builder The StringBuilder to append to
     * @param style   The text block style properties
     */
    protected void appendTextBlockAttributes(StringBuilder builder, TextBlockStyleProperties style) {
        if (style == null) return;

        // Text color
        if (style.getTextColor() != null) {
            builder.append(" color=\"")
                    .append(GenerateUtils.escapeXml(style.getTextColor()))
                    .append("\"");
        }

        // Line height
        if (style.getLineHeight() != null) {
            builder.append(" line-height=\"")
                    .append(GenerateUtils.escapeXml(style.getLineHeight()))
                    .append("\"");
        }

        // Text alignment
        if (style.getTextAlign() != null) {
            builder.append(" text-align=\"")
                    .append(GenerateUtils.escapeXml(style.getTextAlign().getValue()))
                    .append("\"");
        }

        // Span (for multi-column layouts)
        if (style.getSpan() != null) {
            builder.append(" span=\"")
                    .append(GenerateUtils.escapeXml(style.getSpan().getValue()))
                    .append("\"");
            builder.append(" space-before.conditionality=\"retain\"");
            builder.append(" space-after.conditionality=\"retain\"");
        }

        // Linefeed treatment
        if (style.getLinefeedTreatment() != null) {
            builder.append(" linefeed-treatment=\"")
                    .append(GenerateUtils.escapeXml(style.getLinefeedTreatment().getValue()))
                    .append("\"");
        }
    }

    /**
     * Gets the accessibility role for the specific text block (e.g., "P" or "H1").
     * Must be implemented by subclasses.
     *
     * @param textBlock The text block element
     * @return The PDF/UA role string
     */
    protected abstract String getRole(TextBlock textBlock);

    /**
     * Appends attributes that are specific to the concrete subclass
     * (e.g., headline-specific styles).
     * To be implemented by subclasses.
     *
     * @param builder The StringBuilder to append to
     * @param style   The text block style properties
     */
    protected abstract void appendSpecificAttributes(StringBuilder builder, TextBlockStyleProperties style);
}