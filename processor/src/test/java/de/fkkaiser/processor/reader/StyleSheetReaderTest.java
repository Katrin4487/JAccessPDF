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
package de.fkkaiser.processor.reader;

import de.fkkaiser.model.style.StyleSheet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StyleSheetReader Tests")
class StyleSheetReaderTest {

    private StyleSheetReader reader;

    @BeforeEach
    void setUp() {
        reader = new StyleSheetReader();
    }

    @Test
    @DisplayName("should parse valid JSON with text styles")
    void shouldParseValidJson() throws JsonReadException {
        String json = """
        {
          "text-styles": [
            { "font-family-name":"Arial","name": "normal" , "font-size":"12pt", "font-weight":"700", "font-style":"italic" }
          ]
        }
        """;
        InputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

        // ACT
        StyleSheet result = reader.readJson(stream);

        // ASSERT
        assertNotNull(result);
        assertNotNull(result.textStyles());
        assertFalse(result.textStyles().isEmpty());
    }

    @Test
    @DisplayName("should throw JsonReadException if textStyles is empty")
    void shouldThrowExceptionForEmptyTextStyles() {
        // ARRANGE
        String json = "{ \"text-styles\": [] }";
        InputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

        // ACT & ASSERT
        JsonReadException exception = assertThrows(JsonReadException.class, () -> reader.readJson(stream));

        assertTrue(exception.getMessage().contains("has no text styles"));
    }

    @Test
    @DisplayName("should throw JsonReadException for malformed JSON")
    void shouldThrowExceptionForMalformedJson() {
        // ARRANGE
        String json = "{ \"text-styles\": [ }"; // Syntaxfehler
        InputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

        assertThrows(JsonReadException.class, () -> reader.readJson(stream));
    }
}
