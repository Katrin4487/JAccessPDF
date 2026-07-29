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