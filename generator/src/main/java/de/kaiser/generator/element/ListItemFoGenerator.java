package de.kaiser.generator.element;

import de.kaiser.generator.XslFoGenerator;
import de.kaiser.model.structure.Element;
import de.kaiser.model.structure.Headline;
import de.kaiser.model.structure.ListItem;
import de.kaiser.model.structure.TextBlock;
import de.kaiser.model.style.ElementBlockStyleProperties;
import de.kaiser.model.style.StyleSheet;
import de.kaiser.model.style.TextBlockStyleProperties;

import java.net.URL;
import java.util.List;

/**
 * Generates the XSL-FO structure for a ListItem.
 * A ListItem is treated as a block-level container, similar to a paragraph.
 */
public class ListItemFoGenerator extends TextBlockFoGenerator {

    public ListItemFoGenerator(XslFoGenerator mainGenerator) {
        super(mainGenerator);
    }

    /**
     * Generates the content of a list item, which is essentially a block
     * that can contain other block-level elements.
     */
    @Override
    public void generate(Element element, StyleSheet styleSheet, StringBuilder builder, List<Headline> headlines, URL imageUrl) {
        ListItem listItem = (ListItem) element;
        ElementBlockStyleProperties style = listItem.getResolvedStyle();

        // The content of a list item is wrapped in a single <fo:block>.
        // This block carries all styling for the list item.
        builder.append("            <fo:block");

        // Use the helper methods inherited from TextBlockFoGenerator
        // to apply all common text and block styles.
        appendCommonAttributes(builder, style, styleSheet);
        if(style instanceof TextBlockStyleProperties textStyle){
            appendSpecificAttributes(builder, textStyle);
        }

        builder.append(">");

        // Delegate the generation of the list item's content back to the main generator.
        // This allows a list item to contain paragraphs, other lists, tables, etc.
        if (listItem.getElements() != null) {
            mainGenerator.generateBlockElements(listItem.getElements(), styleSheet, builder, headlines,imageUrl);
        }

        builder.append("            </fo:block>");
    }

    @Override
    protected String getRole(TextBlock textBlock) {
      return null;
    }

    @Override
    protected void appendSpecificAttributes(StringBuilder builder, TextBlockStyleProperties style) {
        // Currently, ListItem has no specific styles beyond what TextBlock offers.

    }
}
