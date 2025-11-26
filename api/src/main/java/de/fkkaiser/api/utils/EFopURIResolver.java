package de.fkkaiser.api.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * Technical helper class required by the Java XML Transformer.
 * <p>
 * While the {@link EFopResourceResolver} handles visual assets (images/fonts),
 * this class handles <b>structural resources</b> during the XML transformation phase.
 * </p>
 * <p>
 * <b>Relevance for this Project:</b><br>
 * Even though this project generates PDFs primarily from Java objects (and not via complex XSLT scripts),
 * the underlying XML processor requires a {@link URIResolver} to be configured to handle
 * potential path resolutions correctly without throwing errors.
 * </p>
 * <p>
 * It acts as a bridge, delegating any internal path resolution requests to the
 * {@link EResourceProvider}.
 * </p>
 *
 * @param resourceProvider The resource provider used to locate resources.
 * @author Katrin Kaiser
 * @version 1.0.0
 * @see EFopResourceResolver (for Images/Fonts)
 */
public record EFopURIResolver(EResourceProvider resourceProvider) implements URIResolver {

    private static final Logger log = LoggerFactory.getLogger(EFopURIResolver.class);

    /**
     * Creates a new URIResolver.
     *
     * @param resourceProvider the provider to bridge technical resource requests; must not be null.
     */
    public EFopURIResolver {
        Objects.requireNonNull(resourceProvider, "ResourceProvider cannot be null.");
    }

    /**
     * Called internally by the XML Transformer when it needs to resolve a relative path.
     * <p>
     * This implementation attempts to find the requested resource via the {@link EResourceProvider}.
     * If found, it returns a {@link StreamSource} for the processor to consume (closing is handled internally).
     * </p>
     *
     * @param href the relative path/URI to resolve
     * @param base the base URI (context), often unused in this specific setup
     * @return a {@link Source} containing the resource stream, or {@code null} if not found
     * @throws TransformerException if the resolution process fails technically
     */
    @Override
    public Source resolve(String href, String base) throws TransformerException {
        // Just a sanity check for the XML processor
        Objects.requireNonNull(href, "href cannot be null.");

        // Log on trace/debug level as this is mostly internal noise
        log.debug("XML Transformer requesting path resolution for: '{}'", href);

        try {
            // Delegate to our main resource mechanism
            URL resourceUrl = resourceProvider.getResource(href);

            if (resourceUrl != null) {
                // Return the stream AND the system ID (url) so relative paths inside that file would work
                return new StreamSource(resourceUrl.openStream(), resourceUrl.toExternalForm());
            } else {
                // If we return null, the default resolver might try (or it just fails gracefully)
                log.warn("URIResolver: Could not find '{}' via ResourceProvider.", href);
                return null;
            }
        } catch (IOException e) {
            log.error("Technical error resolving XML resource '{}': {}", href, e.getMessage());
            throw new TransformerException("Failed to resolve URI: " + href, e);
        }
    }
}