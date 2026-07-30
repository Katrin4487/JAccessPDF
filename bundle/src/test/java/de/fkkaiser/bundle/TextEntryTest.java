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
package de.fkkaiser.bundle;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TextEntryTest {


    @Test
    public void shouldExtractPlaceholders() {

        String text = "Hallo {{placeholder1}} {{placeholder2}} und {{placeholder3}}";

        List<String> placeholders = new TextEntry(text).extractPlaceholders();

        assertEquals(3, placeholders.size());
        assertTrue(placeholders.contains("placeholder1"));
        assertTrue(placeholders.contains("placeholder2"));
        assertTrue(placeholders.contains("placeholder3"));
    }
}
