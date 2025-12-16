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

import de.fkkaiser.generator.*;
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.structure.BlockImage;
import de.fkkaiser.model.structure.Element;
import de.fkkaiser.model.structure.Headline;
import de.fkkaiser.model.style.BlockImageStyleProperties;
import de.fkkaiser.model.style.StyleSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Base64;
import java.util.List;

/**
 * Generator for Images
 *
 * @author Katrin Kaiser
 * @version 1.1.2
 */
@Internal
public class ImageFoGenerator extends ElementFoGenerator {
    private static final Logger log = LoggerFactory.getLogger(ImageFoGenerator.class);

     @Override
    public void generate(Element element,
                         StyleSheet styleSheet,
                         StringBuilder builder,
                         List<Headline> headlines,
                         ImageResolver imageResolver,
                         boolean isExternalArtefact) {
        BlockImage blockImage = (BlockImage) element;
        BlockImageStyleProperties style = blockImage.getResolvedStyle();

        TagBuilder blockBuilder = GenerateUtils.tagBuilder(GenerateConst.BLOCK);

        if (isExternalArtefact) {
            blockBuilder.addAttribute(GenerateConst.CONTENT_TYPE, GenerateConst.EXTERNAL_ARTIFACT);
        }

        appendBlockAttributes(blockBuilder, style, styleSheet);

        // Create the external-graphic element
        TagBuilder graphicBuilder = GenerateUtils.tagBuilder(GenerateConst.EXTERNAL_GRAPHIC);
        appendImageAttributes(graphicBuilder, style);

        // SVGs werden automatisch zu PNG konvertiert!
        String srcDataUri = ImageUtils.resolveToDataUri(blockImage.getPath(), imageResolver);
        if (srcDataUri != null) {
            graphicBuilder.addAttribute(GenerateConst.SRC, srcDataUri);

            String altText = (blockImage.getAltText() != null && !blockImage.getAltText().isEmpty())
                    ? blockImage.getAltText()
                    : "";
            graphicBuilder.addAttribute(GenerateConst.ALT_TEXT, altText);
        } else {
            log.warn("Could not resolve image for block image at path: {}", blockImage.getPath());
        }

        blockBuilder.addChild(graphicBuilder);
        blockBuilder.buildInto(builder);
    }

    private void appendBlockAttributes(TagBuilder builder, BlockImageStyleProperties style, StyleSheet styleSheet) {
        if (style == null) return;

        builder
                .addAttribute(GenerateConst.TEXT_ALIGN, style.getAlignment())
                .addAttribute(GenerateConst.WIDTH, style.getBlockWidth());

        setFontStyle(styleSheet, style, builder);
    }

    private void appendImageAttributes(TagBuilder builder, BlockImageStyleProperties style) {
        if (style == null) return;

        builder
                .addAttribute(GenerateConst.CONTENT_WIDTH, style.getContentWidth())
                .addAttribute(GenerateConst.SCALING, style.getScaling());
    }
}