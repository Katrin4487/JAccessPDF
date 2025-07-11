package de.kaiser.model.structure;

/**
 * Defines the available 'type' constants for inline elements.
 * This class should be used by the library's users,
 * to set the type of element in the JSON structure.
 */
public final class InlineElementTypes {

    /** Prevents instantiation of the class. */
    private InlineElementTypes() {}

    public static final String TEXT_RUN = "text-run";
    public static final String PAGE_NUMBER = "page-number";
    public static final String FOOTNOTE = "footnote";
    public static final String HYPERLINK = "hyperlink";


}
