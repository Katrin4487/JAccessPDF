package de.fkkaiser.model.font;

import de.fkkaiser.model.annotation.PublicAPI;

import java.util.Objects;

/**
 * Predefined font variants that combine font style and weight into convenient constants.
 * This enum provides a simple way to specify common font variations without needing to
 * remember the exact style and weight combinations required by Apache FOP.
 *
 * <p><b>Purpose:</b></p>
 * When configuring fonts for PDF generation, Apache FOP requires specific style (normal, italic, oblique)
 * and weight (400, 700, etc.) values. This enum encapsulates these common combinations, making
 * font configuration more intuitive and less error-prone for library users.
 *
 * <p><b>Font Variants:</b></p>
 * <table>
 *   <caption>Font Variants Overview</caption>
 *   <tr>
 *     <th>Variant</th>
 *     <th>Style</th>
 *     <th>Weight</th>
 *     <th>Description</th>
 *   </tr>
 *   <tr>
 *     <td>REGULAR</td>
 *     <td>normal</td>
 *     <td>400</td>
 *     <td>Standard font appearance</td>
 *   </tr>
 *   <tr>
 *     <td>BOLD</td>
 *     <td>normal</td>
 *     <td>700</td>
 *     <td>Bold weight, upright style</td>
 *   </tr>
 *   <tr>
 *     <td>ITALIC</td>
 *     <td>italic</td>
 *     <td>400</td>
 *     <td>Italic style, regular weight</td>
 *   </tr>
 *   <tr>
 *     <td>OBLIQUE</td>
 *     <td>oblique</td>
 *     <td>400</td>
 *     <td>Oblique (slanted) style, regular weight</td>
 *   </tr>
 *   <tr>
 *     <td>BOLD_ITALIC</td>
 *     <td>italic</td>
 *     <td>700</td>
 *     <td>Bold weight with italic style</td>
 *   </tr>
 *   <tr>
 *     <td>BOLD_OBLIQUE</td>
 *     <td>oblique</td>
 *     <td>700</td>
 *     <td>Bold weight with oblique style</td>
 *   </tr>
 * </table>
 *
 * <p><b>Usage Example 1 - Basic Usage:</b></p>
 * <pre>{@code
 * // Get style and weight separately
 * FontStyleValue style = FontVariants.BOLD.fontStyleValue();  // NORMAL
 * String weight = FontVariants.BOLD.fontWeight();              // "700"
 *
 * // Create a FontType manually
 * FontType boldFont = new FontType(
 *     "fonts/Arial-Bold.ttf",
 *     FontVariants.BOLD.fontStyleValue(),
 *     FontVariants.BOLD.fontWeight()
 * );
 * }</pre>
 *
 * <p><b>Usage Example 2 - Convenience Method:</b></p>
 * <pre>{@code
 * // Use the convenience method for cleaner code
 * FontType regular = FontVariants.REGULAR.toFontType("fonts/Arial-Regular.ttf");
 * FontType bold = FontVariants.BOLD.toFontType("fonts/Arial-Bold.ttf");
 * FontType italic = FontVariants.ITALIC.toFontType("fonts/Arial-Italic.ttf");
 * FontType boldItalic = FontVariants.BOLD_ITALIC.toFontType("fonts/Arial-BoldItalic.ttf");
 *
 * // Build a complete font family
 * FontFamily arial = new FontFamily("Arial",
 *     List.of(regular, bold, italic, boldItalic)
 * );
 * }</pre>
 *
 * <p><b>Note on Italic vs. Oblique:</b></p>
 * While italic and oblique are both slanted text styles:
 * <ul>
 *   <li><b>Italic:</b> Typically uses specially designed glyphs with a cursive appearance</li>
 *   <li><b>Oblique:</b> Usually a mechanically slanted version of the regular font</li>
 * </ul>
 * Most fonts use italic, but some (especially sans-serif fonts) may use oblique instead.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 * @see FontType
 * @see FontStyleValue
 * @see FontFamily
 */
@PublicAPI
public enum FontVariants {

    /**
     * Regular font variant with normal style and standard weight.
     * This is the default, unmodified font appearance.
     * <ul>
     *   <li>Style: normal</li>
     *   <li>Weight: 400</li>
     * </ul>
     */
    REGULAR,

