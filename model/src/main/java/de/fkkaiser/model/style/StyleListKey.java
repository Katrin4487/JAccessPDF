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
import de.fkkaiser.model.JsonPropertyName;
import de.fkkaiser.model.annotation.PublicAPI;

/**
 * Enum for all available style list keys in the JSON representation of the document model.
 * This enum is used to map the JSON keys to their corresponding style list types.
 *
 * @author Katrin Kaiser
 * @version 1.0
 */
public enum StyleListKey {

    /**
     * Key for text styles in the JSON representation.
     */
    TEXT_STYLES(JsonPropertyName.TEXT_STYLES),
    /**
     * Key for element styles in the JSON representation
     */
    ELEMENT_STYLES(JsonPropertyName.ELEMENT_STYLES),
    /**
     * Key for page master styles in the JSON representation.
     */
    PAGE_MASTER_STYLES(JsonPropertyName.PAGE_MASTER_STYLES);

    private final String jsonKey;

    StyleListKey(String jsonKey) {
        this.jsonKey = jsonKey;
    }

    /**
     * Returns the JSON key associated with this style list key.
     * @return JSON key
     */
    @PublicAPI
    public String getJsonKey() {
        return jsonKey;
    }

    /**
     * Generates the StyleListKey from the JSON key.
     * @param jsonKey key used in the JSON
     * @return StyleListKey element
     * @throws IllegalArgumentException if the JSON key is unknown
     */
    @PublicAPI
    public static StyleListKey fromJsonKey(String jsonKey) throws IllegalArgumentException {
        for (StyleListKey key : StyleListKey.values()) {
            if (key.getJsonKey().equals(jsonKey)) {
                return key;
            }
        }
        throw new IllegalArgumentException("Unknown JSON key: " + jsonKey);
    }
}
