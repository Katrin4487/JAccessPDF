package de.kaiser.generator.element;

import de.kaiser.generator.XslFoGenerator;
import de.kaiser.model.structure.Element;
import de.kaiser.model.structure.Headline;
import de.kaiser.model.structure.InlineElement;
import de.kaiser.model.structure.TextBlock;
import de.kaiser.model.style.StyleSheet;
import de.kaiser.model.style.TextBlockStyleProperties;

import java.util.List;

/**
 * Abstract base class for all block-level text elements (e.g., paragraphs, headlines).
 * It handles the common logic of generating an <fo:block> and its inline content.
 */
public abstract class TextBlockFoGenerator extends ElementFoGenerator {

    protected final XslFoGenerator mainGenerator;

    protected TextBlockFoGenerator(XslFoGenerator mainGenerator) {
        this.mainGenerator = mainGenerator;
    }

    @Override
    public void generate(Element element, StyleSheet styleSheet, StringBuilder builder, List<Headline> headlines) {
        TextBlock textBlock = (TextBlock) element;
        TextBlockStyleProperties style = textBlock.getResolvedStyle();

        String headlineId = "";
        if (element instanceof Headline) {
            headlineId = " id=\"headline" + headlines.size() + "\"";
            headlines.add((Headline) element);
        }

        builder.append("      <fo:block").append(headlineId).append(" role=\"").append(getRole(textBlock)).append("\"");

        // Append all style attributes
        appendCommonAttributes(builder, style, styleSheet);
        appendSpecificAttributes(builder, style);

        builder.append(">\n");

        // Delegate inline content generation to the main generator.
        if (textBlock.getInlineElements() != null) {
            for (InlineElement inlineElement : textBlock.getInlineElements()) {
                mainGenerator.generateInlineElement(inlineElement, styleSheet, builder);
            }
        }

        builder.append("      </fo:block>\n");
    }

    /**
     * Appends common attributes shared by all TextBlock elements.
     */
    void appendCommonAttributes(StringBuilder builder, TextBlockStyleProperties style, StyleSheet styleSheet) {
        if (style == null) return;

        // Apply font styles
        setFontStyle(styleSheet, style, builder);
        // Apply other common text block properties
        if (style.getTextColor() != null) {
            builder.append(" color=\"").append(escapeXml(style.getTextColor())).append("\"");
        }
        if (style.getLineHeight() != null) {
            builder.append(" line-height=\"").append(escapeXml(style.getLineHeight())).append("\"");
        }
        if (style.getTextAlign() != null) {
            builder.append(" text-align=\"").append(escapeXml(style.getTextAlign())).append("\"");
        }
        if (style.getSpaceAfter() != null) {
            builder.append(" space-after=\"").append(escapeXml(style.getSpaceAfter())).append("\"");
        }
        if (style.getSpaceBefore() != null) {
            builder.append(" space-before=\"").append(escapeXml(style.getSpaceBefore())).append("\"");
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
    }

    /**
     * Gets the accessibility role for the specific text block (e.g., "P" or "H1").
     * Must be implemented by subclasses.
     */
    protected abstract String getRole(TextBlock textBlock);

    /**
     * Appends attributes that are specific to the concrete subclass (e.g., headline-specific styles).
     * To be implemented by subclasses.
     */
    protected abstract void appendSpecificAttributes(StringBuilder builder, TextBlockStyleProperties style);
}
