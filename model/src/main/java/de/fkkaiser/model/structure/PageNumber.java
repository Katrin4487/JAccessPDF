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
import de.fkkaiser.model.style.StyleResolverContext;
import de.fkkaiser.model.style.StyleResolverContext;

/**
 * An empty "marker" element
 * (XSL-FO page number)
 */
@JsonTypeName("page-number")
public class PageNumber extends AbstractInlineElement {

    @JsonCreator
    public PageNumber(
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("variant") String variant) {
        super(styleClass, variant);
    }

    public PageNumber(){
        this(null, null);
    }

    public PageNumber(String styleClass){
        this(styleClass, null);
    }

    @Override
    public String getType() {
        return InlineElementTypes.PAGE_NUMBER;
    }

    @Override
    public void resolveStyles(StyleResolverContext context) {
        // PageNumber hat typischerweise keinen eigenen Stil,
        // erbt aber den Kontext. Wenn doch, könnte hier Logik stehen.
    }
}
