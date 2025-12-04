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

import java.util.ArrayList;
import java.util.List;

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
    public static void appendTextStyleTags(StringBuilder builder, TextStyle ts) {
        if (ts.fontFamilyName() != null) {
            builder.append(" font-family=\"").append(GenerateUtils.escapeXml(ts.fontFamilyName())).append("\"");
        }
        if (ts.fontSize() != null) {
            builder.append(" font-size=\"").append(GenerateUtils.escapeXml(ts.fontSize())).append("\"");
        }
        if (ts.fontWeight() != null) {
            builder.append(" font-weight=\"").append(GenerateUtils.escapeXml(ts.fontWeight())).append("\"");
        }
        if (ts.fontStyle() != null) {
            builder.append(" font-style=\"").append(GenerateUtils.escapeXml(ts.fontStyle().toLowerCase())).append("\"");
        }
    }

    static TagBuilder tagBuilder(String tagName) {
        return new TagBuilder(tagName);
    }

    @Internal
    static class TagBuilder {
        private final StringBuilder builder;
        private String attributes = "";
        private final String tagName;
        private final List<Object> contentParts = new ArrayList<>();
        private String tagPrefix = "fo:";

        public TagBuilder(String tagName) {
            this.builder = new StringBuilder();
            this.tagName = tagName;
        }

        public TagBuilder addAttribute(String name, String value) {
            if (value != null) {
                attributes += GenerateConst.SPACE
                        + name + GenerateConst.EQUALS
                        + GenerateConst.GQQ
                        + GenerateUtils.escapeXml(value)
                        + GenerateConst.GQQ;
            }
            return this;
        }

        public TagBuilder addContent(String content) {
            contentParts.add(GenerateUtils.escapeXml(content));
            return this;
        }

        public TagBuilder addNestedContent(String content) {
            contentParts.add(content);
            return this;
        }

        public TagBuilder addChild(TagBuilder childBuilder) {
            contentParts.add(childBuilder);
            return this;
        }

        public TagBuilder withPrefix(String tagPrefix) {
            this.tagPrefix = tagPrefix;
            return this;
        }


        public String build() {
            builder.append(GenerateConst.OPENER_OPEN_TAG)
                    .append(tagPrefix)
                    .append(tagName);
            if (!attributes.isBlank()) {
                builder.append(attributes);
            }
            builder.append(GenerateConst.CLOSER);


            for (Object part : contentParts) {
                if (part instanceof TagBuilder) {
                    builder.append(((TagBuilder) part).build());
                } else {
                    builder.append(part.toString());
                }
            }

            builder.append(GenerateConst.OPENER_CLOSE_TAG)
                    .append(tagPrefix)
                    .append(tagName)
                    .append(GenerateConst.CLOSER);

            return builder.toString();
        }

        public void buildInto(StringBuilder target) {
            target.append(build());
        }
    }
}
