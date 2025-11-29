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

import de.fkkaiser.model.annotation.PublicAPI;

/**
 * Defines the available 'type' constants for inline elements.
 * This class should be used by the library's users,
 * to set the type of element in the JSON structure.
 */
public final class InlineElementTypes {

    /** Prevents instantiation of the class. */
    private InlineElementTypes() {}

    @PublicAPI
    public static final String TEXT_RUN = "text-run";
    @PublicAPI
    public static final String PAGE_NUMBER = "page-number";
    @PublicAPI
    public static final String FOOTNOTE = "footnote";
    @PublicAPI
    public static final String HYPERLINK = "hyperlink";


}
