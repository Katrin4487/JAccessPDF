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
package de.fkkaiser.model.style;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import de.fkkaiser.model.annotation.Internal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * Custom Jackson Deserializer for DefaultStyles.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
@Internal
public class DefaultStylesDeserializer extends JsonDeserializer<DefaultStyles> {

    private static final Logger log = LoggerFactory.getLogger(DefaultStylesDeserializer.class);

    /**
     * Deserializes JSON into a DefaultStyles object.
     *
     * @param p the JsonParser
     * @param ctxt the DeserializationContext
     * @return the deserialized DefaultStyles object
     * @throws IOException if an I/O error occurs
     */
    @Override
    public DefaultStyles deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        DefaultStyles defaults = new DefaultStyles();
        JsonNode node = p.getCodec().readTree(p);

        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            try {
                StandardElementType type = StandardElementType.fromJsonKey(field.getKey());
                defaults.set(type, field.getValue().asText());
            } catch (IllegalArgumentException e) {
                log.warn("Unknown standard element type in default styles: {}", field.getKey());
            }
        }

        return defaults;
    }
}