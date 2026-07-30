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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fkkaiser.bundle.resolver.DataPlaceholderResolver;
import de.fkkaiser.bundle.resolver.TextPlaceholderResolver;
import de.fkkaiser.model.structure.Document;

import java.util.Map;

/**
 * Engine that resolves placeholders in the document tree and generates a PDF document.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
public final class MasterBundleEngine {

    /**
     * Resolves placeholders in the document tree and generates a PDF document.
     * @param template The template document to resolve placeholders in.
     * @param hiddenMap A map of hidden elements.
     * @param mapper The Jackson mapper to use for deserialization.
     * @return The resolved PDF document.
     * @throws JAccessParseException If a text entry is not found for a placeholder key.
     * @throws JsonProcessingException If there is an error during deserialization.
     */
    public static Document resolve(MasterBundle template, Map<String, Boolean> hiddenMap, Map<String, String> dataMap,
                                   ObjectMapper mapper) throws JAccessParseException, JsonProcessingException {
        JsonNode tree = VisibilityFilter.filter(template.documentTree(), hiddenMap);
        tree = TextPlaceholderResolver.resolve(tree, template.textBundle());
        tree = DataPlaceholderResolver.resolve(tree, dataMap);
        return mapper.treeToValue(tree, Document.class);
    }
}
