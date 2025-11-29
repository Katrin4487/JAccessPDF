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
import de.fkkaiser.model.font.FontStyleValue;

/**
 * Represents a named text style that defines the visual appearance of text elements.
 * A TextStyle combines font properties (family, size, weight, style) into a reusable
 * style definition that can be referenced by document elements.
 *
 * <p><b>Purpose:</b></p>
 * Text styles serve as named templates for text formatting. Instead of specifying font
 * properties repeatedly throughout a document, you can define styles like "heading1",
 * "body-text", or "footnote" and reference them by name. This promotes consistency
 * and makes document-wide style changes easier.
 *
 * <p><b>Components:</b></p>
 * <ul>
 *   <li><b>name:</b> Unique identifier for this style (e.g., "heading1", "body-text")</li>
 *   <li><b>fontSize:</b> Size of the font with unit (e.g., "12pt", "16px")</li>
 *   <li><b>fontFamilyName:</b> Name of the font family (e.g., "Arial", "Roboto")</li>
 *   <li><b>fontWeight:</b> Weight/boldness of the font (e.g., "400", "700", "bold")</li>
 *   <li><b>fontStyle:</b> Style of the font ("normal", "italic", "oblique")</li>
 * </ul>
 *
 * <p><b>JSON Representation:</b></p>
 * <pre>{@code
 * {
 *   "name": "heading1",
 *   "font-size": "24pt",
 *   "font-family-name": "Roboto",
 *   "font-weight": "700",
 *   "font-style": "normal"
 * }
 * }</pre>
 *
 * <p><b>Usage Example 1 - Direct Construction:</b></p>
 * <pre>{@code
 * // Create a text style manually
 * TextStyle heading = new TextStyle(
 *     "heading1",
 *     "24pt",
 *     "Roboto",
 *     "700",
 *     "normal"
 * );
 *
 * // Reference it in a style sheet
 * styleSheet.addTextStyle(heading);
 * }</pre>
 *
 * <p><b>Usage Example 2 - Using Factory (Recommended):</b></p>
 * <pre>{@code
 * // Create a factory for a specific font family
 * TextStyle.TextStyleFactory factory = new TextStyle.TextStyleFactory("Roboto");
 *
 * // Create various text styles quickly
 * TextStyle heading1 = factory.bold("heading1", 24);
 * TextStyle heading2 = factory.bold("heading2", 18);
 * TextStyle bodyText = factory.normal("body-text", 12);
 * TextStyle emphasis = factory.italic("emphasis", 12);
 * TextStyle strongEmphasis = factory.boldItalic("strong-emphasis", 12);
 * }</pre>
 *
 * <p><b>Font Size Units:</b></p>
 * Common units for font size include:
 * <ul>
 *   <li><b>pt</b> - Points (1pt = 1/72 inch) - most common for print</li>
 *   <li><b>px</b> - Pixels - common for screen display</li>
 *   <li><b>em</b> - Relative to parent element's font size</li>
 *   <li><b>rem</b> - Relative to root element's font size</li>
 * </ul>
 *
 * <p><b>Validation:</b></p>
 * The compact constructor validates that:
 * <ul>
 *   <li>name is not null or empty</li>
 *   <li>fontSize is not null or empty</li>
 *   <li>fontFamilyName is not null or empty</li>
 *   <li>fontWeight is not null or empty</li>
 *   <li>fontStyle is not null or empty</li>
 * </ul>
 *
 * <p><b>Immutability:</b></p>
 * As a record, TextStyle is immutable. Once created, its values cannot be changed.
 * This ensures thread-safety and prevents accidental modification.
 *
 * @param name           the unique identifier for this style (e.g., "heading1");
 *                       must not be {@code null} or empty
 * @param fontSize       the font size with unit (e.g., "12pt", "16px");
 *                       must not be {@code null} or empty
 * @param fontFamilyName the name of the font family to use (e.g., "Arial", "Roboto");
 *                       must not be {@code null} or empty
 * @param fontWeight     the font weight (e.g., "400", "700", "bold");
 *                       must not be {@code null} or empty
 * @param fontStyle      the font style ("normal", "italic", or "oblique");
 *                       must not be {@code null} or empty
 *
 * @author FK Kaiser
 * @version 1.0
 * @see StyleSheet
 * @see FontStyleValue
 * @see TextStyleFactory
 */
