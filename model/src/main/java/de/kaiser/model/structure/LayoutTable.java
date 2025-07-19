package de.kaiser.model.structure;

import de.kaiser.model.style.StyleResolverContext;

public class LayoutTable implements Element{

    private Element elementLeft;
    private Element elementRight;


    @Override
    public String getType() {
        return ElementTypes.LAYOUT_TABLE;
    }

    @Override
    public String getStyleClass() {
        return "";
    }

    /**
     * Resolves styles for the given element using the provided style resolver context.
     *
     * @param context The style resolver context containing style map and default text style properties
     */
    @Override
    public void resolveStyles(StyleResolverContext context) {

    }
}
