package de.fkkaiser.bundle;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TextEntryTest {


    @Test
    public void shouldExtractPlaceholders() {

        String text = "Hallo {{placeholder1}} {{placeholder2}} und {{placeholder3}}";

        List<String> placeholders = new TextEntry(text).extractPlaceholders();

        assertEquals(3, placeholders.size());
        assertTrue(placeholders.contains("placeholder1"));
        assertTrue(placeholders.contains("placeholder2"));
        assertTrue(placeholders.contains("placeholder3"));
    }
}
