package de.kaiser.generator.element;

import de.kaiser.generator.XslFoGenerator;
import de.kaiser.model.structure.TextBlock;
import de.kaiser.model.style.ParagraphStyleProperties;
import de.kaiser.model.style.TextBlockStyleProperties;

/**
 * Generates XSL-FO representation for Paragraph elements.
 */
public class ParagraphFoGenerator extends TextBlockFoGenerator {

    /**
     * Constructor for ParagraphFoGenerator.
     * @param mainGenerator The main generator for delegation.
     */
    public ParagraphFoGenerator(XslFoGenerator mainGenerator) {
        super(mainGenerator);
    }

    /**
     * Returns the accessibility role for a paragraph ("P").
     * @param textBlock The paragraph element.
     * @return The role string "P".
     */
    @Override
    protected String getRole(TextBlock textBlock) {
        return "P";
    }

    /**
     * Appends paragraph-specific attributes to the FO block.
     * @param builder The StringBuilder to append to.
     * @param style The resolved style properties.
     */
    @Override
    protected void appendSpecificAttributes(StringBuilder builder, TextBlockStyleProperties style) {
        // This method handles properties that only exist in ParagraphStyleProperties.
        if (style instanceof ParagraphStyleProperties pStyle) {
            if (pStyle.getTextIndent() != null) {
                builder.append(" text-indent=\"").append(escapeXml(pStyle.getTextIndent())).append("\"");
            }
            if (pStyle.getTextAlignLast() != null) {
                builder.append(" text-align-last=\"").append(escapeXml(pStyle.getTextAlignLast())).append("\"");
            }
            if (pStyle.isHyphenate()) {
                builder.append(" hyphenate=\"true\"");
            }
            if (pStyle.getLanguage() != null) {
                builder.append(" language=\"").append(escapeXml(pStyle.getLanguage())).append("\"");
            }
            // The 'orphans' and 'widows' properties control the minimum number of lines
            // of a paragraph to be left at the top or bottom of a page.
            if (pStyle.isOrphans()) {
                builder.append(" orphans=\"2\""); // Default to 2 if true
            }
            if (pStyle.isWidows()) {
                builder.append(" widows=\"2\""); // Default to 2 if true
            }
        }
    }
}
