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
package de.fkkaiser.model.style.builder;

import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.structure.ElementTargetType;
import de.fkkaiser.model.style.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class with standard element styles.
 * This styles can be used, if there are no custom styles defined in the document.
 * It is used e.g. in SimpleStyleManager class  (api/simplelayer/SimpleStyleManager.java) to create a default StyleSheet for SimpleDocument.
 *
 * @author Katrin Kaiser
 * @version 1.0
 */
public class StandardElementStyles {


     /** style name for the default A4 page master style **/
    public static final String PAGE_MASTER_STYLE_NAME = "simple-a4-portrait";

    /** prefix for the style names of heading styles (1-6) **/
    private static final String PREFIX_HEADINGS_STYLE_NAME = "heading-";

    // Element Style Identifier (names)
    /** style name for the paragraph style **/
    public static final String PARAGRAPH_STYLE_NAME = "paragraph-default";
    /** style name for heading 1 style **/
    @PublicAPI
    public static final String HEADING_1_STYLE_NAME = PREFIX_HEADINGS_STYLE_NAME + "1";
    /** style name for heading 2 style **/
    @PublicAPI
    public static final String HEADING_2_STYLE_NAME = PREFIX_HEADINGS_STYLE_NAME + "2";
    /** style name for heading 3 style **/
    @PublicAPI
    public static final String HEADING_3_STYLE_NAME = PREFIX_HEADINGS_STYLE_NAME + "3";
    /** style name for heading 4 style **/
    @PublicAPI
    public static final String HEADING_4_STYLE_NAME = PREFIX_HEADINGS_STYLE_NAME + "4";
    /** style name for heading 5 style **/
    @PublicAPI
    public static final String HEADING_5_STYLE_NAME = PREFIX_HEADINGS_STYLE_NAME + "5";
    /** style name for heading 6 style **/
    @PublicAPI
    public static final String HEADING_6_STYLE_NAME = PREFIX_HEADINGS_STYLE_NAME + "6";

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


    /**
     * Creates a default StyleSheet with standard element styles for a given font family.
     * @param fontFamilyName name of the font family (should exist)
     * @return a fully configured StyleSheet with all default styles
     */
    public static StyleSheet styleSheetForFontFamily(String fontFamilyName) {
        final List<ElementStyle> elementStyles = new ArrayList<>();
        final List<PageMasterStyle> pageMasterStyles = new ArrayList<>();

        final List<TextStyle> textStyles = new ArrayList<>(StandardTextStyles.forFontFamily(fontFamilyName));

        // Default paragraph element style
        ParagraphStyleProperties defaultProps = new ParagraphStyleProperties();
        defaultProps.setSpaceBefore("12pt");
        defaultProps.setTextStyleName(StandardTextStyles.REGULAR_PARAGRAPH_TEXT);
        elementStyles.add(new ElementStyle(
                PARAGRAPH_STYLE_NAME,
                ElementTargetType.PARAGRAPH,
                defaultProps
        ));


        // Create element styles for each heading level
        for (int level = 1; level <= 6; level++) {
            ParagraphStyleProperties headingProps = new ParagraphStyleProperties();
            headingProps.setTextStyleName(StandardTextStyles.PREFIX_HEADINGS_TEXT + level);
            headingProps.setSpaceBefore("18pt");
            elementStyles.add(new ElementStyle(
                    PREFIX_HEADINGS_STYLE_NAME + level,
                    ElementTargetType.HEADLINE,
                    headingProps
            ));
        }

        // Unordered list style with disc bullets
        ListStyleProperties listPropsUnordered = new ListStyleProperties();
        listPropsUnordered.setTextStyleName(StandardTextStyles.REGULAR_PARAGRAPH_TEXT);
        listPropsUnordered.setListStyleType(ListStyleType.BULLET);
        elementStyles.add(new ElementStyle(
                UNORDERED_LIST_STYLE_NAME,
                ElementTargetType.LIST,
                listPropsUnordered
        ));

        // Ordered list style with decimal numbering
        ListStyleProperties listPropsOrdered = new ListStyleProperties();
        listPropsOrdered.setTextStyleName(StandardTextStyles.REGULAR_PARAGRAPH_TEXT);
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
        tableProp.setTextStyleName(StandardTextStyles.REGULAR_PARAGRAPH_TEXT);
        elementStyles.add(new ElementStyle(
                TABLE_STYLE_NAME,
                ElementTargetType.TABLE,
                tableProp
        ));

        // Header Cells
        TableCellStyleProperties tableHeaderCellStyleProperties = new TableCellStyleProperties();
        tableHeaderCellStyleProperties.setTextStyleName(StandardTextStyles.BOLD_PARAGRAPH_TEXT);
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

        return new StyleSheet(textStyles, elementStyles, pageMasterStyles,null);
    }
}
