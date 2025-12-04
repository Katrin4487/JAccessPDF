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

import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.structure.ElementTargetType;
import de.fkkaiser.model.style.*;

/**
 * Fluent builder for creating paragraph element styles with detailed properties.
 * This builder allows setting all paragraph-specific properties in a readable,
 * chainable manner.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
@PublicAPI
public class ParagraphStyleBuilder {
    private final String name;
    private final ParagraphStyleProperties properties;

    /**
     * Creates a paragraph style builder with a text style reference.
     *
     * @param name      the name of this paragraph style
     * @param textStyle the text style to use
     */
    @PublicAPI
    public ParagraphStyleBuilder(String name, TextStyle textStyle) {
        this.name = name;
        this.properties = new ParagraphStyleProperties();
        this.properties.setTextStyleName(textStyle.name());
    }

    /**
     * Creates a paragraph style builder with a text style name reference.
     *
     * @param name          the name of this paragraph style
     * @param textStyleName the name of the text style to use
     */
    @PublicAPI
    public ParagraphStyleBuilder(String name, String textStyleName) {
        this.name = name;
        this.properties = new ParagraphStyleProperties();
        this.properties.setTextStyleName(textStyleName);
    }

    /**
     * Defines the text alignment for this paragraph.
     *
     * @param textAlign the text alignment to set
     * @return this builder instance for method chaining
     */
    @PublicAPI
    public ParagraphStyleBuilder withTextAlign(TextAlign textAlign) {
        properties.setTextAlign(textAlign);
        return this;
    }

    /**
     * Defines the text indent (space before the text in the first line).
     * You can choose between the units: mm, cm, in, pt, pc, em or %.
     *
     * @param textIndent value with unit for the text indent (e.g., "2em", "10mm")
     * @return this builder instance for method chaining
     */
    @PublicAPI
    public ParagraphStyleBuilder withTextIndent(String textIndent) {
        properties.setTextIndent(textIndent);
        return this;
    }

    /**
     * Defines the white space before a text block.
     * You can choose between the units: mm, cm, in, pt, pc, em or %.
     *
     * @param spaceBefore value with unit for space before (e.g., "1em", "5mm")
     * @return this builder instance for method chaining
     */
    @PublicAPI
    public ParagraphStyleBuilder withSpaceBefore(String spaceBefore) {
        properties.setSpaceBefore(spaceBefore);
        return this;
    }

    /**
     * Defines the white space after a text block.
     * You can choose between the units: mm, cm, in, pt, pc, em or %.
     *
     * @param spaceAfter value with unit for space after (e.g., "1em", "5mm")
     * @return this builder instance for method chaining
     */
    @PublicAPI
    public ParagraphStyleBuilder withSpaceAfter(String spaceAfter) {
        properties.setSpaceAfter(spaceAfter);
        return this;
    }

    /**
     * Defines if automatic splitting (or division) of words at the end of a line is enabled.
     * You must specify the correct language of your text so that hyphenation works correctly.
     *
     * @param hyphenate true to enable hyphenation, false otherwise
     * @return this builder instance for method chaining
     */
    @PublicAPI
    public ParagraphStyleBuilder withHyphenation(boolean hyphenate) {
        properties.setHyphenate(hyphenate);
        return this;
    }

    /**
     * Defines the language of the paragraph. This only makes sense if the paragraph
     * is written in a different language than the document.
     *
     * @param language language code in BCP 47 format (e.g., "en-US", "de-DE")
     * @return this builder instance for method chaining
     */
    @PublicAPI
    public ParagraphStyleBuilder withLanguage(String language) {
        properties.setLanguage(language);
        return this;
    }

    /**
     * Defines the background color of this paragraph. Valid color values are:
     * <ul>
     *     <li><strong>Hex code:</strong> e.g., #F0F0F0</li>
     *     <li><strong>Color name:</strong> e.g., yellow</li>
     *     <li><strong>RGB function:</strong> rgb(255,0,0)</li>
     * </ul>
     *
     * @param color background color for this paragraph
     * @return this builder instance for method chaining
     */
    @PublicAPI
    public ParagraphStyleBuilder withBackgroundColor(String color) {
        properties.setBackgroundColor(color);
        return this;
    }

    /**
     * Builds the paragraph element style with all configured properties.
     * This method must be called at the end of every builder chain.
     *
     * @return the configured {@link ElementStyle} for the paragraph
     */
    @PublicAPI
    public ElementStyle build() {
        return new ElementStyle(name, ElementTargetType.PARAGRAPH, properties);
    }
}