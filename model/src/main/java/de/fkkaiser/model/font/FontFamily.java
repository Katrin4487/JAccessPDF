package de.fkkaiser.model.font;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents a font family, which is a collection of related {@link FontType} instances
 * that share the same base typeface but differ in style (italic) and weight (bold).
 *
 * <p>A font family groups together all variants of a typeface under a single name.
 * For example, the "Arial" font family might include:</p>
 * <ul>
 *   <li>Arial Regular (normal style, weight 400)</li>
 *   <li>Arial Bold (normal style, weight 700)</li>
 *   <li>Arial Italic (italic style, weight 400)</li>
 *   <li>Arial Bold Italic (italic style, weight 700)</li>
 * </ul>
 *
 * <p><b>Purpose in PDF Generation:</b></p>
 * Apache FOP uses font families to match styled text in documents to the correct font files.
 * When text is marked as bold or italic, FOP looks for a font type within the family
 * that matches the requested style and weight.
 *
 * <p><b>Font Family Name:</b></p>
 * The font family name should match the name used in your document's style definitions.
 * This allows the style resolver to correctly link text styles to font files during
 * PDF generation.
 *
 * <p><b>JSON Representation:</b></p>
 * <pre>{@code
 * {
 *   "font-family": "Roboto",
 *   "types": [
 *     {
 *       "path": "fonts/Roboto-Regular.ttf",
 *       "font-style": "normal",
 *       "font-weight": "400"
 *     },
 *     {
 *       "path": "fonts/Roboto-Bold.ttf",
 *       "font-style": "normal",
 *       "font-weight": "700"
 *     }
 *   ]
 * }
 * }</pre>
 *
 * <p><b>Usage Example 1 - Direct Construction:</b></p>
 * <pre>{@code
 * // Create font types
 * FontType regular = new FontType("fonts/Roboto-Regular.ttf", FontStyleValue.NORMAL, "400");
 * FontType bold = new FontType("fonts/Roboto-Bold.ttf", FontStyleValue.NORMAL, "700");
 * FontType italic = new FontType("fonts/Roboto-Italic.ttf", FontStyleValue.ITALIC, "400");
 *
 * // Create font family
 * FontFamily roboto = new FontFamily("Roboto", List.of(regular, bold, italic));
 * }</pre>
 *
 * <p><b>Usage Example 2 - Using FontVariants:</b></p>
 * <pre>{@code
 * FontFamily arial = new FontFamily("Arial", List.of(
 *     FontVariants.REGULAR.toFontType("fonts/Arial-Regular.ttf"),
 *     FontVariants.BOLD.toFontType("fonts/Arial-Bold.ttf"),
 *     FontVariants.ITALIC.toFontType("fonts/Arial-Italic.ttf"),
 *     FontVariants.BOLD_ITALIC.toFontType("fonts/Arial-BoldItalic.ttf")
 * ));
 * }</pre>
 *
 * <p><b>Usage Example 3 - Factory Methods:</b></p>
 * <pre>{@code
 * // Quick single-font family for simple documents
 * FontFamily simple = FontFamily.withSingleFont("MyFont", "fonts/MyFont.ttf");
 *
 * // Standard four-variant family (regular, bold, italic, bold-italic)
 * FontFamily complete = FontFamily.withStandardVariants(
 *     "Roboto",
 *     "fonts/Roboto-Regular.ttf",
 *     "fonts/Roboto-Bold.ttf",
 *     "fonts/Roboto-Italic.ttf",
 *     "fonts/Roboto-BoldItalic.ttf"
 * );
 * }</pre>
 *
 * <p><b>Validation:</b></p>
 * The compact constructor validates that:
 * <ul>
 *   <li>fontFamily (name) is not null or empty</li>
 *   <li>fontTypes list is not null and contains at least one font type</li>
 * </ul>
 *
 * <p><b>Immutability:</b></p>
 * As a record, FontFamily is immutable. Once created, its values cannot be changed.
 * This ensures thread-safety and prevents accidental modification.
 *
 * @param fontFamily the name of the font family (e.g., "Arial", "Roboto");
 *                   must not be {@code null} or empty
 * @param fontTypes  the list of font types belonging to this family;
 *                   must not be {@code null} and must contain at least one font type
 *
 * @author FK Kaiser
 * @version 1.0
 * @see FontType
 * @see FontFamilyList
 * @see FontVariants
 */
