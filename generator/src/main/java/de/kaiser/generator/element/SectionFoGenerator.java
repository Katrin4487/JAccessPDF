package de.kaiser.generator.element;

import de.kaiser.generator.XslFoGenerator;
import de.kaiser.model.structure.Element;
import de.kaiser.model.structure.Headline;
import de.kaiser.model.structure.Section;
import de.kaiser.model.style.SectionStyleProperties;
import de.kaiser.model.style.StyleSheet;

import java.net.URL;
import java.util.List;

/**
 * Generates the XSL-FO structure for an ESection element.
 * An ESection is rendered as an fo:block that can contain other block-level elements.
 */
public class SectionFoGenerator extends ElementFoGenerator {

    private final XslFoGenerator mainGenerator;

    /**
     * Constructor for ESectionFoGenerator.
     *
     * @param mainGenerator The main generator used for delegating content generation.
     */
    public SectionFoGenerator(XslFoGenerator mainGenerator) {
        this.mainGenerator = mainGenerator;
    }

    /**
     * Generates the XSL-FO string for a section element.
     *
     * @param element    The section element to be processed.
     * @param styleSheet The entire StyleSheet for accessing style information.
     * @param builder    The StringBuilder to which the generated string is appended.
     * @param headlines  The list of headlines for bookmark generation.
     */
    @Override
    public void generate(Element element, StyleSheet styleSheet, StringBuilder builder, List<Headline> headlines, URL imageUrl) {
        Section section = (Section) element;
        SectionStyleProperties style = section.getResolvedStyle();

        builder.append("      <fo:block role=\"Sect\"");
        appendSectionAttributes(builder, style, styleSheet);
        builder.append(">");

        // getElements is never null
        mainGenerator.generateBlockElements(section.getElements(), styleSheet, builder, headlines,imageUrl);

        builder.append("      </fo:block>");
    }

    /**
     * Appends section-specific attributes to the fo:block tag.
     *
     * @param builder    The StringBuilder to append to.
     * @param style      The resolved style properties for the section.
     * @param styleSheet The stylesheet for font lookups.
     */
    private void appendSectionAttributes(StringBuilder builder, SectionStyleProperties style, StyleSheet styleSheet) {
        if (style == null) return;

        setFontStyle(styleSheet, style, builder);

        if (style.getPadding() != null) {
            builder.append(" padding=\"").append(escapeXml(style.getPadding())).append("\"");
        }
        if(style.getPaddingBottom() != null){
            builder.append(" padding-bottom=\"").append(escapeXml(style.getPaddingBottom())).append("\"");
        }
        if (style.getBorder() != null) {
            builder.append(" border=\"").append(escapeXml(style.getBorder())).append("\"");
        }
        if (style.getBackgroundColor() != null) {
            builder.append(" background-color=\"").append(escapeXml(style.getBackgroundColor())).append("\"");
        }
        if(style.getStartIndent() != null){
            builder.append(" start-indent=\"").append(escapeXml(style.getStartIndent())).append("\"");
        }
        if(style.getEndIndent() != null){
            builder.append(" end-indent=\"").append(escapeXml(style.getEndIndent())).append("\"");
        }
        if(style.getSpaceBefore()!=null){
            builder.append(" space-before=\"").append(escapeXml(style.getSpaceBefore())).append("\"");
        }
        if(style.getSpaceAfter()!=null){
            builder.append(" space-after=\"").append(escapeXml(style.getSpaceAfter())).append("\"");
        }
    }
}
