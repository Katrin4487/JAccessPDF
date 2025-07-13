package de.kaiser.generator.element;

import de.kaiser.model.structure.InlineElement;
import de.kaiser.model.structure.TextRun;
import de.kaiser.model.style.StyleSheet;
import de.kaiser.model.style.TextRunStyleProperties;
import de.kaiser.model.style.TextStyle;

import java.util.Optional;

/**
 * Generates XSL-FO for a styled TextRun element.
 * It resolves the font-style-name to apply correct font attributes.
 */
public class TextRunFoGenerator extends InlineElementFoGenerator {

    @Override
    public void generate(InlineElement element, StyleSheet styleSheet, StringBuilder builder) {
        TextRun textRun = (TextRun) element;
        TextRunStyleProperties style = textRun.getResolvedStyle();

        if (style == null) {
            builder.append(escapeXml(textRun.getText()));
            return;
        }

        Optional<TextStyle> textStyleOpt = Optional.empty();
        if (style.getFontStyleName() != null) {
            textStyleOpt = styleSheet.findFontStyleByName(style.getFontStyleName());
        }

        // Check if there is any styling to apply. If not, don't create an empty <fo:inline>.
        boolean hasStyling = textStyleOpt.isPresent() || style.getTextColor() != null || style.getTextDecoration() != null;

        if (hasStyling) {
            builder.append("<fo:inline");

            // Apply attributes from the found TextStyle object
            textStyleOpt.ifPresent(ts -> {
                if (ts.fontFamilyName() != null) {
                    builder.append(" font-family=\"").append(escapeXml(ts.fontFamilyName())).append("\"");
                }
                if (ts.fontSize() != null) {
                    builder.append(" font-size=\"").append(escapeXml(ts.fontSize())).append("\"");
                }
                if (ts.fontWeight() != null) {
                    builder.append(" font-weight=\"").append(escapeXml(ts.fontWeight())).append("\"");
                }
                if (ts.fontStyle() != null) {
                    builder.append(" font-style=\"").append(escapeXml(ts.fontStyle().toLowerCase())).append("\"");
                }
            });

            if (style.getTextColor() != null) {
                builder.append(" color=\"").append(escapeXml(style.getTextColor())).append("\"");
            }
            if (style.getTextDecoration() != null) {
                builder.append(" text-decoration=\"").append(escapeXml(style.getTextDecoration())).append("\"");
            }

            if(style.getBaselineShift() != null){
                builder.append(" baseline-shift=\"").append(escapeXml(style.getBaselineShift())).append("\"");
            }

            builder.append(">");
            builder.append(escapeXml(textRun.getText()));
            builder.append("</fo:inline>");
        } else {
            // No specific styling found, just output the text.
            builder.append(escapeXml(textRun.getText()));
        }
    }
}
