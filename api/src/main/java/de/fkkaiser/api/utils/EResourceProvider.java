/*
 * Copyright 2025 Katrin Kaiser
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.fkkaiser.api.utils;

import java.io.IOException;
import java.net.URL;

/**
 * Interface for providing access to resources required during document processing.
 * Implementations of this interface define how resources (such as fonts, images, or stylesheets)
 * are located and loaded within different environments.
 *
 * <p>This abstraction allows the document processing framework to work with various
 * resource storage mechanisms, including classpath resources, file system access,
 * database storage, or remote URLs.</p>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * EResourceProvider provider = new EClasspathResourceProvider();
 * URL fontUrl = provider.getResource("fonts/Roboto-Regular.ttf");
 * if (fontUrl != null) {
 *     // Process the font
 * }
 * }</pre>
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 * @see EClasspathResourceProvider
 * @see EFopResourceResolver
 * @see EFopURIResolver
 */
public interface EResourceProvider {

    /**
     * Loads a resource by its name or path.
     * The interpretation of the name parameter depends on the implementation.
     * For example, a classpath-based implementation would search the classpath,
     * while a filesystem-based implementation might look in a specific directory.
     *
     * @param name the name or path of the resource (e.g., "fonts/Roboto-Regular.ttf",
     *             "images/logo.png", "styles/template.xsl")
     * @return a URL pointing to the resource, or {@code null} if the resource cannot be found
     * @throws IOException if an I/O error occurs while attempting to locate the resource
     *
     */
    URL getResource(String name) throws IOException;
}