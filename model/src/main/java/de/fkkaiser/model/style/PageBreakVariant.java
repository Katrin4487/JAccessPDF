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
 * Page break variants for sections.
 * <p>Available are:</p>
 * <ul>
 *   <li>"AUTO" - No forced break (default)</li>
 *   <li>"PAGE" - Start section on a new page</li>
 *   <li>"COLUMN" - Start section in a new column</li>
 *   <li>"EVEN_PAGE" - Start section on an even-numbered page</li>
 *   <li>"ODD_PAGE" - Start section on an odd-numbered page</li>
 * </ul>
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 *
 */
@PublicAPI
public enum PageBreakVariant {


    /**
     * No forced break (default)
     */
    AUTO("auto"),
    /**
     * Start section on a new page
     */
    PAGE("page"),
    /**
     * Start section in a new column
     */
    COLUMN("column"),
    /**
     * Start section on an even-numbered page
     */
    EVEN_PAGE("even-page"),
    /**
     * Start section on an odd-numbered page
     */
    ODD_PAGE("odd-page");

    private final String foValue;

    PageBreakVariant(String foValue) {
        this.foValue = foValue;
    }

    /**
     * Returns the corresponding XSL-FO value for the page break variant.
     *
     * @return the XSL-FO break-before value
     */
    public String getFoValue() {
        return foValue;
    }

    /**
     * Creates a PageBreakVariant from a string representation.
     *
     * @param text the string representation of the page break variant
     * @return the corresponding PageBreakVariant, or null if not found
     */
    @Internal
    @JsonCreator
    public static PageBreakVariant fromString(String text) {
        if (text == null) {
            return null;
        }

        for (PageBreakVariant b : PageBreakVariant.values()) {
            if (b.foValue.equalsIgnoreCase(text)) {
                return b;
            }
            if (b.name().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
