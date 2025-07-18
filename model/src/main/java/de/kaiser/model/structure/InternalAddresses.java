package de.kaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

public record InternalAddresses(

        @JsonProperty("font-dictionary")
        String fontDictionary,
        @JsonProperty("image-dictionary")
        String imageDictionary
) {


}