package de.fkkaiser.model.structure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Headline Class Tests")
class HeadlineTest {

    @Test
    @DisplayName("should apply default level when it is null in JSON")
    void shouldApplyDefaultLevelWhenNull() {
        // Act
        Headline headline = new Headline("h1-style", null, null, null);

        // Assert
        assertEquals(1, headline.getLevel(), "Default level should be 1 when null is provided.");
    }

    @Test
    @DisplayName("should use the explicitly provided level")
    void shouldUseExplicitlyProvidedLevel() {
        // Act
        Headline headline = new Headline("h3-style", null, null, 3);

        // Assert
        assertEquals(3, headline.getLevel(), "The explicit level 3 should be used.");
    }
}