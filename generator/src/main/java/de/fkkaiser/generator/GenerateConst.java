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
package de.fkkaiser.generator;

/**
 * This class contains constant values used in the generation process.
 *
 * @author Katrin Kaiser
 * @version 1.1.0
 */
public class GenerateConst {

    private GenerateConst() {
        // Private constructor to prevent instantiation
    }

    // Often used strings
    /**
     * Equals sign
     */
    public static final String EQUALS = "=";
    /**
     * Single space
     */
    public static final String SPACE = " ";
    /**
     * Closing angle bracket
     */
    public static final String CLOSER = ">";

    /**
     * Opening angle bracket
     */
    public static final String OPENER_OPEN_TAG = "<";

    /**
     * Closing tag opener
     */
    public static final String OPENER_CLOSE_TAG = "</";
    /**
     * Quote
     */
    public static final String GQQ = "\"";


    // == Tags for FOP generation ==
    /**
     * Tag for inline elements in FOP XML
     */
    public static final String INLINE = "inline";
    /**
     * Tag for block elements in FOP XML
     */
    public static final String BLOCK = "block";
    /**
     * Tag for external-graphic (image)
     */
    public static final String EXTERNAL_GRAPHIC = "external-graphic";

    /**
     * Tag for table
     */
    public static final String TABLE = "table";
    /**
     * Tag for table column
     */
    public static final String TABLE_COLUMN = "table-column";
    /**
     * Tag for table body
     */
    public static final String TABLE_BODY = "table-body";
    /**
     * Tag for table header
     */
    public static final String TABLE_HEADER = "table-header";
    /**
     * Tag for table footer
     */
    public static final String TABLE_FOOTER = "table-footer";
    /**
     * Tag for table row
     */
    public static final String TABLE_ROW = "table-row";
    /**
     * Tag for table cell
     */
    public static final String TABLE_CELL = "table-cell";
    /**
     * Tag for list (block)
     */
    public static final String LIST_BLOCK = "list-block";
    /**
     * tag for list-item
     */
    public static final String LIST_ITEM = "list-item";

    /**
     * tag for list-item-label
     */
    public static final String LIST_ITEM_LABEL = "list-item-label";

    public static final String LIST_ITEM_BODY = "list-item-body";

    // == Params for FOP generation ==
    /**
     * Parameter for id in FOP XML
     */
    public static final String ID = "id";
    /**
     * Parameter for role in FOP XML
     */
    public static final String ROLE = "role";
    /**
     * Parameter for alt text (fox) in FOP XML
     */
    public static final String ALT_TEXT = "fox:alt-text";
    /**
     * Parameter for external destination (link) in FOP XML
     */
    public static final String EXTERNAL_DESTINATION = "external-destination";
    /**
     * Parameter for color in FOP XML
     */
    public static final String COLOR = "color";

    /**
     * Parameter for text-decoration in FOP XML
     */
    public static final String TEXT_DECORATION = "text-decoration";
    /**
     * Parameter for baseline-shift FOP XML
     */
    public static final String BASELINE_SHIFT = "baseline-shift";

    /**
     * Parameter for content-type (fox) in FOP XML
     */
    public static final String CONTENT_TYPE = "fox:content-type";

    /**
     * Parameter for font-family in FOP XML
     */
    public static final String FLOW_NAME = "flow-name";
    /**
     * Parameter for font-family in FOP XML
     */
    public static final String FONT_FAMILY = "font-family";
    /**
     * Parameter for font-weight in FOP XML
     */
    public static final String FONT_WEIGHT = "font-weight";
    /**
     * Parameter for font-size in FOP XML
     */
    public static final String FONT_SIZE = "font-size";
    /**
     * Parameter for font-style in FOP XML
     */
    public static final String FONT_STYLE = "font-style";

    /**
     * Parameter for text-align in FOP XML
     */
    public static final String VERTICAL_ALIGN = "vertical-align";
    /**
     * Parameter for keep with next within page in FOP XML
     */
    public static final String KEEP_WITH_NEXT_WITHIN_PAGE = "keep-with-next.within-page";

