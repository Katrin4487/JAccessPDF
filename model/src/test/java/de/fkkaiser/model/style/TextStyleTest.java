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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TextStyleTest {

    @Test
    @DisplayName("Should create text style with correct properties")
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