package de.fkkaiser.generator.element;

import de.fkkaiser.generator.GenerateUtils;
import de.fkkaiser.generator.ImageResolver;
import de.fkkaiser.generator.XslFoGenerator;
import de.fkkaiser.model.structure.*;
import de.fkkaiser.model.style.ListStyleProperties;
import de.fkkaiser.model.style.StyleSheet;
import java.util.List;


/**
 * Generates XSL-FO markup for list elements.
 * <p>
 * This generator supports both ordered and unordered lists with customizable
 * list style types (bullets, numbers, letters) and positioning modes (inside/outside).
 * It handles list items with custom labels and generates appropriate XSL-FO list-block
 * structures with proper accessibility roles.
 * </p>
 */
public class ListFoGenerator extends ElementFoGenerator {

    protected final XslFoGenerator mainGenerator;

    /**
     * Constructs a new ListFoGenerator.
     *
     * @param mainGenerator the main generator for delegating sub-element generation
     */
    public ListFoGenerator(XslFoGenerator mainGenerator) {
        super();
        this.mainGenerator = mainGenerator;
    }

    /**
     * Generates XSL-FO markup for a list element.
     * <p>
     * Creates a fo:list-block with appropriate styling and accessibility roles,
     * then generates fo:list-item elements for each item in the list. Handles
     * custom labels and supports both "inside" and "outside" list style positioning.
     * </p>
     *
     * @param element the list element to generate
     * @param styleSheet the stylesheet containing style definitions
     * @param builder the StringBuilder to append XSL-FO markup to
     * @param headlines the list of headlines for cross-referencing
     * @param resolver the image resolver for resolving image paths
     * @param isExternalArtefact whether this is an external artifact
     */
    @Override
    public void generate(Element element,
                         StyleSheet styleSheet,
                         StringBuilder builder,
                         List<Headline> headlines,
                         ImageResolver resolver,
                         boolean isExternalArtefact) {
        SimpleList list = (SimpleList) element;
        ListStyleProperties style = list.getResolvedStyle();

        builder.append("      <fo:list-block role=\"L\"");
        appendListBlockAttributes(builder, style, styleSheet);
        if(isExternalArtefact) {
            builder.append(" fox:content-type=\"external-artifact\"");
        }
        builder.append(">\n");

        if (list.getItems() != null) {
            int counter = 1;
            for (ListItem item : list.getItems()) {

                builder.append("        <fo:list-item space-before=\"0.2cm\" role=\"LI\"");
                builder.append(">");
                builder.append("          <fo:list-item-label role=\"Lbl\" end-indent=\"label-end()\">");
                builder.append("            <fo:block>");

                // Generate custom label if provided, otherwise use default based on list type
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

                // Check the user's desired list-style-position (not the resolved value)
                String desiredPosition = style.getListStylePosition();

                // Apply "inside" positioning logic only if the user requested "inside"
                // AND has not manually set provisional-distance-between-starts
                if ("inside".equalsIgnoreCase(desiredPosition) && !style.isProvDistBetweenStartsManuallySet()) {

                    // "Inside" mode: marker appears inside the content flow
                    // (distance="0pt" is automatically returned by the getter)
                    // We wrap the content in a fo:block with text-indent to create proper alignment

                    // Use the label-separation value as the text indent for the first line
                    String textIndent = style.getProvLabelSeparation();

                    builder.append("<fo:block text-indent=\"").append(GenerateUtils.escapeXml(textIndent)).append("\">");

                    // Generate the block element content within our wrapper
                    mainGenerator.generateBlockElement(item, styleSheet, builder, headlines, resolver, false);

                    builder.append("</fo:block>");

                } else {
                    // Standard behavior for "outside" positioning or power-user override
                    mainGenerator.generateBlockElement(item, styleSheet, builder, headlines, resolver, false);
                }
                builder.append("          </fo:list-item-body>");
                builder.append("        </fo:list-item>");
                counter++;
            }
        }
        builder.append("      </fo:list-block>");
    }


