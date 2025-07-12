package de.kaiser.processor.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.kaiser.model.style.StyleSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class StyleSheetReader {
    private static final Logger log = LoggerFactory.getLogger(StyleSheetReader.class);
    private static final ObjectMapper objectMapper = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    public StyleSheet readJson(InputStream inputStream) throws JsonReadException {
        if (inputStream == null) {
            throw new IllegalArgumentException("Input stream cannot be null.");
        }
        try {
            log.debug("Reading stylesheet...");
            StyleSheet styleSheet = objectMapper.readValue(inputStream, StyleSheet.class);
            if (styleSheet == null) {
                log.error("Parsing JSON resulted in a null StyleSheet object.");
                throw new JsonReadException("Parsed StyleSheet is null.");
            }
            if (styleSheet.textStyles() == null || styleSheet.textStyles().isEmpty()) {
                log.error("Stylesheet must contain at least one text style.");
                throw new JsonReadException("Stylesheet has no text styles.");
            }

            return styleSheet;

        } catch (IOException io) {
            log.error("Error reading JSON input stream for StyleSheet", io);
            // 4. Konsistente Fehlerbehandlung
            throw new JsonReadException("Failed to read or parse StyleSheet JSON.", io);
        }
    }
}