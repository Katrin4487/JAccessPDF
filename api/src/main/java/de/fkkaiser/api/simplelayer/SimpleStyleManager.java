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
import de.fkkaiser.model.style.builder.ParagraphStyleBuilder;

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
    private static final String REGULAR_PARAGRAPH_TEXT = "text-default";
    private static final String PREFIX_HEADINGS_TEXT = "text-heading-";
    private static final String BOLD_PARAGRAPH_TEXT = "text-bold-default";

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
        final List<TextStyle> textStyles = new ArrayList<>();
        final List<ElementStyle> elementStyles = new ArrayList<>();
        final List<PageMasterStyle> pageMasterStyles = new ArrayList<>();

        // Create the default text style with base font settings
        textStyles.add(new TextStyle(
                REGULAR_PARAGRAPH_TEXT,
                "12px",       // Standardgröße
                FONT_FAMILY,
                "400",        // normal weight
                "normal"
        ));

        // Create heading text styles with decreasing font sizes
        // Level 1: 24px, Level 2: 22px, Level 3: 20px, etc.
        for (int level = 1; level <= 6; level++) {
            int fontSize = 24 - (level - 1) * 2;
            textStyles.add(new TextStyle(
                    PREFIX_HEADINGS_TEXT + level,
                    fontSize + "px",
                    FONT_FAMILY,
                    "700",      // bold weight
                    "normal"
            ));
        }

        textStyles.add(new TextStyle(
                BOLD_PARAGRAPH_TEXT,
                "12px",
                FONT_FAMILY,
                "700",
                "normal"
        ));

        // Default paragraph element style
        ParagraphStyleProperties defaultProps = new ParagraphStyleProperties();
        defaultProps.setSpaceBefore("1em");
        defaultProps.setTextStyleName(REGULAR_PARAGRAPH_TEXT);
        elementStyles.add(new ElementStyle(
                PARAGRAPH_STYLE_NAME,
                ElementTargetType.PARAGRAPH,
                defaultProps
        ));

        // Create element styles for each heading level
        for (int level = 1; level <= 6; level++) {
            ParagraphStyleProperties headingProps = new ParagraphStyleProperties();
            headingProps.setTextStyleName(PREFIX_HEADINGS_TEXT + level);
            headingProps.setSpaceBefore("1.5em");
            elementStyles.add(new ElementStyle(
                    PREFIX_HEADINGS_STYLE_NAME + level,
                    ElementTargetType.HEADLINE,
                    headingProps
            ));
        }

        // Unordered list style with disc bullets
        ListStyleProperties listPropsUnordered = new ListStyleProperties();
        listPropsUnordered.setTextStyleName(REGULAR_PARAGRAPH_TEXT);
        listPropsUnordered.setListStyleType(ListStyleType.BULLET);
        elementStyles.add(new ElementStyle(
                UNORDERED_LIST_STYLE_NAME,
                ElementTargetType.LIST,
                listPropsUnordered
        ));

        // Ordered list style with decimal numbering
        ListStyleProperties listPropsOrdered = new ListStyleProperties();
        listPropsOrdered.setTextStyleName(REGULAR_PARAGRAPH_TEXT);
        listPropsOrdered.setListStyleType(ListStyleType.NUMBER);
        elementStyles.add(new ElementStyle(
                ORDERED_LIST_STYLE_NAME,
                ElementTargetType.LIST,
                listPropsOrdered
        ));

        // Default image style with centered alignment and uniform scaling
        BlockImageStyleProperties imagePropsDefault = new BlockImageStyleProperties();
        imagePropsDefault.setAlignment("center");
        imagePropsDefault.setScaling("uniform");
        imagePropsDefault.setContentWidth("auto");
        elementStyles.add(new ElementStyle(
                IMAGE_STYLE_NAME,
                ElementTargetType.BLOCK_IMAGE,
                imagePropsDefault
        ));

        // Default table style
        TableStyleProperties tableProp = new TableStyleProperties();
        tableProp.setTextStyleName(REGULAR_PARAGRAPH_TEXT);
        elementStyles.add(new ElementStyle(
                TABLE_STYLE_NAME,
                ElementTargetType.TABLE,
                tableProp
        ));

        // Header Cells
        TableCellStyleProperties tableHeaderCellStyleProperties = new TableCellStyleProperties();
        tableHeaderCellStyleProperties.setTextStyleName(BOLD_PARAGRAPH_TEXT);
        elementStyles.add(new ElementStyle(
                TABLE_HEADER_CELL_STYLE_NAME,
                ElementTargetType.TABLE_CELL,
                tableHeaderCellStyleProperties
        ));

        // Default table cell style
        TableCellStyleProperties tableCellStyleProperties = new TableCellStyleProperties();
        elementStyles.add(new ElementStyle(
                TABLE_CELL_STYLE_NAME,
                ElementTargetType.TABLE_CELL,
                tableCellStyleProperties
        ));

        // A4 portrait page master
        PageMasterStyle pageMaster = new PageMasterStyle(PAGE_MASTER_STYLE_NAME);
        pageMasterStyles.add(pageMaster);

        return new StyleSheet(textStyles, elementStyles, pageMasterStyles);
    }
}