package de.fkkaiser.model.font;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a specific font type within a {@link FontFamily}, linking a font file
 * to its style (normal, italic, oblique) and weight (100-900 or keywords).
 *
 * <p>This is an immutable record that defines a single font variant within a family.
 * For example, within the "Arial" font family, you might have separate FontType instances
 * for Arial-Regular.ttf (normal, 400), Arial-Bold.ttf (normal, 700), and
 * Arial-Italic.ttf (italic, 400).</p>
 *
 * <p><b>Components:</b></p>
 * <ul>
 *   <li><b>path:</b> The resource path to the font file (e.g., "fonts/Arial-Bold.ttf")</li>
 *   <li><b>fontStyle:</b> The style of the font ({@link FontStyleValue#NORMAL},
 *       {@link FontStyleValue#ITALIC}, or {@link FontStyleValue#OBLIQUE})</li>
 *   <li><b>fontWeight:</b> The weight/thickness of the font (typically "400" for regular,
 *       "700" for bold, but can be any value from 100-900 in increments of 100)</li>
 * </ul>
 *
 * <p><b>Font File Requirements:</b></p>
 * The font file referenced by {@code path} should be:
 * <ul>
 *   <li>Available as a resource in the classpath (typically in src/main/resources)</li>
 *   <li>Embedded in your JAR file for distribution</li>
 *   <li>In a supported format (TrueType .ttf, OpenType .otf, or Type 1 .pfb)</li>
 * </ul>
 *
 * <p><b>Font Weight Values:</b></p>
 * Common weight values follow the CSS specification:
 * <ul>
 *   <li>100: Thin</li>
 *   <li>200: Extra Light</li>
 *   <li>300: Light</li>
 *   <li>400: Normal/Regular (default)</li>
 *   <li>500: Medium</li>
 *   <li>600: Semi Bold</li>
 *   <li>700: Bold</li>
 *   <li>800: Extra Bold</li>
 *   <li>900: Black/Heavy</li>
 * </ul>
 * Keywords like "normal" (400) and "bold" (700) are also supported.
 *
 * <p><b>JSON Representation:</b></p>
 * <pre>{@code
 * {
 *   "path": "fonts/Arial-Bold.ttf",
 *   "font-style": "normal",
 *   "font-weight": "700"
 * }
 * }</pre>
 *
 * <p><b>Usage Example 1 - Direct Construction:</b></p>
 * <pre>{@code
 * // Create font types for a complete font family
 * FontType regular = new FontType(
 *     "fonts/Roboto-Regular.ttf",
 *     FontStyleValue.NORMAL,
 *     "400"
 * );
 *
 * FontType bold = new FontType(
 *     "fonts/Roboto-Bold.ttf",
 *     FontStyleValue.NORMAL,
 *     "700"
 * );
 *
 * FontType italic = new FontType(
 *     "fonts/Roboto-Italic.ttf",
 *     FontStyleValue.ITALIC,
 *     "400"
 * );
 *
 * // Combine into a font family
 * FontFamily roboto = new FontFamily("Roboto", List.of(regular, bold, italic));
 * }</pre>
 *
 * <p><b>Usage Example 2 - Using FontVariants (Recommended):</b></p>
 * <pre>{@code
 * // Simpler syntax using the FontVariants helper
 * FontType regular = FontVariants.REGULAR.toFontType("fonts/Roboto-Regular.ttf");
 * FontType bold = FontVariants.BOLD.toFontType("fonts/Roboto-Bold.ttf");
 * FontType italic = FontVariants.ITALIC.toFontType("fonts/Roboto-Italic.ttf");
 * FontType boldItalic = FontVariants.BOLD_ITALIC.toFontType("fonts/Roboto-BoldItalic.ttf");
 *
 * FontFamily roboto = new FontFamily("Roboto",
 *     List.of(regular, bold, italic, boldItalic)
 * );
 * }</pre>
 *
 * <p><b>Validation:</b></p>
 * The compact constructor validates that:
 * <ul>
 *   <li>path is not null or empty</li>
 *   <li>fontStyle is not null</li>
 *   <li>fontWeight is not null or empty</li>
 * </ul>
 *
 * <p><b>Immutability:</b></p>
 * As a record, FontType is immutable. Once created, its values cannot be changed.
 * This ensures thread-safety and prevents accidental modification.
 *
 * @param path       the resource path to the font file (e.g., "fonts/Arial-Bold.ttf");
 *                   must not be {@code null} or empty
 * @param fontStyle  the style of the font (NORMAL, ITALIC, or OBLIQUE);
 *                   must not be {@code null}
 * @param fontWeight the weight of the font as a string (e.g., "400", "700", "bold");
 *                   must not be {@code null} or empty
 *
 * @author FK Kaiser
 * @version 1.0
 * @see FontFamily
 * @see FontStyleValue
 * @see FontVariants
 */
public record FontType(
        @JsonProperty("path") String path,
        @JsonProperty("font-style") FontStyleValue fontStyle,
        @JsonProperty("font-weight") String fontWeight
) {

    /**
     * Compact constructor that validates the font type parameters.
     * This constructor is automatically called whenever a FontType is created,
     * ensuring that all instances are valid.
     *
     * @throws IllegalArgumentException if any parameter is null or empty
     */
    public FontType {
        if (path == null || path.trim().isEmpty()) {
            throw new IllegalArgumentException("Font path cannot be null or empty");
        }
        if (fontStyle == null) {
            throw new IllegalArgumentException("Font style cannot be null");
        }
        if (fontWeight == null || fontWeight.trim().isEmpty()) {
            throw new IllegalArgumentException("Font weight cannot be null or empty");
        }
    }

    /**
     * Creates a FontType with regular (normal) style and weight 400.
     * This is a convenience factory method for the most common font variant.
     *
     * @param path the resource path to the font file; must not be {@code null} or empty
     * @return a new FontType with normal style and weight 400
     */
    public static FontType regular(String path) {
        return new FontType(path, FontStyleValue.NORMAL, "400");
    }

    /**
     * Creates a FontType with normal style and bold weight (700).
     * This is a convenience factory method for bold font variants.
     *
     * @param path the resource path to the font file; must not be {@code null} or empty
     * @return a new FontType with normal style and weight 700
     */
    public static FontType bold(String path) {
        return new FontType(path, FontStyleValue.NORMAL, "700");
    }

    /**
     * Creates a FontType with italic style and weight 400.
     * This is a convenience factory method for italic font variants.
     *
     * @param path the resource path to the font file; must not be {@code null} or empty
     * @return a new FontType with italic style and weight 400
     */
    public static FontType italic(String path) {
        return new FontType(path, FontStyleValue.ITALIC, "400");
    }

    /**
     * Creates a FontType with italic style and bold weight (700).
     * This is a convenience factory method for bold italic font variants.
     *
     * @param path the resource path to the font file; must not be {@code null} or empty
     * @return a new FontType with italic style and weight 700
     */
    public static FontType boldItalic(String path) {
        return new FontType(path, FontStyleValue.ITALIC, "700");
    }
}