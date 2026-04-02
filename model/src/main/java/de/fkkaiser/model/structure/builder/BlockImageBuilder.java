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
package de.fkkaiser.model.structure.builder;

import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.structure.BlockImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fluent builder for constructing {@link BlockImage} instances with a clear, readable API.
 * @author Katrin Kaiser
 * @version 1.0.0
 *
 */
public class BlockImageBuilder {

    private static final Logger log = LoggerFactory.getLogger(BlockImageBuilder.class);

    private final String styleClass;
    private String path;
    private String altText;
    private String base64Data;
    private String svgContent;

    /**
     * Constructor for BlockImageBuilder.
     * @param styleClass The style class to be applied to the image.
     *
     */
    public BlockImageBuilder(String styleClass) {
        if (styleClass != null && styleClass.trim().isEmpty()) {
            throw new IllegalArgumentException("Style class cannot be empty");
        }
        this.styleClass = styleClass;
    }

    /**
     * Sets the path to the image file.
     * Note: This method overrides any existing base64 data or SVG content.
     * @param path the path to the image file
     * @return the builder instance for chaining
     */
    @PublicAPI
    public BlockImageBuilder withPath(String path) {
        this.path = path;
        if(base64Data != null || svgContent != null) {
            log.warn("Setting path overrides existing base64 data or SVG content");
            this.base64Data = null;
            this.svgContent = null;
        }
        return this;
    }

    /**
     * Sets the alternative text for the image.
     * @param altText the alternative text for the image
     * @return the builder instance for chaining
     */
    @PublicAPI
    public BlockImageBuilder withAltText(String altText) {
        this.altText = altText;
        return this;
    }

    /**
     * Sets the base64 encoded image data.
     * Note: This method overrides any existing path or SVG content.
     * @param base64Data the base64 encoded image data
     * @return the builder instance for chaining
     */
    @PublicAPI
    public BlockImageBuilder withBase64Data(String base64Data) {
        if(path != null || svgContent != null) {
            log.warn("Setting base64 data overrides existing path or SVG content");
            this.path = null;
            this.svgContent = null;
        }
        this.base64Data = base64Data;
        return this;
    }

    /**
     * Sets the SVG content for the image.
     * Note: This method overrides any existing path or base64 data.
     * @param svgContent the SVG content for the image
     * @return the builder instance for chaining
     */
    @PublicAPI
    public BlockImageBuilder withSvgContent(String svgContent) {
        this.svgContent = svgContent;
        if(path != null || base64Data != null) {
            log.warn("Setting SVG content overrides existing path or base64 data");
            this.path = null;
            this.base64Data = null;
        }
        return this;
    }

    /**
     * Builds the BlockImage object.
     * @return the BlockImage object
     */
    public BlockImage build() {
        return new BlockImage(styleClass,path,altText,base64Data,svgContent);
    }

}
