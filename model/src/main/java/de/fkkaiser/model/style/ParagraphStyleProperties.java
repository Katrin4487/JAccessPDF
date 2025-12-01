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
import de.fkkaiser.model.annotation.Inheritable;
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;

/**
 * Represents the style properties specific to a paragraph element.
 * <p>
 * This class extends `TextBlockStyleProperties` and provides additional
 * properties such as text indentation, alignment, hyphenation, and language.
 * It also supports inheritable properties for consistent styling.
 * <p>
 * The class includes methods for merging styles, copying properties, and
 * applying them to other style objects.
 *
 * @author Katrin Kaiser
 * @version 1.0.1
 */
@Internal
@JsonTypeName(StyleTargetTypes.PARAGRAPH)
public class ParagraphStyleProperties extends TextBlockStyleProperties {

    @Inheritable
    @JsonProperty("text-indent")
    private String textIndent;

    @Inheritable
    @JsonProperty("text-align")
    private String textAlign; //justify..

    @Inheritable
    @JsonProperty("text-align-last")
    private String textAlignLast; //start, end...

    @Inheritable
    @JsonProperty("hyphenate")
    private boolean hyphenate;

    @Inheritable
    @JsonProperty("language")
    private String language;

    @Inheritable
    @JsonProperty("orphans")
    private boolean orphans;

    @Inheritable
    @JsonProperty("widows")
    private boolean widows;

    // --- Getters and Setters ---

    /**
     * Gets the text indentation for the paragraph.
     *
     * @return the text indentation as a `String`
     */
    public String getTextIndent() {
        return textIndent;
    }


    /**
     * Sets the text indentation for the paragraph.
     *
     * @param textIndent the text indentation to set
     */
    public void setTextIndent(String textIndent) {
        this.textIndent = textIndent;
    }

    /**
     * Checks if widows are enabled for the paragraph.
     *
     * @return `true` if widows are enabled, otherwise `false`
     */
    @Internal
    public boolean isWidows() {
        return widows;
    }


    /**
     * Sets the widows property for the paragraph.
     *
     * @param widows the widows property to set
     */
    @PublicAPI
    public void setWidows(boolean widows) {
        this.widows = widows;
    }

    /**
     * Gets the text alignment for the paragraph.
     *
     * @return the text alignment as a `String`
     */
    @Internal
    @Override
    public String getTextAlign() {
        return textAlign;
    }

    /**
     * Sets the text alignment for the paragraph.
     *
     * @param textAlign the text alignment to set
     */
    @PublicAPI
    @Override
    public void setTextAlign(String textAlign) {
        this.textAlign = textAlign;
    }

    /**
     * Gets the alignment of the last line of the paragraph.
     *
     * @return the alignment of the last line as a `String`
     */
    @Internal
    public String getTextAlignLast() {
        return textAlignLast;
    }

    /**
     * Sets the alignment of the last line of the paragraph.
     *
     * @param textAlignLast the alignment to set
     */
    @PublicAPI
    public void setTextAlignLast(String textAlignLast) {
        this.textAlignLast = textAlignLast;
    }

    /**
     * Checks if hyphenation is enabled for the paragraph.
     *
     * @return `true` if hyphenation is enabled, otherwise `false`
     */
    @Internal
    public boolean isHyphenate() {
        return hyphenate;
    }

    /**
     * Sets the hyphenation property for the paragraph.
     *
     * @param hyphenate the hyphenation property to set
     */
    @PublicAPI
    public void setHyphenate(boolean hyphenate) {
        this.hyphenate = hyphenate;
    }

    /**
     * Gets the language of the paragraph content.
     *
     * @return the language as a `String`
     */
    public String getLanguage() {
        return language;
    }


    /**
     * Sets the language of the paragraph content.
     *
     * @param language the language to set
     */
    @PublicAPI
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Checks if orphans are enabled for the paragraph.
     *
     * @return `true` if orphans are enabled, otherwise `false`
     */
    @Internal
    public boolean isOrphans() {
        return orphans;
    }


    /**
     * Sets the orphans property for the paragraph.
     *
     * @param orphans the orphans property to set
     */
    @PublicAPI
    public void setOrphans(boolean orphans) {
        this.orphans = orphans;
    }




    /**
     * Creates a copy of the current `ParagraphStyleProperties` object.
     *
     * @return a new `ParagraphStyleProperties` instance with the same properties
     */
    @Internal
    @Override
    public ParagraphStyleProperties copy() {
        ParagraphStyleProperties newInstance = new ParagraphStyleProperties();
        applyPropertiesTo(newInstance);

        return newInstance;
    }

    /**
     * Applies the current properties to another `ElementBlockStyleProperties` object.
     * <p>
     * This method copies all paragraph-specific properties to the target object.
     *
     * @param target the target `ElementBlockStyleProperties` to apply properties to
     */
    @Internal
    @Override
    protected void applyPropertiesTo(ElementBlockStyleProperties target) {
        super.applyPropertiesTo(target);
        if (target instanceof ParagraphStyleProperties paragraphTarget) {
            paragraphTarget.textIndent = this.textIndent;
            paragraphTarget.textAlign = this.textAlign;
            paragraphTarget.textAlignLast = this.textAlignLast;
            paragraphTarget.hyphenate = this.hyphenate;
            paragraphTarget.language = this.language;
            paragraphTarget.orphans = this.orphans;
            paragraphTarget.widows = this.widows;
        }
    }
}
