package de.fkkaiser.bundle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.Map;

public final class VisibilityFilter {

    public static JsonNode filter(JsonNode documentTree, Map<String, Boolean> visibility) {
        JsonNode copy = documentTree.deepCopy();
        walk(copy, visibility);
        return copy;
    }

    private static void walk(JsonNode node, Map<String, Boolean> isHiddenMap) {
        node.fields().forEachRemaining(entry -> {
            if (entry.getValue().isArray()) {
                filterArray((ArrayNode) entry.getValue(), isHiddenMap);
            } else if (entry.getValue().isObject()) {
                walk(entry.getValue(), isHiddenMap);
            }
        });
    }

    private static void filterArray(ArrayNode array, Map<String, Boolean> isHiddenMap) {
        for (int i = array.size() - 1; i >= 0; i--) {
            JsonNode element = array.get(i);
            if (isConditionalBlock(element)) {
                array.remove(i);
                if (!isHidden(element, isHiddenMap)) {
                    ArrayNode children = (ArrayNode) element.get("elements");
                    for (int j = children.size() - 1; j >= 0; j--) {
                        array.insert(i, children.get(j));
                    }
                }
            } else if (element.isContainerNode()) {
                walk(element, isHiddenMap); // in normale Elemente weiter absteigen
            }
        }
    }

    private static boolean isConditionalBlock(JsonNode node) {
        return node.isObject() && "conditional-block".equals(node.path("type").asText(null));
    }

    private static boolean isHidden(JsonNode conditionalBlock, Map<String, Boolean> data) {
        String flag = conditionalBlock.path("hidden-if").asText(null);
        return Boolean.TRUE.equals(data.get(flag));
    }
}
