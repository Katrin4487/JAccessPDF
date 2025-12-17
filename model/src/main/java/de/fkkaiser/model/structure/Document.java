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

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.structure.builder.DocumentBuilder;

import java.util.List;

/**
 * Represents a document structure containing metadata, internal addresses, and page sequences.
 *
 * <p>The {@code Document} record encapsulates the overall structure of a document,
 * including its metadata, any internal addresses for resources, and the sequence of pages
 * that make up the document content.</p>
 *
 * <p>Instances of this class are immutable and can be created using the {@link DocumentBuilder}
 * for a fluent and flexible construction process.</p>
 *
 * @author Katrin Kaiser
 * @version 1.0.1
 */
@PublicAPI
public record Document(
        @JsonProperty("internal-addresses")
        InternalAddresses internalAddresses,

        @JsonProperty("metadata")
        Metadata metadata,

        @JsonProperty("page-sequences")
        List<PageSequence> pageSequences
) {

    /**
     * Constructs a Document instance.
     *
     * @param internalAddresses the internal addresses for resources within the document;
     *                          may be {@code null} if not applicable
     * @param metadata          the document metadata (title, author, language, etc.);
     *                          must not be {@code null}
     * @param pageSequences     the list of page sequences that make up the document content;
     *                          may be {@code null} or empty if no pages are defined
     */
    @PublicAPI
    public Document {
        if (pageSequences == null) {
            pageSequences = List.of();
        }
    }

    /**
     * Creates a new DocumentBuilder initialized with the provided metadata.
     *
     * @param metadata the metadata to initialize the document builder with
     * @return a new DocumentBuilder instance
     */
    @PublicAPI
    public static DocumentBuilder builder(Metadata metadata) {
        return new DocumentBuilder(metadata);
    }
}