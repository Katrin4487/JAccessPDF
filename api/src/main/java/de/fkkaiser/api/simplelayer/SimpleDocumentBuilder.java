package de.fkkaiser.api.simplelayer;

import de.fkkaiser.api.utils.EClasspathResourceProvider;
import de.fkkaiser.api.utils.EResourceProvider;
import de.fkkaiser.model.annotation.VisibleForTesting;
import de.fkkaiser.model.structure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static de.fkkaiser.api.simplelayer.SimpleStyleManager.*;

/**
 * A builder class for constructing a SimpleDocument.
 * This class allows stepwise creation of a document by adding various elements like paragraphs,
 * headings, lists, images, and tables.
 * It supports defining metadata such as title and language for the document, and
 * customization of resource providers for external dependencies.
 *
 * @author FK Kaiser
 * @version 1.0.0
 */
public class SimpleDocumentBuilder {

    private static final Logger log = LoggerFactory.getLogger(SimpleDocumentBuilder.class);

    private final ArrayList<SimpleDocument.ContentElement> elements;
    private final SimpleStyleManager styleManager;
    private final SimpleFontManager fontManager;
    private EResourceProvider resourceProvider; // Can be null (will use default)
    private final Metadata metadata;

    private SimpleDocumentBuilder(String title) {
        // Internal state
        this.elements = new ArrayList<>();
        this.styleManager = new SimpleStyleManager();
        this.fontManager = new SimpleFontManager();
        this.metadata = new Metadata();
        this.metadata.setTitle(title);
    }

