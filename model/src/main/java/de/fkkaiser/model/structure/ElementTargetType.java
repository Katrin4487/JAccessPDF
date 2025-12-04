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
package de.fkkaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import de.fkkaiser.model.JsonPropertyName;
import de.fkkaiser.model.annotation.Internal;

/**
 * Enumeration of different element target types in the document structure.
 * Each enum constant corresponds to a specific type of document element,
 * such as paragraphs, headlines, lists, tables, sections, parts, block images,
 * layout tables, list items, table cells, text runs, footnotes, hyperlinks, and page numbers.
 *
 * @author Katrin Kaiser
 * @version 1.0.1
 */
public enum ElementTargetType {
    PARAGRAPH(JsonPropertyName.PARAGRAPH),
    HEADLINE(JsonPropertyName.HEADLINE),
    LIST(JsonPropertyName.LIST),
    TABLE(JsonPropertyName.TABLE),
    SECTION(JsonPropertyName.SECTION),
    PART(JsonPropertyName.PART),
    BLOCK_IMAGE(JsonPropertyName.BLOCK_IMAGE),
    LAYOUT_TABLE(JsonPropertyName.LAYOUT_TABLE),
    LIST_ITEM(JsonPropertyName.LIST_ITEM),
    TABLE_CELL(JsonPropertyName.TABLE_CELL),
    TEXT_RUN(JsonPropertyName.TEXT_RUN),
    FOOTNOTE(JsonPropertyName.FOOTNOTE),
    HYPERLINK(JsonPropertyName.HYPERLINK),
    PAGE_NUMBER(JsonPropertyName.PAGE_NUMBER);

    private final String value;

    ElementTargetType(String value) {
        this.value = value;
    }

    /**
     * Returns the string value associated with the enum constant.
     * @return the string value of the enum constant
     */
    @Internal
    @JsonValue
    public String getValue() {
        return  value;
    }

    /**
     * Creates an ElementTargetType enum constant from the given string.
     * The comparison is case-insensitive and checks both the enum name
     * and the associated string value.
     *
     * @param text the string representation of the element target type
     * @return the corresponding ElementTargetType enum constant, or null if no match is found
     */
    @Internal
    @JsonCreator
    public static ElementTargetType fromString(String text) {
        if (text == null) {
            return null;
        }

        for (ElementTargetType b : ElementTargetType.values()) {
            if (b.getValue().equalsIgnoreCase(text)) {
                return b;
            }
            if (b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
