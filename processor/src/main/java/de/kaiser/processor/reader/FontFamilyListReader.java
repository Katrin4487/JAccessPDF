package de.kaiser.processor.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.kaiser.model.font.FontFamilyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.InputStream;

/**
 * Reads and parses a JSON stream into a FontFamilyList object.
 */
public class FontFamilyListReader {
    private static final Logger log = LoggerFactory.getLogger(FontFamilyListReader.class);

    // 1. ObjectMapper wird nur einmal initialisiert und wiederverwendet.
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Reads a JSON input stream and parses it into a FontFamilyList object.
     *
     * @param inputStream The JSON input stream. Cannot be null.
     * @return A populated FontFamilyList object.
     * @throws JsonReadException if the stream cannot be read or parsed.
     */
    public FontFamilyList readJson(InputStream inputStream) throws JsonReadException {
        if (inputStream == null) {
            throw new IllegalArgumentException("Input stream cannot be null.");
        }
        try {
            log.debug("Reading font family list from stream...");
            FontFamilyList list = objectMapper.readValue(inputStream, FontFamilyList.class);
            if(list==null || list.getFontFamilyList()==null || list.getFontFamilyList().isEmpty()){
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

// Diese Exception-Klasse in eine eigene Datei legen.
// public class FontReadException extends RuntimeException { ... }