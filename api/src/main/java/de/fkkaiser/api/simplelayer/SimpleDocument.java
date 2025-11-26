package de.fkkaiser.api.simplelayer;

import de.fkkaiser.api.PdfGenerationException;
import de.fkkaiser.api.PdfGenerationFacade;
import de.fkkaiser.api.utils.EResourceProvider;
import de.fkkaiser.model.font.FontFamilyList;
import de.fkkaiser.model.structure.*;
import de.fkkaiser.model.style.StyleSheet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static de.fkkaiser.api.simplelayer.SimpleStyleManager.*;

/**
 * Represents a finalized, "built" PDF document.
 * This object is immutable and can be safely exported.
 *
 * <pre>{@code
 * SimpleDocument doc = SimpleDocumentBuilder.create("My PDF")
 *  .addParagraph("Hello World")
 *  .build();
 *  doc.saveAs("output.pdf");
 * }
 *
 * </pre>
 *
 * @author Katrin Kaiser
 * @version 1.1.0
 */
public class SimpleDocument {

    // Final, immutable models
    private final Document document;
    private final StyleSheet styleSheet;
    private final FontFamilyList fontFamilyList;
    private final EResourceProvider resourceProvider;

    /**
     * Internal constructor used by SimpleDocumentBuilder.
     * Converts simple elements into the document model.
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

        // Pass elements directly to buildDocument, don't store as field
        this.document = buildDocument(elements, styleManager, metadata);
    }

    /**
     * Saves the pdf file to the given file path.
     *
     * @param filePath path where to save this file
     * @throws PdfGenerationException if it was impossible to generate PDF
     * @throws IOException            if it was not possible to save the file
     */
    public void saveAs(String filePath) throws IOException, PdfGenerationException {
        ByteArrayOutputStream stream = generatePDF();

        Path outputPath = Paths.get(filePath);
        try (OutputStream fileOutputStream = Files.newOutputStream(outputPath)) {
            stream.writeTo(fileOutputStream);
        }
    }

    /**
     * Generates the PDF and returns it as a ByteArrayOutputStream.
     *
     * @return {@link java.io.ByteArrayOutputStream} of the PDF.
     * @throws PdfGenerationException if it was not possible to generate the PDF.
     */
    public ByteArrayOutputStream toStream() throws PdfGenerationException {
        return generatePDF();
    }

    // === Private Helper Methods ===

    /**
     * Generates PDF using the facade.
     */
    private ByteArrayOutputStream generatePDF() throws PdfGenerationException {
        PdfGenerationFacade facade = new PdfGenerationFacade(resourceProvider);
        return facade.generatePDF(document, styleSheet, fontFamilyList);
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

        // Create a content area
        ContentArea bodyContent = new ContentArea(contentElements);

        // Create a page sequence with the default page master
        PageSequence sequence = PageSequence.builder(PAGE_MASTER_STYLE_NAME)
                .body(bodyContent)
                .build();

        // Create a document
        return new Document(null, metadata, Collections.singletonList(sequence));
    }

    // === Internal Element Classes ===

    /**
     * Internal interface for elements that can be converted to model objects.
     */
    interface ContentElement {
        Element toModelObject(SimpleStyleManager styleManager);
    }

    record ParagraphElement(String text, String styleName) implements ContentElement {

        @Override
        public Element toModelObject(SimpleStyleManager styleManager) {
            return new Paragraph(styleName, text);
        }
    }

    /**
     * Represents a list element that can be included in a document.
     * This class supports both ordered and unordered lists, with each list item containing
     * a paragraph of text.
     *
     * <p>The list element is built into a model object during document processing, where each
     * item in the list is converted into a structured {@link de.fkkaiser.model.structure.ListItem}
     * object within a {@link de.fkkaiser.model.structure.SimpleList}.</p>
     *
     * @param items     the list of strings that form the content of the list items
     * @param styleName the name of the style class to be applied to this list element
     * @param ordered   {@code true} for ordered lists, {@code false} for unordered lists
     */
    record ListElement(List<String> items, String styleName, boolean ordered) implements ContentElement {

        @Override
        public Element toModelObject(SimpleStyleManager styleManager) {
            ListOrdering ordering = ordered ? ListOrdering.ORDERED : ListOrdering.UNORDERED;

            List<ListItem> listItems = new ArrayList<>();
            for (String item : items) {
                Paragraph paragraph = new Paragraph(PARAGRAPH_STYLE_NAME, item);
                ListItem listItem = new ListItem(null, null, null, Collections.singletonList(paragraph));
                listItems.add(listItem);
            }
            return new SimpleList(styleName, ordering, listItems);
        }
    }

