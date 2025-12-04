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
import de.fkkaiser.model.JsonPropertyName;
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.style.ElementStyleProperties;
import de.fkkaiser.model.style.StyleResolverContext;

/**
 * Represents a generic element in the document structure.
 *
 * <p>This interface serves as the base type for all document elements,
 * enabling polymorphic handling and JSON serialization/deserialization
 * based on the {@code type} property.</p>
 *
 * <p>Concrete implementations include {@link Paragraph}, {@link Headline},
 * {@link SimpleList}, {@link Table}, {@link Section}, {@link BlockImage},
 * and {@link LayoutTable}.</p>
 *
 * @author Katrin Kaiser
 * @version 1.1.0
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Paragraph.class, name = JsonPropertyName.PARAGRAPH),
        @JsonSubTypes.Type(value = Headline.class, name = JsonPropertyName.HEADLINE),
        @JsonSubTypes.Type(value = SimpleList.class, name = JsonPropertyName.LIST),
        @JsonSubTypes.Type(value = Table.class, name = JsonPropertyName.TABLE),
        @JsonSubTypes.Type(value = Section.class, name = JsonPropertyName.SECTION),
        @JsonSubTypes.Type(value = BlockImage.class, name = JsonPropertyName.BLOCK_IMAGE),
        @JsonSubTypes.Type(value = LayoutTable.class, name = JsonPropertyName.LAYOUT_TABLE),
})
public interface Element {

    /**
     * Returns the type identifier for this element.
     *
     * <p>This type is used for JSON serialization and element type identification
     * within the document structure.</p>
     *
     * @return the element's target type
     */
    @Internal
    ElementTargetType getType();

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
    @Internal
    String getStyleClass();

    /**
     * Resolves and applies styles for this element based on the provided context.
     *
     * <p>This method looks up the element's specific styles using its style class,
     * merges them with inherited styles from the parent context, and stores the
     * final resolved styles for rendering.</p>
     *
     * @param context the style resolver context containing style maps and parent styles
     */
    @Internal
    void resolveStyles(StyleResolverContext context);

    /**
     * Returns the resolved style properties for this element.
     *
     * <p>This method provides access to the final computed styles after
     * the resolution process has been performed via {@link #resolveStyles(StyleResolverContext)}.
     * The returned {@link ElementStyleProperties} contains all applicable
     * styling attributes for rendering the element.</p>
     *
     * @return the resolved style properties
     */
    @Internal
    ElementStyleProperties getResolvedStyle();
}