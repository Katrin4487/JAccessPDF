package de.kaiser.generator;

import de.kaiser.generator.element.*;
import de.kaiser.model.structure.*;
import de.kaiser.model.style.ElementStyle;
import de.kaiser.model.style.PageMasterStyle;
import de.kaiser.model.style.StyleSheet;
import de.kaiser.model.style.TextBlockStyleProperties;
import de.kaiser.model.style.TextStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generates an XSL-FO XML structure from a Document object.
 * This class is stateless and orchestrates the generation process by delegating
 * to specialized sub-generators.
 */
public class XslFoGenerator {

    private static final Logger log = LoggerFactory.getLogger(XslFoGenerator.class);
    private final Map<Class<? extends Element>, ElementFoGenerator> blockGeneratorRegistry = new HashMap<>();
    private final Map<Class<? extends InlineElement>, InlineElementFoGenerator> inlineGeneratorRegistry = new HashMap<>();

    public XslFoGenerator() {
        // Register Block-Level Generators
        this.blockGeneratorRegistry.put(Paragraph.class, new ParagraphFoGenerator(this));
        this.blockGeneratorRegistry.put(Headline.class, new HeadlineFoGenerator(this));
        // this.blockGeneratorRegistry.put(List.class, new ListFoGenerator(this));
        // this.blockGeneratorRegistry.put(Table.class, new TableFoGenerator(this));
        // this.blockGeneratorRegistry.put(Section.class, new SectionFoGenerator(this));
        // this.blockGeneratorRegistry.put(ListItem.class, new ListItemFoGenerator(this));

        // Register Inline-Level Generators
        this.inlineGeneratorRegistry.put(TextRun.class, new TextRunFoGenerator());
        this.inlineGeneratorRegistry.put(PageNumber.class, new PageNumberFoGenerator());
        this.inlineGeneratorRegistry.put(Hyperlink.class, new HyperlinkFoGenerator());
        this.inlineGeneratorRegistry.put(Footnote.class, new FootnoteFoGenerator(this));
    }

    /**
     * Main method to start the XSL-FO generation process.
     * @param document The fully processed Document object with resolved styles.
     * @param styleSheet The StyleSheet with all layout and style definitions.
     * @return A string containing the complete XSL-FO document.
     */
    public String generate(Document document, StyleSheet styleSheet) {
        if (document == null || styleSheet == null) {
            return "";
        }

        StringBuilder foBuilder = new StringBuilder();
        List<Headline> headlines = new ArrayList<>();
        String defaultFontFamily = findDefaultFontFamily(styleSheet);

        generateRootStart(foBuilder, document, defaultFontFamily);
        generateLayoutMasterSet(foBuilder, styleSheet);
        generateDeclarations(foBuilder, document);
        generatePageSequences(foBuilder, document, styleSheet, headlines);
        generateBookmarks(foBuilder, headlines);
        generateRootEnd(foBuilder);

        return foBuilder.toString();
    }

    // --- Public Helpers for Sub-Generators ---

    public void generateBlockElement(Element element, StyleSheet styleSheet, StringBuilder builder, List<Headline> headlines) {
        if (element == null) return;
        ElementFoGenerator generator = blockGeneratorRegistry.get(element.getClass());
        if (generator != null) {
            generator.generate(element, styleSheet, builder, headlines);
        } else {
            log.warn("No block generator registered for element type {}.", element.getClass().getSimpleName());
        }
    }

    /**
     * Generates XSL-FO output for a list of block elements using a specified style sheet and StringBuilder.
     *
     * @param elements The list of block elements to generate XSL-FO for. Must not be null.
     * @param styleSheet The StyleSheet with all layout and style definitions. Must not be null.
     * @param builder The StringBuilder where the generated XSL-FO output will be appended.
     * @param headlines The list of headline elements to be included in the generation process.
     */
    public void generateBlockElements(List<Element> elements, StyleSheet styleSheet, StringBuilder builder, List<Headline> headlines) {
        if (elements == null) return;
        for (Element element : elements) {
            generateBlockElement(element, styleSheet, builder, headlines);
        }
    }

    /**
     * Generates XSL-FO output for the provided inline element using a specified style sheet and StringBuilder.
     *
     * @param element The InlineElement object to be processed. Must not be null.
     * @param styleSheet The StyleSheet object containing font and style information. Must not be null.
     * @param builder The StringBuilder where the generated XSL-FO output will be appended.
     */
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

    private void generatePageSequences(StringBuilder foBuilder, Document document, StyleSheet styleSheet, List<Headline> headlines) {
        final String placeholder = "<§§BOOKMARK_TREE§§>";
        foBuilder.append(placeholder);

        for (PageSequence sequence : document.pageSequences()) {
            foBuilder.append("  <fo:page-sequence master-reference=\"").append(escapeXml(sequence.styleClass())).append("\">\n");

            if (sequence.header() != null) {
                foBuilder.append("    <fo:static-content flow-name=\"xsl-region-before\">\n");
                generateBlockElements(sequence.header().elements(), styleSheet, foBuilder, headlines);
                foBuilder.append("    </fo:static-content>\n");
            }
            if (sequence.footer() != null) {
                foBuilder.append("    <fo:static-content flow-name=\"xsl-region-after\">\n");
                generateBlockElements(sequence.footer().elements(), styleSheet, foBuilder, headlines);
                foBuilder.append("    </fo:static-content>\n");
            }

            foBuilder.append("    <fo:flow flow-name=\"xsl-region-body\">\n");
            generateBlockElements(sequence.body().elements(), styleSheet, foBuilder, headlines);
            foBuilder.append("    </fo:flow>\n");

            foBuilder.append("  </fo:page-sequence>\n");
        }
    }

