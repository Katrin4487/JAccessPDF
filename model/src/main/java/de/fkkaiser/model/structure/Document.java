package de.fkkaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fkkaiser.model.structure.builder.DocumentBuilder;
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


}