    /**
     * Represents a heading element in a document. This class implements the {@link ContentElement}
     * interface, allowing it to be converted into a corresponding model object representation.
     * <p>
     * The heading element is characterized by its text content and its heading level, which indicates
     * the level of the heading (e.g., H1, H2, etc.). The heading level determines the specific style that
     * will be applied to this element when converting it to a model object.
     *
     * @param text  The text content of the heading.
     * @param level The level of the heading (e.g., 1 for H1, 2 for H2, etc.).
     */
    record HeadingElement(String text, int level) implements ContentElement {

        @Override
        public Element toModelObject(SimpleStyleManager styleManager) {
            String styleName = PREFIX_HEADINGS_STYLE_NAME + level;
            return new Headline(styleName, text, this.level);
        }
    }

    /**
     * Internal element for an image.
     *
     * @param path    the relative path, e.g. "images/logo.png"
     * @param altText alternative text for the image. If altText is null the image is
     *                interpreted as an artifact
     */
    record ImageElement(String path, String altText) implements ContentElement {
        ImageElement(String path) {
            this(path, null);
        }

        @Override
        public Element toModelObject(SimpleStyleManager styleManager) {

            return new BlockImage(IMAGE_STYLE_NAME, path, altText);
        }
    }

    /**
     * Represents a table.
     *
     * @param simpleTable {@link de.fkkaiser.api.simplelayer.SimpleTable} the represents the table
     */
    record TableElement(SimpleTable simpleTable) implements SimpleDocument.ContentElement {

        @Override
        public Element toModelObject(SimpleStyleManager styleManager) {
            TableSection header = null;
            if (!simpleTable.headerRows.isEmpty()) {
                header = new TableSection(convertRows(simpleTable.headerRows, styleManager));
            }

            TableSection body = null;
            if (!simpleTable.bodyRows.isEmpty()) {
                body = new TableSection(convertRows(simpleTable.bodyRows, styleManager));
            }

            return new Table(
                    simpleTable.styleClass,
                    simpleTable.columns,
                    header,
                    body,
                    null // Footer isn't supported yet
            );
        }

        /**
         * Converts a list of {@link SimpleTableRow} objects into a list of {@link TableRow} objects.
         *
         * @param simpleRows   the list of {@link SimpleTableRow} to be converted; if null, an empty list is returned
         * @param styleManager the {@link SimpleStyleManager} responsible for managing styles during the conversion
         * @return a list of {@link TableRow} objects derived from the input {@link SimpleTableRow} list
         */
        private List<TableRow> convertRows(List<SimpleTableRow> simpleRows, SimpleStyleManager styleManager) {
            // simple rows are neve null
            return simpleRows.stream()
                    .map(simpleRow -> new TableRow(convertCells(simpleRow.cells(), styleManager)))
                    .collect(Collectors.toList());
        }

        /**
         * Converts SimpleTableRow list to TableRow list.
         */
        private List<TableCell> convertCells(List<SimpleTableCell> simpleCells, SimpleStyleManager styleManager) {
            if (simpleCells == null) {
                return Collections.emptyList();
            }
            return simpleCells.stream()
                    .map(simpleCell -> convertCell(simpleCell, styleManager))
                    .collect(Collectors.toList());
        }

        /**
         * Converts a {@link SimpleTableCell} into a {@link TableCell} by transforming its contents
         * and applying the specified style manager for style resolution.
         *
         * @param simpleCell   the {@link SimpleTableCell} to be converted; must not be null
         * @param styleManager the {@link SimpleStyleManager} responsible for managing styles during the conversion; must not be null
         * @return a {@link TableCell} object created from the given {@link SimpleTableCell}, with transformed elements and applied styles
         */
        private TableCell convertCell(SimpleTableCell simpleCell, SimpleStyleManager styleManager) {

            final List<Element> coreElements = simpleCell.elements.stream()
                    .map(contentElement -> contentElement.toModelObject(styleManager))
                    .collect(Collectors.toList());


            return new TableCell(
                    TABLE_CELL_STYLE_NAME,
                    coreElements,
                    simpleCell.colspan,
                    simpleCell.rowspan
            );
        }
    }
}

