package de.fkkaiser.bundle.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import de.fkkaiser.bundle.*;

import java.util.*;
import java.util.regex.Matcher;

/**
 * Validates the placeholders in a template against the provided data.
 * This class checks for missing text keys and data keys.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
public final class TemplateValidator {

    /**
     * Validates the placeholders in the given template against the provided data.
     * @param template The template to validate.
     * @param data The data to validate against.
     * @return A PlaceholderValidationResult containing the missing text keys and data keys.
     */
    public static PlaceholderValidationResult validate(MasterBundle template, Map<String, String> data) {
        Set<String> missingTextKeys = findMissingTextKeys(template.documentTree(), template.textBundle());
        Set<String> missingDataKeys = findMissingDataKeys(template.textBundle(), data);
        return new PlaceholderValidationResult(missingTextKeys, missingDataKeys);
    }

    // =========================== Helper Methods ===========================
    
    private static Set<String> findMissingTextKeys(JsonNode node, TextBundle textBundle) {
        Set<String> missingKeys = new HashSet<>();
        walk(node, textBundle, missingKeys);
        return missingKeys;
    }

    private static Set<String> findMissingDataKeys(TextBundle textBundle, Map<String, String> data) {
        Set<String> missingDataKeys = new HashSet<>(Set.of());
        for (TextEntry entry : textBundle.getTextContent().values()) {
            List<String> placeholders = entry.extractPlaceholders();
            for (String placeholder : placeholders) {
                if (!data.containsKey(placeholder)) {
                    missingDataKeys.add(placeholder);
                }
            }
        }
        return missingDataKeys;
    }


    /**
     * Recursively walks the JSON tree and collect text placeholders with rich text from the TextBundle.
     *
     * @param node       The current JSON node being processed.
     * @param textBundle The TextBundle containing the text content.
     */
    private static void walk(JsonNode node, TextBundle textBundle,Set<String> missingKeys){
        if (node instanceof ObjectNode objectNode) {

            Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                JsonNode value = field.getValue();

                if ("text".equals(field.getKey()) && value.isTextual()) {
                    Matcher matcher = CONSTANTS.KEY_PATTERN.matcher(value.asText());
                    if (matcher.matches()) {
                        String key = matcher.group(1);
                        TextEntry entry = textBundle.getTextContent().get(key);
                        if (entry == null) {
                            missingKeys.add(key);
                        }
                    }
                } else if (value.isContainerNode()) {
                    walk(value, textBundle,missingKeys);
                }
            }
        } else if (node instanceof ArrayNode arrayNode) {
            for (JsonNode element : arrayNode) {
                if (element.isContainerNode()) {
                    walk(element, textBundle,missingKeys); // Recursively walk the array elements
                }
            }
        }
    }
}
