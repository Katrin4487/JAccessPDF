// NEU: z.B. in de/fkkaiser/generator/ImageResolver.java
package de.fkkaiser.generator;

import java.io.IOException;
import java.net.URL;

/**
 * Functional interface for resolving image resources based on a relative path.
 * Implementations of this interface define how to locate and load images
 * required during document generation.
 *
 * <p>This abstraction allows the document generation framework to work with various
 * image storage mechanisms, including classpath resources, file system access,
 * database storage, or remote URLs.</p>

 * @author Katrin Kaiser
 * @version 1.0.0
 */
@FunctionalInterface
public interface ImageResolver {
    URL resolve(String relativePath) throws IOException;
}