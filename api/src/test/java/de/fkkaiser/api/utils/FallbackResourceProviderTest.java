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
