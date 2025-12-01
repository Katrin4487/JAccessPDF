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

import de.fkkaiser.model.annotation.Internal;

/**
 * A class representing properties of table styles
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
@Internal
public class LayoutTableStyleProperties extends ElementBlockStyleProperties{

    @Internal
    @Override
    public void mergeWith(ElementStyleProperties elemBase) {

    }

    /**
     * Creates a deep copy of this style properties object.
     * This is crucial to prevent style modifications on one element
     * from affecting another.
     *
     * @return A new instance with the same property values as this object.
     */
    @Override
    public ElementBlockStyleProperties copy() {
        return null;
    }
}
