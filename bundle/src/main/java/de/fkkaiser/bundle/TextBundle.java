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
package de.fkkaiser.bundle;

import java.util.Map;


/**
 * Represents a text bundle containing text content.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
public class TextBundle {

    Map<String,TextEntry> textContent;

    public TextBundle(Map<String,TextEntry> textContent) {
        this.textContent = textContent;
    }

    public Map<String, TextEntry> getTextContent() {
        return textContent;
    }

}
