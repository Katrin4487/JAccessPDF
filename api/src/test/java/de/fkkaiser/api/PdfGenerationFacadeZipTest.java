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
package de.fkkaiser.api;

import de.fkkaiser.api.utils.EClasspathResourceProvider;
import de.fkkaiser.api.utils.EResourceProvider;
import de.fkkaiser.bundle.MasterBundle;
import de.fkkaiser.bundle.zip.BundleZipWriter;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PdfGenerationFacadeZipTest {

    private static final String DOCUMENT_JSON = """
            {
              "metadata": { "title": "Test", "author": "Test", "language": "en-US" },
              "page-sequences": [
                {
                  "style-class": "main-content",
                  "body": {
                    "elements": [
                      {
                        "type": "paragraph",
                        "style-class": "standard-paragraph",
                        "inline-elements": [
                          { "type": "text-run", "text": "${greeting}" }
                        ]
                      }
                    ]
                  }
                }
              ]
            }
            """;

    private static final String STYLES_JSON = """
            {
              "page-master-styles": [
                { "name": "main-content", "page-height": "29.7cm", "page-width": "21cm", "margin": "2cm" }
              ],
              "text-styles": [
                { "name": "normal-text", "font-size": "11pt", "font-family-name": "Open Sans", "font-weight": "400", "font-style": "normal" }
              ],
              "element-styles": [
                { "name": "standard-paragraph", "target-element": "paragraph", "properties": { "text-style-name": "normal-text" } }
              ]
            }
            """;

    private static final String FONTS_JSON = """
            {
              "font-families": [
                {
                  "font-family": "Open Sans",
                  "types": [
                    { "path": "fonts/OpenSans-Regular.ttf", "font-weight": "400", "font-style": "normal" }
                  ]
                }
              ]
            }
            """;

    private static final String TEXT_JSON = """
            {
              "greeting": { "rich-text": "Hallo {{name}}" }
            }
            """;

    @Test
    void shouldReadTemplateFromZipWithoutTouchingFontsOrFop() throws Exception {
        byte[] zipBytes = BundleZipWriter.write(DOCUMENT_JSON, STYLES_JSON, FONTS_JSON, TEXT_JSON,
                Map.of("fonts/OpenSans-Regular.ttf", new byte[]{1, 2, 3}));

        EResourceProvider provider = new EClasspathResourceProvider();
        PdfGenerationFacade facade = new PdfGenerationFacade(provider);

        MasterBundle template = facade.readTemplate(new ByteArrayInputStream(zipBytes));

        assertNotNull(template);
        assertEquals(1, template.styleSheet().textStyles().size());
        assertTrue(template.textBundle().getTextContent().containsKey("greeting"));
    }

    @Test
    void shouldGeneratePdfFromZipUsingFontsFromTheZip() throws Exception {
        byte[] realFontBytes = readClasspathResource("fonts/OpenSans-Regular.ttf");
        byte[] zipBytes = BundleZipWriter.write(DOCUMENT_JSON, STYLES_JSON, FONTS_JSON, TEXT_JSON,
                Map.of("fonts/OpenSans-Regular.ttf", realFontBytes));

        EResourceProvider provider = new EClasspathResourceProvider();
        PdfGenerationFacade facade = new PdfGenerationFacade(provider);

        ByteArrayOutputStream pdf = facade.generatePDF(
                new ByteArrayInputStream(zipBytes),
                Map.of(),
                Map.of("name", "Welt"));

        assertTrue(pdf.size() > 0);
    }

    private byte[] readClasspathResource(String name) throws Exception {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(name)) {
            assertNotNull(in, "test resource not found: " + name);
            return in.readAllBytes();
        }
    }
}
