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
package de.fkkaiser.model.style;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PageMasterStyleTest {

    @Test
    void shouldCreateObjectWithDefaultValuesForNoArgsConstructor() {
        // When a new PageMasterStyle is created without arguments
        PageMasterStyle style = new PageMasterStyle();

        // Then it should have default values
        assertNotNull(style, "The object should not be null.");
        assertNull(style.getName(), "Name should be null (required field).");
        assertEquals("29.7cm", style.getPageHeight(), "PageHeight should have A4 default.");
        assertEquals("21cm", style.getPageWidth(), "PageWidth should have A4 default.");
        assertNull(style.getMargin(), "Margin should be null.");
        assertEquals("2cm", style.getMarginTop(), "MarginTop should have default.");
        assertEquals("2cm", style.getMarginBottom(), "MarginBottom should have default.");
        assertEquals("2.5cm", style.getMarginLeft(), "MarginLeft should have default.");
        assertEquals("2.5cm", style.getMarginRight(), "MarginRight should have default.");
        assertEquals("0cm", style.getHeaderExtent(), "HeaderExtent should have default.");
        assertEquals("0cm", style.getFooterExtent(), "FooterExtent should have default.");
        assertEquals("1", style.getColumnCount(), "ColumnCount should be 1.");
        assertEquals("0cm", style.getColumnGap(), "ColumnGap should be 0cm.");
        assertTrue(style.isAutoAdjustMargins(), "AutoAdjustMargins should be true by default.");
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
                "1.5cm",
                "1",
                "0cm",
                true
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
                () -> assertEquals("1.5cm", style.getFooterExtent()),
                () -> assertEquals("1", style.getColumnCount()),
                () -> assertEquals("0cm", style.getColumnGap()),
                () -> assertTrue(style.isAutoAdjustMargins())
        );
    }

    @Test
    void shouldCreateA4PortraitWithSimpleConstructor() {
        PageMasterStyle style = new PageMasterStyle("my-page");

        assertEquals("my-page", style.getName());
        assertEquals("29.7cm", style.getPageHeight());
        assertEquals("21cm", style.getPageWidth());
        assertEquals("2cm", style.getMarginTop());
        assertEquals("2cm", style.getMarginBottom());
        assertEquals("2.5cm", style.getMarginLeft());
        assertEquals("2.5cm", style.getMarginRight());
    }

    // === Validation Tests ===

    @Test
    void shouldThrowExceptionWhenValidatingWithoutName() {
        PageMasterStyle style = new PageMasterStyle();

        NullPointerException exception = assertThrows(NullPointerException.class, style::validate);
        assertTrue(exception.getMessage().contains("name"));
    }

    @Test
    void shouldThrowExceptionWhenValidatingWithEmptyName() {
        PageMasterStyle style = new PageMasterStyle();
        style.setName("   ");

        IllegalStateException exception = assertThrows(IllegalStateException.class, style::validate);
        assertTrue(exception.getMessage().contains("name"));
    }

    @Test
    void shouldValidateSuccessfullyWithValidName() {
        PageMasterStyle style = new PageMasterStyle("valid-name");

        assertDoesNotThrow(style::validate);
    }

    // === Dimension Validation Tests ===

    @Test
    void shouldNormalizeDimensionWithoutUnit() {
        PageMasterStyle style = new PageMasterStyle("test");
        style.setMarginTop("5");

        // Should normalize to "5cm" (default unit)
        assertEquals("5cm", style.getMarginTop());
    }

    @Test
    void shouldNormalizeDimensionWithUnsupportedUnit() {
        PageMasterStyle style = new PageMasterStyle("test");
        style.setPageHeight("11inches");

        // Should normalize to "11cm" (default unit) and log warning
        assertEquals("11cm", style.getPageHeight());
    }

    @Test
    void shouldAcceptValidDimensionWithSupportedUnit() {
        PageMasterStyle style = new PageMasterStyle("test");
        style.setMarginTop("2.5cm");

        assertEquals("2.5cm", style.getMarginTop());
    }

    @Test
    void shouldThrowExceptionForInvalidDimensionFormat() {
        PageMasterStyle style = new PageMasterStyle("test");

        assertThrows(IllegalArgumentException.class, () -> style.setMarginTop("abc"));
    }

    // === Auto-Adjust Margin Tests ===

    @Test
    void shouldAutoAdjustMarginTopWhenSmallerThanHeaderExtent() {
        PageMasterStyle style = new PageMasterStyle("test");
        style.setHeaderExtent("3cm");
        style.setMarginTop("1cm");

        // marginTop should be auto-adjusted to 3cm
        assertEquals("3cm", style.getMarginTop());
    }

    @Test
    void shouldNotAdjustMarginTopWhenLargerThanHeaderExtent() {
        PageMasterStyle style = new PageMasterStyle("test");
        style.setHeaderExtent("2cm");
        style.setMarginTop("5cm");

        // marginTop should remain 5cm
        assertEquals("5cm", style.getMarginTop());
    }

    @Test
    void shouldAutoAdjustMarginBottomWhenSmallerThanFooterExtent() {
        PageMasterStyle style = new PageMasterStyle("test");
        style.setFooterExtent("3cm");
        style.setMarginBottom("1cm");

        // marginBottom should be auto-adjusted to 3cm
        assertEquals("3cm", style.getMarginBottom());
    }

    @Test
    void shouldNotAdjustWhenAutoAdjustMarginsIsDisabled() {
        PageMasterStyle style = new PageMasterStyle("test");
        style.setAutoAdjustMargins(false);
        style.setHeaderExtent("3cm");
        style.setMarginTop("1cm");

        // marginTop should remain 1cm (no auto-adjustment)
        assertEquals("1cm", style.getMarginTop());
    }

    @Test
    void shouldForceMarginTopWithoutAdjustment() {
        PageMasterStyle style = new PageMasterStyle("test");
        style.setHeaderExtent("3cm");
        style.forceMarginTop("1cm");

        // marginTop should be 1cm even though it's less than headerExtent
        assertEquals("1cm", style.getMarginTop());
        // autoAdjustMargins should be disabled
        assertFalse(style.isAutoAdjustMargins());
    }

    @Test
    void shouldForceMarginBottomWithoutAdjustment() {
        PageMasterStyle style = new PageMasterStyle("test");
        style.setFooterExtent("3cm");
        style.forceMarginBottom("1cm");

        assertEquals("1cm", style.getMarginBottom());
        assertFalse(style.isAutoAdjustMargins());
    }

    // === Column Tests ===

    @Test
    void shouldSetColumnGapToZeroWhenColumnCountIsOne() {
        PageMasterStyle style = new PageMasterStyle("test");
        style.setColumnCount("1");
        style.setColumnGap("2cm");

        // columnGap should be forced to 0cm when columnCount is 1
        assertEquals("0cm", style.getColumnGap());
    }

    @Test
    void shouldAllowColumnGapWhenColumnCountIsGreaterThanOne() {
        PageMasterStyle style = new PageMasterStyle("test");
        style.setColumnCount("2");
        style.setColumnGap("1cm");

        assertEquals("1cm", style.getColumnGap());
    }

    @Test
    void shouldHandleInvalidColumnCountGracefully() {
        PageMasterStyle style = new PageMasterStyle("test");
        style.setColumnCount("abc");

        // Should default to "1" and columnGap to "0cm"
        assertEquals("1", style.getColumnCount());
        assertEquals("0cm", style.getColumnGap());
    }

    @Test
    void shouldHandleNegativeColumnCount() {
        PageMasterStyle style = new PageMasterStyle("test");
        style.setColumnCount("-5");

        // Should default to "1" and columnGap to "0cm"
        assertEquals("1", style.getColumnCount());
        assertEquals("0cm", style.getColumnGap());
    }

    // === PageSize Enum Tests ===

    @Test
    void shouldSetPageSizeUsingEnum() {
        PageMasterStyle style = new PageMasterStyle("test");
        style.setPageSize(PageMasterStyle.PageSize.A3_LANDSCAPE);

        assertEquals("42cm", style.getPageWidth());
        assertEquals("29.7cm", style.getPageHeight());
    }

    @Test
    void shouldUseA4PortraitAsDefaultInSimpleConstructor() {
        PageMasterStyle style = new PageMasterStyle("test");

        assertEquals("21cm", style.getPageWidth());
        assertEquals("29.7cm", style.getPageHeight());
    }

    // === Margin Shorthand Tests ===

    @Test
    void shouldSetAllMarginsWhenUsingMarginShorthand() {
        PageMasterStyle style = new PageMasterStyle("test");
        style.setMargin("3cm");

        assertEquals("3cm", style.getMargin());
        assertEquals("3cm", style.getMarginTop());
        assertEquals("3cm", style.getMarginBottom());
        // Note: marginLeft and marginRight are NOT set by setMargin currently
    }

    // === Null Handling Tests ===

    @Test
    void shouldHandleNullDimensionsGracefully() {
        PageMasterStyle style = new PageMasterStyle("test");

        assertDoesNotThrow(() -> {
            style.setMarginTop(null);
            style.setPageHeight(null);
        });
    }
}