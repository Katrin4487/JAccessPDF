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

import de.fkkaiser.model.annotation.Internal;
import org.apache.xmlgraphics.io.Resource;
import org.apache.xmlgraphics.io.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Bridges Apache FOP's resource loading with the application's {@link EResourceProvider}.
 * <p>
 * During PDF generation, Apache FOP needs to access external assets such as:
 * <ul>
 * <li><b>Images</b> (referenced in the document via {@code addImage})</li>
 * <li><b>Fonts</b> (referenced in the style definitions, e.g., "Open Sans")</li>
 * </ul>
 * <p>
 * Since FOP cannot natively load files from inside a JAR or a custom classpath structure,
 * this resolver intercepts these requests and delegates them to the {@link EResourceProvider}.
 * </p>
 * <p>
 * <b>Lifecycle Management:</b><br>
 * This class implements {@link AutoCloseable} to track and close all InputStreams opened
 * during the PDF generation process. This prevents file locks and memory leaks.
 * </p>
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
@Internal
public class EFopResourceResolver implements ResourceResolver, AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(EFopResourceResolver.class);
    private final EResourceProvider resourceProvider;

    // Tracks all streams opened for FOP to ensure they are closed later
    private final List<InputStream> openStreams = Collections.synchronizedList(new ArrayList<>());

    /**
     * Creates a new resolver that uses the given provider to locate assets.
     *
     * @param resourceProvider the provider used to load images and fonts from the classpath/filesystem
     */
    public EFopResourceResolver(EResourceProvider resourceProvider) {
        this.resourceProvider = Objects.requireNonNull(resourceProvider, "ResourceProvider cannot be null.");
    }

    /**
     * Called by Apache FOP when it needs to load an asset (Image or Font).
     * <p>
     * This method translates the URI request from FOP (e.g., "images/logo.png") into an
     * actual InputStream provided by the application's resource handling.
     * </p>
     *
     * @param uri the URI of the asset (image path or font file) required by FOP
     * @return a {@link Resource} containing the InputStream and MIME-type of the asset
     * @throws IOException if the asset (image/font) could not be found via the ResourceProvider
     */
    @Override
    public Resource getResource(URI uri) throws IOException {
        log.debug("FOP requesting asset: {}", uri.getPath());

        // Normalize URI: FOP might send 'file:images/logo.png' or just 'images/logo.png'
        String pathRequest = uri.getPath();
        if (pathRequest == null) pathRequest = uri.toString();

        // Delegate to our provider (e.g., load from JAR)
        URL resourceUrl = resourceProvider.getResource(pathRequest);
        if (resourceUrl == null) {
            throw new IOException("Asset not found via ResourceProvider: " + pathRequest);
        }

        // Open connection to detect MIME-type (important for image processing)
        URLConnection connection = resourceUrl.openConnection();
        connection.setUseCaches(false); // Avoid caching issues with JAR resources

        InputStream inputStream = connection.getInputStream();
        String mimeType = connection.getContentType();

        // Track the stream to close it safely after PDF generation
        openStreams.add(inputStream);

        return new Resource(mimeType, inputStream);
    }

    /**
     * Returns an OutputStream for the given URI.
     * This is strictly for reading assets (images/fonts), so writing is disabled.
     */
    @Override
    public OutputStream getOutputStream(URI uri) {
        throw new UnsupportedOperationException("Writing resources is not supported by this resolver.");
    }

    /**
     * Closes all InputStreams that were opened for FOP during this session.
     * This ensures that no file handles (images/fonts) remain locked after PDF generation.
     */
    @Override
    public void close() {
        if (openStreams.isEmpty()) {
            return;
        }
        log.debug("Cleaning up: closing {} asset streams managed by EFopResourceResolver.", openStreams.size());

        for (InputStream stream : openStreams) {
            try {
                stream.close();
            } catch (IOException e) {
                // Log but continue closing other streams
                log.warn("Warning: Failed to close an asset stream: {}", e.getMessage());
            }
        }
        openStreams.clear();
    }
}