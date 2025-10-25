// NEU: z.B. in de/fkkaiser/generator/ImageResolver.java
package de.fkkaiser.generator;

import java.io.IOException;
import java.net.URL;

/**
 * Ein Interface, das der Generator-Modul bereitstellt,
 * damit höhere Module (wie 'api') eine Logik zur
 * Bildauflösung bereitstellen können.
 */
@FunctionalInterface
public interface ImageResolver {
    URL resolve(String relativePath) throws IOException;
}