package de.fkkaiser.processor;

import de.fkkaiser.model.structure.ContentArea;
import de.fkkaiser.model.structure.Document;
import de.fkkaiser.model.structure.Element;
import de.fkkaiser.model.structure.PageSequence;
import de.fkkaiser.model.style.StyleResolverContext;
import de.fkkaiser.model.style.StyleSheet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StyleResolverService Tests")
class StyleResolverServiceTest {


    @Mock
    private Document mockDocument;
    @Mock
    private StyleSheet mockStyleSheet;
    @Mock
    private PageSequence mockPageSequence;
    @Mock
    private ContentArea mockHeader, mockBody, mockFooter;
    @Mock
    private Element mockElementHeader, mockElementBody, mockElementFooter;

    @Test
    @DisplayName("should call resolveStyles on all elements in the document")
    void shouldCallResolveStylesOnAllElements() {
        when(mockStyleSheet.elementStyles()).thenReturn(Collections.emptyList());

        when(mockDocument.pageSequences()).thenReturn(List.of(mockPageSequence));

        when(mockPageSequence.header()).thenReturn(mockHeader);
        when(mockPageSequence.body()).thenReturn(mockBody);
        when(mockPageSequence.footer()).thenReturn(mockFooter);
        when(mockHeader.elements()).thenReturn(List.of(mockElementHeader));
        when(mockBody.elements()).thenReturn(List.of(mockElementBody));
        when(mockFooter.elements()).thenReturn(List.of(mockElementFooter));
        StyleResolverService.resolve(mockDocument, mockStyleSheet);
        verify(mockElementHeader, times(1)).resolveStyles(any(StyleResolverContext.class));
        verify(mockElementBody, times(1)).resolveStyles(any(StyleResolverContext.class));
        verify(mockElementFooter, times(1)).resolveStyles(any(StyleResolverContext.class));
    }

    @Test
    @DisplayName("should do nothing and not throw an exception if document is null")
    void shouldDoNothingWhenDocumentIsNull() {
        assertDoesNotThrow(() -> StyleResolverService.resolve(null, mockStyleSheet));
    }

    @Test
    @DisplayName("should handle null content areas gracefully")
    void shouldHandleNullContentAreas() {
        when(mockStyleSheet.elementStyles()).thenReturn(Collections.emptyList());
        when(mockDocument.pageSequences()).thenReturn(List.of(mockPageSequence));
        when(mockPageSequence.body()).thenReturn(null);
        when(mockPageSequence.header()).thenReturn(null);
        when(mockPageSequence.footer()).thenReturn(null);

        assertDoesNotThrow(() -> StyleResolverService.resolve(mockDocument, mockStyleSheet));

        verify(mockElementHeader, never()).resolveStyles(any());
    }
}