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
import de.fkkaiser.model.configuration.JAccessStrictnessException;
import de.fkkaiser.model.configuration.StrictnessMode;
import de.fkkaiser.model.structure.BlockImage;
import de.fkkaiser.model.structure.Element;
import de.fkkaiser.model.structure.Headline;
import de.fkkaiser.model.style.BlockImageStyleProperties;
import de.fkkaiser.model.style.StyleSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * Generator for Images
 *
 * @author Katrin Kaiser
 * @version 1.3.0
 */
@Internal
public class ImageFoGenerator extends ElementFoGenerator {
    private static final Logger log = LoggerFactory.getLogger(ImageFoGenerator.class);

    private final XslFoGenerator mainGenerator;

    /**
     * Constructs an ImageFoGenerator with the specified XslFoGenerator.
     * @param xslFoGenerator The main XslFoGenerator instance used for generating FO content.
     */
    public ImageFoGenerator(XslFoGenerator xslFoGenerator) {
        this.mainGenerator = xslFoGenerator;
    }

    /**
     * Generates the FO representation of a BlockImage element.
     * @param element The element to be processed.
     * @param styleSheet The entire StyleSheet for accessing, for example, Font information.
     * @param builder The StringBuilder to which the generated string is appended.
     * @param headlines The list of headlines in the document (for generating IDs).
     * @param imageResolver The image resolver to resolve image paths.
     * @param isExternalArtefact Indicates if the element is part of an external artifact.
     */
     @Override
    public void generate(Element element,
                         StyleSheet styleSheet,
                         StringBuilder builder,
                         List<Headline> headlines,
                         ImageResolver imageResolver,
                         boolean isExternalArtefact) {

        BlockImage blockImage = (BlockImage) element;

         // isBlank, matching the check below: whitespace-only alt text is treated
         // as missing, so it is normalised to "" rather than written through to
         // the PDF verbatim. In LENIENT and WARN, FOP then substitutes its own
         // placeholder - the same outcome as for alt text that was never set.
         String altText = (blockImage.getAltText() != null && !blockImage.getAltText().isBlank())
                 ? blockImage.getAltText()
                 : "";

        BlockImageStyleProperties style = blockImage.getResolvedStyle();

        TagBuilder blockBuilder = GenerateUtils.tagBuilder(GenerateConst.BLOCK);

        if (isExternalArtefact) {
            blockBuilder.addAttribute(GenerateConst.CONTENT_TYPE, GenerateConst.EXTERNAL_ARTIFACT);
        }

        appendBlockAttributes(blockBuilder, style, styleSheet);

        // Create the external-graphic element
        TagBuilder graphicBuilder = GenerateUtils.tagBuilder(GenerateConst.EXTERNAL_GRAPHIC);
        appendImageAttributes(graphicBuilder, style);

        // Resolve the image path and set the src attribute
        String srcDataUri;
        String imagePath = blockImage.getPath();
        if (blockImage.getSvgContent() != null){
            // Like the base64 case below: inline SVG has no path, so give log
            // messages something to name the image by instead of "null".
            imagePath = "image is inline SVG, no path available";
            srcDataUri = ImageUtils.svgContentToDataUri(blockImage.getSvgContent());
        } else if (blockImage.getBase64Data() != null) {
            imagePath = "image is base64 encoded, no path available";
            srcDataUri = blockImage.getBase64Data();
        } else {
            imagePath = blockImage.getPath();
            srcDataUri = ImageUtils.resolveToDataUri(blockImage.getPath(), imageResolver);
        }

        if (srcDataUri != null) {
            graphicBuilder.addAttribute(GenerateConst.SRC, srcDataUri);

            graphicBuilder.addAttribute(GenerateConst.ALT_TEXT, altText);
        } else {
            log.warn("Could not resolve image for block image at path: {}", imagePath);
        }

         if(altText.isBlank()) {
             if(mainGenerator.getConfig().strictnessMode() == StrictnessMode.STRICT) {
                 throw new JAccessStrictnessException("BlockImage at path '" + imagePath + "' has no alt text. This is a violation of PDF/UA-1.");
             }
             if(mainGenerator.getConfig().strictnessMode() == StrictnessMode.WARN){
                 // imagePath, not getPath(): for base64 and inline SVG images
                 // there is no path, which would make the message say "path 'null'".
                 log.warn("BlockImage at path '{}' has no alt text. This is a violation of PDF/UA-1.", imagePath);

             }
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