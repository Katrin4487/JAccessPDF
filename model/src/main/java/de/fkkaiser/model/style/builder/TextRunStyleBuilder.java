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
package de.fkkaiser.model.style.builder;

import de.fkkaiser.model.structure.ElementTargetType;
import de.fkkaiser.model.style.ElementStyle;
import de.fkkaiser.model.style.TextRunStyleProperties;

/**
 * Fluent builder for creating text run element styles with detailed properties.
 * This builder allows setting all text run-specific properties in a readable,
 * chainable manner.
 *
 * @author Katrin Kaiser
 * @version 1.0.1
 */
public class TextRunStyleBuilder {

    private final String name;
    private final TextRunStyleProperties properties;

    public TextRunStyleBuilder(String name){
        this.name = name;
        this.properties = new TextRunStyleProperties();
    }

    /**
     * Sets the name of the text style to apply to this inline element.
     *
     * @param textStyleName name of the text style
     */
    @SuppressWarnings("unused")
    public TextRunStyleBuilder withTextStyleName(String textStyleName){
        properties.setTextStyleName(textStyleName);
        return this;
    }

    /**
     * Sets the text color for this inline element.
     *
     * @param textColor the color to apply (e.g., {@code "#FF0000"}, {@code "rgb(255,0,0)"});
     *
     */
    @SuppressWarnings("unused")
    public TextRunStyleBuilder withTextColor(String textColor){
        properties.setTextColor(textColor);
        return this;
    }

    /**
     * Sets the text decoration for this inline element.
     *
     * @param textDecoration the decoration to apply (e.g., {@code "underline"}, {@code "line-through"});
     *
     */
    @SuppressWarnings("unused")
    public TextRunStyleBuilder withTextDecoration(String textDecoration){
        properties.setTextDecoration(textDecoration);
        return this;
    }

    /**
     * Sets the background color for this inline element.
     *
     * @param backgroundColor the color to apply (e.g., {@code "#FF0000"}, {@code "rgb(255,0,0)"});
     *
     */
    @SuppressWarnings("unused")
    public TextRunStyleBuilder withBackgroundColor(String backgroundColor){
        properties.setBackgroundColor(backgroundColor);
        return this;
    }

    @SuppressWarnings("unused")
    public TextRunStyleBuilder withBaselineShift(String baselineShift){
        properties.setBaselineShift(baselineShift);
        return this;
    }

    public ElementStyle build(){
        return new ElementStyle(this.name, ElementTargetType.TEXT_RUN,this.properties);
    }
}
