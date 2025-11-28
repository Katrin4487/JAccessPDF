package de.fkkaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.style.StyleResolverContext;

/**
 * Abstract base class providing common functionality for all {@link Element} implementations.
 *
 * <p>This class implements shared properties that are common to most document elements,
 * reducing code duplication in concrete element classes. It serves as the foundation
 * for block-level elements in the document structure.</p>
 *
 * <p><b>Common Properties:</b></p>
 * <ul>
 *   <li><b>styleClass:</b> A reference to an {@link de.fkkaiser.model.style.ElementStyle}
 *       that defines how the element should be rendered</li>
 *   <li><b>variant:</b> An optional semantic modifier (e.g., "warning", "highlight")
 *       that can alter the element's appearance or behavior</li>
 * </ul>
 *
 * <p><b>Usage Pattern:</b></p>
 * Concrete element classes (like {@link Paragraph}, {@link Headline}, {@link Section})
 * extend this class and add their specific properties and behavior while inheriting
 * the common style class and variant handling.
 *
 * <p><b>Style Resolution:</b></p>
 * Subclasses must implement the {@link #resolveStyles(StyleResolverContext)} method
 * to define how styles are computed and applied to the element. The resolved style
 * properties are typically stored in a subclass-specific field.
 *
 * <p><b>Design Note:</b></p>
 * The {@code resolvedStyle} field is intentionally left to concrete subclasses
 * because different element types require different style property classes
 * (e.g., {@link de.fkkaiser.model.style.TextBlockStyleProperties} for text blocks,
 * {@link de.fkkaiser.model.style.TableStyleProperties} for tables).
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 * @see Element
 * @see AbstractInlineElement
 * @see TextBlock
 */
@Internal
public abstract class AbstractElement implements Element {

    @JsonProperty("style-class")
    protected String styleClass;

    @JsonProperty("variant")
    protected String variant;

    /**
     * No-argument constructor required for Jackson deserialization.
     *
     * <p>Jackson uses this constructor to create an instance of the class
     * before populating its fields via reflection. This constructor should
     * not be used directly in application code.</p>
     */
    @PublicAPI
    public AbstractElement() {
    }

    /**
     * Creates a new AbstractElement with the specified style class and variant.
     *
     * @param styleClass the CSS-like style class name for styling;
     *                   may be {@code null} if no specific styling is needed
     * @param variant    the semantic variant identifier (e.g., "warning", "note");
     *                   may be {@code null} if no variant is needed
     */
    @PublicAPI
    public AbstractElement(String styleClass, String variant) {
        this.styleClass = styleClass;
        this.variant = variant;
    }

    /**
     * Returns the type identifier for this element.
     * This is used for JSON serialization and element type identification.
     *
     * <p>The type corresponds to the {@code type} field in the JSON structure
     * and must match one of the constants defined in {@link ElementTypes} or
     * {@link InlineElementTypes}.</p>
     *
     * @return the type identifier string (e.g., "paragraph", "headline")
     */
    @Override
    public abstract String getType();

    /**
     * Returns the style class name for this element.
     *
     * <p>The style class is used to look up the element's style properties
     * from the style map during style resolution.</p>
     *
     * @return the style class name, or {@code null} if no style class is set
     */
    @Override
    public String getStyleClass() {
        return this.styleClass;
    }

    /**
     * Returns the semantic variant identifier for this element.
     *
     * <p>Variants are optional modifiers that can alter the element's appearance
     * or behavior without requiring a separate style class. Common variants include
     * "warning", "note", "highlight", etc.</p>
     *
     * @return the variant identifier, or {@code null} if no variant is set
     */
    public String getVariant() {
        return this.variant;
    }

    /**
     * Resolves the styles for this element using the provided style resolver context.
     *
     * <p>This method must be implemented by all concrete subclasses to define how
     * styles are computed and applied. The implementation typically:</p>
     * <ol>
     *   <li>Retrieves the specific style from the style map using {@link #getStyleClass()}</li>
     *   <li>Merges it with the parent style from the context</li>
     *   <li>Stores the result in a subclass-specific resolved style field</li>
     *   <li>Recursively resolves styles for child elements</li>
     * </ol>
     *
     * @param context the style resolver context containing the style map and parent style;
     *                must not be {@code null}
     */
    @Override
    public abstract void resolveStyles(StyleResolverContext context);
}