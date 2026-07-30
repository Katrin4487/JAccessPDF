package de.fkkaiser.bundle.resolver;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import de.fkkaiser.bundle.CONSTANTS;
import de.fkkaiser.bundle.JAccessParseException;
import de.fkkaiser.bundle.TextEntry;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;

public final class DataPlaceholderResolver {

    public static JsonNode resolve(JsonNode documentTree, Map<String, String> data) throws JAccessParseException {
        JsonNode copy = documentTree.deepCopy();
        walk(copy, data);
        return copy;
    }

    private static void walk(JsonNode node, Map<String, String> data) throws JAccessParseException {
        if (node instanceof ObjectNode objectNode) {
            Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                JsonNode value = field.getValue();

                if ("text".equals(field.getKey()) && value.isTextual()) {
                    field.setValue(TextNode.valueOf(replacePlaceholders(value.asText(), data)));
                } else if (value.isContainerNode()) {
                    walk(value, data);
                }
            }
        } else if (node instanceof ArrayNode arrayNode) {
            for (JsonNode element : arrayNode) {
                if (element.isContainerNode()) {
                    walk(element, data); // Recursively walk the array elements
                }
            }
        }
    }

    private static String replacePlaceholders(String text, Map<String, String> data) throws JAccessParseException {
        Matcher matcher = CONSTANTS.PLACEHOLDER_PATTERN.matcher(text);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            String key = matcher.group(1);
            String value = data.get(key);
            if (value == null) {
                throw new JAccessParseException("No data value found for placeholder: " + key);
            }
            matcher.appendReplacement(result, Matcher.quoteReplacement(value));
        }
        matcher.appendTail(result);
        return result.toString();
    }
}
