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
    private static final String PREFIX_HEADINGS_STYLE_NAME = "h";

    private static final String SUFFIX_DEFAULT_STYLE_NAME = "-default";

    // Element Style Identifier (names)
    /** style name for the paragraph style **/
    public static final String PARAGRAPH_STYLE_NAME = "p" + SUFFIX_DEFAULT_STYLE_NAME;
    /** style name for heading 1 style **/
    @PublicAPI
    public static final String HEADING_1_STYLE_NAME = PREFIX_HEADINGS_STYLE_NAME + "1" + SUFFIX_DEFAULT_STYLE_NAME;
    /** style name for heading 2 style **/
    @PublicAPI
    public static final String HEADING_2_STYLE_NAME = PREFIX_HEADINGS_STYLE_NAME + "2" + SUFFIX_DEFAULT_STYLE_NAME;
    /** style name for heading 3 style **/
    @PublicAPI
    public static final String HEADING_3_STYLE_NAME = PREFIX_HEADINGS_STYLE_NAME + "3" + SUFFIX_DEFAULT_STYLE_NAME;
    /** style name for heading 4 style **/
    @PublicAPI
    public static final String HEADING_4_STYLE_NAME = PREFIX_HEADINGS_STYLE_NAME + "4" + SUFFIX_DEFAULT_STYLE_NAME;
    /** style name for heading 5 style **/
    @PublicAPI
    public static final String HEADING_5_STYLE_NAME = PREFIX_HEADINGS_STYLE_NAME + "5" + SUFFIX_DEFAULT_STYLE_NAME;
    /** style name for heading 6 style **/
    @PublicAPI
    public static final String HEADING_6_STYLE_NAME = PREFIX_HEADINGS_STYLE_NAME + "6" + SUFFIX_DEFAULT_STYLE_NAME;

      /** style name for unordered list style **/
    @PublicAPI
    public static final String UNORDERED_LIST_STYLE_NAME = "ul" + SUFFIX_DEFAULT_STYLE_NAME;
    /** style name for ordered list style **/
    @PublicAPI
    public static final String ORDERED_LIST_STYLE_NAME = "ol" + SUFFIX_DEFAULT_STYLE_NAME;
    /** style name for image style **/
    @PublicAPI
    public static final String IMAGE_STYLE_NAME = "image" + SUFFIX_DEFAULT_STYLE_NAME;
    /** style name for table style **/
    @PublicAPI
    public static final String TABLE_STYLE_NAME = "table" + SUFFIX_DEFAULT_STYLE_NAME;
    /** style name for table header cell style **/
    @PublicAPI
    public static final String TABLE_HEADER_CELL_STYLE_NAME = "header-cell" + SUFFIX_DEFAULT_STYLE_NAME;
    /** style name for normal table cell style **/
    @PublicAPI
    public static final String TABLE_CELL_STYLE_NAME = "cell" + SUFFIX_DEFAULT_STYLE_NAME;
    /** style name for bold inline text (e.g. Markdown "strong" / "**bold**") **/
    @PublicAPI
    public static final String STRONG_STYLE_NAME = "strong" + SUFFIX_DEFAULT_STYLE_NAME;
    /** style name for italic inline text (e.g. Markdown "em" / "*italic*") **/
    @PublicAPI
    public static final String EM_STYLE_NAME = "em" + SUFFIX_DEFAULT_STYLE_NAME;
    /** style name for inline code text (e.g. Markdown "`code`") **/
    @PublicAPI
    public static final String CODE_STYLE_NAME = "code" + SUFFIX_DEFAULT_STYLE_NAME;
    /** style name for hyperlinks **/
    @PublicAPI
    public static final String HYPERLINK_STYLE_NAME = "hyperlink" + SUFFIX_DEFAULT_STYLE_NAME;

    /**
     * Gets the default style name for a heading of the given level.
     *
     * @param level the heading level (1-6)
     * @return the default style name, e.g. "h1-default" for level 1
     * @throws IllegalArgumentException if level is not between 1 and 6
     */
    @PublicAPI
    public static String headingStyleName(int level) {
        return switch (level) {
            case 1 -> HEADING_1_STYLE_NAME;
            case 2 -> HEADING_2_STYLE_NAME;
            case 3 -> HEADING_3_STYLE_NAME;
            case 4 -> HEADING_4_STYLE_NAME;
            case 5 -> HEADING_5_STYLE_NAME;
            case 6 -> HEADING_6_STYLE_NAME;
            default -> throw new IllegalArgumentException("Heading level must be between 1 and 6, got: " + level);
        };
    }


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
            HeadlineStyleProperties headingProps = new HeadlineStyleProperties();
            headingProps.setTextStyleName(StandardTextStyles.PREFIX_HEADINGS_TEXT + level);
            headingProps.setSpaceBefore("18pt");
            elementStyles.add(new ElementStyle(
                    PREFIX_HEADINGS_STYLE_NAME + level + SUFFIX_DEFAULT_STYLE_NAME,
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

        // Bold inline text style (e.g. Markdown "**bold**")
        TextRunStyleProperties strongProps = new TextRunStyleProperties();
        strongProps.setTextStyleName(StandardTextStyles.BOLD_PARAGRAPH_TEXT);
        elementStyles.add(new ElementStyle(
                STRONG_STYLE_NAME,
                ElementTargetType.TEXT_RUN,
                strongProps
        ));

        // Italic inline text style (e.g. Markdown "*italic*")
        TextRunStyleProperties emProps = new TextRunStyleProperties();
        emProps.setTextStyleName(StandardTextStyles.ITALIC_PARAGRAPH_TEXT);
        elementStyles.add(new ElementStyle(
                EM_STYLE_NAME,
                ElementTargetType.TEXT_RUN,
                emProps
        ));

        // Inline code text style (e.g. Markdown "`code`"). No monospace font is bundled
        // with the library, so this intentionally reuses the regular font and relies on
        // a distinct color to set it apart - that avoids referencing an unembedded font
        // family, which would violate PDF/UA-1 (see PdfGenerationFacade font validation).
        TextRunStyleProperties codeProps = new TextRunStyleProperties();
        codeProps.setTextStyleName(StandardTextStyles.REGULAR_PARAGRAPH_TEXT);
        codeProps.setTextColor("#555555");
        elementStyles.add(new ElementStyle(
                CODE_STYLE_NAME,
                ElementTargetType.TEXT_RUN,
                codeProps
        ));

        // Default hyperlink style: underline, not just color, so the link is not
        // distinguishable by color alone (useful for accessibility).
        TextRunStyleProperties hyperlinkProps = new TextRunStyleProperties();
        hyperlinkProps.setTextStyleName(StandardTextStyles.REGULAR_PARAGRAPH_TEXT);
        hyperlinkProps.setTextColor("blue");
        hyperlinkProps.setTextDecoration("underline");
        elementStyles.add(new ElementStyle(
                HYPERLINK_STYLE_NAME,
                ElementTargetType.HYPERLINK,
                hyperlinkProps
        ));

        // A4 portrait page master
        PageMasterStyle pageMaster = new PageMasterStyle(PAGE_MASTER_STYLE_NAME);
        pageMasterStyles.add(pageMaster);

        return new StyleSheet(textStyles, elementStyles, pageMasterStyles,null);
    }
}
