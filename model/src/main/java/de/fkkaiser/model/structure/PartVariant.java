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

import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;

/**
 * Enumeration representing the variants of a Part element.
 */
@PublicAPI
public enum PartVariant {
    PART("Part"),
    ARTICLE("Article");

    private final String pdfRole;

    /**
     * Constructor for PartVariant.
     * @param pdfRole The corresponding PDF/UA role for the variant.
     */
    PartVariant(String pdfRole) {
        this.pdfRole = pdfRole;
    }

    /**
     * Returns the corresponding PDF/UA structure role.
     * @return the PDF/UA role name (e.g., "Part", "Article")
     */
     @Internal
    public String getPdfRole() {
        return pdfRole;
    }

    /**
     * Returns the lowercase variant name for style resolution.
     * For example: PART → "part", ARTICLE → "article"
     * @return the lowercase variant name
     */
    public String getStyleName() {
        return name().toLowerCase();
    }

}
