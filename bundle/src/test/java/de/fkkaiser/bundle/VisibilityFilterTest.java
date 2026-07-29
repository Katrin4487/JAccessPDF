package de.fkkaiser.bundle;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class VisibilityFilterTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String JSON_WITH_CONDITIONAL_BLOCK = """
            {
              "elements": [
                { "type": "paragraph", "text": "before" },
                {
                  "type": "conditional-block",
                  "hidden-if": "hideContainer1",
                  "elements": [
                    { "type": "paragraph", "text": "conditional-content" }
                  ]
                },
                { "type": "paragraph", "text": "after" }
              ]
            }
            """;

    @Test
    void shouldRemoveBlockWhenFlagIsTrue() throws Exception {
        JsonNode documentTree = objectMapper.readTree(JSON_WITH_CONDITIONAL_BLOCK);

        JsonNode resolved = VisibilityFilter.filter(documentTree, Map.of("hideContainer1", true));

        JsonNode elements = resolved.get("elements");
        assertEquals(2, elements.size());
        assertEquals("before", elements.get(0).get("text").asText());
        assertEquals("after", elements.get(1).get("text").asText());
    }

    @Test
    void shouldSpliceChildrenInPlaceWhenFlagIsFalse() throws Exception {
        JsonNode documentTree = objectMapper.readTree(JSON_WITH_CONDITIONAL_BLOCK);

        JsonNode resolved = VisibilityFilter.filter(documentTree, Map.of("hideContainer1", false));

        JsonNode elements = resolved.get("elements");
        assertEquals(3, elements.size());
        assertEquals("before", elements.get(0).get("text").asText());
        assertEquals("conditional-content", elements.get(1).get("text").asText());
        assertEquals("after", elements.get(2).get("text").asText());
    }

    @Test
    void shouldShowBlockByDefaultWhenFlagIsMissing() throws Exception {
        JsonNode documentTree = objectMapper.readTree(JSON_WITH_CONDITIONAL_BLOCK);

        JsonNode resolved = VisibilityFilter.filter(documentTree, Map.of());

        JsonNode elements = resolved.get("elements");
        assertEquals(3, elements.size());
        assertEquals("conditional-content", elements.get(1).get("text").asText());
    }

    @Test
    void shouldNotLeaveConditionalBlockTypeInResult() throws Exception {
        JsonNode documentTree = objectMapper.readTree(JSON_WITH_CONDITIONAL_BLOCK);

        JsonNode resolved = VisibilityFilter.filter(documentTree, Map.of("hideContainer1", false));

        for (JsonNode element : resolved.get("elements")) {
            assertNotEquals("conditional-block", element.path("type").asText(null));
        }
    }

    @Test
    void shouldFilterNestedConditionalBlocks() throws Exception {
        String nestedJson = """
                {
                  "elements": [
                    {
                      "type": "part",
                      "elements": [
                        { "type": "paragraph", "text": "outer" },
                        {
                          "type": "conditional-block",
                          "hidden-if": "hideNested",
                          "elements": [
                            { "type": "paragraph", "text": "nested-content" }
                          ]
                        }
                      ]
                    }
                  ]
                }
                """;
        JsonNode documentTree = objectMapper.readTree(nestedJson);

        JsonNode resolved = VisibilityFilter.filter(documentTree, Map.of("hideNested", true));

        JsonNode partElements = resolved.get("elements").get(0).get("elements");
        assertEquals(1, partElements.size());
        assertEquals("outer", partElements.get(0).get("text").asText());
    }
}
