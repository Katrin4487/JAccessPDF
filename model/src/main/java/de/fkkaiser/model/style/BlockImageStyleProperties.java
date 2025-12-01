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
import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.annotation.Inheritable;

/**
 * Concrete style properties for a block image element.
 * This class defines styling options specific to block-level images
 * within a document, such as width, scaling, and alignment.
 *
 * <p><b>Inheritance Behavior:</b></p>
 * Image-specific properties (content-width, scaling, alignment) are NOT inherited
 * from parent elements, as each image typically needs its own dimensions.
 * Only properties marked with {@link Inheritable} in parent classes
 * (like background-color) may be inherited.
 *
 * @author Katrin Kaiser
 * @version 1.1.0
 */
@PublicAPI(status = PublicAPI.Status.EXPERIMENTAL)
@JsonTypeName(StyleTargetTypes.BLOCK_IMAGE)
public class BlockImageStyleProperties extends ElementBlockStyleProperties {

    /**
     * The display width of the image content.
     * This property controls how wide the actual image is rendered on the page.
     *
     * <p><b>Valid Values:</b></p>
     * <ul>
     *   <li><b>Absolute units:</b> {@code "10cm"}, {@code "4in"}, {@code "300pt"}, {@code "25mm"}</li>
     *   <li><b>Percentage:</b> {@code "100%"} (relative to containing block), {@code "50%"}</li>
     *   <li><b>Auto:</b> {@code "auto"} (uses intrinsic image width)</li>
     *   <li><b>Scale-to-fit:</b> {@code "scale-to-fit"} (scales to fit available space)</li>
     * </ul>
     *
     * <p><b>Behavior:</b></p>
     * When combined with {@link #scaling}, this property determines the final rendered size:
     * <ul>
     *   <li>If scaling is {@code "uniform"}, aspect ratio is preserved</li>
     *   <li>If scaling is {@code "non-uniform"}, image may be stretched</li>
     *   <li>If not set, the image uses its intrinsic dimensions</li>
     * </ul>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * // Fixed width
     * props.setContentWidth("15cm");
     *
     * // Full width of container
     * props.setContentWidth("100%");
     *
     * // Half width of container
     * props.setContentWidth("50%");
     * }</pre>
     *
     * @see #setContentWidth(String)
     * @see #scaling
     */
    @JsonProperty("content-width")
    private String contentWidth;

    /**
     * The width of the block container that holds the image.
     * This property defines the total width available for the image block,
     * which may be different from the actual image content width.
     *
     * <p><b>Valid Values:</b></p>
     * <ul>
     *   <li><b>Absolute units:</b> {@code "10cm"}, {@code "4in"}, {@code "300pt"}</li>
     *   <li><b>Percentage:</b> {@code "100%"} (relative to parent container)</li>
     *   <li><b>Auto:</b> {@code "auto"} (adjusts to content width)</li>
     * </ul>
     *
     * <p><b>Use Case:</b></p>
     * This is useful when you want the image to be smaller than its container,
     * allowing for horizontal alignment within the block (e.g., centering a
     * small image within a full-width block).
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * // Full-width block with centered image
     * props.setBlockWidth("100%");     // Block spans full width
     * props.setContentWidth("10cm");   // Image is only 10cm wide
     * props.setAlignment("center");    // Image centered in block
     * }</pre>
     *
     * @see #setBlockWidth(String)
     * @see #contentWidth
     * @see #alignment
     */
    @JsonProperty("block-width")
    private String blockWidth;

