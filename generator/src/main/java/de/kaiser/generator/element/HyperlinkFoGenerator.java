package de.kaiser.generator.element;


import de.kaiser.model.structure.Hyperlink;
import de.kaiser.model.structure.InlineElement;
import de.kaiser.model.style.StyleSheet;

public class HyperlinkFoGenerator extends InlineElementFoGenerator {
    @Override
    public void generate(InlineElement element, StyleSheet styleSheet, StringBuilder builder) {
        Hyperlink link = (Hyperlink) element;
        builder.append("<fo:basic-link external-destination=\"")
                .append(escapeXml(link.getHref()))
                .append("\" fox:alt-text=\"")
                .append(escapeXml(link.getAltText()))
                .append("\">");
        // A link's text itself could be styled, so we just output the text here.
        // For rich content in links, this would need to delegate back to a main generator.
        builder.append(escapeXml(link.getText()));
        builder.append("</fo:basic-link>");
    }
}