package de.fkkaiser.model.style;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Optional;

/**
 * Concrete style properties for a paragraph element.
 */
@JsonTypeName(StyleTargetTypes.PARAGRAPH)
public class ParagraphStyleProperties extends TextBlockStyleProperties {

    @JsonProperty("text-indent")
    private String textIndent;

    @JsonProperty("text-align")
    private String textAlign; //justify..

    @JsonProperty("text-align-last")
    private String textAlignLast; //start, end...

    @JsonProperty("hyphenate")
    private boolean hyphenate;

    @JsonProperty("language")
    private String language;

    @JsonProperty("orphans")
    private boolean orphans;

    @JsonProperty("widows")
    private boolean widows;

    // --- Getters and Setters ---

    public String getTextIndent() {
        return textIndent;
    }

    public void setTextIndent(String textIndent) {
        this.textIndent = textIndent;
    }

    public boolean isWidows() {
        return widows;
    }

    public void setWidows(boolean widows) {
        this.widows = widows;
    }

    @Override
    public String getTextAlign() {
        return textAlign;
    }

    @Override
    public void setTextAlign(String textAlign) {
        this.textAlign = textAlign;
    }

    public String getTextAlignLast() {
        return textAlignLast;
    }

    public void setTextAlignLast(String textAlignLast) {
        this.textAlignLast = textAlignLast;
    }

    public boolean isHyphenate() {
        return hyphenate;
    }

    public void setHyphenate(boolean hyphenate) {
        this.hyphenate = hyphenate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isOrphans() {
        return orphans;
    }

    public void setOrphans(boolean orphans) {
        this.orphans = orphans;
    }


    // --- Overrides ---

    @Override
    public void mergeWith(ElementStyleProperties base) {
        super.mergeWith(base);
        if (base instanceof ParagraphStyleProperties baseParagraph) {
            this.textIndent = Optional.ofNullable(this.textIndent).orElse(baseParagraph.textAlign);
            this.textAlign = Optional.ofNullable(this.textAlign).orElse(baseParagraph.textAlignLast);
            this.textAlignLast = Optional.ofNullable(this.textAlignLast).orElse(baseParagraph.textAlignLast);
            this.language = Optional.ofNullable(this.language).orElse(baseParagraph.language);
        }
    }

    @Override
    public ParagraphStyleProperties copy() {
        ParagraphStyleProperties newInstance = new ParagraphStyleProperties();
        applyPropertiesTo(newInstance);

        return newInstance;
    }

    @Override
    protected void applyPropertiesTo(ElementBlockStyleProperties target) {
        super.applyPropertiesTo(target);
        if (target instanceof ParagraphStyleProperties paragraphTarget) {
            paragraphTarget.textIndent = this.textIndent;
            paragraphTarget.textAlign = this.textAlign;
            paragraphTarget.textAlignLast = this.textAlignLast;
            paragraphTarget.hyphenate = this.hyphenate;
            paragraphTarget.language = this.language;
            paragraphTarget.orphans = this.orphans;
            paragraphTarget.widows = this.widows;
        }
    }
}
