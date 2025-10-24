package de.fkkaiser.model.structure;

import de.fkkaiser.model.style.StyleResolverContext;
import de.fkkaiser.model.style.StyleResolverContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("Footnote Class Tests")
class FootnoteTest {

    @Mock
    private StyleResolverContext mockContext;
    @Mock
    private InlineElement mockInlineElement;

    @Test
    @DisplayName("should generate a unique ID on creation")
    void shouldGenerateUniqueId() {
        Footnote footnote1 = new Footnote(null, null, null, null);
        Footnote footnote2 = new Footnote(null, null, null, null);

        assertNotNull(footnote1.getId());
        assertNotEquals(footnote1.getId(), footnote2.getId());
    }

    @Test
    @DisplayName("should apply default index when provided index is null")
    void shouldApplyDefaultIndexWhenNull() {
        Footnote footnote = new Footnote(null, null, null, null);
        assertEquals("*", footnote.getIndex());
    }

    @Test
    @DisplayName("should apply default index when provided index is empty")
    void shouldApplyDefaultIndexWhenEmpty() {
        Footnote footnote = new Footnote("", null, null, null);
        assertEquals("*", footnote.getIndex());
    }
}