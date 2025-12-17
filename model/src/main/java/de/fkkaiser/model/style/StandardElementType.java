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
import de.fkkaiser.model.structure.ElementTargetType;

/**
 * Enum representing standard element types.
 * This enum is used for mapping default styles to common document elements.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
public enum StandardElementType {
    H1("h1", ElementTargetType.HEADLINE),
    H2("h2", ElementTargetType.HEADLINE),
    H3("h3", ElementTargetType.HEADLINE),
    H4("h4", ElementTargetType.HEADLINE),
    H5("h5", ElementTargetType.HEADLINE),
    H6("h6", ElementTargetType.HEADLINE),
    P("p", ElementTargetType.PARAGRAPH),
    UL("ul", ElementTargetType.LIST),
    OL("ol", ElementTargetType.LIST),
    LI("li", ElementTargetType.LIST_ITEM),
    TABLE("table", ElementTargetType.TABLE),
    IMAGE("image", ElementTargetType.BLOCK_IMAGE),
    HYPERLINK("hyperlink", ElementTargetType.HYPERLINK),
    FOOTNOTE("footnote", ElementTargetType.FOOTNOTE),
    PART("part", ElementTargetType.PART),
    SECTION("section", ElementTargetType.SECTION);

    private final String jsonKey;
    private final ElementTargetType targetType;

    /**
     * Constructor for StandardElementType enum.
     *
     * @param jsonKey    the JSON key associated with the element type
     * @param targetType the target type of the element
     */
    StandardElementType(String jsonKey, ElementTargetType targetType) {
        this.jsonKey = jsonKey;
        this.targetType = targetType;
    }

    /**
     * Gets the JSON key associated with the element type.
     *
     * @return the JSON key
     */
    public String getJsonKey() {
        return jsonKey;
    }
    /**
     * Gets the target type of the element.
     *
     * @return the element target type
     */
    @SuppressWarnings("unused")
    public ElementTargetType getTargetType() {
        return targetType;
    }

    /**
     * Gets the default style name for the element type.
     *
     * @return the default style name
     */
    public String getDefaultStyleName() {
        return jsonKey + "-default";
    }

    /**
     * Creates a StandardElementType enum constant from the given JSON key.
     *
     * @param key the JSON key representing the element type
     * @return the corresponding StandardElementType enum constant
     * @throws IllegalArgumentException if no matching element type is found
     */
    @JsonCreator
    public static StandardElementType fromJsonKey(String key) {
        for (StandardElementType type : values()) {
            if (type.jsonKey.equals(key)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown element type: " + key);
    }
}

