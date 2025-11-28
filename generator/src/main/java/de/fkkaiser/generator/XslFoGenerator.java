package de.fkkaiser.generator;

import de.fkkaiser.generator.element.*;
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.structure.*;
import de.fkkaiser.model.style.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generates an XSL-FO XML structure from a Document object.
 * Refactored to avoid placeholders by buffering page content.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
@Internal
public class XslFoGenerator {

    private static final Logger log = LoggerFactory.getLogger(XslFoGenerator.class);
    private final Map<Class<? extends Element>, ElementFoGenerator> blockGeneratorRegistry = new HashMap<>();
    private final Map<Class<? extends InlineElement>, InlineElementFoGenerator> inlineGeneratorRegistry = new HashMap<>();

    public XslFoGenerator() {
        this.blockGeneratorRegistry.put(Paragraph.class, new ParagraphFoGenerator(this));
        this.blockGeneratorRegistry.put(Headline.class, new HeadlineFoGenerator(this));
        this.blockGeneratorRegistry.put(SimpleList.class, new ListFoGenerator(this));
        this.blockGeneratorRegistry.put(Table.class, new TableFoGenerator(this));
        this.blockGeneratorRegistry.put(Section.class, new SectionFoGenerator(this));
        this.blockGeneratorRegistry.put(ListItem.class, new ListItemFoGenerator(this));
        this.blockGeneratorRegistry.put(BlockImage.class,new ImageFoGenerator());
        this.blockGeneratorRegistry.put(LayoutTable.class,new LayoutTableFoGenerator(this));
        this.inlineGeneratorRegistry.put(TextRun.class, new TextRunFoGenerator());
        this.inlineGeneratorRegistry.put(PageNumber.class, new PageNumberFoGenerator());
        this.inlineGeneratorRegistry.put(Hyperlink.class, new HyperlinkFoGenerator());
        this.inlineGeneratorRegistry.put(Footnote.class, new FootnoteFoGenerator(this));
    }

    /**
     * Main method to start the XSL-FO generation process.
     */
    @Internal
    public String generate(Document document, StyleSheet styleSheet, ImageResolver resolver) {
        if (document == null || styleSheet == null) {
            return "";
        }
        StringBuilder contentBuilder = new StringBuilder();
        List<Headline> headlines = new ArrayList<>();

        generatePageSequences(contentBuilder, document, styleSheet, headlines, resolver);

        StringBuilder foBuilder = new StringBuilder();
        String defaultFontFamily = findDefaultFontFamily(styleSheet);

        generateRootStart(foBuilder, document, defaultFontFamily);
        generateLayoutMasterSet(foBuilder, styleSheet);
        generateDeclarations(foBuilder, document);

        generateBookmarks(foBuilder, headlines);

        foBuilder.append(contentBuilder);

        // D. Abschluss
        generateRootEnd(foBuilder);

        return foBuilder.toString();
    }

    // --- Public Helpers  ---
    @Internal
    public void generateBlockElement(Element element, StyleSheet styleSheet, StringBuilder builder, List<Headline> headlines, ImageResolver resolver, boolean isExternalArtefact) {
        if (element == null) return;
        ElementFoGenerator generator = blockGeneratorRegistry.get(element.getClass());
        if (generator != null) {
            generator.generate(element, styleSheet, builder, headlines, resolver, isExternalArtefact);
        } else {
            log.warn("No block generator registered for element type {}.", element.getClass().getSimpleName());
        }
    }

    @Internal
    public void generateBlockElements(List<Element> elements, StyleSheet styleSheet, StringBuilder builder, List<Headline> headlines, ImageResolver resolver, boolean isExternalArtefact) {
        if (elements == null) return;
        for (Element element : elements) {
            generateBlockElement(element, styleSheet, builder, headlines, resolver, isExternalArtefact);
        }
    }

