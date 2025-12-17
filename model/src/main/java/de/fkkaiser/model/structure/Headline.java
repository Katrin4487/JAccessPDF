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
package de.fkkaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.fkkaiser.model.JsonPropertyName;
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.structure.builder.HeadlineBuilder;
import de.fkkaiser.model.style.StandardElementType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * Represents a headline element in a document structure.
 *
 * @author Katrin Kaiser
 * @version 1.1.1
 */
@PublicAPI
@JsonTypeName(JsonPropertyName.HEADLINE)
public final class Headline extends TextBlock {

    private static final Logger log = LoggerFactory.getLogger(Headline.class);

    /**
     * Default level used when no level is specified or when {@code null} is provided.
     * Level 1 represents the highest level in the document hierarchy.
     */
    private static final int DEFAULT_LEVEL = 1;

    /**
     * The hierarchy level of the headline, corresponding to h1-h6 in HTML.
     * Valid values are 1 through 6, with 1 being the highest level.
     */
    private final int level;

    private String id;

    /**
     * Constructs a Headline with the specified style class, inline elements, and level.
     *
     * @param styleClass     the CSS-like style class for styling
     * @param inlineElements the list of inline elements that make up the headline content
     * @param level          the headline level (1-6); if {@code null}, defaults to {@value #DEFAULT_LEVEL}
     */
    @JsonCreator
    public Headline(
            @JsonProperty(JsonPropertyName.STYLE_CLASS) String styleClass,
            @JsonProperty(JsonPropertyName.INLINE_ELEMENTS) List<InlineElement> inlineElements,
            @JsonProperty(JsonPropertyName.LEVEL) Integer level
    ) {
        super(styleClass, inlineElements);

        if (level == null) {
            log.warn("Headline 'level' is not defined. Using default value {}.", DEFAULT_LEVEL);
            this.level = DEFAULT_LEVEL;
        } else if (level < 1 || level > 6) {
            log.warn("Headline 'level' is out of bounds ({}). It must be between 1 and 6. Using default value {}.", level, DEFAULT_LEVEL);
            this.level = DEFAULT_LEVEL;
        } else {
            this.level = level;
        }
    }

    /**
     * Convenience constructor for creating a simple headline with plain text content.
     *
     * <p>This constructor allows quick creation of headlines by providing just
     * the style class, text content, and level. It internally creates a single
     * {@link TextRun} inline element to hold the provided text.</p>
     *
     * @param styleClass the CSS-like style class for styling
     * @param text       the plain text content of the headline
     * @param level      the headline level (1-6); if {@code null}, defaults to {@value #DEFAULT_LEVEL}
     */
    public Headline(String styleClass, String text, int level) {
        this(styleClass, Collections.singletonList(new TextRun(text)), level);
    }

    /**
     * Returns the type of this element, which is {@link ElementTargetType#HEADLINE}.
     * @return the element type HEADLINE
     */
    @Override
    public ElementTargetType getType() {
        return ElementTargetType.HEADLINE;
    }

    /**
     * Returns the hierarchical level of this headline.
     *
     * <p>The level indicates the headline's position in the document hierarchy,
     * with 1 being the highest (most important) level and 6 being the lowest.
     * This value is guaranteed to be non-null due to the default fallback in
     * the constructor.</p>
     *
     * @return the headline level (1-6); never {@code null}
     */
    public int getLevel() {
        return level;
    }

    /**
     * Returns the unique identifier of the headline.
     * Please note that this value shall be set during generation
     * and may be {@code null} beforehand.
     * @return the headline ID, or {@code null} if not set
     */
    @Internal
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the headline.
     * This method is intended for internal use during generation.
     *
     * @param id the headline ID to set
     */
    @Internal
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Creates a new {@link HeadlineBuilder} for constructing Headline instances.
     *
     * @param styleClass The style class to be applied to the headline.
     * @return A new HeadlineBuilder instance.
     */
    @PublicAPI
    public static HeadlineBuilder builder(String styleClass) {
        return new HeadlineBuilder(styleClass);
    }

    /**
     * Gets the standard element type corresponding to the headline level.
     *
     * @return the standard element type for this headline
     */
    @Override
    public StandardElementType getStandardElementType() {

        return switch (level) {
            case 2 -> StandardElementType.H2;
            case 3 -> StandardElementType.H3;
            case 4 -> StandardElementType.H4;
            case 5 -> StandardElementType.H5;
            case 6 -> StandardElementType.H6;
            default -> StandardElementType.H1;
        };

    }
}