    /**
     * Appends the specific style attributes for the fo:list-block element.
     * <p>
     * Includes font styling and provisional distance attributes that control
     * the spacing between list labels and content.
     * </p>
     *
     * @param builder the StringBuilder to append attributes to
     * @param style the list style properties
     * @param styleSheet the stylesheet for resolving font styles
     */
    private void appendListBlockAttributes(StringBuilder builder, ListStyleProperties style, StyleSheet styleSheet) {
        if (style == null) return;

        setFontStyle(styleSheet, style, builder);

        if (style.getProvDistBetweenStarts() != null) {
            builder.append(" provisional-distance-between-starts=\"").append(GenerateUtils.escapeXml(style.getProvDistBetweenStarts())).append("\"");
        }
        if (style.getProvLabelSeparation() != null) {
            builder.append(" provisional-label-separation=\"").append(GenerateUtils.escapeXml(style.getProvLabelSeparation())).append("\"");
        }
    }

    /**
     * Generates the default list item label based on list type and ordering.
     * <p>
     * Priority order:
     * <ol>
     *   <li>Custom image (list-style-image)</li>
     *   <li>Style type (list-style-type)</li>
     *   <li>Default (bullet for unordered, number for ordered)</li>
     * </ol>
     * </p>
     *
     * @param builder the StringBuilder to append the label to
     * @param ordering the list ordering type (ordered or unordered)
     * @param style the list style properties
     * @param counter the current item number (for ordered lists)
     */
    private void generateDefaultListItemLabel(StringBuilder builder, ListOrdering ordering, ListStyleProperties style, int counter) {
        // Priority: 1. Image, 2. Type, 3. Default
        if (style != null && style.getListStyleImage() != null) {
            builder.append("<fo:external-graphic src=\"").append(GenerateUtils.escapeXml(style.getListStyleImage())).append("\"/>");
            return;
        }

        String listStyleType = (style != null) ? style.getListStyleType() : null;

        String label;
        if (ordering == ListOrdering.ORDERED) {
            label = getOrderedLabel(listStyleType, counter);
        } else {
            label = getUnorderedLabel(listStyleType);
        }
        builder.append(label);
    }

    /**
     * Generates the label for an ordered list item.
     * <p>
     * Supports different numbering styles including:
     * <ul>
     *   <li>decimal (default): 1, 2, 3, ...</li>
     *   <li>lower-alpha: a, b, c, ...</li>
     *   <li>upper-alpha: A, B, C, ...</li>
     * </ul>
     * Additional styles like lower-roman and upper-roman can be added as needed.
     * </p>
     *
     * @param type the list-style-type value
     * @param counter the current item number
     * @return the formatted label string
     */
    private String getOrderedLabel(String type, int counter) {
        if (type == null) return counter + ".";
        return switch (type) {
            case "lower-alpha" -> (char) ('a' + counter - 1) + ".";
            case "upper-alpha" -> (char) ('A' + counter - 1) + ".";
            // Additional ordered types like 'lower-roman', 'upper-roman' can be added here
            default -> counter + ".";
        };
    }

    /**
     * Generates the label for an unordered list item.
     * <p>
     * Supports different bullet styles:
     * <ul>
     *   <li>disc (default): filled circle (•)</li>
     *   <li>circle: hollow circle (○)</li>
     *   <li>square: filled square (▪)</li>
     * </ul>
     * </p>
     *
     * @param type the list-style-type value
     * @return the Unicode character for the bullet
     */
    private String getUnorderedLabel(String type) {
        if (type == null) return "&#x2022;";  // bullet
        return switch (type) {
            case "circle" -> "&#x25CB;";  // hollow circle
            case "square" -> "&#x25AA;";  // small square
            default -> "&#x2022;";        // bullet
        };
    }
}