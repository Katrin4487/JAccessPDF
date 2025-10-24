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
 * EFopURIResolver is a class that implements the URIResolver interface to resolve resources for FOP processing.
 * It constructs the resource's full path based on the provided base path and resource name, then attempts to load
 * the resource using the specified EResourceProvider.
 *
 */
public class EFopURIResolver implements URIResolver {

    private static final Logger log = LoggerFactory.getLogger(EFopURIResolver.class);

    private final EResourceProvider resourceProvider;

    public EFopURIResolver( EResourceProvider resourceProvider) {

        this.resourceProvider = resourceProvider;
    }

    @Override
    public Source resolve(String href, String base) throws TransformerException {
        try {
            URL resourceUrl = resourceProvider.getResource(href);

            if (resourceUrl != null) {
                System.out.println("URIResolver: Ressource '" + href + "' erfolgreich als URL '" + resourceUrl + "' gefunden.");
                return new StreamSource(resourceUrl.openStream(), resourceUrl.toExternalForm());
            } else {
                System.err.println("URIResolver: Ressource '" + href + "' NICHT gefunden.");
                return null;
            }
        } catch (IOException e) {
            throw new TransformerException("Konnte Ressource nicht auflösen: " + href, e);
        }
    }
}

