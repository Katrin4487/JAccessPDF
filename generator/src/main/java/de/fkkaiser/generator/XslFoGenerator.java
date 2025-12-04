/*
 * Copyright 2025 Katrin Kaiser
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
 * @version 1.1.0
 */
@Internal
public class XslFoGenerator {

    // Fo Tags
    private static final String STATIC_CONTENT_TAG = "static-content";
    private static final String FLOW_TAG = "flow";
    private static final String PAGE_SEQUENCE = "page-sequence";
    private static final String LAYOUT_MASTER_SET = "layout-master-set";
    private static final String SIMPLE_PAGE_MASTER = "simple-page-master";
    private static final String REGION_BODY = "region-body";
    private static final String REGION_BEFORE = "region-before";
    private static final String REGION_AFTER = "region-after";

    //Values for Attributes
    private static final String XSL_REGION_BODY = "xsl-region-body";
    private static final String XSL_REGION_BEFORE = "xsl-region-before";
    private static final String XSL_REGION_AFTER = "xsl-region-after";
    private static final String MASTER_REFERENCE = "master-reference";
    private static final String MASTER_NAME = "master-name";
    private static final String MARGIN_LEFT = "margin-left";
    private static final String MARGIN_RIGHT = "margin-right";
    private static final String MARGIN_BOTTOM = "margin-bottom";
    private static final String MARGIN_TOP = "margin-top";
    private static final String MARGIN = "margin";
    private static final String PAGE_HEIGHT = "page-height";
    private static final String PAGE_WIDTH = "page-width";
    private static final String COLUMN_COUNT = "column-count";
    private static final String COLUMN_GAP = "column-gap";
    private static final String REGION_NAME = "region-name";
    private static final String EXTENT = "extent";

    private static final Logger log = LoggerFactory.getLogger(XslFoGenerator.class);
    private final Map<Class<? extends Element>, ElementFoGenerator> blockGeneratorRegistry = new HashMap<>();
    private final Map<Class<? extends InlineElement>, InlineElementFoGenerator> inlineGeneratorRegistry = new HashMap<>();

    public XslFoGenerator() {
        this.blockGeneratorRegistry.put(Paragraph.class, new ParagraphFoGenerator(this));
        this.blockGeneratorRegistry.put(Headline.class, new HeadlineFoGenerator(this));
        this.blockGeneratorRegistry.put(SimpleList.class, new ListFoGenerator(this));
        this.blockGeneratorRegistry.put(Table.class, new TableFoGenerator(this));
        this.blockGeneratorRegistry.put(Section.class, new SectionFoGenerator(this));
        this.blockGeneratorRegistry.put(Part.class, new PartFoGenerator(this));
        this.blockGeneratorRegistry.put(ListItem.class, new ListItemFoGenerator(this));
        this.blockGeneratorRegistry.put(BlockImage.class,new ImageFoGenerator());
        this.blockGeneratorRegistry.put(LayoutTable.class,new LayoutTableFoGenerator(this));
         this.inlineGeneratorRegistry.put(TextRun.class, new TextRunFoGenerator());
        this.inlineGeneratorRegistry.put(PageNumber.class, new PageNumberFoGenerator());
        this.inlineGeneratorRegistry.put(Hyperlink.class, new HyperlinkFoGenerator());
        this.inlineGeneratorRegistry.put(Footnote.class, new FootnoteFoGenerator(this));
    }

    /**
     * Generates the complete XSL-FO document as a String.
     * @param document {@link Document} representing the content structure
     * @param styleSheet {@link StyleSheet} defining styles
     * @param resolver {@link ImageResolver} for image handling
     * @return XSL-FO document as a String
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

        generateRootEnd(foBuilder);

        return foBuilder.toString();
    }

    /**
     * Generates a block-level element. An {@code null} element is ignored.
     * If no generator is registered for the element type, a warning is logged.
     * @param element {@link Element} to generate
     * @param styleSheet {@link StyleSheet} for styling
     * @param builder StringBuilder to append generated FO
     * @param headlines List of headlines for bookmarks
     * @param resolver {@link ImageResolver} for image handling
     * @param isExternalArtefact indicates if the element is part of an external artefact (e.g., header/footer)
     */
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