public record FontFamily(
        @JsonProperty("font-family") String fontFamily,
        @JsonProperty("types") List<FontType> fontTypes
) {

    /**
     * Compact constructor that validates the font family parameters.
     * This constructor is automatically called whenever a FontFamily is created,
     * ensuring that all instances are valid.
     *
     * @throws IllegalArgumentException if fontFamily is null/empty or if fontTypes is null/empty
     */
    public FontFamily {
        if (fontFamily == null || fontFamily.trim().isEmpty()) {
            throw new IllegalArgumentException("Font family name cannot be null or empty");
        }
        if (fontTypes == null || fontTypes.isEmpty()) {
            throw new IllegalArgumentException("Font types list cannot be null or empty");
        }
    }

    /**
     * Returns the name of this font family.
     * This method provides a more intuitive accessor name compared to the
     * auto-generated {@code fontFamily()} method from the record.
     *
     * <p><b>Note:</b> This method is equivalent to calling {@code fontFamily()}
     * and is provided for improved readability in client code.</p>
     *
     * @return the font family name (e.g., "Arial", "Roboto")
     */
    public String getName() {
        return fontFamily;
    }

    /**
     * Creates a simple font family with a single regular font variant.
     * This is useful for basic documents that don't require bold or italic text,
     * or as a fallback when other variants are not available.
     *
     * <p>The created font type will have:</p>
     * <ul>
     *   <li>Style: {@link FontStyleValue#NORMAL}</li>
     *   <li>Weight: "400" (regular)</li>
     * </ul>
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * FontFamily simple = FontFamily.withSingleFont(
     *     "MyCustomFont",
     *     "fonts/MyCustomFont.ttf"
     * );
     * }</pre>
     *
     * @param familyName the name of the font family; must not be {@code null} or empty
     * @param fontPath   the resource path to the font file; must not be {@code null} or empty
     * @return a new FontFamily with a single regular font type
     * @throws IllegalArgumentException if any parameter is null or empty
     */
    @SuppressWarnings("unused")
    public static FontFamily withSingleFont(String familyName, String fontPath) {
        FontType regular = FontType.regular(fontPath);
        return new FontFamily(familyName, List.of(regular));
    }

    /**
     * Creates a font family with the four standard variants: regular, bold, italic, and bold italic.
     * This is the most common configuration for professional documents that require various
     * text styles.
     *
     * <p>The created font family will include:</p>
     * <ul>
     *   <li>Regular: normal style, weight 400</li>
     *   <li>Bold: normal style, weight 700</li>
     *   <li>Italic: italic style, weight 400</li>
     *   <li>Bold Italic: italic style, weight 700</li>
     * </ul>
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * FontFamily roboto = FontFamily.withStandardVariants(
     *     "Roboto",
     *     "fonts/Roboto-Regular.ttf",
     *     "fonts/Roboto-Bold.ttf",
     *     "fonts/Roboto-Italic.ttf",
     *     "fonts/Roboto-BoldItalic.ttf"
     * );
     * }</pre>
     *
     * @param familyName     the name of the font family; must not be {@code null} or empty
     * @param regularPath    the path to the regular font file; must not be {@code null} or empty
     * @param boldPath       the path to the bold font file; must not be {@code null} or empty
     * @param italicPath     the path to the italic font file; must not be {@code null} or empty
     * @param boldItalicPath the path to the bold italic font file; must not be {@code null} or empty
     * @return a new FontFamily with four standard font variants
     * @throws IllegalArgumentException if any parameter is null or empty
     */
    @SuppressWarnings("unused")
    public static FontFamily withStandardVariants(
            String familyName,
            String regularPath,
            String boldPath,
            String italicPath,
            String boldItalicPath) {

        return new FontFamily(familyName, List.of(
                FontType.regular(regularPath),
                FontType.bold(boldPath),
                FontType.italic(italicPath),
                FontType.boldItalic(boldItalicPath)
        ));
    }

    /**
     * Creates a font family with regular and bold variants only.
     * This is useful for documents that don't use italic text, or when
     * italic variants are not available.
     *
     * <p>The created font family will include:</p>
     * <ul>
     *   <li>Regular: normal style, weight 400</li>
     *   <li>Bold: normal style, weight 700</li>
     * </ul>
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * FontFamily arial = FontFamily.withBoldVariant(
     *     "Arial",
     *     "fonts/Arial-Regular.ttf",
     *     "fonts/Arial-Bold.ttf"
     * );
     * }</pre>
     *
     * @param familyName  the name of the font family; must not be {@code null} or empty
     * @param regularPath the path to the regular font file; must not be {@code null} or empty
     * @param boldPath    the path to the bold font file; must not be {@code null} or empty
     * @return a new FontFamily with regular and bold font variants
     * @throws IllegalArgumentException if any parameter is null or empty
     */
    @SuppressWarnings("unused")
    public static FontFamily withBoldVariant(
            String familyName,
            String regularPath,
            String boldPath) {

        return new FontFamily(familyName, List.of(
                FontType.regular(regularPath),
                FontType.bold(boldPath)
        ));
    }
}