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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContentAreaTest {

    @Test
    @DisplayName("should replace a null elements list with an empty list")
    void shouldReplaceNullElementsWithEmptyList() {
        ContentArea contentArea = new ContentArea(null);

        assertNotNull(contentArea.elements(), "Elements list should not be null.");
        assertTrue(contentArea.elements().isEmpty(), "Elements list should be empty.");
    }

    @Test
    @DisplayName("should create an empty ContentArea with the no-args constructor")
    void shouldWorkWithNoArgsConstructor() {
        ContentArea contentArea = new ContentArea();

        assertNotNull(contentArea.elements(), "Elements list should not be null.");
        assertTrue(contentArea.elements().isEmpty(), "Elements list should be empty.");
    }
}