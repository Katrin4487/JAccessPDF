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
import de.fkkaiser.model.style.ElementBlockStyleProperties;
import de.fkkaiser.model.style.PageBreakVariant;
import de.fkkaiser.model.style.StyleSheet;

/**
 * Abstract base class for generators that produce block-level FO elements
 * with common block properties (padding, borders, spacing, breaks, etc.).
 *
 * <p>This class provides shared logic for Section, Part, and potentially other
 * block containers.</p>
 *
 * @author Katrin Kaiser
 * @version 1.1.0
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
    protected void appendBlockAttributes(StringBuilder builder,
                                         ElementBlockStyleProperties style,
                                         StyleSheet styleSheet) {
        if (style == null) return;

        // Font style
        setFontStyle(styleSheet, style, builder);

        // Page break controls
        appendPageBreakAttributes(builder, style);

        // Keep controls
        if (Boolean.TRUE.equals(style.getKeepWithNext())) {
            builder.append(" keep-with-next.within-page=\"always\"");
        }

        // Spacing
        if (style.getSpaceBefore() != null) {
            builder.append(" space-before=\"")
                    .append(GenerateUtils.escapeXml(style.getSpaceBefore()))
                    .append("\"");
        }
        if (style.getSpaceAfter() != null) {
            builder.append(" space-after=\"")
                    .append(GenerateUtils.escapeXml(style.getSpaceAfter()))
                    .append("\"");
        }

        // Indentation
        if (style.getStartIndent() != null) {
            builder.append(" start-indent=\"")
                    .append(GenerateUtils.escapeXml(style.getStartIndent()))
                    .append("\"");
        }
        if (style.getEndIndent() != null) {
            builder.append(" end-indent=\"")
                    .append(GenerateUtils.escapeXml(style.getEndIndent()))
                    .append("\"");
        }

        // Padding
        appendPaddingAttributes(builder, style);

        // Borders
        appendBorderAttributes(builder, style);

        // Background
        if (style.getBackgroundColor() != null) {
            builder.append(" background-color=\"")
                    .append(GenerateUtils.escapeXml(style.getBackgroundColor()))
                    .append("\"");
        }
    }

    /**
     * Appends page break attributes (break-before, break-after).
     */
    private void appendPageBreakAttributes(StringBuilder builder, ElementBlockStyleProperties style) {
        if (style.getBreakBefore() != null) {
            builder.append(" break-before=\"")
                    .append(style.getBreakBefore().getFoValue())
            .append("\"");
        }
        if(style.getBreakAfter() != null && style.getBreakAfter() == PageBreakVariant.PAGE) {
            builder.append(" break-after=\"")
                    .append(style.getBreakAfter().getFoValue())
                    .append("\"");
        }
    }

    /**
     * Appends padding attributes (all sides).
     */
    private void appendPaddingAttributes(StringBuilder builder, ElementBlockStyleProperties style) {
        if (style.getPadding() != null) {
            builder.append(" padding=\"")
                    .append(GenerateUtils.escapeXml(style.getPadding()))
                    .append("\"");
        }
        if (style.getPaddingTop() != null) {
            builder.append(" padding-top=\"")
                    .append(GenerateUtils.escapeXml(style.getPaddingTop()))
                    .append("\"");
        }
        if (style.getPaddingBottom() != null) {
            builder.append(" padding-bottom=\"")
                    .append(GenerateUtils.escapeXml(style.getPaddingBottom()))
                    .append("\"");
        }
        if (style.getPaddingLeft() != null) {
            builder.append(" padding-left=\"")
                    .append(GenerateUtils.escapeXml(style.getPaddingLeft()))
                    .append("\"");
        }
        if (style.getPaddingRight() != null) {
            builder.append(" padding-right=\"")
                    .append(GenerateUtils.escapeXml(style.getPaddingRight()))
                    .append("\"");
        }
    }

    /**
     * Appends border attributes (all sides).
     */
    private void appendBorderAttributes(StringBuilder builder, ElementBlockStyleProperties style) {
        if (style.getBorder() != null) {
            builder.append(" border=\"")
                    .append(GenerateUtils.escapeXml(style.getBorder()))
                    .append("\"");
        }
        if (style.getBorderTop() != null) {
            builder.append(" border-top=\"")
                    .append(GenerateUtils.escapeXml(style.getBorderTop()))
                    .append("\"");
        }
        if (style.getBorderBottom() != null) {
            builder.append(" border-bottom=\"")
                    .append(GenerateUtils.escapeXml(style.getBorderBottom()))
                    .append("\"");
        }
        if (style.getBorderLeft() != null) {
            builder.append(" border-left=\"")
                    .append(GenerateUtils.escapeXml(style.getBorderLeft()))
                    .append("\"");
        }
        if (style.getBorderRight() != null) {
            builder.append(" border-right=\"")
                    .append(GenerateUtils.escapeXml(style.getBorderRight()))
                    .append("\"");
        }
    }
}