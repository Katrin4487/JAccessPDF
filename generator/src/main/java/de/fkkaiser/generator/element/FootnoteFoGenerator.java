package de.fkkaiser.generator.element;

import de.fkkaiser.generator.GenerateUtils;
import de.fkkaiser.model.structure.Footnote;
import de.fkkaiser.model.structure.InlineElement;
import de.fkkaiser.model.structure.TextBlock;
import de.fkkaiser.model.style.ElementBlockStyleProperties;
import de.fkkaiser.model.style.FootnoteStyleProperties;
import de.fkkaiser.model.style.StyleSheet;
import de.fkkaiser.generator.XslFoGenerator;
import de.fkkaiser.model.style.TextBlockStyleProperties;

public class FootnoteFoGenerator extends InlineElementFoGenerator {

    private final XslFoGenerator mainGenerator;
    // KORREKTUR: Helfer-Klasse zum Anwenden von Block-Stilen hinzugefügt
    private final StyleApplier styleHelper;


    public FootnoteFoGenerator(XslFoGenerator mainGenerator) {
        this.mainGenerator = mainGenerator;
        // KORREKTUR: Initialisierung des Helfers für die Style-Anwendung
        this.styleHelper = new StyleApplier(mainGenerator);
    }

    /**
     * Private innere Helferklasse, die von TextBlockFoGenerator erbt,
     * nur um an die (package-private) Methode appendCommonAttributes zu gelangen.
     */
    private static class StyleApplier extends TextBlockFoGenerator {
        public StyleApplier(XslFoGenerator mainGenerator) {
            super(mainGenerator);
        }

        // Exponiert die Methode, die wir brauchen
        public void applyStyles(StringBuilder builder, ElementBlockStyleProperties style, StyleSheet styleSheet) {
            super.appendCommonAttributes(builder, style, styleSheet);
        }

        // Erfüllt die abstrakten Methoden
        @Override protected String getRole(TextBlock textBlock) { return null; }
        @Override protected void appendSpecificAttributes(StringBuilder builder, TextBlockStyleProperties style) { }
    }

    @Override
    public void generate(InlineElement element, StyleSheet styleSheet, StringBuilder builder) {
        Footnote footnote = (Footnote) element;

        FootnoteStyleProperties styleProperties = footnote.getResolvedStyle();
        // <Note> tagging. We will create the accessible structure manually.
        builder.append("<fo:footnote role=\"Span\">");

        builder.append("<fo:footnote-body>");

        builder.append("<fo:block id=\"").append(GenerateUtils.escapeXml(footnote.getId())).append("\"");

        if(styleProperties!=null){
            // KORREKTUR: Anstatt Stile manuell zu setzen, rufen wir den Helfer auf,
            // der ALLE TextBlock-Stile (Schriftart, Farbe, Abstände, Einzüge etc.) anwendet.
            styleHelper.applyStyles(builder, styleProperties, styleSheet);

        }
        builder.append(">");



        if (footnote.getInlineElements() != null) {
            // We prepend the index number to the footnote text for clarity.
            builder.append("<fo:inline font-size=\"8pt\" vertical-align=\"super\">")
                    .append(GenerateUtils.escapeXml(footnote.getIndex()))
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
