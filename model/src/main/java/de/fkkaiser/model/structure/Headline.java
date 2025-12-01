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
import de.fkkaiser.model.annotation.Internal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * Represents a headline element in a PDF document structure.
 *
 * <p>Headlines are specialized text blocks that provide hierarchical structure and
 * visual emphasis in documents. They function similarly to HTML heading tags (h1-h6),
 * with numeric levels indicating their position in the document hierarchy. Headlines
 * typically receive distinct styling (larger font size, bold weight, increased spacing)
 * to make them stand out from regular body text.</p>
 *
 * <p><b>Purpose in PDF Generation:</b></p>
 * During PDF rendering, headlines serve multiple purposes:
 * <ul>
 *   <li><b>Visual Structure:</b> Provide visual hierarchy through distinct styling</li>
 *   <li><b>Document Outline:</b> Can be used to generate PDF bookmarks and table of contents</li>
 *   <li><b>Accessibility:</b> Enable screen readers to navigate document structure (PDF/UA)</li>
 *   <li><b>Semantic Markup:</b> Tag content with semantic meaning for tagged PDFs</li>
 * </ul>
 *
 * <p><b>Hierarchy Levels:</b></p>
 * Headlines support six hierarchical levels (1-6), following the HTML heading convention:
 * <ul>
 *   <li><b>Level 1:</b> Top-level headlines (e.g., document title, major sections)</li>
 *   <li><b>Level 2:</b> Major subsections</li>
 *   <li><b>Level 3:</b> Sub-subsections</li>
 *   <li><b>Levels 4-6:</b> Finer-grained hierarchical divisions</li>
 * </ul>
 *
 * <p>Lower numbers indicate higher importance in the hierarchy. A well-structured document
 * typically uses levels sequentially without skipping (e.g., h1 → h2 → h3, not h1 → h3).</p>
 *
 * <p><b>Inheritance from TextBlock:</b></p>
 * Headline extends {@link TextBlock}, inheriting its ability to contain inline elements
 * and participate in style resolution. This means headlines can contain formatted text,
 * inline images, or other inline elements, not just plain text. The TextBlock foundation
 * provides the underlying text handling and style resolution mechanisms.
 *
 * <p><b>JSON Representation:</b></p>
 * <pre>{@code
 * {
 *   "type": "headline",
 *   "level": 1,
 *   "style-class": "section-title",
 *   "variant": "primary",
 *   "inline-elements": [
 *     {
 *       "type": "text-run",
 *       "text": "Chapter 1: Introduction"
 *     }
 *   ]
 * }
 * }</pre>
 *
 * <p><b>Default Level Handling:</b></p>
 * If no level is specified in the JSON or {@code null} is provided, the headline
 * defaults to level {@value #DEFAULT_LEVEL}. This ensures that every headline has
 * a valid level, though a warning is logged to alert developers of the missing value.
 * It is recommended to always specify the level explicitly for clarity and proper
 * document structure.
 *
 * <p><b>Usage Example 1 - JSON Deserialization:</b></p>
 * <pre>{@code
 * // JSON is automatically deserialized to a Headline via Jackson
 * {
 *   "type": "headline",
 *   "level": 2,
 *   "style-class": "subsection",
 *   "inline-elements": [
 *     { "type": "text-run", "text": "Background" }
 *   ]
 * }
 * }</pre>
 *
 * <p><b>Usage Example 2 - Programmatic Construction:</b></p>
 * <pre>{@code
 * // Using the convenience constructor for simple text headlines
 * Headline title = new Headline("chapter-title", "Chapter 1: Introduction", 1);
 *
 * // Using the full constructor with formatted content
 * TextRun bold = new TextRun("Important", "emphasis", null);
 * TextRun regular = new TextRun(" Section", null, null);
 * Headline heading = new Headline(
 *     "subsection",
 *     List.of(bold, regular),
 *     "warning",
 *     2
 * );
 * }</pre>
 *
 * <p><b>Style Resolution:</b></p>
 * Headline inherits style resolution from {@link TextBlock}. During resolution,
 * the headline's styleClass is used to look up specific styling properties that
 * define its appearance (font, size, weight, color, spacing). These styles are
 * typically configured to visually distinguish headlines from body text based
 * on their level.
 *
 * <p><b>Best Practices:</b></p>
 * <ul>
 *   <li>Always specify explicit level values for clarity</li>
 *   <li>Use levels sequentially to maintain proper hierarchy</li>
 *   <li>Define distinct styles for different headline levels</li>
 *   <li>Use descriptive styleClass names that indicate purpose (e.g., "chapter-title", "subsection-header")</li>
 *   <li>Consider accessibility when structuring document hierarchy</li>
 * </ul>
 *
 * @author FK Kaiser
 * @version 1.0
 * @see TextBlock
 * @see InlineElement
 * @see ElementTypes
 */
@JsonTypeName(ElementTypes.HEADLINE)
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
     * Creates a new Headline element with full configuration.
     *
     * <p>This constructor is the primary constructor used by Jackson during JSON
     * deserialization. It initializes all properties including those inherited from
     * parent classes ({@link TextBlock} and {@link AbstractElement}).</p>
     *
     * <p><b>Level Validation:</b></p>
     * If the provided level is {@code null}, a warning is logged and the default
     * level {@value #DEFAULT_LEVEL} is used. This ensures every headline has a valid
     * level value, though explicit specification is recommended.
     *
     * <p><b>Note:</b> While the level parameter accepts {@code Integer} (nullable)
     * for JSON compatibility, the resulting headline will always have a non-null
     * level value due to the default fallback.</p>
     *
     * @param styleClass     the CSS-like style class for styling properties; may be {@code null}
     * @param inlineElements the list of inline elements forming the headline content;
     *                       may be {@code null} or empty
     * @param variant        an optional variant identifier for style variations; may be {@code null}
     * @param level          the headline level (1-6); if {@code null}, defaults to {@value #DEFAULT_LEVEL}
     */
    @JsonCreator
    public Headline(
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("inline-elements") List<InlineElement> inlineElements,
            @JsonProperty("variant") String variant,
            @JsonProperty("level") Integer level
    ) {
        super(styleClass, inlineElements, variant);

        if (level == null) {
            log.warn("Headline 'level' is not defined. Using default value {}.", DEFAULT_LEVEL);
            this.level = DEFAULT_LEVEL;
        } else {
            this.level = level;
        }
    }

    /**
     * Creates a new Headline element with simple text content.
     *
     * <p>This convenience constructor simplifies the creation of headlines containing
     * only plain text. It automatically wraps the provided text in a {@link TextRun}
     * and constructs a headline without a variant.</p>
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * Headline title = new Headline("chapter-title", "Introduction", 1);
     * Headline subsection = new Headline("subsection", "Background Information", 2);
     * }</pre>
     *
     * <p><b>Note:</b> For headlines with formatted text, mixed styles, or other
     * inline elements, use the full constructor {@link #Headline(String, List, String, Integer)}
     * instead.</p>
     *
     * @param styleClass the CSS-like style class for styling properties; may be {@code null}
     * @param text       the plain text content of the headline; may be {@code null}
     * @param level      the headline level (1-6); if {@code null}, defaults to {@value #DEFAULT_LEVEL}
     */
    public Headline(String styleClass, String text, Integer level) {
        this(styleClass, Collections.singletonList(new TextRun(text)), null, level);
    }

    /**
     * Returns the element type identifier.
     *
     * @return the constant {@link ElementTypes#HEADLINE}
     */
    @Override
    public String getType() {
        return ElementTypes.HEADLINE;
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

    @Internal
    public String getId() {
        return id;
    }

    @Internal
    public void setId(String id) {
        this.id = id;
    }
}