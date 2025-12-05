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
import de.fkkaiser.generator.TagBuilder;
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

        TagBuilder listBlockBuilder = GenerateUtils.tagBuilder("list-block")
                .addAttribute("role", "L");

        appendListBlockAttributes(listBlockBuilder, style, styleSheet);

        if (isExternalArtefact) {
            listBlockBuilder.addAttribute("fox:content-type", "external-artifact");
        }

        // Generate list items
        int counter = 1;
        for (ListItem item : list.getItems()) {
            TagBuilder listItemBuilder = generateListItem(item, list, style, counter, styleSheet, headlines, resolver);
            listBlockBuilder.addChild(listItemBuilder);
            counter++;
        }

        listBlockBuilder.buildInto(builder);
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
     * @param headlines the list of headlines
     * @param resolver the image resolver
     * @return the TagBuilder for the list item
     */
    private TagBuilder generateListItem(ListItem item,
                                        SimpleList list,
                                        ListStyleProperties listStyle,
                                        int counter,
                                        StyleSheet styleSheet,
                                        List<Headline> headlines,
                                        ImageResolver resolver) {

        TagBuilder listItemBuilder = GenerateUtils.tagBuilder("list-item")
                .addAttribute("space-before", "0.2cm")
                .addAttribute("role", "LI");

        // Generate label
        TagBuilder labelBuilder = generateListItemLabel(item, list, listStyle, counter, styleSheet);
        listItemBuilder.addChild(labelBuilder);

        // Generate body
        TagBuilder bodyBuilder = generateListItemBody(item, styleSheet, headlines, resolver);
        listItemBuilder.addChild(bodyBuilder);

        return listItemBuilder;
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
     * @return the TagBuilder for the list item label
     */
    private TagBuilder generateListItemLabel(ListItem item,
                                             SimpleList list,
                                             ListStyleProperties listStyle,
                                             int counter,
                                             StyleSheet styleSheet) {

        TagBuilder labelBlockBuilder = GenerateUtils.tagBuilder("block");

        // Generate custom label if provided, otherwise use default based on list type
        if (item.getLabel() != null && !item.getLabel().isEmpty()) {
            StringBuilder labelContent = new StringBuilder();
            for (InlineElement inline : item.getLabel()) {
                mainGenerator.generateInlineElement(inline, styleSheet, labelContent);
            }
            labelBlockBuilder.addNestedContent(labelContent.toString());
        } else {
            ListItemStyleProperties itemStyle = null;
            if (item.getResolvedStyle() instanceof ListItemStyleProperties) {
                itemStyle = (ListItemStyleProperties) item.getResolvedStyle();
            }
            if (itemStyle == null || !itemStyle.getListStyleType().equals("none")) {
                String labelText = generateDefaultListItemLabel(list.getOrdering(), listStyle, counter);
                labelBlockBuilder.addNestedContent(labelText);
            }
        }

        return GenerateUtils.tagBuilder("list-item-label")
                .addAttribute("role", "Lbl")
                .addAttribute("end-indent", "label-end()")
                .addChild(labelBlockBuilder);
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
     * @param styleSheet the stylesheet
     * @param headlines the list of headlines
     * @param resolver the image resolver
     * @return the TagBuilder for the list item body
     */
    private TagBuilder generateListItemBody(ListItem item,
                                            StyleSheet styleSheet,
                                            List<Headline> headlines,
                                            ImageResolver resolver) {

        StringBuilder bodyContent = new StringBuilder();
        mainGenerator.generateBlockElement(item, styleSheet, bodyContent, headlines, resolver, false);

        return GenerateUtils.tagBuilder("list-item-body")
                .addAttribute("role", "LBody")
                .addAttribute("start-indent", "body-start()")
                .addNestedContent(bodyContent.toString());
    }

    /**
     * Appends the specific style attributes for the fo:list-block element.
     * <p>
     * Includes font styling and provisional distance attributes that control
     * the spacing between list labels and content.
     * </p>
     *
     * @param builder the TagBuilder to add attributes to
     * @param style the list style properties
     * @param styleSheet the stylesheet for resolving font styles
     */
    private void appendListBlockAttributes(TagBuilder builder, ListStyleProperties style, StyleSheet styleSheet) {
        if (style == null) return;

        setFontStyle(styleSheet, style, builder);

        builder
                .addAttribute("provisional-distance-between-starts", style.getProvDistBetweenStarts())
                .addAttribute("provisional-label-separation", style.getProvLabelSeparation());
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
     * @param ordering the list ordering type (ordered or unordered)
     * @param style the list style properties
     * @param counter the current item number (for ordered lists)
     * @return the label as a string (can contain XML markup for images)
     */
    private String generateDefaultListItemLabel(ListOrdering ordering,
                                                ListStyleProperties style,
                                                int counter) {
        // Priority: 1. Image, 2. Type, 3. Default
        if (style != null && style.getListStyleImage() != null) {
            return GenerateUtils.tagBuilder("external-graphic")
                    .addAttribute("src", style.getListStyleImage())
                    .build();
        }

        String listStyleType = (style != null) ? style.getListStyleType() : null;

        if (ordering == ListOrdering.ORDERED) {
            return getOrderedLabel(listStyleType, counter);
        } else {
            return getUnorderedLabel(listStyleType);
        }
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