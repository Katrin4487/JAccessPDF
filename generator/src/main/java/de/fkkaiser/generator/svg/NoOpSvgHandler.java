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
 * Default SVG handler that rejects all SVG images.
 * This is used when no SVG support library is available.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
public class NoOpSvgHandler implements SvgHandler {

    @Override
    public byte[] convertToPng(byte[] svgBytes, float width, float height)
            throws SvgConversionException {
        throw new SvgConversionException(
                """
                        SVG support is not available. \
                        To enable SVG support, add one of the following dependencies:
                          - jaccess-pdf-svg-batik (recommended, full SVG support)
                        Or convert your SVG files to PNG before using them."""
        );
    }

    /**
     * Returns the name of this SVG handler.
     * @return "name" of the handler ("noop")
     */
    @Override
    public String getName() {
        return "noop";
    }

    /**
     *  Returns the priority of this SVG handler.
     *  This handler has the lowest priority to act as a fallback.
     * @return priority value (-1000)
     */
    @Override
    public int getPriority() {
        return -1000; // Lowest priority
    }

    /**
     * Indicates whether this SVG handler is available.
     * This handler is always available as a fallback.
     * @return true
     */
    @Override
    public boolean isAvailable() {
        return true; // Always available as fallback
    }
}