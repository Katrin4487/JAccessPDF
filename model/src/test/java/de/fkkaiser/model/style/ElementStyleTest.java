package de.fkkaiser.model.style;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

        ParagraphStyleProperties properties = new ParagraphStyleProperties(/* ... ggf. Parameter hier einfügen ... */);
        ElementStyle originalStyle = new ElementStyle("important-paragraph", StyleTargetTypes.PARAGRAPH, properties);

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
        ElementStyle originalStyle = new ElementStyle("main-headline", StyleTargetTypes.HEADLINE, properties);

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
        ElementStyle originalStyle = new ElementStyle("bold-text", StyleTargetTypes.TEXT_RUN, properties);

        String json = objectMapper.writeValueAsString(originalStyle);

        ElementStyle deserializedStyle = objectMapper.readValue(json, ElementStyle.class);

        assertNotNull(deserializedStyle,"Style should not be null");
        assertEquals(originalStyle.name(), deserializedStyle.name(),"Name should match");
        assertEquals(originalStyle.targetElement(), deserializedStyle.targetElement(),"TargetElement should match");
        assertNotNull(deserializedStyle.properties(),"Properties should not be null");
        assertInstanceOf(TextRunStyleProperties.class, deserializedStyle.properties(), "The properties should be type of TextRunStyleProperties.");

    }


}