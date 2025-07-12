package de.kaiser.processor.reader;

import de.kaiser.model.structure.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("DocumentReader Tests")
class DocumentReaderTest {

    private DocumentReader documentReader;

    @BeforeEach
    void setUp() {
        // Wir testen die Version, die den ObjectMapper wiederverwendet.
        documentReader = new DocumentReader();
    }

    @Test
    @DisplayName("should read valid JSON and return a populated Document object")
    void shouldReadValidJsonAndReturnDocument() throws JsonReadException {
        // ARRANGE: Ein gültiger JSON-String, der eine einfache Dokumentstruktur darstellt.
        String validJson = """
                {
                  "metadata": {
                    "title": "Test Document"
                  },
                  "page-sequences": []
                }
                """;
        InputStream inputStream = new ByteArrayInputStream(validJson.getBytes(StandardCharsets.UTF_8));

        Document document = documentReader.readJson(inputStream);

        assertNotNull(document, "Document should not be null for valid JSON.");
        assertNotNull(document.metadata(), "Metadata should be parsed.");
        assertEquals("Test Document", document.metadata().title());
        assertNotNull(document.pageSequences(), "PageSequences list should be parsed.");
    }

    @Test
    @DisplayName("should throw DocumentReadException for malformed JSON")
    void shouldThrowExceptionForMalformedJson() {
        // ARRANGE: Ein ungültiger JSON-String (z.B. eine fehlende schließende Klammer).
        String malformedJson = """
                {
                  "metadata": {
                    "title": "Test Document"
                }
                """;
        InputStream inputStream = new ByteArrayInputStream(malformedJson.getBytes(StandardCharsets.UTF_8));

        assertThrows(JsonReadException.class, () -> documentReader.readJson(inputStream), "A DocumentReadException should be thrown for malformed JSON.");
    }
}