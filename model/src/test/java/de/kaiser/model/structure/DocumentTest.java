package de.kaiser.model.structure;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentTest {


    @Test
    @DisplayName("Should create document and hold provided values")
    public void shouldCreateDocumentWithGivenValues() {

        Metadata metadata = new Metadata("A title");

        List<PageSequence> sequences = Collections.singletonList(PageSequence.builder("style").build());

        Document document = new Document(null,metadata, sequences);

        assertEquals(metadata, document.metadata());
        assertEquals(sequences, document.pageSequences());
    }

    @Test
    @DisplayName("Should replace a null pageSequences list with an empty list")
    public void shouldReplaceNullPageSequencesWithEmptyList() {

        Metadata metadata = new Metadata("A title");
        Document document = new Document(null,metadata, null);
        assertNotNull(document.pageSequences(), "The pageSequences list should never be null.");
        assertTrue(document.pageSequences().isEmpty(), "The list should be empty.");
    }
}