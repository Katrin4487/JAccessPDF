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

import de.fkkaiser.generator.GenerateUtils;
import de.fkkaiser.generator.ImageResolver;
import de.fkkaiser.model.structure.BlockImage;
import de.fkkaiser.model.structure.Element;
import de.fkkaiser.model.structure.Headline;
import de.fkkaiser.model.style.BlockImageStyleProperties;
import de.fkkaiser.model.style.StyleSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.List;

public class ImageFoGenerator extends ElementFoGenerator {
    private static final Logger log = LoggerFactory.getLogger(PartFoGenerator.class);


    @Override
    public void generate(Element element,
                         StyleSheet styleSheet,
                         StringBuilder builder,
                         List<Headline> headlines,
                         ImageResolver imageResolver,
                         boolean isExternalArtefact) {
        BlockImage blockImage = (BlockImage) element;
        BlockImageStyleProperties style = blockImage.getResolvedStyle();

        builder.append("      <fo:block");
        if(isExternalArtefact) {
            builder.append(" fox:content-type=\"external-artifact\"");
        }
        appendBlockAttributes(builder, style, styleSheet);
        builder.append(">");
        builder.append("<fo:external-graphic ");
        appendImageAttributes(builder, style);


        try{
            URL absoluteUrl = imageResolver.resolve(blockImage.getPath());


            if(absoluteUrl!=null) {
                InputStream inputStream = absoluteUrl.openStream();
                byte[] imageBytes = inputStream.readAllBytes();
                String mimeType = absoluteUrl.toString().endsWith("png") ? "image/png" : "image/jpeg";
                String base64String = Base64.getEncoder().encodeToString(imageBytes);
                String srcDataUri = "data:" + mimeType + ";base64," + base64String;

                builder.append(" src=\"").append(srcDataUri).append("\"");
                if(blockImage.getAltText()!=null && !blockImage.getAltText().isEmpty()){
                    builder.append(" fox:alt-text=\"").append(blockImage.getAltText()).append("\"");
                }else{
                    builder.append(" fox:alt-text=\"\"");
                }

            }
        } catch (IOException e){
            log.error("Unable to resolve image url for block image");
        }

        builder.append("/>");
        builder.append("      </fo:block>");
    }

    private void appendBlockAttributes(StringBuilder builder, BlockImageStyleProperties style, StyleSheet styleSheet) {
        if (style == null) return;
        if(style.getAlignment()!=null){
            builder.append(" text-align=\"").append(GenerateUtils.escapeXml(style.getAlignment())).append("\"");
        }
        if(style.getBlockWidth()!=null){
            builder.append(" width=\"").append(style.getBlockWidth()).append("\"");
        }

        setFontStyle(styleSheet, style, builder);

    }

    private void appendImageAttributes(StringBuilder builder, BlockImageStyleProperties style) {
        if (style == null) return;

        if (style.getContentWidth() != null) {
            builder.append(" content-width=\"").append(GenerateUtils.escapeXml(style.getContentWidth())).append("\"");
        }
        if(style.getScaling() != null){
            builder.append(" scaling=\"").append(GenerateUtils.escapeXml(style.getScaling())).append("\"");
        }
    }
}
