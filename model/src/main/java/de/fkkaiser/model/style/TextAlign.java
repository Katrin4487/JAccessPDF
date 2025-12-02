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
 * Enumeration representing text alignment options.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
@PublicAPI
public enum TextAlign {

    /**
     * Text aligned to the start of the line.
     */
    START("start"),
    /**
     * Text aligned to the end of the line, regarding the text direction.
     */
    END("end"),
    /**
     * Text centered within the line, regarding text direction.
     */
    CENTER("center"),
    /**
     * Text justified to fill the line.
     */
    JUSTIFY("justify"),
    /**
     * Text aligned to the left side.
     */
    LEFT("left"),
    /**
     * Text aligned to the right side.
     */
    RIGHT("right");

    private final String value;

    @PublicAPI
    TextAlign(String value) {
        this.value = value;
    }

    @Internal
    public String getValue() {
        return value;
    }

    /**
     * Creates a TextAlign from a string representation.
     *
     * @param text the string representation of the text alignment
     * @return the corresponding TextAlign, or null if not found
     */
    @Internal
    @JsonCreator
    public static TextAlign fromString(String text) {
        if (text == null) {
            return null;
        }
        for (TextAlign b : TextAlign.values()) {
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
