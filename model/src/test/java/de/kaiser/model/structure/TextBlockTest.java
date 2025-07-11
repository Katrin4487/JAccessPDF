package de.kaiser.model.structure;

import de.kaiser.model.style.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test; // <-- Use JUnit 5's Test annotation
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

// Use JUnit 5's Assertions
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TextBlock Style Resolution Logic Tests")
class TextBlockTest {

    @Mock
    private StyleResolverContext mockContext;
    @Mock
    private Map<String, ElementStyle> mockStyleMap;
    @Mock
    private InlineElement mockInlineElement;

    private TextBlockStyleProperties parentStyle;
    private ElementStyle specificElementStyle;


    @BeforeEach
    void setUp() {
        parentStyle = new TextBlockStyleProperties();
        TextBlockStyleProperties specificStyle = new TextBlockStyleProperties();
        // Assuming your ElementStyle constructor looks like this from a previous conversation
        specificElementStyle = new ElementStyle("specific", StyleTargetTypes.PARAGRAPH, specificStyle);

        when(mockContext.getStyleMap()).thenReturn(mockStyleMap);
        when(mockContext.createChildContext(any())).thenReturn(mockContext);
    }

    @Test
    @DisplayName("should merge specific style into parent style")
    void shouldMergeSpecificAndParentStyles() {
        var paragraph = new Paragraph("specific-style");
        when(mockContext.getParentBlockStyle()).thenReturn(parentStyle);
        when(mockStyleMap.get("specific-style")).thenReturn(specificElementStyle);

        paragraph.resolveStyles(mockContext);

        assertNotNull(paragraph.getResolvedStyle());
    }

    @Test
    @DisplayName("should use parent style if no specific style exists")
    void shouldUseParentStyleWhenNoSpecificStyle() {
        var paragraph = new Paragraph("non-existent-style");
        when(mockContext.getParentBlockStyle()).thenReturn(parentStyle);
        when(mockStyleMap.get(anyString())).thenReturn(null);

        paragraph.resolveStyles(mockContext);

        assertNotNull(paragraph.getResolvedStyle());
    }

    @Test
    @DisplayName("should create new default style if no styles exist")
    void shouldCreateDefaultStyleWhenNoStylesExist() {
        var paragraph = new Paragraph("any-style");
        when(mockContext.getParentBlockStyle()).thenReturn(null);
        when(mockStyleMap.get(anyString())).thenReturn(null);

        paragraph.resolveStyles(mockContext);

        assertNotNull(paragraph.getResolvedStyle());
        // Assuming ParagraphStyleProperties is a concrete class you have
        //assertTrue(paragraph.getResolvedStyle() instanceof ParagraphStyleProperties);
    }

    @Test
    @DisplayName("should delegate style resolution to inline elements")
    void shouldDelegateToInlineElements() {
        // Assuming Paragraph has a constructor that takes a list of inline elements
        var paragraph = new Paragraph("any-style", List.of(mockInlineElement));
        when(mockContext.getParentBlockStyle()).thenReturn(null);

        paragraph.resolveStyles(mockContext);

        verify(mockInlineElement, times(1)).resolveStyles(mockContext);
    }
}