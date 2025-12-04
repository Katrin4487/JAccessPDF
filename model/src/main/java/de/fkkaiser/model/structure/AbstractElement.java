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
import de.fkkaiser.model.JsonPropertyName;
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.style.StyleResolverContext;

/**
 * Abstract element class.
 *
 * @author Katrin Kaiser
 * @version 1.0.2
 */
@Internal
public abstract class AbstractElement implements Element {

    @JsonProperty(JsonPropertyName.STYLE_CLASS)
    protected String styleClass;

    /**
     * No-argument constructor required for Jackson deserialization.
     *
     * <p>Jackson uses this constructor to create an instance of the class
     * before populating its fields via reflection. This constructor should
     * not be used directly in application code.</p>
     */
    @Internal
    public AbstractElement() {
    }

    /**
     * Constructor for an abstract element
     * @param styleClass style class name of the element
     */
    @Internal
    public AbstractElement(String styleClass) {
        this.styleClass = styleClass;

    }

    /**
     * Returns the type identifier for this element.
     * This is used for JSON serialization and element type identification.
     *
     */
    @Override
    public abstract ElementTargetType getType();

    /**
     * Returns the style class name for this element.
     *
     * <p>The style class is used to look up the element's style properties
     * from the style map during style resolution.</p>
     *
     * @return the style class name, or {@code null} if no style class is set
     */
    @Override
    public String getStyleClass() {
        return this.styleClass;
    }


    /**
     * Resolves and applies styles for this element using the provided context.
     *
     * <p>Concrete subclasses must implement this method to perform style
     * resolution based on their specific style properties and the given
     * {@link StyleResolverContext}.</p>
     *
     * @param context the style resolver context containing style information
     */
    @Internal
    @Override
    public abstract void resolveStyles(StyleResolverContext context);
}