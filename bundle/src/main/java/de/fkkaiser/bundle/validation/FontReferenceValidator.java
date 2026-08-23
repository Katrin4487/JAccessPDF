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
package de.fkkaiser.bundle.validation;

import de.fkkaiser.model.font.FontFamily;
import de.fkkaiser.model.font.FontFamilyList;
import de.fkkaiser.model.font.FontType;
import de.fkkaiser.model.style.StyleSheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class to check if all referenced fonts are available.
 *
 * @version 1.0
 * @author Katrin Kaiser
 */
public class FontReferenceValidator {

    /**
     * Checks if all referenced fonts in the stylesheet are available in the font family list.
     *
     * @param styleSheet Stylesheet to check
     * @param fonts      Font family list to check against
     * @return List of missing font references, empty if all fonts are available
     */
    public static List<String> findMissingFontReferences(StyleSheet styleSheet, FontFamilyList fonts) {

        List<String> missingFontReferences = new ArrayList<>();

        styleSheet.textStyles().forEach(textStyle -> {
            String fontFamilyName = textStyle.fontFamilyName();
            String fontStyle = textStyle.fontStyle();
            String fontWeight = textStyle.fontWeight();

            Optional<FontFamily> fontFamilyOptional = fonts.getFontFamilyList().stream()
                    .filter(currFontFamily -> currFontFamily.getName().equalsIgnoreCase(fontFamilyName))
                    .findFirst();

            if (fontFamilyOptional.isEmpty()) {
                missingFontReferences.add("TextStyle "+textStyle.name()+" references to missining FontFamily "+fontFamilyName);
            } else {
                FontFamily fontFamily = fontFamilyOptional.get();

                Optional<FontType> fontTypeOptional = fontFamily.fontTypes().stream().filter(fontType -> fontType.fontStyle().toString().equalsIgnoreCase(fontStyle) && fontType.fontWeight().equals(fontWeight))
                        .findFirst();
                if (fontTypeOptional.isEmpty()) {
                    missingFontReferences.add("TextStyle "+textStyle.name()+" references to missing Variant of FontFamily "+fontFamilyName + ":  Missing style/weight: " + fontStyle + "/" + fontWeight);
                }
            }
        });

        return missingFontReferences;
    }
}

