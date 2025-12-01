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
package de.fkkaiser.model.util;

import de.fkkaiser.model.annotation.Internal;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for handling dimension values with units.
 * This class only handles parsing and validation - logging is the responsibility of the caller.
 */
@Internal
public class DimensionUtil {

    public static final Set<String> SUPPORTED_UNITS = Set.of("cm", "mm", "in", "pt", "px");
    public static final String DEFAULT_UNIT = "cm";

    private static final Pattern DIMENSION_PATTERN = Pattern.compile("^([0-9]*\\.?[0-9]+)\\s*([a-zA-Z]*)$");

    /**
     * Result of dimension validation and normalization.
     * @param original the original dimension string
     * @param normalized the normalized dimension string
     * @param hasWarning indicates if there is a warning
     * @param warningMessage the warning message, if any
     */
    public record ValidationResult(String original, String normalized, boolean hasWarning, String warningMessage) {

        /**
         * Creates a successful ValidationResult without warnings.
         * @param dimension the dimension string
         * @return ValidationResult instance
         */
        @Internal
        public static ValidationResult ok(String dimension) {
            return new ValidationResult(dimension, dimension, false, null);
        }

        /**
         * Creates a ValidationResult with a warning.
         * @param original the original dimension string
         * @param normalized the normalized dimension string
         * @param message the warning message
         * @return ValidationResult instance
         */
        @Internal
        public static ValidationResult warning(String original, String normalized, String message) {
            return new ValidationResult(original, normalized, true, message);
        }
    }

    /**
     * Validates and normalizes a dimension string.
     * Returns a ValidationResult that the caller can use to log warnings.
     *
     * @param dimension the dimension string to validate
     * @return ValidationResult containing normalized dimension and optional warning
     * @throws IllegalArgumentException if dimension format is completely invalid
     */
    @Internal
    public static ValidationResult validateAndNormalize(String dimension) {
        if (dimension == null || dimension.trim().isEmpty()) {
            return ValidationResult.ok(null);
        }

        dimension = dimension.trim();
        Matcher matcher = DIMENSION_PATTERN.matcher(dimension);

        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                    String.format("Invalid dimension format: '%s'. Expected format: <number><unit> (e.g., '2.5cm', '1in')",
                            dimension));
        }

        String value = matcher.group(1);
        String unit = matcher.group(2);

        // No unit specified - use default
        if (unit.isEmpty()) {
            String normalized = value + DEFAULT_UNIT;
            String warning = String.format(
                    "Dimension '%s' has no unit. Using default unit '%s' → '%s'",
                    dimension, DEFAULT_UNIT, normalized);
            return ValidationResult.warning(dimension, normalized, warning);
        }

        // Unit specified but not supported
        if (!SUPPORTED_UNITS.contains(unit.toLowerCase())) {
            String normalized = value + DEFAULT_UNIT;
            String warning = String.format(
                    "Unsupported unit '%s' in dimension '%s'. Supported units: %s. Using default unit '%s' → '%s'",
                    unit, dimension, SUPPORTED_UNITS, DEFAULT_UNIT, normalized);
            return ValidationResult.warning(dimension, normalized, warning);
        }

        // Valid dimension
        return ValidationResult.ok(value + unit.toLowerCase());
    }

    /**
     * Parses a dimension string to a double value in centimeters.
     */
    @Internal
    public static double toCentimeters(String dimension) {
        if (dimension == null) {
            return 0.0;
        }

        Matcher matcher = DIMENSION_PATTERN.matcher(dimension.trim());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid dimension format: " + dimension);
        }

        double value = Double.parseDouble(matcher.group(1));
        String unit = matcher.group(2).toLowerCase();

        if (unit.isEmpty()) {
            unit = DEFAULT_UNIT;
        }

        return switch (unit) {
            case "mm" -> value / 10.0;
            case "in" -> value * 2.54;
            case "pt" -> value * 0.0353;
            case "px" -> value * 0.0264;
            default -> value; //cm is default
        };
    }

    public static boolean isValidLineHeight(String val) {
        if (val == null || val.trim().isEmpty()) return true; // inherit default

        if ("normal".equals(val)) return true;

        // Regex für:
        // 1. Zahl + Einheit (12pt, 1.5cm)
        // 2. Prozent (120%)
        // 3. Reine Zahl (1.5, 2)
        String pattern = "^\\s*" +
                "([0-9]+(\\.[0-9]+)?)" +
                "(pt|px|mm|cm|in|em|%|)" +
                "\\s*$";

        return val.matches(pattern);
    }
}