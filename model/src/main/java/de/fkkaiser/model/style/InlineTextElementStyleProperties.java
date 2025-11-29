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
package de.fkkaiser.model.style;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Abstract base class for style properties of inline text elements.
 * This class provides formatting properties for text that appears within the normal
 * flow of a paragraph, such as emphasized words, hyperlinks, or specially formatted phrases.
 *
 * <p><b>Purpose:</b></p>
 * Inline text elements are portions of text within a larger text block (like a paragraph)
 * that have different formatting from the surrounding text. Unlike block elements, they
 * don't create line breaks and flow naturally with the surrounding text.
 *
 * <p><b>Common Inline Text Elements:</b></p>
 * <ul>
 *   <li><b>Text runs:</b> Basic inline text with specific formatting</li>
 *   <li><b>Hyperlinks:</b> Clickable text with special styling</li>
 *   <li><b>Emphasized text:</b> Bold, italic, or underlined text</li>
 *   <li><b>Code snippets:</b> Inline code with monospace font</li>
 *   <li><b>Superscript/Subscript:</b> Mathematical or chemical notation</li>
 * </ul>
 *
 * <p><b>XSL-FO Mapping:</b></p>
 * This class maps to properties of XSL-FO's {@code <fo:inline>} element:
 * <ul>
 *   <li>{@link #textStyleName} → Reference to a {@link TextStyle} definition</li>
 *   <li>{@link #textDecoration} → {@code text-decoration}: Underline, line-through, etc.</li>
 *   <li>{@link #textColor} → {@code color}: Text color</li>
 *   <li>{@link #lineFeedTreatment} → {@code linefeed-treatment}: How line breaks are handled</li>
 * </ul>
 *
 * <p><b>Style Inheritance:</b></p>
 * Inline text elements can inherit properties from their containing block element
 * (e.g., a paragraph). The {@link #mergeWith(ElementStyleProperties)} method implements
 * this inheritance by filling in missing properties from the parent block style.
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * // Create a text style for bold text
 * TextStyle boldStyle = TextStyle.builder()
 *     .withName("bold-text")
 *     .withFontWeight("bold")
 *     .build();
 *
 * // Create inline text properties that reference this style
 * TextRunStyleProperties inlineProps = new TextRunStyleProperties();
 * inlineProps.setTextStyleName("bold-text");
 * inlineProps.setTextColor("#FF0000");
 * inlineProps.setTextDecoration("underline");
 *
 * // Apply within a paragraph
 * Paragraph para = new Paragraph();
 * para.addText("This is normal text with ");
 * para.addInlineText("important emphasized text", inlineProps);
 * para.addText(" continuing normally.");
 * }</pre>
 *
 * <p><b>JSON Representation:</b></p>
 * <pre>{@code
 * {
 *   "text-style-name": "bold-text",
 *   "text-decoration": "underline",
 *   "text-color": "#FF0000",
 *   "linefeed-treatment": "preserve"
 * }
 * }</pre>
 *
 * @author FK Kaiser
 * @version 1.0
 * @see InlineElementStyleProperties
 * @see TextStyle
 * @see TextBlockStyleProperties
 * @since 1.0
 */
public abstract class InlineTextElementStyleProperties extends InlineElementStyleProperties {

    private static final Logger logger = LoggerFactory.getLogger(InlineTextElementStyleProperties.class);

    /**
     * Reference to a named {@link TextStyle} that defines the font properties
     * for this inline text element.
     *
     * <p><b>Purpose:</b></p>
     * Instead of defining font family, size, weight, and style directly on each
     * inline element, this property references a predefined TextStyle by name.
     * This promotes consistency and makes global style changes easier.
     *
     * <p><b>Behavior:</b></p>
     * The referenced TextStyle must exist in the document's {@link StyleSheet}.
     * During PDF generation, the FOP processor will look up this name and apply
     * all font properties defined in that TextStyle.
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * // Define a TextStyle in the stylesheet
     * TextStyle codeStyle = new TextStyle("code-font", "Courier New", "10pt");
     *
     * // Reference it in inline properties
     * inlineProps.setTextStyleName("code-font");
     * }</pre>
     *
     * <p><b>Inheritance:</b></p>
     * If not explicitly set, this property can be inherited from the containing
     * block element (e.g., paragraph) via {@link #mergeWith(ElementStyleProperties)}.
     *
     * @see #setTextStyleName(String)
     * @see TextStyle
     */
    @JsonProperty("text-style-name")
    private String textStyleName;

    /**
     * Text decoration applied to this inline text element.
     *
     * <p><b>Valid Values:</b></p>
     * <ul>
     *   <li><b>{@code "none"}:</b> No decoration (default)</li>
     *   <li><b>{@code "underline"}:</b> Draws a line under the text</li>
     *   <li><b>{@code "overline"}:</b> Draws a line above the text</li>
     *   <li><b>{@code "line-through"}:</b> Draws a line through the text (strikethrough)</li>
     *   <li><b>Multiple values:</b> {@code "underline overline"} (space-separated)</li>
     *   <li><b>{@code "blink"}:</b> Causes text to blink (rarely supported)</li>
     * </ul>
     *
     * <p><b>Common Use Cases:</b></p>
     * <ul>
     *   <li><b>Hyperlinks:</b> Typically use {@code "underline"}</li>
     *   <li><b>Deleted text:</b> Use {@code "line-through"}</li>
     *   <li><b>Emphasized text:</b> May use {@code "underline"} or none (rely on bold/italic)</li>
     *   <li><b>Abbreviations:</b> Sometimes use {@code "underline"} with dotted style</li>
     * </ul>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * // Underlined link
     * linkProps.setTextDecoration("underline");
     *
     * // Strikethrough for deleted text
     * deletedProps.setTextDecoration("line-through");
     *
     * // Multiple decorations
     * specialProps.setTextDecoration("underline overline");
     * }</pre>
     *
     * <p><b>XSL-FO Mapping:</b></p>
     * Maps directly to the XSL-FO {@code text-decoration} property.
     *
     * @see #setTextDecoration(String)
     */
    @JsonProperty("text-decoration")
    private String textDecoration;

    /**
     * The color of the text for this inline element.
     * This property overrides any color specified in the referenced {@link #textStyleName}.
     *
     * <p><b>Valid Color Formats:</b></p>
     * <ul>
     *   <li><b>Hex code:</b> {@code "#FF0000"} (red), {@code "#00FF00"} (green)</li>
     *   <li><b>RGB function:</b> {@code "rgb(255, 0, 0)"} (red)</li>
     *   <li><b>Color name:</b> {@code "red"}, {@code "blue"}, {@code "green"}, etc.</li>
     *   <li><b>CMYK (FOP extension):</b> {@code "cmyk(0, 1, 1, 0)"}</li>
     * </ul>
     *
     * <p><b>Common Use Cases:</b></p>
     * <ul>
     *   <li><b>Hyperlinks:</b> Often blue ({@code "#0000FF"}) or custom brand color</li>
     *   <li><b>Error messages:</b> Red ({@code "#FF0000"}) or warning orange</li>
     *   <li><b>Success indicators:</b> Green ({@code "#00FF00"})</li>
     *   <li><b>Code syntax highlighting:</b> Different colors for keywords, strings, etc.</li>
     * </ul>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * // Blue link
     * linkProps.setTextColor("#0000FF");
     *
     * // Red error text
     * errorProps.setTextColor("rgb(255, 0, 0)");
     *
     * // Green success message
     * successProps.setTextColor("green");
     * }</pre>
     *
     * <p><b>Inheritance:</b></p>
     * If not explicitly set, this property can be inherited from the containing
     * block element via {@link #mergeWith(ElementStyleProperties)}.
     *
     * <p><b>XSL-FO Mapping:</b></p>
     * Maps directly to the XSL-FO {@code color} property.
     *
     * @see #setTextColor(String)
     */
    @JsonProperty("text-color")
    private String textColor;

    /**
     * Defines how line feed characters ({@code \n}, {@code \r}) are treated
     * within this inline text element.
     *
     * <p><b>Valid Values:</b></p>
     * <ul>
     *   <li><b>{@code "ignore"}:</b> Line feeds are removed/ignored (default in most contexts)</li>
     *   <li><b>{@code "preserve"}:</b> Line feeds are preserved and rendered as line breaks</li>
     *   <li><b>{@code "treat-as-space"}:</b> Line feeds are converted to spaces</li>
     *   <li><b>{@code "treat-as-zero-width-space"}:</b> Line feeds become zero-width spaces (allows breaks)</li>
     * </ul>
     *
     * <p><b>Common Use Cases:</b></p>
     * <ul>
     *   <li><b>Preformatted text:</b> Use {@code "preserve"} to maintain original line breaks</li>
     *   <li><b>Code blocks:</b> Use {@code "preserve"} to show code exactly as written</li>
     *   <li><b>Poetry/verse:</b> Use {@code "preserve"} to maintain line structure</li>
     *   <li><b>Normal text:</b> Use {@code "treat-as-space"} (default behavior)</li>
     * </ul>
     *
     * <p><b>Example:</b></p>
     * <pre>{@code
     * // Preserve line breaks in code
     * codeProps.setLineFeedTreatment("preserve");
     *
     * // Convert line breaks to spaces in normal text
     * normalProps.setLineFeedTreatment("treat-as-space");
     * }</pre>
     *
     * <p><b>Relationship to Content:</b></p>
     * When combined with the content transformation in your PDF generator
     * (e.g., converting {@code \n} to Unicode line separator {@code \u2028}),
     * this property controls how those characters are rendered.
     *
     * <p><b>Inheritance:</b></p>
     * If not explicitly set, this property can be inherited from the containing
     * block element via {@link #mergeWith(ElementStyleProperties)}.
     *
     * <p><b>XSL-FO Mapping:</b></p>
     * Maps directly to the XSL-FO {@code linefeed-treatment} property.
     *
     * @see #setLineFeedTreatment(String)
     */
    @JsonProperty("linefeed-treatment")
    private String lineFeedTreatment;

    /**
     * Merges properties from a base style into this style.
     * This method implements property inheritance from parent block elements.
     *
     * <p><b>Inheritance Logic:</b></p>
     * Properties that are {@code null} in this inline style are filled in with
     * values from the parent block style (if it's a {@link TextBlockStyleProperties}).
     * This allows inline elements to inherit text styling from their container.
     *
     * <p><b>Inherited Properties:</b></p>
     * <ul>
     *   <li>{@link #textStyleName} - Inherits font definition</li>
     *   <li>{@link #textColor} - Inherits text color</li>
     *   <li>{@link #lineFeedTreatment} - Inherits line break handling</li>
     * </ul>
     *
     * <p><b>Example Scenario:</b></p>
     * <pre>{@code
     * // Paragraph style defines default text properties
     * ParagraphStyleProperties paraProps = new ParagraphStyleProperties();
     * paraProps.setTextStyleName("body-text");
     * paraProps.setTextColor("#000000");
     *
     * // Inline element inherits color but overrides text style
     * InlineTextElementStyleProperties inlineProps = new TextRunStyleProperties();
     * inlineProps.setTextStyleName("bold-text"); // Override
     * // textColor is null, will be inherited from paragraph
     *
     * inlineProps.mergeWith(paraProps);
     * // Result: textStyleName = "bold-text", textColor = "#000000"
     * }</pre>
     *
     * @param elemBase the base style to inherit from; only {@link TextBlockStyleProperties}
     *                 instances are processed for property inheritance
     */
    @Override
    public void mergeWith(ElementStyleProperties elemBase) {
        if (elemBase instanceof TextBlockStyleProperties textBase) {
            logger.debug("Merging inline text properties with block style. Current textStyleName: {}", textStyleName);
            this.textStyleName = Optional.ofNullable(this.textStyleName).orElse(textBase.getTextStyleName());
            this.textColor = Optional.ofNullable(this.textColor).orElse(textBase.getTextColor());
            this.lineFeedTreatment = Optional.ofNullable(this.lineFeedTreatment).orElse(textBase.getLinefeedTreatment());
        }
    }

    /**
     * Creates a deep copy of this style properties object.
     * All properties are copied to a new instance.
     *
     * <p><b>Note:</b></p>
     * This implementation returns an {@link InlineElementStyleProperties} instance.
     * Subclasses should override this method to return their specific type.
     *
     * @return a new style properties instance with identical values
     */
    @Override
    public ElementStyleProperties copy() {
        InlineElementStyleProperties copy = new InlineElementStyleProperties();
        applyPropertiesTo(copy);
        return copy;
    }

    /**
     * Applies all properties from this style to a target style object.
     * This is used internally by {@link #copy()} and for style inheritance mechanisms.
     *
     * <p><b>Type Safety:</b></p>
     * Properties are only applied if the target is an instance of
     * {@link InlineTextElementStyleProperties} to ensure type compatibility.
     *
     * @param target the target style to apply properties to; if not an
     *               {@link InlineTextElementStyleProperties}, only parent properties are applied
     */
    @Override
    protected void applyPropertiesTo(InlineElementStyleProperties target) {
        super.applyPropertiesTo(target);
        if (target instanceof InlineTextElementStyleProperties textTarget) {
            textTarget.textStyleName = this.textStyleName;
            textTarget.textDecoration = this.textDecoration;
            textTarget.textColor = this.textColor;
            textTarget.lineFeedTreatment = this.lineFeedTreatment;
        }
    }

    // --- Getter & Setter ---

    /**
     * Gets the name of the referenced text style.
     *
     * @return the text style name, or {@code null} if not set
     * @see #textStyleName
     */
    public String getTextStyleName() {
        return textStyleName;
    }

    /**
     * Gets the text decoration specification.
     *
     * @return the text decoration (e.g., {@code "underline"}, {@code "line-through"}),
     *         or {@code null} if not set
     * @see #textDecoration
     */
    public String getTextDecoration() {
        return textDecoration;
    }

    /**
     * Gets the text color.
     *
     * @return the text color (e.g., {@code "#FF0000"}, {@code "red"}),
     *         or {@code null} if not set
     * @see #textColor
     */
    public String getTextColor() {
        return textColor;
    }

    /**
     * Sets the name of the text style to apply to this inline element.
     *
     * @param fontStyleName the text style name; may be {@code null} to unset or inherit
     * @see #textStyleName
     */
    public void setTextStyleName(String fontStyleName) {
        this.textStyleName = fontStyleName;
    }

    /**
     * Sets the text decoration for this inline element.
     *
     * @param textDecoration the decoration to apply (e.g., {@code "underline"}, {@code "line-through"});
     *                       may be {@code null} to use default (no decoration)
     * @see #textDecoration
     */
    public void setTextDecoration(String textDecoration) {
        this.textDecoration = textDecoration;
    }

    /**
     * Sets the text color for this inline element.
     *
     * @param textColor the color to apply (e.g., {@code "#FF0000"}, {@code "rgb(255,0,0)"});
     *                  may be {@code null} to inherit from parent block
     * @see #textColor
     */
    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    /**
     * Gets the line feed treatment specification.
     *
     * @return the line feed treatment (e.g., {@code "preserve"}, {@code "ignore"}),
     *         or {@code null} if not set
     * @see #lineFeedTreatment
     */
    public String getLineFeedTreatment() {
        return lineFeedTreatment;
    }

    /**
     * Sets how line feed characters are handled in this inline element.
     *
     * @param lineFeedTreatment the treatment mode (e.g., {@code "preserve"}, {@code "treat-as-space"});
     *                          may be {@code null} to use default behavior
     * @see #lineFeedTreatment
     */
    public void setLineFeedTreatment(String lineFeedTreatment) {
        this.lineFeedTreatment = lineFeedTreatment;
    }
}