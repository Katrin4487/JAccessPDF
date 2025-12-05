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

import de.fkkaiser.generator.*;
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
 * @version 1.1.2
 */
public abstract class TextBlockFoGenerator extends BlockElementFoGenerator {

    private static final String PREFIX_HEADLINE_ID = "headline-";
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

        TagBuilder blockBuilder = GenerateUtils.tagBuilder(GenerateConst.BLOCK)
                .addAttribute(GenerateConst.ROLE, getRole(textBlock));

        // Generate unique ID for headlines
        if (element instanceof Headline headline) {
            String theId = PREFIX_HEADLINE_ID + UUID.randomUUID();
            blockBuilder.addAttribute(GenerateConst.ID, theId);
            headline.setId(theId);
            headlines.add(headline);
        }

        // Append common block attributes (from BlockElementFoGenerator)
        appendBlockAttributes(blockBuilder, style, styleSheet);

        // Append text-specific attributes
        appendTextBlockAttributes(blockBuilder, style);

        // Append element-specific attributes (e.g., headline level-specific styles)
        appendSpecificAttributes(blockBuilder, style);

        if (isExternalArtefact) {
            blockBuilder.addAttribute(GenerateConst.CONTENT_TYPE, GenerateConst.EXTERNAL_ARTIFACT);
        }

        // Generate inline content
        if (textBlock.getInlineElements() != null) {
            StringBuilder inlineContent = new StringBuilder();
            for (InlineElement inlineElement : textBlock.getInlineElements()) {
                mainGenerator.generateInlineElement(inlineElement, styleSheet, inlineContent);
            }
            blockBuilder.addNestedContent(inlineContent.toString());
        }

        blockBuilder.buildInto(builder);
    }

    /**
     * Appends text-specific attributes that are unique to TextBlock elements.
     * These properties are in addition to the common block properties.
     *
     * @param builder The StringBuilder to append to
     * @param style   The text block style properties
     */
    protected void appendTextBlockAttributes(TagBuilder builder, TextBlockStyleProperties style) {
        if (style == null) return;

        // Text color
        if (style.getTextColor() != null) {
            builder.addAttribute(GenerateConst.COLOR,style.getTextColor());
        }

        // Line height
        if (style.getLineHeight() != null) {
            builder.addAttribute(GenerateConst.LINE_HEIGHT,style.getLineHeight());
        }

        // Text alignment
        if (style.getTextAlign() != null) {
            builder.addAttribute(GenerateConst.TEXT_ALIGN,style.getTextAlign().toString());
        }

        // Span (for multi-column layouts)
        if (style.getSpan() != null) {
            builder.addAttribute(GenerateConst.SPAN_PARAM,style.getSpan().toString());
            builder.addAttribute(GenerateConst.SPACE_BEFORE_CONDITIONALITY,GenerateConst.RETAIN);
            builder.addAttribute(GenerateConst.SPACE_AFTER_CONDITIONALITY,GenerateConst.RETAIN);
        }

        // Linefeed treatment
        if (style.getLinefeedTreatment() != null) {
            builder.addAttribute(GenerateConst.LINEFEED_TREATMENT,style.getLinefeedTreatment().getValue());
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
    protected abstract void appendSpecificAttributes(TagBuilder builder, TextBlockStyleProperties style);
}