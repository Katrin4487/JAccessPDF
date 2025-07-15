package de.kaiser.model.style;

/**
 * Defines the available 'target-element' constants for style definitions.
 * Using these constants prevents typos in the style JSON configuration.
 */
public final class StyleTargetTypes {

    /** Prevents instantiation of the class. */
    private StyleTargetTypes() {}

    public static final String PARAGRAPH = "paragraph";
    public static final String HEADLINE = "headline";
    public static final String LIST = "list";
    public static final String TABLE = "table";
    public static final String TABLE_CELL = "table-cell";
    public static final String SECTION = "section";
    public static final String TEXT_RUN = "text-run";
    public static final String FOOTNOTE = "footnote";
    public static final String PART = "part";
}

