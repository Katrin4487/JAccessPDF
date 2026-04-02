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
package de.fkkaiser.generator;

import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ImageUtils.
 * SVG tests run only when Batik is available on the classpath.
 */
class ImageUtilsTest {

    ImageResolver resolver = path -> getClass().getClassLoader().getResource(path);

    @Test
    void testPngImage() {
        String dataUri = ImageUtils.resolveToDataUri(
                "images/img.png",
                resolver
        );

        assertNotNull(dataUri);
        assertTrue(dataUri.startsWith("data:image/png;base64,"));
    }

    @Test
    void testJpegImage() {
        // Test JPEG - funktioniert immer
        String dataUri = ImageUtils.resolveToDataUri(
                "images/img2.jpg",
                resolver
        );

        assertNotNull(dataUri);
        assertTrue(dataUri.startsWith("data:image/jpeg;base64,"));
    }

    @Test
    void testSvgWithBatikAvailable() {

        String dataUri = ImageUtils.resolveToDataUri(
                "images/checkmark.svg",
                resolver
        );

        if (isBatikAvailable()) {
            assertNotNull(dataUri);
            assertTrue(dataUri.startsWith("data:image/png;base64,"));
        } else {
            assertNull(dataUri);
        }
    }

    @Test
    void testSvgHandlerSelection() {
        if (isBatikAvailable()) {
            assertEquals("batik", getCurrentSvgHandlerName());
        } else {
            assertEquals("noop", getCurrentSvgHandlerName());
        }
    }

    private boolean isBatikAvailable() {
        try {
            Class.forName("org.apache.batik.transcoder.image.PNGTranscoder");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private String getCurrentSvgHandlerName() {
        return ImageUtils.getSvgHandlerName();
    }

    @Test
    void testSvgContentToDataUri_withBatik() {
        String svgContent = "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"10\" height=\"10\">"
                + "<rect width=\"10\" height=\"10\" fill=\"black\"/></svg>";

        if (isBatikAvailable()) {
            String dataUri = ImageUtils.svgContentToDataUri(svgContent);
            assertNotNull(dataUri);
            assertTrue(dataUri.startsWith("data:image/png;base64,"));

            // Verify it's valid base64
            String base64Part = dataUri.substring("data:image/png;base64,".length());
            assertDoesNotThrow(() -> Base64.getDecoder().decode(base64Part));
        } else {
            // NoOpSvgHandler should throw SvgConversionException
            assertThrows(Exception.class, () -> ImageUtils.svgContentToDataUri(svgContent));
        }
    }

    @Test
    void testSvgContentToDataUri_nullInput() {
        assertThrows(NullPointerException.class, () -> ImageUtils.svgContentToDataUri(null));
    }

}