package de.fkkaiser.api.simplelayer;

import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.font.FontFamily;
import de.fkkaiser.model.font.FontFamilyList;
import de.fkkaiser.model.font.FontStyleValue;
import de.fkkaiser.model.font.FontType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static de.fkkaiser.api.simplelayer.SimpleStyleManager.FONT_FAMILY;

/**
 * Manages fonts for {@link SimpleDocument}.
 * <p>
 * <strong>PDF/UA REQUIREMENT:</strong> All fonts MUST be embedded.
 * Default: Uses Open Sans (Apache 2.0 License, Google Fonts).
 * </p>
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
@Internal("Used internally by SimpleDocumentBuilder")
class SimpleFontManager {


    /**
     * Builds the FontFamilyList with embedded fonts.
     * <p>
     * PDF/UA COMPLIANCE:
     * <ul>
     *   <li>All fonts MUST be embedded</li>
     *   <li>Default: Open Sans (Apache 2.0, Google Fonts)</li>
     * </ul>
     * </p>
     *
     * @return configured {@link FontFamilyList} with embedded fonts
     */
    FontFamilyList buildFontFamilyList() {
        FontFamilyList list = new FontFamilyList();
        list.setFontFamilyList(createDefaultOpenSansFonts());

        return list;
    }

    /**
     * Creates default Open Sans (Apache 2.0 License) fonts for PDF/UA compliance.
     * Includes Regular, Bold, Italic, and Bold Italic variants.
     *
     * @return list containing the Open Sans font family
     */
    private List<FontFamily> createDefaultOpenSansFonts() {
        final List<FontType> openSansVariants = new ArrayList<>();

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

        final FontFamily openSans = new FontFamily(FONT_FAMILY, openSansVariants);
        return Collections.singletonList(openSans);
    }

}