    /**
     * Generates multiple block-level elements.
     *
     * @param elements           List of {@link Element} to generate
     * @param styleSheet         {@link StyleSheet} for styling
     * @param builder            StringBuilder to append generated FO
     * @param headlines          List of headlines for bookmarks
     * @param resolver           {@link ImageResolver} for image handling
     * @param isExternalArtefact indicates if the elements are part of an external artefact (e.g., header/footer)
     */
    @Internal
    public void generateBlockElements(List<Element> elements, StyleSheet styleSheet, StringBuilder builder, List<Headline> headlines, ImageResolver resolver, boolean isExternalArtefact) {
        if (elements == null) return;
        for (Element element : elements) {
            generateBlockElement(element, styleSheet, builder, headlines, resolver, isExternalArtefact);
        }
    }

    /**
     * Generates an inline element. An {@code null} element is ignored. If no generator is registered
     * for the element type, a warning is logged.
     * @param element {@link InlineElement} to generate
     * @param styleSheet {@link StyleSheet} for styling
     * @param builder StringBuilder to append generated FO
     */
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

    /**
     * Generates the page sequences for the document.
     * @param builder StringBuilder to append generated FO
     * @param document {@link Document} representing the content structure
     * @param styleSheet {@link StyleSheet} defining styles
     * @param headlines List of headlines for bookmarks
     * @param resolver {@link ImageResolver} for image handling
     */
    @Internal
    private void generatePageSequences(StringBuilder builder, Document document, StyleSheet styleSheet, List<Headline> headlines, ImageResolver resolver) {
        for (PageSequence sequence : document.pageSequences()) {
            log.debug("Generating page-sequence with master-reference '{}'.", sequence.styleClass());

            GenerateUtils.TagBuilder pageSeq = GenerateUtils.tagBuilder(PAGE_SEQUENCE)
                    .addAttribute(MASTER_REFERENCE, sequence.styleClass());

            // Header
            if (sequence.header() != null) {
                pageSeq.addChild(
                        createStaticContent(XSL_REGION_BEFORE, sequence.header().elements(),
                                styleSheet, headlines, resolver)
                );
            }

            // Footer
            if (sequence.footer() != null) {
                pageSeq.addChild(
                        createStaticContent(XSL_REGION_AFTER, sequence.footer().elements(),
                                styleSheet, headlines, resolver)
                );
            }
            // Body
            pageSeq.addChild(
                    createFlow(sequence.body().elements(),
                            styleSheet, headlines, resolver)
            );
            pageSeq.buildInto(builder);
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

        log.debug("Generating root start. With language '{}', default font family '{}'.", lang, defaultFontFamily);

        foBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append("<fo:root xmlns:fo=\"http://www.w3.org/1999/XSL/Format\"")
                .append(" xmlns:fox=\"http://xmlgraphics.apache.org/fop/extensions\"")
                .append(" xml:lang=\"").append(GenerateUtils.escapeXml(lang)).append("\" ");

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

        log.debug("Generating XMP metadata for PDF/UA-1 compliance.");

        GenerateUtils.TagBuilder title = GenerateUtils.tagBuilder("title")
                .withPrefix("dc:")
                .addChild(
                        GenerateUtils.tagBuilder("Alt")
                                .withPrefix("rdf:")
                                .addChild(
                                        GenerateUtils.tagBuilder("li")
                                                .withPrefix("rdf:")
                                                .addAttribute("xml:lang", "x-default")
                                                .addContent(metadata.getTitle())
                                )
                );

        GenerateUtils.TagBuilder description = GenerateUtils.tagBuilder("Description")
                .withPrefix("rdf:")
                .addAttribute("rdf:about", "")
                .addChild(title);

        if (metadata.getAuthor() != null) {
            description.addChild(
                    GenerateUtils.tagBuilder("creator")
                            .withPrefix("dc:")
                            .addContent(metadata.getAuthor())
            );
        }

        if (metadata.getSubject() != null) {
            description.addChild(
                    GenerateUtils.tagBuilder("description")
                            .withPrefix("dc:")
                            .addContent(metadata.getSubject())
            );
        }

        GenerateUtils.tagBuilder("declarations")
                .addChild(
                        GenerateUtils.tagBuilder("xmpmeta")
                                .withPrefix("x:")
                                .addChild(
                                        GenerateUtils.tagBuilder("RDF")
                                                .withPrefix("rdf:")
                                                .addChild(description)
                                )
                )
                .buildInto(foBuilder);
    }

    private void generateLayoutMasterSet(StringBuilder foBuilder, StyleSheet styleSheet) {
        GenerateUtils.TagBuilder layoutMasterSet = GenerateUtils.tagBuilder(LAYOUT_MASTER_SET);

        if (styleSheet.pageMasterStyles() != null) {
            for (PageMasterStyle pageStyle : styleSheet.pageMasterStyles()) {
                GenerateUtils.TagBuilder simpleMaster = GenerateUtils.tagBuilder(SIMPLE_PAGE_MASTER)
                        .addAttribute(MASTER_NAME, pageStyle.getName())
                        .addAttribute(PAGE_HEIGHT, pageStyle.getPageHeight())
                        .addAttribute(PAGE_WIDTH, pageStyle.getPageWidth());

                // Margin handling
                if (pageStyle.getMargin() != null && !pageStyle.getMargin().isEmpty()) {
                    simpleMaster.addAttribute(MARGIN, pageStyle.getMargin());
                } else {
                    simpleMaster
                            .addAttribute(MARGIN_TOP, pageStyle.getMarginTop())
                            .addAttribute(MARGIN_BOTTOM, pageStyle.getMarginBottom())
                            .addAttribute(MARGIN_LEFT, pageStyle.getMarginLeft())
                            .addAttribute(MARGIN_RIGHT, pageStyle.getMarginRight());
                }

                // Region body
                GenerateUtils.TagBuilder regionBody = GenerateUtils.tagBuilder(REGION_BODY)
                        .addAttribute(MARGIN_TOP, pageStyle.getHeaderExtent())
                        .addAttribute(MARGIN_BOTTOM, pageStyle.getFooterExtent())
                        .addAttribute(COLUMN_COUNT, pageStyle.getColumnCount())
                        .addAttribute(COLUMN_GAP, pageStyle.getColumnGap());

                simpleMaster.addChild(regionBody);

                // Region before (header)
                if (pageStyle.getHeaderExtent() != null) {
                    simpleMaster.addChild(
                            GenerateUtils.tagBuilder(REGION_BEFORE)
                                    .addAttribute(REGION_NAME, XSL_REGION_BEFORE)
                                    .addAttribute(EXTENT, pageStyle.getHeaderExtent())
                    );
                }

                // Region after (footer)
                if (pageStyle.getFooterExtent() != null) {
                    simpleMaster.addChild(
                            GenerateUtils.tagBuilder(REGION_AFTER)
                                    .addAttribute(REGION_NAME, XSL_REGION_AFTER)
                                    .addAttribute(EXTENT, pageStyle.getFooterExtent())
                    );
                }

                layoutMasterSet.addChild(simpleMaster);
            }
        }

        layoutMasterSet.buildInto(foBuilder);
    }

    private void generateRootEnd(StringBuilder foBuilder) {
        foBuilder.append("</fo:root>");
    }

    /**
     * Find the default-text-style if set. This text-style would be set als
     * default for the hole document
     * @param styleSheet StyleSheet of this document
     * @return identifier of the default font-family if default text style is set, null otherwise
     */
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
                .flatMap(styleSheet::findFontStyleByName)
                .map(TextStyle::fontFamilyName)
                .orElse(null);
    }


