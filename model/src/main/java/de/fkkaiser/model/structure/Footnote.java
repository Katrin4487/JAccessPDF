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
     * Creates a new Footnote element.
     *
     * <p>This constructor is used by Jackson during JSON deserialization.
     * A unique identifier is automatically generated for each footnote instance
     * to enable proper linking between references and content during rendering.</p>
     *
     * <p><b>Index Validation:</b></p>
     * If the provided index is {@code null} or empty, a warning is logged and
     * the default index "{@value #DEFAULT_INDEX}" is used instead. This ensures
     * that every footnote has a valid display marker.
     *
     * @param index          the visible footnote marker (e.g., "1", "*", "a");
     *                       if {@code null} or empty, defaults to "{@value #DEFAULT_INDEX}"
     * @param styleClass     the CSS-like style class for styling properties; may be {@code null}
     * @param inlineElements the list of inline elements forming the footnote content;
     *                       may be {@code null} or empty for footnotes without content
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
     * <p>These elements define what appears in the footnote area, typically
     * including text segments, formatted text, or even nested inline elements.
     * The content is rendered with styles inherited from the footnote's
     * resolved style properties.</p>
     *
     * @return the list of inline elements; may be {@code null} or empty
     */
    public List<InlineElement> getInlineElements() {
        return inlineElements;
    }

    /**
     * Returns the resolved style properties after style resolution.
     *
     * <p>This value is populated during the {@link #resolveStyles(StyleResolverContext)}
     * call and should not be accessed before style resolution has been performed.</p>
     *
     * @return the resolved style properties; may be {@code null} before resolution
     */
    public FootnoteStyleProperties getResolvedStyle() {
        return resolvedStyle;
    }

    /**
     * Sets the resolved style properties for this footnote.
     *
     * <p>This method is package-private and intended for internal use during
     * the style resolution process. It should not be called by client code.</p>
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
     * Resolves the style for the footnote body and delegates style resolution
     * to its child inline elements.
     *
     * <p>This method implements a two-phase resolution strategy:</p>
     *
     * <p><b>Phase 1 - Footnote Body Resolution:</b></p>
     * <ol>
     *   <li>Looks up element-specific styles using the footnote's styleClass</li>
     *   <li>If found and of type {@link FootnoteStyleProperties}, creates a copy</li>
     *   <li>Merges with parent block styles to inherit properties</li>
     *   <li>If no specific style exists, creates default properties and merges with parent</li>
     *   <li>Stores the final resolved style in {@link #resolvedStyle}</li>
     * </ol>
     *
     * <p><b>Phase 2 - Child Element Resolution:</b></p>
     * <ol>
     *   <li>Creates a new child context using the footnote's resolved style as parent</li>
     *   <li>Recursively calls {@code resolveStyles} on each inline element</li>
     *   <li>Each child inherits properties from the footnote's resolved style</li>
     * </ol>
     *
     * <p>This cascading approach ensures that footnote content can have distinct
     * styling from the main document text while maintaining internal consistency.</p>
     *
     * <p><b>Example Style Cascade:</b></p>
     * <pre>
     * Document Style (parent) → Footnote Style (this) → Text Segment Style (children)
     *   font-size: 12pt           font-size: 9pt         (inherits 9pt from footnote)
     *   color: black              color: gray            (inherits gray from footnote)
     * </pre>
     *
     * @param context the style resolver context containing the style map and parent styles;
     *                must not be {@code null}
     */
    @Override
    public void resolveStyles(StyleResolverContext context) {
        // 1. Resolve the style for the footnote body itself.
        ElementStyle specificElementStyle = context.styleMap().get(this.getStyleClass());
        if (specificElementStyle != null && specificElementStyle.properties() instanceof FootnoteStyleProperties specificStyle) {
            FootnoteStyleProperties finalStyle = specificStyle.copy();
            finalStyle.mergeWith(context.parentBlockStyle());
            this.setResolvedStyle(finalStyle);
        } else {
            // If no specific style, create a new one that just inherits from the parent.
            FootnoteStyleProperties defaultStyle = new FootnoteStyleProperties();
            defaultStyle.mergeWith(context.parentBlockStyle());
            this.setResolvedStyle(defaultStyle);
        }

        // 2. Create a new context for the children inside the footnote.
        // The resolved style of the footnote body becomes the new parent style.
        StyleResolverContext childContext = context.createChildContext(this.getResolvedStyle());

        // 3. Delegate to child elements.
        if (inlineElements != null) {
            for (InlineElement element : inlineElements) {
                element.resolveStyles(childContext);
            }
        }
    }
}