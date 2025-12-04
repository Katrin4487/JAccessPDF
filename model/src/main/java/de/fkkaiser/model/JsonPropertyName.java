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
package de.fkkaiser.model;

import de.fkkaiser.model.annotation.Internal;


/**
 * Contains JSON property names used in the model.
 *
 * @author Katrin Kaiser
 * @version 1.0.1
 */
@Internal
public class JsonPropertyName {

    private JsonPropertyName() {
        // private constructor to prevent instantiation
    }

    // === Identifier for TargetTypes

    /**
     * Identifier for Headline TargetType
     */
    public static final String HEADLINE = "headline";
    /**
     * Identifier for Paragraph TargetType
     */
    public static final String PARAGRAPH = "paragraph";
    /**
     * Identifier for List TargetType
     */
    public static final String LIST = "list";
    /**
     * Identifier for Table TargetType
     */
    public static final String TABLE = "table";
    /**
     * Identifier for Section TargetType
     */
    public static final String SECTION = "section";
    /**
     * Identifier for Part TargetType
     */
    public static final String PART = "part";
    /**
     * Identifier for BlockImage TargetType
     */
    public static final String BLOCK_IMAGE = "block-image";
    /**
     * Identifier for LayoutTable TargetType
     */
    public static final String LAYOUT_TABLE = "layout-table";
    /**
     * Identifier for ListItem TargetType
     */
    public static final String LIST_ITEM = "list-item";
    /**
     * Identifier for TableCell TargetType
     */
    public static final String TABLE_CELL = "table-cell";
    /**
     * Identifier for TextRun TargetType
     */
    public static final String TEXT_RUN = "text-run";
    /**
     * Identifier for Footnote TargetType
     */
    public static final String FOOTNOTE = "footnote";
    /**
     * Identifier for PageNumber TargetType
     */
    public static final String PAGE_NUMBER = "page-number";
    /**
     * Identifier for Hyperlink TargetType
     */
    public static final String HYPERLINK = "hyperlink";

    // === Properties for structure elements
    /**
     * Property name for style class (CSS-like style class)
     */
    public static final String STYLE_CLASS = "style-class";
    /**
     * Property name for path (e.g., image path)
     */
    public static final String PATH = "path";
    /**
     * Property name for alternative text (alt-text)
     */
    public static final String ALT_TEXT = "alt-text";

    /**
     * Property name for inline elements within blocks
     */
    public static final String INLINE_ELEMENTS = "inline-elements";

    /**
     * Property name for level (e.g., headline level)
     */
    public static final String LEVEL = "level";
    /**
     * Property name for href of hyperlinks
     */
    public static final String HREF = "href";
    /**
     * Property name for (visible) text (e.g. in hyperlinks)
     */
    public static final String TEXT = "text";

    /**
     * Property name for the font dictionary (internal addresses)
     */
    public static final String FONT_DICTIONARY = "font-dictionary";
    /**
     * Property name for the image dictionary (internal addresses)
     */
    public static final String IMAGE_DICTIONARY = "image-dictionary";
    /**
     * Property name for the left element in an layout table
     */
    public static final String ELEMENT_LEFT = "element-left";
    /**
     * Property name for the right element in an layout table
     */
    public static final String ELEMENT_RIGHT = "element-right";
    /**
     * Property name for the label of a list item
     */
    public static final String LABEL = "label";
    /**
     * Property name for the body elements of a list item
     */
    public static final String ELEMENTS = "elements";
    /**
     * Property name for the variant of a section or part
     */
    public static final String VARIANT = "variant";
    /**
     * Property name for ordering of a list
     */
    public static final String ORDERING = "ordering";
    /**
     * Property name for items of a list
     */
    public static final String ITEMS = "items";
    /**
     * Property name for rows of a table
     */
    public static final String COLUMNS = "columns";
    /**
     * Property name for rows of a table
     */
    public static final String ROWS = "rows";
    /**
     * Property name for columns span of a table
     */
    public static final String COL_SPAN = "col-span";
    /**
     * Property name for rows span of a table
     */
    public static final String ROW_SPAN = "row-span";
    /**
     * Property name for cells of a table
     */
    public static final String CELLS = "cells";

    // === metadata properties
    /**
     * Property name for document keywords
     */
    public static final String KEYWORDS = "keywords";
    /**
     * Property name for document author
     */
    public static final String AUTHOR = "author";
    /**
     * Property name for document title
     */
    public static final String TITLE = "title";
    /**
     * Property name for document producer
     */
    public static final String PRODUCER = "producer";
    /**
     * Property name for document subject
     */
    public static final String LANGUAGE = "language";
    /**
     * Property name for document creation date
     */
    public static final String CREATION_DATE = "creation-date";
    /**
     * Property name for document display title flag
     */
    public static final String DISPLAY_DOC_TITLE = "display-doc-title";
    /**
     * Property name for document subject
     */
    public static final String SUBJECT = "subject";

