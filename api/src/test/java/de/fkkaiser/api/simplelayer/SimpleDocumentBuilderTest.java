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
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

class SimpleDocumentBuilderTest {

    @Nested
    @DisplayName("Heading Hierarchy Tests")
    class HeadingHierarchyTests {

        @Test
        @DisplayName("First heading must be level 1")
        void firstHeadingMustBeLevel1() {
            SimpleDocumentBuilder builder = SimpleDocumentBuilder.create("Test");

            // Versuche direkt mit H2 zu starten
            builder.addHeading("Should be corrected to H1", 2);

            // Prüfe ob auf H1 korrigiert wurde
            var elements = builder.getElements();
            assertEquals(1, elements.size());

            SimpleDocument.HeadingElement heading =
                    (SimpleDocument.HeadingElement) elements.getFirst();
            assertEquals(1, heading.level(), "First heading should be auto-corrected to level 1");
        }

        @Test
        @DisplayName("Valid hierarchy: H1 → H2 → H3")
        void validHierarchyDescending() {
            SimpleDocumentBuilder builder = SimpleDocumentBuilder.create("Test")
                    .addHeading("Chapter", 1)
                    .addHeading("Section", 2)
                    .addHeading("Subsection", 3);

            var elements = builder.getElements();
            assertEquals(3, elements.size());

            assertEquals(1, ((SimpleDocument.HeadingElement) elements.get(0)).level());
            assertEquals(2, ((SimpleDocument.HeadingElement) elements.get(1)).level());
            assertEquals(3, ((SimpleDocument.HeadingElement) elements.get(2)).level());
        }

        @Test
        @DisplayName("Valid hierarchy: Going up is allowed (H3 → H2 → H1)")
        void validHierarchyAscending() {
            SimpleDocumentBuilder builder = SimpleDocumentBuilder.create("Test")
                    .addHeading("Chapter", 1)
                    .addHeading("Section", 2)
                    .addHeading("Subsection", 3)
                    .addHeading("Section 2", 2)    // H3 → H2 erlaubt
                    .addHeading("Chapter 2", 1);   // H2 → H1 erlaubt

            var elements = builder.getElements();
            assertEquals(5, elements.size());

            assertEquals(3, ((SimpleDocument.HeadingElement) elements.get(2)).level());
            assertEquals(2, ((SimpleDocument.HeadingElement) elements.get(3)).level());
            assertEquals(1, ((SimpleDocument.HeadingElement) elements.get(4)).level());
        }

        @Test
        @DisplayName("Invalid: Skipping levels downward (H1 → H3) should auto-correct to H2")
        void skippingLevelsDownwardAutoCorrects() {
            SimpleDocumentBuilder builder = SimpleDocumentBuilder.create("Test")
                    .addHeading("Chapter", 1)
                    .addHeading("Should be H2, not H3", 3);  // Überspringt H2

            var elements = builder.getElements();

            SimpleDocument.HeadingElement secondHeading =
                    (SimpleDocument.HeadingElement) elements.get(1);
            assertEquals(2, secondHeading.level(),
                    "H3 after H1 should be auto-corrected to H2");
        }

        @Test
        @DisplayName("Invalid: Multiple level skip (H1 → H4) should correct to H2")
        void multipleSkipAutoCorrects() {
            SimpleDocumentBuilder builder = SimpleDocumentBuilder.create("Test")
                    .addHeading("Chapter", 1)
                    .addHeading("Should be H2", 4);  // Überspringt 2 Level

            var elements = builder.getElements();

            SimpleDocument.HeadingElement secondHeading =
                    (SimpleDocument.HeadingElement) elements.get(1);
            assertEquals(2, secondHeading.level(),
                    "H4 after H1 should be auto-corrected to H2");
        }

        @Test
        @DisplayName("Same level is allowed (H2 → H2)")
        void sameLevelAllowed() {
            SimpleDocumentBuilder builder = SimpleDocumentBuilder.create("Test")
                    .addHeading("Chapter", 1)
                    .addHeading("Section 1", 2)
                    .addHeading("Section 2", 2);  // Gleiches Level OK

            var elements = builder.getElements();

            assertEquals(2, ((SimpleDocument.HeadingElement) elements.get(1)).level());
            assertEquals(2, ((SimpleDocument.HeadingElement) elements.get(2)).level());
        }

