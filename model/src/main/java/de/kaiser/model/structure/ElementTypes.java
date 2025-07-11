package de.kaiser.model.structure;

/**
 * Defines the available 'type' constants for content elements.
 * This class should be used by the library's users,
 * to set the type of element in the JSON structure.
 */
public final class ElementTypes {

    /** Prevents instantiation of the class. */
    private ElementTypes() {}

    public static final String PARAGRAPH = "paragraph";
    public static final String HEADLINE = "headline";
    public static final String LIST = "list";
    public static final String TABLE = "table";
    public static final String SECTION = "section";

}
