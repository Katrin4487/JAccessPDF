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

import de.fkkaiser.generator.GenerateUtils;
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.font.FontFamily;
import de.fkkaiser.model.font.FontFamilyList;
import de.fkkaiser.model.font.FontType;
import de.fkkaiser.api.utils.EResourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

/**
 * Package-private helper class for building Apache FOP font configuration XML.
 * This class processes a {@link FontFamilyList} and generates the corresponding
 * XML configuration string that FOP requires to register and use custom fonts.
 *
 * <p><b>Thread Safety:</b></p>
 * This class is not thread-safe. Each instance should be used by a single thread,
 * or external synchronization should be applied if shared across threads.

 * <p><b>Note:</b> This class is package-private and intended for internal use
 * within the API package. </p>
 *
 * @param resourceProvider The resource provider used to resolve font file URLs.
 * @param fontFamilyList   The list of font families to be processed and included in the configuration.
 * @author Katrin Kaiser
 * @version 1.1.0
 * @see FontFamilyList
 * @see FontFamily
 * @see FontType
 * @see EResourceProvider
 */
@Internal
record EFontFamilyLoader(EResourceProvider resourceProvider, FontFamilyList fontFamilyList) {

    private static final Logger log = LoggerFactory.getLogger(EFontFamilyLoader.class);

    // XML element and attribute names
    private static final String XML_FONTS = "fonts";
    private static final String XML_FONT = "font";
    private static final String XML_FONT_TRIPLET = "font-triplet";
    private static final String ATTR_EMBED_URL = "embed-url";
    private static final String ATTR_KERNING = "kerning";
    private static final String ATTR_NAME = "name";
    private static final String ATTR_STYLE = "style";
    private static final String ATTR_WEIGHT = "weight";

    // Configuration constants
    private static final String KERNING_ENABLED = "false";
    private static final String EMPTY_FONTS_XML = "<fonts/>";

    /**
     * Constructs a new EFontFamilyLoader with the specified resource provider and font family list.
     *
     * @param resourceProvider the resource provider to use for resolving font file paths;
     *                         must not be {@code null}
     * @param fontFamilyList   the list of font families to process; may be {@code null} or empty
     *                         (in which case an empty {@code <fonts/>} element will be generated)
     * @throws IllegalArgumentException if resourceProvider is {@code null}
     */
    EFontFamilyLoader {
        Objects.requireNonNull(resourceProvider, "ResourceProvider cannot be null.");
    }

    /**
     * Generates an XML configuration string for all fonts in the font family list.
     * This method processes each font family and creates the corresponding FOP font
     * configuration XML that can be used to register custom fonts.
     *
     * @return an XML string containing the font configuration for Apache FOP;
     * returns {@code "<fonts/>"} if the font family list is null or empty
     * @throws IOException if an I/O error occurs while resolving font file URLs
     */
    public String getFontListString() throws IOException {
        if (isEmptyFontList()) {
            log.warn("Font family list is null or empty, no fonts will be registered.");
            return EMPTY_FONTS_XML;
        }

        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<").append(XML_FONTS).append(">");

        List<FontFamily> fontFamilies = fontFamilyList.getFontFamilyList();
        for (FontFamily fontFamily : fontFamilies) {
            appendFontFamily(xmlBuilder, fontFamily);
        }

        xmlBuilder.append("</").append(XML_FONTS).append(">");
        return xmlBuilder.toString();
    }

    /**
     * Checks if the font family list is null, contains null, or is empty.
     *
     * @return {@code true} if the font list is empty or invalid, {@code false} otherwise
     */
    private boolean isEmptyFontList() {
        return fontFamilyList == null
                || fontFamilyList.getFontFamilyList() == null
                || fontFamilyList.getFontFamilyList().isEmpty();
    }

    /**
     * Appends all font types from a font family to the XML configuration.
     *
     * @param xmlBuilder the StringBuilder to append XML content to
     * @param fontFamily the font family to process
     * @throws IOException if an I/O error occurs while resolving font file URLs
     */
    private void appendFontFamily(StringBuilder xmlBuilder, FontFamily fontFamily) throws IOException {
        String fontFamilyName = fontFamily.fontFamily();

        if (fontFamilyName == null || fontFamilyName.trim().isEmpty()) {
            log.warn("Font family has null or empty name, skipping");
            return;
        }

        List<FontType> fontTypes = fontFamily.fontTypes();
        if (fontTypes == null || fontTypes.isEmpty()) {
            log.debug("Font family '{}' has no font types, skipping", fontFamilyName);
            return;
        }

        for (FontType fontType : fontTypes) {
            appendFontType(xmlBuilder, fontFamilyName, fontType);
        }
    }

    /**
     * Appends a single font type configuration to the XML.
     * This method resolves the font file URL, extracts font properties,
     * and generates the complete {@code <font>} element with its {@code <font-triplet>}.
     *
     * @param xmlBuilder     the StringBuilder to append XML content to
     * @param fontFamilyName the name of the font family
     * @param fontType       the font type to process
     * @throws IOException if an I/O error occurs while resolving the font file URL
     */
    private void appendFontType(StringBuilder xmlBuilder, String fontFamilyName, FontType fontType)
            throws IOException {
        String fontFilePath = fontType.path();

        if (fontFilePath == null || fontFilePath.trim().isEmpty()) {
            log.warn("Font type in family '{}' has null or empty path, skipping", fontFamilyName);
            return;
        }

        URL fontUrl = resourceProvider.getResource(fontFilePath);

        if (fontUrl == null) {
            log.warn("Could not find font file resource: {}", fontFilePath);
            return;
        }

        String weight = fontType.fontWeight();
        String style = fontType.fontStyle().toString().toLowerCase();

        // Build font element
        xmlBuilder.append("<").append(XML_FONT)
                .append(" ").append(ATTR_EMBED_URL).append("=\"")
                .append(GenerateUtils.escapeXml(fontUrl.toExternalForm()))
                .append("\" ").append(ATTR_KERNING).append("=\"")
                .append(KERNING_ENABLED)
                .append("\">");

        // Build font-triplet element
        xmlBuilder.append("<").append(XML_FONT_TRIPLET)
                .append(" ").append(ATTR_NAME).append("=\"")
                .append(GenerateUtils.escapeXml(fontFamilyName))
                .append("\" ").append(ATTR_STYLE).append("=\"")
                .append(GenerateUtils.escapeXml(style))
                .append("\" ").append(ATTR_WEIGHT).append("=\"")
                .append(GenerateUtils.escapeXml(weight))
                .append("\"/>");

        // Close font element
        xmlBuilder.append("</").append(XML_FONT).append(">");
    }

}