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
// NEU: z.B. in de/fkkaiser/generator/ImageResolver.java
package de.fkkaiser.generator;

import java.io.IOException;
import java.net.URISyntaxException;
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
    URL resolve(String relativePath) throws IOException, URISyntaxException;
}