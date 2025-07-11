package de.kaiser.model.structure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Simple Inline Element Tests")
class SimpleInlineElementsTest {

    @Test
    @DisplayName("Hyperlink should hold all its properties")
    void hyperlinkShouldHoldAllProperties() {
        // Act
        Hyperlink hyperlink = new Hyperlink("Click here", "link-style", "external", "https://example.com");

        // Assert
        assertEquals("Click here", hyperlink.getText());
        assertEquals("link-style", hyperlink.getStyleClass());
        assertEquals("https://example.com", hyperlink.getHref());
    }

    @Test
    @DisplayName("PageNumber should be created correctly")
    void pageNumberShouldBeCreated() {
        PageNumber pageNumber = new PageNumber("page-num-style", "default");

        // Assert
        assertEquals("page-num-style", pageNumber.getStyleClass());
        assertEquals("default", pageNumber.getVariant());
    }
}