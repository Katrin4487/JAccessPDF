package de.fkkaiser.generator.element;

import de.fkkaiser.model.structure.InlineElement;
import de.fkkaiser.model.style.StyleSheet;

public class PageNumberFoGenerator extends InlineElementFoGenerator{
    /**
     * Generates the XSL-FO string for a specific inline element.
     *
     * @param element    The inline element to be processed.
     * @param styleSheet The entire StyleSheet for accessing font information.
     * @param builder    The StringBuilder to which the generated string is appended.
     */
    @Override
    public void generate(InlineElement element, StyleSheet styleSheet, StringBuilder builder) {
        builder.append("<fo:page-number/>");
    }
}
