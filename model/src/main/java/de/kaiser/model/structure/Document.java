package de.kaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonProperty;

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

}