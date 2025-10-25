package de.fkkaiser.api.simplelayer;

import de.fkkaiser.api.utils.EClasspathResourceProvider;
import de.fkkaiser.api.utils.EResourceProvider;
import de.fkkaiser.model.structure.Metadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SimpleDocumentBuilder {

    private static final Logger log = LoggerFactory.getLogger(SimpleDocumentBuilder.class);


    // Internal state
    private final String title;
    private final ArrayList<SimpleDocument.ContentElement> elements;
    private final SimpleStyleManager styleManager;
    private final SimpleFontManager fontManager;
    private EResourceProvider resourceProvider; // Can be null (will use default)
    private final Metadata metadata;

    private SimpleDocumentBuilder(String title) {
        this.title = title;
        this.elements = new ArrayList<>();
        this.styleManager = new SimpleStyleManager();
        this.fontManager = new SimpleFontManager();
        // Fix 1: Initialize metadata to prevent NullPointerException
        this.metadata = new Metadata();
        // Set title as initial metadata
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
        return addParagraph(text, "default");
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
     * Proof's if heading level is allowed at this position ad add's the heading with
     * level 1, if the heading level is not allowed.
     */
    public SimpleDocumentBuilder addHeading(String text, int level) {
        if (level < 1 || level > 6) {
            throw new IllegalArgumentException("Heading level must be between 1 and 6");
        }else if(level != 1) {
            boolean found = false;
            for(int i = elements.size() - 1; i >= 0; i--){
                SimpleDocument.ContentElement currElem = elements.get(i);
                if(currElem instanceof SimpleDocument.HeadingElement heading){
                    if(heading.level() == level || heading.level()-1 == level){
                        found = true;
                        break; // everything's fine
                    }
                }
            }
            if(!found){
                log.warn("Heading level {} is not allowed for heading with text {}. Adding heading with level 1", level, text);
                elements.add(new SimpleDocument.HeadingElement(text, 1));
            }else {
                elements.add(new SimpleDocument.HeadingElement(text, level));
            }
        }else {
            elements.add(new SimpleDocument.HeadingElement(text, level));
        }
        return this;
    }

    /**
     * Adds a heading level 1 (convenience method).
     */
    public SimpleDocumentBuilder addHeading(String text) {
        return addHeading(text, 1);
    }

    public SimpleDocumentBuilder addUnorderedList(List<String> items){
        SimpleDocument.ListElement listElement = new SimpleDocument.ListElement(items,styleManager.getUnorderedListName(),false);
        elements.add(listElement);
        return this;
    }

    public SimpleDocumentBuilder addOrderedList(List<String> items){
        SimpleDocument.ListElement listElement = new SimpleDocument.ListElement(items,styleManager.getOrderedListName(),true);
        elements.add(listElement);
        return this;
    }

    /**
     * Adds an image to the document.
     * The path must be relative to the 'resources' folder (e.g., "logo.png" or "images/logo.png").
     * This method ensures the final path is correctly prefixed with "images/" for the generator.
     *
     * @param relativePath The path to the image, relative to 'resources'
     * @param altText alt text for this image
     */
    public SimpleDocumentBuilder addImage(String relativePath,String altText) {
        if (relativePath == null || relativePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Image path cannot be null or empty");
        }

        // Normalize path (remove leading slashes, fix backslashes)
        String finalPath = relativePath.replace("\\", "/");
        if (finalPath.startsWith("/")) {
            finalPath = finalPath.substring(1);
        }

        if (!finalPath.startsWith("images/")) {
            finalPath = "images/" + finalPath;
        }

        elements.add(new SimpleDocument.ImageElement(finalPath,altText));
        return this;
    }

    public SimpleDocumentBuilder addImage(String relativePath) {
       return addImage(relativePath,null);
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
    public SimpleDocumentBuilder withLanguage(String language){
        this.metadata.setLanguage(language);
        return this;
    }

    /**
     * Builds the final, immutable SimpleDocument.
     * This object can then be saved or exported as a stream.
     */
    public SimpleDocument build() {
        // Fix 2: Pass the configured resource provider, or use a default if null
        EResourceProvider providerToUse = (this.resourceProvider != null)
                ? this.resourceProvider
                : new EClasspathResourceProvider();

        return new SimpleDocument(elements, styleManager, fontManager, metadata, providerToUse);
    }

    /**
     * BEREINIGT EINEN BILD-PFAD
     * Diese Logik stellt sicher, dass der Pfad relativ zum Classpath-Root ist
     * und 'images/' enthält.
     * @param relativePath Der vom Nutzer angegebene Pfad (z.B. "logo.png" oder "images/logo.png")
     * @return Der bereinigte, volle Pfad (z.B. "images/logo.png")
     */
    public static String normalizeImagePath(String relativePath) {
        if (relativePath == null) {
            return "";
        }
        // Ersetze Backslashes und entferne führende Slashes
        String cleanPath = relativePath.replace("\\", "/").replaceAll("^/", "");

        if (cleanPath.startsWith("images/")) {
            return cleanPath; // Pfad ist schon korrekt
        }
        return "images/" + cleanPath; // Füge 'images/' Präfix hinzu
    }
}

