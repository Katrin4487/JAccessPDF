/**
 * Contains the data models for defining layouts, styles, and design properties.
 * <p>
 * This package is at the heart of visual design. It defines how documents
 * and their individual elements (paragraphs, tables, etc.) are displayed.
 * The central class is the {@link de.kaiser.model.style.StyleSheet}, which bundles all
 * style information.
 *
 * <h2>Architectural Concepts</h2>
 * <ol>
 * <li>
 * <b>Layout Definitions:</b>
 * <ul>
 * <li>{@link de.kaiser.model.style.PageMasterStyle}: Defines the layout of a single page (size, margins, header/footer areas).</li>
 * <li>{@link de.kaiser.model.style.PageSequenceMaster}: Defines the sequence of page layouts (e.g., for the first page, even/odd pages).</li>
 * </ul>
 * </li>
 * <li>
 * <b>Element Styles:</b>
 * <ul>
 * <li>{@link de.kaiser.model.style.ElementStyle}: The central class that associates a name (e.g., "important-paragraph") with a set of properties.</li>
 * <li>{@link de.kaiser.model.style.ElementBlockStyleProperties}: An quasi-abstract base class for all property sets regarding a block element. Each concrete element (e.g., Paragraph, Table) has its own subclass to define specific attributes.</li>
 * <li>{@link de.kaiser.model.style.InlineElementStyleProperties}: An quasi-abstract base class for all property sets regarding an inline element. Each concrete element (e.g., Text-Run, Footnote) has its own subclass to define specific attributes.</li>
 * </ul>
 * </li>
 * <li>
 * <b>Text Styles (formerly Font Styles):</b>
 * <ul>
 * <li>{@link de.kaiser.model.style.TextStyle}: A named, reusable style (e.g., "headline-font") that combines a font family with size, weight, and style. These are referred to by the {@code EElementStyleProperties} to control typography.</li>
 * </ul>
 * </li>
 * <li>
 * <b>Style Resolution:</b>
 * <ul>
 * <li>{@link de.kaiser.model.style.StyleResolverContext}: An object that maintains state (e.g., the map of all available styles and the style of the current parent element) during the processing process to enable the correct inheritance of styles.</li>
 * </ul>
 * </li>
 * </ol>
 *
 * @since 1.0
 * @author Katrin Kaiser
 */
package de.kaiser.model.style;