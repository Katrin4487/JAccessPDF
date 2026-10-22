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
package de.fkkaiser.markdown;

/**
 * Thrown when a Markdown source cannot be converted into the document model -
 * e.g. because it contains a construct that is not supported by the chosen
 * converter (for inline Markdown, that is any block-level construct, raw HTML,
 * or an image).
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
public class MarkdownConversionException extends RuntimeException {

    public MarkdownConversionException(String message) {
        super(message);
    }

    public MarkdownConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
