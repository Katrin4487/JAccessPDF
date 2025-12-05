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
package de.fkkaiser.model.style;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.fkkaiser.model.JsonPropertyName;
import de.fkkaiser.model.annotation.Inheritable;
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;

/**
 * List item style properties
 * @author Katrin Kaiser
 * @version 1.1.0
 */
@JsonTypeName(JsonPropertyName.LIST_ITEM)
public class ListItemStyleProperties extends TextBlockStyleProperties{

    @Inheritable
    @JsonProperty(JsonPropertyName.LIST_STYLE_TYPE)
    private ListStyleType listStyleType;

    @JsonProperty(JsonPropertyName.SPACE_BEFORE)
    private String spaceBefore;

    @JsonProperty(JsonPropertyName.SPACE_AFTER)
    private String spaceAfter;

    public ListItemStyleProperties() {
        super();
    }

    /**
     * Constructor for list item properties
     * @param spaceBefore space before value
     * @param spaceAfter space after value
     * @param listStyleType list style type
     */
    @PublicAPI
    public ListItemStyleProperties(String spaceBefore, String spaceAfter, ListStyleType listStyleType) {
       super();
        this.spaceBefore = spaceBefore;
        this.spaceAfter = spaceAfter;
        this.listStyleType = listStyleType;
    }

    /**
     * Returns the space before for this value
     * @return space before value as String
     */
    public String getSpaceBefore() {
        return spaceBefore;
    }

    /**
     * Returns the space after value for this list item instance
     * @return String with value
     */
    @Internal
    public String getSpaceAfter() {
        return spaceAfter;
    }

    /**
     * Returns the list style type for this instance
     * @return list style type value
     */
    @Internal
    public ListStyleType getListStyleType() {
        return listStyleType;
    }

    @Internal
    @Override
    public ListItemStyleProperties copy() {
        ListItemStyleProperties newIstance = new ListItemStyleProperties();
        applyPropertiesTo(newIstance);
        return newIstance;
    }

    @Internal
    protected void applyPropertiesTo(TextBlockStyleProperties targetStyle) {
        super.applyPropertiesTo(targetStyle);
        if (targetStyle instanceof ListItemStyleProperties listItemStyle) {
            listItemStyle.spaceBefore = this.spaceBefore;
            listItemStyle.spaceAfter = this.spaceAfter;
            listItemStyle.listStyleType = this.listStyleType;
        }
    }
}
