/**
 * Contains the data models for defining and configuring fonts for PDF generation.
 * This package provides a comprehensive font management system that allows declaration
 * of font families, their variants (bold, italic, etc.), and configuration of font
 * properties required by Apache FOP.
 *
 * <h2>Package Overview</h2>
 * <p>
 * The font package forms the foundation of the library's font management capabilities.
 * It enables users to define which fonts should be embedded in generated PDFs, specify
 * their characteristics (style and weight), and organize them into logical font families
 * that can be referenced in document styles.
 * </p>
 *
 * <h2>Core Architecture</h2>
 * <p>
 * The font system is built on a hierarchical structure with three main levels:
 * </p>
 * <ol>
 * <li>
 * <b>{@link de.fkkaiser.model.font.FontFamilyList}</b>:
 * The top-level container that holds all font families to be used in a PDF.
 * This is what gets passed to the PDF generation facade.
 * </li>
 * <li>
 * <b>{@link de.fkkaiser.model.font.FontFamily}</b>:
 * Represents a single font family (e.g., "Arial" or "Roboto") and contains
 * all its variants (regular, bold, italic, etc.).
 * </li>
 * <li>
 * <b>{@link de.fkkaiser.model.font.FontType}</b>:
 * Represents a specific font file with its associated style and weight.
 * Links a physical font file path to its rendering characteristics.
 * </li>
 * </ol>
 *
 * <h2>Supporting Types</h2>
 * <ul>
 * <li>
 * <b>{@link de.fkkaiser.model.font.FontStyleValue}</b>:
 * Enum defining the three standard font styles: NORMAL, ITALIC, and OBLIQUE.
 * </li>
 * <li>
 * <b>{@link de.fkkaiser.model.font.FontVariants}</b>:
 * Convenience enum that combines common style and weight combinations
 * (REGULAR, BOLD, ITALIC, BOLD_ITALIC, etc.) for easier font configuration.
 * </li>
 * </ul>
 *
 * <h2>Usage Patterns</h2>
 *
 * <h3>Pattern 1: Direct Construction</h3>
 * <p>Suitable for simple configurations or when building programmatically:</p>
 * <pre>{@code
 * // Create font types
 * FontType regular = new FontType("fonts/Arial-Regular.ttf", FontStyleValue.NORMAL, "400");
 * FontType bold = new FontType("fonts/Arial-Bold.ttf", FontStyleValue.NORMAL, "700");
 *
 * // Create font family
 * FontFamily arial = new FontFamily("Arial", List.of(regular, bold));
 *
 * // Create font list
 * FontFamilyList fonts = new FontFamilyList();
 * fonts.addFontFamily(arial);
 * }</pre>
 *
 * <h3>Pattern 2: Using FontVariants (Recommended)</h3>
 * <p>Simplifies font type creation by using predefined style/weight combinations:</p>
 * <pre>{@code
 * // Much cleaner syntax
 * FontFamily roboto = new FontFamily("Roboto", List.of(
 *     FontVariants.REGULAR.toFontType("fonts/Roboto-Regular.ttf"),
 *     FontVariants.BOLD.toFontType("fonts/Roboto-Bold.ttf"),
 *     FontVariants.ITALIC.toFontType("fonts/Roboto-Italic.ttf"),
 *     FontVariants.BOLD_ITALIC.toFontType("fonts/Roboto-BoldItalic.ttf")
 * ));
 * }</pre>
 *
 * <h3>Pattern 3: Factory Methods</h3>
 * <p>Quick creation for common font family configurations:</p>
 * <pre>{@code
 * // Single font (simplest)
 * FontFamily simple = FontFamily.withSingleFont("MyFont", "fonts/MyFont.ttf");
 *
 * // Standard four variants (most common)
 * FontFamily complete = FontFamily.withStandardVariants(
 *     "Arial",
 *     "fonts/Arial-Regular.ttf",
 *     "fonts/Arial-Bold.ttf",
 *     "fonts/Arial-Italic.ttf",
 *     "fonts/Arial-BoldItalic.ttf"
 * );
 *
 * // Just regular and bold
 * FontFamily basic = FontFamily.withBoldVariant(
 *     "Helvetica",
 *     "fonts/Helvetica-Regular.ttf",
 *     "fonts/Helvetica-Bold.ttf"
 * );
 * }</pre>
 *
 * <h3>Pattern 4: Builder API (Most Flexible)</h3>
 * <p>Fluent API for complex configurations with multiple font families:</p>
 * <pre>{@code
 * FontFamilyList fonts = FontFamilyList.builder()
 *     .addFontFamily("Roboto")
 *         .addFont("fonts/Roboto-Regular.ttf", FontVariants.REGULAR)
 *         .addFont("fonts/Roboto-Bold.ttf", FontVariants.BOLD)
 *         .addFont("fonts/Roboto-Italic.ttf", FontVariants.ITALIC)
 *         .endFontFamily()
 *     .addFontFamily("Open Sans")
 *         .addFont("fonts/OpenSans-Regular.ttf", FontVariants.REGULAR)
 *         .endFontFamily()
 *     .build();
 * }</pre>
 *
 * <h2>Font File Requirements</h2>
 * <p>
 * Font files must be:
 * </p>
 * <ul>
 *   <li>Available as resources in the classpath (typically in {@code src/main/resources/fonts/})</li>
 *   <li>Embedded in the JAR file when distributing the application</li>
 *   <li>In a supported format: TrueType (.ttf), OpenType (.otf), or Type 1 (.pfb)</li>
 *   <li>Licensed for embedding in PDFs (check font license agreements)</li>
 * </ul>
 *
 * <h2>Font Weights</h2>
 * <p>
 * Font weights follow the CSS specification and are specified as strings:
 * </p>
 * <table>
 *     <caption>CSS Font Weight Values</caption>
 *   <tr>
 *     <th>Weight</th>
 *     <th>Name</th>
 *     <th>Common Usage</th>
 *   </tr>
 *   <tr><td>100</td><td>Thin</td><td>Very light text</td></tr>
 *   <tr><td>200</td><td>Extra Light</td><td>Light text</td></tr>
 *   <tr><td>300</td><td>Light</td><td>Light body text</td></tr>
 *   <tr><td>400</td><td>Normal/Regular</td><td>Standard body text (default)</td></tr>
 *   <tr><td>500</td><td>Medium</td><td>Slightly emphasized text</td></tr>
 *   <tr><td>600</td><td>Semi Bold</td><td>Subheadings</td></tr>
 *   <tr><td>700</td><td>Bold</td><td>Headings, strong emphasis</td></tr>
 *   <tr><td>800</td><td>Extra Bold</td><td>Very strong emphasis</td></tr>
 *   <tr><td>900</td><td>Black/Heavy</td><td>Maximum emphasis</td></tr>
 * </table>
 * <p>
 * Keywords "normal" (400) and "bold" (700) are also supported.
 * </p>
 *
 * <h2>Integration with PDF Generation</h2>
 * <p>
 * The {@link de.fkkaiser.model.font.FontFamilyList} is passed to the
 * PdfGenerationFacade, which:
 * </p>
 * <ol>
 *   <li>Validates all font configurations</li>
 *   <li>Generates Apache FOP font configuration XML</li>
 *   <li>Registers fonts with the FOP engine</li>
 *   <li>Makes fonts available for use in document styles</li>
 * </ol>
 *
 * <h2>Best Practices</h2>
 * <ul>
 *   <li><b>Always include at least regular (400) and bold (700) variants</b> for professional documents</li>
 *   <li><b>Use {@link de.fkkaiser.model.font.FontVariants}</b> for standard configurations
 *       to avoid mistakes in style/weight combinations</li>
 *   <li><b>Store fonts in a dedicated directory</b> like {@code src/main/resources/fonts/}</li>
 *   <li><b>Use descriptive family names</b> that match your style sheet font-family references</li>
 *   <li><b>Validate font licenses</b> before embedding them in distributed applications</li>
 *   <li><b>Test all font variants</b> to ensure they render correctly in generated PDFs</li>
 * </ul>
 *
 * <h2>Thread Safety</h2>
 * <p>
 * The model classes ({@link de.fkkaiser.model.font.FontType} and
 * {@link de.fkkaiser.model.font.FontFamily}) are immutable records and therefore thread-safe.
 * The {@link de.fkkaiser.model.font.FontFamilyList} is mutable and not thread-safe;
 * it should be fully configured before being shared across threads.
 * </p>
 *
 * @since 1.0
 * @author Katrin Kaiser
 * @version 1.1
 */
package de.fkkaiser.model.font;