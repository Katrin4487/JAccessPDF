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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.fkkaiser.model.JsonPropertyName;
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.style.ElementBlockStyleProperties;
import de.fkkaiser.model.style.ElementStyle;
import de.fkkaiser.model.style.StyleResolverContext;
import de.fkkaiser.model.style.TextRunStyleProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Represents an inline element for displaying text within a document.
 *
 * @author Katrin Kaiser
 * @version 1.0.1
 */
@JsonTypeName(JsonPropertyName.TEXT_RUN)
public class TextRun extends AbstractInlineElement {

    private static final Logger log = LoggerFactory.getLogger(TextRun.class);

    private final String text;


    @JsonIgnore
    private TextRunStyleProperties resolvedStyle;

    /**
     * Construtor for text-run
     *
     * @param text       text content for this text-run
     * @param styleClass name of the style-class (can be null)
     * @throws NullPointerException if text is null
     */
    @JsonCreator
    public TextRun(
            @JsonProperty(JsonPropertyName.TEXT) String text,
            @JsonProperty(JsonPropertyName.STYLE_CLASS) String styleClass
    ) {
        super(styleClass);
        Objects.requireNonNull(text, "text must not be null");
        this.text = text;
    }

    /**
     * Contstructor for a Text-Run with no specific style class.
     * A Text-Run can inherit styles from its parent
     *
     * @param text text content of the text-run (should not be {@code null})
     * @throws NullPointerException if text is {@code null}
     */
    @PublicAPI
    public TextRun(String text) {
        this(text, null);
    }

    /**
     * Returns the type of this element
     *
     * @return ElementTargetType.TEXT_RUN
     */
    @Internal
    @Override
    public ElementTargetType getType() {
        return ElementTargetType.TEXT_RUN;
    }

    /**
     * Returns the resolved style of this element
     *
     * @return TextRunStyleProperty of this element
     */
    @Internal
    public TextRunStyleProperties getResolvedStyle() {
        return resolvedStyle;
    }

    /**
     * Retuns the text-content of this text run
     *
     * @return text content
     */
    @Internal
    public String getText() {
        return text;
    }

    /**
     * Resolves the styles for this text run based on the provided context.
     * TIt determines the final value for each property
     * by checking the specific style first, then falling back to the parent style.
     *
     * @param context The style resolver context containing necessary style information.
     */
    @Override
    public void resolveStyles(StyleResolverContext context) {
        ElementBlockStyleProperties parentStyle = context.parentBlockStyle();
        TextRunStyleProperties specificRunStyle = new TextRunStyleProperties();

        if (styleClass != null && !styleClass.isEmpty()) {

            ElementStyle specificElementStyle = context.styleMap().get(styleClass);
            if (specificElementStyle != null && specificElementStyle.properties() instanceof TextRunStyleProperties) {

                specificRunStyle = (TextRunStyleProperties) specificElementStyle.properties();

            } else {
                log.warn("Style class '{}' not found or has incorrect type.", this.styleClass);
                specificRunStyle = new TextRunStyleProperties();

            }

        }

        log.debug("Resolving specific run style {} for text {}", specificRunStyle.getTextStyleName(), this.text);

        this.resolvedStyle = TextRunStyleProperties.createResolved(parentStyle, specificRunStyle);
    }
}
