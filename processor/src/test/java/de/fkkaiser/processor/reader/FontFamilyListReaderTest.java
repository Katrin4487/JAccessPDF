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

import de.fkkaiser.model.font.FontFamilyList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("FontFamilyListReader Tests")
class FontFamilyListReaderTest {

    private FontFamilyListReader reader;

    @BeforeEach
    void setUp() {
        reader = new FontFamilyListReader();
    }

    @Test
    @DisplayName("should read valid JSON and return a FontFamilyList object")
    void shouldReadValidJsonAndReturnList() throws JsonReadException {
        // ARRANGE: Ein gültiger JSON-String.
        String validJson = """
        {
          "font-families": [
            { "font-family": "Arial", "types": [{"font-style": "normal", "font-weight": "normal", "path": "arial.ttf"}] }
          ]
        }
        """;
        InputStream inputStream = new ByteArrayInputStream(validJson.getBytes(StandardCharsets.UTF_8));
        FontFamilyList result = reader.readJson(inputStream);
        assertNotNull(result, "The result should not be null for valid JSON.");

    }

    @Test
    @DisplayName("should throw JsonReadException for malformed JSON")
    void shouldThrowJsonReadExceptionForMalformedJson() {

        String malformedJson = """
        {
          "font-families": [
            { "font-family": "Arial" }
        """;
        InputStream inputStream = new ByteArrayInputStream(malformedJson.getBytes(StandardCharsets.UTF_8));

        // ACT & ASSERT: Prüfen, ob die korrekte Exception geworfen wird.
        assertThrows(JsonReadException.class, () -> reader.readJson(inputStream));
    }

    @Test
    @DisplayName("should throw JsonReadException for malformed JSON")
    void shouldThrowJsonReadExceptionForEmptyFontFamilies() {

        String malformedJson = """
        {
          "font-families": [
            ]
            }
        """;
        InputStream inputStream = new ByteArrayInputStream(malformedJson.getBytes(StandardCharsets.UTF_8));

        assertThrows(JsonReadException.class, () -> reader.readJson(inputStream));
    }

    @Test
    @DisplayName("should throw NullPointerException for null input stream")
    void shouldThrowNullPointerExceptionForNullInputStream() {

        assertThrows(NullPointerException.class, () -> reader.readJson(null));
    }
}