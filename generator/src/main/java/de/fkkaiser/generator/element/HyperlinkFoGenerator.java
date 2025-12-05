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
package de.fkkaiser.generator.element;

import de.fkkaiser.generator.GenerateConst;
import de.fkkaiser.generator.GenerateUtils;
import de.fkkaiser.generator.TagBuilder;
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.structure.Hyperlink;
import de.fkkaiser.model.structure.InlineElement;
import de.fkkaiser.model.style.StyleSheet;
import de.fkkaiser.model.style.TextRunStyleProperties;
import de.fkkaiser.model.style.TextStyle;

import java.util.Optional;

/**
 * Generator for Hyperlinks
 *
 * @author Katrin Kaiser
 * @version 1.1.1
 */
@Internal
public class HyperlinkFoGenerator extends InlineElementFoGenerator {

    private static final String BASIC_LINK_TAG = "basic-link";
    @Override
    public void generate(InlineElement element, StyleSheet styleSheet, StringBuilder builder) {
        Hyperlink link = (Hyperlink) element;

        TagBuilder linkBuilder = GenerateUtils.tagBuilder(BASIC_LINK_TAG)
                .addAttribute(GenerateConst.EXTERNAL_DESTINATION, link.getHref())
                .addAttribute(GenerateConst.ALT_TEXT, link.getAltText());

        TextRunStyleProperties style = link.getResolvedStyle();
        if (style != null) {
            // Font style from TextStyle
            Optional<TextStyle> fontStyleOpt = styleSheet.findFontStyleByName(style.getTextStyleName());
            fontStyleOpt.ifPresent(fontStyle -> linkBuilder
                    .addAttribute(GenerateConst.FONT_FAMILY, fontStyle.fontFamilyName())
                    .addAttribute(GenerateConst.FONT_SIZE, fontStyle.fontSize())
                    .addAttribute(GenerateConst.FONT_WEIGHT, fontStyle.fontWeight())
                    .addAttribute(GenerateConst.FONT_STYLE, fontStyle.fontStyle()));

            // Text-specific styling
            linkBuilder
                    .addAttribute(GenerateConst.COLOR, style.getTextColor())
                    .addAttribute(GenerateConst.BACKGROUND_COLOR, style.getBackgroundColor())
                    .addAttribute(GenerateConst.TEXT_DECORATION, style.getTextDecoration())
                    .addAttribute(GenerateConst.BASELINE_SHIFT, style.getBaselineShift());
        }

        linkBuilder.addContent(link.getText());
        linkBuilder.buildInto(builder);
    }
}