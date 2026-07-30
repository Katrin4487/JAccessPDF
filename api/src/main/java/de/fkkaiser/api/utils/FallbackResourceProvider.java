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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * An {@link EResourceProvider} that tries a primary provider first and falls back
 * to a secondary provider if the primary one cannot resolve a resource.
 *
 * <p>Typical use case: a primary provider backed by imported/extracted files
 * (e.g. fonts unpacked from a ZIP) with a classpath-based fallback that guarantees
 * a resource (e.g. a default font) can always be resolved, even when a specific
 * font is missing from the import.</p>
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
public class FallbackResourceProvider implements EResourceProvider {

    private static final Logger log = LoggerFactory.getLogger(FallbackResourceProvider.class);

    private final EResourceProvider primary;
    private final EResourceProvider fallback;

    /**
     * Constructs a FallbackResourceProvider.
     *
     * @param primary  the provider to try first; must not be {@code null}
     * @param fallback the provider to use if the primary cannot resolve the resource;
     *                 must not be {@code null}
     */
    public FallbackResourceProvider(EResourceProvider primary, EResourceProvider fallback) {
        this.primary = Objects.requireNonNull(primary, "primary must not be null");
        this.fallback = Objects.requireNonNull(fallback, "fallback must not be null");
    }

    /**
     * Resolves a resource via the primary provider, falling back to the secondary
     * provider (with a warning log) if the primary provider cannot resolve it or
     * fails with an {@link IOException}.
     *
     * @param name the name or path of the resource
     * @return a URL pointing to the resource, or {@code null} if neither provider can resolve it
     * @throws IOException if the fallback provider itself fails
     */
    @Override
    public URL getResource(String name) throws IOException {
        URL url;
        try {
            url = primary.getResource(name);
        } catch (IOException e) {
            log.warn("Primary resource provider failed to resolve '{}', falling back: {}", name, e.getMessage());
            return fallback.getResource(name);
        }

        if (url == null) {
            log.warn("Primary resource provider could not resolve '{}', falling back to default", name);
            return fallback.getResource(name);
        }

        return url;
    }
}
