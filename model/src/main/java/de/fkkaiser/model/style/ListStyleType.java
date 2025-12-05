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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;

/**
 * Enum for list style types
 * @author Katrin Kaiser
 * @version 1.0.0
 */
@PublicAPI
public enum ListStyleType {

    NONE("none"),
    BULLET("bullet"),
    CIRCLE("circle"),
    SQUARE("square"),
    NUMBER("number"),
    LOWER_ALPHA("lower-alpha"),
    UPPER_ALPHA("upper-alpha");

    private final String value;
    ListStyleType(String value) {
        this.value = value;
    }


    /**
     * Returns the (fo-) value for this type. This method is used
     * in the generator module
     * @return String value of the type
     */
    @Internal
    @JsonValue
    public String getValue() {
        return value;
    }

    /**
     * Internal method for jackson
     * @param text text for type
     * @return correct type
     */
    @Internal
    @JsonCreator
    public static ListStyleType fromString(String text) {
        if (text == null) {
            return null;
        }

        for (ListStyleType b : ListStyleType.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
            if (b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