    /**
     * Creates a new SimpleDocumentBuilder with the given title.
     * The title is used for PDF metadata (required for PDF/UA).
     *
     * @param title title of the PDF document.
     */
    public static SimpleDocumentBuilder create(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Document title is required (PDF/UA compliance)");
        }
        return new SimpleDocumentBuilder(title);
    }

    /**
     * Adds a paragraph with the given text using default styling.
     *
     * @param text content of the paragraph.
     */
    public SimpleDocumentBuilder addParagraph(String text) {
        return addParagraph(text, PARAGRAPH_STYLE_NAME);
    }

    /**
     * Adds a paragraph with the given text and named style.
     *
     * @param text      content of the paragraph
     * @param styleName style-class of the paragraph
     */
    public SimpleDocumentBuilder addParagraph(String text, String styleName) {
        if (text == null) {
            throw new IllegalArgumentException("Paragraph text cannot be null");
        }
        elements.add(new SimpleDocument.ParagraphElement(text, styleName));
        return this;
    }

    /**
     * Adds a heading with the given text.
     * Level 1 = largest, Level 6 = smallest.
     * <p>
     * <strong>PDF/UA Compliance:</strong> Enforces valid heading hierarchy.
     * You may not skip levels when going deeper (e.g., H1 → H3 is invalid
     * and will be autocorrected to H2). Going up in hierarchy is always allowed.
     * </p>
     *
     * @param text  the heading text
     * @param level the heading level (1-6)
     * @return this builder instance
     * @throws IllegalArgumentException if level is not between 1 and 6
     */
    public SimpleDocumentBuilder addHeading(String text, int level) {
        if (level < 1 || level > 6) {
            throw new IllegalArgumentException("Heading level must be between 1 and 6");
        }

        // level 1 is always ok
        if (level == 1) {
            elements.add(new SimpleDocument.HeadingElement(text, level));
            return this;
        }

        // find last heading...
        int lastHeadingLevel = 0;
        for (int i = elements.size() - 1; i >= 0; i--) {
            if (elements.get(i) instanceof SimpleDocument.HeadingElement heading) {
                lastHeadingLevel = heading.level();
                break;
            }
        }

        // if there is no last heading: only h1 is allowed
        if (lastHeadingLevel == 0) {
            log.warn("First heading must be level 1, got level {}. Adding as level 1.", level);
            elements.add(new SimpleDocument.HeadingElement(text, 1));
            return this;
        }

        // PDF/UA: only jumping one level lower is allowed
        if (level > lastHeadingLevel + 1) {
            int correctedLevel = lastHeadingLevel + 1;
            log.warn("Heading level {} skips levels after H{}. Correcting to level {}. Text: '{}'",
                    level, lastHeadingLevel, correctedLevel, text);
            elements.add(new SimpleDocument.HeadingElement(text, correctedLevel));
        } else {
            // Everything: equal, higher, or one lower
            elements.add(new SimpleDocument.HeadingElement(text, level));
        }

        return this;
    }

    /**
     * Adds heading level 1 (convenience method).
     *
     * @param text content of the heading
     */
    public SimpleDocumentBuilder addHeading(String text) {
        return addHeading(text, 1);
    }

    /**
     * Adds an unordered list.
     *
     * @param items {@link java.util.List} of Strings that forms the content of the items
     * @return this builder instance
     * @throws IllegalArgumentException if items is {@code null} or empty
     */
    public SimpleDocumentBuilder addUnorderedList(List<String> items) {

        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Unordered list cannot be null or empty");
        }
        SimpleDocument.ListElement listElement = new SimpleDocument.ListElement(items, UNORDERED_LIST_STYLE_NAME, false);
        elements.add(listElement);
        return this;
    }

    /**
     * Adds an ordered list.
     *
     * @param items {@link java.util.List} of Strings that forms the content of the items
     * @return this builder instance
     * @throws IllegalArgumentException if items is {@code null} or empty
     */
    public SimpleDocumentBuilder addOrderedList(List<String> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Ordered list cannot be null or empty");
        }
        SimpleDocument.ListElement listElement = new SimpleDocument.ListElement(items, ORDERED_LIST_STYLE_NAME, true);
        elements.add(listElement);
        return this;
    }

    /**
     * Adds an image to the document.
     * The path must be relative to the 'resources' folder (e.g., "logo.png" or "images/logo.png").
     * This method ensures the final path is correctly prefixed with "images/" for the generator.
     *
     * @param relativePath The path to the image, relative to 'resources'
     * @param altText      alt text for this image
     * @throws IllegalArgumentException if relativePath is {@code null} or empty
     */
    public SimpleDocumentBuilder addImage(String relativePath, String altText) {

        String finalPath = normalizeImagePath(relativePath);
        if (finalPath.isEmpty()) {
            throw new IllegalArgumentException("Image path cannot be null or empty");
        }

        elements.add(new SimpleDocument.ImageElement(finalPath, altText));
        return this;
    }

    /**
     * Adds an image (100 % width)
     *
     * @param relativePath relative Path to the image file (e.g. images/myimage.png)
     * @return this builder instance
     */
    public SimpleDocumentBuilder addImage(String relativePath) {
        return addImage(relativePath, null);
    }

    /**
     * Adds a table to the document
     *
     * @param table {@link SimpleTable} that represents the table
     * @return this builder instance
     */
    public SimpleDocumentBuilder addTable(SimpleTable table) {
        if (table == null) {
            throw new IllegalArgumentException("Table object cannot be null");
        }
        elements.add(new SimpleDocument.TableElement(table));
        return this;
    }


    /**
     * Sets a custom resource provider (e.g., for custom font locations).
     * By default, uses classpath resources.
     *
     * @param provider {@link de.fkkaiser.api.utils.EResourceProvider} that should be used
     */
    public SimpleDocumentBuilder withResourceProvider(EResourceProvider provider) {
        this.resourceProvider = provider;
        return this;
    }

    /**
     * Sets the document's language code (e.g., "en-US", "de-DE").
     * Required for PDF/UA compliance.
     *
     * @param language language code in BCP 47 format
     * @return this builder instance
     */
    public SimpleDocumentBuilder withLanguage(String language) {
        this.metadata.setLanguage(language);
        return this;
    }

    /**
     * Builds the final, immutable SimpleDocument.
     * This object can then be saved or exported as a stream.
     *
     * @return the final {@link SimpleDocument}
     */
    public SimpleDocument build() {
        final EResourceProvider providerToUse = (this.resourceProvider != null)
                ? this.resourceProvider
                : new EClasspathResourceProvider();

        return new SimpleDocument(elements, styleManager, fontManager, metadata, providerToUse);
    }

    /**
     * Normalizes a given relative image path by ensuring it uses consistent directory
     * separators, removes leading slashes, and prefixes the path with "images/" if necessary.
     *
     * @param relativePath The relative path to the image that needs normalization.
     *                     Can include leading slashes or backslashes.
     * @return A normalized image path with "images/" prefixed, unless the path
     * already starts with "images/". Returns an empty string if the input is null.
     */
    public static String normalizeImagePath(String relativePath) {
        if (relativePath == null || relativePath.trim().isEmpty()) {
            return "";
        }
        String cleanPath = relativePath.replace("\\", "/").replaceAll("^/", "");
        return cleanPath.startsWith("images/") ? cleanPath : "images/" + cleanPath;
    }


    /**
     * Method for test only
     *
     * @return list of content elements of the Simple Document
     */
    @VisibleForTesting
    List<SimpleDocument.ContentElement> getElements() {
        return elements;
    }
}