    /**
     * Defines how the image is scaled when its intrinsic size differs from
     * the specified {@link #contentWidth}.
     *
     * <p><b>Valid Values:</b></p>
     * <ul>
     *   <li><b>{@code "uniform"}:</b> (recommended) Scales the image proportionally,
     *       preserving the aspect ratio. The image is sized to fit within the
     *       specified dimensions without distortion.</li>
     *   <li><b>{@code "non-uniform"}:</b> Scales the image independently in each
     *       dimension, potentially distorting the aspect ratio to exactly match
     *       the specified size.</li>
     *   <li><b>{@code "inherit"}:</b> Uses the scaling method from the parent element.</li>
     * </ul>
     *
     * <p><b>Default Behavior:</b></p>
     * If not specified, most FOP processors default to {@code "uniform"} scaling
     * to prevent image distortion.
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * // Maintain aspect ratio (recommended for photos)
     * props.setScaling("uniform");
     * props.setContentWidth("10cm");
     * // Height will be calculated automatically
     *
     * // Stretch to exact dimensions (use carefully)
     * props.setScaling("non-uniform");
     * props.setContentWidth("10cm");
     * props.setContentHeight("5cm");
     * // Image may appear distorted
     * }</pre>
     *
     * @see #setScaling(String)
     * @see #contentWidth
     */
    @JsonProperty("scaling")
    private String scaling;

    /**
     * Horizontal alignment of the image within its containing block.
     * This property only has an effect when the image is narrower than its container.
     *
     * <p><b>Valid Values:</b></p>
     * <ul>
     *   <li><b>{@code "left"}:</b> Aligns the image to the left edge</li>
     *   <li><b>{@code "center"}:</b> Centers the image horizontally</li>
     *   <li><b>{@code "right"}:</b> Aligns the image to the right edge</li>
     *   <li><b>{@code "start"}:</b> Aligns to the start edge (left in LTR, right in RTL)</li>
     *   <li><b>{@code "end"}:</b> Aligns to the end edge (right in LTR, left in RTL)</li>
     * </ul>
     *
     * <p><b>Typical Usage:</b></p>
     * Most block images are centered for visual balance, but left or right
     * alignment can be useful for specific design requirements.
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * // Centered image (most common)
     * props.setAlignment("center");
     *
     * // Left-aligned thumbnail
     * props.setAlignment("left");
     *
     * // Right-aligned figure
     * props.setAlignment("right");
     * }</pre>
     *
     * <p><b>Note:</b></p>
     * This maps to the XSL-FO {@code text-align} property on the containing block.
     *
     * @see #setAlignment(String)
     */
    @JsonProperty("alignment")
    private String alignment;

    // --- Getters and Setters ---

    /**
     * Gets the display width of the image content.
     *
     * @return the content width (e.g., {@code "10cm"}, {@code "100%"}), or {@code null} if not set
     * @see #contentWidth
     */
    public String getContentWidth() {
        return contentWidth;
    }

    /**
     * Sets the display width of the image content.
     *
     * @param contentWidth the content width to set (e.g., {@code "10cm"}, {@code "100%"}, {@code "auto"});
     *                     may be {@code null} to use default behavior
     * @see #contentWidth
     */
    public void setContentWidth(String contentWidth) {
        this.contentWidth = contentWidth;
    }

    /**
     * Gets the scaling method for the image.
     *
     * @return the scaling method (e.g., {@code "uniform"}, {@code "non-uniform"}),
     *         or {@code null} if not set
     * @see #scaling
     */
    public String getScaling() {
        return scaling;
    }

    /**
     * Sets the scaling method for the image.
     *
     * @param scaling the scaling method to set (typically {@code "uniform"} or {@code "non-uniform"});
     *                may be {@code null} to use default behavior
     * @see #scaling
     */
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
    public String getAlignment() {
        return alignment;
    }

    /**
     * Sets the horizontal alignment of the image within its containing block.
     *
     * @param alignment the alignment to set (e.g., {@code "center"}, {@code "left"}, {@code "right"});
     *                  may be {@code null} to use default behavior
     * @see #alignment
     */
    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    /**
     * Gets the width of the block container that holds the image.
     *
     * @return the block width (e.g., {@code "100%"}, {@code "20cm"}), or {@code null} if not set
     * @see #blockWidth
     */
    public String getBlockWidth() {
        return blockWidth;
    }

    /**
     * Sets the width of the block container that holds the image.
     *
     * @param blockWidth the block width to set (e.g., {@code "100%"}, {@code "20cm"});
     *                   may be {@code null} to use default behavior
     * @see #blockWidth
     */
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