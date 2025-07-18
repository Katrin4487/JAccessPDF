package de.kaiser.generator.element;

import de.kaiser.generator.XslFoGenerator;
import de.kaiser.model.structure.BlockImage;
import de.kaiser.model.structure.Element;
import de.kaiser.model.structure.Headline;
import de.kaiser.model.structure.Part;
import de.kaiser.model.style.BlockImageStyleProperties;
import de.kaiser.model.style.PartStyleProperties;
import de.kaiser.model.style.StyleSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Base64;
import java.util.List;

public class ImageFoGenerator extends ElementFoGenerator {
    private static final Logger log = LoggerFactory.getLogger(PartFoGenerator.class);

    private final XslFoGenerator mainGenerator;

    public ImageFoGenerator(XslFoGenerator mainGenerator) {
        this.mainGenerator = mainGenerator;
    }

    @Override
    public void generate(Element element, StyleSheet styleSheet, StringBuilder builder, List<Headline> headlines, URL imageUrl) {
        BlockImage blockImage = (BlockImage) element;
        BlockImageStyleProperties style = blockImage.getResolvedStyle();

        builder.append("      <fo:block");
        appendBlockAttributes(builder, style, styleSheet);
        builder.append(">\n");
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
            System.out.println("InputStream erfolgreich geöffnet. Verfügbare Bytes: " + inputStream.available());

        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }

        builder.append("/>\n");
        builder.append("      </fo:block>\n");
    }

    private void appendBlockAttributes(StringBuilder builder, BlockImageStyleProperties style, StyleSheet styleSheet) {
        if (style == null) return;
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