    /**
     * Bold font variant with normal style and heavy weight.
     * Used for emphasis or headings.
     * <ul>
     *   <li>Style: normal</li>
     *   <li>Weight: 700</li>
     * </ul>
     */
    BOLD,

    /**
     * Italic font variant with italic style and standard weight.
     * Commonly used for emphasis, quotations, or foreign words.
     * <ul>
     *   <li>Style: italic</li>
     *   <li>Weight: 400</li>
     * </ul>
     */
    ITALIC,

    /**
     * Oblique font variant with oblique style and standard weight.
     * Similar to italic but typically a slanted version of the regular font.
     * <ul>
     *   <li>Style: oblique</li>
     *   <li>Weight: 400</li>
     * </ul>
     */
    OBLIQUE,

    /**
     * Bold italic font variant combining heavy weight with italic style.
     * Used for strong emphasis.
     * <ul>
     *   <li>Style: italic</li>
     *   <li>Weight: 700</li>
     * </ul>
     */
    BOLD_ITALIC,

    /**
     * Bold oblique font variant combining heavy weight with oblique style.
     * Used for strong emphasis with slanted appearance.
     * <ul>
     *   <li>Style: oblique</li>
     *   <li>Weight: 700</li>
     * </ul>
     */
    BOLD_OBLIQUE;

    /**
     * Returns the font style value for this variant.
     * The style determines the slant and appearance of the font.
     *
     * <p><b>Mapping:</b></p>
     * <ul>
     *   <li>REGULAR, BOLD → {@link FontStyleValue#NORMAL}</li>
     *   <li>ITALIC, BOLD_ITALIC → {@link FontStyleValue#ITALIC}</li>
     *   <li>OBLIQUE, BOLD_OBLIQUE → {@link FontStyleValue#OBLIQUE}</li>
     * </ul>
     *
     * @return the font style value (NORMAL, ITALIC, or OBLIQUE) for this variant
     */
    @PublicAPI
    public FontStyleValue fontStyleValue() {
        return switch (this) {
            case BOLD_ITALIC, ITALIC -> FontStyleValue.ITALIC;
            case BOLD_OBLIQUE, OBLIQUE -> FontStyleValue.OBLIQUE;
            default -> FontStyleValue.NORMAL;
        };
    }

    /**
     * Returns the font weight for this variant as a string.
     * The weight determines how thick or bold the font appears.
     *
     * <p><b>Font Weight Values:</b></p>
     * <ul>
     *   <li><b>400:</b> Regular/normal weight (REGULAR, ITALIC, OBLIQUE)</li>
     *   <li><b>700:</b> Bold weight (BOLD, BOLD_ITALIC, BOLD_OBLIQUE)</li>
     * </ul>
     *
     * <p><b>Note:</b> The values follow the CSS font-weight specification, where
     * 400 represents normal weight and 700 represents bold weight. Apache FOP
     * requires these as string values.</p>
     *
     * @return the font weight as a string ("400" for regular, "700" for bold)
     */
    @PublicAPI
    public String fontWeight() {
        return switch (this) {
            case BOLD, BOLD_ITALIC, BOLD_OBLIQUE -> "700";
            default -> "400";
        };
    }

    /**
     * Convenience method to create a {@link FontType} with this variant's style and weight.
     * This method simplifies font type creation by automatically applying the correct
     * style and weight values for this variant.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * // Instead of:
     * FontType bold = new FontType(
     *     "fonts/Arial-Bold.ttf",
     *     FontVariants.BOLD.fontStyleValue(),
     *     FontVariants.BOLD.fontWeight()
     * );
     *
     * // You can simply write:
     * FontType bold = FontVariants.BOLD.toFontType("fonts/Arial-Bold.ttf");
     * }</pre>
     *
     * @param fontFilePath the path to the font file (e.g., "fonts/Arial-Bold.ttf");
     *                     must not be {@code null}
     * @return a new {@link FontType} instance with the correct style and weight for this variant
     * @throws NullPointerException if fontFilePath is {@code null}
     */
    @PublicAPI
    public FontType toFontType(String fontFilePath) {
        Objects.requireNonNull(fontFilePath, "Font file path must no be null");
        return new FontType(fontFilePath, fontStyleValue(), fontWeight());
    }
}