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

import java.net.URL;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MapBackedResourceProviderTest {

    @Test
    void shouldReturnNullForUnknownResource() throws Exception {
        MapBackedResourceProvider provider = new MapBackedResourceProvider(Map.of());

        assertNull(provider.getResource("fonts/Unknown.ttf"));
    }

    @Test
    void shouldResolveKnownResourceToItsBytes() throws Exception {
        byte[] fontBytes = {1, 2, 3, 4, 5};
        MapBackedResourceProvider provider = new MapBackedResourceProvider(
                Map.of("fonts/Arial.ttf", fontBytes));

        URL url = provider.getResource("fonts/Arial.ttf");

        assertTrue(url != null);
        assertArrayEquals(fontBytes, url.openStream().readAllBytes());
    }

    @Test
    void shouldReturnSameUrlOnRepeatedLookups() throws Exception {
        byte[] fontBytes = {1, 2, 3};
        MapBackedResourceProvider provider = new MapBackedResourceProvider(
                Map.of("fonts/Arial.ttf", fontBytes));

        URL first = provider.getResource("fonts/Arial.ttf");
        URL second = provider.getResource("fonts/Arial.ttf");

        assertSame(first, second);
    }

    @Test
    void shouldKeepFileExtensionInMaterializedFile() throws Exception {
        byte[] fontBytes = {1};
        MapBackedResourceProvider provider = new MapBackedResourceProvider(
                Map.of("fonts/Arial.ttf", fontBytes));

        URL url = provider.getResource("fonts/Arial.ttf");

        assertTrue(url.getFile().endsWith(".ttf"));
    }
}
