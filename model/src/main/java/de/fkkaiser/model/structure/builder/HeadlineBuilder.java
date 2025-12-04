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

import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.structure.Headline;
import de.fkkaiser.model.structure.InlineElement;
import de.fkkaiser.model.structure.TextRun;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Fluent builder for creating Headline elements with inline content and level.
 *
 * @author Katrin Kaiser
 * @version 1.0.1
 */
@PublicAPI
public class HeadlineBuilder {

    private final List<InlineElement> inlineElements;
    private int level;
    private final String styleClass;

    /**
     * Constructor for HeadlineBuilder.
     *
     * @param styleClass The style class to be applied to the headline.
     * @throws NullPointerException if the styleClass is null.
     */
    @Internal
    public HeadlineBuilder(String styleClass) {
        Objects.requireNonNull(styleClass);
        this.styleClass = styleClass;
        this.inlineElements = new ArrayList<>();
        this.level = 1;
    }

    /**
     * Adds an inline element to the headline.
     *
     * @param inlineElement The inline element to add.
     * @return The HeadlineBuilder instance for method chaining.
     * @throws NullPointerException if the inlineElement is null.
     *
     */
    @PublicAPI
    public HeadlineBuilder addInlineElement(InlineElement inlineElement) {
        Objects.requireNonNull(inlineElement);
        this.inlineElements.add(inlineElement);
        return this;
    }

    /**
     * Adds a text run to the headline.
     *
     * @param text The text content to add.
     * @return The HeadlineBuilder instance for method chaining.
     * @throws NullPointerException if the text is null.
     */
    @PublicAPI
    public HeadlineBuilder addInlineText(String text) {
        Objects.requireNonNull(text, "Text for TextRun cannot be null.");
        this.inlineElements.add(new TextRun(text, null));
        return this;
    }

    /**
     * Sets the level of the headline.
     *
     * @param level The level to set (e.g., 1 for H1, 2 for H2).
     * @return The HeadlineBuilder instance for method chaining.
     * @throws IllegalArgumentException if the level is not between 1 and 6.
     */
    @PublicAPI
    public HeadlineBuilder setLevel(int level) {
        if (level < 1 || level > 6) {
            throw new IllegalArgumentException("Headline level must be between 1 and 6.");
        }
        this.level = level;
        return this;
    }


    /**
     * Builds and returns the Headline object.
     *
     * @return The constructed Headline object.
     */
    @PublicAPI
    public Headline build() {
        return new Headline(this.styleClass, this.inlineElements, this.level);
    }

}
