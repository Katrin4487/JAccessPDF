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
package de.fkkaiser.generator.svg;

/**
 * Service Provider Interface for SVG handling strategies.
 * <p>
 * Implementations can be provided by adding optional dependencies.
 * JAccessPDF will automatically detect and use available implementations.
 * </p>
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
public interface SvgHandler {

    /**
     * Converts SVG bytes to PNG bytes.
     *
     * @param svgBytes The SVG content
     * @param width Target width in pixels
     * @param height Target height in pixels
     * @return PNG bytes
     * @throws SvgConversionException if conversion fails
     */
    byte[] convertToPng(byte[] svgBytes, float width, float height) throws SvgConversionException;

    /**
     * Returns the name of this SVG handler implementation.
     */
    String getName();

    /**
     * Returns the priority of this handler (higher = preferred).
     * Allows users to control which implementation is used when multiple are available.
     */
    default int getPriority() {
        return 0;
    }

    /**
     * Checks if this handler is available (all required dependencies present).
     */
    boolean isAvailable();
}