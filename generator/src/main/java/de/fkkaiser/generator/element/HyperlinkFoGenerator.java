package de.fkkaiser.generator.element;


import de.fkkaiser.generator.GenerateUtils;
import de.fkkaiser.model.structure.Hyperlink;
import de.fkkaiser.model.structure.InlineElement;
import de.fkkaiser.model.style.StyleSheet;
import de.fkkaiser.model.style.TextRunStyleProperties;
import de.fkkaiser.model.style.TextStyle;

import java.util.Optional;

public class HyperlinkFoGenerator extends InlineElementFoGenerator {
    @Override
    public void generate(InlineElement element, StyleSheet styleSheet, StringBuilder builder) {
        Hyperlink link = (Hyperlink) element;

        builder.append("<fo:basic-link external-destination=\"")
                .append(GenerateUtils.escapeXml(link.getHref()))
                .append("\" fox:alt-text=\"")
                .append(GenerateUtils.escapeXml(link.getAltText()));

        TextRunStyleProperties style = link.getResolvedStyle();
        if(style != null) {
            Optional<TextStyle> fontStyleName = styleSheet.findFontStyleByName(style.getTextStyleName());
            fontStyleName.ifPresent(fontStyle -> builder.append("\" font-family=\"")
                    .append(GenerateUtils.escapeXml(fontStyle.fontFamilyName()))
                    .append("\" font-size=\"")
                    .append(GenerateUtils.escapeXml(fontStyle.fontSize()))
                    .append("\" font-weight=\"")
                    .append(GenerateUtils.escapeXml(fontStyle.fontWeight()))
                    .append("\" font-style=\"")
                    .append(GenerateUtils.escapeXml(fontStyle.fontStyle())));

            if(style.getTextColor() !=null){
                builder.append("\" color=\"")
                        .append(GenerateUtils.escapeXml(style.getTextColor()));
            }
            if(style.getBackgroundColor() !=null){
                builder.append("\" background-color=\"")
                        .append(GenerateUtils.escapeXml(style.getBackgroundColor()));
            }
            if(style.getTextDecoration() !=null){
                builder.append("\" text-decoration=\"")
                        .append(GenerateUtils.escapeXml(style.getTextDecoration()));
            }

            if(style.getBaselineShift() != null){
                builder.append("\" baseline-shift=\"")
                        .append(GenerateUtils.escapeXml(style.getBaselineShift()));
            }
        }
        builder.append("\">");
        builder.append(GenerateUtils.escapeXml(link.getText()));
        builder.append("</fo:basic-link>");
    }
}