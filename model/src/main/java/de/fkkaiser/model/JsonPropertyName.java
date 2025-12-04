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
 * @version 1.0.0
 */
@Internal
public class JsonPropertyName {


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
}