    /**
     * TagBuilder to create a static-content for header or footer of the document
     * @param flowName name of the flow
     * @param elements List of elements to place in the document
     * @param styleSheet {@link StyleSheet} of the document
     * @param headlines List of {@link Headline} elements (for bookmark generation)
     * @param resolver {@link ImageResolver} to find image resources
     * @return TagBuilder to build the tag
     */
    private GenerateUtils.TagBuilder createStaticContent(String flowName, List<Element> elements,
                                                         StyleSheet styleSheet, List<Headline> headlines,
                                                         ImageResolver resolver) {
        StringBuilder content = new StringBuilder();
        generateBlockElements(elements, styleSheet, content, headlines, resolver, true);

        return GenerateUtils.tagBuilder(STATIC_CONTENT_TAG)
                .addAttribute(GenerateConst.FLOW_NAME, flowName)
                .addNestedContent(content.toString());
    }

    /**
     * Generates an flow tag (used for the body)
     * @param elements list of elements that should be added to the flox
     * @param styleSheet {@link StyleSheet} used in the document
     * @param headlines List of {@link Headline} in this document for bookmark tree
     * @param resolver {@link ImageResolver} to find image resources
     * @return {@link GenerateUtils.TagBuilder} to create the flow tag
     */
    private GenerateUtils.TagBuilder createFlow(List<Element> elements,
                                                StyleSheet styleSheet, List<Headline> headlines,
                                                ImageResolver resolver) {
        StringBuilder content = new StringBuilder();
        generateBlockElements(elements, styleSheet, content, headlines, resolver, false);

        return GenerateUtils.tagBuilder(FLOW_TAG)
                .addAttribute(GenerateConst.FLOW_NAME, XSL_REGION_BODY)
                .addNestedContent(content.toString());
    }

}