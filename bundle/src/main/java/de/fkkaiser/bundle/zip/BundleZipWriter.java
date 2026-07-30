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

import de.fkkaiser.bundle.CONSTANTS;
import de.fkkaiser.bundle.JAccessParseException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Writes a bundle (document/styles/fonts/text JSON plus font files) into a single
 * ZIP byte array, using the same entry names that {@link BundleZipReader} expects.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
public final class BundleZipWriter {

    private BundleZipWriter() {
    }

    /**
     * Writes the given bundle JSON documents and font files into a ZIP file.
     *
     * @param documentJson the document JSON, written as {@value CONSTANTS#DOCUMENT_JSON_FILE_NAME}
     * @param stylesJson   the stylesheet JSON, written as {@value CONSTANTS#STYLES_JSON_FILE_NAME}
     * @param fontsJson    the font family list JSON, written as {@value CONSTANTS#FONTS_JSON_FILE_NAME}
     * @param textJson     the text bundle JSON, written as {@value CONSTANTS#TEXT_JSON_FILE_NAME}
     * @param fontFiles    font file bytes, keyed by the exact entry name/path referenced
     *                     as "path" in {@code fontsJson} (see {@link BundleZipReader})
     * @return the ZIP file content as a byte array
     * @throws JAccessParseException if the ZIP cannot be written
     */
    public static byte[] write(String documentJson, String stylesJson, String fontsJson, String textJson,
                                Map<String, byte[]> fontFiles) throws JAccessParseException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(byteStream, StandardCharsets.UTF_8)) {
            writeEntry(zipOutputStream, CONSTANTS.DOCUMENT_JSON_FILE_NAME, documentJson);
            writeEntry(zipOutputStream, CONSTANTS.STYLES_JSON_FILE_NAME, stylesJson);
            writeEntry(zipOutputStream, CONSTANTS.FONTS_JSON_FILE_NAME, fontsJson);
            writeEntry(zipOutputStream, CONSTANTS.TEXT_JSON_FILE_NAME, textJson);

            for (Map.Entry<String, byte[]> fontFile : fontFiles.entrySet()) {
                writeEntry(zipOutputStream, fontFile.getKey(), fontFile.getValue());
            }
        } catch (IOException e) {
            throw new JAccessParseException("Failed to write bundle ZIP stream", e);
        }

        return byteStream.toByteArray();
    }

    // ================================= HELPER METHODS ==================================

    private static void writeEntry(ZipOutputStream zipOutputStream, String name, String content) throws IOException {
        writeEntry(zipOutputStream, name, content.getBytes(StandardCharsets.UTF_8));
    }

    private static void writeEntry(ZipOutputStream zipOutputStream, String name, byte[] content) throws IOException {
        zipOutputStream.putNextEntry(new ZipEntry(name));
        zipOutputStream.write(content);
        zipOutputStream.closeEntry();
    }
}
