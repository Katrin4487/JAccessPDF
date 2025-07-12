package de.kaiser.generator.element;

import de.kaiser.generator.XslFoGenerator;
import de.kaiser.model.structure.Element;
import de.kaiser.model.structure.Headline;
import de.kaiser.model.structure.TextBlock;
import de.kaiser.model.style.HeadlineStyleProperties;
import de.kaiser.model.style.StyleSheet;
import de.kaiser.model.style.TextBlockStyleProperties;

import java.util.List;

/**
 * Generates XSL-FO for Headline elements.
 */
public class HeadlineFoGenerator extends TextBlockFoGenerator {

    /**
     * Constructor for HeadlineFoGenerator.
     * @param mainGenerator The main generator for delegation.
     */
    public HeadlineFoGenerator(XslFoGenerator mainGenerator) {
        super(mainGenerator);
    }

    /**
     * Returns the accessibility role for the headline (e.g., "H1", "H2").
     * @param textBlock The headline element.
     * @return The corresponding role string.
     */
    @Override
    protected String getRole(TextBlock textBlock) {
        if (textBlock instanceof Headline) {
            return "H" + ((Headline) textBlock).getLevel();
        }
        return "P";
    }

    /**
     * Appends headline-specific attributes to the FO block.
     * @param builder The StringBuilder to append to.
     * @param style The resolved style properties.
     */
    @Override
    protected void appendSpecificAttributes(StringBuilder builder, TextBlockStyleProperties style) {

        if (style instanceof HeadlineStyleProperties hStyle) {
            if (hStyle.getKeepWithNext() != null && hStyle.getKeepWithNext()) {
                builder.append(" keep-with-next.within-page=\"always\"");
            }

        }
    }
}
