package de.fkkaiser.model.util;

import de.fkkaiser.model.annotation.Internal;

/**
 * Utility class for validating border property strings.
 *
 * @author Katrin Kaiser
 * @version 1.0.1
 */
public class BorderUtil {

    /**
     * Validates if the provided border string conforms to expected CSS-like syntax.
     * Acceptable formats include:
     * - "none" or "hidden"
     * - "<width> <style> <color>", where:
     *   - width: a number followed by pt, px, mm, cm, in, em, %, or thin, medium, thick
     *   - style: solid, dashed, dotted, double, groove, ridge, inset, outset
     *   - color: hex code (#RRGGBB or #RGB), color name, or rgb(r,g,b)
     *
     * @param border The border string to validate.
     * @return true if the border string is valid or empty/null; false otherwise.
     */
    @Internal
    public static boolean isValidBorder(String border) {
        if (border == null || border.trim().isEmpty()) {
            return true;
        }

        if (border.equals("none") || border.equals("hidden")) {
            return true;
        }

        // Regex korrigiert mit Klammern um den Width-Block
        String pattern = "^\\s*" +
                "(([0-9.]+)(pt|px|mm|cm|in|em|%)|thin|medium|thick)" +
                "\\s+" +
                "(solid|dashed|dotted|double|groove|ridge|inset|outset)" +
                "\\s+" +
                "(#[0-9a-fA-F]{6}|#[0-9a-fA-F]{3}|[a-zA-Z]+|rgb\\([^)]+\\))" +
                "\\s*$";

        return border.matches(pattern);
    }
}