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
package de.fkkaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fkkaiser.model.structure.builder.DocumentBuilder;

/**
 * Contains internal directory paths for font and image resources used in PDF generation.
 *
 * <p>This record defines the locations of resource directories that contain fonts and images
 * referenced by the document. Unlike direct file paths, these paths point to directories
 * where the PDF generator can discover and load resources dynamically.</p>
 *
 * <p><b>Purpose and Design:</b></p>
 * InternalAddresses serves as a configuration bridge between the document structure and
 * the actual resource files. By specifying resource directories rather than individual
 * files, the system can:
 * <ul>
 *   <li>Discover all available resources in a location</li>
 *   <li>Support multiple font files in a single directory</li>
 *   <li>Enable centralized resource management</li>
 *   <li>Simplify resource organization and maintenance</li>
 * </ul>
 *
 * <p><b>Resource Discovery:</b></p>
 * The PDF generation system scans the specified directories to:
 * <ul>
 *   <li><b>Font Directory:</b> Locate and register all font files (.ttf, .otf, etc.)</li>
 *   <li><b>Image Directory:</b> Locate and register all image files (.png, .jpg, etc.)</li>
 * </ul>
 *
 * <p><b>Path Semantics:</b></p>
 * <table>
 *   <caption>Path Specifications</caption>
 *   <tr>
 *     <th>Field</th>
 *     <th>Points To</th>
 *     <th>Example</th>
 *     <th>Contains</th>
 *   </tr>
 *   <tr>
 *     <td>fontDictionary</td>
 *     <td>Directory path</td>
 *     <td>"fonts" or "resources/fonts"</td>
 *     <td>*.ttf, *.otf font files</td>
 *   </tr>
 *   <tr>
 *     <td>imageDictionary</td>
 *     <td>Directory path</td>
 *     <td>"images" or "resources/images"</td>
 *     <td>*.png, *.jpg, *.svg image files</td>
 *   </tr>
 * </table>
 *
 * <p><b>Null Values:</b></p>
 * Both fields are optional:
 * <ul>
 *   <li><b>fontDictionary = null:</b> Uses default system fonts only</li>
 *   <li><b>imageDictionary = null:</b> No images available in the document</li>
 *   <li><b>Both null:</b> Text-only document with system fonts (perfectly valid)</li>
 * </ul>
 *
 * <p><b>JSON Representation:</b></p>
 * <pre>{@code
 * {
 *   "internal-addresses": {
 *     "font-dictionary": "fonts",
 *     "image-dictionary": "images"
 *   }
 * }
 * }</pre>
 *
 * <p><b>Usage Example 1 - Both Resources:</b></p>
 * <pre>{@code
 * // Document with custom fonts and images
 * InternalAddresses addresses = new InternalAddresses(
 *     "fonts",              // Directory containing *.ttf, *.otf files
 *     "images"              // Directory containing *.png, *.jpg files
 * );
 *
 * Document doc = Document.builder(metadata)
 *     .withInternalAddresses(addresses)
 *     .addPageSequence(...)
 *     .build();
 * }</pre>
 *
 * <p><b>Usage Example 2 - Only Custom Fonts:</b></p>
 * <pre>{@code
 * // Document with custom fonts but no images
 * InternalAddresses addresses = new InternalAddresses(
 *     "resources/corporate-fonts",  // Custom font directory
 *     null                          // No images
 * );
 *
 * Document doc = Document.builder(metadata)
 *     .withInternalAddresses(addresses)
 *     .addPageSequence(contentPage)
 *     .build();
 * }</pre>
 *
 * <p><b>Usage Example 3 - Only Images:</b></p>
 * <pre>{@code
 * // Document with images but using default system fonts
 * InternalAddresses addresses = new InternalAddresses(
 *     null,                         // Use system fonts
 *     "resources/product-images"    // Custom image directory
 * );
 *
 * Document doc = Document.builder(metadata)
 *     .withInternalAddresses(addresses)
 *     .addPageSequence(catalogPage)
 *     .build();
 * }</pre>
 *
 * <p><b>Usage Example 4 - Nested Directory Structure:</b></p>
 * <pre>{@code
 * // Using nested resource directories
 * InternalAddresses addresses = new InternalAddresses(
 *     "src/main/resources/fonts",
 *     "src/main/resources/images"
 * );
 * }</pre>
 *
 * <p><b>Usage Example 5 - No External Resources:</b></p>
 * <pre>{@code
 * // Text-only document with system fonts
 * InternalAddresses addresses = new InternalAddresses(null, null);
 *
 * // Or simply don't set internal addresses:
 * Document doc = Document.builder(metadata)
 *     // No .withInternalAddresses() call
 *     .addPageSequence(textOnlyPage)
 *     .build();
 * }</pre>
 *
 * <p><b>Directory Structure Example:</b></p>
 * <pre>
 * project/
 * ├── fonts/                    ← fontDictionary: "fonts"
 * │   ├── Roboto-Regular.ttf
 * │   ├── Roboto-Bold.ttf
 * │   ├── Roboto-Italic.ttf
 * │   └── OpenSans-Regular.ttf
 * ├── images/                   ← imageDictionary: "images"
 * │   ├── logo.png
 * │   ├── banner.jpg
 * │   └── diagram.svg
 * └── document.json
 * </pre>
 *
 * <p><b>Path Resolution:</b></p>
 * Paths can be:
 * <ul>
 *   <li><b>Relative:</b> "fonts", "resources/fonts" (relative to application root)</li>
 *   <li><b>Absolute:</b> "/usr/share/fonts", "C:/Resources/Fonts" (full system path)</li>
 *   <li><b>Classpath:</b> Treated as resource paths within the application JAR</li>
 * </ul>
 *
 * The PDF generation system determines the appropriate resolution method based on the
 * path format and runtime environment.
 *
 * <p><b>Resource Discovery Process:</b></p>
 * When a directory is specified:
 * <ol>
 *   <li>PDF generator locates the directory</li>
 *   <li>Scans for files with supported extensions</li>
 *   <li>Registers each discovered resource by filename or ID</li>
 *   <li>Makes resources available for document elements to reference</li>
 * </ol>
 *
 * <p><b>Supported File Types:</b></p>
 * <ul>
 *   <li><b>Fonts:</b> .ttf (TrueType), .otf (OpenType), .pfb (Type 1)</li>
 *   <li><b>Images:</b> .png, .jpg, .jpeg, .gif, .svg</li>
 * </ul>
 *
 * <p><b>Integration with Document Elements:</b></p>
 * Once resources are registered via InternalAddresses, document elements can reference them:
 * <pre>{@code
 * // Fonts are referenced by family name in style definitions
 * TextBlockStyleProperties style = new TextBlockStyleProperties();
 * style.setFontFamily("Roboto");  // Must exist in font directory
 *
 * // Images are referenced by filename or ID
 * BlockImage image = new BlockImage("logo-style", "logo.png", "Company Logo");
 * }</pre>
 *
 * <p><b>Error Handling:</b></p>
 * Common issues and their handling:
 * <ul>
 *   <li><b>Directory not found:</b> PDF generator logs error, falls back to defaults</li>
 *   <li><b>Empty directory:</b> No resources registered, defaults are used</li>
 *   <li><b>Invalid file formats:</b> Unsupported files are ignored during discovery</li>
 *   <li><b>Missing referenced resource:</b> Error during PDF generation</li>
 * </ul>
 *
 * <p><b>Best Practices:</b></p>
 * <ul>
 *   <li>Use relative paths for portability across environments</li>
 *   <li>Keep resources organized in dedicated directories</li>
 *   <li>Use clear, descriptive directory names ("fonts", "images")</li>
 *   <li>Test resource discovery in development before deployment</li>
 *   <li>Include fallback fonts in case custom fonts fail to load</li>
 * </ul>
 *
 * <p><b>Performance Considerations:</b></p>
 * <ul>
 *   <li>Resource discovery happens once during PDF generator initialization</li>
 *   <li>Large directories may increase startup time</li>
 *   <li>Consider organizing resources into subdirectories if needed</li>
 *   <li>Unused resources in directories don't affect runtime performance</li>
 * </ul>
 *
 * <p><b>Deployment:</b></p>
 * When deploying applications:
 * <ul>
 *   <li>Ensure resource directories are included in the deployment package</li>
 *   <li>Verify directory paths are correct for the target environment</li>
 *   <li>Consider embedding resources in JAR for self-contained deployment</li>
 * </ul>
 *
 * <p><b>Immutability:</b></p>
 * As a record, InternalAddresses instances are immutable after construction. To use
 * different resource directories, create a new InternalAddresses instance.
 *
 * <p><b>Thread Safety:</b></p>
 * InternalAddresses instances are immutable and thread-safe. Multiple threads can
 * safely read from the same instance.
 *
 * @param fontDictionary  the path to the directory containing font files
 *                        (e.g., "fonts", "resources/fonts");
 *                        may be {@code null} to use default system fonts only
 * @param imageDictionary the path to the directory containing image files
 *                        (e.g., "images", "resources/images");
 *                        may be {@code null} if no images are used in the document
 *
 * @author FK Kaiser
 * @version 1.0
 * @see Document
 * @see DocumentBuilder
 */
public record InternalAddresses(
        @JsonProperty("font-dictionary")
        String fontDictionary,

        @JsonProperty("image-dictionary")
        String imageDictionary
) {
        // Record with no custom implementation - all functionality provided by record semantics
        // No validation needed as null values are valid and represent "no custom resources"
}