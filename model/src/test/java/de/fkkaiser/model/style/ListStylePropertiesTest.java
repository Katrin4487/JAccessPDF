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
package de.fkkaiser.model.style;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ListStyleProperties class.
 */
class ListStylePropertiesTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("copy() should create a new instance with identical properties")
    void copyShouldCreateIdenticalInstance() {
        // 1. Arrange: Create an original object with all properties set
        ListStyleProperties original = new ListStyleProperties();
        original.setTextStyleName("test-font");
        original.setTextColor("#111111");
        original.setProvDistBetweenStarts("2cm");
        original.setProvLabelSeparation("0.5cm");
        original.setListStyleType(ListStyleType.BULLET);
        original.setListStyleImage("url(bullet.png)");

        // 2. Act: Create a copy
        ListStyleProperties copy = original.copy();

        // 3. Assert: Verify the copy
        assertNotSame(original, copy, "The copied object should be a new instance.");
        assertEquals(original.getTextStyleName(), copy.getTextStyleName());
        assertEquals(original.getTextColor(), copy.getTextColor());
        assertEquals(original.getProvDistBetweenStarts(), copy.getProvDistBetweenStarts());
        assertEquals(original.getProvLabelSeparation(), copy.getProvLabelSeparation());
        assertEquals(original.getListStyleType(), copy.getListStyleType());
        assertEquals(original.getListStyleImage(), copy.getListStyleImage());
    }

//    @Test
//    @DisplayName("mergeWith() should inherit properties from a base style")
//    void mergeWithShouldInheritNullProperties() {
//       ListStyleProperties base = new ListStyleProperties();
//        base.setTextColor("#000000");
//        base.setListStyleType("square");
//        base.setProvDistBetweenStarts("3cm");
//
//        ListStyleProperties target = new ListStyleProperties();
//        target.setTextColor("#FF0000");
//        target.mergeWith(base);
//
//        // 3. Assert: Check the merged properties
//        assertEquals("#FF0000", target.getTextColor(), "Target's existing color should be kept.");
//        assertEquals("square", target.getListStyleType(), "list-style-type should be inherited from base.");
//        assertEquals("3cm", target.getProvDistBetweenStarts(), "provisional-distance should be inherited from base.");
//    }

    @Test
    @DisplayName("should deserialize correctly from a JSON string")
    void shouldDeserializeFromJson() throws Exception {
        String json = """
        {
          "text-style-name": "list-font",
          "text-color": "#222",
          "provisional-distance-between-starts": "2.5cm",
          "provisional-label-separation": "1cm",
          "list-style-type": "circle",
          "list-style-image": "url(image.png)"
        }
        """;

        // 2. Act: Deserialize the JSON
        ListStyleProperties properties = objectMapper.readValue(json, ListStyleProperties.class);

        // 3. Assert: Verify all fields are mapped correctly
        assertNotNull(properties);
        assertEquals("list-font", properties.getTextStyleName());
        assertEquals("#222", properties.getTextColor());
        assertEquals("2.5cm", properties.getProvDistBetweenStarts());
        assertEquals("1cm", properties.getProvLabelSeparation());
        assertEquals(ListStyleType.CIRCLE, properties.getListStyleType());
        assertEquals("url(image.png)", properties.getListStyleImage());
    }
}
