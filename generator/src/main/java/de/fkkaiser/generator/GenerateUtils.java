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
package de.fkkaiser.generator;

import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.style.TextStyle;

/**
 * Different Util methods to generate FOP XML.
 *
 * @author Katrin Kaiser
 * @version 1.1.0
 *
 */
@Internal
public class GenerateUtils {

    /**
     * Escapes special XML characters in the given text.
     * <p>
     * This method replaces the following characters with their corresponding XML entities:
     * - `&` with `&amp;`
     * - `<` with `&lt;`
     * - `>` with `&gt;`
     * - `"` with `&quot;`
     * - `'` with `&apos;`
     *
     * @param text the input string to escape; if null, an empty string is returned
     * @return the escaped string, or an empty string if the input is null
     */
    @Internal
    public static String escapeXml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    /**
     * Appends XML tags for font declaration based on the provided `TextStyle` object.
     * <p>
     * This method generates and appends XML attributes for font-family, font-size, font-weight,
     * and font-style to the given `StringBuilder`. Each attribute is escaped using `escapeXml`
     * to ensure XML safety.
     *
     * @param builder the `StringBuilder` to append the tags to
     * @param ts      the `TextStyle` object containing font information; attributes are appended only if not null
     */
    @Internal
    public static void appendTextStyleTags(TagBuilder builder, TextStyle ts) {
        if (ts.fontFamilyName() != null) {
            builder.addAttribute(GenerateConst.FONT_FAMILY, ts.fontFamilyName());
        }
        if (ts.fontSize() != null) {
            builder.addAttribute(GenerateConst.FONT_SIZE, ts.fontSize());
             }
        if (ts.fontWeight() != null) {
            builder.addAttribute(GenerateConst.FONT_WEIGHT, ts.fontWeight());
             }
        if (ts.fontStyle() != null) {
            builder.addAttribute(GenerateConst.FONT_STYLE, ts.fontStyle());
             }
    }

    public static TagBuilder tagBuilder(String tagName) {
        return new TagBuilder(tagName);
    }

}
