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
package de.fkkaiser.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a style property as inheritable from parent elements.
 *
 * <p>Properties marked with {@code @Inheritable} will be inherited
 * from parent elements during style merging if they are null in the
 * child element. This follows CSS-like inheritance rules but allows
 * fine-grained control per property and per element type.</p>
 *
 * <h2>Usage Examples:</h2>
 * <pre>{@code
 * // Color properties typically inherit
 * @Inheritable
 * @JsonProperty("text-color")
 * private String textColor;
 *
 * // Background might inherit in Section but not in Paragraph
 * @Inheritable  // in SectionStyleProperties
 * @JsonProperty("background-color")
 * private String backgroundColor;
 *
 * @Inheritable(false)  // in ParagraphStyleProperties
 * @JsonProperty("background-color")
 * private String backgroundColor;
 *
 * // Layout properties should not inherit
 * @JsonProperty("break-before")  // no @Inheritable
 * private PageBreakVariant breakBefore;
 * }</pre>
 *
 * <p><strong>Note:</strong> Properties without this annotation are
 * NOT inherited (safe default behavior).</p>
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Inheritable {
    /**
     * Whether the property should be inherited from parent elements.
     *
     * @return true if the property should be inherited, false otherwise
     */
    boolean value() default true;
}