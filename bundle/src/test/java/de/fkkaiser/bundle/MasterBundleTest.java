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
package de.fkkaiser.bundle;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fkkaiser.model.structure.ContentArea;
import de.fkkaiser.model.structure.Document;
import de.fkkaiser.model.structure.Paragraph;
import de.fkkaiser.model.structure.TextRun;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MasterBundleTest {

    private final ObjectMapper mapper = new ObjectMapper();

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
                      },
                      {
                        "type": "conditional-block",
                        "hidden-if": "hideExtra",
                        "elements": [
                          {
                            "type": "paragraph",
                            "style-class": "standard-paragraph",
                            "inline-elements": [
                              { "type": "text-run", "text": "extra content" }
                            ]
                          }
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
              "greeting": { "rich-text": "Hallo {{name}}, willkommen!" }
            }
            """;

    @Test
    void shouldBuildMasterBundleFromJson() throws Exception {
        MasterBundle bundle = MasterBundle.fromJson(DOCUMENT_JSON, STYLES_JSON, FONTS_JSON, TEXT_JSON, mapper);

        assertNotNull(bundle.documentTree());
        assertEquals(1, bundle.styleSheet().textStyles().size());
        assertEquals(1, bundle.fonts().getFontFamilyList().size());
        assertEquals("Hallo {{name}}, willkommen!", bundle.textBundle().getTextContent().get("greeting").getRichText());
    }

    @Test
    void shouldResolveFullPipelineIntoFinalDocument() throws Exception {
        MasterBundle bundle = MasterBundle.fromJson(DOCUMENT_JSON, STYLES_JSON, FONTS_JSON, TEXT_JSON, mapper);

        Document document = MasterBundleEngine.resolve(
                bundle,
                Map.of("hideExtra", true),
                Map.of("name", "Frau Müller"),
                mapper);

        ContentArea body = document.pageSequences().get(0).body();
        assertEquals(1, body.elements().size());

        Paragraph paragraph = (Paragraph) body.elements().get(0);
        TextRun textRun = (TextRun) paragraph.getInlineElements().get(0);
        assertEquals("Hallo Frau Müller, willkommen!", textRun.getText());
    }

    @Test
    void shouldKeepConditionalContentWhenNotHidden() throws Exception {
        MasterBundle bundle = MasterBundle.fromJson(DOCUMENT_JSON, STYLES_JSON, FONTS_JSON, TEXT_JSON, mapper);

        Document document = MasterBundleEngine.resolve(
                bundle,
                Map.of("hideExtra", false),
                Map.of("name", "Frau Müller"),
                mapper);

        ContentArea body = document.pageSequences().get(0).body();
        assertEquals(2, body.elements().size());
    }

    @Test
    void shouldFailWhenDataPlaceholderIsMissing() throws Exception {
        MasterBundle bundle = MasterBundle.fromJson(DOCUMENT_JSON, STYLES_JSON, FONTS_JSON, TEXT_JSON, mapper);

        org.junit.jupiter.api.Assertions.assertThrows(JAccessParseException.class, () ->
                MasterBundleEngine.resolve(bundle, Map.of("hideExtra", true), Map.of(), mapper));
    }
}
