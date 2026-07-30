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
package de.fkkaiser.bundle.validation;

import java.util.Set;

/**
 * Represents the result of a placeholder validation.
 * The result contains a set of missing text keys and a set of missing data keys (two Sets)
 *
 * @param missingTextKeys Set of Keys that ar in the data map but not in the text bundle
 * @param missingDataKeys Set of Keys that ar in the text bundle but not in the data map
 *
 */
public record PlaceholderValidationResult(
        Set<String> missingTextKeys,
        Set<String> missingDataKeys
) {
    /**
     * Returns true if there are no missing keys
     *
     * @return true if there are no missing keys
     */
    public boolean isValid() {
        return missingTextKeys.isEmpty() && missingDataKeys.isEmpty();
    }

    /**
     * Returns the missing text keys (keys in the data map but not in the text bundle)
     *
     * @return Set of missing text keys
     */
    public Set<String> missingTextKeys() {
        return missingTextKeys;
    }

    /**
     * Returns the missing data keys (keys in the text bundle but not in the data map)
     *
     * @return Set of missing data keys
     */
    public Set<String> missingDataKeys() {
        return missingDataKeys;
    }
}
