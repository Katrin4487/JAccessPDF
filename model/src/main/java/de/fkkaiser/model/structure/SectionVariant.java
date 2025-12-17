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

import de.fkkaiser.model.JsonPropertyName;
import de.fkkaiser.model.annotation.PublicAPI;

/**
 * Defines the semantic variants for sections that map to PDF/UA structure types.
 * Each variant determines both the visual styling and the accessibility role.
 *
 * @author Katrin Kaiser
 * @version 1.0.1
 */
@PublicAPI
public enum SectionVariant {

    /**
     * Standard section (default).
     * Maps to PDF/UA role "Sect".
     * Use for general content grouping.
     */
    SECTION(JsonPropertyName.SECT),

    /**
     * Note or important information.
     * Maps to PDF/UA role "Note".
     * Use for warnings, errors, important notices, tips.
     */
    NOTE(JsonPropertyName.NOTE),

    /**
     * Complementary or aside content.
     * Maps to PDF/UA role "Aside".
     * Use for examples, sidebars, supplementary information.
     */
    ASIDE(JsonPropertyName.ASIDE);

    private final String pdfRole;

    SectionVariant(String pdfRole) {
        this.pdfRole = pdfRole;
    }

    /**
     * Returns the corresponding PDF/UA structure role.
     *
     * @return the PDF/UA role name (e.g., "Sect", "Note", "Aside")
     */
    public String getPdfRole() {
        return pdfRole;
    }

    /**
     * Returns the lowercase variant name for style resolution.
     * For example: SECTION → "section", NOTE → "note"
     *
     * @return the lowercase variant name
     */
    public String getStyleName() {
        return name().toLowerCase();
    }
}