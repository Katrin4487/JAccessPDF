package de.fkkaiser.model.structure;

import de.fkkaiser.model.annotation.PublicAPI;

/**
 * Defines the available 'type' constants for inline elements.
 * This class should be used by the library's users,
 * to set the type of element in the JSON structure.
 */
public final class InlineElementTypes {

    /** Prevents instantiation of the class. */
    private InlineElementTypes() {}

    @PublicAPI
    public static final String TEXT_RUN = "text-run";
    @PublicAPI
    public static final String PAGE_NUMBER = "page-number";
    @PublicAPI
    public static final String FOOTNOTE = "footnote";
    @PublicAPI
    public static final String HYPERLINK = "hyperlink";


}
