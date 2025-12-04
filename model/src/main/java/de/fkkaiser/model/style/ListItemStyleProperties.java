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

@JsonTypeName(StyleTargetTypes.LIST_ITEM)
public class ListItemStyleProperties extends TextBlockStyleProperties{

    @Inheritable
    @JsonProperty(JsonPropertyName.LIST_STYLE_TYPE)
    private String listStyleType;

    @JsonProperty(JsonPropertyName.SPACE_BEFORE)
    private String spaceBefore;

    @JsonProperty(JsonPropertyName.SPACE_AFTER)
    private String spaceAfter;

    public ListItemStyleProperties() {
        super();
    }

    public ListItemStyleProperties(String spaceBefore, String spaceAfter, String listStyleType) {
       super();
        this.spaceBefore = spaceBefore;
        this.spaceAfter = spaceAfter;
        this.listStyleType = listStyleType;
    }

    public String getSpaceBefore() {
        return spaceBefore;
    }
    public String getSpaceAfter() {
        return spaceAfter;
    }
    public String getListStyleType() {
        return listStyleType;
    }

    @Override
    public ListItemStyleProperties copy() {
        ListItemStyleProperties newIstance = new ListItemStyleProperties();
        applyPropertiesTo(newIstance);
        return newIstance;
    }

    protected void applyPropertiesTo(TextBlockStyleProperties targetStyle) {
        super.applyPropertiesTo(targetStyle);
        if (targetStyle instanceof ListItemStyleProperties listItemStyle) {
            listItemStyle.spaceBefore = this.spaceBefore;
            listItemStyle.spaceAfter = this.spaceAfter;
            listItemStyle.listStyleType = this.listStyleType;
        }
    }
}
