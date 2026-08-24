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
package de.fkkaiser.model.style.builder;

import de.fkkaiser.model.style.TextStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * Class with standard text styles.
 * This styles can be used, if there are no custom styles defined in the document.
 * It is used e.g. in SimpleStyleManager class  (api/simplelayer/SimpleStyleManager.java) to create a default StyleSheet for SimpleDocument.
 *
 * @author Katrin Kaiser
 * @version 1.0
 */
public class StandardTextStyles {

    public static final String PREFIX_HEADINGS_TEXT = "text-heading-";

    public static final String REGULAR_PARAGRAPH_TEXT = "text-default";
    public static final String HEADING_1_TEXT = PREFIX_HEADINGS_TEXT + "1";
    public static final String HEADING_2_TEXT = PREFIX_HEADINGS_TEXT + "2";
    public static final String HEADING_3_TEXT = PREFIX_HEADINGS_TEXT + "3";
    public static final String HEADING_4_TEXT = PREFIX_HEADINGS_TEXT + "4";
    public static final String HEADING_5_TEXT = PREFIX_HEADINGS_TEXT + "5";
    public static final String HEADING_6_TEXT = PREFIX_HEADINGS_TEXT + "6";
    public static final String BOLD_PARAGRAPH_TEXT = "text-bold-default";


    /**
     * Creates default text styles for a given font family
     * @param fontFamilyName name of the font family (should exist)
     * @return List with the available text styles
     */
    public static List<TextStyle> forFontFamily(String fontFamilyName){

        // paragraph text style
        TextStyle normalText = new TextStyle.TextStyleFactory(fontFamilyName)
                .normal(REGULAR_PARAGRAPH_TEXT, "12px");

        TextStyle boldText = new TextStyle.TextStyleFactory(fontFamilyName)
                .bold(BOLD_PARAGRAPH_TEXT, "12px");

        TextStyle h1Text = new TextStyle.TextStyleFactory(fontFamilyName)
                .bold(HEADING_1_TEXT, "24px");

        TextStyle h2Text = new TextStyle.TextStyleFactory(fontFamilyName)
                .bold(HEADING_2_TEXT, "20px");

        TextStyle h3Text = new TextStyle.TextStyleFactory(fontFamilyName)
                .bold(HEADING_3_TEXT, "18px");

        TextStyle h4Text = new TextStyle.TextStyleFactory(fontFamilyName)
                .bold(HEADING_4_TEXT, "16px");

        TextStyle h5Text = new TextStyle.TextStyleFactory(fontFamilyName)
                .bold(HEADING_5_TEXT, "14px");

        TextStyle h6Text = new TextStyle.TextStyleFactory(fontFamilyName)
                .bold(HEADING_6_TEXT, "12px");

        return new ArrayList<>(List.of(normalText, boldText, h1Text, h2Text, h3Text, h4Text, h5Text, h6Text));

    }

}
