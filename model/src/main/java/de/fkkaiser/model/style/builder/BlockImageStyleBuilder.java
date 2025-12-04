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
package de.fkkaiser.model.style.builder;

import de.fkkaiser.model.structure.ElementTargetType;
import de.fkkaiser.model.style.BlockImageStyleProperties;
import de.fkkaiser.model.style.ElementStyle;

public class BlockImageStyleBuilder {
    String name;
    BlockImageStyleProperties properties;

    public BlockImageStyleBuilder(String name) {
        this.name = name;
        this.properties = new BlockImageStyleProperties();
    }

    /**
     * Sets the horizontal alignment of the image within its containing block.
     *
     * @param alignment the alignment to set (e.g., {@code "center"}, {@code "left"}, {@code "right"});
     *
     */
    @SuppressWarnings("unused")
    public BlockImageStyleBuilder withAlignment(String alignment) {
        this.properties.setAlignment(alignment);
        return this;
    }

    /**
     * Sets the width of the block container that holds the image.
     *
     * @param blockWidth the block width to set (e.g., {@code "100%"}, {@code "20cm"});
     *                   may be {@code null} to use default behavior
     */
    @SuppressWarnings("unused")
    public BlockImageStyleBuilder withBlockWidth(String blockWidth) {
        this.properties.setBlockWidth(blockWidth);
        return this;
    }

    /**
     * Sets the display width of the image content.
     *
     * @param contentWidth the content width to set (e.g., {@code "10cm"}, {@code "100%"}, {@code "auto"});
     *
     */
    @SuppressWarnings("unused")
    public BlockImageStyleBuilder withContentWidth(String contentWidth) {
        this.properties.setContentWidth(contentWidth);
        return this;
    }

    /**
     * Sets the scaling method for the image.
     *
     * @param scaling the scaling method to set (typically {@code "uniform"} or {@code "non-uniform"});
     *
     */
    @SuppressWarnings("unused")
    public BlockImageStyleBuilder withScaling(String scaling) {
        this.properties.setScaling(scaling);
        return this;
    }

    public ElementStyle build() {
        return new ElementStyle(this.name, ElementTargetType.BLOCK_IMAGE, this.properties);
    }
}