    /**
     * Paramter for keept together within page
     */
    public static final String KEEP_TOGETHER_WITHIN_PAGE =  "keep-together.within-page";
    /**
     * Parameter for space before in FOP XML
     */
    public static final String SPACE_BEFORE = "space-before";
    /**
     * Parameter for space after in FOP XML
     */
    public static final String SPACE_AFTER = "space-after";
    /**
     * Parameter for start indent in FOP XML
     */
    public static final String START_INDENT = "start-indent";
    /**
     * Parameter for end indent in FOP XML
     */
    public static final String END_INDENT = "end-indent";
    /**
     * Parameter for background color in FOP XML
     */
    public static final String BACKGROUND_COLOR = "background-color";
    /**
     * Parameter for break before in FOP XML
     */
    public static final String BREAK_BEFORE = "break-before";
    /**
     * Parameter for break after in FOP XML
     */
    public static final String BREAK_AFTER = "break-after";
    /**
     * Parameter for padding in FOP XML
     */
    public static final String PADDING = "padding";
    /**
     * Parameter for padding top in FOP XML
     */
    public static final String PADDING_TOP = "padding-top";
    /**
     * Parameter for padding bottom in FOP XML
     */
    public static final String PADDING_BOTTOM = "padding-bottom";
    /**
     * Parameter for padding left in FOP XML
     */
    public static final String PADDING_LEFT = "padding-left";
    /**
     * Parameter for padding right in FOP XML
     */
    public static final String PADDING_RIGHT = "padding-right";
    /**
     * Parameter for border in FOP XML
     */
    public static final String BORDER = "border";
    /**
     * Parameter for border top in FOP XML
     */
    public static final String BORDER_TOP = "border-top";
    /**
     * Parameter for border bottom in FOP XML
     */
    public static final String BORDER_BOTTOM = "border-bottom";
    /**
     * Parameter for border left in FOP XML
     */
    public static final String BORDER_LEFT = "border-left";
    /**
     * Parameter for border right in FOP XML
     */
    public static final String BORDER_RIGHT = "border-right";

    /**
     * Parameter for source in FOM XML
     */
    public static final String SRC = "src";

    /**
     * Parameter for text align in FOP XML
     */
    public static final String TEXT_ALIGN = "text-align";

    /**
     * Paramter for width in FOP XML
     */
    public static final String WIDTH = "width";
    /**
     * Parameter for content width in FOP XML
     */
    public static final String CONTENT_WIDTH = "content-width";
    /**
     * Paramter for scaling in FOP XML
     */
    public static final String SCALING = "scaling";

    /**
     * Parameter for table-layout in FOP XML
     */
    public static final String TABLE_LAYOUT = "table-layout";

    /**
     * Parameter for column-width in FOP XML
     */
    public static final String COLUMN_WIDTH = "column-width";

    /**
     * Parameter for provisional disctance between starts (list item)
     */
    public static final String PROVISIONAL_DISTANCE_BETWEEN_STARTS = "provisional-distance-between-starts";
    /**
     * Parameter for provisional label separation (list item)
     */
    public static final String PROVISIONAL_LABEL_SEPARATION = "provisional-label-separation";

    /**
     * Parameter for text indent
     */
    public static final String TEXT_INDENT = "text-indent";
    /**
     * Parameter for text align last
     */
    public static final String TEXT_ALIGN_LAST = "text-align-last";
    /**
     * Parameter for Language
     */
    public static final String LANGUAGE = "language";

    /**
     * Parameter for hypenate
     */
    public static final String HYPHENATE = "hyphenate";

    /**
     * Parameter for orphans
     */
    public static final String ORPHANS = "orphans";

    /**
     * Parameter for widows
     */
    public static final String WIDOWS = "widows";
    /**
     * Parameter for border collapse
     */
    public static final String BORDER_COLLAPSE = "border-collapse";

    /**
     * Parameter for col span (table)
     */
    public static final String NUMBER_COLUMNS_SPANNED = "number-columns-spanned";
    /**
     * Parameter for line height
     */
    public static final String LINE_HEIGHT = "line-height";

    public static final String CONTENT_HEIGHT = "content-height";
    /**
     * Paramter for row span (table)
     */
    public static final String NUMBER_ROWS_SPANNED = "number-rows-spanned";
    /**
     * Paramter for display align (table)
     */
    public static final String DISPLAY_ALIGN = "display-align";
    /**
     * Parameter for span (Use SPAN for tag!)
     */
    public static final String SPAN_PARAM = "span";
    /**
     * Parameter for space before conditionality
     */
    public static final String SPACE_BEFORE_CONDITIONALITY = "space-before.conditionality";

    /**
     * Parameter for space after conditionality
     */
    public static final String SPACE_AFTER_CONDITIONALITY = "space-after.conditionality";

    public static final String LINEFEED_TREATMENT = "linefeed-treatment";

    // === Param Values

    //Roles
    /**
     * Value "Span"
     */
    public static final String SPAN = "Span";
    /**
     * Value "always"
     */
    public static final String ALWAYS = "always";

    /**
     * Value "retain"
     */
    public static final String RETAIN = "retain";
    /**
     * Value "true"
     */
    public static final String TRUE = "true";
    /**
     * Value "fixed"
     */
    public static final String FIXED = "fixed";

    /**
     * Value "external-artifact"
     */
    public static final String EXTERNAL_ARTIFACT = "external-artifact";

    // === Roles
    /**
     * Role Tag for paragraph
     */
    public static final String ROLE_PARAGRAPH = "P";

    /**
     * Role Tag (prefix) for Headlines
     */
    public static final String ROLE_HEADLINE = "H";


    /**
     * Role Tag for Div
     */
    public static final String ROLE_DIV = "Div";

    /**
     * Tag for instream-foreign-object (for SVG images)
     */
    public static final String INSTREAM_FOREIGN_OBJECT = "instream-foreign-object";



}
