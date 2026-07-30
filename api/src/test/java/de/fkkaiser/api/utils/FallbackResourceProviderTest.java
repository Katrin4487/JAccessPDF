package de.fkkaiser.api.utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FallbackResourceProviderTest {

    @Test
    void shouldReturnPrimaryResultWhenResolvable() throws Exception {
        URL expected = new URL("file:/primary/font.ttf");
        EResourceProvider primary = name -> expected;
        EResourceProvider fallback = name -> {
            throw new AssertionError("fallback should not be called");
        };

        FallbackResourceProvider provider = new FallbackResourceProvider(primary, fallback);

        assertEquals(expected, provider.getResource("fonts/Arial.ttf"));
    }

    @Test
    void shouldFallBackWhenPrimaryReturnsNull() throws Exception {
        URL expected = new URL("file:/fallback/default.ttf");
        EResourceProvider primary = name -> null;
        EResourceProvider fallback = name -> expected;

        FallbackResourceProvider provider = new FallbackResourceProvider(primary, fallback);

        assertEquals(expected, provider.getResource("fonts/Arial.ttf"));
    }

    @Test
    void shouldFallBackWhenPrimaryThrows() throws Exception {
        URL expected = new URL("file:/fallback/default.ttf");
        EResourceProvider primary = name -> { throw new IOException("not found"); };
        EResourceProvider fallback = name -> expected;

        FallbackResourceProvider provider = new FallbackResourceProvider(primary, fallback);

        assertEquals(expected, provider.getResource("fonts/Arial.ttf"));
    }

    @Test
    void shouldReturnNullWhenBothProvidersFail() throws Exception {
        EResourceProvider primary = name -> null;
        EResourceProvider fallback = name -> null;

        FallbackResourceProvider provider = new FallbackResourceProvider(primary, fallback);

        assertNull(provider.getResource("fonts/Arial.ttf"));
    }
}
