package de.fkkaiser.generator.element;

import de.fkkaiser.model.structure.InlineElement;
import de.fkkaiser.model.structure.TextRun;
import de.fkkaiser.model.style.StyleSheet;
import de.fkkaiser.model.style.TextRunStyleProperties;
import de.fkkaiser.model.style.TextStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Generates XSL-FO for a styled TextRun element.
 * It resolves the font-style-name to apply correct font attributes.
 */
public class TextRunFoGenerator extends InlineElementFoGenerator {

    private static final Logger log = LoggerFactory.getLogger(TextRunFoGenerator.class);

    @Override
    public void generate(InlineElement element, StyleSheet styleSheet, StringBuilder builder) {
        TextRun textRun = (TextRun) element;
        TextRunStyleProperties style = textRun.getResolvedStyle();

        if (style == null) {
            builder.append(escapeXml(textRun.getText()));
            return;
        }

        Optional<TextStyle> textStyleOpt = Optional.empty();
        if (style.getTextStyleName() != null) {
            textStyleOpt = styleSheet.findFontStyleByName(style.getTextStyleName());
        }

        boolean hasStyling = textStyleOpt.isPresent()
                || style.getTextColor() != null
                || style.getTextDecoration() != null
                || style.getBaselineShift() != null;

        builder.append("<fo:inline");

        if (hasStyling) {
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

            log.debug("Text-Color -->{}",style.getTextColor());
            if (style.getTextColor() != null) {
                builder.append(" color=\"").append(escapeXml(style.getTextColor())).append("\"");
            }
            if (style.getTextDecoration() != null) {
                builder.append(" text-decoration=\"").append(escapeXml(style.getTextDecoration())).append("\"");
            }

            if (style.getBaselineShift() != null) {
                builder.append(" baseline-shift=\"").append(escapeXml(style.getBaselineShift())).append("\"");
            }

            if (style.getLineFeedTreatment() != null) {
                builder.append(" linefeed-treatment=\"").append(escapeXml(style.getLineFeedTreatment())).append("\"");
            }


        }
        builder.append(">");
        builder.append(normalizeText(textRun.getText()));

        builder.append("</fo:inline>");
        log.debug("Generated: {}", builder);
    }
}
