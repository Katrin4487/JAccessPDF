package de.fkkaiser.model.style.builder;

import de.fkkaiser.model.style.ElementStyle;
import de.fkkaiser.model.style.ParagraphStyleProperties;
import de.fkkaiser.model.style.StyleTargetTypes;
import de.fkkaiser.model.style.TextStyle;

/**
 * Fluent builder for creating paragraph element styles with detailed properties.
 * This builder allows setting all paragraph-specific properties in a readable,
 * chainable manner.
 *
 */
public  class ParagraphStyleBuilder {
    private final String name;
    private final ParagraphStyleProperties properties;

    public ParagraphStyleBuilder(String name, TextStyle textStyle) {
        this.name = name;
        this.properties = new ParagraphStyleProperties();
        this.properties.setTextStyleName(textStyle.name());
    }

    /**
     * Sets the text algin. The following alignments are available.
     * <ul>
     *     <li>start: Aligns the text at the start edge. In LTR-Languages it's equal to left,
     *     in RTL-Languages it's equal to right.</li>
     *     <li>end: Aligns the text at the end edge. In LTR-Languages it's equal to right,
     *     in RTL-Languages it's equal to left</li>
     *     <li>left: Aligns the text at the left edge</li>
     *     <li>right: Aligns the text at the right edge</li>
     *     <li>center: centers the text</li>
     *     <li>justify: Creates justified text. The text (except the last line) is adjusted to align
     *     flush with both the left and right margins.</li>
     *     <li>Aligns text on a specific character. This is often used in tables to align numbers
     *     by the decimal separator (e.g., text-align=".").</li>
     * </ul>
     *
     * @param textAlign keyword that defines the alignment
     * @return ParagraphStyleBuilder to use this method in the chain
     */
    @SuppressWarnings("unused")
    public ParagraphStyleBuilder withTextAlign(String textAlign) {
        properties.setTextAlign(textAlign);
        return this;
    }

    /**
     * Defines the text indent (space before the text in the first line).
     * You can choose between the units: mm, cm, in, pt, pc, em or %.
     *
     * @param textIndent String with value and unit for the text indent
     * @return ParagraphStyleBuilder to use this method in a chain
     */
    @SuppressWarnings("unused")
    public ParagraphStyleBuilder withTextIndent(String textIndent) {
        properties.setTextIndent(textIndent);
        return this;
    }

    /**
     * Defines the white space before a text block.
     * You can choose between the units: mm, cm, in, pt, pc, em or %.
     *
     * @param spaceBefore String with value and unit for space before
     * @return ParagraphStyleBuilder to use this method in a chain
     */
    public ParagraphStyleBuilder withSpaceBefore(String spaceBefore) {
        properties.setSpaceBefore(spaceBefore);
        return this;
    }

    /**
     * Defines the white space after a text block.
     * You can choose between the units: mm, cm, in, pt, pc, em or %.
     *
     * @param spaceAfter String with value and unit for space after
     * @return ParagraphStyleBuilder to use this method in a chain
     */
    public ParagraphStyleBuilder withSpaceAfter(String spaceAfter) {
        properties.setSpaceAfter(spaceAfter);
        return this;
    }

    /**
     * Defines if automatic splitting (or division) of words at the end of a line is enabled.
     * You must specify the correct language of your text that the hybernation works correctly.
     *
     * @param hyphenate true to enabled hyphenation, else false
     * @return ParagraphStyleBuilder to use this method in a chain
     */
    @SuppressWarnings("unused")
    public ParagraphStyleBuilder withHyphenation(boolean hyphenate) {
        properties.setHyphenate(hyphenate);
        return this;
    }

    /**
     * Defines the language of the paragraph. It only make sense if the paragraph
     * is written in a different language then the document.
     *
     * @param language language as ISO-Code String (e.g. en-US)
     * @return ParagraphStyleBuilder to use this method in a chain
     */
    public ParagraphStyleBuilder withLanguage(String language) {
        properties.setLanguage(language);
        return this;
    }

    /**
     * Defines the background color of this paragraph. Valid color values are:
     * <ul>
     *     <li>HexCode: e.g. #F0F0F0</li>
     *     <li>Color Name: e.g. yellow</li>
     *     <li>RGB-Function: rgb(255,0,0)</li>
     * </ul>
     *
     * @param color background color for this paragraph as string
     * @return ParagraphStyleBuilder to use this method in a chain
     */
    public ParagraphStyleBuilder withBackgroundColor(String color) {
        properties.setBackgroundColor(color);
        return this;
    }

    /**
     * Build method for the ParagraphStyleBuilder.
     * This method must be called at the end of every ParagraphBuilder chain.
     *
     * @return ElementStyle for the paragraph, that should be created with the
     * ParagraphStyleBuilder
     */
    public ElementStyle build() {
        return new ElementStyle(name, StyleTargetTypes.PARAGRAPH, properties);
    }
}