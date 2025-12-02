/*
 * Copyright 2025 Katrin Kaiser
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.fkkaiser.generator.element;

import de.fkkaiser.generator.GenerateUtils;
import de.fkkaiser.generator.ImageResolver;
import de.fkkaiser.generator.XslFoGenerator;
import de.fkkaiser.model.structure.*;
import de.fkkaiser.model.style.ListItemStyleProperties;
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
 * <p>
 * The generator delegates the content generation of list items to the ListItemFoGenerator,
 * maintaining a clean separation of concerns between list structure and item content.
 * </p>
 *
 * @author Katrin Kaiser
 * @version 1.3.0
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
                generateListItem(item, list, style, counter, styleSheet, builder, headlines, resolver);
                counter++;
            }
        }
        builder.append("      </fo:list-block>");
    }

    /**
     * Generates a single list item including label and body.
     * <p>
     * This method creates the fo:list-item structure with fo:list-item-label
     * and fo:list-item-body. The body content is delegated to ListItemFoGenerator
     * via the main generator's generateBlockElement method.
     * </p>
     *
     * @param item the list item to generate
     * @param list the parent list (for accessing ordering type)
     * @param listStyle the resolved style of the parent list
     * @param counter the item counter (for ordered lists)
     * @param styleSheet the stylesheet
     * @param builder the StringBuilder to append to
     * @param headlines the list of headlines
     * @param resolver the image resolver
     */
    private void generateListItem(ListItem item,
                                  SimpleList list,
                                  ListStyleProperties listStyle,
                                  int counter,
                                  StyleSheet styleSheet,
                                  StringBuilder builder,
                                  List<Headline> headlines,
                                  ImageResolver resolver) {

        builder.append("        <fo:list-item space-before=\"0.2cm\" role=\"LI\">");

        // Generate label
        generateListItemLabel(item, list, listStyle, counter, styleSheet, builder);

        // Generate body
        generateListItemBody(item, listStyle, styleSheet, builder, headlines, resolver);

        builder.append("        </fo:list-item>\n");
    }

    /**
     * Generates the fo:list-item-label portion of a list item.
     * <p>
     * The label can be either custom (from inline elements in the ListItem)
     * or generated based on the list's ordering and style type.
     * </p>
     *
     * @param item the list item
     * @param list the parent list
     * @param listStyle the list style properties
     * @param counter the item counter
     * @param styleSheet the stylesheet
     * @param builder the StringBuilder to append to
     */
    private void generateListItemLabel(ListItem item,
                                       SimpleList list,
                                       ListStyleProperties listStyle,
                                       int counter,
                                       StyleSheet styleSheet,
                                       StringBuilder builder) {

        builder.append("          <fo:list-item-label role=\"Lbl\" end-indent=\"label-end()\">");
        builder.append("            <fo:block>");

        // Generate custom label if provided, otherwise use default based on list type
        if (item.getLabel() != null && !item.getLabel().isEmpty()) {
            for (InlineElement inline : item.getLabel()) {
                mainGenerator.generateInlineElement(inline, styleSheet, builder);
            }
        } else {
            ListItemStyleProperties itemStyle = null;
            if (item.getResolvedStyle() instanceof ListItemStyleProperties) {
                itemStyle = (ListItemStyleProperties) item.getResolvedStyle();
            }
            if(itemStyle == null || !itemStyle.getListStyleType().equals("none")){
                generateDefaultListItemLabel(builder, list.getOrdering(), listStyle, counter);
            }
        }

        builder.append("</fo:block>");
        builder.append("          </fo:list-item-label>\n");
    }

    /**
     * Generates the fo:list-item-body portion of a list item.
     * <p>
     * This method handles the "inside" positioning special case by wrapping
     * the content in an additional fo:block with text-indent. For standard
     * "outside" positioning or power-user overrides, it delegates directly
     * to the ListItemFoGenerator.
     * </p>
     *
     * @param item the list item
     * @param listStyle the list style properties
     * @param styleSheet the stylesheet
     * @param builder the StringBuilder to append to
     * @param headlines the list of headlines
     * @param resolver the image resolver
     */
    private void generateListItemBody(ListItem item,
                                      ListStyleProperties listStyle,
                                      StyleSheet styleSheet,
                                      StringBuilder builder,
                                      List<Headline> headlines,
                                      ImageResolver resolver) {

        builder.append("          <fo:list-item-body role=\"LBody\" start-indent=\"body-start()\">\n");

        mainGenerator.generateBlockElement(item, styleSheet, builder, headlines, resolver, false);

        builder.append("          </fo:list-item-body>\n");
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
            builder.append(" provisional-distance-between-starts=\"")
                    .append(GenerateUtils.escapeXml(style.getProvDistBetweenStarts()))
                    .append("\"");
        }
        if (style.getProvLabelSeparation() != null) {
            builder.append(" provisional-label-separation=\"")
                    .append(GenerateUtils.escapeXml(style.getProvLabelSeparation()))
                    .append("\"");
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
    private void generateDefaultListItemLabel(StringBuilder builder,
                                              ListOrdering ordering,
                                              ListStyleProperties style,
                                              int counter) {
        // Priority: 1. Image, 2. Type, 3. Default
        if (style != null && style.getListStyleImage() != null) {
            builder.append("<fo:external-graphic src=\"")
                    .append(GenerateUtils.escapeXml(style.getListStyleImage()))
                    .append("\"/>");
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
            case "none" -> "";
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
            case "none" -> "";
            case "circle" -> "&#x25CB;";  // hollow circle
            case "square" -> "&#x25AA;";  // small square
            default -> "&#x2022;";        // bullet
        };
    }
}