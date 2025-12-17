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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.fkkaiser.model.JsonPropertyName;
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.structure.builder.ParagraphBuilder;
import de.fkkaiser.model.style.StandardElementType;

import java.util.List;


/**
 * Represents a paragraph element in a document structure.
 *
 * @author Katrin Kaiser
 * @version 1.1.2
 */
@PublicAPI
@JsonTypeName(JsonPropertyName.PARAGRAPH)
public class Paragraph extends TextBlock {

    /**
     * Constructs a Paragraph with the specified style class and inline elements.
     *
     * @param styleClass     name of the style class for the paragraph
     * @param inlineElements list of inline elements contained in the paragraph
     */
    @PublicAPI
    public Paragraph(
            @JsonProperty(JsonPropertyName.STYLE_CLASS) String styleClass,
            @JsonProperty(JsonPropertyName.INLINE_ELEMENTS) List<InlineElement> inlineElements
    ) {
        super(styleClass, inlineElements);
    }

    /**
     * Creates a paragraph with a single standalone text run.
     *
     * @param styleClass     the CSS-like class name for styling;
     *                       must not be {@code null}
     * @param standAloneText the standalone text content of the paragraph;
     */
    @PublicAPI
    public Paragraph(String styleClass, String standAloneText) {
        super(styleClass, List.of(new TextRun(standAloneText, null)));
    }


    /**
     * Creates a paragraph with a single inline element.
     *
     * @param styleClass    the CSS-like class name for styling;
     *                      must not be {@code null}
     * @param inlineElement the inline element to include in the paragraph;
     */
    @PublicAPI
    public Paragraph(String styleClass, InlineElement inlineElement) {
        super(styleClass, List.of(inlineElement));
    }


    /**
     * Constructs a Paragraph with the specified style class.
     *
     * @param styleClass name of the style class for the paragraph
     */
    @PublicAPI
    public Paragraph(String styleClass) {
        super(styleClass);
    }

    // ==================== Overrides ====================

    /**
     * Returns the type of this element as PARAGRAPH.
     *
     * @return ElementTargetType.PARAGRAPH
     */
    @Override
    public ElementTargetType getType() {
        return ElementTargetType.PARAGRAPH;
    }

    /**
     * Creates a new {@link ParagraphBuilder} instance for building a paragraph
     * with the specified style class.
     *
     * @param styleClass the CSS-like class name for styling;
     *                   must not be empty
     * @return a new ParagraphBuilder instance
     * @throws IllegalArgumentException if styleClass is empty
     *
     */
    @PublicAPI
    public static ParagraphBuilder builder(String styleClass) {
        return new ParagraphBuilder(styleClass);
    }

    /**
     * Gets the standard element type for the paragraph.
     *
     * @return StandardElementType.P
     */
    @Internal
    @Override
    public StandardElementType getStandardElementType() {
        return StandardElementType.P;
    }
}