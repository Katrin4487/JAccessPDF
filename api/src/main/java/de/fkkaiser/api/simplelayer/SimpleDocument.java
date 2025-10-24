package de.fkkaiser.api.simplelayer;

import de.fkkaiser.api.PdfGenerationFacade;
import de.fkkaiser.api.utils.EResourceProvider;
import de.fkkaiser.model.font.FontFamilyList;
import de.fkkaiser.model.structure.*;
import de.fkkaiser.model.style.StyleSheet;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a finalized, "built" PDF document.
 * This object is immutable and can be safely exported.
 *
 * <pre>
 * SimpleDocument doc = SimpleDocumentBuilder.create("My PDF")
 * .addParagraph("Hello World")
 * .build();
 * doc.saveAs("output.pdf");
 * </pre>
 */
public class SimpleDocument {

    // Final, immutable models
    private final Document document;
    private final StyleSheet styleSheet;
    private final FontFamilyList fontFamilyList;
    private final EResourceProvider resourceProvider;

    // Internal style manager, not needed after construction
    // private final SimpleStyleManager styleManager;

    /**
     * Internal constructor, called by SimpleDocumentBuilder.
     * This constructor does the "heavy lifting" of converting the simple
     * elements into the complex internal document model.
     */
    SimpleDocument(ArrayList<ContentElement> elements,
                   SimpleStyleManager styleManager,
                   SimpleFontManager fontManager,
                   Metadata metadata,
                   EResourceProvider resourceProvider) {

        // Build final models
        this.styleSheet = styleManager.buildStyleSheet();
        this.fontFamilyList = fontManager.buildFontFamilyList();
        this.resourceProvider = resourceProvider;

        // Cleanup 4: Pass elements directly to buildDocument, don't store as field
        this.document = buildDocument(elements, styleManager, metadata);

        // We no longer need the styleManager or fontManager as fields,
        // as the styleSheet and fontFamilyList are now built.
        // this.styleManager = styleManager;
    }

    /**
     * Saves the PDF to the specified file path.
     */
    public void saveAs(String filePath) throws Exception {
        // Cleanup 3: No need for ensureBuilt()
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
        // Cleanup 3: No need for ensureBuilt()
        return generatePDF();
    }

    // === Private Helper Methods ===

    private ByteArrayOutputStream generatePDF() throws Exception {
        PdfGenerationFacade facade = new PdfGenerationFacade();
        return facade.generatePDF(document, styleSheet, fontFamilyList, resourceProvider);
    }

    /**
     * Converts the simple builder elements into the final document model.
     */
    private Document buildDocument(ArrayList<ContentElement> elements,
                                   SimpleStyleManager styleManager,
                                   Metadata metadata) {

        // Convert elements to actual Paragraph/Headline objects
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

    // === Internal Element Classes (as before) ===

    interface ContentElement {
        Element toModelObject(SimpleStyleManager styleManager);
    }

    static class ParagraphElement implements ContentElement {
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

    record HeadingElement(String text, int level) implements ContentElement {

        @Override
        public Element toModelObject(SimpleStyleManager styleManager) {
            String styleName = styleManager.getHeadingStyleName(level);
            return new Headline(styleName, text, this.level);
        }
    }
}
