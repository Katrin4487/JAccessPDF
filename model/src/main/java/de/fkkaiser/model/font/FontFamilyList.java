package de.fkkaiser.model.font;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FontFamilyList {

    @JsonProperty("font-families")
    private List<FontFamily> fontFamilyList;

    // --- Constructor ---
    public FontFamilyList() {}


    public List<FontFamily> getFontFamilyList() {
        return fontFamilyList;
    }

    public void setFontFamilyList(List<FontFamily> fontFamilyList) {
        this.fontFamilyList = fontFamilyList;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < fontFamilyList.size(); i++){
            FontFamily eFontFamily = fontFamilyList.get(i);
            builder.append(eFontFamily.toString());
            if (i < fontFamilyList.size() - 1) {
                builder.append(",\n");
            }
        }
        return builder.toString();
    }
}
