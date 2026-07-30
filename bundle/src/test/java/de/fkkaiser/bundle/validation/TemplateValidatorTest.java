package de.fkkaiser.bundle.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fkkaiser.bundle.MasterBundle;
import de.fkkaiser.bundle.TextBundle;
import de.fkkaiser.bundle.TextEntry;
import de.fkkaiser.model.font.FontFamilyList;
import de.fkkaiser.model.style.StyleSheet;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TemplateValidatorTest {

    private final ObjectMapper mapper = new ObjectMapper();
    private static final StyleSheet EMPTY_STYLE_SHEET = new StyleSheet(List.of(), List.of(), List.of(), null);

    @Test
    void shouldReportMissingTextKey() throws Exception {
        JsonNode documentTree = mapper.readTree("""
                { "type": "text-run", "text": "${unknownKey}" }
                """);
        TextBundle textBundle = new TextBundle(Map.of());

        PlaceholderValidationResult result = TemplateValidator.validate(
                masterBundle(documentTree, textBundle), Map.of());

        assertTrue(result.missingTextKeys().contains("unknownKey"));
        assertTrue(result.missingDataKeys().isEmpty());
        assertFalse(result.isValid());
    }

    @Test
    void shouldReportMissingDataKey() throws Exception {
        JsonNode documentTree = mapper.readTree("""
                { "type": "text-run", "text": "${greeting}" }
                """);
        TextBundle textBundle = new TextBundle(Map.of(
                "greeting", new TextEntry("Hallo {{name}}")));

        PlaceholderValidationResult result = TemplateValidator.validate(
                masterBundle(documentTree, textBundle), Map.of());

        assertTrue(result.missingTextKeys().isEmpty());
        assertTrue(result.missingDataKeys().contains("name"));
        assertFalse(result.isValid());
    }

    @Test
    void shouldBeValidWhenEverythingResolves() throws Exception {
        JsonNode documentTree = mapper.readTree("""
                { "type": "text-run", "text": "${greeting}" }
                """);
        TextBundle textBundle = new TextBundle(Map.of(
                "greeting", new TextEntry("Hallo {{name}}")));

        PlaceholderValidationResult result = TemplateValidator.validate(
                masterBundle(documentTree, textBundle), Map.of("name", "Welt"));

        assertTrue(result.isValid());
        assertEquals(0, result.missingTextKeys().size());
        assertEquals(0, result.missingDataKeys().size());
    }

    /**
     * TemplateValidator only reads documentTree() and textBundle(); styleSheet()/fonts()
     * are irrelevant here but required to construct a MasterBundle.
     */
    private static MasterBundle masterBundle(JsonNode documentTree, TextBundle textBundle) {
        return new MasterBundle(documentTree, EMPTY_STYLE_SHEET, new FontFamilyList(), textBundle);
    }
}
