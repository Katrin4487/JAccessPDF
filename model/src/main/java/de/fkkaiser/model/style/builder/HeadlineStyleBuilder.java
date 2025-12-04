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

import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.structure.ElementTargetType;
import de.fkkaiser.model.style.ElementStyle;
import de.fkkaiser.model.style.HeadlineStyleProperties;
import de.fkkaiser.model.style.TextStyle;

/**
 * Fluent builder for creating headline element styles with detailed properties.
 * @author Katrin Kaiser
 * @version 1.0.1
 *
 */
@PublicAPI
public class HeadlineStyleBuilder {
    private final String name;
    private final HeadlineStyleProperties properties;

    /**
     * Constructor for HeadlineStyleBuilder.
     *
     * @param name      The name of the headline style.
     * @param textStyle The base text style to be used for this headline.
     */
    @PublicAPI
    public HeadlineStyleBuilder(String name, TextStyle textStyle) {
        this.name = name;
        this.properties = new HeadlineStyleProperties();
        this.properties.setTextStyleName(textStyle.name());
    }

    /**
     * Defines the white space before a headline.
     * You can choose between the units: mm, cm, in, pt, pc, em or %.
     *
     * @param spaceBefore String with value and unit for space before
     * @return HeadlineStyleBuilder to use this method in a chain
     */
    @PublicAPI
    public HeadlineStyleBuilder withSpaceBefore(String spaceBefore) {
        properties.setSpaceBefore(spaceBefore);
        return this;
    }

    /**
     * Defines the white space after a headline.
     * You can choose between the units: mm, cm, in, pt, pc, em or %.
     *
     * @param spaceAfter String with value and unit for space after
     * @return HeadlineStyleBuilder to use this method in a chain
     */
    @PublicAPI
    public HeadlineStyleBuilder withSpaceAfter(String spaceAfter) {
        properties.setSpaceAfter(spaceAfter);
        return this;
    }

    /**
     * Defines the layout instruction that prevents a page break between the current block and
     * the block that immediately follows it.
     * Available values are:
     * <ul>
     *     <li>int value: sets a strength or priority for the keep</li>
     *     <li>auto: (default): sets strength to 0</li>
     *     <li>always: is equivalent to the highest possible strength (like infinity)</li>
     * </ul>
     *
     * @param keepWithNext key-value for keept with next
     * @return HeadlineStyleBuilder to use this method in a chain
     */
    @PublicAPI
    public HeadlineStyleBuilder withKeepWithNext(boolean keepWithNext) {
        properties.setKeepWithNext(keepWithNext);
        return this;
    }

    /**
     * Defines the background color of this headline. Valid color values are:
     * <ul>
     *     <li>HexCode: e.g. #F0F0F0</li>
     *     <li>Color Name: e.g. yellow</li>
     *     <li>RGB-Function: rgb(255,0,0)</li>
     * </ul>
     *
     * @param color background color for this headline as string
     * @return HeadlineStyleBuilder to use this method in a chain
     */
    @PublicAPI
    public HeadlineStyleBuilder withBackgroundColor(String color) {
        properties.setBackgroundColor(color);
        return this;
    }

    /**
     * Builder method of the HeadlineStyleBuilder chain. This method must be called
     * at the end of every HeadlineStyleBuilder chain.
     *
     * @return ElementStyle of a headline that was created with a HeadlineBuilder chain.
     */
    @PublicAPI
    public ElementStyle build() {
        return new ElementStyle(name, ElementTargetType.HEADLINE, properties);
    }
}
