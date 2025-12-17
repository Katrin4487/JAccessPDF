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

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fkkaiser.model.JsonPropertyName;
import de.fkkaiser.model.annotation.PublicAPI;

/**
 * Enum representing the ordering of a list: ordered or unordered.
 *
 * @author Katrin Kaiser
 * @version 1.0.2
 */
@PublicAPI
public enum ListOrdering {
    @JsonProperty(JsonPropertyName.UNORDERED)
    UNORDERED,
    @JsonProperty(JsonPropertyName.ORDERED)
    ORDERED;

    @Override
    public String toString() {
        return this == ORDERED ? JsonPropertyName.ORDERED : JsonPropertyName.UNORDERED;
    }
}
