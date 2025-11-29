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
package de.fkkaiser.model.font;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enumeration representing the three standard font style values defined in CSS and XSL-FO.
 * The font style determines whether text appears upright (normal), slanted with specially
 * designed glyphs (italic), or mechanically slanted (oblique).
 *
 * <p><b>Font Style Definitions:</b></p>
 * <ul>
 *   <li><b>Normal:</b> The default, upright font appearance with no slant</li>
 *   <li><b>Italic:</b> A slanted style using specially designed cursive glyphs.
 *       Most serif fonts (like Times New Roman) have dedicated italic versions.</li>
 *   <li><b>Oblique:</b> A slanted style created by mechanically skewing the regular font.
 *       More common in sans-serif fonts (like Arial or Helvetica).</li>
 * </ul>
 *
 * <p><b>Italic vs. Oblique:</b></p>
 * While both appear slanted, there's an important distinction:
 * <ul>
 *   <li><b>Italic:</b> Uses specially designed glyphs with different letter shapes (e.g., 'a' becomes 'a')</li>
 *   <li><b>Oblique:</b> Simply slants the regular letterforms without redesigning them</li>
 * </ul>
 *
 * <p><b>Usage in Apache FOP:</b></p>
 * These values map directly to the {@code font-style} property in XSL-FO, which is used
 * by Apache FOP for PDF generation. The enum values are serialized to their lowercase
 * string representations ("normal", "italic", "oblique") when used in font configuration.
 *
 * <p><b>JSON Serialization:</b></p>
 * This enum uses Jackson annotations for JSON serialization:
 * <pre>{@code
 * {
 *   "font-style": "italic"
 * }
 * }</pre>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * // Create a font type with italic style
 * FontType italic = new FontType(
 *     "fonts/Arial-Italic.ttf",
 *     FontStyleValue.ITALIC,
 *     "400"
 * );
 *
 * // Or use the convenience enum
 * FontType italic2 = FontVariants.ITALIC.toFontType("fonts/Arial-Italic.ttf");
 *
 * // Get string representation for FOP
 * String styleString = FontStyleValue.ITALIC.toString();  // "italic"
 * }</pre>
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 * @see FontType
 * @see FontVariants
 */
public enum FontStyleValue {

    /**
     * Normal (upright) font style with no slant.
     * This is the default font appearance and the most commonly used style.
     *
     * <p><b>XSL-FO value:</b> "normal"</p>
     */
    @JsonProperty("normal")
    NORMAL,

    /**
     * Italic font style with slanted text using specially designed glyphs.
     * Italic fonts typically have cursive characteristics and are commonly used
     * for emphasis, quotations, foreign words, or titles.
     *
     * <p><b>XSL-FO value:</b> "italic"</p>
     *
     * <p><b>Note:</b> Requires a dedicated italic font file. If not available,
     * some rendering engines may simulate italic by slanting the regular font,
     * though this is not recommended for high-quality output.</p>
     */
    @JsonProperty("italic")
    ITALIC,

    /**
     * Oblique font style with mechanically slanted text.
     * Unlike italic, oblique uses the same letterforms as the regular font,
     * just slanted at an angle (typically 12-15 degrees).
     *
     * <p><b>XSL-FO value:</b> "oblique"</p>
     *
     * <p><b>Note:</b> Less common than italic. Some sans-serif fonts prefer
     * oblique over italic for their slanted variants.</p>
     */
    @JsonProperty("oblique")
    OBLIQUE;

    /**
     * Returns the lowercase string representation of this font style value.
     * This representation is used in XSL-FO documents and font configuration
     * for Apache FOP.
     *
     * <p><b>Return Values:</b></p>
     * <ul>
     *   <li>NORMAL → "normal"</li>
     *   <li>ITALIC → "italic"</li>
     *   <li>OBLIQUE → "oblique"</li>
     * </ul>
     *
     * @return the lowercase string name of this font style ("normal", "italic", or "oblique")
     */
    @Override
    public String toString() {
        return switch (this) {
            case NORMAL -> "normal";
            case ITALIC -> "italic";
            case OBLIQUE -> "oblique";
        };
    }
}