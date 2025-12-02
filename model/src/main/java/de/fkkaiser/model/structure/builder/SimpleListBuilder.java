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

import de.fkkaiser.model.structure.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SimpleListBuilder {

    private String styleClass;
    private ListOrdering ordering;
    private List<ListItem> items;

    public SimpleListBuilder(){
        items = new ArrayList<ListItem>();
        ordering = ListOrdering.UNORDERED;
    }

    public SimpleListBuilder withStyleClass(String styleClass){
        this.styleClass = styleClass;
        return this;
    }

    public SimpleListBuilder withOrdering(ListOrdering ordering){
        Objects.requireNonNull(ordering);
        this.ordering = ordering;
        return this;
    }

    public  SimpleListBuilder addItems(List<ListItem> items){
        Objects.requireNonNull(items);
        this.items = items;
        return this;
    }

    public SimpleListBuilder addItem(ListItem item){
        Objects.requireNonNull(item);
        this.items.add(item);
        return this;
    }

    public SimpleListBuilder addTextAsItem(String text){
        Objects.requireNonNull(text);
        Paragraph p = Paragraph.builder(null).addInlineText(text).build();
        this.items.add(new ListItem(null,null,List.of(p)));
        return this;
    }

    public SimpleListBuilder addLabelAndTextAsItem(String label, String text){
        Objects.requireNonNull(label, "Label must not be null");
        Objects.requireNonNull(text, "Text must not be null");
        Paragraph p = Paragraph.builder(null).addInlineText(text).build();
        TextRun lr = new TextRun(label,null);
        this.items.add(new ListItem(null,List.of(lr),List.of(p)));
        return this;
    }

    public SimpleList build(){
        return new SimpleList(styleClass,ordering,items);
    }

}
