package de.kaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ListOrdering {
    @JsonProperty("unordered")
    UNORDERED,
    @JsonProperty("ordered")
    ORDERED;

    @Override
    public String toString() {
        return switch (this) {
            case UNORDERED -> "unordered";
            case ORDERED -> "ordered";
            default ->
                //Should not happen
                    "unordered";
        };
    }
}
