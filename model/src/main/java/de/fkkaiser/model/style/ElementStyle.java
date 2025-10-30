package de.fkkaiser.model.style;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.fkkaiser.model.style.builder.HeadlineStyleBuilder;
/**
 * Represents a single, named style definition in the stylesheet.
 * An ElementStyle connects a style name (e.g., "important-paragraph") to a set of
 * properties, targeting a specific element type (paragraph, headline, list, etc.).
 *
 * <p><b>Purpose:</b></p>
 * Element styles serve as reusable style templates that can be applied to document
 * elements. Instead of defining formatting properties inline for each element, you
 * define named styles once and reference them by name throughout the document.
 *
 * <p><b>Polymorphic Design:</b></p>
 * This class uses Jackson's polymorphic deserialization to automatically map different
 * property types based on the {@code target-element} field:
 * <ul>
 *   <li>"paragraph" → {@link ParagraphStyleProperties}</li>
 *   <li>"headline" → {@link HeadlineStyleProperties}</li>
 *   <li>"list" → {@link ListStyleProperties}</li>
 *   <li>"table" → {@link TableStyleProperties}</li>
 *   <li>"table-cell" → {@link TableCellStyleProperties}</li>
 *   <li>"section" → {@link SectionStyleProperties}</li>
 *   <li>"text-run" → {@link TextRunStyleProperties}</li>
 *   <li>"footnote" → {@link FootnoteStyleProperties}</li>
 *   <li>"block-image" → {@link BlockImageStyleProperties}</li>
 *   <li>"layout-table" → {@link LayoutTableStyleProperties}</li>
 * </ul>
 *
 * <p><b>JSON Representation:</b></p>
 * <pre>{@code
 * {
 *   "name": "body-paragraph",
 *   "target-element": "paragraph",
 *   "properties": {
 *     "text-style-name": "normal-text",
 *     "text-align": "justify",
 *     "text-indent": "1cm",
 *     "space-before": "6pt",
 *     "space-after": "6pt"
 *   }
 * }
 * }</pre>
 *
 * <p><b>Usage Example 1 - Direct Construction:</b></p>
 * <pre>{@code
 * // Create properties
 * ParagraphStyleProperties props = new ParagraphStyleProperties();
 * props.setTextStyleName("body-text");
 * props.setTextAlign("justify");
 * props.setTextIndent("1cm");
 *
 * // Create element style
 * ElementStyle paragraphStyle = new ElementStyle(
 *     "body-paragraph",
 *     StyleTargetTypes.PARAGRAPH,
 *     props
 * );
 * }</pre>
 *
 * <p><b>Usage Example 2 - Using Factory Methods (Recommended):</b></p>
 * <pre>{@code
 * // Simple factory for basic styles
 * TextStyle normalText = factory.normal("body-text", 12);
 * ElementStyle paragraph = ElementStyle.forParagraph("body-paragraph", normalText);
 * ElementStyle headline = ElementStyle.forHeadline("h1", boldText);
 *
 * // Advanced factory with builder for complex styles
 * ElementStyle advancedParagraph = ElementStyle.paragraphBuilder("body-paragraph")
 *     .withTextStyle(normalText)
 *     .withTextAlign("justify")
 *     .withTextIndent("1cm")
 *     .withSpaceBefore("6pt")
 *     .withSpaceAfter("6pt")
 *     .build();
 * }</pre>
 *
 * <p><b>Usage Example 3 - In a StyleSheet:</b></p>
 * <pre>{@code
 * StyleSheet styleSheet = StyleSheet.builder()
 *     .addTextStyle(normalText)
 *     .addTextStyle(boldText)
 *     .addElementStyle(ElementStyle.forParagraph("body", normalText))
 *     .addElementStyle(ElementStyle.forHeadline("h1", boldText))
 *     .addElementStyle(ElementStyle.forList("bullet-list", normalText))
 *     .build();
 * }</pre>
 *
 * <p><b>Style Inheritance:</b></p>
 * Element styles support inheritance through the {@link ElementStyleProperties#mergeWith(ElementStyleProperties)}
 * mechanism. Child elements can inherit and override properties from parent elements.
 *
 * <p><b>Validation:</b></p>
 * The compact constructor validates that:
 * <ul>
 *   <li>name is not null or empty</li>
 *   <li>targetElement is not null or empty</li>
 *   <li>properties is not null</li>
 *   <li>targetElement matches one of the known {@link StyleTargetTypes}</li>
 * </ul>
 *
 * <p><b>Immutability:</b></p>
 * As a record, ElementStyle is immutable. Once created, its values cannot be changed.
 * This ensures thread-safety and prevents accidental modification.
 *
 * @param name          the unique identifier for this style (e.g., "body-paragraph");
 *                      must not be {@code null} or empty
 * @param targetElement the type of element this style applies to (e.g., "paragraph");
 *                      must be one of {@link StyleTargetTypes};
 *                      must not be {@code null} or empty
 * @param properties    the style properties specific to the target element type;
 *                      must not be {@code null}
 * @author FK Kaiser
 * @version 1.1
 * @see ElementStyleProperties
 * @see StyleTargetTypes
 * @see StyleSheet
 * @see TextStyle
 */
