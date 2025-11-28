package de.fkkaiser.generator.element;

import de.fkkaiser.generator.GenerateUtils;
import de.fkkaiser.model.structure.InlineElement;
import de.fkkaiser.model.structure.TextRun;
import de.fkkaiser.model.style.StyleSheet;
import de.fkkaiser.model.style.TextRunStyleProperties;
import de.fkkaiser.model.style.TextStyle;
import java.util.Optional;

/**
 * Generates XSL-FO markup for TextRun inline elements.
 * <p>
 * This generator handles the creation of fo:inline elements with appropriate
 * text styling attributes. It resolves text style references from the stylesheet
 * and applies font properties, colors, decorations, and baseline shifts as needed.
 * For unstyled text runs, it outputs plain text without wrapper elements.
 * </p>
 */
public class TextRunFoGenerator extends InlineElementFoGenerator {


    /**
     * Generates XSL-FO markup for a TextRun element.
     * <p>
     * If the text run has no styling, the text is output directly without a
     * fo:inline wrapper. Otherwise, a fo:inline element is created with all
     * applicable style attributes:
     * <ul>
     *   <li>Font properties (family, size, weight, style) from referenced text style</li>
     *   <li>Text color</li>
     *   <li>Text decoration (underline, line-through, etc.)</li>
     *   <li>Baseline shift (for superscript/subscript)</li>
     *   <li>Linefeed treatment</li>
     * </ul>
     * The text content is normalized before output to ensure proper whitespace handling.
     * </p>
     *
     * @param element the TextRun element to generate
     * @param styleSheet the stylesheet for resolving text style references
     * @param builder the StringBuilder to append XSL-FO markup to
     */
    @Override
    public void generate(InlineElement element, StyleSheet styleSheet, StringBuilder builder) {
        TextRun textRun = (TextRun) element;
        TextRunStyleProperties style = textRun.getResolvedStyle();

        // Output plain text if no styling is defined
        if (style == null) {
            builder.append(GenerateUtils.escapeXml(textRun.getText()));
            return;
        }

        // Resolve the referenced text style if a style name is specified
        Optional<TextStyle> textStyleOpt = Optional.empty();
        if (style.getTextStyleName() != null) {
            textStyleOpt = styleSheet.findFontStyleByName(style.getTextStyleName());
        }

        // Check if any styling attributes are present
        boolean hasStyling = textStyleOpt.isPresent()
                || style.getTextColor() != null
                || style.getTextDecoration() != null
                || style.getBaselineShift() != null;

        builder.append("<fo:inline");

        if (hasStyling) {
            // Apply font properties from the resolved text style
            textStyleOpt.ifPresent(ts -> {
                if (ts.fontFamilyName() != null) {
                    builder.append(" font-family=\"").append(GenerateUtils.escapeXml(ts.fontFamilyName())).append("\"");
                }
                if (ts.fontSize() != null) {
                    builder.append(" font-size=\"").append(GenerateUtils.escapeXml(ts.fontSize())).append("\"");
                }
                if (ts.fontWeight() != null) {
                    builder.append(" font-weight=\"").append(GenerateUtils.escapeXml(ts.fontWeight())).append("\"");
                }
                if (ts.fontStyle() != null) {
                    builder.append(" font-style=\"").append(GenerateUtils.escapeXml(ts.fontStyle().toLowerCase())).append("\"");
                }
            });

            // Apply direct style properties
            if (style.getTextColor() != null) {
                builder.append(" color=\"").append(GenerateUtils.escapeXml(style.getTextColor())).append("\"");
            }
            if (style.getTextDecoration() != null) {
                builder.append(" text-decoration=\"").append(GenerateUtils.escapeXml(style.getTextDecoration())).append("\"");
            }

            // Baseline shift for superscript/subscript positioning
            if (style.getBaselineShift() != null) {
                builder.append(" baseline-shift=\"").append(GenerateUtils.escapeXml(style.getBaselineShift())).append("\"");
            }

            // Linefeed treatment controls how line breaks are handled
            if (style.getLineFeedTreatment() != null) {
                builder.append(" linefeed-treatment=\"").append(GenerateUtils.escapeXml(style.getLineFeedTreatment())).append("\"");
            }
        }

        builder.append(">");

        // Normalize text to ensure proper whitespace handling in XSL-FO
        builder.append(normalizeText(textRun.getText()));

        builder.append("</fo:inline>");
    }
}