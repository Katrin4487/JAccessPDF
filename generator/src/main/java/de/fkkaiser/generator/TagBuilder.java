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
package de.fkkaiser.generator;

import de.fkkaiser.model.annotation.Internal;

import java.util.ArrayList;
import java.util.List;

@Internal
public class TagBuilder {
    private final StringBuilder builder;
    private String attributes = "";
    private final String tagName;
    private final List<Object> contentParts = new ArrayList<>();
    private String tagPrefix = "fo:";

    public TagBuilder(String tagName) {
        this.builder = new StringBuilder();
        this.tagName = tagName;
    }

    public TagBuilder addAttribute(String name, String value) {
        if (value != null) {
            attributes += GenerateConst.SPACE
                    + name + GenerateConst.EQUALS
                    + GenerateConst.GQQ
                    + GenerateUtils.escapeXml(value)
                    + GenerateConst.GQQ;
        }
        return this;
    }

    public TagBuilder addContent(String content) {
        contentParts.add(GenerateUtils.escapeXml(content));
        return this;
    }

    public TagBuilder addNestedContent(String content) {
        contentParts.add(content);
        return this;
    }

    public TagBuilder addChild(TagBuilder childBuilder) {
        contentParts.add(childBuilder);
        return this;
    }

    public TagBuilder withPrefix(String tagPrefix) {
        this.tagPrefix = tagPrefix;
        return this;
    }


    public String build() {
        builder.append(GenerateConst.OPENER_OPEN_TAG)
                .append(tagPrefix)
                .append(tagName);
        if (!attributes.isBlank()) {
            builder.append(attributes);
        }
        builder.append(GenerateConst.CLOSER);


        for (Object part : contentParts) {
            if (part instanceof TagBuilder) {
                builder.append(((TagBuilder) part).build());
            } else {
                builder.append(part.toString());
            }
        }

        builder.append(GenerateConst.OPENER_CLOSE_TAG)
                .append(tagPrefix)
                .append(tagName)
                .append(GenerateConst.CLOSER);

        return builder.toString();
    }

    public void buildInto(StringBuilder target) {
        target.append(build());
    }
}