    private void generateBookmarks(StringBuilder foBuilder, List<Headline> headlines) {
        final String placeholder = "<§§BOOKMARK_TREE§§>";
        BookmarkGenerator generator = new BookmarkGenerator();
        String bookmarkTreeXml = generator.generateBookmarkTree(headlines);

        int placeholderIndex = foBuilder.indexOf(placeholder);
        if (placeholderIndex != -1) {
            foBuilder.replace(placeholderIndex, placeholderIndex + placeholder.length(), bookmarkTreeXml);
        }
    }

    private void generateRootStart(StringBuilder foBuilder, Document document, String defaultFontFamily) {
        Metadata metadata = document.metadata();
        String lang = (metadata != null && metadata.language() != null && !metadata.language().isEmpty()) ? metadata.language() : "en";

        foBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n")
                .append("<fo:root xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"")
                .append(" xmlns:fox=\"http://xmlgraphics.apache.org/fop/extensions\"")
                .append(" xml:lang=\"").append(escapeXml(lang)).append("\"");

        if (defaultFontFamily != null && !defaultFontFamily.isEmpty()) {
            foBuilder.append(" font-family=\"").append(escapeXml(defaultFontFamily)).append("\"");
        }

        foBuilder.append(" xmlns:x=\"adobe:ns:meta/\" xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n");
    }

    private void generateDeclarations(StringBuilder foBuilder, Document document) {
        Metadata metadata = document.metadata();
        if (metadata == null || metadata.title() == null || metadata.title().isEmpty()) {
            log.warn("PDF/UA-1 conformance requires a title, but none was provided in the metadata. Skipping XMP metadata generation.");
            return;
        }

        foBuilder.append("  <fo:declarations>\n")
                .append("    <x:xmpmeta>\n")
                .append("      <rdf:RDF>\n")
                .append("        <rdf:Description rdf:about=\"\">\n")
                .append("          <dc:title>").append(escapeXml(metadata.title())).append("</dc:title>\n");

        if (metadata.author() != null) {
            foBuilder.append("          <dc:creator>").append(escapeXml(metadata.author())).append("</dc:creator>\n");
        }
        if (metadata.subject() != null) {
            foBuilder.append("          <dc:description>").append(escapeXml(metadata.subject())).append("</dc:description>\n");
        }

        foBuilder.append("        </rdf:Description>\n")
                .append("      </rdf:RDF>\n")
                .append("    </x:xmpmeta>\n")
                .append("  </fo:declarations>\n");
    }

    private void generateLayoutMasterSet(StringBuilder foBuilder, StyleSheet styleSheet) {
        foBuilder.append("  <fo:layout-master-set>\n");
        if (styleSheet.pageMasterStyles() != null) {
            for (PageMasterStyle pageStyle : styleSheet.pageMasterStyles()) {
                foBuilder.append("    <fo:simple-page-master master-name=\"").append(escapeXml(pageStyle.getName())).append("\"")
                        .append(" page-height=\"").append(escapeXml(pageStyle.getPageHeight())).append("\"")
                        .append(" page-width=\"").append(escapeXml(pageStyle.getPageWidth())).append("\"");

                if (pageStyle.getMargin() != null && !pageStyle.getMargin().isEmpty()) {
                    foBuilder.append(" margin=\"").append(escapeXml(pageStyle.getMargin())).append("\"");
                } else {
                    foBuilder.append(" margin-top=\"").append(escapeXml(pageStyle.getMarginTop())).append("\"")
                            .append(" margin-bottom=\"").append(escapeXml(pageStyle.getMarginBottom())).append("\"")
                            .append(" margin-left=\"").append(escapeXml(pageStyle.getMarginLeft())).append("\"")
                            .append(" margin-right=\"").append(escapeXml(pageStyle.getMarginRight())).append("\"");
                }
                foBuilder.append(">\n");

                foBuilder.append("      <fo:region-body");
                if (pageStyle.getHeaderExtent() != null) {
                    foBuilder.append(" margin-top=\"").append(escapeXml(pageStyle.getHeaderExtent())).append("\"");
                }
                if (pageStyle.getFooterExtent() != null) {
                    foBuilder.append(" margin-bottom=\"").append(escapeXml(pageStyle.getFooterExtent())).append("\"");
                }
                foBuilder.append("/>\n");

                if (pageStyle.getHeaderExtent() != null) {
                    foBuilder.append("      <fo:region-before region-name=\"xsl-region-before\" extent=\"")
                            .append(escapeXml(pageStyle.getHeaderExtent()))
                            .append("\"/>\n");
                }
                if (pageStyle.getFooterExtent() != null) {
                    foBuilder.append("      <fo:region-after region-name=\"xsl-region-after\" extent=\"")
                            .append(escapeXml(pageStyle.getFooterExtent()))
                            .append("\"/>\n");
                }
                foBuilder.append("    </fo:simple-page-master>\n");
            }
        }
        foBuilder.append("  </fo:layout-master-set>\n");
    }

    private void generateRootEnd(StringBuilder foBuilder) {
        foBuilder.append("</fo:root>\n");
    }

    private String findDefaultFontFamily(StyleSheet styleSheet) {
        if (styleSheet == null || styleSheet.elementStyles() == null) {
            return null;
        }
        return styleSheet.elementStyles().stream()
                .filter(s -> "default-text-style".equals(s.name()))
                .findFirst()
                .map(ElementStyle::properties)
                .filter(p -> p instanceof TextBlockStyleProperties)
                .map(p -> ((TextBlockStyleProperties) p).getTextStyleName())
                .flatMap(styleSheet::findFontStyleByName) // Assuming findFontStyleByName was renamed to findTextStyleByName
                .map(TextStyle::fontFamilyName)
                .orElse(null);
    }

    private String escapeXml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
