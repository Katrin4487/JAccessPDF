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
package de.fkkaiser.model.structure;

import de.fkkaiser.model.annotation.PublicAPI;
/**
 * Defines the available 'type' constants for content elements.
 *
 * <p>These constants are used in the JSON structure to identify element types
 * and are mapped to concrete Java classes through Jackson's {@code @JsonTypeName}
 * and {@code @JsonSubTypes} annotations.</p>
 *
 * <p><b>Usage in JSON:</b></p>
 * <pre>{@code
 * {
 *   "type": "paragraph",
 *   "style-class": "body-text",
 *   "inline-elements": [...]
 * }
 * }</pre>
 *
 * <p><b>Usage in Java:</b></p>
 * <pre>{@code
 * String elementType = element.getType();
 * if (ElementTypes.PARAGRAPH.equals(elementType)) {
 *     // Handle paragraph
 * }
 * }</pre>
 *
 * <p><b>Important:</b> These constants must match the values used in
 * {@code @JsonTypeName} annotations on the corresponding element classes.</p>
 *
 * @author FK Kaiser
 * @version 1.0
 * @see Element
 * @see InlineElementTypes
 */
public final class ElementTypes {

    /** Prevents instantiation of this utility class. */
    private ElementTypes() {}

    /** Type identifier for paragraph elements. @see Paragraph */
    @PublicAPI
    public static final String PARAGRAPH = "paragraph";

    /** Type identifier for headline elements. @see Headline */
    @PublicAPI
    public static final String HEADLINE = "headline";

    /** Type identifier for list elements. @see SimpleList */
    @PublicAPI
    public static final String LIST = "list";

    /** Type identifier for table elements. @see Table */
    @PublicAPI
    public static final String TABLE = "table";

    /** Type identifier for section elements. @see Section */
    @PublicAPI
    public static final String SECTION = "section";

    /** Type identifier for part elements. @see Part */
    @PublicAPI
    public static final String PART = "part";

    /** Type identifier for block image elements. @see BlockImage */
    @PublicAPI
    public static final String BLOCK_IMAGE = "block-image";

    /** Type identifier for layout table elements. @see LayoutTable */
    @PublicAPI
    public static final String LAYOUT_TABLE = "layout-table";
}