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
 * Enum representing different treatments for linefeeds in text processing.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
@PublicAPI
public enum LinefeedTreatment {

    /**
     * Preserve linefeeds as they are.
     */
    PRESERVE("preserve"),
    /**
     * Treat linefeeds as spaces.
     */
    TREAT_AS_SPACE("treat-as-space"),
    /**
     * Treat linefeeds as zero-width spaces.
     */
    TREAT_AS_ZERO_WITH_SPACE("treat-as-zero-with-space"),
    /**
     * Remove linefeeds entirely.
     */
    REMOVE("remove"),
    /**
     * Ignore linefeeds.
     */
    IGNORE("ignore");

    private final String value;

    /**
     * Constructor for LinefeedTreatment enum.
     *
     * @param value The string value associated with the enum constant.
     */
    LinefeedTreatment(String value) {
        this.value = value;
    }

    /**
     * Gets the string value associated with the enum constant.
     *
     * @return The string value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Creates a LinefeedTreatment from a string representation.
     *
     * @param text the string representation of the linefeed treatment
     * @return the corresponding LinefeedTreatment, or null if not found
     */
    @Internal
    @JsonCreator
    public static LinefeedTreatment fromString(String text) {
        if (text == null) {
            return null;
        }

        for (LinefeedTreatment b : LinefeedTreatment.values()) {
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
