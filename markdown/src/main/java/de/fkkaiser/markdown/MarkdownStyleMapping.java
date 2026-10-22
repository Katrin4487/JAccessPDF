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
package de.fkkaiser.markdown;

import de.fkkaiser.model.style.builder.StandardElementStyles;

/**
 * Maps Markdown inline constructs ({@code **strong**}, {@code *em*}, {@code `code`})
 * to the {@code ElementStyle} names used to render them.
 * <p>
 * {@link #defaults()} points at the {@code TEXT_RUN} styles that
 * {@link StandardElementStyles} registers for exactly this purpose. Pass a custom
 * mapping to {@link InlineMarkdownConverter} if a document uses its own StyleSheet
 * with differently named styles for bold/italic/code inline text.
 *
 * @param strongStyleName style name used for {@code **strong**} / {@code __strong__} text
 * @param emphasisStyleName style name used for {@code *em*} / {@code _em_} text
 * @param codeStyleName style name used for {@code `code`} spans
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
public record MarkdownStyleMapping(
        String strongStyleName,
        String emphasisStyleName,
        String codeStyleName
) {

    /**
     * The mapping matching {@link StandardElementStyles}'s built-in defaults.
     *
     * @return a mapping onto {@code strong-default}, {@code em-default} and {@code code-default}
     */
    public static MarkdownStyleMapping defaults() {
        return new MarkdownStyleMapping(
                StandardElementStyles.STRONG_STYLE_NAME,
                StandardElementStyles.EM_STYLE_NAME,
                StandardElementStyles.CODE_STYLE_NAME
        );
    }
}
