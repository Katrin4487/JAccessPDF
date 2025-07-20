package de.kaiser.generator.element;

import de.kaiser.generator.XslFoGenerator;
import de.kaiser.model.structure.Element;
import de.kaiser.model.structure.Headline;
import de.kaiser.model.structure.LayoutTable;
import de.kaiser.model.style.StyleSheet;

import java.net.URL;
import java.util.List;

public class LayoutTableFoGenerator extends ElementFoGenerator {


    protected final XslFoGenerator mainGenerator;

    public LayoutTableFoGenerator(final XslFoGenerator mainGenerator) {
        super();
        this.mainGenerator = mainGenerator;
    }

    //ToDO

    @Override
    public void generate(Element element, StyleSheet styleSheet, StringBuilder builder, List<Headline> headlines, URL imageUrl) {

        LayoutTable table = (LayoutTable) element;
        builder.append("<fo:table  table-layout=\"fixed\" width=\"100%\">");
        builder.append("<fo:table-column column-width=\"85%\"/><fo:table-column column-width=\"15%\"/>");
        builder.append(" <fo:table-body>");
        builder.append("<fo:table-row><fo:table-cell padding=\"0pt\">");
        mainGenerator.generateBlockElement(table.getElementLeft(),styleSheet,builder,headlines,imageUrl);
        builder.append( "</fo:table-cell>");
        builder.append("<fo:table-cell end-indent=\"0pt\" text-align=\"end\" padding=\"0pt\">");
        mainGenerator.generateBlockElement(table.getElementRight(), styleSheet, builder, headlines, imageUrl);
        builder.append("</fo:table-cell>");
        builder.append( "</fo:table-row>");
        builder.append("</fo:table-body>");
        builder.append("</fo:table>");
    }
}
