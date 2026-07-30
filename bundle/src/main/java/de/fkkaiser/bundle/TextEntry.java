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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Represents a text entry in a bundle.
 * This class encapsulates a rich text string and an optional description.
 * Placeholder syntax is supported to allow for dynamic text replacement.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
public class TextEntry {


    private final String richText;
    private String description;


    @JsonCreator
    public TextEntry(
            @JsonProperty("rich-text") String richText,
            @JsonProperty("description") String description) {
        this.richText = richText;
        this.description = description;
    }
    public TextEntry(String richText) {
        this(richText,null);
    }

    public String getRichText() {
        return richText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String descrption) {
        this.description = descrption;
    }

    public boolean hasPlaceholders() {
        return CONSTANTS.TEXT_PLACEHOLDER_PATTERN.matcher(this.richText).find();
    }


    public List<String> extractPlaceholders() {
        List<String> placeholders = new ArrayList<>();
        Matcher matcher = CONSTANTS.TEXT_PLACEHOLDER_PATTERN.matcher(this.richText);

        while (matcher.find()) {
            placeholders.add(matcher.group(1));
        }

        return placeholders;
    }


}