public record TextStyle(
        @JsonProperty("name") String name,
        @JsonProperty("font-size") String fontSize,
        @JsonProperty("font-family-name") String fontFamilyName,
        @JsonProperty("font-weight") String fontWeight,
        @JsonProperty("font-style") String fontStyle
) {

    /**
     * Compact constructor that validates all text style parameters.
     * This constructor is automatically called whenever a TextStyle is created,
     * ensuring that all instances are valid.
     *
     * @throws IllegalArgumentException if any parameter is null or empty
     */
    public TextStyle {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Text style name cannot be null or empty");
        }
        if (fontSize == null || fontSize.trim().isEmpty()) {
            throw new IllegalArgumentException("Font size cannot be null or empty");
        }
        if (fontFamilyName == null || fontFamilyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Font family name cannot be null or empty");
        }
        if (fontWeight == null || fontWeight.trim().isEmpty()) {
            throw new IllegalArgumentException("Font weight cannot be null or empty");
        }
        if (fontStyle == null || fontStyle.trim().isEmpty()) {
            throw new IllegalArgumentException("Font style cannot be null or empty");
        }
    }

    /**
     * Returns the font family name for this text style.
     *
     * <p><b>Note:</b> This method is equivalent to calling {@code fontFamilyName()}
     * and is provided for improved readability in client code. The auto-generated
     * {@code fontFamilyName()} method from the record can also be used.</p>
     *
     * @return the font family name (e.g., "Arial", "Roboto")
     */
    public String getFontFamilyName() {
        return fontFamilyName;
    }

    /**
     * Factory class for creating {@link TextStyle} instances with common configurations.
     * The factory simplifies creation of text styles by providing convenient methods
     * for standard font variants (bold, italic, normal, etc.) while maintaining
     * consistency across styles that share the same font family.
     *
     * <p><b>Purpose:</b></p>
     * When creating multiple text styles that use the same font family, this factory
     * eliminates repetition and ensures consistency. Instead of specifying the font
     * family for each style, you create one factory and use it to generate all related styles.
     *
     * <p><b>Font Size Handling:</b></p>
     * If a font size less than 1 is provided, the factory automatically uses a default
     * size of {@value #DEFAULT_FONT_SIZE_PT} points to ensure valid output. Font sizes
     * are always rendered with the "pt" (points) unit.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * // Create a factory for the Roboto font family
     * TextStyle.TextStyleFactory factory = new TextStyle.TextStyleFactory("Roboto");
     *
     * // Generate multiple text styles efficiently
     * TextStyle h1 = factory.bold("heading1", 24);           // 24pt bold
     * TextStyle h2 = factory.bold("heading2", 18);           // 18pt bold
     * TextStyle body = factory.normal("body-text", 12);      // 12pt normal
     * TextStyle quote = factory.italic("quote", 11);         // 11pt italic
     * TextStyle strong = factory.boldItalic("strong", 12);   // 12pt bold italic
     *
     * // Add to style sheet
     * styleSheet.addTextStyle(h1);
     * styleSheet.addTextStyle(h2);
     * styleSheet.addTextStyle(body);
     * }</pre>
     *
     * <p><b>Available Methods:</b></p>
     * <ul>
     *   <li>{@link #normal(String, int)} - Regular weight (400), normal style</li>
     *   <li>{@link #bold(String, int)} - Bold weight (700), normal style</li>
     *   <li>{@link #italic(String, int)} - Regular weight (400), italic style</li>
     *   <li>{@link #oblique(String, int)} - Regular weight (400), oblique style</li>
     *   <li>{@link #boldItalic(String, int)} - Bold weight (700), italic style</li>
     *   <li>{@link #boldOblique(String, int)} - Bold weight (700), oblique style</li>
     * </ul>
     *
     * <p><b>Thread Safety:</b></p>
     * This factory is immutable and thread-safe. A single instance can be safely
     * shared across multiple threads.
     *
     * @see TextStyle
     * @see FontStyleValue
     */
    @SuppressWarnings("unused")
    public static class TextStyleFactory {

        /**
         * Font weight constant for bold text.
         * Corresponds to CSS font-weight: 700.
         */
        public static final String BOLD_WEIGHT = "700";

        /**
         * Font weight constant for normal (regular) text.
         * Corresponds to CSS font-weight: 400.
         */
        public static final String NORMAL_WEIGHT = "400";

        /**
         * Default font size in points when an invalid size (< 1) is provided.
         */
        private static final int DEFAULT_FONT_SIZE_PT = 12;

        /**
         * The font family name used for all text styles created by this factory.
         */
        private final String fontFamilyName;

        /**
         * Creates a new TextStyleFactory for the specified font family.
         * All text styles created by this factory will use this font family.
         *
         * @param fontFamilyName the name of the font family (e.g., "Roboto", "Arial");
         *                       must not be {@code null} or empty
         * @throws IllegalArgumentException if fontFamilyName is {@code null} or empty
         */
        public TextStyleFactory(String fontFamilyName) {
            if (fontFamilyName == null || fontFamilyName.trim().isEmpty()) {
                throw new IllegalArgumentException("Font family name cannot be null or empty");
            }
            this.fontFamilyName = fontFamilyName;
        }

        /**
         * Creates a bold text style with normal (upright) appearance.
         *
         * <p><b>Properties:</b></p>
         * <ul>
         *   <li>Weight: 700 (bold)</li>
         *   <li>Style: normal</li>
         * </ul>
         *
         * @param name     the unique identifier for this style (e.g., "heading1");
         *                 must not be {@code null} or empty
         * @param fontSize the font size in points; if less than 1, defaults to
         *                 {@value #DEFAULT_FONT_SIZE_PT}pt
         * @return a new TextStyle with bold weight and normal style
         */
        public TextStyle bold(String name, int fontSize) {
            return createTextStyle(name, fontSize, BOLD_WEIGHT, FontStyleValue.NORMAL);
        }

        /**
         * Creates a normal (regular) text style with standard weight and upright appearance.
         * This is the most common style for body text.
         *
         * <p><b>Properties:</b></p>
         * <ul>
         *   <li>Weight: 400 (normal)</li>
         *   <li>Style: normal</li>
         * </ul>
         *
         * @param name     the unique identifier for this style (e.g., "body-text");
         *                 must not be {@code null} or empty
         * @param fontSize the font size in points; if less than 1, defaults to
         *                 {@value #DEFAULT_FONT_SIZE_PT}pt
         * @return a new TextStyle with normal weight and normal style
         */
        public TextStyle normal(String name, int fontSize) {
            return createTextStyle(name, fontSize, NORMAL_WEIGHT, FontStyleValue.NORMAL);
        }

        /**
         * Creates an italic text style with normal weight.
         * Commonly used for emphasis, quotations, or foreign words.
         *
         * <p><b>Properties:</b></p>
         * <ul>
         *   <li>Weight: 400 (normal)</li>
         *   <li>Style: italic</li>
         * </ul>
         *
         * @param name     the unique identifier for this style (e.g., "emphasis");
         *                 must not be {@code null} or empty
         * @param fontSize the font size in points; if less than 1, defaults to
         *                 {@value #DEFAULT_FONT_SIZE_PT}pt
         * @return a new TextStyle with normal weight and italic style
         */
        public TextStyle italic(String name, int fontSize) {
            return createTextStyle(name, fontSize, NORMAL_WEIGHT, FontStyleValue.ITALIC);
        }

        /**
         * Creates an oblique text style with normal weight.
         * Similar to italic but uses a slanted version of the regular font.
         *
         * <p><b>Properties:</b></p>
         * <ul>
         *   <li>Weight: 400 (normal)</li>
         *   <li>Style: oblique</li>
         * </ul>
         *
         * @param name     the unique identifier for this style (e.g., "slanted");
         *                 must not be {@code null} or empty
         * @param fontSize the font size in points; if less than 1, defaults to
         *                 {@value #DEFAULT_FONT_SIZE_PT}pt
         * @return a new TextStyle with normal weight and oblique style
         */
        public TextStyle oblique(String name, int fontSize) {
            return createTextStyle(name, fontSize, NORMAL_WEIGHT, FontStyleValue.OBLIQUE);
        }

        /**
         * Creates a bold oblique text style.
         * Combines bold weight with oblique (slanted) style for strong emphasis.
         *
         * <p><b>Properties:</b></p>
         * <ul>
         *   <li>Weight: 700 (bold)</li>
         *   <li>Style: oblique</li>
         * </ul>
         *
         * @param name     the unique identifier for this style (e.g., "strong-slanted");
         *                 must not be {@code null} or empty
         * @param fontSize the font size in points; if less than 1, defaults to
         *                 {@value #DEFAULT_FONT_SIZE_PT}pt
         * @return a new TextStyle with bold weight and oblique style
         */
        public TextStyle boldOblique(String name, int fontSize) {
            return createTextStyle(name, fontSize, BOLD_WEIGHT, FontStyleValue.OBLIQUE);
        }

        /**
         * Creates a bold italic text style.
         * Combines bold weight with italic style for strong emphasis.
         *
         * <p><b>Properties:</b></p>
         * <ul>
         *   <li>Weight: 700 (bold)</li>
         *   <li>Style: italic</li>
         * </ul>
         *
         * @param name     the unique identifier for this style (e.g., "strong-emphasis");
         *                 must not be {@code null} or empty
         * @param fontSize the font size in points; if less than 1, defaults to
         *                 {@value #DEFAULT_FONT_SIZE_PT}pt
         * @return a new TextStyle with bold weight and italic style
         */
        public TextStyle boldItalic(String name, int fontSize) {
            return createTextStyle(name, fontSize, BOLD_WEIGHT, FontStyleValue.ITALIC);
        }

        /**
         * Internal helper method to create a TextStyle with validated font size.
         * This method centralizes the font size validation logic and ensures
         * consistency across all factory methods.
         *
         * @param name           the style name
         * @param fontSize       the font size in points
         * @param weight         the font weight
         * @param styleValue     the font style value enum
         * @return a new TextStyle instance
         */
        private TextStyle createTextStyle(String name, int fontSize, String weight, FontStyleValue styleValue) {
            // Validate and normalize font size
            int validatedSize = fontSize < 1 ? DEFAULT_FONT_SIZE_PT : fontSize;

            return new TextStyle(
                    name,
                    validatedSize + "pt",
                    fontFamilyName,
                    weight,
                    styleValue.toString()
            );
        }
    }
}