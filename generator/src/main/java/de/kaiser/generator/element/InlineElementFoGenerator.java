package de.kaiser.generator.element;

import de.kaiser.model.structure.InlineElement;
import de.kaiser.model.style.StyleSheet;

/**
 * Abstract base class for generating XSL-FO strings for specific inline elements.
 */
public abstract class InlineElementFoGenerator {

    /**
     * Generates the XSL-FO string for a specific inline element.
     * @param element    The inline element to be processed.
     * @param styleSheet The entire StyleSheet for accessing font information.
     * @param builder    The StringBuilder to which the generated string is appended.
     */
    public abstract void generate(InlineElement element, StyleSheet styleSheet, StringBuilder builder);

    protected String escapeXml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
