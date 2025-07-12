package de.kaiser.generator.element;

import de.kaiser.generator.XslFoGenerator;
import de.kaiser.model.structure.InlineElement;
import de.kaiser.model.style.StyleSheet;

public class FootnoteFoGenerator extends InlineElementFoGenerator{
    public FootnoteFoGenerator(XslFoGenerator xslFoGenerator) {
        super();
    }

    /**
     * Generates the XSL-FO string for a specific inline element.
     *
     * @param element    The inline element to be processed.
     * @param styleSheet The entire StyleSheet for accessing font information.
     * @param builder    The StringBuilder to which the generated string is appended.
     */
    @Override
    public void generate(InlineElement element, StyleSheet styleSheet, StringBuilder builder) {

    }
}
