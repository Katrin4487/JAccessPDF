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
import de.fkkaiser.bundle.MasterBundle;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BundleZipReaderTest {

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
    void shouldReadMasterBundleAndFontFilesFromZipStream() throws Exception {
        byte[] fontBytes = new byte[] { 1, 2, 3, 4 };
        ByteArrayInputStream zipStream = new ByteArrayInputStream(createZip(fontBytes));

        ZipImportResult result = BundleZipReader.read(zipStream, mapper);

        MasterBundle bundle = result.masterBundle();
        assertNotNull(bundle);
        assertEquals(0, bundle.styleSheet().pageMasterStyles().size());
        assertEquals("Hallo", bundle.textBundle().getTextContent().get("greeting").getRichText());
        assertEquals(1, result.fontFiles().size());
        assertArrayEquals(fontBytes, result.fontFiles().get("fonts/OpenSans-Regular.ttf"));
    }

    private byte[] createZip(byte[] fontBytes) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream, StandardCharsets.UTF_8)) {
            addEntry(zipOutputStream, "document.json", DOCUMENT_JSON.getBytes(StandardCharsets.UTF_8));
            addEntry(zipOutputStream, "styles.json", STYLES_JSON.getBytes(StandardCharsets.UTF_8));
            addEntry(zipOutputStream, "fonts.json", FONTS_JSON.getBytes(StandardCharsets.UTF_8));
            addEntry(zipOutputStream, "text.json", TEXT_JSON.getBytes(StandardCharsets.UTF_8));
            addEntry(zipOutputStream, "fonts/OpenSans-Regular.ttf", fontBytes);
        }
        return outputStream.toByteArray();
    }

    private static void addEntry(ZipOutputStream zipOutputStream, String name, byte[] content) throws Exception {
        zipOutputStream.putNextEntry(new ZipEntry(name));
        zipOutputStream.write(content);
        zipOutputStream.closeEntry();
    }
}

