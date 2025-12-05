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
import de.fkkaiser.model.structure.LayoutTable;
import de.fkkaiser.model.style.StyleSheet;

import java.util.List;

/**
 * Generator for Layout Table
 *
 * @author Katrin Kaiser
 * @version 1.0.0
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
        TagBuilder tableBuilder = GenerateUtils.tagBuilder("table")
                .addAttribute("table-layout", "fixed")
                .addAttribute("width", "100%")
                .addChild(
                        GenerateUtils.tagBuilder("table-column")
                                .addAttribute("column-width", "85%")
                )
                .addChild(
                        GenerateUtils.tagBuilder("table-column")
                                .addAttribute("column-width", "15%")
                )
                .addChild(
                        GenerateUtils.tagBuilder("table-body")
                                .addChild(
                                        GenerateUtils.tagBuilder("table-row")
                                                .addChild(
                                                        GenerateUtils.tagBuilder("table-cell")
                                                                .addAttribute("padding", "0pt")
                                                                .addNestedContent(leftContent.toString())
                                                )
                                                .addChild(
                                                        GenerateUtils.tagBuilder("table-cell")
                                                                .addAttribute("end-indent", "0pt")
                                                                .addAttribute("text-align", "end")
                                                                .addAttribute("padding", "0pt")
                                                                .addNestedContent(rightContent.toString())
                                                )
                                )
                );

        tableBuilder.buildInto(builder);
    }
}