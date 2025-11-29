/*
 * Copyright 2025 Katrin Kaiser
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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