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
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.fkkaiser.model.structure.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * A utility class for reading documents from JSON input streams and deserializing them into Document objects.
 * Utilizes ObjectMapper for deserialization and logging for handling errors.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
public final class DocumentReader {

    private static final Logger log = LoggerFactory.getLogger(DocumentReader.class);
    private static final ObjectMapper objectMapper = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    /**
     * Reads a JSON input stream and deserializes it into a Document object using the ObjectMapper.
     *
     * @param inputStream the input stream containing the JSON data to read
     * @return the deserialized Document object representing the JSON structure
     * @throws JsonReadException if there is an issue reading or parsing the JSON data
     */
    public Document readJson(InputStream inputStream) throws JsonReadException {
        try {
            log.debug("Reading font document from stream...");
            return objectMapper.readValue(inputStream, Document.class);

        } catch (IOException io) {
            log.error("Not able to read structure json, {}", io.getMessage());
            throw new JsonReadException("Not able to read structure-json:",io);
        }
    }
}