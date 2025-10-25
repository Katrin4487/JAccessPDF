package de.fkkaiser.generator.element;

import de.fkkaiser.generator.ImageResolver;
import de.fkkaiser.generator.XslFoGenerator;
import de.fkkaiser.model.structure.Element;
import de.fkkaiser.model.structure.Headline;
import de.fkkaiser.model.structure.Part;
import de.fkkaiser.model.style.PartStyleProperties;
import de.fkkaiser.model.style.StyleSheet;
import org.apache.xmlgraphics.image.loader.spi.ImageImplRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;

public class PartFoGenerator extends ElementFoGenerator {

    private static final Logger log = LoggerFactory.getLogger(PartFoGenerator.class);

    private final XslFoGenerator mainGenerator;

    public PartFoGenerator(XslFoGenerator mainGenerator) {
        this.mainGenerator = mainGenerator;
    }

    @Override
    public void generate(Element element, StyleSheet styleSheet, StringBuilder builder, List<Headline> headlines, ImageResolver resolver) {
        Part part = (Part) element;
        PartStyleProperties style = part.getResolvedStyle();

        builder.append("      <fo:block");
        appendPartAttributes(builder, style, styleSheet);
        builder.append(">");


        mainGenerator.generateBlockElements(part.getElements(), styleSheet, builder, headlines,resolver);

        builder.append("      </fo:block>");
    }

    private void appendPartAttributes(StringBuilder builder, PartStyleProperties style, StyleSheet styleSheet) {
        if (style == null) return;

        // Vererbbare Eigenschaften wie Schriftart etc.
        setFontStyle(styleSheet, style, builder);


        if (style.getPageBreakBefore() != null) {
            builder.append(" break-before=\"").append(escapeXml(style.getPageBreakBefore())).append("\"");
        }
    }
}

