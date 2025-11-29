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

import de.fkkaiser.generator.ImageResolver;
import de.fkkaiser.generator.XslFoGenerator;
import de.fkkaiser.model.structure.Element;
import de.fkkaiser.model.structure.Headline;
import de.fkkaiser.model.structure.ListItem;
import de.fkkaiser.model.structure.TextBlock;
import de.fkkaiser.model.style.ElementBlockStyleProperties;
import de.fkkaiser.model.style.StyleSheet;
import de.fkkaiser.model.style.TextBlockStyleProperties;
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
    public void generate(Element element,
                         StyleSheet styleSheet,
                         StringBuilder builder,
                         List<Headline> headlines,
                         ImageResolver resolver,
                         boolean isExternalArtefact) {
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
            mainGenerator.generateBlockElements(listItem.getElements(), styleSheet, builder, headlines,resolver,false);
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
