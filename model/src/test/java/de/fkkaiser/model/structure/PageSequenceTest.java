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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PageSequenceTest {

    @Nested
    @DisplayName("Validation Logic")
    class ValidationTests {

        @Test
        @DisplayName("should throw IllegalArgumentException when styleClass is null")
        void shouldThrowExceptionWhenStyleClassIsNull() {
            assertThrows(IllegalArgumentException.class, () -> PageSequence.builder(null).build(), "An IllegalArgumentException should be thrown for a null styleClass.");
        }

        @Test
        @DisplayName("should NOT throw an exception when styleClass is provided")
        void shouldNotThrowExceptionForValidStyleClass() {
            assertDoesNotThrow(() -> {
                PageSequence.builder("valid-style").build();
            });
        }
    }

    @Nested
    @DisplayName("Builder Logic")
    class PageSequenceBuilderTests {

        @Test
        @DisplayName("should build with only the required styleClass")
        void shouldBuildWithOnlyRequiredStyleClass() {
            PageSequence sequence = PageSequence.builder("main-style").build();

            assertEquals("main-style", sequence.styleClass());
            assertNull(sequence.body(), "Body should be null if not set.");
            assertNull(sequence.header(), "Header should be null if not set.");
            assertNull(sequence.footer(), "Footer should be null if not set.");
        }

        @Test
        @DisplayName("should build with all properties set")
        void shouldBuildWithAllPropertiesSet() {
            // ARRANGE: Erstelle Instanzen deiner echten ContentArea-Klasse
            var body = new ContentArea(); // oder wie auch immer du sie erstellst
            var header = new ContentArea();
            var footer = new ContentArea();

            // ACT: Baue das Objekt mit allen gesetzten Eigenschaften
            PageSequence sequence = PageSequence.builder("full-style")
                    .body(body)
                    .header(header)
                    .footer(footer)
                    .build();

            // ASSERT: Prüfe, ob alle Felder korrekt gesetzt wurden
            assertEquals("full-style", sequence.styleClass());
            assertEquals(body, sequence.body());
            assertEquals(header, sequence.header());
            assertEquals(footer, sequence.footer());
        }
    }
}