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

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Headline Class Tests")
class HeadlineTest {

    @Test
    @DisplayName("should apply default level when it is null in JSON")
    void shouldApplyDefaultLevelWhenNull() {
        // Act
        Headline headline = new Headline("h1-style", null, null, null);

        // Assert
        assertEquals(1, headline.getLevel(), "Default level should be 1 when null is provided.");
    }

    @Test
    @DisplayName("should use the explicitly provided level")
    void shouldUseExplicitlyProvidedLevel() {
        // Act
        Headline headline = new Headline("h3-style", null, null, 3);

        // Assert
        assertEquals(3, headline.getLevel(), "The explicit level 3 should be used.");
    }
}