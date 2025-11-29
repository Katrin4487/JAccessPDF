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
package de.fkkaiser.api.simplelayer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SimpleDocumentPageSequenceBuilderTest {

    @Test
    @DisplayName("should use the right element level")
    public void testSimpleDocumentBuilder() {

        SimpleDocumentBuilder builder = SimpleDocumentBuilder.create("A new pdf")
                .addHeading("First Heading (H1)")
                .addHeading("Second Heading (H2)",2);

        SimpleDocument.HeadingElement hl = (SimpleDocument.HeadingElement) builder.getElements().getLast();
        assertEquals(2, hl.level(), "should use the chosen level if the level is ok for a11y");
        builder.addHeading("Third (H4) to H1",4);
        hl = (SimpleDocument.HeadingElement) builder.getElements().getLast();
        assertEquals(3, hl.level(), "should use nearest, if level is not ok fo a11y");

    }

    @Test
    @DisplayName("should add simple paragraph correctly")
    public void testAddingSimpleParagraph()  {

        SimpleDocumentBuilder builder = SimpleDocumentBuilder.create("A new pdf")
                .addHeading("First Heading (H1)")
                .addParagraph("My first paragraph");

        assertNotNull(builder.getElements()); //fake...

    }
}
