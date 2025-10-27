package de.fkkaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the root object of the document structure, deserialized from the main JSON file.
 */
public record Document(
        @JsonProperty("internal-addresses")
        InternalAddresses internalAddresses,
        @JsonProperty("metadata")
        Metadata metadata,
        @JsonProperty("page-sequences")
        List<PageSequence> pageSequences
) {

    // construct
    public Document {
        if (pageSequences == null) {
            pageSequences = List.of();
        }
    }

    /**
     * Entry point to create a new Document using the Builder pattern.
     *
     * @return A new DocumentBuilder instance.
     */
    public static DocumentBuilder builder(Metadata metadata) {
        return new DocumentBuilder(metadata);
    }


    /**
     * A fluent builder for creating an immutable Document object.
     */
    @SuppressWarnings("unused")
    public static class DocumentBuilder {
        private final Metadata metadata;
        private final List<PageSequence> pageSequences;
        private  InternalAddresses internalAddresses;

        /**
         * Private constructor to enforce usage via Document.builder()
         */
        private DocumentBuilder(Metadata metadata) {
            this.metadata = metadata;
            this.pageSequences = new ArrayList<>();
        }

        /**
         * Adds an internalAddresses object to the document
         *
         * @param internalAddresses internalAddresses object, that should be added
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

}