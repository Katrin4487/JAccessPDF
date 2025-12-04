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
}
