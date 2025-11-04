package de.fkkaiser.model.structure.builder;

import de.fkkaiser.model.structure.Document;
import de.fkkaiser.model.structure.InternalAddresses;
import de.fkkaiser.model.structure.Metadata;
import de.fkkaiser.model.structure.PageSequence;

import java.util.ArrayList;
import java.util.List;


/**
 * A fluent builder for creating an immutable Document object.
 */

@SuppressWarnings("unused")
public class DocumentBuilder {

    private final Metadata metadata;
    private final List<PageSequence> pageSequences;
    private InternalAddresses internalAddresses;

    /**
     * Private constructor to enforce usage via Document.builder()
     */
    public DocumentBuilder(Metadata metadata) {
        this.metadata = metadata;
        this.pageSequences = new ArrayList<>();
    }

    /**
     * Adds an internalAddresses object to the document
     *
     * @param internalAddresses internalAddresses object that should be added
     * @return the builder instance for the fluent chaining
     */
    public DocumentBuilder withInternalAddresses(InternalAddresses internalAddresses) {
        this.internalAddresses = internalAddresses;
        return this;
    }

    /**
     * Adds a new PageSequence to the Document
     *
     * @param pageSequence PageSequence object to be added
     * @return the builder instance for the fluent chaining
     */
    public DocumentBuilder addPageSequence(PageSequence pageSequence) {
        this.pageSequences.add(pageSequence);
        return this;
    }

    /**
     * Builds the final, immutable Document object.
     *
     * @return A new Document record.
     */
    public Document build() {
        return new Document(internalAddresses, metadata, List.copyOf(pageSequences));
    }
}
