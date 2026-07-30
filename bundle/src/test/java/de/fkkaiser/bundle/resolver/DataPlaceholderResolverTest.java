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
package de.fkkaiser.bundle.resolver;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fkkaiser.bundle.JAccessParseException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DataPlaceholderResolverTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void shouldReplaceEmbeddedPlaceholdersWithDataValues() throws Exception {
        String json = """
                { "type": "text-run", "text": "Hallo {{name}}, hier ist Ihre {{bestellung}}." }
                """;
        JsonNode documentTree = mapper.readTree(json);

        JsonNode resolved = DataPlaceholderResolver.resolve(documentTree,
                Map.of("name", "Frau Müller", "bestellung", "Bestellung Nr. 42"));

        assertEquals("Hallo Frau Müller, hier ist Ihre Bestellung Nr. 42.", resolved.get("text").asText());
    }

    @Test
    void shouldLeaveTextWithoutPlaceholdersUnchanged() throws Exception {
        String json = """
                { "type": "text-run", "text": "Ganz normaler Text ohne Platzhalter." }
                """;
        JsonNode documentTree = mapper.readTree(json);

        JsonNode resolved = DataPlaceholderResolver.resolve(documentTree, Map.of());

        assertEquals("Ganz normaler Text ohne Platzhalter.", resolved.get("text").asText());
    }

    @Test
    void shouldThrowWhenDataValueIsMissing() throws Exception {
        String json = """
                { "type": "text-run", "text": "Hallo {{name}}." }
                """;
        JsonNode documentTree = mapper.readTree(json);

        assertThrows(JAccessParseException.class,
                () -> DataPlaceholderResolver.resolve(documentTree, Map.of()));
    }

    @Test
    void shouldHandleReplacementValuesWithRegexSpecialCharacters() throws Exception {
        String json = """
                { "type": "text-run", "text": "Betrag: {{amount}}" }
                """;
        JsonNode documentTree = mapper.readTree(json);

        JsonNode resolved = DataPlaceholderResolver.resolve(documentTree,
                Map.of("amount", "$100 \\ Rabatt"));

        assertEquals("Betrag: $100 \\ Rabatt", resolved.get("text").asText());
    }

    @Test
    void shouldRecurseIntoNestedStructures() throws Exception {
        String json = """
                {
                  "elements": [
                    {
                      "type": "part",
                      "elements": [
                        { "type": "text-run", "text": "Hallo {{name}}" }
                      ]
                    }
                  ]
                }
                """;
        JsonNode documentTree = mapper.readTree(json);

        JsonNode resolved = DataPlaceholderResolver.resolve(documentTree, Map.of("name", "Welt"));

        JsonNode nestedText = resolved.get("elements").get(0).get("elements").get(0).get("text");
        assertEquals("Hallo Welt", nestedText.asText());
    }
}