public record ElementStyle(
        @JsonProperty("name") String name,
        @JsonProperty("target-element") String targetElement,
        @JsonProperty("properties")
        @JsonTypeInfo(
                use = JsonTypeInfo.Id.NAME,
                include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
                property = "target-element"
        )
        @JsonSubTypes({
                @JsonSubTypes.Type(value = ParagraphStyleProperties.class, name = StyleTargetTypes.PARAGRAPH),
                @JsonSubTypes.Type(value = HeadlineStyleProperties.class, name = StyleTargetTypes.HEADLINE),
                @JsonSubTypes.Type(value = ListStyleProperties.class, name = StyleTargetTypes.LIST),
                @JsonSubTypes.Type(value = TableStyleProperties.class, name = StyleTargetTypes.TABLE),
                @JsonSubTypes.Type(value = TableCellStyleProperties.class, name = StyleTargetTypes.TABLE_CELL),
                @JsonSubTypes.Type(value = SectionStyleProperties.class, name = StyleTargetTypes.SECTION),
                @JsonSubTypes.Type(value = TextRunStyleProperties.class, name = StyleTargetTypes.TEXT_RUN),
                @JsonSubTypes.Type(value = FootnoteStyleProperties.class, name = StyleTargetTypes.FOOTNOTE),
                @JsonSubTypes.Type(value = BlockImageStyleProperties.class, name = StyleTargetTypes.BLOCK_IMAGE),
                @JsonSubTypes.Type(value = LayoutTableStyleProperties.class, name = StyleTargetTypes.LAYOUT_TABLE)
        })
        ElementStyleProperties properties
) {

    /**
     * Compact constructor that validates all ElementStyle parameters.
     * This constructor is automatically called whenever an ElementStyle is created,
     * ensuring that all instances are valid.
     *
     * <p><b>Validation Rules:</b></p>
     * <ul>
     *   <li>name must not be null or empty (after trimming)</li>
     *   <li>targetElement must not be null or empty (after trimming)</li>
     *   <li>properties must not be null</li>
     *   <li>targetElement must be one of the known types in {@link StyleTargetTypes}</li>
     * </ul>
     *
     * @throws IllegalArgumentException if any parameter is null, empty, or invalid
     */
    public ElementStyle {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Element style name cannot be null or empty");
        }
        if (targetElement == null || targetElement.trim().isEmpty()) {
            throw new IllegalArgumentException("Target element cannot be null or empty");
        }
        if (properties == null) {
            throw new IllegalArgumentException("Element style properties cannot be null");
        }

        // Validate that targetElement is a known type
        if (!isValidTargetElement(targetElement)) {
            throw new IllegalArgumentException(
                    String.format("Unknown target element type: '%s'. Must be one of: %s",
                            targetElement, getValidTargetElements())
            );
        }
    }

    /**
     * Checks if the target element type is valid.
     *
     * @param targetElement the target element type to check
     * @return {@code true} if valid, {@code false} otherwise
     */
    private static boolean isValidTargetElement(String targetElement) {
        return targetElement.equals(StyleTargetTypes.PARAGRAPH) ||
                targetElement.equals(StyleTargetTypes.HEADLINE) ||
                targetElement.equals(StyleTargetTypes.LIST) ||
                targetElement.equals(StyleTargetTypes.TABLE) ||
                targetElement.equals(StyleTargetTypes.TABLE_CELL) ||
                targetElement.equals(StyleTargetTypes.SECTION) ||
                targetElement.equals(StyleTargetTypes.TEXT_RUN) ||
                targetElement.equals(StyleTargetTypes.FOOTNOTE) ||
                targetElement.equals(StyleTargetTypes.BLOCK_IMAGE) ||
                targetElement.equals(StyleTargetTypes.LAYOUT_TABLE) ||
                targetElement.equals(StyleTargetTypes.PART);
    }

    /**
     * Returns a comma-separated string of all valid target element types.
     * Used for error messages.
     *
     * @return a string listing all valid target elements
     */
    private static String getValidTargetElements() {
        return String.join(", ",
                StyleTargetTypes.PARAGRAPH,
                StyleTargetTypes.HEADLINE,
                StyleTargetTypes.LIST,
                StyleTargetTypes.TABLE,
                StyleTargetTypes.TABLE_CELL,
                StyleTargetTypes.SECTION,
                StyleTargetTypes.TEXT_RUN,
                StyleTargetTypes.FOOTNOTE,
                StyleTargetTypes.BLOCK_IMAGE,
                StyleTargetTypes.LAYOUT_TABLE,
                StyleTargetTypes.PART
        );
    }

    // ==================== Factory Methods ====================

    /**
     * Creates an ElementStyle for a paragraph with basic text style configuration.
     * This is a convenience method for creating simple paragraph styles that only
     * require a text style reference.
     *
     * <p>For more complex paragraph styles with additional properties like text-align,
     * text-indent, spacing, etc., use {@link #paragraphBuilder(String, TextStyle)} instead.</p>
     *
     * @param name      the unique identifier for this element style (e.g., "body-paragraph");
     *                  must not be {@code null} or empty
     * @param textStyle the TextStyle to apply to the paragraph;
     *                  must not be {@code null}
     * @return a new ElementStyle for paragraphs
     * @throws IllegalArgumentException if name or textStyle is {@code null}
     */
    @SuppressWarnings("unused")
    public static ElementStyle forParagraph(String name, TextStyle textStyle) {
        if (textStyle == null) {
            throw new IllegalArgumentException("TextStyle cannot be null");
        }
        ParagraphStyleProperties props = new ParagraphStyleProperties();
        props.setTextStyleName(textStyle.name());
        return new ElementStyle(name, StyleTargetTypes.PARAGRAPH, props);
    }

    /**
     * Creates an ElementStyle for a headline with basic text style configuration.
     * This is a convenience method for creating simple headline styles that only
     * require a text style reference.
     *
     * <p>For more complex headline styles with additional properties, use
     * {@link #headlineBuilder(String, TextStyle)} instead.</p>
     *
     * @param name      the unique identifier for this element style (e.g., "h1");
     *                  must not be {@code null} or empty
     * @param textStyle the TextStyle to apply to the headline;
     *                  must not be {@code null}
     * @return a new ElementStyle for headlines
     * @throws IllegalArgumentException if name or textStyle is {@code null}
     */
    @SuppressWarnings("unused")
    public static ElementStyle forHeadline(String name, TextStyle textStyle) {
        if (textStyle == null) {
            throw new IllegalArgumentException("TextStyle cannot be null");
        }
        HeadlineStyleProperties props = new HeadlineStyleProperties();
        props.setTextStyleName(textStyle.name());
        return new ElementStyle(name, StyleTargetTypes.HEADLINE, props);
    }

    /**
     * Creates an ElementStyle for a list with basic text style configuration.
     *
     * @param name      the unique identifier for this element style (e.g., "bullet-list");
     *                  must not be {@code null} or empty
     * @param textStyle the TextStyle to apply to list items;
     *                  must not be {@code null}
     * @return a new ElementStyle for lists
     * @throws IllegalArgumentException if name or textStyle is {@code null}
     */
    @SuppressWarnings("unused")
    public static ElementStyle forList(String name, TextStyle textStyle) {
        if (textStyle == null) {
            throw new IllegalArgumentException("TextStyle cannot be null");
        }
        ListStyleProperties props = new ListStyleProperties();
        props.setTextStyleName(textStyle.name());
        return new ElementStyle(name, StyleTargetTypes.LIST, props);
    }

    /**
     * Creates an ElementStyle for a table with basic text style configuration.
     *
     * @param name      the unique identifier for this element style (e.g., "data-table");
     *                  must not be {@code null} or empty
     * @param textStyle the TextStyle to apply to table content;
     *                  must not be {@code null}
     * @return a new ElementStyle for tables
     * @throws IllegalArgumentException if name or textStyle is {@code null}
     */
    @SuppressWarnings("unused")
    public static ElementStyle forTable(String name, TextStyle textStyle) {
        if (textStyle == null) {
            throw new IllegalArgumentException("TextStyle cannot be null");
        }
        TableStyleProperties props = new TableStyleProperties();
        props.setTextStyleName(textStyle.name());
        return new ElementStyle(name, StyleTargetTypes.TABLE, props);
    }

    /**
     * Creates an ElementStyle for a table cell with basic text style configuration.
     *
     * @param name      the unique identifier for this element style (e.g., "header-cell");
     *                  must not be {@code null} or empty
     * @param textStyle the TextStyle to apply to cell content;
     *                  must not be {@code null}
     * @return a new ElementStyle for table cells
     * @throws IllegalArgumentException if name or textStyle is {@code null}
     */
    @SuppressWarnings("unused")
    public static ElementStyle forTableCell(String name, TextStyle textStyle) {
        if (textStyle == null) {
            throw new IllegalArgumentException("TextStyle cannot be null");
        }
        TableCellStyleProperties props = new TableCellStyleProperties();
        props.setTextStyleName(textStyle.name());
        return new ElementStyle(name, StyleTargetTypes.TABLE_CELL, props);
    }

    /**
     * Creates an ElementStyle for a section with basic configuration.
     *
     * @param name the unique identifier for this element style (e.g., "chapter-section");
     *             must not be {@code null} or empty
     * @return a new ElementStyle for sections
     */
    @SuppressWarnings("unused")
    public static ElementStyle forSection(String name) {
        SectionStyleProperties props = new SectionStyleProperties();
        return new ElementStyle(name, StyleTargetTypes.SECTION, props);
    }

    /**
     * Creates an ElementStyle for a text run (inline text) with basic text style configuration.
     *
     * @param name      the unique identifier for this element style (e.g., "bold-text");
     *                  must not be {@code null} or empty
     * @param textStyle the TextStyle to apply to the text run;
     *                  must not be {@code null}
     * @return a new ElementStyle for text runs
     * @throws IllegalArgumentException if name or textStyle is {@code null}
     */
    @SuppressWarnings("unused")
    public static ElementStyle forTextRun(String name, TextStyle textStyle) {
        if (textStyle == null) {
            throw new IllegalArgumentException("TextStyle cannot be null");
        }
        TextRunStyleProperties props = new TextRunStyleProperties();
        props.setTextStyleName(textStyle.name());
        return new ElementStyle(name, StyleTargetTypes.TEXT_RUN, props);
    }


    /**
     * Creates an ElementStyle for a footnote with basic text style configuration.
     *
     * @param name      the unique identifier for this element style (e.g., "footnote-text");
     *                  must not be {@code null} or empty
     * @param textStyle the TextStyle to apply to footnote content;
     *                  must not be {@code null}
     * @return a new ElementStyle for footnotes
     * @throws IllegalArgumentException if name or textStyle is {@code null}
     */
    @SuppressWarnings("unused")
    public static ElementStyle forFootnote(String name, TextStyle textStyle) {
        if (textStyle == null) {
            throw new IllegalArgumentException("TextStyle cannot be null");
        }
        FootnoteStyleProperties props = new FootnoteStyleProperties();
        props.setTextStyleName(textStyle.name());
        return new ElementStyle(name, StyleTargetTypes.FOOTNOTE, props);
    }

    /**
     * Creates an ElementStyle for a block image with basic configuration.
     *
     * @param name the unique identifier for this element style (e.g., "centered-image");
     *             must not be {@code null} or empty
     * @return a new ElementStyle for block images
     */
    @SuppressWarnings("unused")
    public static ElementStyle forBlockImage(String name) {
        BlockImageStyleProperties props = new BlockImageStyleProperties();
        return new ElementStyle(name, StyleTargetTypes.BLOCK_IMAGE, props);
    }

    /**
     * Creates an ElementStyle for a layout table with basic configuration.
     *
     * @param name the unique identifier for this element style (e.g., "layout-grid");
     *             must not be {@code null} or empty
     * @return a new ElementStyle for layout tables
     */
    @SuppressWarnings("unused")
    public static ElementStyle forLayoutTable(String name) {
        LayoutTableStyleProperties props = new LayoutTableStyleProperties();
        return new ElementStyle(name, StyleTargetTypes.LAYOUT_TABLE, props);
    }

    // ==================== Builder Factory Methods ====================

    /**
     * Creates a builder for constructing a paragraph style with detailed properties.
     * This builder provides a fluent API for setting multiple paragraph-specific
     * properties such as text alignment, indentation, spacing, and more.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * ElementStyle paragraphStyle = ElementStyle.paragraphBuilder("body", normalText)
     *     .withTextAlign("justify")
     *     .withTextIndent("1cm")
     *     .withSpaceBefore("6pt")
     *     .withSpaceAfter("6pt")
     *     .withHyphenation(true)
     *     .build();
     * }</pre>
     *
     * @param name      the unique identifier for this element style
     * @param textStyle the TextStyle to apply
     * @return a new ParagraphStyleBuilder
     */
    @SuppressWarnings("unused")
    public static ParagraphStyleBuilder paragraphBuilder(String name, TextStyle textStyle) {
        return new ParagraphStyleBuilder(name, textStyle);
    }

    /**
     * Creates a builder for constructing a headline style with detailed properties.
     *
     * @param name      the unique identifier for this element style
     * @param textStyle the TextStyle to apply
     * @return a new HeadlineStyleBuilder
     */
    @SuppressWarnings("unused")
    public static HeadlineStyleBuilder headlineBuilder(String name, TextStyle textStyle) {
        return new HeadlineStyleBuilder(name, textStyle);
    }

    // ==================== Builder Classes ====================

    /**
     * Fluent builder for creating paragraph element styles with detailed properties.
     * This builder allows setting all paragraph-specific properties in a readable,
     * chainable manner.
     *
     * @see #paragraphBuilder(String, TextStyle)
     */
    public static class ParagraphStyleBuilder {
        private final String name;
        private final ParagraphStyleProperties properties;

        private ParagraphStyleBuilder(String name, TextStyle textStyle) {
            this.name = name;
            this.properties = new ParagraphStyleProperties();
            this.properties.setTextStyleName(textStyle.name());
        }

        /**
         * Sets the text algin. The following alignments are available.
         * <ul>
         *     <li>start: Aligns the text at the start edge. In LTR-Languages it's equal to left,
         *     in RTL-Languages it's equal to right.</li>
         *     <li>end: Aligns the text at the end edge. In LTR-Languages it's equal to right,
         *     in RTL-Languages it's equal to left</li>
         *     <li>left: Aligns the text at the left edge</li>
         *     <li>right: Aligns the text at the right edge</li>
         *     <li>center: centers the text</li>
         *     <li>justify: Creates justified text. The text (except the last line) is adjusted to align
         *     flush with both the left and right margins.</li>
         *     <li>Aligns text on a specific character. This is often used in tables to align numbers
         *     by the decimal separator (e.g., text-align=".").</li>
         * </ul>
         *
         * @param textAlign keyword that defines the alignment
         * @return ParagraphStyleBuilder to use this method in the chain
         */
        @SuppressWarnings("unused")
        public ParagraphStyleBuilder withTextAlign(String textAlign) {
            properties.setTextAlign(textAlign);
            return this;
        }

        /**
         * Defines the text indent (space before the text in the first line).
         * You can choose between the units: mm, cm, in, pt, pc, em or %.
         *
         * @param textIndent String with value and unit for the text indent
         * @return ParagraphStyleBuilder to use this method in a chain
         */
        @SuppressWarnings("unused")
        public ParagraphStyleBuilder withTextIndent(String textIndent) {
            properties.setTextIndent(textIndent);
            return this;
        }

        /**
         * Defines the white space before a text block.
         * You can choose between the units: mm, cm, in, pt, pc, em or %.
         *
         * @param spaceBefore String with value and unit for space before
         * @return ParagraphStyleBuilder to use this method in a chain
         */
        public ParagraphStyleBuilder withSpaceBefore(String spaceBefore) {
            properties.setSpaceBefore(spaceBefore);
            return this;
        }

        /**
         * Defines the white space after a text block.
         * You can choose between the units: mm, cm, in, pt, pc, em or %.
         *
         * @param spaceAfter String with value and unit for space after
         * @return ParagraphStyleBuilder to use this method in a chain
         */
        public ParagraphStyleBuilder withSpaceAfter(String spaceAfter) {
            properties.setSpaceAfter(spaceAfter);
            return this;
        }

        /**
         * Defines if automatic splitting (or division) of words at the end of a line is enabled.
         * You must specify the correct language of your text that the hybernation works correctly.
         *
         * @param hyphenate true to enabled hyphenation, else false
         * @return ParagraphStyleBuilder to use this method in a chain
         */
        @SuppressWarnings("unused")
        public ParagraphStyleBuilder withHyphenation(boolean hyphenate) {
            properties.setHyphenate(hyphenate);
            return this;
        }

        /**
         * Defines the language of the paragraph. It only make sense if the paragraph
         * is written in a different language then the document.
         *
         * @param language language as ISO-Code String (e.g. en-US)
         * @return ParagraphStyleBuilder to use this method in a chain
         */
        public ParagraphStyleBuilder withLanguage(String language) {
            properties.setLanguage(language);
            return this;
        }

        /**
         * Defines the background color of this paragraph. Valid color values are:
         * <ul>
         *     <li>HexCode: e.g. #F0F0F0</li>
         *     <li>Color Name: e.g. yellow</li>
         *     <li>RGB-Function: rgb(255,0,0)</li>
         * </ul>
         *
         * @param color background color for this paragraph as string
         * @return ParagraphStyleBuilder to use this method in a chain
         */
        public ParagraphStyleBuilder withBackgroundColor(String color) {
            properties.setBackgroundColor(color);
            return this;
        }

        /**
         * Build method for the ParagraphStyleBuilder.
         * This method must be called at the end of every ParagraphBuilder chain.
         *
         * @return ElementStyle for the paragraph, that should be created with the
         * ParagraphStyleBuilder
         */
        public ElementStyle build() {
            return new ElementStyle(name, StyleTargetTypes.PARAGRAPH, properties);
        }
    }


}