package de.fkkaiser.api.simplelayer;

import de.fkkaiser.api.utils.EClasspathResourceProvider;
import de.fkkaiser.api.utils.EResourceProvider;
import de.fkkaiser.model.structure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static de.fkkaiser.api.simplelayer.SimpleStyleManager.*;

/**
 * A builder class for constructing a SimpleDocument.
 * This class allows stepwise creation of a document by adding various elements like paragraphs, headings, lists, images, and tables.
 * It supports defining metadata such as title and language for the document, and customization of resource providers for external dependencies.
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
     */
    public static SimpleDocumentBuilder create(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Document title is required (PDF/UA compliance)");
        }
        return new SimpleDocumentBuilder(title);
    }

    /**
     * Adds a paragraph with the given text using default styling.
     */
    public SimpleDocumentBuilder addParagraph(String text) {
        return addParagraph(text, PARAGRAPH_STYLE_NAME);
    }

    /**
     * Adds a paragraph with the given text and named style.
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
     * <br/>
     * Proof's if heading level is allowed at this position ad adds the heading with
     * level 1, if the heading level is not allowed.
     */
    public SimpleDocumentBuilder addHeading(String text, int level) {
        if (level < 1 || level > 6) {
            throw new IllegalArgumentException("Heading level must be between 1 and 6");
        } else if (level != 1) {
            boolean found = false;
            for (int i = elements.size() - 1; i >= 0; i--) {
                SimpleDocument.ContentElement currElem = elements.get(i);
                if (currElem instanceof SimpleDocument.HeadingElement heading) {

                    if (heading.level() == level || heading.level() + 1 == level) {
                        found = true;
                        break; // everything's fine
                    }
                }
            }
            if (!found) {
                log.warn("Heading level {} is not allowed for heading with text {}. Adding heading with level 1", level, text);
                elements.add(new SimpleDocument.HeadingElement(text, 1));
            } else {
                elements.add(new SimpleDocument.HeadingElement(text, level));
            }
        } else {
            elements.add(new SimpleDocument.HeadingElement(text, level));
        }
        return this;
    }

    /**
     * Adds heading level 1 (convenience method).
     */
    public SimpleDocumentBuilder addHeading(String text) {
        return addHeading(text, 1);
    }

    public SimpleDocumentBuilder addUnorderedList(List<String> items) {
        SimpleDocument.ListElement listElement = new SimpleDocument.ListElement(items, UNORDERED_LIST_STYLE_NAME, false);
        elements.add(listElement);
        return this;
    }

    public SimpleDocumentBuilder addOrderedList(List<String> items) {
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
     */
    public SimpleDocumentBuilder addImage(String relativePath, String altText) {
        if (relativePath == null || relativePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Image path cannot be null or empty");
        }

        // Normalize a path (remove leading slashes, fix backslashes)
        String finalPath = relativePath.replace("\\", "/");
        if (finalPath.startsWith("/")) {
            finalPath = finalPath.substring(1);
        }

        if (!finalPath.startsWith("images/")) {
            finalPath = "images/" + finalPath;
        }

        elements.add(new SimpleDocument.ImageElement(finalPath, altText));
        return this;
    }

    public SimpleDocumentBuilder addImage(String relativePath) {
        return addImage(relativePath, null);
    }

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
     */
    public SimpleDocumentBuilder withResourceProvider(EResourceProvider provider) {
        this.resourceProvider = provider;
        return this;
    }

    /**
     * Sets the document's language (e.g., "en-US", "de-DE").
     * Required for PDF/UA compliance.
     */
    public SimpleDocumentBuilder withLanguage(String language) {
        this.metadata.setLanguage(language);
        return this;
    }

    /**
     * Builds the final, immutable SimpleDocument.
     * This object can then be saved or exported as a stream.
     */
    public SimpleDocument build() {
        // Fix 2: Pass the configured resource provider or use a default if null
        EResourceProvider providerToUse = (this.resourceProvider != null)
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
     *         already starts with "images/". Returns an empty string if the input is null.
     */
    public static String normalizeImagePath(String relativePath) {
        if (relativePath == null) {
            return "";
        }
        // Replace backslashes and delete leading slashes
        String cleanPath = relativePath.replace("\\", "/").replaceAll("^/", "");

        if (cleanPath.startsWith("images/")) {
            return cleanPath; // the path is already correct
        }
        return "images/" + cleanPath; // Add 'images/' prefix
    }


    /**
     * Method for test only
     *
     * @return list of content elements of the Simple Document
     */
    List<SimpleDocument.ContentElement> getElements() {
        return elements;
    }
}

