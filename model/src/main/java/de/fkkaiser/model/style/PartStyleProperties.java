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

@JsonTypeName(StyleTargetTypes.PART) // Annahme: StyleTargetTypes.PART = "part"
public class PartStyleProperties extends ElementBlockStyleProperties {

    @JsonProperty("page-break-before")
    private String pageBreakBefore;

    // --- Getters and Setters ---
    public String getPageBreakBefore() { return pageBreakBefore; }
    public void setPageBreakBefore(String pageBreakBefore) { this.pageBreakBefore = pageBreakBefore; }

    // --- Overrides ---
    public void mergeWith(ElementBlockStyleProperties base) {
        super.mergeWith(base);
        if (base instanceof PartStyleProperties basePart) {
            mergeProperty(this.pageBreakBefore, basePart.getPageBreakBefore(), this::setPageBreakBefore);
        }
    }

    @Override
    public PartStyleProperties copy() {
        PartStyleProperties copy = new PartStyleProperties();
        applyPropertiesTo(copy);
        return copy;
    }

    @Override
    public void applyPropertiesTo(ElementBlockStyleProperties target) {
        super.applyPropertiesTo(target);
        if (target instanceof PartStyleProperties partTarget) {
            partTarget.setPageBreakBefore(this.pageBreakBefore);
        }
    }


    private <T> void mergeProperty(T current, T base, Consumer<T> setter) {
        if (current == null) {
            setter.accept(base);
        }
    }
}