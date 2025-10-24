package de.fkkaiser.api.utils;

import java.net.URL;


/**
 * Eine Standardimplementierung des ResourceProviders, die den Java Classpath verwendet.
 * Nützlich für Standard-Java/Maven-Projekte.
 */
public class EClasspathResourceProvider implements EResourceProvider {
    @Override
    public URL getResource(String name) {
        // Stellt sicher, dass der Pfad für Classpath-Loading absolut ist.
        String path = name.startsWith("/") ? name : "/" + name;
        return EClasspathResourceProvider.class.getResource(path);
    }
}
