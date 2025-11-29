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
package de.fkkaiser.model.structure;


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

    @Test
    @DisplayName("Should create an Document with metadata only")
    public void shouldCreateDocumentWithMetadataOnly() {

        Document document = Document.builder(new Metadata("A title")).build();
        assertNotNull(document.metadata(), "The metadata should never be null.");
        assertTrue(document.pageSequences().isEmpty(), "The pageSequences list should never be null but empty.");
        assertNull(document.internalAddresses(), "The internal addresses should be null, if not present.");
    }

    @Test
    @DisplayName("Should add Page Sequences with Builder")
    public void shouldAddPageSequencesWithBuilder() {
        Document document = Document.builder(new Metadata("A title"))
                .addPageSequence(new PageSequence("style",new ContentArea(),new ContentArea(),new ContentArea()))
                .withInternalAddresses(new InternalAddresses("fonts","images"))
                .build();

        assertFalse(document.pageSequences().isEmpty());
        assertNotNull(document.internalAddresses());

    }
}