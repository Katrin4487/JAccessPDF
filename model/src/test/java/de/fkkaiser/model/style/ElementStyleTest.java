package de.fkkaiser.model.style;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Test
    @DisplayName("Should create ParagraphStyle with chain")
    public void shouldCreateParagraphStyleWithChain() {
        TextStyle.TextStyleFactory factory = new TextStyle.TextStyleFactory("Open Sans");
        TextStyle aStyle = factory.normal("normal-font",12);
        ElementStyle style = ElementStyle.paragraphBuilder("aName",aStyle)
                .withTextAlign("start")
                .withBackgroundColor("green")
                .withLanguage("en-US")
                .withSpaceBefore("5cm")
                .withSpaceAfter("6cm")
                .build();

        assertEquals(style.targetElement(),StyleTargetTypes.PARAGRAPH,"Should create style with target type paragraph");

        ParagraphStyleProperties properties = (ParagraphStyleProperties) style.properties();
        assertEquals(properties.getTextAlign(),"start", "Should create style with set alignment");
        assertEquals(properties.getBackgroundColor(),"green", "Should create style with set background color");
        assertEquals(properties.getLanguage(),"en-US", "Should create style with set language");
        assertEquals(properties.getSpaceBefore(),"5cm", "Should create style with space before 5");
        assertEquals(properties.getSpaceAfter(),"6cm", "Should create style with space after 6");
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

        assertEquals(style.targetElement(),StyleTargetTypes.HEADLINE,"Should create style with target type headline");
        HeadlineStyleProperties properties = (HeadlineStyleProperties) style.properties();
        assertEquals(properties.getSpaceAfter(),"1cm", "Should create style with set alignment");
        assertEquals(properties.getBackgroundColor(),"#F0F0F0", "Should create style with set background color");
        assertTrue(properties.getKeepWithNext());
    }
}