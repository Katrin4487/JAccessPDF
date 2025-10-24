package de.fkkaiser.model.structure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContentAreaTest {

    @Test
    @DisplayName("should replace a null elements list with an empty list")
    void shouldReplaceNullElementsWithEmptyList() {
        ContentArea contentArea = new ContentArea(null);

        assertNotNull(contentArea.elements(), "Elements list should not be null.");
        assertTrue(contentArea.elements().isEmpty(), "Elements list should be empty.");
    }

    @Test
    @DisplayName("should create an empty ContentArea with the no-args constructor")
    void shouldWorkWithNoArgsConstructor() {
        ContentArea contentArea = new ContentArea();

        assertNotNull(contentArea.elements(), "Elements list should not be null.");
        assertTrue(contentArea.elements().isEmpty(), "Elements list should be empty.");
    }
}