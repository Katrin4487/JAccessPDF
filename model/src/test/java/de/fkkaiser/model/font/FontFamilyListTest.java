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
package de.fkkaiser.model.font;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FontFamilyListTest {

    @Test
    @DisplayName("Should build an empty FontFamilyList")
    public void shouldBuildAnEmptyFontFamilyList() {
        FontFamilyList fontFamilyList = FontFamilyList.builder().build();

        assertTrue(fontFamilyList.getFontFamilyList().isEmpty());
    }

    @Test
    @DisplayName("Should build an FontFamilyList with chaining")
    public void shouldBuildAnFontFamilyListWithChain() {
        FontFamilyList fontFamilyList = FontFamilyList.builder()
                .addFontFamily("Arial")
                .addFont("/apath",FontStyleValue.NORMAL,"400")
                .addFont("/secondpath",FontStyleValue.ITALIC,"400")
                .endFontFamily()
                .addFontFamily("Arial")
                .addFont("/cpath",FontStyleValue.NORMAL,"400")
                .endFontFamily()
                .build();

        assertEquals(2, fontFamilyList.getFontFamilyList().size());
        assertEquals("Arial", fontFamilyList.getFontFamilyList().get(1).fontFamily());
    }

    @Test
    @DisplayName("Should end font family chain automatically")
    public void shouldEndFontFamilyChainAutomatically() {
        FontFamilyList fontFamilyList = FontFamilyList.builder()
                .addFontFamily("Arial")
                .addFont("/apath",FontStyleValue.NORMAL,"400")
                .addFont("/secondpath",FontStyleValue.ITALIC,"400")
                .addFontFamily("Arial")
                .addFont("/cpath",FontStyleValue.NORMAL,"400")
                .build();

        assertEquals(2, fontFamilyList.getFontFamilyList().size());
        assertEquals("Arial", fontFamilyList.getFontFamilyList().get(1).fontFamily());
    }
}