        @Test
        @DisplayName("Complex valid hierarchy")
        void complexValidHierarchy() {
            SimpleDocumentBuilder builder = SimpleDocumentBuilder.create("Test")
                    .addHeading("Chapter 1", 1)
                    .addHeading("Section 1.1", 2)
                    .addHeading("Subsection 1.1.1", 3)
                    .addHeading("Subsection 1.1.2", 3)
                    .addHeading("Section 1.2", 2)
                    .addHeading("Chapter 2", 1)
                    .addHeading("Section 2.1", 2);

            var elements = builder.getElements();
            int[] expectedLevels = {1, 2, 3, 3, 2, 1, 2};

            for (int i = 0; i < expectedLevels.length; i++) {
                assertEquals(expectedLevels[i],
                        ((SimpleDocument.HeadingElement) elements.get(i)).level(),
                        "Heading " + i + " should have level " + expectedLevels[i]);
            }
        }

        @Test
        @DisplayName("Exception: Level below 1")
        void levelBelowOneThrowsException() {
            SimpleDocumentBuilder builder = SimpleDocumentBuilder.create("Test");

            assertThrows(IllegalArgumentException.class,
                    () -> builder.addHeading("Invalid", 0));
        }

        @Test
        @DisplayName("Exception: Level above 6")
        void levelAboveSixThrowsException() {
            SimpleDocumentBuilder builder = SimpleDocumentBuilder.create("Test");

            assertThrows(IllegalArgumentException.class,
                    () -> builder.addHeading("Invalid", 7));
        }

        @Test
        @DisplayName("Heading after paragraph should work")
        void headingAfterParagraphWorks() {
            SimpleDocumentBuilder builder = SimpleDocumentBuilder.create("Test")
                    .addParagraph("Some text")
                    .addHeading("First Heading", 1)
                    .addParagraph("More text")
                    .addHeading("Second Heading", 2);

            var elements = builder.getElements();

            // Element 1 ist Heading H1
            assertInstanceOf(SimpleDocument.HeadingElement.class, elements.get(1));
            assertEquals(1, ((SimpleDocument.HeadingElement) elements.get(1)).level());

            // Element 3 ist Heading H2
            assertInstanceOf(SimpleDocument.HeadingElement.class, elements.get(3));
            assertEquals(2, ((SimpleDocument.HeadingElement) elements.get(3)).level());
        }
    }

    @Nested
    @DisplayName("Basic Builder Tests")
    class BasicBuilderTests {

        @Test
        @DisplayName("Create builder with valid title")
        void createBuilderWithValidTitle() {
            assertDoesNotThrow(() -> SimpleDocumentBuilder.create("Valid Title"));
        }

        @Test
        @DisplayName("Exception: null title")
        void nullTitleThrowsException() {
            assertThrows(NullPointerException.class,
                    () -> SimpleDocumentBuilder.create(null));
        }

        @Test
        @DisplayName("Exception: empty title")
        void emptyTitleThrowsException() {
            assertThrows(IllegalArgumentException.class,
                    () -> SimpleDocumentBuilder.create(""));
        }

        @Test
        @DisplayName("Exception: blank title")
        void blankTitleThrowsException() {
            assertThrows(IllegalArgumentException.class,
                    () -> SimpleDocumentBuilder.create("   "));
        }

        @Test
        @DisplayName("Convenience method addHeading(text) uses level 1")
        void convenienceMethodUsesLevelOne() {
            SimpleDocumentBuilder builder = SimpleDocumentBuilder.create("Test")
                    .addHeading("Should be H1");

            var elements = builder.getElements();
            SimpleDocument.HeadingElement heading =
                    (SimpleDocument.HeadingElement) elements.getFirst();

            assertEquals(1, heading.level());
        }

        @Test
        @DisplayName("Method chaining works")
        void methodChainingWorks() {
            SimpleDocumentBuilder builder = SimpleDocumentBuilder.create("Test")
                    .addHeading("H1", 1)
                    .addParagraph("Text")
                    .addHeading("H2", 2)
                    .withLanguage("de-DE");

            assertNotNull(builder);
            assertEquals(3, builder.getElements().size());
        }
    }

    @Nested
    @DisplayName("Paragraph Tests")
    class ParagraphTests {

        @Test
        @DisplayName("Add paragraph with text")
        void addParagraphWithText() {
            SimpleDocumentBuilder builder = SimpleDocumentBuilder.create("Test")
                    .addParagraph("Hello World");

            var elements = builder.getElements();
            assertEquals(1, elements.size());
            assertInstanceOf(SimpleDocument.ParagraphElement.class, elements.getFirst());
        }

        @Test
        @DisplayName("Exception: null paragraph text")
        void nullParagraphThrowsException() {
            SimpleDocumentBuilder builder = SimpleDocumentBuilder.create("Test");

            assertThrows(NullPointerException.class,
                    () -> builder.addParagraph(null));
        }
    }
}