package de.fkkaiser.generator.element;

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

public class ImageFoGenerator extends ElementFoGenerator {
    private static final Logger log = LoggerFactory.getLogger(PartFoGenerator.class);


    @Override
    public void generate(Element element, StyleSheet styleSheet, StringBuilder builder, List<Headline> headlines, URL imageUrl) {
        BlockImage blockImage = (BlockImage) element;
        BlockImageStyleProperties style = blockImage.getResolvedStyle();

        builder.append("      <fo:block");
        appendBlockAttributes(builder, style, styleSheet);
        builder.append(">");
        builder.append("<fo:external-graphic ");
        appendImageAttributes(builder, style);


        try{
            URL fileUrl = imageUrl.toURI().resolve(blockImage.getPath()).toURL();
            InputStream inputStream = fileUrl.openStream();
            byte[] imageBytes = inputStream.readAllBytes();
            String mimeType = fileUrl.toString().endsWith("png") ? "image/png" : "image/jpeg";
            String base64String = Base64.getEncoder().encodeToString(imageBytes);
            String srcDataUri = "data:" + mimeType + ";base64," + base64String;
            builder.append(" src=\"").append(srcDataUri).append("\"");
            builder.append(" fox:alt-text=\"\""); //ToDO

        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }

        builder.append("/>");
        builder.append("      </fo:block>");
    }

    private void appendBlockAttributes(StringBuilder builder, BlockImageStyleProperties style, StyleSheet styleSheet) {
        if (style == null) return;
        if(style.getAlignment()!=null){
            builder.append(" text-align=\"").append(escapeXml(style.getAlignment())).append("\"");
        }
        if(style.getBlockWidth()!=null){
            builder.append(" width=\"").append(style.getBlockWidth()).append("\"");
        }

        setFontStyle(styleSheet, style, builder);

    }

    private void appendImageAttributes(StringBuilder builder, BlockImageStyleProperties style) {
        if (style == null) return;

        if (style.getContentWidth() != null) {
            builder.append(" content-width=\"").append(escapeXml(style.getContentWidth())).append("\"");
        }
        if(style.getScaling() != null){
            builder.append(" scaling=\"").append(escapeXml(style.getScaling())).append("\"");
        }
    }
}
