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
    public static final String INLINE_TAG = "inline";
    /**
     * Tag for block elements in FOP XML
     */
    public static final String BLOCK = "block";

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


}
