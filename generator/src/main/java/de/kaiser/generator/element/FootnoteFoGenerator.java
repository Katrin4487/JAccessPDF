package de.kaiser.generator.element;


import de.kaiser.model.structure.Footnote;
import de.kaiser.model.structure.InlineElement;
import de.kaiser.model.style.FootnoteStyleProperties;
import de.kaiser.model.style.StyleSheet;
import de.kaiser.generator.XslFoGenerator;

public class FootnoteFoGenerator extends InlineElementFoGenerator {

    private final XslFoGenerator mainGenerator;

    public FootnoteFoGenerator(XslFoGenerator mainGenerator) {
        this.mainGenerator = mainGenerator;
    }

    @Override
    public void generate(InlineElement element, StyleSheet styleSheet, StringBuilder builder) {
        Footnote footnote = (Footnote) element;

        FootnoteStyleProperties styleProperties = footnote.getResolvedStyle();

        // WORKAROUND: Use role="Span" to prevent FOP's automatic (and broken)
        // <Note> tagging. We will create the accessible structure manually.
        builder.append("<fo:footnote role=\"Span\">");

        // --- The Reference in the Text (The Link) ---
        // This inline element is explicitly tagged as a Link for accessibility.
        builder.append("<fo:inline role=\"Link\">");
        // The basic-link points to the ID of the note's content block.
        builder.append("<fo:basic-link fox:alt-text=\"Link Footnote\" internal-destination=\"").append(escapeXml(footnote.getId())).append("\">");
        // The visual representation of the link (the superscript number)
        builder.append("<fo:inline font-size=\"8pt\" vertical-align=\"super\">")
                .append(escapeXml(footnote.getIndex()))
                .append("</fo:inline>");
        builder.append("</fo:basic-link>");
        builder.append("</fo:inline>");

        // --- The Footnote Body at the bottom of the page (The Note) ---
        builder.append("<fo:footnote-body>");

        builder.append("<fo:block id=\"").append(escapeXml(footnote.getId())).append("\"");

        if(styleProperties!=null){
            if(styleProperties.getStartIndent() != null){
                builder.append(" start-indent=\"").append(escapeXml(styleProperties.getStartIndent())).append("\"");
            }
            if(styleProperties.getEndIndent() != null){
                builder.append(" end-indent=\"").append(escapeXml(styleProperties.getEndIndent())).append("\"");
            }
            //TODO set the other styles...
        }
        builder.append(">");


        if (footnote.getInlineElements() != null) {
            // We prepend the index number to the footnote text for clarity.
            builder.append("<fo:inline font-size=\"8pt\" vertical-align=\"super\">")
                    .append(escapeXml(footnote.getIndex()))
                    .append("</fo:inline> ");

            for (InlineElement inline : footnote.getInlineElements()) {
                mainGenerator.generateInlineElement(inline, styleSheet, builder);
            }
        }

        builder.append("</fo:block>");
        builder.append("</fo:footnote-body>");
        builder.append("</fo:footnote>");
    }
}
