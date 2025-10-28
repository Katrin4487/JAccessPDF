package de.fkkaiser.model.font;

/**
 * Helper enum for default font styles
 */
public enum DefaultFontStyles {

    NORMAL,
    BOLD,
    ITALIC,
    OBLIQUE,
    BOLD_OBLIQUE,
    BOLD_ITALIC;

    /**
     * Returns the FontStyleValue for the cases normal, bold, italic and oblique.
     * Helps to create FontTypes.
     * @return font style value for the chosen stlye (italic, oblique, normal)
     */
    @SuppressWarnings("unused")
    public FontStyleValue fontStyleValue() {
        return switch (this){
            case BOLD_ITALIC, ITALIC -> FontStyleValue.ITALIC;
            case BOLD_OBLIQUE,OBLIQUE -> FontStyleValue.OBLIQUE;
            default -> FontStyleValue.NORMAL;
        };
    }

    /**
     * Returns the default font weights for normal(=400), bold(=700), italic(=400) and oblique(=400).
     * @return String representing the weight
     */
    @SuppressWarnings("unused")
    public String fontWeight(){
        return switch (this){
            case BOLD,BOLD_ITALIC,BOLD_OBLIQUE -> "700";
            default -> "400";
        };
    }

    /**
     * Returns the default name of the font style
     * @return name of the font name
     */
    @SuppressWarnings("unused")
    public String fontStyleName(){
        return this.fontStyleValue().name();
    }
}
