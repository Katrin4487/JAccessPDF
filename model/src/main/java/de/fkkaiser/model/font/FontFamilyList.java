package de.fkkaiser.model.font;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Container class for a list of {@link FontFamily} objects to be used in PDF generation.
 * This class manages all font families that should be registered and available for use
 * in the generated PDF document.
 *
 * <p><b>Purpose:</b></p>
 * The FontFamilyList serves as the top-level configuration for fonts in the PDF generation
 * pipeline. It is passed to the de.fkkaiser.api.PdfGenerationFacade to register
 * all required fonts with Apache FOP before document rendering begins.
 *
 * <p><b>JSON Representation:</b></p>
 * <pre>{@code
 * {
 *   "font-families": [
 *     {
 *       "font-family": "Roboto",
 *       "types": [
 *         {"path": "fonts/Roboto-Regular.ttf", "font-style": "normal", "font-weight": "400"},
 *         {"path": "fonts/Roboto-Bold.ttf", "font-style": "normal", "font-weight": "700"}
 *       ]
 *     },
 *     {
 *       "font-family": "Open Sans",
 *       "types": [
 *         {"path": "fonts/OpenSans-Regular.ttf", "font-style": "normal", "font-weight": "400"}
 *       ]
 *     }
 *   ]
 * }
 * }</pre>
 *
 * <p><b>Usage Example 1 - Direct Construction:</b></p>
 * <pre>{@code
 * // Create font families
 * FontFamily roboto = FontFamily.withStandardVariants(
 *     "Roboto",
 *     "fonts/Roboto-Regular.ttf",
 *     "fonts/Roboto-Bold.ttf",
 *     "fonts/Roboto-Italic.ttf",
 *     "fonts/Roboto-BoldItalic.ttf"
 * );
 *
 * FontFamily openSans = FontFamily.withSingleFont(
 *     "Open Sans",
 *     "fonts/OpenSans-Regular.ttf"
 * );
 *
 * // Create font list
 * FontFamilyList fontList = new FontFamilyList();
 * fontList.setFontFamilyList(List.of(roboto, openSans));
 * }</pre>
 *
 * <p><b>Usage Example 2 - Using Builder (Recommended):</b></p>
 * <pre>{@code
 * // Fluent builder API for complex font configurations
 * FontFamilyList fontList = FontFamilyList.builder()
 *     .addFontFamily("Roboto")
 *         .addFont("fonts/Roboto-Regular.ttf", FontStyleValue.NORMAL, "400")
 *         .addFont("fonts/Roboto-Bold.ttf", FontStyleValue.NORMAL, "700")
 *         .addFont("fonts/Roboto-Italic.ttf", FontStyleValue.ITALIC, "400")
 *         .addFont("fonts/Roboto-BoldItalic.ttf", FontStyleValue.ITALIC, "700")
 *         .endFontFamily()
 *     .addFontFamily("Open Sans")
 *         .addFont("fonts/OpenSans-Regular.ttf", FontStyleValue.NORMAL, "400")
 *         .endFontFamily()
 *     .build();
 * }</pre>
 *
 * <p><b>Usage Example 3 - Simple Convenience Methods:</b></p>
 * <pre>{@code
 * FontFamilyList fontList = new FontFamilyList();
 *
 * // Add complete font families directly
 * fontList.addFontFamily(robotoFamily);
 * fontList.addFontFamily(openSansFamily);
 *
 * // Or create an empty list (will use default font)
 * FontFamilyList emptyList = new FontFamilyList();
 * }</pre>
 *
 * <p><b>Default Font Behavior:</b></p>
 * If a FontFamilyList is null or empty when passed to the PDF generation facade,
 * a default font (Open Sans Regular) will be automatically added to ensure PDF
 * generation can proceed successfully.
 *
 * <p><b>Thread Safety:</b></p>
 * This class is not thread-safe. If shared across threads, external synchronization
 * is required. However, once the internal list is set and the object is passed to
 * the PDF generator, it should not be modified.
 *
 * @author FK Kaiser
 * @version 1.1
 * @see FontFamily
 * @see FontType
 * @see FontFamilyListBuilder
 */
