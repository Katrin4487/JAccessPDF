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
 * Exception thrown during SVG conversion errors.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
public class SvgConversionException extends RuntimeException {
    /**
     * Constructor for SvgConversionException.
     *
     * @param message the error message
     * @param e       the underlying exception
     */
    public SvgConversionException(String message, Exception e) {
        super(message, e);
    }

    /**
     * Constructor for SvgConversionException with only message.
     *
     * @param message the error message
     */
    public SvgConversionException(String message) {
        super(message);
    }
}
