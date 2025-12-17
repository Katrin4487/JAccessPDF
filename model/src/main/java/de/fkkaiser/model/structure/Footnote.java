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
import de.fkkaiser.model.style.ElementStyle;
import de.fkkaiser.model.style.FootnoteStyleProperties;
import de.fkkaiser.model.style.StandardElementType;
import de.fkkaiser.model.style.StyleResolverContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

/**
 * Represents a footnote element within a document.
 *
 * <p>A footnote consists of a visible index marker and a collection of inline
 * elements that form the footnote content. The footnote is linked to its
 * reference in the main text via a unique identifier.</p>
 *
 * <p>Footnotes are typically rendered at the bottom of the page or section
 * where they are referenced, with their content styled according to the
 * resolved footnote style properties.</p>
 *
 * @author Katrin Kaiser
 * @version 1.0.1
 */
@JsonTypeName(JsonPropertyName.FOOTNOTE)
public final class Footnote extends AbstractInlineElement {

    private static final Logger log = LoggerFactory.getLogger(Footnote.class);

    /**
     * Default index value used when no index is provided or when an empty index is given.
     * The asterisk is a common typographical symbol for footnotes, especially for
     * single footnotes or when numeric indexes are not appropriate.
     */
    private static final String DEFAULT_INDEX = "*";

    @JsonIgnore
    private final String id;
    private final String index;
    private final List<InlineElement> inlineElements;

    @JsonIgnore
    private FootnoteStyleProperties resolvedStyle;

    /**
     * Constructs a Footnote with the specified index, style class, and inline
     * elements.
     *
     * @param index          the visible index marker for the footnote; if {@code null}
     *                       or empty, defaults to "{@value #DEFAULT_INDEX}"
     * @param styleClass     the CSS-like style class for styling
     * @param inlineElements the list of inline elements that make up the footnote content
     *
     */
    @JsonCreator
    public Footnote(
            @JsonProperty("index") String index,
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("inline-elements") List<InlineElement> inlineElements
    ) {
        super(styleClass);
        this.id = "footnote-" + UUID.randomUUID();
        if (index == null || index.isEmpty()) {
            log.warn("index is null or empty. Set default one {}", DEFAULT_INDEX);
            this.index = DEFAULT_INDEX;
        } else {
            this.index = index;
        }
        this.inlineElements = inlineElements;
    }

    /**
     * Returns the unique identifier for this footnote.
     *
     * <p>This ID is automatically generated upon construction using a UUID and is
     * prefixed with "footnote-" for clarity. The ID is used internally to link
     * footnote references in the main text with their corresponding content in
     * the footnote area during PDF rendering.</p>
     *
     * <p><b>Note:</b> This ID is not serialized to JSON and will be different
     * each time a footnote is deserialized from JSON.</p>
     *
     * @return the unique footnote identifier (e.g., "footnote-123e4567-e89b-12d3-a456-426614174000")
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the visible index marker for this footnote.
     *
     * <p>The index is what appears in the document as the footnote reference.
     * This value is guaranteed to be non-null and non-empty, with a default
     * value of "{@value #DEFAULT_INDEX}" applied if none was provided.</p>
     *
     * @return the footnote index marker; never {@code null} or empty
     */
    public String getIndex() {
        return index;
    }

    /**
     * Returns the list of inline elements that form the footnote content.
     *
     * @return the list of inline elements; may be {@code null} or empty
     */
    public List<InlineElement> getInlineElements() {
        return inlineElements;
    }

    /**
     * Returns the resolved style properties after style resolution.
     *
     * @return the resolved style properties; may be {@code null} before resolution
     */
    public FootnoteStyleProperties getResolvedStyle() {
        return resolvedStyle;
    }

    /**
     * Sets the resolved style properties for this footnote.
     *
     * @param resolvedStyle the resolved style properties to set
     */
    void setResolvedStyle(FootnoteStyleProperties resolvedStyle) {
        this.resolvedStyle = resolvedStyle;
    }

    /**
     * Returns the type identifier for this element.
     *
     * @return the element's target type FOOTNOTE
     */
    @Override
    public ElementTargetType getType() {
        return ElementTargetType.FOOTNOTE;
    }

    /**
     * Resolves and applies styles for this footnote using the provided context.
     *
     * @param context the style resolver context containing style information
     */
    @Override
    public void resolveStyles(StyleResolverContext context) {
        ElementStyle specificElementStyle = context.styleMap().get(this.getStyleClass());
        if (specificElementStyle != null && specificElementStyle.properties() instanceof FootnoteStyleProperties specificStyle) {
            FootnoteStyleProperties finalStyle = specificStyle.copy();
            finalStyle.mergeWith(context.parentBlockStyle());
            this.setResolvedStyle(finalStyle);
        } else {
            FootnoteStyleProperties defaultStyle = new FootnoteStyleProperties();
            defaultStyle.mergeWith(context.parentBlockStyle());
            this.setResolvedStyle(defaultStyle);
        }

        StyleResolverContext childContext = context.createChildContext(this.getResolvedStyle());

        if (inlineElements != null) {
            for (InlineElement element : inlineElements) {
                element.resolveStyles(childContext);
            }
        }
    }

    /**
     * Returns the standard element type for this footnote.
     *
     * @return the standard element type FOOTNOTE
     */
    @Override
    public StandardElementType getStandardElementType() {
        return StandardElementType.FOOTNOTE;
    }
}