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
import de.fkkaiser.model.structure.LayoutTable;
import de.fkkaiser.model.style.StyleSheet;
import java.util.List;

public class LayoutTableFoGenerator extends ElementFoGenerator {


    protected final XslFoGenerator mainGenerator;

    public LayoutTableFoGenerator(final XslFoGenerator mainGenerator) {
        super();
        this.mainGenerator = mainGenerator;
    }

    //ToDO

    @Override
    public void generate(Element element,
                         StyleSheet styleSheet,
                         StringBuilder builder,
                         List<Headline> headlines,
                         ImageResolver resolver,
                         boolean isExternalArtefact) {

        LayoutTable table = (LayoutTable) element;
        builder.append("<fo:table table-layout=\"fixed\" width=\"100%\">");
        builder.append("<fo:table-column column-width=\"85%\"/><fo:table-column column-width=\"15%\"/>");
        builder.append(" <fo:table-body>");
        builder.append("<fo:table-row><fo:table-cell padding=\"0pt\">");
        mainGenerator.generateBlockElement(table.getElementLeft(),styleSheet,builder,headlines,resolver,false);
        builder.append( "</fo:table-cell>");
        builder.append("<fo:table-cell end-indent=\"0pt\" text-align=\"end\" padding=\"0pt\">");
        mainGenerator.generateBlockElement(table.getElementRight(), styleSheet, builder, headlines,resolver,false);
        builder.append("</fo:table-cell>");
        builder.append( "</fo:table-row>");
        builder.append("</fo:table-body>");
        builder.append("</fo:table>");
    }
}
