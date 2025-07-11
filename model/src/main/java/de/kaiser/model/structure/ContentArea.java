package de.kaiser.model.structure;

import java.util.List;

/**
 * A container for a list of content elements (EElement).
 * Used to define the content of Header, Body, and Footer.
 */
public record ContentArea(
        List<Element> elements
) {
    /**
     * Initializes the EContentArea with a list of elements.
     *
     * @param elements The list of elements to be contained in the content area.
     */
    public ContentArea {
        if (elements == null) {
            elements = List.of();
        }
    }

    public ContentArea(){
        this(null);
    }

}