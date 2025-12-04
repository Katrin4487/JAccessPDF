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
import de.fkkaiser.model.structure.ElementTargetType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class ElementStyleTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }


    @Test
    void shouldSerializeAndDeserializeParagraphStyle() throws Exception {

        ParagraphStyleProperties properties = new ParagraphStyleProperties();
        ElementStyle originalStyle = new ElementStyle("important-paragraph", ElementTargetType.PARAGRAPH, properties);

        String json = objectMapper.writeValueAsString(originalStyle);

        ElementStyle deserializedStyle = objectMapper.readValue(json, ElementStyle.class);

        assertNotNull(deserializedStyle,"Style should not be null");
        assertEquals(originalStyle.name(), deserializedStyle.name(),"Name should match");
        assertEquals(originalStyle.targetElement(), deserializedStyle.targetElement(),"TargetElement should match");
        assertNotNull(deserializedStyle.properties(),"Properties should not be null");
        assertInstanceOf(ParagraphStyleProperties.class, deserializedStyle.properties(), "The properties should by type of ParagraphStyleProperties.");

    }

    @Test
    void shouldSerializeAndDeserializeHeadlineStyle() throws Exception {

        HeadlineStyleProperties properties = new HeadlineStyleProperties();
        ElementStyle originalStyle = new ElementStyle("main-headline", ElementTargetType.HEADLINE, properties);

        String json = objectMapper.writeValueAsString(originalStyle);

        ElementStyle deserializedStyle = objectMapper.readValue(json, ElementStyle.class);

        assertNotNull(deserializedStyle,"Style should not be null");
        assertEquals(originalStyle.name(), deserializedStyle.name(),"Name should match");
        assertEquals(originalStyle.targetElement(), deserializedStyle.targetElement(),"TargetElement should match");
        assertNotNull(deserializedStyle.properties(),"Properties should not be null");
        assertInstanceOf(HeadlineStyleProperties.class, deserializedStyle.properties(), "The properties should be from type headline properties");
    }


    @Test
    void shouldSerializeAndDeserializeTextRunStyle() throws Exception {

        TextRunStyleProperties properties = new TextRunStyleProperties();
        ElementStyle originalStyle = new ElementStyle("bold-text", ElementTargetType.TEXT_RUN, properties);

        String json = objectMapper.writeValueAsString(originalStyle);

        ElementStyle deserializedStyle = objectMapper.readValue(json, ElementStyle.class);

        assertNotNull(deserializedStyle,"Style should not be null");
        assertEquals(originalStyle.name(), deserializedStyle.name(),"Name should match");
        assertEquals(originalStyle.targetElement(), deserializedStyle.targetElement(),"TargetElement should match");
        assertNotNull(deserializedStyle.properties(),"Properties should not be null");
        assertInstanceOf(TextRunStyleProperties.class, deserializedStyle.properties(), "The properties should be type of TextRunStyleProperties.");

    }

    @Test
    @DisplayName("Should create ParagraphStyle with chain")
    public void shouldCreateParagraphStyleWithChain() {
        TextStyle.TextStyleFactory factory = new TextStyle.TextStyleFactory("Open Sans");
        TextStyle aStyle = factory.normal("normal-font",12);
        ElementStyle style = ElementStyle.paragraphBuilder("aName",aStyle)
                .withTextAlign(TextAlign.START)
                .withBackgroundColor("green")
                .withLanguage("en-US")
                .withSpaceBefore("5cm")
                .withSpaceAfter("6cm")
                .build();

        assertEquals(ElementTargetType.PARAGRAPH, style.targetElement(),"Should create style with target type paragraph");

        ParagraphStyleProperties properties = (ParagraphStyleProperties) style.properties();
        assertEquals(TextAlign.START, properties.getTextAlign(), "Should create style with set alignment");
        assertEquals("green", properties.getBackgroundColor(), "Should create style with set background color");
        assertEquals("en-US", properties.getLanguage(), "Should create style with set language");
        assertEquals("5cm", properties.getSpaceBefore(), "Should create style with space before 5");
        assertEquals("6cm", properties.getSpaceAfter(), "Should create style with space after 6");
    }

    @Test
    @DisplayName("Should create HeadlineStyleWithChain")
    public void shouldCreateHeadlineStyleWithChain() {
        TextStyle.TextStyleFactory factory = new TextStyle.TextStyleFactory("Open Sans");
        TextStyle aStyle = factory.normal("normal-font",12);

        ElementStyle style = ElementStyle.headlineBuilder("myheadline",aStyle)
                .withBackgroundColor("#F0F0F0")
                .withSpaceAfter("1cm")
                .withSpaceBefore("1cm")
                .withKeepWithNext(true)
                .build();

        assertEquals(ElementTargetType.HEADLINE, style.targetElement(),"Should create style with target type headline");
        HeadlineStyleProperties properties = (HeadlineStyleProperties) style.properties();
        assertEquals("1cm", properties.getSpaceAfter(), "Should create style with set alignment");
        assertEquals("#F0F0F0", properties.getBackgroundColor(), "Should create style with set background color");
        assertTrue(properties.getKeepWithNext());
    }

    @Test
    @DisplayName("Should create image with chain")
    public void shouldCreateImageWithChain(){
        ElementStyle elementStyle = ElementStyle.imageBuilder("default")
                .withContentWidth("auto")
                .withAlignment("right")
                .withScaling("uniform")
                .build();
        assertEquals(ElementTargetType.BLOCK_IMAGE, elementStyle.targetElement(), "should have the correct target type");

        BlockImageStyleProperties properties = (BlockImageStyleProperties) elementStyle.properties();
        assertEquals("auto", properties.getContentWidth(), "Should create style with set alignment");
        assertEquals("right", properties.getAlignment(), "Should create style with set alignment");
        assertEquals("uniform", properties.getScaling(), "Should create style with set scaling");
    }
}