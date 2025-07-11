package de.kaiser.processor.reader;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import de.kaiser.model.style.StyleSheet;

/**
 * Class for reading a JSON input stream and converting it into a StyleSheet object.
 * Provides a method to read the input stream and map it to a StyleSheet instance.
 */
public class StyleSheetReader {
    private static final Logger log = LoggerFactory.getLogger(StyleSheetReader.class);
    private static final ObjectMapper objectMapper = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    /**
     * Reads a JSON input stream and converts it into a StyleSheet object.
     *
     * @param inputStream The input stream containing JSON data to be read.
     * @return The parsed StyleSheet object if successful, or null if an error occurs during reading.
     */
    public StyleSheet readJson(InputStream inputStream) {

        try {
            log.debug("Reading stylesheet...");
            StyleSheet styleSheet = objectMapper.readValue(inputStream, StyleSheet.class);
            if(styleSheet == null){
                log.error("Failed to read stylesheet");
                throw new JsonParseException("Failed to read stylesheet");
            }
            if(styleSheet.textStyles().isEmpty()){
                log.error("Stylesheet has no text styles");
                throw new JsonParseException("Stylesheet has no text styles"); //You need embed fonts set in text styles!
            }
            return objectMapper.readValue(inputStream, StyleSheet.class);
        } catch (IOException io) {
            log.error("Error reading JSON input stream for StyleSheet", io);
            return null;
        }
    }
}