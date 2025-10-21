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
 *
 * PDF/UA REQUIREMENT: All fonts MUST be embedded.
 * Default: Uses Open Sans (Apache 2.0 License, Google Fonts).
 */
class SimpleFontManager {

    private final List<FontFamily> customFonts = new ArrayList<>();
    private boolean useDefaultEmbeddedFont = true;

    /**
     * Registers a custom font family (single style).
     *
     * @param familyName e.g., "Roboto"
     * @param regularPath e.g., "fonts/Roboto-Regular.ttf"
     */
    void registerFont(String familyName, String regularPath) {
        FontType regularFont = new FontType(regularPath, FontStyleValue.NORMAL, "400");
        FontFamily family = new FontFamily(familyName, Collections.singletonList(regularFont));
        customFonts.add(family);
        useDefaultEmbeddedFont = false;
    }

    /**
     * Registers a font family with multiple weights/styles.
     */
    void registerFontFamily(String familyName, String regularPath, String boldPath) {
        List<FontType> variants = new ArrayList<>();
        variants.add(new FontType(regularPath, FontStyleValue.NORMAL, "400"));
        variants.add(new FontType(boldPath, FontStyleValue.NORMAL, "700"));

        FontFamily family = new FontFamily(familyName, variants);
        customFonts.add(family);
        useDefaultEmbeddedFont = false;
    }

    /**
     * Builds the FontFamilyList.
     *
     * PDF/UA COMPLIANCE:
     * - All fonts MUST be embedded
     * - Default: Open Sans (Apache 2.0, Google Fonts)
     */
    FontFamilyList buildFontFamilyList() {
        FontFamilyList list = new FontFamilyList();

        if (customFonts.isEmpty() && useDefaultEmbeddedFont) {
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

    /**
     * Gets the name of the default font family.
     */
    String getDefaultFontFamily() {
        if (customFonts.isEmpty()) {
            return "Open Sans";
        }
        return customFonts.getFirst().getName();
    }
}