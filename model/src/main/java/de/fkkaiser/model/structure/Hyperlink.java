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
import de.fkkaiser.model.JsonPropertyName;
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a hyperlink inline element with text, style, href, and alt text.
 * @author Katrin Kaiser
 * @version 1.1.1
 */
public class Hyperlink extends TextRun {

    private static final Logger log = LoggerFactory.getLogger(Hyperlink.class);

    private final String href;
    private final String altText;


    /**
     * Constructs a Hyperlink with the specified text, style class, href, and alt text.
     * @param text the visible text of the hyperlink
     * @param styleClass the CSS-like style class for styling
     * @param href the URL or destination of the hyperlink
     * @param altText the alternative text for accessibility purposes
     */
    @PublicAPI
    @JsonCreator
    public Hyperlink(
            @JsonProperty(JsonPropertyName.TEXT) String text,
            @JsonProperty(JsonPropertyName.STYLE_CLASS) String styleClass,
            @JsonProperty(JsonPropertyName.HREF) String href,
            @JsonProperty(JsonPropertyName.ALT_TEXT) String altText
    ) {
        super(text, styleClass);

        if (text == null || text.isBlank()) {
            log.info("text is null; Setting href as text");
        }
        if (href == null || href.isBlank()) {
            log.warn("Hyperlink created with empty or null href");
            this.href = "";
        } else {
            this.href = href;
        }
        if (altText == null || altText.isBlank()) {
            log.info("altText is null; Setting text as altText");
            this.altText = text;
        } else {
            this.altText = altText;
        }
    }

    /**
     * Constructs a Hyperlink with the specified href and style class.
     * The visible text and href are set to the provided href value, and altText is null.
     * @param href the URL or destination of the hyperlink
     * @param styleClass the CSS-like style class for styling
     */
    @PublicAPI
    public Hyperlink(String href, String styleClass) {
        this(href, styleClass, href, null);
    }

    /**
     * Constructs a Hyperlink with the specified href, style class, and alt text.
     * The visible text is set to the provided href value.
     * @param href the URL or destination of the hyperlink
     * @param styleClass the CSS-like style class for styling
     * @param altText the alternative text for accessibility purposes
     */
    @PublicAPI
    public Hyperlink(String href, String styleClass,String altText) {
        this(href, styleClass, href, altText);
    }


    /**
     * Returns the URL or destination of this hyperlink.
     * @return the hyperlink destination URL; never {@code null}, may be empty
     */
    @Internal
    public String getHref() {
        return href;
    }

    /**
     * Returns the alternative text for accessibility purposes.
     *
     * <p>The alternative text provides a description of the link for screen readers
     * and assistive technologies. If no explicit alternative text was provided during
     * construction, this returns the visible text content as fallback.</p>
     *
     * @return the alternative text; may be {@code null} if both altText and text were null
     */
    @Internal
    public String getAltText() {
        return altText;
    }

    /**
     * Returns the type of this element as HYPERLINK.
     * @return the element type HYPERLINK
     */
    @Override
    public ElementTargetType getType() {
        return ElementTargetType.HYPERLINK;
    }
}