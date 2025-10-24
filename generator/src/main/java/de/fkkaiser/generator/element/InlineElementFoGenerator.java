package de.fkkaiser.generator.element;

import de.fkkaiser.model.structure.InlineElement;
import de.fkkaiser.model.style.StyleSheet;

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
