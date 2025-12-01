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
package de.fkkaiser.model.structure.builder;

import de.fkkaiser.model.structure.Headline;
import de.fkkaiser.model.structure.InlineElement;
import de.fkkaiser.model.structure.TextRun;

import java.util.ArrayList;
import java.util.List;

public class HeadlineBuilder {

    private List<InlineElement> inlineElements = new ArrayList<InlineElement>();
    private int level;
    private final String styleClass;

    public  HeadlineBuilder(String styleClass) {
        this.styleClass = styleClass;
        this.inlineElements = new ArrayList<>();
        this.level = 1;
    }
    public HeadlineBuilder addInlineElement(InlineElement inlineElement) {
        this.inlineElements.add(inlineElement);
        return this;
    }

    public HeadlineBuilder addText(String text) {
        this.inlineElements.add(new TextRun(text, null,null));
        return this;
    }
    public HeadlineBuilder setLevel(int level) {
        this.level = level;
        return this;
    }


    public Headline build() {
        return new Headline(this.styleClass, this.inlineElements,this.level);
    }

}
