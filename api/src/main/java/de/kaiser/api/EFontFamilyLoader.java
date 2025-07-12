package de.kaiser.api;

import de.kaiser.api.utils.EResourceProvider;
import de.kaiser.model.font.FontFamily;
import de.kaiser.model.font.FontFamilyList;
import de.kaiser.model.font.FontType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

/**
 * Helper class to build the font configuration for FOP.
 */
class EFontFamilyLoader {

    private static final Logger log = LoggerFactory.getLogger(EFontFamilyLoader.class);

    private final EResourceProvider resourceProvider;
    private final FontFamilyList fontFamilyList;

    public EFontFamilyLoader(EResourceProvider resourceProvider, FontFamilyList list) {
        this.resourceProvider = resourceProvider;
        this.fontFamilyList = list;
    }

    public String getFontListString() throws IOException {
        if (fontFamilyList == null || fontFamilyList.getFontFamilyList() == null) {
            log.warn("Font family list is null or empty, no fonts will be registered.");
            return "<fonts/>";
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n<fonts>");

        for (FontFamily fontFamily : fontFamilyList.getFontFamilyList()) {
            String fontFamilyName = fontFamily.fontFamily();
            for (FontType fontType : fontFamily.fontTypes()) {
                String fontFilePath = fontType.path();
                URL fontUrl = resourceProvider.getResource(fontFilePath);
                String weight = fontType.fontWeight();
                String style = fontType.fontStyle().toString().toLowerCase();

                if (fontUrl != null) {
                    stringBuilder.append("<font embed-url=\"").append(fontUrl.toExternalForm()).append("\" kerning=\"false\">");
                    stringBuilder.append("<font-triplet name=\"").append(fontFamilyName)
                            .append("\" style=\"").append(style)
                            .append("\" weight=\"").append(weight).append("\"/>");
                    stringBuilder.append("</font>");
                } else {
                    log.warn("Could not find font file resource: {}", fontFilePath);
                }
            }
        }
        stringBuilder.append("</fonts>\n");
        return stringBuilder.toString();
    }
}
