package de.kaiser.generator.element;

import de.kaiser.generator.XslFoGenerator;
import de.kaiser.model.structure.Element;
import de.kaiser.model.structure.Headline;
import de.kaiser.model.structure.Part;
import de.kaiser.model.style.PartStyleProperties;
import de.kaiser.model.style.StyleSheet;
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
    public void generate(Element element, StyleSheet styleSheet, StringBuilder builder, List<Headline> headlines, URL imageUrl) {
        Part part = (Part) element;
        PartStyleProperties style = part.getResolvedStyle();

        builder.append("      <fo:block");
        appendPartAttributes(builder, style, styleSheet);
        builder.append(">");


        mainGenerator.generateBlockElements(part.getElements(), styleSheet, builder, headlines, imageUrl);

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

