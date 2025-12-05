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

import de.fkkaiser.generator.*;
import de.fkkaiser.model.structure.Element;
import de.fkkaiser.model.structure.Headline;
import de.fkkaiser.model.structure.LayoutTable;
import de.fkkaiser.model.style.StyleSheet;

import java.util.List;

/**
 * Generator for Layout Table
 *
 * @author Katrin Kaiser
 * @version 1.0.1
 */
public class LayoutTableFoGenerator extends ElementFoGenerator {

    protected final XslFoGenerator mainGenerator;

    public LayoutTableFoGenerator(final XslFoGenerator mainGenerator) {
        super();
        this.mainGenerator = mainGenerator;
    }

    @Override
    public void generate(Element element,
                         StyleSheet styleSheet,
                         StringBuilder builder,
                         List<Headline> headlines,
                         ImageResolver resolver,
                         boolean isExternalArtefact) {

        LayoutTable table = (LayoutTable) element;

        // Generate content for left and right cells
        StringBuilder leftContent = new StringBuilder();
        mainGenerator.generateBlockElement(table.getElementLeft(), styleSheet, leftContent, headlines, resolver, false);

        StringBuilder rightContent = new StringBuilder();
        mainGenerator.generateBlockElement(table.getElementRight(), styleSheet, rightContent, headlines, resolver, false);

        // Build the table structure
        TagBuilder tableBuilder = GenerateUtils.tagBuilder(GenerateConst.TABLE)
                .addAttribute(GenerateConst.TABLE_LAYOUT, "fixed") //Customize Me!
                .addAttribute(GenerateConst.WIDTH, "100%")
                .addChild(
                        GenerateUtils.tagBuilder(GenerateConst.TABLE_COLUMN)
                                .addAttribute(GenerateConst.COLUMN_WIDTH, "85%")
                )
                .addChild(
                        GenerateUtils.tagBuilder(GenerateConst.TABLE_COLUMN)
                                .addAttribute(GenerateConst.COLUMN_WIDTH, "15%")
                )
                .addChild(
                        GenerateUtils.tagBuilder(GenerateConst.TABLE_BODY)
                                .addChild(
                                        GenerateUtils.tagBuilder(GenerateConst.TABLE_ROW)
                                                .addChild(
                                                        GenerateUtils.tagBuilder(GenerateConst.TABLE_CELL)
                                                                .addAttribute(GenerateConst.PADDING, "0pt")
                                                                .addNestedContent(leftContent.toString())
                                                )
                                                .addChild(
                                                        GenerateUtils.tagBuilder(GenerateConst.TABLE_CELL)
                                                                .addAttribute(GenerateConst.END_INDENT, "0pt")
                                                                .addAttribute(GenerateConst.TEXT_ALIGN, "end")
                                                                .addAttribute(GenerateConst.PADDING, "0pt")
                                                                .addNestedContent(rightContent.toString())
                                                )
                                )
                );

        tableBuilder.buildInto(builder);
    }
}