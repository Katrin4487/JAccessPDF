package de.fkkaiser.bundle;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Resolves text placeholders in a JSON document using the provided TextBundle.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
public class TextPlaceholderResolver {

    private static final Pattern KEY_PATTERN = Pattern.compile("^\\$\\{(.+)}$");


    /**
     * Resolves text placeholders in the given JSON document using the provided TextBundle.
     * @param documentTree The JSON document tree to resolve text placeholders in.
     * @param textBundle The TextBundle containing the text content.
     * @return The resolved JSON document tree.
     * @throws JAccessParseException If a text entry is not found for a placeholder key.
     */
    public static JsonNode resolve(JsonNode documentTree, TextBundle textBundle) throws JAccessParseException {
        JsonNode jsonNode = documentTree.deepCopy();
        walk(jsonNode, textBundle); // trows JAccessParseException if a text entry is not found
        return jsonNode;
    }

    /**
     * Recursively walks the JSON tree and replaces text placeholders with rich text from the TextBundle.
     *
     * @param node       The current JSON node being processed.
     * @param textBundle The TextBundle containing the text content.
     * @throws JAccessParseException If a text entry is not found for a placeholder key.
     */
    private static void walk(JsonNode node, TextBundle textBundle) throws JAccessParseException {
        if (node instanceof ObjectNode objectNode) {
            Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                JsonNode value = field.getValue();

                if ("text".equals(field.getKey()) && value.isTextual()) {
                    Matcher matcher = KEY_PATTERN.matcher(value.asText());
                    if (matcher.matches()) {
                        String key = matcher.group(1);
                        TextEntry entry = textBundle.getTextContent().get(key);
                        if (entry == null) {
                            throw new JAccessParseException("TextEntry not found for key: " + key);
                        }
                        field.setValue(TextNode.valueOf(entry.getRichText()));
                    }
                } else if (value.isContainerNode()) {
                    walk(value, textBundle);
                }
            }
        } else if (node instanceof ArrayNode arrayNode) {
            for (JsonNode element : arrayNode) {
                if (element.isContainerNode()) {
                    walk(element, textBundle); // Recursively walk the array elements
                }
            }
        }
    }


}
