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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fkkaiser.bundle.resolver.TextPlaceholderResolver;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextPlaceholderResolverTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldReplacePlaceholderWithTextEntryContent() throws Exception {
        String json = """
                {
                  "type": "text-run",
                  "text": "${greeting}",
                  "style-class": "normal-text-run"
                }
                """;
        JsonNode documentTree = objectMapper.readTree(json);

        TextBundle textBundle = new TextBundle(
                Map.of("greeting", new TextEntry("Hallo und willkommen!")));

        JsonNode resolved = TextPlaceholderResolver.resolve(documentTree, textBundle);

        assertEquals("Hallo und willkommen!", resolved.get("text").asText());
    }
}