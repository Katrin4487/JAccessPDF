package de.fkkaiser.model.structure;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * An abstract base class for all InlineElement implementations.
 * <p>
 * It inherits common fields like {@code styleClass} and {@code variant} from
 * {@link AbstractElement} and implements the {@link InlineElement} interface,
 * providing a common foundation for all inline elements like {@link TextRun}.
 * </p>
 */
public abstract class AbstractInlineElement extends AbstractElement implements InlineElement {

    /**
     * The constructor for Jackson and subclasses.
     *
     * @param styleClass The style class to apply.
     * @param variant    The semantic variant of the element.
     */
    public AbstractInlineElement(
            @JsonProperty("style-class") String styleClass,
            @JsonProperty("variant") String variant
    ) {

        super(styleClass, variant);
    }

    @SuppressWarnings("unused")
    public AbstractInlineElement(String styleClass){
        this(styleClass, null);
    }

}
