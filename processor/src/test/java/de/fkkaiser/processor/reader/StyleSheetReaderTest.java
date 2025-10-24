package de.fkkaiser.processor.reader;

import de.fkkaiser.model.style.StyleSheet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StyleSheetReader Tests")
class StyleSheetReaderTest {

    private StyleSheetReader reader;

    @BeforeEach
    void setUp() {
        reader = new StyleSheetReader();
    }

    @Test
    @DisplayName("should parse valid JSON with text styles")
    void shouldParseValidJson() throws JsonReadException {
        String json = """
        {
          "text-styles": [
            { "name": "normal" }
          ]
        }
        """;
        InputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

        // ACT
        StyleSheet result = reader.readJson(stream);

        // ASSERT
        assertNotNull(result);
        assertNotNull(result.textStyles());
        assertFalse(result.textStyles().isEmpty());
    }

    @Test
    @DisplayName("should throw JsonReadException if textStyles is empty")
    void shouldThrowExceptionForEmptyTextStyles() {
        // ARRANGE
        String json = "{ \"text-styles\": [] }";
        InputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

        // ACT & ASSERT
        JsonReadException exception = assertThrows(JsonReadException.class, () -> reader.readJson(stream));

        assertTrue(exception.getMessage().contains("has no text styles"));
    }

    @Test
    @DisplayName("should throw JsonReadException for malformed JSON")
    void shouldThrowExceptionForMalformedJson() {
        // ARRANGE
        String json = "{ \"text-styles\": [ }"; // Syntaxfehler
        InputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

        assertThrows(JsonReadException.class, () -> reader.readJson(stream));
    }
}
