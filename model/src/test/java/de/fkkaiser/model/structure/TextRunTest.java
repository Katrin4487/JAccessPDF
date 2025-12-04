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
package de.fkkaiser.model.structure;

import de.fkkaiser.model.style.*;
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
        when(mockContext.parentBlockStyle()).thenReturn(mockParentStyle);
        when(mockContext.styleMap()).thenReturn(mockStyleMap);
    }

    @Test
    @DisplayName("should resolve styles gracefully when style class is not found")
    void shouldResolveGracefullyWhenStyleNotFound() {
        TextRun textRun = new TextRun("Some text", "non-existent-style");
        when(mockStyleMap.get("non-existent-style")).thenReturn(null);
        textRun.resolveStyles(mockContext);
        assertNotNull(textRun.getResolvedStyle());
    }

    @Test
    @DisplayName("should resolve styles when style class is found")
    void shouldResolveWithSpecificStyle() {
        TextRun textRun = new TextRun("Some text", "found-style");
        ElementStyle elementStyle = new ElementStyle("found-style", ElementTargetType.TEXT_RUN, mockSpecificStyle);
        when(mockStyleMap.get("found-style")).thenReturn(elementStyle);
        textRun.resolveStyles(mockContext);
        assertNotNull(textRun.getResolvedStyle());
    }
}