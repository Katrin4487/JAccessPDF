package de.fkkaiser.generator.element;

import de.fkkaiser.generator.XslFoGenerator;
import de.fkkaiser.model.structure.Headline;
import de.fkkaiser.model.structure.TextBlock;
import de.fkkaiser.model.style.HeadlineStyleProperties;
import de.fkkaiser.model.style.TextBlockStyleProperties;

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
