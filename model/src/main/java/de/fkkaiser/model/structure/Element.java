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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.fkkaiser.model.style.ElementStyleProperties;
import de.fkkaiser.model.style.StyleResolverContext;

/**
 * Base interface for all content elements in a PDF document structure.
 *
 * <p>Element defines the contract for all structural components that can be included
 * in a PDF document, such as paragraphs, headlines, lists, tables, and images. Each
 * element type has its own specific properties and rendering behavior, but all share
 * common functionality for type identification, styling, and style resolution.</p>
 *
 * <p><b>Purpose in PDF Generation:</b></p>
 * Elements form the building blocks of the document structure. During PDF generation,
 * each element is processed according to its type, with styles resolved and applied
 * before rendering. The element hierarchy allows for complex document layouts with
 * nested structures and inherited styling.
 *
 * <p><b>JSON Polymorphism:</b></p>
 * This interface uses Jackson's polymorphism mechanism to enable type-safe deserialization
 * from JSON. The {@code type} field in the JSON structure determines which concrete
 * implementation class will be instantiated. This allows for a flexible document model
 * where different element types can be mixed in the same structure.
 *
 * <p><b>Supported Element Types:</b></p>
 * <ul>
 *   <li>{@link Paragraph} - Text paragraphs with inline formatting ({@value ElementTypes#PARAGRAPH})</li>
 *   <li>{@link Headline} - Document headings with hierarchical levels ({@value ElementTypes#HEADLINE})</li>
 *   <li>{@link SimpleList} - Ordered and unordered lists ({@value ElementTypes#LIST})</li>
 *   <li>{@link Table} - Data tables with rows and cells ({@value ElementTypes#TABLE})</li>
 *   <li>{@link Section} - Container elements for grouping content ({@value ElementTypes#SECTION})</li>
 *   <li>{@link BlockImage} - Block-level images ({@value ElementTypes#BLOCK_IMAGE})</li>
 *   <li>{@link LayoutTable} - Tables for layout purposes ({@value ElementTypes#LAYOUT_TABLE})</li>
 * </ul>
 *
 * <p><b>JSON Structure Example:</b></p>
 * <pre>{@code
 * {
 *   "type": "paragraph",
 *   "style-class": "body-text",
 *   "content": [
 *     { "type": "text-segment", "text": "This is a paragraph." }
 *   ]
 * }
 * }</pre>
 *
 * <p>The {@code type} field is mandatory and must match one of the registered element type
 * constants defined in {@link ElementTypes}. Jackson uses this field to determine which
 * concrete class to instantiate during deserialization.</p>
 *
 * <p><b>Style Resolution:</b></p>
 * Elements participate in a two-phase lifecycle:
 * <ol>
 *   <li><b>Construction:</b> Elements are created from JSON or programmatically</li>
 *   <li><b>Style Resolution:</b> The {@link #resolveStyles(StyleResolverContext)} method
 *       is called to resolve and merge styles before rendering</li>
 * </ol>
 *
 * <p>Style resolution allows elements to inherit styles from their parent context and
 * merge them with element-specific styles defined via the styleClass. This creates a
 * flexible and powerful styling system similar to CSS cascading.</p>
 *
 * <p><b>Implementation Requirements:</b></p>
 * Implementing classes must:
 * <ul>
 *   <li>Be registered in the {@link JsonSubTypes} annotation above</li>
 *   <li>Use a {@link JsonTypeInfo} name that matches a constant in {@link ElementTypes}</li>
 *   <li>Implement type-specific style resolution logic in {@link #resolveStyles(StyleResolverContext)}</li>
 *   <li>Provide proper Jackson annotations for JSON serialization/deserialization</li>
 * </ul>
 *
 * @author FK Kaiser
 * @version 1.0
 * @see ElementTypes
 * @see StyleResolverContext
 * @see Paragraph
 * @see Headline
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Paragraph.class, name = ElementTypes.PARAGRAPH),
        @JsonSubTypes.Type(value = Headline.class, name = ElementTypes.HEADLINE),
        @JsonSubTypes.Type(value = SimpleList.class, name = ElementTypes.LIST),
        @JsonSubTypes.Type(value = Table.class, name = ElementTypes.TABLE),
        @JsonSubTypes.Type(value = Section.class, name = ElementTypes.SECTION),
        @JsonSubTypes.Type(value = BlockImage.class, name = ElementTypes.BLOCK_IMAGE),
        @JsonSubTypes.Type(value = LayoutTable.class, name = ElementTypes.LAYOUT_TABLE),
})
public interface Element {

    /**
     * Returns the type identifier for this element.
     *
     * <p>The type identifier corresponds to one of the constants defined in
     * {@link ElementTypes} and is used for JSON polymorphism. This value determines
     * which concrete class is instantiated during deserialization and can be used
     * for type-specific processing during rendering.</p>
     *
     * <p><b>Note:</b> The returned value must match the {@code name} attribute in
     * the {@link JsonSubTypes.Type} annotation for this element's class.</p>
     *
     * @return the element type identifier (e.g., "paragraph", "headline", "table")
     */
    String getType();

    /**
     * Returns the style class name used to look up styling properties.
     *
     * <p>The style class is similar to a CSS class name and is used during style
     * resolution to locate element-specific styles in the document's style map.
     * If no style class is specified (returns {@code null}), the element will
     * use only inherited styles from its parent context.</p>
     *
     * <p><b>Example:</b> A paragraph with styleClass "body-text" would look up
     * its specific styles under the "body-text" key in the style map.</p>
     *
     * @return the style class name; may be {@code null} if no specific style is applied
     */
    String getStyleClass();

    /**
     * Resolves styles for this element using the provided style resolver context.
     *
     * <p>This method is called during document preparation to resolve and merge styles
     * before rendering begins. The resolution process typically involves:</p>
     * <ol>
     *   <li>Looking up element-specific styles from the context's style map using {@link #getStyleClass()}</li>
     *   <li>Retrieving parent styles from the context</li>
     *   <li>Merging specific and inherited styles according to element-specific rules</li>
     *   <li>Storing the resolved styles internally for use during rendering</li>
     * </ol>
     *
     * <p><b>Style Inheritance:</b></p>
     * The context provides access to parent styles, allowing child elements to inherit
     * properties such as font family, text color, margins, or alignment. Each element
     * type determines which properties are inherited and how conflicts are resolved.
     *
     * <p><b>Implementation Note:</b></p>
     * This method typically modifies the internal state of the element by storing
     * resolved styles in a transient field. It should be called exactly once during
     * document preparation and before rendering begins.
     *
     * <p>For container elements (like {@link Section}), this method should recursively
     * call {@code resolveStyles} on all child elements, passing an updated context
     * that reflects the container's resolved styles.</p>
     *
     * @param context the style resolver context containing the style map and parent styles;
     *                must not be {@code null}
     * @throws NullPointerException if context is {@code null}
     */
    void resolveStyles(StyleResolverContext context);

    ElementStyleProperties getResolvedStyle();
}