public class FontFamilyList {

    private static final Logger log = LoggerFactory.getLogger(FontFamilyList.class);

    @JsonProperty("font-families")
    private List<FontFamily> fontFamilyList;

    /**
     * Default constructor that creates an empty font family list.
     * Primarily used for JSON deserialization by Jackson.
     *
     * <p>After construction, use {@link #setFontFamilyList(List)} or
     * {@link #addFontFamily(FontFamily)} to populate the list.</p>
     */
    public FontFamilyList() {
        // Empty constructor for Jackson deserialization
    }

    /**
     * Returns the list of font families.
     *
     * @return the list of {@link FontFamily} objects, or {@code null} if not set
     */
    public List<FontFamily> getFontFamilyList() {
        return fontFamilyList;
    }

    /**
     * Sets the list of font families.
     * This replaces any existing font families in the list.
     *
     * @param fontFamilyList the list of font families to set; may be {@code null}
     */
    public void setFontFamilyList(List<FontFamily> fontFamilyList) {
        this.fontFamilyList = fontFamilyList;
    }

    /**
     * Adds a single font family to the list.
     * If the internal list is null, it will be initialized.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * FontFamilyList fontList = new FontFamilyList();
     * fontList.addFontFamily(robotoFamily);
     * fontList.addFontFamily(openSansFamily);
     * }</pre>
     *
     * @param fontFamily the font family to add; must not be {@code null}
     * @throws IllegalArgumentException if fontFamily is {@code null}
     */
    @SuppressWarnings("unused")
    public void addFontFamily(FontFamily fontFamily) {
        if (fontFamily == null) {
            throw new IllegalArgumentException("Font family cannot be null");
        }

        if (this.fontFamilyList == null) {
            this.fontFamilyList = new ArrayList<>();
        }

        this.fontFamilyList.add(fontFamily);
    }

    /**
     * Checks if the font family list is empty or null.
     *
     * @return {@code true} if the list is null or contains no font families, {@code false} otherwise
     */
    public boolean isEmpty() {
        return fontFamilyList == null || fontFamilyList.isEmpty();
    }

    /**
     * Returns the number of font families in the list.
     *
     * @return the number of font families, or 0 if the list is null
     */
    public int size() {
        return fontFamilyList == null ? 0 : fontFamilyList.size();
    }

