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

// ==================== Builder Factory ====================

import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.structure.Hyperlink;
import de.fkkaiser.model.structure.InlineElement;
import de.fkkaiser.model.structure.Paragraph;
import de.fkkaiser.model.structure.TextRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * Fluent builder for creating Paragraph elements with inline content.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
@PublicAPI
public class ParagraphBuilder {

    private static final Logger log = LoggerFactory.getLogger(ParagraphBuilder.class);

    private final String styleClass;
    private String styleClassRegular;
    private String styleClassBold;
    private String styleClassItalic;
    private String styleClassHyperlink;
    private final List<InlineElement> inlineElements;


    /**
     * Paragraph builder constructor.
     * @param styleClass the CSS-like class name for styling;
     */
    @Internal
    public ParagraphBuilder(String styleClass) {
        if (styleClass == null || styleClass.trim().isEmpty()) {
            throw new IllegalArgumentException("Style class cannot be null or empty");
        }
        this.styleClass = styleClass;
        this.inlineElements = new ArrayList<>();
    }

    /**
     * Adds multiple inline elements to the paragraph at once.
     * This is useful when you have pre-built inline elements to add.
     *
     * @param inlineElements the inline elements to add; must not be {@code null}
     * @return this builder instance for method chaining
     * @throws IllegalArgumentException if inlineElements is {@code null}
     */
    @PublicAPI
    public ParagraphBuilder withInlineElements(List<InlineElement> inlineElements) {
        if (inlineElements == null) {
            throw new IllegalArgumentException("Inline elements list cannot be null");
        }
        this.inlineElements.addAll(inlineElements);
        return this;
    }

    /**
     * Sets the style class to be used for regular text.
     * This style class will be applied to all text added via {@link #addInlineText(String)}.
     * @param styleClassRegular the style class for regular text (e.g., "regular-text");
     *                          must not be {@code null} or empty
     * @return this builder instance for method chaining
     */
    @PublicAPI
    public ParagraphBuilder withStyleClassRegular(String styleClassRegular) {
        this.styleClassRegular = styleClassRegular;
        return this;
    }

    /**
     * Sets the style class to be used for bold text.
     * This style class will be applied to all text added via {@link #addInlineTextBold(String)}.
     *
     *
     * @param styleClassBold the style class for bold text (e.g., "bold-text");
     *                       must not be {@code null} or empty
     * @return this builder instance for method chaining
     */
    @PublicAPI
    public ParagraphBuilder withStyleClassBold(String styleClassBold) {
        this.styleClassBold = styleClassBold;
        return this;
    }

    public ParagraphBuilder withStyleClassItalic(String italicStyle) {

        this.styleClassItalic = italicStyle;
        return this;
    }

    public ParagraphBuilder withStyleClassHyperlink(String hyperlinkStyle) {
        this.styleClassHyperlink = hyperlinkStyle;
        return this;
    }

    /**
    /**
     * Adds a custom inline element to the paragraph.
     * Use this when you need to add elements other than simple text runs,
     * such as links or other specialized inline elements.
     *
     * @param inlineElement the inline element to add; must not be {@code null}
     * @return this builder instance for method chaining
     * @throws IllegalArgumentException if inlineElement is {@code null}
     */
    @PublicAPI
    public ParagraphBuilder addInlineElement(InlineElement inlineElement) {
        if (inlineElement == null) {
            throw new IllegalArgumentException("Inline element cannot be null");
        }
        this.inlineElements.add(inlineElement);
        return this;
    }

    /**
     * Adds inline text with the paragraph's default style class.
     * The text will be rendered using the style class specified in the builder constructor.
     *
     * @param inlineText the text to add; must not be {@code null}
     * @return this builder instance for method chaining
     * @throws IllegalArgumentException if inlineText is {@code null}
     */
    @PublicAPI
    public ParagraphBuilder addInlineText(String inlineText) {
        if (inlineText == null) {
            throw new IllegalArgumentException("Inline text cannot be null");
        }
        InlineElement inlineElement = new TextRun(inlineText, styleClassRegular);
        this.inlineElements.add(inlineElement);
        return this;
    }


