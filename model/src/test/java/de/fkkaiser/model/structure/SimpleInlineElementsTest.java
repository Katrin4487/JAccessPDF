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
package de.fkkaiser.model.structure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Simple Inline Element Tests")
class SimpleInlineElementsTest {

    @Test
    @DisplayName("Hyperlink should hold all its properties")
    void hyperlinkShouldHoldAllProperties() {
        // Act
        Hyperlink hyperlink = new Hyperlink("Click here", "link-style", "external", "https://example.com",null);

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