    // === Page sequence properties
    /**
     * Property name for body content area
     */
    public static final String BODY = "body";
    /**
     * Property name for header content area
     */
    public static final String HEADER = "header";
    /**
     * Property name for footer content area
     */
    public static final String FOOTER = "footer";

    // === Element style
    /**
     * Property name for style name
     */
    public static final String NAME = "name";
    /**
     * Property name for target element type
     */
    public static final String TARGET_ELEMENT = "target-element";
    /**
     * Property name for style properties
     */
    public static final String PROPERTIES = "properties";
    /**
     * Property name for text style name
     */
    public static final String TEXT_STYLE_NAME = "text-style-name";
    /**
     * Property name for text decoration
     */
    public static final String TEXT_DECORATION = "text-decoration";
    /**
     * Property name for text color
     */
    public static final String TEXT_COLOR = "text-color";
    /**
     * Property name for linefeed treatment
     */
    public static final String LINEFEED_TREATMENT = "linefeed-treatment";

    /**
     * Property name for list style type
     */
    public static final String LIST_STYLE_TYPE = "list-style-type";

    /**
     * Property name for provisional distance between starts
     */
    public static final String PROVISIONAL_DISTANCE_BETWEEN_STARTS = "provisional-distance-between-starts";
    /**
     * Property name for provisional label separation
     */
    public static final String PROVISIONAL_LABEL_SEPARATION = "provisional-label-separation";
    /**
     * Property name for list style image
     */
    public static final String LIST_STYLE_IMAGE = "list-style-image";

    // === Style properties
    /**
     * Property name for content width
     */
    public static final String CONTENT_WIDTH = "content-width";
    /**
     * Property name for block width
     */
    public static final String BLOCK_WIDTH = "block-width";
    /**
     * Property name for scaling
     */
    public static final String SCALING = "scaling";
    /**
     * Property name for alignment
     */
    public static final String ALIGNMENT = "alignment";

    /**
     * Property name for space before an element
     */
    public static final String PADDING = "padding";
    /**
     * Property name for space before an element
     */
    public static final String SPACE_BEFORE = "space-before";
    /**
     * Property name for space after an element
     */
    public static final String SPACE_AFTER = "space-after";
    /**
     * Property name for start indent of an element
     */
    public static final String START_INDENT = "start-indent";
    /**
     * Property name for end indent of an element
     */
    public static final String END_INDENT = "end-indent";
    /**
     * Property name for left padding of an element
     */
    public static final String PADDING_LEFT = "padding-left";
    /**
     * Property name for right padding of an element
     */
    public static final String PADDING_RIGHT = "padding-right";
    /**
     * Property name for top padding of an element
     */
    public static final String PADDING_TOP = "padding-top";
    /**
     * Property name for bottom padding of an element
     */
    public static final String PADDING_BOTTOM = "padding-bottom";

    public static final String TEXT_INDENT = "text-indent";
    public static final String ORPHANS = "orphans";
    public static final String WIDOWS = "widows";
    public static final String HYPHENATE = "hyphenate";
    public static final String LINE_HEIGHT = "line-height";
    public static final String TEXT_ALIGN_LAST = "text-align-last";
    public static final String SECTION_MARKER = "section-marker";
    public static final String KEEP_TOGETHER = "keep-together";

    public static final String BORDER = "border";
    public static final String BORDER_LEFT = "border-left";
    public static final String BORDER_RIGHT = "border-right";
    public static final String BORDER_TOP = "border-top";
    public static final String BORDER_BOTTOM = "border-bottom";
    public static final String KEEP_WITH_NEXT = "keep-with-next";
    public static final String BREAK_BEFORE = "break-before";
    public static final String BREAK_AFTER = "break-after";
    public static final String BACKGROUND_COLOR = "background-color";
    
    // === Page master
    public static final String PAGE_HEIGHT = "page-height";
    public static final String PAGE_WIDTH = "page-width";
    public static final String MARGIN_TOP = "margin-top";
    public static final String MARGIN_BOTTOM = "margin-bottom";
    public static final String HEADER_EXTENT = "header-extent";
    public static final String FOOTER_EXTENT = "footer-extent";
    public static final String COLUMN_GAP = "column-gap";
    public static final String COLUMN_COUNT = "column-count";
    public static final String MARGIN = "margin";
    public static final String MARGIN_LEFT = "margin-left";
    public static final String MARGIN_RIGHT = "margin-right";
    public static final String AUTO_ADJUST_MARGINS = "auto-adjust-margins";
    public static final String VERTICAL_ALIGN = "vertical-align";
    public static final String BORDER_COLLAPSE = "border-collapse";
    public static final String BASELINE_SHIFT = "baseline-shift";

    // === StyleSheet Properties
    public static final String TEXT_STYLES = "text-styles";
    public static final String ELEMENT_STYLES = "element-styles";
    public static final String PAGE_MASTER_STYLES = "page-master-styles";
}
