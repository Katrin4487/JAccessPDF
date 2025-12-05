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
import de.fkkaiser.model.annotation.Inheritable;

import java.util.Objects;

/**
 * Style properties specific to list elements.
 *
 * @author Katrin Kaiser
 * @version 1.2.1
 */
@JsonTypeName(JsonPropertyName.LIST)
public class ListStyleProperties extends TextBlockStyleProperties {

    /**
     * Default distance between list item starts when marker is positioned outside.
     */
    public static final String DEFAULT_OUTSIDE_DISTANCE = "1.5em";

    /**
     * Default separation between the list marker and the content.
     */
    public static final String DEFAULT_LABEL_SEPARATION = "0.5em";

    @Inheritable
    @JsonProperty(JsonPropertyName.PROVISIONAL_DISTANCE_BETWEEN_STARTS)
    private String provDistBetweenStarts;

    @Inheritable
    @JsonProperty(JsonPropertyName.PROVISIONAL_LABEL_SEPARATION)
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
    @Inheritable
    @JsonProperty(JsonPropertyName.LIST_STYLE_TYPE)
    private ListStyleType listStyleType;

    /**
     * Defines an image to be used as the list item marker.
     * <p>
     * Expects a URL to the image. When set, this takes precedence over list-style-type.
     * </p>
     */
    @Inheritable
    @JsonProperty(JsonPropertyName.LIST_STYLE_IMAGE)
    private String listStyleImage;


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
        if (this.isProvDistBetweenStartsManuallySet()) {
            return this.provDistBetweenStarts;
        }

        return DEFAULT_OUTSIDE_DISTANCE;
    }

    public String getProvLabelSeparation() {
        return Objects.requireNonNullElse(this.provLabelSeparation, DEFAULT_LABEL_SEPARATION);
    }

    public void setProvDistBetweenStarts(String provDistBetweenStarts) {
        if (this.provLabelSeparation != null && provDistBetweenStarts != null) {

            double dist = parseLength(provDistBetweenStarts);
            double sep = parseLength(this.provLabelSeparation);

            if (dist < sep) {

                System.err.println("Warning: provisional-distance-between-starts (" +
                        provDistBetweenStarts + ") is smaller than provisional-label-separation (" +
                        this.provLabelSeparation + "). This may cause label-content overlap.");
            }
        }
        this.provDistBetweenStarts = provDistBetweenStarts;
    }


    /**
     * Sets the provisional separation between the list label and the content.
     *
     * @param provLabelSeparation the separation value (e.g., "0.5em", "10pt")
     */
    public void setProvLabelSeparation(String provLabelSeparation) {
        this.provLabelSeparation = provLabelSeparation;
    }

    public ListStyleType getListStyleType() {
        return listStyleType;
    }

    public void setListStyleType(ListStyleType listStyleType) {
        this.listStyleType = listStyleType;
    }

    public String getListStyleImage() {
        return listStyleImage;
    }

    public void setListStyleImage(String listStyleImage) {
        this.listStyleImage = listStyleImage;
    }


    // --- Overrides ---

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
        return newInstance;
    }

    private double parseLength(String length) {
        if (length == null || length.isEmpty()) return 0;
        try {
            String numPart = length.replaceAll("[^0-9.-]", "");
            return Double.parseDouble(numPart);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}