package de.fkkaiser.generator.element;


import de.fkkaiser.generator.ImageResolver;
import de.fkkaiser.generator.XslFoGenerator;
import de.fkkaiser.model.structure.*;
import de.fkkaiser.model.style.ListStyleProperties;
import de.fkkaiser.model.style.StyleSheet;

import java.net.URL;
import java.util.List;

/**
 * Generates XSL-FO for EList elements, supporting both ordered and unordered lists
 * and custom list style types.
 */
public class ListFoGenerator extends ElementFoGenerator {

    protected final XslFoGenerator mainGenerator;

    /**
     * Constructor for EListFoGenerator.
     * @param mainGenerator The main generator for delegation.
     */
    public ListFoGenerator(XslFoGenerator mainGenerator) {
        super(); // Call to super() is implicit, but good for clarity
        this.mainGenerator = mainGenerator;
    }

    @Override
    public void generate(Element element, StyleSheet styleSheet, StringBuilder builder, List<Headline> headlines, ImageResolver resolver) {
        SimpleList list = (SimpleList) element;
        ListStyleProperties style = list.getResolvedStyle();

        builder.append("      <fo:list-block role=\"L\"");
        appendListBlockAttributes(builder, style, styleSheet);
        builder.append(">\n");

        if (list.getItems() != null) {
            int counter = 1;
            for (ListItem item : list.getItems()) {

                builder.append("        <fo:list-item space-before=\"0.2cm\" role=\"LI\">");
                builder.append("          <fo:list-item-label role=\"Lbl\" end-indent=\"label-end()\">");
                builder.append("            <fo:block>");

                if (item.getLabel() != null && !item.getLabel().isEmpty()) {
                    for (InlineElement inline : item.getLabel()) {
                        mainGenerator.generateInlineElement(inline, styleSheet, builder);
                    }
                } else {
                    generateDefaultListItemLabel(builder, list.getOrdering(), style, counter);
                }

                builder.append("</fo:block>");
                builder.append("          </fo:list-item-label>");
                builder.append("          <fo:list-item-body role=\"LBody\" start-indent=\"body-start()\">");

                mainGenerator.generateBlockElement(item, styleSheet, builder, headlines,resolver);

                builder.append("          </fo:list-item-body>");
                builder.append("        </fo:list-item>");
                counter++;
            }
        }
        builder.append("      </fo:list-block>");
    }


    /**
     * Appends the specific style attributes for the fo:list-block element.
     */
    private void appendListBlockAttributes(StringBuilder builder, ListStyleProperties style, StyleSheet styleSheet) {
        if (style == null) return;

        setFontStyle(styleSheet, style, builder);

        if (style.getProvDistBetweenStarts() != null) {
            builder.append(" provisional-distance-between-starts=\"").append(escapeXml(style.getProvDistBetweenStarts())).append("\"");
        }
        if (style.getProvLabelSeparation() != null) {
            builder.append(" provisional-label-separation=\"").append(escapeXml(style.getProvLabelSeparation())).append("\"");
        }
        if (style.getListStylePosition() != null) {
            builder.append(" list-style-position=\"").append(escapeXml(style.getListStylePosition())).append("\"");
        }
    }

    /**
     * Generates the content of the list item label (e.g., a bullet or a number).
     */
    private void generateDefaultListItemLabel(StringBuilder builder, ListOrdering ordering, ListStyleProperties style, int counter) {
        // Priority: 1. Image, 2. Type, 3. Default
        if (style != null && style.getListStyleImage() != null) {
            builder.append("<fo:external-graphic src=\"").append(escapeXml(style.getListStyleImage())).append("\"/>");
            return;
        }

        String listStyleType = (style != null) ? style.getListStyleType() : null;

        if (ordering == ListOrdering.ORDERED) {
            String label = getOrderedLabel(listStyleType, counter);
            builder.append(label);
        } else {
            String label = getUnorderedLabel(listStyleType);
            builder.append(label);
        }
    }

    private String getOrderedLabel(String type, int counter) {
        if (type == null) return counter + ".";
        return switch (type) {
            case "lower-alpha" -> (char) ('a' + counter - 1) + ".";
            case "upper-alpha" -> (char) ('A' + counter - 1) + ".";
            // Add other ordered types like 'lower-roman', 'upper-roman' here
            default -> counter + ".";
        };
    }

    private String getUnorderedLabel(String type) {
        if (type == null) return "&#x2022;";
        return switch (type) {
            case "circle" -> "&#x25CB;";
            case "square" -> "&#x25AA;";
            default -> "&#x2022;";
        };
    }
}
