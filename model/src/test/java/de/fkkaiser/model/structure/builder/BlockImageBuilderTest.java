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
package de.fkkaiser.model.structure.builder;

import de.fkkaiser.model.structure.BlockImage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BlockImageBuilderTest {

    @Test
    @DisplayName("Should build image with path and alt text")
    void shouldBuildWithPathAndAltText() {
        BlockImage image = new BlockImageBuilder("image-style")
                .withPath("/tmp/image.png")
                .withAltText("Company logo")
                .build();

        assertEquals("image-style", image.getStyleClass());
        assertEquals("/tmp/image.png", image.getPath());
        assertEquals("Company logo", image.getAltText());
        assertNull(image.getBase64Data());
        assertNull(image.getSvgContent());
    }

    @Test
    @DisplayName("Should build image with base64 data")
    void shouldBuildWithBase64Data() {
        BlockImage image = new BlockImageBuilder("image-style")
                .withBase64Data("iVBORw0KGgoAAAANSUhEUgAAAAE=")
                .withAltText("Inline bitmap")
                .build();

        assertEquals("image-style", image.getStyleClass());
        assertEquals("Inline bitmap", image.getAltText());
        assertEquals("iVBORw0KGgoAAAANSUhEUgAAAAE=", image.getBase64Data());
        assertNull(image.getPath());
        assertNull(image.getSvgContent());
    }

    @Test
    @DisplayName("Should build image with svg content")
    void shouldBuildWithSvgContent() {
        String svg = "<svg viewBox=\"0 0 10 10\"></svg>";

        BlockImage image = new BlockImageBuilder("image-style")
                .withSvgContent(svg)
                .withAltText("Inline vector")
                .build();

        assertEquals("image-style", image.getStyleClass());
        assertEquals("Inline vector", image.getAltText());
        assertEquals(svg, image.getSvgContent());
        assertNull(image.getPath());
        assertNull(image.getBase64Data());
    }

    @Test
    @DisplayName("Should keep latest source when path is set after base64 and svg")
    void shouldKeepPathWhenPathIsSetLast() {
        BlockImage image = new BlockImageBuilder("image-style")
                .withBase64Data("base64")
                .withSvgContent("<svg></svg>")
                .withPath("images/logo.png")
                .build();

        assertEquals("images/logo.png", image.getPath());
        assertNull(image.getBase64Data());
        assertNull(image.getSvgContent());
    }

    @Test
    @DisplayName("Should keep latest source when base64 data is set after path and svg")
    void shouldKeepBase64WhenBase64IsSetLast() {
        BlockImage image = new BlockImageBuilder("image-style")
                .withPath("images/logo.png")
                .withSvgContent("<svg></svg>")
                .withBase64Data("base64")
                .build();

        assertEquals("base64", image.getBase64Data());
        assertNull(image.getPath());
        assertNull(image.getSvgContent());
    }

    @Test
    @DisplayName("Should keep latest source when svg content is set after path and base64")
    void shouldKeepSvgWhenSvgIsSetLast() {
        BlockImage image = new BlockImageBuilder("image-style")
                .withPath("images/logo.png")
                .withBase64Data("base64")
                .withSvgContent("<svg></svg>")
                .build();

        assertEquals("<svg></svg>", image.getSvgContent());
        assertNull(image.getPath());
        assertNull(image.getBase64Data());
    }

    @Test
    @DisplayName("Should allow null style class")
    void shouldAllowNullStyleClass() {
        BlockImage image = new BlockImageBuilder(null)
                .withPath("images/logo.png")
                .build();

        assertNull(image.getStyleClass());
        assertEquals("images/logo.png", image.getPath());
    }

    @Test
    @DisplayName("Should reject empty style class")
    void shouldRejectEmptyStyleClass() {
        assertThrows(IllegalArgumentException.class, () -> new BlockImageBuilder(""));
    }

    @Test
    @DisplayName("Should reject blank style class")
    void shouldRejectBlankStyleClass() {
        assertThrows(IllegalArgumentException.class, () -> new BlockImageBuilder("   \t  \n"));
    }
}

