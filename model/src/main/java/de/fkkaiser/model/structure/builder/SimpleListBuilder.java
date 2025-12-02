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

import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.structure.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Builder class for creating SimpleList instances.
 * @author Katrin Kaiser
 * @version 1.0.0
 */
@PublicAPI
public class SimpleListBuilder {

    private String styleClass;
    private ListOrdering ordering;
    private List<ListItem> items;

    /**
     * Constructor initializes with default values.
     */
    @Internal
    public SimpleListBuilder(){
        items = new ArrayList<>();
        ordering = ListOrdering.UNORDERED;
    }

    /**
     * Sets the style class for the SimpleList.
     * @param styleClass the style class to set
     * @return the builder instance for chaining
     */
    @PublicAPI
    public SimpleListBuilder withStyleClass(String styleClass){
        this.styleClass = styleClass;
        return this;
    }

    /**
     * Sets the ordering type for the SimpleList.
     * @param ordering the ListOrdering to set
     * @return the builder instance for chaining
     */
    @PublicAPI
    public SimpleListBuilder withOrdering(ListOrdering ordering){
        Objects.requireNonNull(ordering);
        this.ordering = ordering;
        return this;
    }

    /**
     * Sets the list of items for the SimpleList.
     * @param items the list of ListItem to set
     * @return the builder instance for chaining
     */
    @PublicAPI
    public  SimpleListBuilder addItems(List<ListItem> items){
        Objects.requireNonNull(items);
        this.items = items;
        return this;
    }

    /**
     * Adds a single ListItem to the SimpleList.
     * @param item the ListItem to add
     * @return the builder instance for chaining
     */
    @PublicAPI
    public SimpleListBuilder addItem(ListItem item){
        Objects.requireNonNull(item);
        this.items.add(item);
        return this;
    }

    /**
     * Adds a text item to the SimpleList.
     * @param text the text content of the list item
     * @return the builder instance for chaining
     */
    @PublicAPI
    public SimpleListBuilder addTextAsItem(String text){
        Objects.requireNonNull(text);
        Paragraph p = Paragraph.builder(null).addInlineText(text).build();
        this.items.add(new ListItem(null,null,List.of(p)));
        return this;
    }

    /**
     * Adds a labeled text item to the SimpleList.
     * @param label the label of the list item
     * @param text the text content of the list item
     * @return the builder instance for chaining
     */
    @PublicAPI
    public SimpleListBuilder addLabelAndTextAsItem(String label, String text){
        Objects.requireNonNull(label, "Label must not be null");
        Objects.requireNonNull(text, "Text must not be null");
        Paragraph p = Paragraph.builder(null).addInlineText(text).build();
        TextRun lr = new TextRun(label,null);
        this.items.add(new ListItem(null,List.of(lr),List.of(p)));
        return this;
    }

    /**
     * Builds and returns the SimpleList instance.
     * @return the constructed SimpleList
     */
    @PublicAPI
    public SimpleList build(){
        return new SimpleList(styleClass,ordering,items);
    }

}
