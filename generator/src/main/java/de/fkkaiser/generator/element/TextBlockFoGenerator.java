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
import de.fkkaiser.model.style.ElementBlockStyleProperties;
import de.fkkaiser.model.style.StyleSheet;
import de.fkkaiser.model.style.TextBlockStyleProperties;
import java.util.List;

/**
 * Abstract base class for all block-level text elements (e.g., paragraphs, headlines).
 * It handles the common logic of generating an fo:block and its inline content.
 */
public abstract class TextBlockFoGenerator extends ElementFoGenerator {

    protected final XslFoGenerator mainGenerator;

    protected TextBlockFoGenerator(XslFoGenerator mainGenerator) {
        this.mainGenerator = mainGenerator;
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

        String headlineId = "";
        if (element instanceof Headline) {
            headlineId = " id=\"headline" + headlines.size() + "\"";
            headlines.add((Headline) element);
        }

        builder.append("      <fo:block")
                .append(headlineId)
                .append(" role=\"").append(getRole(textBlock)
                ).append("\"");

        // Append all style attributes
        appendCommonAttributes(builder, style, styleSheet);
        appendSpecificAttributes(builder, style);

        if (isExternalArtefact) {
            builder.append(" fox:content-type=\"external-artifact\"");
        }

        builder.append(">");

        // Delegate inline content generation to the main generator.
        if (textBlock.getInlineElements() != null) {
            for (InlineElement inlineElement : textBlock.getInlineElements()) {
                mainGenerator.generateInlineElement(inlineElement, styleSheet, builder);
            }
        }

        builder.append("      </fo:block>");
    }

    /**
     * Appends common attributes shared by all TextBlock elements.
     */
    void appendCommonAttributes(StringBuilder builder, ElementBlockStyleProperties style, StyleSheet styleSheet) {
        if (style == null) return;

        // Apply font styles
        setFontStyle(styleSheet, style, builder);
        // Apply other common text block properties
        if (style instanceof TextBlockStyleProperties textProps && textProps.getTextColor() != null) {
            builder.append(" color=\"").append(GenerateUtils.escapeXml(textProps.getTextColor())).append("\"");
        }
        if (style instanceof TextBlockStyleProperties textProps && textProps.getLineHeight() != null) {
            builder.append(" line-height=\"").append(GenerateUtils.escapeXml(textProps.getLineHeight())).append("\"");
        }
        if (style instanceof TextBlockStyleProperties textProps && textProps.getTextAlign() != null) {
            builder.append(" text-align=\"").append(GenerateUtils.escapeXml(textProps.getTextAlign())).append("\"");
        }
        if (style.getSpaceAfter() != null) {
            builder.append(" space-after=\"").append(GenerateUtils.escapeXml(style.getSpaceAfter())).append("\"");
        }
        if (style.getSpaceBefore() != null) {
            builder.append(" space-before=\"").append(GenerateUtils.escapeXml(style.getSpaceBefore())).append("\"");
        }
        if (style.getBackgroundColor() != null) {
            builder.append(" background-color=\"").append(GenerateUtils.escapeXml(style.getBackgroundColor())).append("\"");
        }
        if(style.getStartIndent() != null){
            builder.append(" start-indent=\"").append(GenerateUtils.escapeXml(style.getStartIndent())).append("\"");
        }
        if(style.getEndIndent() != null){
            builder.append(" end-indent=\"").append(GenerateUtils.escapeXml(style.getEndIndent())).append("\"");
        }
        if(style instanceof TextBlockStyleProperties textProps){
            if(textProps.getSpan() != null){
                builder.append(" span=\"").append(GenerateUtils.escapeXml(textProps.getSpan())).append("\"");
                builder.append(" space-before.conditionality=\"retain\"");
                builder.append(" space-after.conditionality=\"retain\"");
            }
            if(textProps.getLinefeedTreatment() !=null){
                builder.append(" linefeed-treatment=\"").append(GenerateUtils.escapeXml(textProps.getLinefeedTreatment())).append("\"");
            }
        }

    }

    /**
     * Gets the accessibility role for the specific text block (e.g., "P" or "H1").
     * Must be implemented by subclasses.
     */
    protected abstract String getRole(TextBlock textBlock);

    /**
     * Appends attributes that are specific to the concrete subclass (e.g., headline-specific styles).
     * To be implemented by subclasses.
     */
    protected abstract void appendSpecificAttributes(StringBuilder builder, TextBlockStyleProperties style);
}
