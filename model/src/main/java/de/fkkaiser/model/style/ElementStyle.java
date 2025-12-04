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

import de.fkkaiser.model.JsonPropertyName;
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.structure.ElementTargetType;
import de.fkkaiser.model.style.builder.BlockImageStyleBuilder;
import de.fkkaiser.model.style.builder.HeadlineStyleBuilder;
import de.fkkaiser.model.style.builder.ParagraphStyleBuilder;
import de.fkkaiser.model.style.builder.TextRunStyleBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents an ElementStyle.
 *
 * @param name name of the style
 * @param targetElement type of element this style targets
 * @param properties style properties specific to the target element
 * @author Katrin Kaiser
 * @version 1.3.0
 */
@PublicAPI
public record ElementStyle(
        @JsonProperty(JsonPropertyName.NAME) String name,
        @JsonProperty(JsonPropertyName.TARGET_ELEMENT) ElementTargetType targetElement,
        @JsonProperty(JsonPropertyName.PROPERTIES)
        @JsonTypeInfo(
                use = JsonTypeInfo.Id.NAME,
                include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
                property = JsonPropertyName.TARGET_ELEMENT
        )
        @JsonSubTypes({
                @JsonSubTypes.Type(value = ParagraphStyleProperties.class, name = JsonPropertyName.PARAGRAPH),
                @JsonSubTypes.Type(value = HeadlineStyleProperties.class, name = JsonPropertyName.HEADLINE),
                @JsonSubTypes.Type(value = ListStyleProperties.class, name = JsonPropertyName.LIST),
                @JsonSubTypes.Type(value = TableStyleProperties.class, name = JsonPropertyName.TABLE),
                @JsonSubTypes.Type(value = TableCellStyleProperties.class, name = JsonPropertyName.TABLE_CELL),
                @JsonSubTypes.Type(value = SectionStyleProperties.class, name = JsonPropertyName.SECTION),
                @JsonSubTypes.Type(value = PartStyleProperties.class, name = JsonPropertyName.PART),
                @JsonSubTypes.Type(value = TextRunStyleProperties.class, name = JsonPropertyName.TEXT_RUN),
                @JsonSubTypes.Type(value = FootnoteStyleProperties.class, name = JsonPropertyName.FOOTNOTE),
                @JsonSubTypes.Type(value = BlockImageStyleProperties.class, name = JsonPropertyName.BLOCK_IMAGE),
                @JsonSubTypes.Type(value = LayoutTableStyleProperties.class, name = JsonPropertyName.LAYOUT_TABLE),
                @JsonSubTypes.Type(value = ListItemStyleProperties.class, name = JsonPropertyName.LIST_ITEM)
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
    }

    // ==================== Factory Methods ====================

    /**
     * Creates an ElementStyle for a paragraph with basic text style configuration.
     * @param name the unique identifier for this element style (e.g., "body-paragraph");
     * @param textStyle the TextStyle to apply to the paragraph;
     * @return a new ElementStyle for paragraphs
     * @throws NullPointerException if name is {@code null}
     * @throws IllegalArgumentException if name is empty
     */
    @PublicAPI
    public static ElementStyle forParagraph(String name, TextStyle textStyle) {
        Objects.requireNonNull(name, "name must not be null");
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("null cannot be an empty string");
        }
        ParagraphStyleProperties props = new ParagraphStyleProperties();
        if(textStyle != null) {
            props.setTextStyleName(textStyle.name());
        }
        return new ElementStyle(name, ElementTargetType.PARAGRAPH, props);
    }

    /**
     * Creates an ElementStyle for a paragraph with specified paragraph style properties.
     * @param name the unique identifier for this element style (e.g., "body-paragraph");
     * @param paragraphStyleProperties the ParagraphStyleProperties to apply;
     * @return a new ElementStyle for paragraphs
     */
    @PublicAPI
    public static ElementStyle forParagraph(String name,ParagraphStyleProperties paragraphStyleProperties) {
        Objects.requireNonNull(name, "name must not be null");
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("null cannot be an empty string");
        }
        ParagraphStyleProperties finalProps = Objects.requireNonNullElse(paragraphStyleProperties, new ParagraphStyleProperties());
        return new ElementStyle(name, ElementTargetType.PARAGRAPH, finalProps);
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
        return new ElementStyle(name, ElementTargetType.HEADLINE, props);
    }

    /**
     * Creates an ElementStyle for a headline with specified headline style properties.
     *
     * @param name                     the unique identifier for this element style (e.g., "h1");
     *                                 must not be {@code null} or empty
     * @param headlineStyleProperties  the HeadlineStyleProperties to apply;
     *                                 if {@code null}, default properties will be used
     * @return a new ElementStyle for headlines
     * @throws NullPointerException if name is {@code null}
     * @throws IllegalArgumentException if name is empty
     */
    @PublicAPI
    public static ElementStyle forHeadline(String name, HeadlineStyleProperties headlineStyleProperties) {
        Objects.requireNonNull(name, "name must not be null");
        if(name.trim().isEmpty()) {
            throw new IllegalArgumentException("null cannot be an empty string");
        }
        HeadlineStyleProperties finalProps = Objects.requireNonNullElse(headlineStyleProperties, new HeadlineStyleProperties());
        return new ElementStyle(name, ElementTargetType.HEADLINE, finalProps);
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
        return new ElementStyle(name, ElementTargetType.LIST, props);
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
        return new ElementStyle(name, ElementTargetType.TABLE, props);
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
        return new ElementStyle(name, ElementTargetType.TABLE_CELL, props);
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
        return new ElementStyle(name, ElementTargetType.SECTION, props);
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
        return new ElementStyle(name, ElementTargetType.PART, props);
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
        return new ElementStyle(name, ElementTargetType.TEXT_RUN, props);
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
        return new ElementStyle(name, ElementTargetType.FOOTNOTE, props);
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
        return new ElementStyle(name, ElementTargetType.BLOCK_IMAGE, props);
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
        return new ElementStyle(name, ElementTargetType.LAYOUT_TABLE, props);
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
        Objects.requireNonNull(targetElement, "targetElement must not be null");

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


}
