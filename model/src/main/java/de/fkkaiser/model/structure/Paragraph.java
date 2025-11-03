package de.fkkaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a paragraph element in a document structure.
 * A paragraph is a block-level element that contains inline elements such as text runs,
 * formatted text, links, or other inline content. Paragraphs are the fundamental building
 * blocks for most text-based document content.
 *
 * <p><b>Structure:</b></p>
 * A paragraph consists of:
 * <ul>
 *   <li><b>Style Class:</b> A reference to an {@link de.fkkaiser.model.style.ElementStyle}
 *       that defines the paragraph's formatting (margins, alignment, text style, etc.)</li>
 *   <li><b>Inline Elements:</b> A list of {@link InlineElement}s that form the paragraph's
 *       content (typically {@link TextRun} instances)</li>
 *   <li><b>Variant:</b> An optional semantic variant (e.g., "warning", "note") that can
 *       modify the paragraph's appearance or behavior</li>
 * </ul>
 *
 * <p><b>JSON Representation:</b></p>
 * <pre>{@code
 * {
 *   "type": "paragraph",
 *   "style-class": "body-paragraph",
 *   "variant": "note",
 *   "inline-elements": [
 *     {
 *       "type": "text-run",
 *       "text": "This is a paragraph with ",
 *       "style-class": "normal-text"
 *     },
 *     {
 *       "type": "text-run",
 *       "text": "bold text",
 *       "style-class": "bold-text"
 *     }
 *   ]
 * }
 * }</pre>
 *
 * <p><b>Usage Example 1 - Simple Paragraph:</b></p>
 * <pre>{@code
 * // Single line of text
 * Paragraph simple = new Paragraph("body-paragraph", "This is simple text");
 *
 * // With variant
 * Paragraph note = new Paragraph("body-paragraph", "Important note", "warning");
 * }</pre>
 *
 * <p><b>Usage Example 2 - Paragraph with Formatted Text:</b></p>
 * <pre>{@code
 * // Create inline elements
 * TextRun normal = new TextRun("This is normal text, ", "normal-text");
 * TextRun bold = new TextRun("this is bold", "bold-text");
 * TextRun moreNormal = new TextRun(", and this is normal again.", "normal-text");
 *
 * // Create paragraph with multiple inline elements
 * Paragraph formatted = new Paragraph(
 *     "body-paragraph",
 *     List.of(normal, bold, moreNormal)
 * );
 * }</pre>
 *
 * <p><b>Usage Example 3 - Using Builder (Recommended for Complex Content):</b></p>
 * <pre>{@code
 * // Create a paragraph with mixed formatting using the builder
 * Paragraph complex = Paragraph.builder("body-paragraph")
 *     .withStyleClassBold("bold-text")
 *     .withStyleClassItalic("italic-text")
 *     .addInlineText("This is normal text, ")
 *     .addInlineTextBold("this is bold")
 *     .addInlineText(", ")
 *     .addInlineTextItalic("this is italic")
 *     .addInlineText(", and this is normal again.")
 *     .build();
 * }</pre>
 *
 * <p><b>Usage Example 4 - Builder with Variants:</b></p>
 * <pre>{@code
 * Paragraph highlighted = Paragraph.builder("body-paragraph")
 *     .withStyleClassBold("bold-text")
 *     .addInlineText("Normal text with ")
 *     .addInlineTextBoldWithVariant("important bold text", "highlight")
 *     .addInlineText(".")
 *     .withVariant("note")
 *     .build();
 * }</pre>
 *
 * <p><b>Common Use Cases:</b></p>
 * <ul>
 *   <li><b>Body Text:</b> Main content paragraphs in documents</li>
 *   <li><b>Formatted Content:</b> Paragraphs with bold, italic, or other inline formatting</li>
 *   <li><b>Special Paragraphs:</b> Notes, warnings, callouts using variants</li>
 *   <li><b>Mixed Content:</b> Paragraphs containing text, links, and other inline elements</li>
 * </ul>
 *
 * <p><b>Inheritance:</b></p>
 * This class extends {@link TextBlock}, which provides common functionality for all
 * block-level text elements (paragraphs, headlines, list items, etc.).
 *
 * @author FK Kaiser
 * @version 1.1
 * @see TextBlock
 * @see InlineElement
 * @see TextRun
 * @see ParagraphBuilder
 */
