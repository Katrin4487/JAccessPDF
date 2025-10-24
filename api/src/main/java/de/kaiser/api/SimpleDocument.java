package de.kaiser.api;

import de.kaiser.api.utils.EClasspathResourceProvider;
import de.kaiser.api.utils.EResourceProvider;
import de.kaiser.model.font.FontFamilyList;
import de.kaiser.model.structure.*;
import de.kaiser.model.style.StyleSheet;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Simplified API for creating PDF documents with minimal boilerplate.
 * Uses sensible defaults for fonts, styles, and page layout.
 *
 * <pre>
 * SimpleDocument doc = SimpleDocument.create("My PDF")
 *     .addParagraph("Hello World")
 *     .build();
 * doc.saveAs("output.pdf");
 * </pre>
 */
public class SimpleDocument {

    // Internal state
    private final String title;
    private final List<ContentElement> elements;
    private final SimpleStyleManager styleManager;
    private final SimpleFontManager fontManager;
    private EResourceProvider resourceProvider;

    // Cached generated models
    private Document document;
    private StyleSheet styleSheet;
    private FontFamilyList fontFamilyList;

    private Metadata metadata;

    private SimpleDocument(String title) {
        this.title = title;
        this.elements = new ArrayList<>();
        this.styleManager = new SimpleStyleManager();
        this.fontManager = new SimpleFontManager();
        this.resourceProvider = new EClasspathResourceProvider();
        this.metadata = new Metadata(title);
    }

    /**
     * Creates a new SimpleDocument with the given title.
     * The title is used for PDF metadata (required for PDF/UA).
     */
    public static SimpleDocument create(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Document title is required (PDF/UA compliance)");
        }
        return new SimpleDocument(title);
    }

    /**
     * Adds a paragraph with the given text using default styling.
     */
    public SimpleDocument addParagraph(String text) {
        return addParagraph(text, "default");
    }

    /**
     * Adds a paragraph with the given text and named style.
     */
    public SimpleDocument addParagraph(String text, String styleName) {
        if (text == null) {
            throw new IllegalArgumentException("Paragraph text cannot be null");
        }
        elements.add(new ParagraphElement(text, styleName));
        return this;
    }

    /**
     * Adds a heading with the given text.
     * Level 1 = largest, Level 6 = smallest.
     */
    public SimpleDocument addHeading(String text, int level) {
        if (level < 1 || level > 6) {
            throw new IllegalArgumentException("Heading level must be between 1 and 6");
        }
        elements.add(new HeadingElement(text, level));
        return this;
    }

    /**
     * Adds a heading level 1 (convenience method).
     */
    public SimpleDocument addHeading(String text) {
        return addHeading(text, 1);
    }

    /**
     * Sets a custom resource provider (e.g., for custom font locations).
     * By default, uses classpath resources.
     */
    public SimpleDocument withResourceProvider(EResourceProvider provider) {
        this.resourceProvider = provider;
        return this;
    }

    /**
     * Builds the internal model structures.
     * Must be called before saveAs() or toStream().
     */
    public SimpleDocument build() {
        // Build font list
        this.fontFamilyList = fontManager.buildFontFamilyList();

        // Build style sheet
        this.styleSheet = styleManager.buildStyleSheet();

        // Build document structure
        this.document = buildDocument();

        return this;
    }

    /**
     * Saves the PDF to the specified file path.
     */
    public void saveAs(String filePath) throws Exception {
        ensureBuilt();

        ByteArrayOutputStream stream = generatePDF();

        Path outputPath = Paths.get(filePath);
        try (OutputStream fileOutputStream = Files.newOutputStream(outputPath)) {
            stream.writeTo(fileOutputStream);
        }
    }

    /**
     * Generates the PDF and returns it as a ByteArrayOutputStream.
     */
    public ByteArrayOutputStream toStream() throws Exception {
        ensureBuilt();
        return generatePDF();
    }

    // === Private Helper Methods ===

    private void ensureBuilt() {
        if (document == null || styleSheet == null || fontFamilyList == null) {
            throw new IllegalStateException(
                    "Document not built yet. Call build() before saveAs() or toStream()."
            );
        }
    }

    private ByteArrayOutputStream generatePDF() throws Exception {
        PdfGenerationFacade facade = new PdfGenerationFacade();
        return facade.generatePDF(document, styleSheet, fontFamilyList, resourceProvider);
    }

    private Document buildDocument() {
        // Convert elements to actual Paragraph objects
        List<Element> contentElements = new ArrayList<>();
        for (ContentElement element : elements) {
            contentElements.add(element.toModelObject(styleManager));
        }

        // Create content area
        ContentArea bodyContent = new ContentArea(contentElements);

        // Create page sequence with default page master
        PageSequence sequence = PageSequence.builder(styleManager.getDefaultPageMasterName())
                .body(bodyContent)
                .build();

        // Create document
        return new Document(null, metadata, Collections.singletonList(sequence));
    }

    // === Internal Element Classes ===

    private interface ContentElement {
        Element toModelObject(SimpleStyleManager styleManager);
    }

    private static class ParagraphElement implements ContentElement {
        private final String text;
        private final String styleName;

        ParagraphElement(String text, String styleName) {
            this.text = text;
            this.styleName = styleName;
        }

        @Override
        public Element toModelObject(SimpleStyleManager styleManager) {
            String actualStyleName = styleManager.getParagraphStyleName(styleName);
            return new Paragraph(actualStyleName, text);
        }
    }

    private record HeadingElement(String text, int level) implements ContentElement {

        @Override
        public Element toModelObject(SimpleStyleManager styleManager) {
            String styleName = styleManager.getHeadingStyleName(level);
            return new Headline(styleName, text, this.level);
        }
    }

    public SimpleDocument withLanguage(String language){
        this.metadata.setLanguage(language);

        return this;
    }

}
