package de.fkkaiser.processor.reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.fkkaiser.model.style.StyleSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * A utility class for reading stylesheets from JSON input streams
 * and deserializing them into StyleSheet objects.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 *
 */
public final class StyleSheetReader {
    private static final Logger log = LoggerFactory.getLogger(StyleSheetReader.class);
    private static final ObjectMapper objectMapper = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    /**
     * Reads a JSON input stream and deserializes it into a {@link StyleSheet} object.
     *
     * @param inputStream the input stream containing the JSON data to read
     * @return the deserialized {@link StyleSheet} object representing the JSON structure
     * @throws JsonReadException if there is an issue reading or parsing the JSON data
     * @throws NullPointerException if the inputStream or its {@link StyleSheet} or
     * its textStyle-list is {@code null}
     */
    public StyleSheet readJson(InputStream inputStream) throws JsonReadException {
        Objects.requireNonNull(inputStream, "inputStream is null");
        try {
            log.debug("Reading stylesheet...");
            StyleSheet styleSheet = objectMapper.readValue(inputStream, StyleSheet.class);
            Objects.requireNonNull(styleSheet, "StyleSheet-Object in inputStream is null");
            Objects.requireNonNull(styleSheet.textStyles(), "textStyles in inputStream is null");
            if (styleSheet.textStyles().isEmpty()) {
                log.error("Stylesheet must contain at least one text style. But none found.");
                throw new JsonReadException("Stylesheet has no text styles.");
            }

            return styleSheet;

        } catch (IOException io) {
            log.error("Error reading JSON input stream for StyleSheet", io);
            throw new JsonReadException("Failed to read or parse StyleSheet JSON.", io);
        }
    }
}