@JsonTypeName(ElementTypes.PARAGRAPH)
public class Paragraph extends TextBlock {

    private static final Logger log = LoggerFactory.getLogger(Paragraph.class);

    // ==================== Constructors ====================

    /**
     * Primary constructor used by Jackson for JSON deserialization.
     * This constructor initializes all properties, including those inherited from parent classes.
     *
     * <p>This constructor is typically used internally by the JSON deserialization process.
     * For programmatic creation, consider using one of the convenience constructors or
     * the {@link ParagraphBuilder} for complex paragraphs.</p>
     *
     * @param styleClass     the CSS-like class name for styling (e.g., "body-paragraph");
     *                       references an {@link de.fkkaiser.model.style.ElementStyle};
     *                       must not be {@code null}
     * @param inlineElements the list of inline elements forming the paragraph's content;
     *                       must not be {@code null} but may be empty
     * @param variant        an optional semantic variant (e.g., "warning", "note");
     *                       may be {@code null}
     */
    public Paragraph(
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("inline-elements") List<InlineElement> inlineElements,
            @JsonProperty("variant") String variant) {
        super(styleClass, inlineElements, variant);
    }

    /**
     * Creates a paragraph with a single text run containing the specified text.
     * This is a convenience constructor for simple paragraphs with plain text and a variant.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * Paragraph note = new Paragraph("body-paragraph", "This is a note", "warning");
     * }</pre>
     *
     * @param styleClass     the CSS-like class name for styling;
     *                       must not be {@code null}
     * @param standAloneText the text content of the paragraph;
     *                       must not be {@code null}
     * @param variant        an optional semantic variant;
     *                       may be {@code null}
     */
    @SuppressWarnings("unused")
    public Paragraph(String styleClass, String standAloneText, String variant) {
        super(styleClass, List.of(new TextRun(standAloneText, null , variant)), variant);
    }

    /**
     * Creates a paragraph with a single text run containing the specified text.
     * This is the simplest constructor for creating plain text paragraphs without variants.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * Paragraph simple = new Paragraph("body-paragraph", "This is simple text");
     * }</pre>
     *
     * @param styleClass     the CSS-like class name for styling;
     *                       must not be {@code null}
     * @param standAloneText the text content of the paragraph;
     *                       must not be {@code null}
     */
    @SuppressWarnings("unused")
    public Paragraph(String styleClass, String standAloneText) {
        super(styleClass, List.of(new TextRun(standAloneText, null, null)));
    }

    /**
     * Creates a paragraph with a single inline element and an optional variant.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * TextRun textRun = new TextRun("Formatted text", "bold-text");
     * Paragraph p = new Paragraph("body-paragraph", textRun, "highlight");
     * }</pre>
     *
     * @param styleClass    the CSS-like class name for styling;
     *                      must not be {@code null}
     * @param inlineElement the single inline element to include;
     *                      must not be {@code null}
     * @param variant       an optional semantic variant;
     *                      may be {@code null}
     */
    @SuppressWarnings("unused")
    public Paragraph(String styleClass, InlineElement inlineElement, String variant) {
        super(styleClass, List.of(inlineElement), variant);
    }

    /**
     * Creates a paragraph with a single inline element.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * TextRun textRun = new TextRun("Formatted text", "bold-text");
     * Paragraph p = new Paragraph("body-paragraph", textRun);
     * }</pre>
     *
     * @param styleClass    the CSS-like class name for styling;
     *                      must not be {@code null}
     * @param inlineElement the single inline element to include;
     *                      must not be {@code null}
     */
    @SuppressWarnings("unused")
    public Paragraph(String styleClass, InlineElement inlineElement) {
        super(styleClass, List.of(inlineElement));
    }

