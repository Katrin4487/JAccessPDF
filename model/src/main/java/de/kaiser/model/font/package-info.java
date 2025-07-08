/**
 * Contains the data models for defining and configuring fonts.
 * <p>
 * This package provides the foundation for the entire font management of the library.
 * It allows the declaration of font families and their various types (bold, italic, etc.)
 * and provides named, reusable font styles that can be referenced in the
 * {@link de.kaiser.model.style.StyleSheet}.
 *
 * <h2>Architecture</h2>
 * The system is based on three central concepts:
 * <ol>
 * <li>
 * <b>{@link de.kaiser.model.font.FontFamily}</b>:
 * Defines a font family (e.g., "Open Sans"). It bundles a list of
 * {@link de.kaiser.model.font.FontType} objects.
 * </li>
 * <li>
 * <b>{@link de.kaiser.model.font.FontType}</b>:
 * Represents a single font type. It links a physical font file
 * (e.g., "OpenSans-Bold.ttf") with a specific {@code font-weight} and {@code font-style}.
 * </li>
 * </ol>
 *
 * @since 1.0
 * @author Katrin Kaiser
 */
package de.kaiser.model.font;

