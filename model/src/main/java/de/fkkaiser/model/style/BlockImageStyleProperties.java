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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.fkkaiser.model.JsonPropertyName;
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;

/**
 * Style properties specific to block-level image elements.
 * This class extends {@link ElementBlockStyleProperties} to include
 * additional properties for controlling image sizing, scaling, and alignment.
 * @author Katrin Kaiser
 * @version 1.1.1
 */
@PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
@JsonTypeName(StyleTargetTypes.BLOCK_IMAGE)
public class BlockImageStyleProperties extends ElementBlockStyleProperties {


    @JsonProperty(JsonPropertyName.CONTENT_WIDTH)
    private String contentWidth;


    @JsonProperty(JsonPropertyName.BLOCK_WIDTH)
    private String blockWidth;

    @JsonProperty(JsonPropertyName.SCALING)
    private String scaling;

    @JsonProperty(JsonPropertyName.ALIGNMENT)
    private String alignment;

    // --- Getters and Setters ---
    /**
     * Gets the display width of the image content.
     *
     * @return the content width (e.g., {@code "10cm"}, {@code "100%"}, {@code "auto"}),
     *         or {@code null} if not set
     */
    @Internal
    public String getContentWidth() {
        return contentWidth;
    }

    /**
     * Sets the display width of the image content.
     *
     * @param contentWidth the content width to set (e.g., {@code "10cm"}, {@code "100%"}, {@code "auto"});
     *                     may be {@code null} to use default behavior
     */
    @PublicAPI
    public void setContentWidth(String contentWidth) {
        this.contentWidth = contentWidth;
    }

    /**
     * Gets the scaling method for the image.
     *
     * @return the scaling method (e.g., {@code "uniform"}, {@code "non-uniform"}),
     *         or {@code null} if not set
     */
    @Internal
    public String getScaling() {
        return scaling;
    }

    /**
     * Sets the scaling method for the image.
     *
     * @param scaling the scaling method to set (typically {@code "uniform"} or {@code "non-uniform"});
     *                may be {@code null} to use default behavior
     */
    @PublicAPI
    public void setScaling(String scaling) {
        this.scaling = scaling;
    }

    /**
     * Gets the horizontal alignment of the image within its containing block.
     *
     * @return the alignment (e.g., {@code "center"}, {@code "left"}, {@code "right"}),
     *         or {@code null} if not set
     * @see #alignment
     */
    @Internal
    public String getAlignment() {
        return alignment;
    }

    /**
     * Sets the horizontal alignment of the image within its containing block.
     *
     * @param alignment the alignment to set (e.g., {@code "center"}, {@code "left"}, {@code "right"});
     *                  may be {@code null} to use default behavior
     */
    @PublicAPI
    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    /**
     * Gets the width of the block container that holds the image.
     *
     * @return the block width (e.g., {@code "100%"}, {@code "20cm"}), or {@code null} if not set
     */
    @Internal
    public String getBlockWidth() {
        return blockWidth;
    }

    /**
     * Sets the width of the block container that holds the image.
     *
     * @param blockWidth the block width to set (e.g., {@code "100%"}, {@code "20cm"});
     *                   may be {@code null} to use default behavior
     */
    @PublicAPI
    public void setBlockWidth(String blockWidth) {
        this.blockWidth = blockWidth;
    }

    // --- Overrides ---

    /**
     * Creates a deep copy of this style properties object.
     * All properties are copied to a new instance.
     *
     * @return a new {@link BlockImageStyleProperties} instance with identical property values
     */
    @Override
    public BlockImageStyleProperties copy() {
        BlockImageStyleProperties copy = new BlockImageStyleProperties();
        applyPropertiesTo(copy);
        return copy;
    }

    /**
     * Applies all properties from this style to a target style object.
     * This is used internally by {@link #copy()} and for style inheritance mechanisms.
     *
     * @param target the target style to apply properties to; if not a
     *               {@link BlockImageStyleProperties}, only common block properties are applied
     */
    @Override
    protected void applyPropertiesTo(ElementBlockStyleProperties target) {
        super.applyPropertiesTo(target);
        if (target instanceof BlockImageStyleProperties partTarget) {
            partTarget.setContentWidth(contentWidth);
            partTarget.setScaling(scaling);
            partTarget.setAlignment(alignment);
            partTarget.setBlockWidth(blockWidth);
        }
    }
}