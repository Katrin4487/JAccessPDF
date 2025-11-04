package de.fkkaiser.model.structure;

import java.util.ArrayList;
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
            elements = new ArrayList<>(); // should be mutable
        }
    }

    /**
     * Default constructor that initializes the ContentArea with no elements.
     * If no explicit list of elements is provided, an empty, mutable list is used internally.
     */
    public ContentArea(){
        this(null);
    }

    /**
     * Adds a new element to the content area.
     *
     * @param element The element to be added. Must not be null.
     *                The provided element represents a unit of content, such as a paragraph, headline, or other defined types.
     */
    @SuppressWarnings("unused")
    public void addElement(Element element){
        elements.add(element);
    }

}