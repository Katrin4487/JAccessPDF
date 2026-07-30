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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.font.FontFamilyList;
import de.fkkaiser.model.style.StyleSheet;
import de.fkkaiser.processor.reader.FontFamilyListReader;
import de.fkkaiser.processor.reader.JsonReadException;
import de.fkkaiser.processor.reader.StyleSheetReader;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Obect that contains all the data needed to generate a PDF document with placeholders
 * @param documentTree JSON tree of the document
 * @param styleSheet Stylesheet
 * @param fonts Font families
 * @param textBundle Text bundle with content
 */
public record MasterBundle(
        JsonNode documentTree,
        StyleSheet styleSheet,
        FontFamilyList fonts,
        TextBundle textBundle
) {
    /**
     * Creates a MasterBundle from JSON
     * @param documentJson JSON of the document, may contain {@code ${key}}/{@code hidden-if} placeholders
     * @param stylesJson JSON of the stylesheet
     * @param fontsJson JSON of the font family list
     * @param textJson JSON object mapping text keys to text entries
     * @param mapper Jackson mapper
     * @return MasterBundle
     * @throws JAccessParseException if any of the JSON inputs cannot be read or parsed
     */
    @PublicAPI
    public static MasterBundle fromJson(String documentJson, String stylesJson,
                                    String fontsJson, String textJson,
                                    ObjectMapper mapper) throws JAccessParseException {
        try {
            JsonNode documentTree = mapper.readTree(documentJson);

            StyleSheet styleSheet = new StyleSheetReader().readJson(
                    new ByteArrayInputStream(stylesJson.getBytes(StandardCharsets.UTF_8)));

            FontFamilyList fonts = new FontFamilyListReader().readJson(
                    new ByteArrayInputStream(fontsJson.getBytes(StandardCharsets.UTF_8)));

            Map<String, TextEntry> textContent =
                    mapper.readValue(textJson, new TypeReference<Map<String, TextEntry>>() {});
            TextBundle textBundle = new TextBundle(textContent);

            return new MasterBundle(documentTree, styleSheet, fonts, textBundle);
        } catch (JsonReadException | java.io.IOException e) {
            throw new JAccessParseException("Failed to parse MasterBundle from JSON", e);
        }
    }
}