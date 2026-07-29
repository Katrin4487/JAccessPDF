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
