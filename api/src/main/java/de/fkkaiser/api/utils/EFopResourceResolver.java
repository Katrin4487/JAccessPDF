package de.fkkaiser.api.utils;

import org.apache.xmlgraphics.io.Resource;
import org.apache.xmlgraphics.io.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;

/**
 * A custom ResourceResolver for FOP that integrates with the EResourceProvider.
 * This class is used by the FopFactory to get InputStreams for resources
 * like images referenced in the XSL-FO document.
 */
public final class EFopResourceResolver implements ResourceResolver {

    private static final Logger log = LoggerFactory.getLogger(EFopResourceResolver.class);
    private final EResourceProvider resourceProvider;

    public EFopResourceResolver(EResourceProvider resourceProvider) {
        if (resourceProvider == null) {
            throw new IllegalArgumentException("ResourceProvider cannot be null.");
        }
        this.resourceProvider = resourceProvider;
    }

    /**
     * Called by FOP to get a Resource for a given URI.
     * @param uri The URI of the resource to retrieve.
     * @return A Resource object containing the InputStream for the resource.
     * @throws IOException if the resource cannot be found or read.
     */
    @Override
    public Resource getResource(URI uri) throws IOException {
        log.debug("FOP ResourceResolver is resolving URI: {}", uri);
        URL resourceUrl = resourceProvider.getResource(uri.toString());
        if (resourceUrl != null) {
            // CORRECTED: Return a new Resource object, which wraps the InputStream.
            return new Resource(resourceUrl.openStream());
        }
        log.warn("Could not resolve resource for URI via ResourceProvider: {}", uri);
        throw new IOException("Resource not found: " + uri);
    }

    /**
     * Called by FOP to get an OutputStream for a given URI.
     * This is typically not needed for reading resources, so we can throw an exception.
     * @param uri The URI of the resource to write to.
     * @return An OutputStream.
     * @throws IOException as this operation is not supported.
     */
    @Override
    public OutputStream getOutputStream(URI uri) throws IOException {
        log.error("OutputStream resolution is not supported by this resolver.");
        throw new UnsupportedOperationException("Writing resources is not supported.");
    }
}
