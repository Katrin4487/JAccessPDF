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
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.style.builder.BlockImageStyleBuilder;
import de.fkkaiser.model.style.builder.HeadlineStyleBuilder;
import de.fkkaiser.model.style.builder.ParagraphStyleBuilder;
import de.fkkaiser.model.style.builder.TextRunStyleBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
 *   <li>"part" → {@link PartStyleProperties}</li>
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
 *
 * @param name          the unique identifier for this style (e.g., "body-paragraph");
 *                      must not be {@code null} or empty
 * @param targetElement the type of element this style applies to (e.g., "paragraph");
 *                      must be one of {@link StyleTargetTypes};
 *                      must not be {@code null} or empty
 * @param properties    the style properties specific to the target element type;
 *                      must not be {@code null}
 * @author Katrin Kaiser
 * @version 1.1.0
 *
 * @see ElementStyleProperties
 * @see StyleTargetTypes
 * @see StyleSheet
 * @see TextStyle
 */
@PublicAPI
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
                @JsonSubTypes.Type(value = PartStyleProperties.class, name = StyleTargetTypes.PART),
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
     * @throws IllegalArgumentException if any parameter is  empty or invalid
     * @throws NullPointerException    if any parameter is {@code null}
     */
    @PublicAPI
    public ElementStyle {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(targetElement, "targetElement must not be null");
        Objects.requireNonNull(properties, "properties must not be null");
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Element style name cannot be empty");
        }
        if (targetElement.trim().isEmpty()) {
            throw new IllegalArgumentException("Target element cannot be empty");
        }

        // Validate that targetElement is a known type
        if (isNotValidTargetElement(targetElement)) {
            throw new IllegalArgumentException(
                    String.format("Unknown target element type: '%s'. Must be one of: %s",
                            targetElement, getValidTargetElements())
            );
        }
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
     * @throws NullPointerException if name or textStyle is {@code null}
     */
    @PublicAPI
    public static ElementStyle forParagraph(String name, TextStyle textStyle) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(textStyle, "textStyle must not be null");
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("null cannot be an empty string");
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
     * @throws NullPointerException if name or textStyle is {@code null}
     * @throws IllegalArgumentException if name is empty
     */
    @PublicAPI
    public static ElementStyle forHeadline(String name, TextStyle textStyle) {
        Objects.requireNonNull(textStyle, "textStyle must not be null");
        Objects.requireNonNull(name, "name must not be null");
        if(name.trim().isEmpty()) {
            throw new IllegalArgumentException("null cannot be an empty string");
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
     * @throws NullPointerException if name or textStyle is {@code null}
     * @throws IllegalArgumentException if name is empty
     */
    @PublicAPI
    public static ElementStyle forList(String name, TextStyle textStyle) {
        Objects.requireNonNull(textStyle, "textStyle must not be null");
        Objects.requireNonNull(name, "name must not be null");
        if(name.trim().isEmpty()) {
            throw new IllegalArgumentException("null cannot be an empty string");
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
     * @throws NullPointerException if name or textStyle is {@code null}
     * @throws IllegalArgumentException if name is empty
     */
    @PublicAPI
    public static ElementStyle forTable(String name, TextStyle textStyle) {
        Objects.requireNonNull(textStyle, "textStyle must not be null");
        Objects.requireNonNull(name, "name must not be null");
        if(name.trim().isEmpty()) {
            throw new IllegalArgumentException("null cannot be an empty string");
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
     * @throws NullPointerException if name or textStyle is {@code null}
     */
    @PublicAPI
    public static ElementStyle forTableCell(String name, TextStyle textStyle) {
        Objects.requireNonNull(textStyle, "textStyle must not be null");
        Objects.requireNonNull(name, "name must not be null");
        if(name.trim().isEmpty()) {
            throw new IllegalArgumentException("null cannot be an empty string");
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
     * @throws NullPointerException if name is {@code null}
     */
    @PublicAPI
    public static ElementStyle forSection(String name) {
        Objects.requireNonNull(name, "name must not be null");
        SectionStyleProperties props = new SectionStyleProperties();
        return new ElementStyle(name, StyleTargetTypes.SECTION, props);
    }

    /**
     * Creates an ElementStyle for a part with basic configuration.
     * @param name the unique identifier for this element style (e.g., "part-style");
     *             must not be {@code null} or empty
     * @return a new ElementStyle for parts
     */
    @PublicAPI
    public static ElementStyle forPart(String name) {
        PartStyleProperties props = new PartStyleProperties();
        return new ElementStyle(name, StyleTargetTypes.PART, props);
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
    @PublicAPI
    public static ElementStyle forTextRun(String name, TextStyle textStyle) {
        Objects.requireNonNull(textStyle, "textStyle must not be null");
        Objects.requireNonNull(name, "name must not be null");
        if(name.trim().isEmpty()) {
            throw new IllegalArgumentException("null cannot be an empty string");
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
     * @throws IllegalArgumentException if name is empty
     * @throws NullPointerException if name or textStyle is {@code null}
     */
    @PublicAPI
    public static ElementStyle forFootnote(String name, TextStyle textStyle) {
        Objects.requireNonNull(textStyle, "textStyle must not be null");
        Objects.requireNonNull(name, "name must not be null");
        if(name.trim().isEmpty()) {
            throw new IllegalArgumentException("null cannot be an empty string");
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
     * @throws NullPointerException if name is {@code null}
     * @throws IllegalArgumentException if name is empty
     */
    @PublicAPI
    public static ElementStyle forBlockImage(String name) {
        Objects.requireNonNull(name, "name must not be null");
        if(name.trim().isEmpty()) {
            throw new IllegalArgumentException("null cannot be an empty string");
        }
        BlockImageStyleProperties props = new BlockImageStyleProperties();
        return new ElementStyle(name, StyleTargetTypes.BLOCK_IMAGE, props);
    }

    /**
     * Creates an ElementStyle for a layout table with basic configuration.
     *
     * @param name the unique identifier for this element style (e.g., "layout-grid");
     *             must not be {@code null} or empty
     * @return a new ElementStyle for layout tables
     * @throws NullPointerException if name is {@code null}
     * @throws IllegalArgumentException if name is empty
     */
    @PublicAPI
    public static ElementStyle forLayoutTable(String name) {
        Objects.requireNonNull(name, "name must not be null");
        if(name.trim().isEmpty()) {
            throw new IllegalArgumentException("null cannot be an empty string");
        }
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
     * @param name      the unique identifier for this element style (not null or empty)
     * @param textStyle the TextStyle to apply (not null)
     * @return a new ParagraphStyleBuilder
     * @throws NullPointerException    if name or textStyle is {@code null}
     * @throws IllegalArgumentException if name is empty
     */
    @PublicAPI
    public static ParagraphStyleBuilder paragraphBuilder(String name, TextStyle textStyle) {
        Objects.requireNonNull(textStyle, "textStyle must not be null");
        Objects.requireNonNull(name, "name must not be null");
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("null cannot be an empty string");
        }
        return new ParagraphStyleBuilder(name, textStyle);
    }

    /**
     * Creates a builder for constructing a headline style with detailed properties.
     *
     * @param name      the unique identifier for this element style
     * @param textStyle the TextStyle to apply
     * @return a new HeadlineStyleBuilder
     */
    @PublicAPI
    public static HeadlineStyleBuilder headlineBuilder(String name, TextStyle textStyle) {
        return new HeadlineStyleBuilder(name, textStyle);
    }

    /**
     * Creates a builder for constructing a block image style with detailed properties.
     *
     * @param name      the unique identifier for this element style
     * @return a new BlockImageStyleBuilder
     */
    @PublicAPI
    public static BlockImageStyleBuilder imageBuilder(String name) {
        return new BlockImageStyleBuilder(name);
    }

    @PublicAPI
    public static TextRunStyleBuilder textRunBuilder(String name) {
        return new TextRunStyleBuilder(name);
    }

    /**
     * Validates the ElementStyle instance.
     * This method checks that all required fields are set and valid.
     * @return a list of validation error messages; empty if valid
     */
    @Internal
    public List<String> validate() {
        List<String> errors = new ArrayList<>();

        // Validate style name
        if (name == null || name.trim().isEmpty()) {
            errors.add("Element style name cannot be null or empty");
        }

        // Validate target element
        if (targetElement == null || targetElement.trim().isEmpty()) {
            errors.add("Target element cannot be null or empty");
        } else if (isNotValidTargetElement(targetElement)) {
            errors.add("Unknown target element type: '" + targetElement + "'");
        }

        // Validate properties
        if (properties == null) {
            errors.add("Element style properties cannot be null");
        } else {
            List<String> propertyErrors = properties.validate();
            if (!propertyErrors.isEmpty()) {
                errors.add("Style '" + name + "' has property errors:");
                errors.addAll(propertyErrors);
            }
        }

        return errors;
    }

    // ==================== Helper Methods ====================
    /**
     * Checks if the target element type is valid.
     *
     * @param targetElement the target element type to check
     * @return {@code true} if invalid, {@code false} otherwise
     */
    private static boolean isNotValidTargetElement(String targetElement) {
        return !targetElement.equals(StyleTargetTypes.PARAGRAPH) &&
                !targetElement.equals(StyleTargetTypes.HEADLINE) &&
                !targetElement.equals(StyleTargetTypes.LIST) &&
                !targetElement.equals(StyleTargetTypes.TABLE) &&
                !targetElement.equals(StyleTargetTypes.TABLE_CELL) &&
                !targetElement.equals(StyleTargetTypes.SECTION) &&
                !targetElement.equals(StyleTargetTypes.TEXT_RUN) &&
                !targetElement.equals(StyleTargetTypes.FOOTNOTE) &&
                !targetElement.equals(StyleTargetTypes.BLOCK_IMAGE) &&
                !targetElement.equals(StyleTargetTypes.LAYOUT_TABLE) &&
                !targetElement.equals(StyleTargetTypes.PART);
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


}