    @Internal
    public void generateInlineElement(InlineElement element, StyleSheet styleSheet, StringBuilder builder) {
        if (element == null) return;
        InlineElementFoGenerator generator = inlineGeneratorRegistry.get(element.getClass());
        if (generator != null) {
            generator.generate(element, styleSheet, builder);
        } else {
            log.warn("No inline generator registered for element type {}.", element.getClass().getSimpleName());
        }
    }

    // --- Private Generation Steps ---

    @Internal
     private void generatePageSequences(StringBuilder builder, Document document, StyleSheet styleSheet, List<Headline> headlines, ImageResolver resolver) {
        for (PageSequence sequence : document.pageSequences()) {
            builder.append("  <fo:page-sequence master-reference=\"").append(GenerateUtils.escapeXml(sequence.styleClass())).append("\">");

            if (sequence.header() != null) {
                builder.append("    <fo:static-content flow-name=\"xsl-region-before\">");
                generateBlockElements(sequence.header().elements(), styleSheet, builder, headlines, resolver, true);
                builder.append("    </fo:static-content>");
            }
            if (sequence.footer() != null) {
                builder.append("    <fo:static-content flow-name=\"xsl-region-after\">");
                generateBlockElements(sequence.footer().elements(), styleSheet, builder, headlines, resolver, true);
                builder.append("    </fo:static-content>");
            }

            builder.append("    <fo:flow flow-name=\"xsl-region-body\">");
            generateBlockElements(sequence.body().elements(), styleSheet, builder, headlines, resolver, false);
            builder.append("    </fo:flow>");

            builder.append("  </fo:page-sequence>");
        }
    }

    @Internal
    private void generateBookmarks(StringBuilder foBuilder, List<Headline> headlines) {
        if (headlines == null || headlines.isEmpty()) {
            return;
        }

        BookmarkGenerator generator = new BookmarkGenerator();
        String bookmarkTreeXml = generator.generateBookmarkTree(headlines);

        foBuilder.append(bookmarkTreeXml);
    }

