package de.fkkaiser.generator.element;

import de.fkkaiser.generator.ImageResolver;
import de.fkkaiser.model.structure.Element;
import de.fkkaiser.model.structure.Headline;
import de.fkkaiser.model.style.ElementStyleProperties;
import de.fkkaiser.model.style.StyleSheet;
import de.fkkaiser.model.style.TextBlockStyleProperties;
import java.util.List;

/**
 * The abstract class for generating XSL-FO strings for specific elements.
 */
public abstract class ElementFoGenerator {


    /**
     * Generates the XSL-FO string for a specific element.
     * @param element The element to be processed.
     * @param styleSheet The entire StyleSheet for accessing, for example, Font information.
     * @param builder The StringBuilder to which the generated string is appended.
     */
    public abstract void generate(Element element,
                                  StyleSheet styleSheet,
                                  StringBuilder builder,
                                  List<Headline> headlines,
                                  ImageResolver resolver,
                                  boolean isExternalArtefact);

    /**
     * Escapes special characters in a given text to ensure it can be safely used in XML.
     *
     * @param text The text to escape special characters from.
     * @return The text with special XML characters escaped
     */
    protected String escapeXml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    /**
     * Sets the font style attributes to the provided StringBuilder based on the given StyleSheet and ElementStyleProperties.
     *
     * @param styleSheet The StyleSheet containing font definitions to look up.
     * @param style The ElementStyleProperties to determine the font style from.
     * @param builder The StringBuilder to append the font style attributes to.
     */
    protected void setFontStyle(StyleSheet styleSheet, ElementStyleProperties style, StringBuilder builder) {
        if (style instanceof TextBlockStyleProperties textStyle) {
            styleSheet.findFontStyleByName(textStyle.getTextStyleName()).ifPresent(fs -> {
                builder.append(" font-family=\"").append(escapeXml(fs.fontFamilyName())).append("\"");
                builder.append(" font-size=\"").append(escapeXml(fs.fontSize())).append("\"");
                builder.append(" font-weight=\"").append(escapeXml(fs.fontWeight())).append("\"");
                builder.append(" font-style=\"").append(escapeXml(fs.fontStyle())).append("\"");
            });
        }
    }

}
