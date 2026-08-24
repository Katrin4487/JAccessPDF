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
package de.fkkaiser.api.simplelayer;

import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.structure.ElementTargetType;
import de.fkkaiser.model.style.*;
import de.fkkaiser.model.style.builder.StandardElementStyles;
import de.fkkaiser.model.style.builder.StandardTextStyles;

import java.util.ArrayList;
import java.util.List;


/**
 * Manages default styles for SimpleDocument.
 * <p>
 * This class provides a centralized style management system that creates
 * PDF/UA compliant styles using Open Sans as the default font family.
 * It handles text styles, element styles, and page master configurations.
 * </p>
 *
 * @author Katrin Kaiser
 * @version 1.1.0
 */
@Internal("Used internally by SimpleDocumentBuilder")
class SimpleStyleManager {

    /** style name for the default A4 page master style **/
    static final String PAGE_MASTER_STYLE_NAME = "simple-a4-portrait";

    // Element Style Identifier (names)
    /** style name for the paragraph style **/
    static final String PARAGRAPH_STYLE_NAME = "paragraph-default";
    /** prefix for the style names of heading styles (1-6) **/
    static final String PREFIX_HEADINGS_STYLE_NAME = "heading-";
    /** style name for unordered list style **/
    static final String UNORDERED_LIST_STYLE_NAME = "list-style-unordered";
    /** style name for ordered list style **/
    static final String ORDERED_LIST_STYLE_NAME = "list-style-ordered";
    /** style name for image style **/
    static final String IMAGE_STYLE_NAME = "image-style";
    /** style name for table style **/
    static final String TABLE_STYLE_NAME = "table-style";
    /** style name for table header cell style **/
    static final String TABLE_HEADER_CELL_STYLE_NAME = "header-cell";
    /** style name for normal table cell style **/
    static final String TABLE_CELL_STYLE_NAME = "cell-default";

    // Text style identifier (names)

    /**
     * Default font family used for all text styles.
     * PDF/UA requires embedded fonts for accessibility compliance.
     */
    static final String FONT_FAMILY = "Open Sans";


    /**
     * Builds a complete StyleSheet with PDF/UA compliant default styles.
     * <p>
     * The stylesheet includes:
     * <ul>
     *   <li>Text styles for default text and headings (levels 1-6)</li>
     *   <li>Element styles for paragraphs, headings, lists, images, and tables</li>
     *   <li>Page master style for A4 portrait layout</li>
     * </ul>
     * All styles use the configured font family (Open Sans by default) which must
     * be embedded for PDF/UA accessibility compliance.
     * </p>
     *
     * @return a fully configured StyleSheet with all default styles
     */
    StyleSheet buildStyleSheet() {
        return StandardElementStyles.styleSheetForFontFamily(FONT_FAMILY);
    }
}