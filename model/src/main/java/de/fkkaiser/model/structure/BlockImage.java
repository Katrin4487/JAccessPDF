package de.fkkaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.fkkaiser.model.style.BlockImageStyleProperties;
import de.fkkaiser.model.style.ElementBlockStyleProperties;
import de.fkkaiser.model.style.ElementStyle;
import de.fkkaiser.model.style.StyleResolverContext;
import java.util.Optional;

/**
 * Represents a block-level image element in a PDF document.
 *
 * <p>BlockImage enables embedding images as standalone block elements with configurable
 * styles and accessibility features. Unlike inline images, block images occupy their own
 * space in the document flow and can be styled with margins, alignment, and sizing properties.</p>
 *
 * <p><b>Purpose in PDF Generation:</b></p>
 * Block images are rendered as separate blocks in the document, similar to how HTML treats
 * images within block-level containers. This allows for precise control over image placement,
 * sizing, and spacing within the document structure.
 *
 * <p><b>Image Path Resolution:</b></p>
 * The image path can be specified as either relative or absolute. During rendering, the path
 * is resolved to locate the actual image file that will be embedded in the PDF.
 *
 * <p><b>Accessibility:</b></p>
 * The altText property provides alternative text for accessibility compliance (PDF/UA).
 * This text is used by screen readers and ensures that visually impaired users can
 * understand the content and purpose of the image.
 *
 * <p><b>JSON Representation:</b></p>
 * <pre>{@code
 * {
 *   "type": "block-image",
 *   "style-class": "company-logo",
 *   "path": "images/logo.png",
 *   "alt-text": "Company logo showing a blue mountain"
 * }
 * }</pre>
 *
 * <p><b>Style Resolution:</b></p>
 * Block images support a comprehensive style system that merges element-specific styles
 * with inherited parent styles. The resolution process follows these steps:
 * <ol>
 *   <li>Look up element-specific styles from the style map using the styleClass</li>
 *   <li>Create a copy of the specific styles (if found) or use defaults</li>
 *   <li>Merge with parent block styles to inherit properties like margins or alignment</li>
 *   <li>Store the final resolved styles for rendering</li>
 * </ol>
 *
 * <p><b>Immutability Note:</b></p>
 * While the class itself is final and its fields are final, the resolvedStyle field
 * is mutable by design. This field is populated during the style resolution phase
 * and is not part of the element's initial construction or JSON deserialization.
 *
 * @author FK Kaiser
 * @version 1.0
 * @see BlockImageStyleProperties
 * @see Element
 * @see ElementTypes
 */
@JsonTypeName(ElementTypes.BLOCK_IMAGE)
public final class BlockImage implements Element {

    private final String styleClass;
    private final String path;
    private final String altText;

    @JsonIgnore
    private BlockImageStyleProperties resolvedStyle;

    /**
     * Creates a new BlockImage element.
     *
     * <p>This constructor is used by Jackson during JSON deserialization.
     * All parameters are optional in the JSON structure, though null values
     * may affect rendering or accessibility compliance.</p>
     *
     * @param styleClass the CSS-like style class for styling properties; may be {@code null}
     * @param path       the path to the image file (relative or absolute); may be {@code null}
     * @param altText    alternative text for accessibility (PDF/UA); may be {@code null}
     */
    @JsonCreator
    public BlockImage(
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("path") String path,
            @JsonProperty("alt-text") String altText
    ) {
        this.styleClass = styleClass;
        this.path = path;
        this.altText = altText;
    }

    // --- Getters ---

    /**
     * Returns the style class name used to look up styling properties.
     *
     * @return the style class name; may be {@code null}
     */
    @Override
    public String getStyleClass() {
        return styleClass;
    }

    /**
     * Returns the path to the image file.
     *
     * @return the image file path; may be {@code null}
     */
    public String getPath() {
        return path;
    }

    /**
     * Returns the alternative text for accessibility purposes.
     *
     * <p>This text should describe the content and purpose of the image
     * for users who cannot view it. It is used to ensure PDF/UA compliance.</p>
     *
     * @return the alternative text; may be {@code null}
     */
    public String getAltText() {
        return altText;
    }

    /**
     * Returns the resolved style properties after style resolution.
     *
     * <p>This value is populated during the {@link #resolveStyles(StyleResolverContext)}
     * call and should not be accessed before style resolution has been performed.</p>
     *
     * @return the resolved style properties; may be {@code null} before resolution
     */
    public BlockImageStyleProperties getResolvedStyle() {
        return resolvedStyle;
    }

    /**
     * Returns the element type identifier.
     *
     * @return the constant {@link ElementTypes#BLOCK_IMAGE}
     */
    @Override
    public String getType() {
        return ElementTypes.BLOCK_IMAGE;
    }

    /**
     * Resolves the style properties of this element by merging specific
     * styles with parent block styles.
     *
     * <p>The resolution process:
     * <ol>
     *   <li>Retrieves the parent block's style properties from the context</li>
     *   <li>Looks up element-specific styles using the styleClass from the style map</li>
     *   <li>Filters to ensure the style is of type {@link BlockImageStyleProperties}</li>
     *   <li>Creates a copy of the specific styles (or defaults if not found)</li>
     *   <li>Merges the parent styles into the copy, with parent values filling in gaps</li>
     *   <li>Stores the final merged result in {@link #resolvedStyle}</li>
     * </ol>
     * </p>
     *
     * <p>This method modifies the internal state of the object by setting the
     * resolvedStyle field. It should be called once during document preparation
     * before rendering begins.</p>
     *
     * @param context the context containing the style map and parent styles;
     *                must not be {@code null}
     */
    @Override
    public void resolveStyles(StyleResolverContext context) {
        ElementBlockStyleProperties parentStyle = context.parentBlockStyle();

        BlockImageStyleProperties specificStyle = Optional.ofNullable(context.styleMap().get(this.getStyleClass()))
                .map(ElementStyle::properties)
                .filter(BlockImageStyleProperties.class::isInstance)
                .map(BlockImageStyleProperties.class::cast)
                .orElse(new BlockImageStyleProperties());

        BlockImageStyleProperties finalStyle = specificStyle.copy();
        finalStyle.mergeWith(parentStyle);

        this.resolvedStyle = finalStyle;
    }
}