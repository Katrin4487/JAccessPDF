package de.fkkaiser.api.utils;

import java.net.URL;

/**
 * A standard implementation of {@link EResourceProvider} that loads resources from the Java classpath.
 * This implementation is suitable for standard Java/Maven projects where resources are packaged
 * within the application's JAR file or available on the classpath.
 *
 * <p>This provider automatically handles path normalization, ensuring that resource paths
 * work correctly regardless of whether they start with a forward slash.</p>
 *
 * <p><b>Resource Location:</b></p>
 * Resources are searched using the Java class loader mechanism, which looks for resources in:
 * <ul>
 *   <li>The application's resources directory (e.g., {@code src/main/resources} in Maven)</li>
 *   <li>JAR files on the classpath</li>
 *   <li>Directories on the classpath</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * EResourceProvider provider = new EClasspathResourceProvider();
 *
 * // Both of these work the same way:
 * URL font1 = provider.getResource("fonts/Roboto-Regular.ttf");
 * URL font2 = provider.getResource("/fonts/Roboto-Regular.ttf");
 *
 * // Load an image
 * URL logo = provider.getResource("images/company-logo.png");
 * }</pre>
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 * @see EResourceProvider
 */
public class EClasspathResourceProvider implements EResourceProvider {

    /**
     * Loads a resource from the classpath by its name or path.
     * The method automatically normalizes the path by ensuring it starts with a forward slash,
     * which is required for proper classpath resource loading.
     *
     * <p><b>Path Handling:</b></p>
     * <ul>
     *   <li>If the path starts with "/", it is used as-is</li>
     *   <li>If the path doesn't start with "/", a leading slash is added</li>
     * </ul>
     *
     * <p><b>Example paths:</b></p>
     * <ul>
     *   <li>{@code "fonts/Roboto-Regular.ttf"} → resolved to {@code "/fonts/Roboto-Regular.ttf"}</li>
     *   <li>{@code "/images/logo.png"} → used as-is</li>
     * </ul>
     *
     * @param name the name or path of the resource relative to the classpath root
     *             (e.g., "fonts/Roboto-Regular.ttf", "/images/logo.png")
     * @return a URL pointing to the resource, or {@code null} if the resource cannot be found
     *         on the classpath
     */
    @Override
    public URL getResource(String name) {
        // Ensure the path is absolute for classpath loading
        String path = name.startsWith("/") ? name : "/" + name;
        return EClasspathResourceProvider.class.getResource(path);
    }
}