    /**
     * Adds inline text with bold styling.
     * The text will be rendered using the bold style class set via
     * {@link #withStyleClassBold(String)}. If no bold style class has been set,
     * a warning is logged and the default style class is used instead.
     *
     * @param inlineText the text to add; must not be {@code null}
     * @return this builder instance for method chaining
     * @throws IllegalArgumentException if inlineText is {@code null}
     */
    @PublicAPI
    public ParagraphBuilder addInlineTextBold(String inlineText) {
        log.debug("Adding inline text bold for inlineText {}. Current style class bold ist {}", inlineText,styleClassBold);
        if (inlineText == null) {
            log.error("Inline text is null");
            throw new IllegalArgumentException("Inline text cannot be null");
        }

        InlineElement inlineElement;
        if (styleClassBold == null) {
            log.warn("No style class for bold defined: using normal style class for text '{}'",
                    inlineText);
            inlineElement = new TextRun(inlineText, styleClass);
        } else {
            inlineElement = new TextRun(inlineText, styleClassBold);
        }
        this.inlineElements.add(inlineElement);
        return this;
    }


    /**
     * Adds inline text with italic styling.
     * The text will be rendered using the italic style class set via
     * {@link #withStyleClassItalic(String)}. If no italic style class has been set,
     * a warning is logged and the default style class is used instead.
     *
     * @param inlineText the text to add; must not be {@code null}
     * @return this builder instance for method chaining
     * @throws IllegalArgumentException if inlineText is {@code null}
     */
    @PublicAPI
    public ParagraphBuilder addInlineTextItalic(String inlineText) {
        if (inlineText == null) {
            throw new IllegalArgumentException("Inline text cannot be null");
        }

        InlineElement inlineElement;
        if (styleClassItalic == null) {
            log.warn("No style class for italic defined: using normal style class for text '{}'",
                    inlineText);
            inlineElement = new TextRun(inlineText, styleClass);
        } else {
            inlineElement = new TextRun(inlineText, styleClassItalic);
        }

        this.inlineElements.add(inlineElement);
        return this;
    }

    /**
     * Adds a hyperlink inline element to the paragraph with alternative text.
     * The hyperlink will be rendered using the hyperlink style class set via
     * {@link #withStyleClassHyperlink(String)}. If no hyperlink style class has been set,
     * a warning is logged and the default style class is used instead.
     * @param text the text to display for the hyperlink
     * @param altText the alternative text for the hyperlink
     * @param href the href (url); must not be {@code null}
     * @return this builder instance for method chaining
     */
    @PublicAPI
    public ParagraphBuilder addHyperlinkWithAltText(String text,String altText,String href){

        Hyperlink hyperlink;

        if(styleClassHyperlink == null){
            log.warn("No style class for hyperlink with alt text defined. Set normal style class for hyperlink with text {} and href {}",text,href);
            hyperlink = new Hyperlink(text,styleClass,href,altText);
        }else {
            hyperlink = new Hyperlink(text,styleClassHyperlink,href,altText);
        }
        this.inlineElements.add(hyperlink);
        return this;
    }

    /**
     * Adds a hyperlink inline element to the paragraph.
     * The hyperlink will be rendered using the hyperlink style class set via
     * {@link #withStyleClassHyperlink(String)}. If no hyperlink style class has been set,
     * a warning is logged and the default style class is used instead.
     * @param text the text to display for the hyperlink
     * @param href  the href (url); must not be {@code null}
     * @return this builder instance for method chaining
     */
    @PublicAPI
    public ParagraphBuilder addHyperlink(String text,String href){

        Hyperlink hyperlink;

        if(styleClassHyperlink == null){
            log.warn("No style class for hyperlink defined. Set normal style class for hyperlink with text {} and href {}",text,href);
            hyperlink = new Hyperlink(text,styleClass,href);
        }else {
            hyperlink = new Hyperlink(text,styleClassHyperlink,href);
        }
        this.inlineElements.add(hyperlink);
        return this;
    }

    /**
     * Builds and returns the final {@link Paragraph} instance.
     * This method creates the paragraph with all inline elements that have been added
     * through the builder's methods.
     *
     * <p><b>Note:</b> After calling this method, the builder can still be used to create
     * additional paragraphs, but they will share the same inline elements list (mutable).
     * For independent paragraphs, create a new builder instance.</p>
     *
     * @return a new Paragraph instance with the configured properties
     */
    public Paragraph build() {
        return new Paragraph(styleClass, inlineElements);
    }

}
