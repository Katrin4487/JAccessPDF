package de.kaiser.generator.element;


import de.kaiser.model.structure.Hyperlink;
import de.kaiser.model.structure.InlineElement;
import de.kaiser.model.style.StyleSheet;
import de.kaiser.model.style.TextRunStyleProperties;
import de.kaiser.model.style.TextStyle;

import java.util.Optional;

public class HyperlinkFoGenerator extends InlineElementFoGenerator {
    @Override
    public void generate(InlineElement element, StyleSheet styleSheet, StringBuilder builder) {
        Hyperlink link = (Hyperlink) element;

        builder.append("<fo:basic-link external-destination=\"")
                .append(escapeXml(link.getHref()))
                .append("\" fox:alt-text=\"")
                .append(escapeXml(link.getAltText()));

        TextRunStyleProperties style = link.getResolvedStyle();
        if(style != null) {
            Optional<TextStyle> fontStyleName = styleSheet.findFontStyleByName(style.getFontStyleName());
            if (fontStyleName.isPresent()) {
                TextStyle fontStyle = fontStyleName.get();
                builder.append("\" font-family=\"")
                        .append(escapeXml(fontStyle.fontFamilyName()))
                        .append("\" font-size=\"")
                        .append(escapeXml(fontStyle.fontSize()))
                        .append("\" font-weight=\"")
                        .append(escapeXml(fontStyle.fontWeight()))
                        .append("\" font-style=\"")
                        .append(escapeXml(fontStyle.fontStyle()));
            }

            if(style.getTextColor() !=null){
                builder.append("\" color=\"")
                        .append(escapeXml(style.getTextColor()));
            }
            if(style.getBackgroundColor() !=null){
                builder.append("\" background-color=\"")
                        .append(escapeXml(style.getBackgroundColor()));
            }
            if(style.getTextDecoration() !=null){
                builder.append("\" text-decoration=\"")
                        .append(escapeXml(style.getTextDecoration()));
            }

            if(style.getBaselineShift() != null){
                builder.append("\" baseline-shift=\"")
                        .append(escapeXml(style.getBaselineShift()));
            }
        }
        builder.append("\">");
        builder.append(escapeXml(link.getText()));
        builder.append("</fo:basic-link>");
    }
}