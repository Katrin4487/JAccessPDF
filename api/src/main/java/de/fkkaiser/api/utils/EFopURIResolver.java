package de.fkkaiser.api.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.net.URL;

/**
 * A custom {@link URIResolver} implementation for XSLT transformations that integrates
 * with the {@link EResourceProvider}. This class enables XSLT processors to resolve
 * and load external resources (such as imported stylesheets, documents, or other assets)
 * during transformation.
 *
 * <p><b>Purpose:</b></p>
 * XSLT processors use URIResolver to handle {@code <xsl:import>}, {@code <xsl:include>},
 * and {@code document()} function calls. This implementation delegates resource resolution
 * to an {@link EResourceProvider}, allowing flexible resource management across different
 * environments.
 *
 * <p><b>Common Use Cases:</b></p>
 * <ul>
 *   <li>Resolving imported or included XSLT stylesheets</li>
 *   <li>Loading external XML documents referenced in transformations</li>
 *   <li>Accessing other resources needed during XSLT processing</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * // Create a resource provider
 * EResourceProvider resourceProvider = new EClasspathResourceProvider();
 *
 * // Create the URI resolver
 * EFopURIResolver uriResolver = new EFopURIResolver(resourceProvider);
 *
 * // Configure the XSLT transformer to use this resolver
 * TransformerFactory factory = TransformerFactory.newInstance();
 * factory.setURIResolver(uriResolver);
 *
 * Transformer transformer = factory.newTransformer(xslSource);
 * transformer.transform(xmlSource, result);
 * }</pre>
 *
 * @author FK Kaiser
 * @version 1.0
 * @see URIResolver
 * @see EResourceProvider
 * @see EFopResourceResolver
 */
public class EFopURIResolver implements URIResolver {

    private static final Logger log = LoggerFactory.getLogger(EFopURIResolver.class);

    /**
     * The resource provider used to locate and load resources.
     */
    private final EResourceProvider resourceProvider;

    /**
     * Constructs a new EFopURIResolver with the specified resource provider.
     *
     * @param resourceProvider the resource provider to use for resolving URIs;
     *                         must not be {@code null}
     * @throws IllegalArgumentException if resourceProvider is {@code null}
     */
    public EFopURIResolver(EResourceProvider resourceProvider) {
        if (resourceProvider == null) {
            throw new IllegalArgumentException("ResourceProvider cannot be null.");
        }
        this.resourceProvider = resourceProvider;
    }

    /**
     * Resolves a URI reference to a {@link Source} object that can be used by the XSLT processor.
     * This method is called by the XSLT transformer when it encounters references to external
     * resources (e.g., {@code <xsl:import>}, {@code <xsl:include>}, or {@code document()} calls).
     *
     * <p><b>Resolution Process:</b></p>
     * <ol>
     *   <li>The method receives an href (the resource reference) and a base URI</li>
     *   <li>It delegates to the {@link EResourceProvider} to locate the resource</li>
     *   <li>If found, it creates a {@link StreamSource} with the resource's InputStream</li>
     *   <li>The StreamSource includes the system ID for proper error reporting</li>
     * </ol>
     *
     * <p><b>Note:</b> The {@code base} parameter is provided by the XSLT processor but may
     * not always be used by this implementation, depending on how the {@link EResourceProvider}
     * handles resource resolution.</p>
     *
     * @param href the URI reference to resolve (e.g., "common/header-template.xsl",
     *             "data/countries.xml")
     * @param base the base URI from which the href is resolved; may be {@code null} or empty
     * @return a {@link Source} object containing the resolved resource, or {@code null} if
     *         the resource cannot be found
     * @throws TransformerException if an error occurs while resolving the resource
     */
    @Override
    public Source resolve(String href, String base) throws TransformerException {
        log.debug("URIResolver: Attempting to resolve resource '{}' with base '{}'", href, base);

        try {
            URL resourceUrl = resourceProvider.getResource(href);

            if (resourceUrl != null) {
                log.debug("URIResolver: Successfully resolved resource '{}' to URL '{}'",
                        href, resourceUrl);
                // Create a StreamSource with both the InputStream and system ID
                // The system ID helps the processor resolve relative references
                return new StreamSource(resourceUrl.openStream(), resourceUrl.toExternalForm());
            } else {
                log.warn("URIResolver: Resource '{}' could not be found", href);
                return null;
            }
        } catch (IOException e) {
            log.error("URIResolver: Failed to resolve resource '{}': {}", href, e.getMessage());
            throw new TransformerException("Could not resolve resource: " + href, e);
        }
    }
}