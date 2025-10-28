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
 * A custom {@link ResourceResolver} for Apache FOP that integrates with the {@link EResourceProvider}.
 * This class acts as a bridge between Apache FOP's resource resolution mechanism and the application's
 * resource provider, enabling FOP to access resources such as images, fonts, and other assets
 * referenced in XSL-FO documents.
 *
 * <p><b>Purpose:</b></p>
 * Apache FOP requires a ResourceResolver to locate and load external resources during PDF generation.
 * This implementation delegates resource resolution to an {@link EResourceProvider}, allowing for
 * flexible resource management across different environments (classpath, filesystem, etc.).
 *
 * <p><b>Resource Types:</b></p>
 * This resolver can handle various resource types referenced in XSL-FO documents:
 * <ul>
 *   <li>Images (PNG, JPEG, GIF, etc.)</li>
 *   <li>Fonts (TTF, OTF, etc.)</li>
 *   <li>SVG graphics</li>
 *   <li>Other external resources</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * // Create a resource provider
 * EResourceProvider resourceProvider = new EClasspathResourceProvider();
 *
 * // Create the resolver
 * EFopResourceResolver resolver = new EFopResourceResolver(resourceProvider);
 *
 * // Configure FOP to use this resolver
 * FopFactoryBuilder builder = new FopFactoryBuilder(new File(".").toURI(), resolver);
 * FopFactory fopFactory = builder.build();
 * }</pre>
 *
 * @author FK Kaiser
 * @version 1.0
 * @see ResourceResolver
 * @see EResourceProvider
 * @see EFopURIResolver
 */
public final class EFopResourceResolver implements ResourceResolver {

    private static final Logger log = LoggerFactory.getLogger(EFopResourceResolver.class);

    /**
     * The resource provider used to locate and load resources.
     */
    private final EResourceProvider resourceProvider;

    /**
     * Constructs a new EFopResourceResolver with the specified resource provider.
     *
     * @param resourceProvider the resource provider to use for resolving resources;
     *                         must not be {@code null}
     * @throws IllegalArgumentException if resourceProvider is {@code null}
     */
    public EFopResourceResolver(EResourceProvider resourceProvider) {
        if (resourceProvider == null) {
            throw new IllegalArgumentException("ResourceProvider cannot be null.");
        }
        this.resourceProvider = resourceProvider;
    }

    /**
     * Resolves and retrieves a resource for the given URI.
     * This method is called by Apache FOP when it encounters a resource reference
     * in an XSL-FO document (e.g., an image or font reference).
     *
     * <p>The method performs the following steps:</p>
     * <ol>
     *   <li>Logs the URI being resolved for debugging purposes</li>
     *   <li>Delegates to the {@link EResourceProvider} to locate the resource</li>
     *   <li>Opens an InputStream to the resource</li>
     *   <li>Wraps the InputStream in a {@link Resource} object</li>
     * </ol>
     *
     * @param uri the URI of the resource to retrieve (e.g., "images/logo.png",
     *            "fonts/Arial.ttf")
     * @return a {@link Resource} object containing the InputStream for the resource
     * @throws IOException if the resource cannot be found or an I/O error occurs
     *                     while opening the resource
     */
    @Override
    public Resource getResource(URI uri) throws IOException {
        log.debug("FOP ResourceResolver is resolving URI: {}", uri);

        URL resourceUrl = resourceProvider.getResource(uri.toString());

        if (resourceUrl != null) {
            // Return a new Resource object, which wraps the InputStream
            return new Resource(resourceUrl.openStream());
        }

        log.warn("Could not resolve resource for URI via ResourceProvider: {}", uri);
        throw new IOException("Resource not found: " + uri);
    }

    /**
     * Attempts to get an OutputStream for writing to a resource at the given URI.
     *
     * <p><b>Note:</b> This operation is not supported by this resolver as it is designed
     * for read-only resource access. Apache FOP typically only needs to read resources,
     * not write them.</p>
     *
     * @param uri the URI of the resource to write to
     * @return an OutputStream (this method always throws an exception)
     * @throws UnsupportedOperationException always thrown, as writing resources is not supported
     */
    @Override
    public OutputStream getOutputStream(URI uri) throws IOException {
        log.error("OutputStream resolution is not supported by this resolver for URI: {}", uri);
        throw new UnsupportedOperationException("Writing resources is not supported by this resolver.");
    }
}