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
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;

/**
 * Span options for text elements. This option is only necessary for page master style with
 * more than one column.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
@PublicAPI
public enum Span {


    /**
     * No spanning
     */
    NONE("none"),
    /**
     * Span across all columns
     */
    ALL("all");

    private final String value;

    /**
     * Constructor for Span enum.
     *
     * @param value the string value representing the span option
     */
    @Internal
    Span(String value) {
        this.value = value;
    }

    /**
     * Returns the string value of the span option.
     *
     * @return the string value
     */
    @Internal
    public String getValue() {
        return value;
    }

    /**
     * Creates a Span enum from a string value.
     *
     * @param text the string representation of the span option
     * @return the corresponding Span enum, or null if no match is found
     */
    @Internal
    @JsonCreator
    public static Span fromString(String text) {
        if (text == null) {
            return null;
        }

        for (Span b : Span.values()) {
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
