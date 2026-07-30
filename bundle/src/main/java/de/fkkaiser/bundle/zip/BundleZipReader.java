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
import de.fkkaiser.bundle.CONSTANTS;
import de.fkkaiser.bundle.JAccessParseException;
import de.fkkaiser.bundle.MasterBundle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class BundleZipReader {


    /**
     * Reads a ZIP stream containing a bundle and returns the MasterBundle and font files.
     * @param zipStream The ZIP stream to read.
     * @param mapper The Jackson mapper to use for deserialization.
     * @return The MasterBundle and font files.
     * @throws JAccessParseException If the ZIP stream is null or if any required entry is missing.
     */
    public static ZipImportResult read(InputStream zipStream, ObjectMapper mapper) throws JAccessParseException {
        if (zipStream == null) {
            throw new JAccessParseException("ZIP stream must not be null");
        }

        try (ZipInputStream zis = new ZipInputStream(zipStream, StandardCharsets.UTF_8)) {
            String documentJson = null;
            String stylesJson = null;
            String fontsJson = null;
            String textJson = null;
            Map<String, byte[]> fontFiles = new HashMap<>();

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    zis.closeEntry();
                    continue;
                }

                String entryName = entry.getName();
                byte[] entryBytes = readEntryBytes(zis);

                switch (entryName) {
                    case CONSTANTS.DOCUMENT_JSON_FILE_NAME -> documentJson = new String(entryBytes, StandardCharsets.UTF_8);
                    case CONSTANTS.STYLES_JSON_FILE_NAME -> stylesJson = new String(entryBytes, StandardCharsets.UTF_8);
                    case CONSTANTS.FONTS_JSON_FILE_NAME -> fontsJson = new String(entryBytes, StandardCharsets.UTF_8);
                    case CONSTANTS.TEXT_JSON_FILE_NAME -> textJson = new String(entryBytes, StandardCharsets.UTF_8);
                    default -> fontFiles.put(entryName, entryBytes);
                }

                zis.closeEntry();
            }

            ensurePresent(documentJson, CONSTANTS.DOCUMENT_JSON_FILE_NAME);
            ensurePresent(stylesJson, CONSTANTS.STYLES_JSON_FILE_NAME);
            ensurePresent(fontsJson,CONSTANTS.FONTS_JSON_FILE_NAME);
            ensurePresent(textJson, CONSTANTS.TEXT_JSON_FILE_NAME);

            return new ZipImportResult(
                    MasterBundle.fromJson(documentJson, stylesJson, fontsJson, textJson, mapper),
                    fontFiles
            );
        } catch (IOException e) {
            throw new JAccessParseException("Failed to read bundle ZIP stream", e);
        }
    }


    // ================================= HELPER METHODS ==================================

    private static void ensurePresent(String value, String entryName) throws JAccessParseException {
        if (value == null) {
            throw new JAccessParseException("Missing required ZIP entry: " + entryName);
        }
    }

    private static byte[] readEntryBytes(ZipInputStream zis) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int read;
        while ((read = zis.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
        return outputStream.toByteArray();
    }
}
