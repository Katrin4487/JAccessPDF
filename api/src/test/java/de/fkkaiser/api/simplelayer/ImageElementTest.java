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
package de.fkkaiser.api.simplelayer;

import de.fkkaiser.model.structure.BlockImage;
import de.fkkaiser.model.structure.Element;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Regression test for {@link SimpleDocument.ImageElement#toModelObject}: its two
 * optional content fields (svgContent, base64Data) must land in the matching
 * {@link BlockImage} getters, not swapped. {@link SimpleDocumentBuilder} does not
 * currently expose a way to set either field, so this constructs the (package-private)
 * record directly - it still guards the mapping for whenever that builder support is added.
 */
@DisplayName("SimpleDocument.ImageElement Tests")
class ImageElementTest {

    @Test
    @DisplayName("Should map svgContent and base64Data to their matching BlockImage getters, not swapped")
    void shouldNotSwapSvgContentAndBase64Data() {
        SimpleDocument.ImageElement imageElement = new SimpleDocument.ImageElement(
                "images/logo.png", "Logo", "<svg>raw-svg-marker</svg>", "YmFzZTY0LW1hcmtlcg==");

        Element result = imageElement.toModelObject(new SimpleStyleManager());

        BlockImage blockImage = (BlockImage) result;
        assertEquals("<svg>raw-svg-marker</svg>", blockImage.getSvgContent());
        assertEquals("YmFzZTY0LW1hcmtlcg==", blockImage.getBase64Data());
    }
}
