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
import de.fkkaiser.model.JsonPropertyName;
import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.structure.builder.PageSequenceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Defines a sequence of content areas for a page layout, including optional header and footer.
 *
 * @param styleClass the CSS-like style class name for page layout
 * @param body       the main content area of the page
 * @param header     the optional header content area
 * @param footer     the optional footer content area
 *
 * @author Katrin Kaiser
 * @version 1.0.1
 *
 */
public record PageSequence(
        @JsonProperty(JsonPropertyName.STYLE_CLASS)
        String styleClass,

        @JsonProperty(JsonPropertyName.BODY)
        ContentArea body,

        @JsonProperty(JsonPropertyName.HEADER)
        ContentArea header,

        @JsonProperty(JsonPropertyName.FOOTER)
        ContentArea footer
) {

    private static final Logger log = LoggerFactory.getLogger(PageSequence.class);

    /**
     * Compact constructor that validates required fields.
     *
     * @param styleClass the CSS-like style class name for page layout;
     *                   must not be {@code null} or empty
     * @throws IllegalArgumentException if styleClass is empty
     * @throws NullPointerException     if styleClass is {@code null}
     */
    public PageSequence {
        Objects.requireNonNull(styleClass, "styleClass must not be null");
        if (styleClass.trim().isEmpty()) {
            log.error("PageSequence styleClass must not be empty.");
            throw new IllegalArgumentException("styleClass must not be empty.");
        }
    }

    /**
     * Creates a new {@link PageSequenceBuilder} with the specified style class.
     *
     * @param styleClass the CSS-like style class name for page layout
     * @return a new instance of {@link PageSequenceBuilder}
     * @throws NullPointerException if styleClass is {@code null}
     */
    @PublicAPI
    public static PageSequenceBuilder builder(String styleClass) {
        Objects.requireNonNull(styleClass, "styleClass must not be null");
        return new PageSequenceBuilder(styleClass);
    }
}