    /**
     * Creates a paragraph with multiple inline elements.
     * Use this constructor when you have pre-built inline elements with different formatting.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * List<InlineElement> elements = List.of(
     *     new TextRun("Normal ", "normal-text"),
     *     new TextRun("bold", "bold-text"),
     *     new TextRun(" text", "normal-text")
     * );
     * Paragraph p = new Paragraph("body-paragraph", elements);
     * }</pre>
     *
     * @param styleClass     the CSS-like class name for styling;
     *                       must not be {@code null}
     * @param inlineElements the list of inline elements;
     *                       must not be {@code null} but may be empty
     */
    public Paragraph(String styleClass, List<InlineElement> inlineElements) {
        super(styleClass, inlineElements);
    }

    /**
     * Creates an empty paragraph with only a style class.
     * Inline elements can be added later if needed.
     *
     * <p><b>Note:</b> Paragraphs without content may not render properly in the final PDF.
     * This constructor is primarily for cases where content will be added programmatically.</p>
     *
     * @param styleClass the CSS-like class name for styling;
     *                   must not be {@code null}
     */
    public Paragraph(String styleClass) {
        super(styleClass);
    }

    // ==================== Overrides ====================

    /**
     * Returns the type identifier for this element.
     * This is used for JSON serialization and element type identification.
     *
     * @return the string constant {@link ElementTypes#PARAGRAPH}
     */
    @Override
    public String getType() {
        return ElementTypes.PARAGRAPH;
    }

    // ==================== Builder Factory ====================

    /**
     * Creates and returns a new {@link ParagraphBuilder} for constructing complex paragraphs.
     * The builder provides a fluent API for adding inline elements with different formatting
     * (normal, bold, italic) without manually creating {@link TextRun} instances.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * Paragraph p = Paragraph.builder("body-paragraph")
     *     .withStyleClassBold("bold-text")
     *     .withStyleClassItalic("italic-text")
     *     .addInlineText("This is normal, ")
     *     .addInlineTextBold("this is bold")
     *     .addInlineText(", and ")
     *     .addInlineTextItalic("this is italic")
     *     .addInlineText(".")
     *     .build();
     * }</pre>
     *
     * @param styleClass the CSS-like class name for the paragraph's default styling;
     *                   must not be {@code null} or empty
     * @return a new ParagraphBuilder instance
     * @throws IllegalArgumentException if styleClass is {@code null} or empty
     */
    public static ParagraphBuilder builder(String styleClass) {
        return new ParagraphBuilder(styleClass);
    }

    // ==================== Builder Class ====================

    /**
     * Fluent builder for creating {@link Paragraph} instances with complex inline content.
     * This builder simplifies the creation of paragraphs containing text with mixed formatting
     * (normal, bold, italic) by automatically creating the appropriate {@link TextRun} instances.
     *
     * <p><b>Features:</b></p>
     * <ul>
     *   <li>Automatic creation of {@link TextRun} instances for text segments</li>
     *   <li>Support for normal, bold, and italic text styles</li>
     *   <li>Support for variants on individual text segments</li>
     *   <li>Ability to add custom {@link InlineElement} instances</li>
     *   <li>Fluent API for readable paragraph construction</li>
     * </ul>
     *
     * <p><b>Basic Usage:</b></p>
     * <pre>{@code
     * Paragraph p = Paragraph.builder("body-paragraph")
     *     .addInlineText("Normal text")
     *     .build();
     * }</pre>
     *
     * <p><b>Advanced Usage with Mixed Formatting:</b></p>
     * <pre>{@code
     * Paragraph p = Paragraph.builder("body-paragraph")
     *     .withStyleClassBold("bold-text")
     *     .withStyleClassItalic("italic-text")
     *     .addInlineText("This paragraph contains ")
     *     .addInlineTextBold("bold")
     *     .addInlineText(", ")
     *     .addInlineTextItalic("italic")
     *     .addInlineText(", and ")
     *     .addInlineTextBoldWithVariant("highlighted bold", "highlight")
     *     .addInlineText(" text.")
     *     .withVariant("note")
     *     .build();
     * }</pre>
     *
     * <p><b>Thread Safety:</b></p>
     * This builder is not thread-safe. Each builder instance should be used by a single thread.
     *
     * @see Paragraph
     * @see TextRun
     * @see InlineElement
     */
    public static class ParagraphBuilder {

