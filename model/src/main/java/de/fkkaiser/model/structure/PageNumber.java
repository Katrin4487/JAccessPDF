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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.fkkaiser.model.JsonPropertyName;
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.style.ElementStyleProperties;
import de.fkkaiser.model.style.StyleResolverContext;
/**
 * Represents a page number inline element in the document structure.
 * @author Katrin Kaiser
 * @version 1.0.1
 */
@JsonTypeName(JsonPropertyName.PAGE_NUMBER)
public class PageNumber extends AbstractInlineElement {

    /**
     * Constructor for PageNumber.
     * @param styleClass the style class to be applied to the page number
     */
    @PublicAPI
    @JsonCreator
    public PageNumber(
            @JsonProperty(JsonPropertyName.STYLE_CLASS) String styleClass
           ) {
        super(styleClass);
    }

    /**
     * Default constructor for PageNumber with no style class.
     */
    @PublicAPI
    public PageNumber(){
        this(null);
    }


    /**
     * Gets the type of the element.
     * @return the element target type as PAGE_NUMBER
     */
    @Override
    public ElementTargetType getType() {
        return ElementTargetType.PAGE_NUMBER;
    }

    /**
     * Resolves styles for the page number element.
     * No specific styles to resolve for page number.
     * @param context the style resolver context
     */
    @Internal
    @Override
    public void resolveStyles(StyleResolverContext context) {
       //nothing here...
    }

    /**
     * Gets the resolved style properties for the page number element.
     * @return null as there are no specific resolved styles for page number
     */
    @Internal
    @Override
    public ElementStyleProperties getResolvedStyle() {
        return null;
    }
}
