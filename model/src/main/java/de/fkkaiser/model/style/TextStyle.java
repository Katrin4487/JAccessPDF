package de.fkkaiser.model.style;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fkkaiser.model.font.FontStyleValue;

/**
 * Represents a named font style that can be referenced by elements.
 * It combines a font family with specific attributes like size, weight, and style,
 * creating a reusable style definition (e.g., "normal-text", "headline-font").
 */
public record TextStyle(
        @JsonProperty("name") String name,
        @JsonProperty("font-size") String fontSize,
        @JsonProperty("font-family-name") String fontFamilyName,
        @JsonProperty("font-weight") String fontWeight,
        @JsonProperty("font-style") String fontStyle
) {
    public String getFontFamilyName() {
        return fontFamilyName;
    }


    @SuppressWarnings("unused")
    public static class TextStyleFactory {

        public static final String BOLD_WEIGHT = "700";
        public static final String  NORMAL_WEIGHT = "400";

        private final String fontFamilyName;

        public TextStyleFactory(String fontFamilyName) {
            this.fontFamilyName = fontFamilyName;
        }

        public TextStyle bold(String name, int fontSize) {
            if(fontSize < 1){
                fontSize = 12;
            }
            return new TextStyle(name,
                    fontSize +"pt",
                    fontFamilyName,BOLD_WEIGHT,
                    FontStyleValue.NORMAL.toString());
        }

        public TextStyle normal(String name, int fontSize) {
            if(fontSize < 1){
                fontSize = 12;
            }
            return new TextStyle(name,
                    fontSize +"pt",
                    fontFamilyName,NORMAL_WEIGHT,
                    FontStyleValue.NORMAL.toString());
        }

        public TextStyle italic(String name, int fontSize) {
            if(fontSize < 1){
                fontSize = 12;
            }
            return new TextStyle(name,
                    fontSize +"pt",
                    fontFamilyName,NORMAL_WEIGHT,
                    FontStyleValue.ITALIC.toString());
        }

        public TextStyle oblique(String name, int fontSize) {
            if(fontSize < 1){
                fontSize = 12;
            }
            return new TextStyle(name,
                    fontSize +"pt",
                    fontFamilyName,NORMAL_WEIGHT,
                    FontStyleValue.OBLIQUE.toString());
        }

        public TextStyle boldOblique(String name, int fontSize) {
            if(fontSize < 1){
                fontSize = 12;
            }
            return new TextStyle(name,
                    fontSize +"pt",
                    fontFamilyName,BOLD_WEIGHT,
                    FontStyleValue.OBLIQUE.toString());
        }

        public TextStyle boldItalic(String name, int fontSize) {
            if(fontSize < 1){
                fontSize = 12;
            }
            return new TextStyle(name,
                    fontSize +"pt",
                    fontFamilyName,BOLD_WEIGHT,
                    FontStyleValue.ITALIC.toString());
        }
    }
}
