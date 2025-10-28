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
                .addNewFontFamily("Arial")
                .addNewFont("/apath",FontStyleValue.NORMAL,"400")
                .addNewFont("/secondpath",FontStyleValue.ITALIC,"400")
                .endFontFamily()
                .addNewFontFamily("Arial")
                .addNewFont("/cpath",FontStyleValue.NORMAL,"400")
                .endFontFamily()
                .build();

        assertEquals(2, fontFamilyList.getFontFamilyList().size());
        assertEquals("Arial", fontFamilyList.getFontFamilyList().get(1).fontFamily());
    }

    @Test
    @DisplayName("Should end font family chain automatically")
    public void shouldEndFontFamilyChainAutomatically() {
        FontFamilyList fontFamilyList = FontFamilyList.builder()
                .addNewFontFamily("Arial")
                .addNewFont("/apath",FontStyleValue.NORMAL,"400")
                .addNewFont("/secondpath",FontStyleValue.ITALIC,"400")
                .addNewFontFamily("Arial")
                .addNewFont("/cpath",FontStyleValue.NORMAL,"400")
                .build();

        assertEquals(2, fontFamilyList.getFontFamilyList().size());
        assertEquals("Arial", fontFamilyList.getFontFamilyList().get(1).fontFamily());
    }
}
