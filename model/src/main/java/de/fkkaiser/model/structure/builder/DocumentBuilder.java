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
package de.fkkaiser.model.structure.builder;

import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.structure.Document;
import de.fkkaiser.model.structure.InternalAddresses;
import de.fkkaiser.model.structure.Metadata;
import de.fkkaiser.model.structure.PageSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder class for a document.
 * @author Katrin Kaiser
 * @version 1.0.1
 */
public class DocumentBuilder {

    private static final Logger log = LoggerFactory.getLogger(DocumentBuilder.class);

    private final Metadata metadata;
    private final List<PageSequence> pageSequences;
    private InternalAddresses internalAddresses;

    /**
     * Constructor
     * @param metadata Metadata for this document
     */
    @Internal
    public DocumentBuilder(Metadata metadata) {
        this.metadata = metadata;
        this.pageSequences = new ArrayList<>();
    }

    /**
     * Adding internal adresses to this document
     * @param internalAddresses internal addresses you want to use
     * @return this instance for chaining
     */
    @PublicAPI
    public DocumentBuilder withInternalAddresses(InternalAddresses internalAddresses) {
        this.internalAddresses = internalAddresses;
        log.debug("Set internal addresses: fonts='{}', images='{}'",
                internalAddresses != null ? internalAddresses.fontDictionary() : "null",
                internalAddresses != null ? internalAddresses.imageDictionary() : "null");
        return this;
    }

    /**
     * Adds a Page Sequence to this document
     * @param pageSequence PageSequence you want to add
     * @return this instance for chaining
     */
    @PublicAPI
    public DocumentBuilder addPageSequence(PageSequence pageSequence) {
        this.pageSequences.add(pageSequence);
        log.debug("Added page sequence with style class: '{}'",
                pageSequence != null ? pageSequence.styleClass() : "null");
        return this;
    }

    /**
     * Builds and return the final document.
     * @return final document.
     */
    @PublicAPI
    public Document build() {
        log.info("Building document: title='{}', page sequences={}, has internal addresses={}",
                metadata != null ? metadata.getTitle() : "null",
                pageSequences.size(),
                internalAddresses != null);

        return new Document(
                internalAddresses,
                metadata,
                List.copyOf(pageSequences)
        );
    }
}