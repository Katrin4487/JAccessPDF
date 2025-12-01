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
package de.fkkaiser.model.style;

/**
 * Defines the available 'target-element' constants for style definitions.
 * Using these constants prevents typos in the style JSON configuration.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
public final class StyleTargetTypes {

    /** Prevents instantiation of the class. */
    private StyleTargetTypes() {}

    public static final String PARAGRAPH = "paragraph";
    public static final String HEADLINE = "headline";
    public static final String LIST = "list";
    public static final String TABLE = "table";
    public static final String TABLE_CELL = "table-cell";
    public static final String SECTION = "section";
    public static final String TEXT_RUN = "text-run";
    public static final String FOOTNOTE = "footnote";
    public static final String PART = "part";
    public static final String BLOCK_IMAGE = "block-image";
    public static final String LAYOUT_TABLE = "layout-table";
}

