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
import de.fkkaiser.model.JsonPropertyName;

/**
 * Base interface for all inline elements in the document structure.
 * Inline elements are elements that can be part of text content, such as
 * text runs, hyperlinks, footnotes, and page numbers.
 * @author Katrin Kaiser
 * @version 1.1.0
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextRun.class, name = JsonPropertyName.TEXT_RUN),
        @JsonSubTypes.Type(value = PageNumber.class, name = JsonPropertyName.PAGE_NUMBER),
        @JsonSubTypes.Type(value = Footnote.class, name = JsonPropertyName.FOOTNOTE),
        @JsonSubTypes.Type(value = Hyperlink.class, name = JsonPropertyName.HYPERLINK)
})
public interface InlineElement extends Element {



}