        private final String styleClass;
        private String styleClassRegular;
        private String styleClassBold;
        private String styleClassItalic;
        private String styleClassHyperlink;
        private String variant;
        private final List<InlineElement> inlineElements;

        /**
         * Package-private constructor. Use {@link Paragraph#builder(String)} to create instances.
         *
         * @param styleClass the default style class for the paragraph
         * @throws IllegalArgumentException if styleClass is {@code null} or empty
         */
        private ParagraphBuilder(String styleClass) {
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
        @SuppressWarnings("unused")
        public ParagraphBuilder withInlineElements(List<InlineElement> inlineElements) {
            if (inlineElements == null) {
                throw new IllegalArgumentException("Inline elements list cannot be null");
            }
            this.inlineElements.addAll(inlineElements);
            return this;
        }

        /**
         * Sets the style class to be used for regular text.
         * This style class will be applied to all text added via {@link #addInlineText(String)}
         * and {@link #addInlineTextWithVariant(String, String)}.
         *
         * @param styleClassRegular the style class for regular text (e.g., "regular-text");
         *                       must not be {@code null} or empty
         * @return this builder instance for method chaining
         */
        @SuppressWarnings("unused")
        public ParagraphBuilder withStyleClassRegular(String styleClassRegular) {
            this.styleClassRegular = styleClassRegular;
            return this;
        }

        /**
         * Sets the style class to be used for bold text.
         * This style class will be applied to all text added via {@link #addInlineTextBold(String)}
         * and {@link #addInlineTextBoldWithVariant(String, String)}.
         *
         * @param styleClassBold the style class for bold text (e.g., "bold-text");
         *                       must not be {@code null} or empty
         * @return this builder instance for method chaining
         */
        @SuppressWarnings("unused")
        public ParagraphBuilder withStyleClassBold(String styleClassBold) {
            this.styleClassBold = styleClassBold;
            return this;
        }

        /**
         * Sets the style class to be used for italic text.
         * This style class will be applied to all text added via {@link #addInlineTextItalic(String)}
         * and {@link #addInlineTextItalicWithVariant(String, String)}.
         *
         * @param styleClassItalic the style class for italic text (e.g., "italic-text");
         *                         must not be {@code null} or empty
         * @return this builder instance for method chaining
         */
        @SuppressWarnings("unused")
        public ParagraphBuilder withStyleClassItalic(String styleClassItalic) {
            this.styleClassItalic = styleClassItalic;
            return this;
        }

        /**
         * Sets the style class to be used for a hyperlink.
         * This style class will be applied to all text added via {@link #addHyperlink(String, String)}
         *
         * @param styleClassHyperlink the style class for hyperlink (e.g., "hyperlink-text");
         *                         must not be {@code null} or empty
         * @return this builder instance for method chaining
         */
        @SuppressWarnings("unused")
        public ParagraphBuilder withStyleClassHyperlink(String styleClassHyperlink) {
            this.styleClassHyperlink = styleClassHyperlink;
            return this;
        }



        /**
         * Sets the variant for the entire paragraph.
         * Variants are semantic modifiers that can affect the paragraph's appearance
         * or behavior (e.g., "warning", "note", "highlight").
         *
         * @param variant the variant identifier; may be {@code null}
         * @return this builder instance for method chaining
         */
        @SuppressWarnings("unused")
        public ParagraphBuilder withVariant(String variant) {
            this.variant = variant;
            return this;
        }

        /**
         * Adds a custom inline element to the paragraph.
         * Use this when you need to add elements other than simple text runs,
         * such as links or other specialized inline elements.
         *
         * @param inlineElement the inline element to add; must not be {@code null}
         * @return this builder instance for method chaining
         * @throws IllegalArgumentException if inlineElement is {@code null}
         */
        @SuppressWarnings("unused")
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
        @SuppressWarnings("unused")
        public ParagraphBuilder addInlineText(String inlineText) {
            if (inlineText == null) {
                throw new IllegalArgumentException("Inline text cannot be null");
            }
            InlineElement inlineElement = new TextRun(inlineText, styleClassRegular,null);
            this.inlineElements.add(inlineElement);
            return this;
        }

        /**
         * Adds inline text with the default style class and a specific variant.
         *
         * @param inlineText the text to add; must not be {@code null}
         * @param variant    the variant for this text segment; may be {@code null}
         * @return this builder instance for method chaining
         * @throws IllegalArgumentException if inlineText is {@code null}
         */
        @SuppressWarnings("unused")
        public ParagraphBuilder addInlineTextWithVariant(String inlineText, String variant) {
            if (inlineText == null) {
                throw new IllegalArgumentException("Inline text cannot be null");
            }
            InlineElement inlineElement = new TextRun(inlineText, styleClass, variant);
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
        @SuppressWarnings("unused")
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
                inlineElement = new TextRun(inlineText, styleClass,null);
            } else {
                inlineElement = new TextRun(inlineText, styleClassBold,null);
            }
            this.inlineElements.add(inlineElement);
            return this;
        }

