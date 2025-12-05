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
import de.fkkaiser.generator.XslFoGenerator;
import de.fkkaiser.model.style.ElementBlockStyleProperties;
import de.fkkaiser.model.style.PageBreakVariant;
import de.fkkaiser.model.style.StyleSheet;
import de.fkkaiser.generator.TagBuilder;

/**
 * Abstract base class for generators that produce block-level FO elements
 * with common block properties (padding, borders, spacing, breaks, etc.).
 *
 * <p>This class provides shared logic for Section, Part, and potentially other
 * block containers.</p>
 *
 * @author Katrin Kaiser
 * @version 1.2.1
 */
public abstract class BlockElementFoGenerator extends ElementFoGenerator {

    protected final XslFoGenerator mainGenerator;

    protected BlockElementFoGenerator(XslFoGenerator mainGenerator) {
        this.mainGenerator = mainGenerator;
    }

    /**
     * Appends common block-level attributes to the fo:block element.
     * These include spacing, indentation, padding, borders, background,
     * and page break controls.
     *
     * @param builder    The StringBuilder to append to
     * @param style      The block style properties
     * @param styleSheet The stylesheet for font resolution
     */
    protected void appendBlockAttributes(TagBuilder builder,
                                         ElementBlockStyleProperties style,
                                         StyleSheet styleSheet) {

        if (style == null) return;

        // Font style
        setFontStyle(styleSheet, style, builder);

        // Page break controls
        appendPageBreakAttributes(builder, style);

        // Keep controls
        if (Boolean.TRUE.equals(style.getKeepWithNext())) {
            builder.addAttribute(GenerateConst.KEEP_WITH_NEXT_WITHIN_PAGE, GenerateConst.ALWAYS);
        }

        //Spacing
        builder.addAttribute(GenerateConst.SPACE_BEFORE, style.getSpaceBefore());
        builder.addAttribute(GenerateConst.SPACE_AFTER, style.getSpaceAfter());

        // Indentation
        builder.addAttribute(GenerateConst.START_INDENT, style.getStartIndent());
        builder.addAttribute(GenerateConst.END_INDENT, style.getEndIndent());

        // Padding
        appendPaddingAttributes(builder, style);

        // Borders
        appendBorderAttributes(builder, style);

        // Background
        builder.addAttribute(GenerateConst.BACKGROUND_COLOR, style.getBackgroundColor());
    }

    /**
     * Appends page break attributes (break-before, break-after).
     */
    private void appendPageBreakAttributes(TagBuilder builder, ElementBlockStyleProperties style) {

        if(style==null) return;
        if(style.getBreakBefore() != null){
            builder.addAttribute(GenerateConst.BREAK_BEFORE, style.getBreakBefore().getFoValue());
        }
        if (style.getBreakAfter() != null && style.getBreakAfter() == PageBreakVariant.PAGE) {
            builder.addAttribute(GenerateConst.BREAK_AFTER, style.getBreakAfter().getFoValue());
        }
    }

    /**
     * Appends padding attributes (all sides).
     */
    private void appendPaddingAttributes(TagBuilder builder, ElementBlockStyleProperties style) {
        builder.addAttribute(GenerateConst.PADDING, style.getPadding());
        builder.addAttribute(GenerateConst.PADDING_TOP, style.getPaddingTop());
        builder.addAttribute(GenerateConst.PADDING_BOTTOM, style.getPaddingBottom());
        builder.addAttribute(GenerateConst.PADDING_LEFT, style.getPaddingLeft());
        builder.addAttribute(GenerateConst.PADDING_RIGHT, style.getPaddingRight());
    }

    /**
     * Appends border attributes (all sides).
     */
    private void appendBorderAttributes(TagBuilder builder, ElementBlockStyleProperties style) {

        builder.addAttribute(GenerateConst.BORDER, style.getBorder());
        builder.addAttribute(GenerateConst.BORDER_TOP, style.getBorderTop());
        builder.addAttribute(GenerateConst.BORDER_BOTTOM, style.getBorderBottom());
        builder.addAttribute(GenerateConst.BORDER_LEFT, style.getBorderLeft());
        builder.addAttribute(GenerateConst.BORDER_RIGHT, style.getBorderRight());
    }
}