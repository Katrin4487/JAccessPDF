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
import de.fkkaiser.generator.GenerateUtils;
import de.fkkaiser.generator.TagBuilder;
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.structure.Footnote;
import de.fkkaiser.model.structure.InlineElement;
import de.fkkaiser.model.structure.TextBlock;
import de.fkkaiser.model.style.ElementBlockStyleProperties;
import de.fkkaiser.model.style.FootnoteStyleProperties;
import de.fkkaiser.model.style.StyleSheet;
import de.fkkaiser.generator.XslFoGenerator;
import de.fkkaiser.model.style.TextBlockStyleProperties;

/**
 * Generates XSL-FO for Footnote elements.
 *
 * @author Katrin Kaiser
 * @version 1.1.1
 */
public class FootnoteFoGenerator extends InlineElementFoGenerator {

    private static final String  FOOTNOTE_TAG = "footnote";
    private static final String FOOTNOTE_BODY_TAG = "footnote-body";

    private final XslFoGenerator mainGenerator;
    private final StyleApplier styleHelper;

    /**
     * Constructor for FootnoteFoGenerator.
     *
     * @param mainGenerator The main XSL-FO generator for delegating content generation.
     */
    @Internal
    public FootnoteFoGenerator(XslFoGenerator mainGenerator) {
        this.mainGenerator = mainGenerator;
        this.styleHelper = new StyleApplier(mainGenerator);
    }

    private static class StyleApplier extends TextBlockFoGenerator {
        public StyleApplier(XslFoGenerator mainGenerator) {
            super(mainGenerator);
        }

        public void applyStyles(TagBuilder builder, ElementBlockStyleProperties style, StyleSheet styleSheet) {
            super.appendBlockAttributes(builder, style, styleSheet);
        }

        @Override protected String getRole(TextBlock textBlock) { return null; }
        @Override protected void appendSpecificAttributes(TagBuilder builder, TextBlockStyleProperties style) { }
    }

    /**
     * Generates the XSL-FO string for a footnote element.
     * @param element    The inline element to be processed.
     * @param styleSheet The entire StyleSheet for accessing font information.
     * @param builder    The StringBuilder to which the generated string is appended.
     */
    @Override
    public void generate(InlineElement element, StyleSheet styleSheet, StringBuilder builder) {
        Footnote footnote = (Footnote) element;
        FootnoteStyleProperties styleProperties = footnote.getResolvedStyle();

        // <Note> tagging. We will create the accessible structure manually.
        TagBuilder footnoteBuilder = GenerateUtils.tagBuilder(FOOTNOTE_TAG)
                .addAttribute(GenerateConst.ROLE, GenerateConst.SPAN);

        // Inline marker for the footnote reference in the text
        TagBuilder inlineMarker = GenerateUtils.tagBuilder(GenerateConst.INLINE)
                .addAttribute(GenerateConst.FONT_SIZE, "8pt") // Customize Me!
                .addAttribute(GenerateConst.VERTICAL_ALIGN, "super")
                .addContent(footnote.getIndex());

        footnoteBuilder.addChild(inlineMarker);

        // Footnote body
        TagBuilder footnoteBodyBuilder = GenerateUtils.tagBuilder(FOOTNOTE_BODY_TAG);

        TagBuilder footnoteBlock = GenerateUtils.tagBuilder(GenerateConst.BLOCK)
                .addAttribute(GenerateConst.ID, footnote.getId());

        if (styleProperties != null) {
            styleHelper.applyStyles(footnoteBlock, styleProperties, styleSheet);
        }

        // Generate inline content for the footnote text
        if (footnote.getInlineElements() != null) {
            StringBuilder inlineContent = new StringBuilder();
            for (InlineElement inline : footnote.getInlineElements()) {
                mainGenerator.generateInlineElement(inline, styleSheet, inlineContent);
            }
            footnoteBlock.addNestedContent(inlineContent.toString());
        }

        footnoteBodyBuilder.addChild(footnoteBlock);
        footnoteBuilder.addChild(footnoteBodyBuilder);
        footnoteBuilder.buildInto(builder);
    }
}