        /**
         * Adds inline text with bold styling and a specific variant.
         *
         * @param inlineText the text to add; must not be {@code null}
         * @param variant    the variant for this text segment; may be {@code null}
         * @return this builder instance for method chaining
         * @throws IllegalArgumentException if inlineText is {@code null}
         */
        @SuppressWarnings("unused")
        public ParagraphBuilder addInlineTextBoldWithVariant(String inlineText, String variant) {
            if (inlineText == null) {
                throw new IllegalArgumentException("Inline text cannot be null");
            }

            InlineElement inlineElement;
            if (styleClassBold == null) {
                log.warn("No style class for bold defined: using normal style class for text '{}' with variant '{}'",
                        inlineText, variant);
                inlineElement = new TextRun(inlineText, styleClass, variant);
            } else {
                inlineElement = new TextRun(inlineText, styleClassBold, variant);
                log.debug("Styleclass TextRun {}",inlineElement.getStyleClass());
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
        @SuppressWarnings("unused")
        public ParagraphBuilder addInlineTextItalic(String inlineText) {
            if (inlineText == null) {
                throw new IllegalArgumentException("Inline text cannot be null");
            }

            InlineElement inlineElement;
            if (styleClassItalic == null) {
                log.warn("No style class for italic defined: using normal style class for text '{}'",
                        inlineText);
                inlineElement = new TextRun(inlineText, styleClass,null);
            } else {
                inlineElement = new TextRun(inlineText, styleClassItalic,null);
            }

            this.inlineElements.add(inlineElement);
            return this;
        }

        /**
         * Adds inline text with italic styling and a specific variant.
         *
         * @param inlineText the text to add; must not be {@code null}
         * @param variant    the variant for this text segment; may be {@code null}
         * @return this builder instance for method chaining
         * @throws IllegalArgumentException if inlineText is {@code null}
         */
        @SuppressWarnings("unused")
        public ParagraphBuilder addInlineTextItalicWithVariant(String inlineText, String variant) {
            if (inlineText == null) {
                throw new IllegalArgumentException("Inline text cannot be null");
            }

            InlineElement inlineElement;
            if (styleClassItalic == null) {
                log.warn("No style class for italic defined: using normal style class for text '{}' with variant '{}'",
                        inlineText, variant);
                inlineElement = new TextRun(inlineText, styleClass, variant);
            } else {
                inlineElement = new TextRun(inlineText, styleClassItalic, variant);
            }

            this.inlineElements.add(inlineElement);
            return this;
        }

        /**
         * Adds inline text with italic styling and a specific variant.
         *
         * @param text the text to add; must not be {@code null}
         * @param href   the href (url); must not be {@code null}
         * @return this builder instance for method chaining
         * @throws IllegalArgumentException if inlineText is {@code null}
         */
        @SuppressWarnings("unused")
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
         * Adds inline text with italic styling and a specific variant.
         *
         * @param text the text to add; must not be {@code null}
         * @param altText alternative text for this hyperlink
         * @param href   the href (url); must not be {@code null}
         * @return this builder instance for method chaining
         * @throws IllegalArgumentException if inlineText is {@code null}
         */
        @SuppressWarnings("unused")
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
            return new Paragraph(styleClass, inlineElements, variant);
        }
    }
}