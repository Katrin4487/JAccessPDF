package de.kaiser.model.style;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PageMasterStyleTest {

    @Test
    void shouldCreateObjectWithNullFieldsForNoArgsConstructor() {
        // When a new PageMasterStyle is created without arguments
        PageMasterStyle style = new PageMasterStyle();

        // Then it should not be null, but all its fields should be
        assertNotNull(style, "The object should not be null.");
        assertNull(style.getName(), "Name should be null.");
        assertNull(style.getPageHeight(), "PageHeight should be null.");
        assertNull(style.getPageWidth(), "PageWidth should be null.");
        assertNull(style.getMargin(), "Margin should be null.");
        assertNull(style.getMarginTop(), "MarginTop should be null.");
        assertNull(style.getMarginBottom(), "MarginBottom should be null.");
        assertNull(style.getMarginLeft(), "MarginLeft should be null.");
        assertNull(style.getMarginRight(), "MarginRight should be null.");
        assertNull(style.getHeaderExtent(), "HeaderExtent should be null.");
        assertNull(style.getFooterExtent(), "FooterExtent should be null.");
    }

    @Test
    void shouldSetAllFieldsCorrectlyWhenUsingAllArgsConstructor() {
        PageMasterStyle style = new PageMasterStyle(
                "Standard Layout",
                "29.7cm",
                "21cm",
                "2cm",
                "2cm",
                "2.5cm",
                "2.5cm",
                "1.5cm",
                "1.5cm"
        );

        assertAll("All properties should be set by the constructor",
                () -> assertEquals("Standard Layout", style.getName()),
                () -> assertEquals("29.7cm", style.getPageHeight()),
                () -> assertEquals("21cm", style.getPageWidth()),
                () -> assertEquals("2cm", style.getMarginTop()),
                () -> assertEquals("2cm", style.getMarginBottom()),
                () -> assertEquals("2.5cm", style.getMarginLeft()),
                () -> assertEquals("2.5cm", style.getMarginRight()),
                () -> assertEquals("1.5cm", style.getHeaderExtent()),
                () -> assertEquals("1.5cm", style.getFooterExtent())
        );
    }
}