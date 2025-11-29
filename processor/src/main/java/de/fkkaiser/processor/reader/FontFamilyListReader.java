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
package de.fkkaiser.processor.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.fkkaiser.model.font.FontFamilyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * Utility class.Reads and parses a JSON stream into a
 * FontFamilyList object.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
public final class FontFamilyListReader {
    private static final Logger log = LoggerFactory.getLogger(FontFamilyListReader.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Reads a JSON input stream and parses it into a FontFamilyList object.
     *
     * @param inputStream The JSON input stream. Cannot be null.
     * @return A populated FontFamilyList object.
     * @throws JsonReadException if the stream cannot be read or parsed.
     * @throws NullPointerException if the inputStream or its {@link FontFamilyList} is null.
     */
    public FontFamilyList readJson(InputStream inputStream) throws JsonReadException {
        Objects.requireNonNull(inputStream, "inputStream is null");

        try {
            log.debug("Reading font family list from stream...");
            FontFamilyList list = objectMapper.readValue(inputStream, FontFamilyList.class);
            Objects.requireNonNull(list, "FontFamilyList-Object in inputStream is null");
            Objects.requireNonNull(list.getFontFamilyList(), "FontFamilyList in inputStream is null");

            if(list.getFontFamilyList().isEmpty()){
                log.error("Empty font family list."); //embed fonts a required
                throw new JsonReadException("Empty font family list.");
            }
            log.info("Successfully read {} font families.", list.getFontFamilyList().size());
            return list;
        } catch (IOException io) {
            log.error("Failed to read font families from stream.", io);
            throw new JsonReadException("Failed to read or parse font family list from JSON."+ io.getMessage());
        }
    }
}
