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

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;

/**
 * Abstract method for all inline elements.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
@Internal
public abstract class AbstractInlineElement extends AbstractElement implements InlineElement {

    /**
     * Constructor for creating an inline element with both style class and variant.
     *
     * <p>This constructor is primarily used by Jackson during JSON deserialization
     * and by subclasses that support both properties.</p>
     *
     * @param styleClass the CSS-like style class name for styling;
     *                   may be {@code null} if no specific styling is needed
     */
    @PublicAPI
    public AbstractInlineElement(
            @JsonProperty("style-class") String styleClass
    ) {
        super(styleClass);
    }

}