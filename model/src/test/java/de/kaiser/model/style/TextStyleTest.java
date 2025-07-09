package de.kaiser.model.style;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TextStyleTest {

    @Test
    void shouldCreateTextStyleWithCorrectProperties() {
        String name = "headline-font";
        String fontSize = "16px";
        String fontFamilyName = "Arial";
        String fontWeight = "bold";
        String fontStyle = "italic";

        TextStyle textStyle = new TextStyle(name, fontSize, fontFamilyName, fontWeight, fontStyle);

        assertNotNull(textStyle);
        assertEquals(name, textStyle.name());
        assertEquals(fontSize, textStyle.fontSize());
        assertEquals(fontFamilyName, textStyle.fontFamilyName());
        assertEquals(fontWeight, textStyle.fontWeight());
        assertEquals(fontStyle, textStyle.fontStyle());
    }
}