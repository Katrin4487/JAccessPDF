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

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An {@link EResourceProvider} backed by an in-memory map of resource bytes,
 * typically the font files extracted from an imported ZIP bundle.
 *
 * <p>Since {@link EResourceProvider} resolves to a {@link URL}, each requested
 * resource is materialized once into a temporary file on first access and the
 * resulting {@code file:} URL is cached for subsequent lookups of the same name.
 * These temp files are transient for the lifetime of one PDF generation request -
 * they are not a persistence decision, just a way to hand FOP a URL it can read.</p>
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
public class MapBackedResourceProvider implements EResourceProvider {

    private final Map<String, byte[]> resources;
    private final Map<String, URL> materialized = new ConcurrentHashMap<>();

    /**
     * Constructs a MapBackedResourceProvider.
     *
     * @param resources a map of resource name (e.g. the "path" used in fonts.json) to its raw bytes;
     *                  must not be {@code null}
     */
    public MapBackedResourceProvider(Map<String, byte[]> resources) {
        this.resources = Objects.requireNonNull(resources, "resources must not be null");
    }

    /**
     * Resolves a resource by writing its bytes to a temporary file (once, then cached)
     * and returning a URL to that file.
     *
     * @param name the resource name; must match a key in the map passed to the constructor
     * @return a URL to the materialized resource, or {@code null} if no bytes are known for that name
     * @throws IOException if the temporary file cannot be created or written
     */
    @Override
    public URL getResource(String name) throws IOException {
        byte[] data = resources.get(name);
        if (data == null) {
            return null;
        }

        URL cached = materialized.get(name);
        if (cached != null) {
            return cached;
        }

        Path tempFile = Files.createTempFile("jaccesspdf-", suffixFor(name));
        Files.write(tempFile, data);
        tempFile.toFile().deleteOnExit();

        URL url = tempFile.toUri().toURL();
        materialized.put(name, url);
        return url;
    }

    private static String suffixFor(String name) {
        int lastDot = name.lastIndexOf('.');
        return lastDot >= 0 ? name.substring(lastDot) : "";
    }
}
