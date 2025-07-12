package de.kaiser.api.utils;

import java.io.IOException;
import java.net.URL;

public interface EResourceProvider {
    /**
     * Lädt eine Ressource anhand ihres Namens/Pfades.
     * @param name Der Name der Ressource (z.B. "fonts/Roboto-Regular.ttf").
     * @return Ein InputStream zur Ressource oder null, wenn nicht gefunden.
     * @throws IOException bei Lesefehlern.
     */
    URL getResource(String name) throws IOException;
}
