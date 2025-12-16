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
import de.fkkaiser.model.structure.Element;
import de.fkkaiser.model.structure.Headline;
import de.fkkaiser.model.structure.ListItem;
import de.fkkaiser.model.structure.TextBlock;
import de.fkkaiser.model.style.ElementBlockStyleProperties;
import de.fkkaiser.model.style.ListItemStyleProperties;
import de.fkkaiser.model.style.StyleSheet;
import de.fkkaiser.model.style.TextBlockStyleProperties;
import java.util.List;

/**
 * Generates the XSL-FO structure for a ListItem's content.
 * <p>
 * A ListItem is treated as a block-level container that can hold other block elements
 * (paragraphs, nested lists, tables, etc.). This generator is called by the ListFoGenerator
 * to handle the content portion of a list item, while the ListFoGenerator itself handles
 * the list structure, labels, and positioning.
 * </p>
 * <p>
 * Note: This generator does NOT create the fo:list-item or fo:list-item-body elements.
 * It only generates the content that goes inside the fo:list-item-body.
 * </p>
 *
 * @author Katrin Kaiser
 * @version 1.1.2
 */
public class ListItemFoGenerator extends TextBlockFoGenerator {

    /**
     * Constructor
     * @param mainGenerator the main XSL-FO generator
     */
    public ListItemFoGenerator(XslFoGenerator mainGenerator) {
        super(mainGenerator);
    }

    /**
     * Generates the content of a list item.
     * <p>
     * The list item content is wrapped in fo:block elements that carry all styling
     * properties. This allows list items to have their own text formatting, spacing,
     * and other block-level properties independent of the parent list's styling.
     * </p>
     * <p>
     * The method delegates the generation of nested elements (paragraphs, nested lists, etc.)
     * back to the main generator, maintaining the recursive generation pattern.
     * </p>
     *
     * @param element the list item element to generate
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
        ListItem listItem = (ListItem) element;
        ElementBlockStyleProperties style = listItem.getResolvedStyle();

        // Note: We do NOT generate fo:list-item or fo:list-item-body here,
        if (listItem.getElements() != null && !listItem.getElements().isEmpty()) {
            mainGenerator.generateBlockElements(listItem.getElements(), styleSheet, builder, headlines, resolver, isExternalArtefact);
        } else {
            TagBuilder emptyBlock = GenerateUtils.tagBuilder("block");
            appendBlockAttributes(emptyBlock, style, styleSheet);

            if (style instanceof TextBlockStyleProperties textStyle) {
                appendSpecificAttributes(emptyBlock, textStyle);
            }

            emptyBlock.buildInto(builder);
        }
    }

    @Override
    protected String getRole(TextBlock textBlock) {
        // List items don't need a specific role - the role="LI" is already
        // set on the fo:list-item by ListFoGenerator
        return null;
    }

    @Override
    protected void appendSpecificAttributes(TagBuilder builder, TextBlockStyleProperties style) {
        if (style instanceof ListItemStyleProperties liStyle) {
            builder
                    .addAttribute("space-before", liStyle.getSpaceBefore())
                    .addAttribute("space-after", liStyle.getSpaceAfter());
        }
    }
}