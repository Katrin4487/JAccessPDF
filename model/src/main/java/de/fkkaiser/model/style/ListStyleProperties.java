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

import java.util.Objects;

/**
 * Style properties for list elements.
 * <p>
 * This class defines visual and layout properties for lists, including:
 * <ul>
 *   <li>List item marker types (bullets, numbers, letters)</li>
 *   <li>Custom list item images</li>
 *   <li>List marker positioning (inside/outside)</li>
 *   <li>Spacing between list markers and content</li>
 * </ul>
 * The class provides intelligent defaults for list spacing that adapt based
 * on the list-style-position property, while still allowing power users to
 * override these values explicitly.
 *
 */
@JsonTypeName(StyleTargetTypes.LIST)
public class ListStyleProperties extends TextBlockStyleProperties {

    /**
     * Default distance between list item starts when marker is positioned outside.
     */
    public static final String DEFAULT_OUTSIDE_DISTANCE = "1.5em";

    /**
     * Default separation between the list marker and the content.
     */
    public static final String DEFAULT_LABEL_SEPARATION = "0.5em";

    @JsonProperty("provisional-distance-between-starts")
    private String provDistBetweenStarts;

    @JsonProperty("provisional-label-separation")
    private String provLabelSeparation;

    /**
     * Defines the type of the list item marker.
     * <p>
     * Examples:
     * <ul>
     *   <li>Unordered lists: "disc", "circle", "square"</li>
     *   <li>Ordered lists: "decimal", "lower-alpha", "upper-alpha", "lower-roman", "upper-roman"</li>
     * </ul>
     * </p>
     */
    @JsonProperty("list-style-type")
    private String listStyleType;

    /**
     * Defines an image to be used as the list item marker.
     * <p>
     * Expects a URL to the image. When set, this takes precedence over list-style-type.
     * </p>
     */
    @JsonProperty("list-style-image")
    private String listStyleImage;

    /**
     * Defines the position of the list item marker relative to the list item's principal block box.
     * <p>
     * Valid values:
     * <ul>
     *   <li>"outside" (default): marker is positioned outside the content box</li>
     *   <li>"inside": marker is positioned inside the content box, as if it were part of the content</li>
     * </ul>
     * </p>
     */
    @JsonProperty("list-style-position")
    private String listStylePosition;

    // --- Getters and Setters ---

    /**
     * Returns the provisional distance between the start of list item labels.
     *<p>
     * This method implements intelligent defaulting with the following priority:
     * <ol>
     *   <li><strong>Explicit value:</strong> If a specific value was set (by power users),
     *       that value is returned</li>
     *   <li><strong>"inside" positioning:</strong> If list-style-position is "inside",
     *       returns "0pt" to align markers with content</li>
     *   <li><strong>Default:</strong> Returns {@link #DEFAULT_OUTSIDE_DISTANCE} for
     *       standard "outside" positioning</li>
     * </ol>
     *
     *
     * @return the provisional distance between list item starts
     */
    public String getProvDistBetweenStarts() {
        // PRIORITY 1: A specific value was set (power user override)
        // This value comes either directly or through the mergeWith() logic
        if (this.isProvDistBetweenStartsManuallySet()) {
            return this.provDistBetweenStarts;
        }

        // PRIORITY 2: User has specified "inside" positioning
        if ("inside".equalsIgnoreCase(this.listStylePosition)) {
            return "0pt";
        }

        // PRIORITY 3: Standard behavior ("outside" or null)
        return DEFAULT_OUTSIDE_DISTANCE;
    }

    /**
     * Sets the provisional distance between the start of list item labels.
     *
     * @param provDistBetweenStarts the distance value (e.g., "1.5em", "20pt")
     */
    public void setProvDistBetweenStarts(String provDistBetweenStarts) {
        this.provDistBetweenStarts = provDistBetweenStarts;
    }

    /**
     * Returns the provisional separation between the list label and the content.
     *
     * @return the label separation distance, or {@link #DEFAULT_LABEL_SEPARATION} if not set
     */
    public String getProvLabelSeparation() {
        return Objects.requireNonNullElse(this.provLabelSeparation, DEFAULT_LABEL_SEPARATION);
    }

    /**
     * Sets the provisional separation between the list label and the content.
     *
     * @param provLabelSeparation the separation value (e.g., "0.5em", "10pt")
     */
    public void setProvLabelSeparation(String provLabelSeparation) {
        this.provLabelSeparation = provLabelSeparation;
    }

    public String getListStyleType() {
        return listStyleType;
    }

    public void setListStyleType(String listStyleType) {
        this.listStyleType = listStyleType;
    }

    public String getListStyleImage() {
        return listStyleImage;
    }

    public void setListStyleImage(String listStyleImage) {
        this.listStyleImage = listStyleImage;
    }

    public String getListStylePosition() {
        return listStylePosition;
    }

    public void setListStylePosition(String listStylePosition) {
        this.listStylePosition = listStylePosition;
    }

    // --- Overrides ---

    /**
     * Merges this style properties object with a base style.
     * <p>
     * Properties that are null in this instance will be inherited from the base
     * style. This allows for cascading style inheritance where specific properties
     * can override base values while inheriting unspecified ones.
     * </p>
     *
     * @param base the base style properties to merge with
     */
    @Override
    public void mergeWith(ElementStyleProperties base) {
        super.mergeWith(base);
        if (base instanceof ListStyleProperties baseList) {
            if (this.provDistBetweenStarts == null) {
                this.provDistBetweenStarts = baseList.getProvDistBetweenStarts();
            }
            if (this.provLabelSeparation == null) {
                this.provLabelSeparation = baseList.getProvLabelSeparation();
            }
            if (this.listStyleType == null) {
                this.listStyleType = baseList.getListStyleType();
            }
            if (this.listStyleImage == null) {
                this.listStyleImage = baseList.getListStyleImage();
            }
        }
    }

    /**
     * Checks if provisional-distance-between-starts was explicitly set.
     * <p>
     * This method determines whether the user (or a merge operation) has
     * explicitly provided a value for provisional-distance-between-starts.
     * This is used to distinguish between explicit values and automatic
     * defaults based on list-style-position.
     * </p>
     *
     * @return true if the value was explicitly set, false if it should use defaults
     */
    public boolean isProvDistBetweenStartsManuallySet() {
        return this.provDistBetweenStarts != null;
    }

    /**
     * Creates a deep copy of this ListStyleProperties instance.
     *
     * @return a new ListStyleProperties object with the same values
     */
    @Override
    public ListStyleProperties copy() {
        ListStyleProperties newInstance = new ListStyleProperties();
        applyPropertiesTo(newInstance);
        newInstance.setProvDistBetweenStarts(this.provDistBetweenStarts);
        newInstance.setProvLabelSeparation(this.provLabelSeparation);
        newInstance.setListStyleType(this.listStyleType);
        newInstance.setListStyleImage(this.listStyleImage);
        newInstance.setListStylePosition(this.listStylePosition);
        return newInstance;
    }
}