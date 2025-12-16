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
package de.fkkaiser.model.structure;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableTest {

    @Test
    @DisplayName("Should create a Table with chaining")
    public void testCreateTableWithChaining() {
        String tableStyle = "Table-Style";
        Paragraph h1 = Paragraph.builder("Header-Paragraph")
                .addInlineText("Header Cell 1")
                .build();
        Paragraph h2 = Paragraph.builder("Header-Paragraph")
                .addInlineText("Header Cell 2")
                .build();
        Paragraph h3 = Paragraph.builder("Header-Paragraph")
                .addInlineText("Header Cell 3")
                .build();
        Paragraph p1 = Paragraph.builder("Body-Paragraph")
                .addInlineText("Body Cell 1")
                .build();
        Paragraph p2 = Paragraph.builder("Body-Paragraph")
                .addInlineText("Body Cell 2")
                .build();
        Paragraph p3 = Paragraph.builder("Body-Paragraph")
                .addInlineText("Body Cell 3")
                .build();

        Table table = Table.builder(tableStyle)
                .addColumns("33%", "33%", "33%")
                .addHeaderStyle("Header-Style")
                .addBodyStyle("Body-Style")
                .addFooterStyle("Footer-Style")
                .addHeaderCells(h1,h2,h3)
                .startBodyRow()
                    .addBodyCell(p1)
                    .addBodyCell(p2)
                    .addBodyCell(p3)
                .endBodyRow()
                .build();

        Assertions.assertEquals(tableStyle,table.getStyleClass());
        Assertions.assertEquals(3,table.getColumns().size());
    }
}
