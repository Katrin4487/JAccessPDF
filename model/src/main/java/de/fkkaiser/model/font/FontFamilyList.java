package de.fkkaiser.model.font;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * List of FontFamily objects that should be used in the pdf.
 */
public class FontFamilyList {

    private static final Logger log = LoggerFactory.getLogger(FontFamilyList.class);

    @JsonProperty("font-families")
    private List<FontFamily> fontFamilyList;

    // --- Constructor ---

    /**
     * Empty constructor for jackson
     */
    public FontFamilyList() {}


    /**
     * Getter method
     * @return current list of FontFamily objects
     */
    public List<FontFamily> getFontFamilyList() {
        return fontFamilyList;
    }

    /**
     * Setter method to set the current FontFamily list
     * @param fontFamilyList list of font families
     */
    public void setFontFamilyList(List<FontFamily> fontFamilyList) {
        this.fontFamilyList = fontFamilyList;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (fontFamilyList == null) {
            return "FontFamilyList (empty)";
        }
        for(int i = 0; i < fontFamilyList.size(); i++){
            FontFamily eFontFamily = fontFamilyList.get(i);
            builder.append(eFontFamily.toString());
            if (i < fontFamilyList.size() - 1) {
                builder.append(",\n");
            }
        }
        return builder.toString();
    }

    public static FontFamilyListBuilder builder(){
        return new FontFamilyListBuilder();
    }

    @SuppressWarnings("unused")
    public static class FontFamilyListBuilder {

        private final List<FontFamily> fontFamilies;

        private String currFamilyName;
        private List<FontType> currFontList;


        private FontFamilyListBuilder() {
            fontFamilies = new ArrayList<>();
        }

        /**
         * Method to start a chain for a FontFamily.
         * After calling this method you can add a new FontType (Font) to the FontFamily
         * with the method addNewFont(...).
         * You can and the chain for this FontFamily with endFontFamily().
         * Notice: Ich a FontFamily is already in progress, it is saved before starting a
         * new one.
         * @param fontFamilyName name of the font family
         * @return This builder instance for fluent chaining.
         */
        public FontFamilyListBuilder addNewFontFamily(String fontFamilyName){
            // If a family is already "in progress", save it before starting a new one.
            if (this.currFamilyName != null) {
                endFontFamily();
            }
            this.currFamilyName = fontFamilyName;
            currFontList = new ArrayList<>();
            return this;
        }

        /**
         * Adds a new font (FontType) to a FontFamily. This method has to be called after
         * the method addNewFontFamily.
         * @param path path where to find the font
         * @param fonstStyleValue correct FontStyleValue (normal, italic)
         * @param weight the font weight as String (e.g 400)
         * @return This builder instance for fluent chaining.
         */
        public FontFamilyListBuilder addNewFont(String path, FontStyleValue fonstStyleValue, String weight){
            if(currFamilyName == null){
                log.error("FontFamilyListBuilder: addNewFont called before addNewFontFamily. Skipping adding Font {}",path);
                return this;
            }
            FontType fontType = new FontType(path,fonstStyleValue,weight);
            this.currFontList.add(fontType);
            return this;
        }

        /**
         * Commits the current font family and returns the builder for fluent chaining.
         */
        public FontFamilyListBuilder endFontFamily(){
            if (currFamilyName != null) {
                this.fontFamilies.add(new FontFamily(currFamilyName, List.copyOf(currFontList)));
                currFamilyName = null;
                currFontList = null; // Set to null to catch errors
            }
            return this;
        }


        /**
         * Builds the final, immutable FontFamily object.
         * @return A new FontFamilyList object.
         */
        public FontFamilyList build() {
            if (currFamilyName != null) {
                log.warn("FontFamilyListBuilder: build() called, but font family '{}' was not explicitly closed with endFontFamily(). Auto-closing.", currFamilyName);
                endFontFamily();
            }

            FontFamilyList fontFamilyList = new FontFamilyList();

            fontFamilyList.setFontFamilyList(List.copyOf(fontFamilies));
            return fontFamilyList;
        }

    }
}