    private void generateRootStart(StringBuilder foBuilder, Document document, String defaultFontFamily) {
        Metadata metadata = document.metadata();
        String lang = (metadata != null && metadata.getLanguage() != null && !metadata.getLanguage().isEmpty()) ? metadata.getLanguage() : "en";

        foBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append("<fo:root xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"")
                .append(" xmlns:fox=\"http://xmlgraphics.apache.org/fop/extensions\"")
                .append(" xml:lang=\"").append(GenerateUtils.escapeXml(lang)).append("\"");

        if (defaultFontFamily != null && !defaultFontFamily.isEmpty()) {
            foBuilder.append(" font-family=\"").append(GenerateUtils.escapeXml(defaultFontFamily)).append("\"");
        }

        foBuilder.append("xmlns:x=\"adobe:ns:meta/\" xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\">");
    }

    private void generateDeclarations(StringBuilder foBuilder, Document document) {
        Metadata metadata = document.metadata();
        if (metadata == null || metadata.getTitle() == null || metadata.getTitle().isEmpty()) {
            log.warn("PDF/UA-1 conformance requires a title, but none was provided in the metadata. Skipping XMP metadata generation.");
            return;
        }

        foBuilder.append("<fo:declarations>")
                .append("<x:xmpmeta>")
                .append("<rdf:RDF>")
                .append("<rdf:Description rdf:about=\"\">")
                .append("<dc:title>").append(GenerateUtils.escapeXml(metadata.getTitle())).append("</dc:title>");

        if (metadata.getAuthor() != null) {
            foBuilder.append("<dc:creator>").append(GenerateUtils.escapeXml(metadata.getAuthor())).append("</dc:creator>");
        }
        if (metadata.getSubject() != null) {
            foBuilder.append("<dc:description>").append(GenerateUtils.escapeXml(metadata.getSubject())).append("</dc:description>");
        }

        foBuilder.append("</rdf:Description>")
                .append("</rdf:RDF>")
                .append("</x:xmpmeta>")
                .append("</fo:declarations>");
    }

    private void generateLayoutMasterSet(StringBuilder foBuilder, StyleSheet styleSheet) {

        foBuilder.append("<fo:layout-master-set>");
        if (styleSheet.pageMasterStyles() != null) {
            for (PageMasterStyle pageStyle : styleSheet.pageMasterStyles()) {
                foBuilder.append("<fo:simple-page-master master-name=\"").append(GenerateUtils.escapeXml(pageStyle.getName())).append("\"")
                        .append(" page-height=\"").append(GenerateUtils.escapeXml(pageStyle.getPageHeight())).append("\"")
                        .append(" page-width=\"").append(GenerateUtils.escapeXml(pageStyle.getPageWidth())).append("\"");

                if (pageStyle.getMargin() != null && !pageStyle.getMargin().isEmpty()) {
                    foBuilder.append(" margin=\"").append(GenerateUtils.escapeXml(pageStyle.getMargin())).append("\"");
                } else {
                    foBuilder.append(" margin-top=\"").append(GenerateUtils.escapeXml(pageStyle.getMarginTop())).append("\"")
                            .append(" margin-bottom=\"").append(GenerateUtils.escapeXml(pageStyle.getMarginBottom())).append("\"")
                            .append(" margin-left=\"").append(GenerateUtils.escapeXml(pageStyle.getMarginLeft())).append("\"")
                            .append(" margin-right=\"").append(GenerateUtils.escapeXml(pageStyle.getMarginRight())).append("\"");
                }
                foBuilder.append(">");

                foBuilder.append("      <fo:region-body");
                if (pageStyle.getHeaderExtent() != null) {
                    foBuilder.append(" margin-top=\"").append(GenerateUtils.escapeXml(pageStyle.getHeaderExtent())).append("\"");
                }
                if (pageStyle.getFooterExtent() != null) {
                    foBuilder.append(" margin-bottom=\"").append(GenerateUtils.escapeXml(pageStyle.getFooterExtent())).append("\"");
                }
                if(pageStyle.getColumnCount() != null) {
                    foBuilder.append(" column-count=\"").append(GenerateUtils.escapeXml(pageStyle.getColumnCount())).append("\"");
                }
                if (pageStyle.getColumnGap() != null){
                    foBuilder.append(" column-gap=\"").append(GenerateUtils.escapeXml(pageStyle.getColumnGap())).append("\"");
                }
                foBuilder.append("/>");

                if (pageStyle.getHeaderExtent() != null) {
                    foBuilder.append("<fo:region-before region-name=\"xsl-region-before\" extent=\"")
                            .append(GenerateUtils.escapeXml(pageStyle.getHeaderExtent()))
                            .append("\"/>");
                }
                if (pageStyle.getFooterExtent() != null) {
                    foBuilder.append("<fo:region-after region-name=\"xsl-region-after\" extent=\"")
                            .append(GenerateUtils.escapeXml(pageStyle.getFooterExtent()))
                            .append("\"/>");
                }
                foBuilder.append("</fo:simple-page-master>");
            }
        }
        foBuilder.append("</fo:layout-master-set>");
    }

    private void generateRootEnd(StringBuilder foBuilder) {
        foBuilder.append("</fo:root>");
    }

    private String findDefaultFontFamily(StyleSheet styleSheet) {
        // ... (Code wie in deiner Vorlage) ...
        if (styleSheet == null || styleSheet.elementStyles() == null) {
            return null;
        }
        return styleSheet.elementStyles().stream()
                .filter(s -> "default-text-style".equals(s.name()))
                .findFirst()
                .map(ElementStyle::properties)
                .filter(p -> p instanceof TextBlockStyleProperties)
                .map(p -> ((TextBlockStyleProperties) p).getTextStyleName())
                .flatMap(styleSheet::findFontStyleByName)
                .map(TextStyle::fontFamilyName)
                .orElse(null);
    }

}