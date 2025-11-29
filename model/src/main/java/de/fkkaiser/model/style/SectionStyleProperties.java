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

import java.util.function.Consumer;

/**
 * Concrete style properties for a section element.
 * A section can be used to group content and apply specific background,
 * padding, or borders to it.
 */
@JsonTypeName(StyleTargetTypes.SECTION)
public class SectionStyleProperties extends ElementBlockStyleProperties {


    // --- Overrides ---
    public void mergeWith(ElementBlockStyleProperties base) {
        super.mergeWith(base);

    }

    @Override
    public SectionStyleProperties copy() {
        SectionStyleProperties newInstance = new SectionStyleProperties();
        applyPropertiesTo(newInstance);
        return newInstance;
    }


    protected void applyPropertiesTo(SectionStyleProperties newInstance) {
        super.applyPropertiesTo(newInstance);

    }

    private <T> void mergeProperty(T current, T base, Consumer<T> setter) {
        if (current == null) {
            setter.accept(base);
        }
    }
}
