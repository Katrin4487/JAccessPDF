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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.fkkaiser.model.JsonPropertyName;
import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.style.BlockImageStyleProperties;
import de.fkkaiser.model.style.ElementBlockStyleProperties;
import de.fkkaiser.model.style.ElementStyle;
import de.fkkaiser.model.style.StyleResolverContext;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents a block image element in a document with associated
 * styling and accessibility properties.
 *
 * @author Katrin Kaiser
 * @version 1.1.0
 *
 */
@PublicAPI
@JsonTypeName(JsonPropertyName.BLOCK_IMAGE)
public final class BlockImage implements Element {

    private final String styleClass;
    private final String path;
    private final String altText;

    @JsonIgnore
    private BlockImageStyleProperties resolvedStyle;

    /**
     * Creates a new BlockImage element.
     *
     * <p>This constructor is used by Jackson during JSON deserialization.
     * All parameters are optional in the JSON structure, though null values
     * may affect rendering or accessibility compliance.</p>
     *
     * @param styleClass the CSS-like style class for styling properties; may be {@code null}
     * @param path       the path to the image file (relative or absolute); may be {@code null}
     * @param altText    alternative text for accessibility (PDF/UA); may be {@code null}
     */
    @JsonCreator
    public BlockImage(
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("path") String path,
            @JsonProperty("alt-text") String altText
    ) {
        this.styleClass = styleClass;
        this.path = path;
        this.altText = altText;
    }

    // --- Getters ---

    /**
     * Returns the style class name used to look up styling properties.
     *
     * @return the style class name; may be {@code null}
     */
    @Override
    public String getStyleClass() {
        return styleClass;
    }

    /**
     * Returns the path to the image file.
     *
     * @return the image file path; may be {@code null}
     */
    public String getPath() {
        return path;
    }

    /**
     * Returns the alternative text for accessibility purposes.
     *
     * <p>This text should describe the content and purpose of the image
     * for users who cannot view it. It is used to ensure PDF/UA compliance.</p>
     *
     * @return the alternative text; may be {@code null}
     */
    public String getAltText() {
        return altText;
    }

    /**
     * Returns the resolved style properties after style resolution.
     *
     * <p>This value is populated during the {@link #resolveStyles(StyleResolverContext)}
     * call and should not be accessed before style resolution has been performed.</p>
     *
     * @return the resolved style properties; may be {@code null} before resolution
     */
    public BlockImageStyleProperties getResolvedStyle() {
        return resolvedStyle;
    }

    /**
     * Returns the type identifier for this element.
     *
     * @return The ElementTargetType representing a block image.
     */
    @Override
    public ElementTargetType getType() {
        return ElementTargetType.BLOCK_IMAGE;
    }

    /**
     * Resolves the style properties of this element by merging specific
     * styles with parent block styles.
     * <p>
     * The resolution process:
     * <ol>
     *   <li>Retrieves the parent block's style properties from the context</li>
     *   <li>Looks up element-specific styles using the styleClass from the style map</li>
     *   <li>Filters to ensure the style is of type {@link BlockImageStyleProperties}</li>
     *   <li>Creates a copy of the specific styles (or defaults if not found)</li>
     *   <li>Merges the parent styles into the copy, with parent values filling in gaps</li>
     *   <li>Stores the final merged result in {@link #resolvedStyle}</li>
     * </ol>
     *
     *
     * <p>This method modifies the internal state of the object by setting the
     * resolvedStyle field. It should be called once during document preparation
     * before rendering begins.</p>
     *
     * @param context the context containing the style map and parent styles;
     *                must not be {@code null}
     * @throws NullPointerException if the context is null
     */
    @Override
    public void resolveStyles(StyleResolverContext context) {
        Objects.requireNonNull(context, "StyleResolverContext must not be null");
        ElementBlockStyleProperties parentStyle = context.parentBlockStyle();

        BlockImageStyleProperties specificStyle = Optional.ofNullable(context.styleMap().get(this.getStyleClass()))
                .map(ElementStyle::properties)
                .filter(BlockImageStyleProperties.class::isInstance)
                .map(BlockImageStyleProperties.class::cast)
                .orElse(new BlockImageStyleProperties());

        BlockImageStyleProperties finalStyle = specificStyle.copy();
        finalStyle.mergeWith(parentStyle);

        this.resolvedStyle = finalStyle;
    }
}