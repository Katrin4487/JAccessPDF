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
 * Abstract base class for all {@link InlineElement} implementations.
 *
 * <p>This class extends {@link AbstractElement} and implements the {@link InlineElement}
 * interface, providing a common foundation for all inline elements such as
 * {@link TextRun}, {@link Hyperlink}, {@link PageNumber}, and {@link Footnote}.</p>
 *
 * <p><b>Inheritance Hierarchy:</b></p>
 * <pre>
 * Element (interface)
 *   └── AbstractElement
 *         └── AbstractInlineElement
 *               ├── TextRun
 *               ├── Hyperlink
 *               ├── PageNumber
 *               └── Footnote
 * </pre>
 *
 * <p><b>Common Properties:</b></p>
 * Inherits {@code styleClass} and {@code variant} from {@link AbstractElement}.
 * These properties allow inline elements to have their own styling independent
 * of their containing block element.
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * // Creating inline elements with different styles
 * InlineElement normal = new TextRun("Normal text", "normal-style");
 * InlineElement bold = new TextRun("Bold text", "bold-style");
 * InlineElement link = new Hyperlink("Click here", "link-style", "https://example.com");
 *
 * // Combining them in a paragraph
 * Paragraph p = new Paragraph("body-text", List.of(normal, bold, link));
 * }</pre>
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 * @see AbstractElement
 * @see InlineElement
 * @see TextRun
 * @see Hyperlink
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
     * @param variant    the semantic variant identifier (e.g., "warning", "emphasis");
     *                   may be {@code null} if no variant is needed
     */
    @PublicAPI
    public AbstractInlineElement(
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("variant") String variant) {
        super(styleClass, variant);
    }

    /**
     * Convenience constructor for creating an inline element with only a style class.
     * The variant is set to {@code null}.
     *
     * <p>This is useful when creating inline elements programmatically that don't
     * require semantic variants.</p>
     *
     * @param styleClass the CSS-like style class name for styling;
     *                   may be {@code null} if no specific styling is needed
     */
    @PublicAPI
    public AbstractInlineElement(String styleClass) {
        this(styleClass, null);
    }
}