    /**
     * Returns a string representation of this font family list.
     * If the list is empty or null, returns "FontFamilyList (empty)".
     * Otherwise, returns a formatted string with all font families.
     *
     * @return a string representation of this font family list
     */
    @Override
    public String toString() {
        if (fontFamilyList == null || fontFamilyList.isEmpty()) {
            return "FontFamilyList (empty)";
        }

        StringBuilder builder = new StringBuilder("FontFamilyList [\n");
        for (int i = 0; i < fontFamilyList.size(); i++) {
            FontFamily fontFamily = fontFamilyList.get(i);
            if (fontFamily != null) {
                builder.append("  ").append(fontFamily);
                if (i < fontFamilyList.size() - 1) {
                    builder.append(",");
                }
                builder.append("\n");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    /**
     * Creates and returns a new {@link FontFamilyListBuilder} for fluent font configuration.
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * FontFamilyList fontList = FontFamilyList.builder()
     *     .addFontFamily("Arial")
     *         .addFont("fonts/Arial-Regular.ttf", FontStyleValue.NORMAL, "400")
     *         .addFont("fonts/Arial-Bold.ttf", FontStyleValue.NORMAL, "700")
     *         .endFontFamily()
     *     .build();
     * }</pre>
     *
     * @return a new builder instance
     */
    public static FontFamilyListBuilder builder() {
        return new FontFamilyListBuilder();
    }

    /**
     * Fluent builder for constructing {@link FontFamilyList} instances with multiple font families.
     * The builder provides a chainable API for adding font families and their variants,
     * making complex font configurations more readable and maintainable.
     *
     * <p><b>Builder Pattern Usage:</b></p>
     * The builder follows a hierarchical pattern:
     * <ol>
     *   <li>Call {@link #addFontFamily(String)} to start a new font family</li>
     *   <li>Call {@link #addFont(String, FontStyleValue, String)} one or more times to add font types</li>
     *   <li>Call {@link #endFontFamily()} to complete the current font family</li>
     *   <li>Repeat steps 1-3 for additional font families</li>
     *   <li>Call {@link #build()} to create the final FontFamilyList</li>
     * </ol>
     *
     * <p><b>Usage Example:</b></p>
     * <pre>{@code
     * FontFamilyList fontList = FontFamilyList.builder()
     *     // First font family
     *     .addFontFamily("Roboto")
     *         .addFont("fonts/Roboto-Regular.ttf", FontStyleValue.NORMAL, "400")
     *         .addFont("fonts/Roboto-Bold.ttf", FontStyleValue.NORMAL, "700")
     *         .addFont("fonts/Roboto-Italic.ttf", FontStyleValue.ITALIC, "400")
     *         .endFontFamily()
     *     // Second font family
     *     .addFontFamily("Open Sans")
     *         .addFont("fonts/OpenSans-Regular.ttf", FontStyleValue.NORMAL, "400")
     *         .endFontFamily()
     *     // Build final result
     *     .build();
     * }</pre>
     *
     * <p><b>Simplified Alternative with FontVariants:</b></p>
     * <pre>{@code
     * FontFamilyList fontList = FontFamilyList.builder()
     *     .addFontFamily("Roboto")
     *         .addFont("fonts/Roboto-Regular.ttf", FontVariants.REGULAR)
     *         .addFont("fonts/Roboto-Bold.ttf", FontVariants.BOLD)
     *         .addFont("fonts/Roboto-Italic.ttf", FontVariants.ITALIC)
     *         .endFontFamily()
     *     .build();
     * }</pre>
     *
     * <p><b>Error Handling:</b></p>
     * <ul>
     *   <li>If {@link #addFont(String, FontStyleValue, String)} is called before {@link #addFontFamily(String)},
     *       an error is logged and the font is skipped</li>
     *   <li>If {@link #build()} is called while a font family is in progress (not closed with
     *       {@link #endFontFamily()}), the family is automatically closed with a warning</li>
     *   <li>If {@link #addFontFamily(String)} is called while another family is in progress,
     *       the previous family is automatically closed before starting the new one</li>
     * </ul>
     *
     * <p><b>Thread Safety:</b></p>
     * This builder is not thread-safe and should only be used by a single thread.
     *
     * @see FontFamilyList
     * @see FontFamily
     * @see FontType
     * @see FontVariants
     */
    public static class FontFamilyListBuilder {

        private final List<FontFamily> fontFamilies;
        private String currentFamilyName;
        private List<FontType> currentFontTypes;

        /**
         * Package-private constructor.
         * Use {@link FontFamilyList#builder()} to create a new builder instance.
         */
        private FontFamilyListBuilder() {
            this.fontFamilies = new ArrayList<>();
        }

        /**
         * Starts a new font family with the specified name.
         *
         * <p>If a font family is already in progress (i.e., {@link #endFontFamily()} hasn't
         * been called yet), it will be automatically completed before starting the new family.</p>
         *
         * <p>After calling this method, use {@link #addFont(String, FontStyleValue, String)}
         * to add font types to this family, then call {@link #endFontFamily()} to complete it.</p>
         *
         * @param fontFamilyName the name of the font family (e.g., "Arial", "Roboto");
         *                       must not be {@code null} or empty
         * @return this builder instance for method chaining
         * @throws IllegalArgumentException if fontFamilyName is {@code null} or empty
         */
        public FontFamilyListBuilder addFontFamily(String fontFamilyName) {
            if (fontFamilyName == null || fontFamilyName.trim().isEmpty()) {
                throw new IllegalArgumentException("Font family name cannot be null or empty");
            }

            // If a family is already in progress, save it before starting a new one
            if (this.currentFamilyName != null) {
                log.debug("Auto-closing previous font family '{}' before starting new family '{}'",
                        currentFamilyName, fontFamilyName);
                endFontFamily();
            }

            this.currentFamilyName = fontFamilyName;
            this.currentFontTypes = new ArrayList<>();

            return this;
        }

        /**
         * Adds a new font type to the current font family.
         * This method must be called after {@link #addFontFamily(String)}.
         *
         * <p>You can call this method multiple times to add different variants
         * (regular, bold, italic, etc.) to the same font family.</p>
         *
         * @param path           the resource path to the font file (e.g., "fonts/Arial-Bold.ttf");
         *                       must not be {@code null} or empty
         * @param fontStyleValue the style of the font (NORMAL, ITALIC, or OBLIQUE);
         *                       must not be {@code null}
         * @param weight         the weight of the font (e.g., "400", "700");
         *                       must not be {@code null} or empty
         * @return this builder instance for method chaining
         */
        public FontFamilyListBuilder addFont(String path, FontStyleValue fontStyleValue, String weight) {
            if (currentFamilyName == null) {
                log.error("FontFamilyListBuilder: addFont called before addFontFamily. " +
                        "Skipping font with path: {}", path);
                return this;
            }

            FontType fontType = new FontType(path, fontStyleValue, weight);
            this.currentFontTypes.add(fontType);

            return this;
        }

        /**
         * Convenience method to add a font type using a {@link FontVariants} constant.
         * This simplifies the API by automatically providing the correct style and weight.
         *
         * <p><b>Usage Example:</b></p>
         * <pre>{@code
         * builder.addFontFamily("Arial")
         *     .addFont("fonts/Arial-Regular.ttf", FontVariants.REGULAR)
         *     .addFont("fonts/Arial-Bold.ttf", FontVariants.BOLD)
         *     .endFontFamily();
         * }</pre>
         *
         * @param path    the resource path to the font file; must not be {@code null} or empty
         * @param variant the font variant (REGULAR, BOLD, ITALIC, etc.); must not be {@code null}
         * @return this builder instance for method chaining
         */
        @SuppressWarnings("unused")
        public FontFamilyListBuilder addFont(String path, FontVariants variant) {
            if (variant == null) {
                throw new IllegalArgumentException("Font variant cannot be null");
            }
            return addFont(path, variant.fontStyleValue(), variant.fontWeight());
        }

        /**
         * Completes the current font family and adds it to the list.
         * After calling this method, you can start a new font family with
         * {@link #addFontFamily(String)}, or call {@link #build()} to create
         * the final FontFamilyList.
         *
         * <p>This method is optional before calling {@link #addFontFamily(String)} again,
         * as the previous family will be auto-closed. However, it's recommended for clarity.</p>
         *
         * @return this builder instance for method chaining
         */
        public FontFamilyListBuilder endFontFamily() {
            if (currentFamilyName != null && currentFontTypes != null && !currentFontTypes.isEmpty()) {
                FontFamily fontFamily = new FontFamily(
                        currentFamilyName,
                        List.copyOf(currentFontTypes)
                );
                this.fontFamilies.add(fontFamily);
                log.debug("Added font family '{}' with {} font type(s)",
                        currentFamilyName, currentFontTypes.size());
            }

            // Reset state
            this.currentFamilyName = null;
            this.currentFontTypes = null;

            return this;
        }

        /**
         * Builds and returns the final {@link FontFamilyList}.
         *
         * <p>If a font family is still in progress (not closed with {@link #endFontFamily()}),
         * it will be automatically completed before building. A warning will be logged in this case.</p>
         *
         * <p>The returned FontFamilyList contains an immutable copy of all font families
         * added through this builder.</p>
         *
         * @return a new FontFamilyList containing all added font families
         */
        public FontFamilyList build() {
            // Auto-close any font family still in progress
            if (currentFamilyName != null) {
                log.warn("FontFamilyListBuilder: build() called, but font family '{}' was not " +
                        "explicitly closed with endFontFamily(). Auto-closing.", currentFamilyName);
                endFontFamily();
            }

            FontFamilyList fontFamilyList = new FontFamilyList();
            fontFamilyList.setFontFamilyList(List.copyOf(fontFamilies));

            log.debug("Built FontFamilyList with {} font family/families", fontFamilies.size());

            return fontFamilyList;
        }
    }
}