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
package de.fkkaiser.bundle.zip;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BundleZipWriterTest {

    private final ObjectMapper mapper = new ObjectMapper();

    private static final String DOCUMENT_JSON = """
            {"metadata":{"title":"Test","author":"Test","language":"en-US"},"page-sequences":[]}
            """;

    private static final String STYLES_JSON = """
            {"page-master-styles":[],"text-styles":[{"name":"normal-text","font-size":"11pt","font-family-name":"Open Sans","font-weight":"400","font-style":"normal"}],"element-styles":[]}
            """;

    private static final String FONTS_JSON = """
            {"font-families":[{"font-family":"Open Sans","types":[{"path":"fonts/OpenSans-Regular.ttf","font-weight":"400","font-style":"normal"}]}]}
            """;

    private static final String TEXT_JSON = """
            {"greeting":{"rich-text":"Hallo"}}
            """;

    @Test
    void shouldWriteAllFourJsonEntriesAndFontFiles() throws Exception {
        byte[] fontBytes = {1, 2, 3, 4};

        byte[] zipBytes = BundleZipWriter.write(DOCUMENT_JSON, STYLES_JSON, FONTS_JSON, TEXT_JSON,
                Map.of("fonts/OpenSans-Regular.ttf", fontBytes));

        Map<String, byte[]> entries = readAllEntries(zipBytes);

        assertEquals(5, entries.size());
        assertEquals(DOCUMENT_JSON, new String(entries.get("document.json"), StandardCharsets.UTF_8));
        assertEquals(STYLES_JSON, new String(entries.get("styles.json"), StandardCharsets.UTF_8));
        assertEquals(FONTS_JSON, new String(entries.get("fonts.json"), StandardCharsets.UTF_8));
        assertEquals(TEXT_JSON, new String(entries.get("text.json"), StandardCharsets.UTF_8));
        assertArrayEquals(fontBytes, entries.get("fonts/OpenSans-Regular.ttf"));
    }

    @Test
    void shouldRoundTripThroughBundleZipReader() throws Exception {
        byte[] fontBytes = {5, 6, 7, 8};

        byte[] zipBytes = BundleZipWriter.write(DOCUMENT_JSON, STYLES_JSON, FONTS_JSON, TEXT_JSON,
                Map.of("fonts/OpenSans-Regular.ttf", fontBytes));

        ZipImportResult result = BundleZipReader.read(new ByteArrayInputStream(zipBytes), mapper);

        assertTrue(result.masterBundle().styleSheet().textStyles().size() == 1);
        assertEquals("Hallo", result.masterBundle().textBundle().getTextContent().get("greeting").getRichText());
        assertEquals(1, result.fontFiles().size());
        assertArrayEquals(fontBytes, result.fontFiles().get("fonts/OpenSans-Regular.ttf"));
    }

    private Map<String, byte[]> readAllEntries(byte[] zipBytes) throws Exception {
        Map<String, byte[]> entries = new java.util.HashMap<>();
        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipBytes), StandardCharsets.UTF_8)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                entries.put(entry.getName(), readAllBytes(zis));
                zis.closeEntry();
            }
        }
        return entries;
    }

    private byte[] readAllBytes(InputStream inputStream) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        return out.toByteArray();
    }
}
