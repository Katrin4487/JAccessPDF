package de.kaiser.api;

import de.kaiser.model.font.FontFamily;
import de.kaiser.model.font.FontFamilyList;
import de.kaiser.model.font.FontStyleValue;
import de.kaiser.model.font.FontType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages fonts for SimpleDocument.
 *<br>
 * PDF/UA REQUIREMENT: All fonts MUST be embedded.
 * Default: Uses Open Sans (Apache 2.0 License, Google Fonts).
 */
class SimpleFontManager {

    private final List<FontFamily> customFonts = new ArrayList<>();


    /**
     * Builds the FontFamilyList.
     * PDF/UA COMPLIANCE:
     * - All fonts MUST be embedded
     * - Default: Open Sans (Apache 2.0, Google Fonts)
     */
    FontFamilyList buildFontFamilyList() {
        FontFamilyList list = new FontFamilyList();

        if (customFonts.isEmpty()) {
            list.setFontFamilyList(createDefaultOpenSansFonts());
        } else {
            list.setFontFamilyList(customFonts);
        }

        return list;
    }

    /**
     * Creates default Open Sans fonts for PDF/UA compliance.
     * Open Sans: Apache 2.0 License, Google Fonts.
     */
    private List<FontFamily> createDefaultOpenSansFonts() {
        List<FontType> openSansVariants = new ArrayList<>();

        // Regular (400)
        openSansVariants.add(new FontType(
                "fonts/OpenSans-Regular.ttf",
                FontStyleValue.NORMAL,
                "400"
        ));

        // Bold (700)
        openSansVariants.add(new FontType(
                "fonts/OpenSans-Bold.ttf",
                FontStyleValue.NORMAL,
                "700"
        ));

        // Italic (400)
        openSansVariants.add(new FontType(
                "fonts/OpenSans-Italic.ttf",
                FontStyleValue.ITALIC,
                "400"
        ));

        // Bold Italic (700)
        openSansVariants.add(new FontType(
                "fonts/OpenSans-BoldItalic.ttf",
                FontStyleValue.ITALIC,
                "700"
        ));

        FontFamily openSans = new FontFamily("Open Sans", openSansVariants);

        return Collections.singletonList(openSans);
    }

}