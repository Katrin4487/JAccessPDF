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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Base interface for all inline elements that can appear within block-level elements.
 *
 * <p>Inline elements are used to add rich formatting within text blocks such as
 * paragraphs, headlines, or list items. They represent content that flows inline
 * with the text, rather than creating new blocks.</p>
 *
 * <p><b>Common Inline Elements:</b></p>
 * <ul>
 *   <li>{@link TextRun} - Plain or formatted text segments</li>
 *   <li>{@link Hyperlink} - Clickable links</li>
 *   <li>{@link PageNumber} - Dynamic page number placeholder</li>
 *   <li>{@link Footnote} - Footnote references with content</li>
 * </ul>
 *
 * <p><b>JSON Polymorphism:</b></p>
 * This interface uses Jackson's {@code @JsonTypeInfo} and {@code @JsonSubTypes}
 * to deserialize the correct concrete class based on the {@code type} field
 * in the JSON structure.
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * List<InlineElement> content = List.of(
 *     new TextRun("Normal text "),
 *     new TextRun("bold text", "bold-style"),
 *     new Hyperlink("Click here", "link-style", "https://example.com")
 * );
 * Paragraph p = new Paragraph("body-text", content);
 * }</pre>
 *
 * @author FK Kaiser
 * @version 1.0
 * @see Element
 * @see TextRun
 * @see Hyperlink
 * @see PageNumber
 * @see Footnote
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextRun.class, name = InlineElementTypes.TEXT_RUN),
        @JsonSubTypes.Type(value = PageNumber.class, name = InlineElementTypes.PAGE_NUMBER),
        @JsonSubTypes.Type(value = Footnote.class, name = InlineElementTypes.FOOTNOTE),
        @JsonSubTypes.Type(value = Hyperlink.class, name = InlineElementTypes.HYPERLINK)
})
public interface InlineElement extends Element {



}
