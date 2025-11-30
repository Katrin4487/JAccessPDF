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
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;

/**
 * Style properties for section elements.
 *
 * <p>A section is a structural container that groups related content and can apply
 * consistent styling (background, borders, padding, spacing) to multiple child elements.</p>
 *
 * <p><b>Inherited Properties from {@link ElementBlockStyleProperties}:</b></p>
 * <ul>
 *   <li>Font properties (family, size, weight, style, color)</li>
 *   <li>Padding (all sides, individual sides)</li>
 *   <li>Border</li>
 *   <li>Background color</li>
 *   <li>Indentation (start, end)</li>
 *   <li>Spacing (before, after)</li>
 * </ul>
 *
 * <p><b>Section-Specific Properties:</b></p>
 * <ul>
 *   <li>{@link #sectionMarker} - Symbol or text displayed before the section</li>
 *   <li>{@link #keepTogether} - Prevents page breaks within the section</li>
 *   <li>{@link #orphans} - Minimum lines at bottom of page</li>
 *   <li>{@link #widows} - Minimum lines at top of page</li>
 * </ul>
 *
 * <p><b>Example JSON:</b></p>
 * <pre>{@code
 * {
 *   "name": "warning-box",
 *   "target": "section",
 *   "properties": {
 *     "section-marker": "⚠️",
 *     "padding": "1cm",
 *     "border": "2pt solid #ff6b6b",
 *     "background-color": "#fff3cd",
 *     "keep-together": true,
 *     "space-before": "0.5cm",
 *     "space-after": "0.5cm"
 *   }
 * }
 * }</pre>
 *
 * @see ElementBlockStyleProperties
 * @see de.fkkaiser.model.structure.Section
 * @author Katrin Kaiser
 * @version 1.1.0
 */
@PublicAPI
@JsonTypeName(StyleTargetTypes.SECTION)
public class SectionStyleProperties extends ElementBlockStyleProperties {

    /**
     * Symbol or text to display before the section content.
     * <p>Common examples:</p>
     * <ul>
     *   <li>"§" - for legal paragraphs</li>
     *   <li>"►" - for emphasized sections</li>
     *   <li>"⚠️" - for warnings</li>
     *   <li>"ℹ️" - for information boxes</li>
     *   <li>"•" - for bullet-style sections</li>
     * </ul>
     */
    @JsonProperty("section-marker")
    private String sectionMarker;

    /**
     * If true, prevents the section from breaking across pages.
     * The entire section will be kept together on one page if possible.
     * <p><b>Note:</b> Only works if the section fits on a single page.</p>
     * <p>Maps to XSL-FO: {@code keep-together.within-page="always"}</p>
     */
    @JsonProperty("keep-together")
    private Boolean keepTogether;


    /**
     * Minimum number of lines that must appear at the bottom of a page.
     * Prevents orphans (single lines stranded at page bottom).
     * <p>Typical value: 2</p>
     * <p>Maps to XSL-FO: {@code orphans}</p>
     */
    @JsonProperty("orphans")
    private Integer orphans;

    /**
     * Minimum number of lines that must appear at the top of a page.
     * Prevents widows (single lines stranded at page top).
     * <p>Typical value: 2</p>
     * <p>Maps to XSL-FO: {@code widows}</p>
     */
    @JsonProperty("widows")
    private Integer widows;

    // --- Getters and Setters ---

    /**
     * Gets the section marker symbol or text.
     *
     * @return the section marker
     */
    @PublicAPI
    public String getSectionMarker() {
        return sectionMarker;
    }

    /**
     * Sets the section marker symbol or text.
     *
     * @param sectionMarker the section marker to set
     */
    @PublicAPI
    public void setSectionMarker(String sectionMarker) {
        this.sectionMarker = sectionMarker;
    }

    /**
     * Gets whether to keep the section together on one page.
     *
     * @return true if the section should be kept together, false otherwise
     */
    @PublicAPI
    public Boolean getKeepTogether() {
        return keepTogether;
    }

    /**
     * Sets whether to keep the section together on one page.
     *
     * @param keepTogether true to keep the section together, false otherwise
     */
    @PublicAPI
    public void setKeepTogether(Boolean keepTogether) {
        this.keepTogether = keepTogether;
    }

    /**
     * Gets the minimum number of orphans (lines at bottom of page).
     *
     * @return the number of orphans
     */
    @PublicAPI
    public Integer getOrphans() {
        return orphans;
    }

    /**
     * Sets the minimum number of orphans (lines at bottom of page).
     *
     * @param orphans the number of orphans to set
     */
    @PublicAPI
    public void setOrphans(Integer orphans) {
        this.orphans = orphans;
    }

    /**
     * Gets the minimum number of widows (lines at top of page).
     *
     * @return the number of widows
     */
    @PublicAPI
    public Integer getWidows() {
        return widows;
    }

    /**
     * Sets the minimum number of widows (lines at top of page).
     *
     * @param widows the number of widows to set
     */
    @PublicAPI
    public void setWidows(Integer widows) {
        this.widows = widows;
    }

    // --- Overrides ---

    /**
     * Merges this style with a base style.
     * Properties that are null in this instance will be inherited from the base style.
     *
     * @param base the base style to merge with
     */
    @Internal
    public void mergeWith(ElementBlockStyleProperties base) {
        super.mergeWith(base);

        if (base instanceof SectionStyleProperties sectionBase) {
            if (this.sectionMarker == null) {
                this.sectionMarker = sectionBase.sectionMarker;
            }
            if (this.keepTogether == null) {
                this.keepTogether = sectionBase.keepTogether;
            }

            if (this.orphans == null) {
                this.orphans = sectionBase.orphans;
            }
            if (this.widows == null) {
                this.widows = sectionBase.widows;
            }
        }
    }

    /**
     * Creates a deep copy of this style.
     *
     * @return a new SectionStyleProperties instance with the same property values
     */
    @Internal
    @Override
    public SectionStyleProperties copy() {
        SectionStyleProperties newInstance = new SectionStyleProperties();
        applyPropertiesTo(newInstance);
        return newInstance;
    }

    /**
     * Applies all properties from this instance to another SectionStyleProperties instance.
     * This is used internally by the {@link #copy()} method.
     *
     * @param newInstance the target instance to copy properties to
     */
    @Internal
    protected void applyPropertiesTo(SectionStyleProperties newInstance) {
        super.applyPropertiesTo(newInstance);

        newInstance.sectionMarker = this.sectionMarker;
        newInstance.keepTogether = this.keepTogether;
        newInstance.orphans = this.orphans;
        newInstance.widows = this.widows;
    }
}