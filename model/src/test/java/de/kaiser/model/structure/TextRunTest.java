package de.kaiser.model.structure;

import de.kaiser.model.style.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TextRun Style Resolution")
public class TextRunTest {

    @Mock
    private StyleResolverContext mockContext;
    @Mock
    private Map<String, ElementStyle> mockStyleMap;
    @Mock
    private TextBlockStyleProperties mockParentStyle;
    @Mock
    private TextRunStyleProperties mockSpecificStyle;

    @BeforeEach
    void setUp() {
        // Mock-Verhalten für den Kontext einrichten
        when(mockContext.getParentBlockStyle()).thenReturn(mockParentStyle);
        when(mockContext.getStyleMap()).thenReturn(mockStyleMap);
    }

    @Test
    @DisplayName("should resolve styles gracefully when style class is not found")
    void shouldResolveGracefullyWhenStyleNotFound() {
        TextRun textRun = new TextRun("Some text", "non-existent-style", null);
        when(mockStyleMap.get("non-existent-style")).thenReturn(null);
        textRun.resolveStyles(mockContext);
        assertNotNull(textRun.getResolvedStyle());
    }

    @Test
    @DisplayName("should resolve styles when style class is found")
    void shouldResolveWithSpecificStyle() {
        TextRun textRun = new TextRun("Some text", "found-style", null);
        ElementStyle elementStyle = new ElementStyle("found-style", StyleTargetTypes.TEXT_RUN, mockSpecificStyle);
        when(mockStyleMap.get("found-style")).thenReturn(elementStyle);
        textRun.resolveStyles(mockContext);
        assertNotNull(textRun.getResolvedStyle());
    }
}