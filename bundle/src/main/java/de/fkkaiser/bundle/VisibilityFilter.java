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
