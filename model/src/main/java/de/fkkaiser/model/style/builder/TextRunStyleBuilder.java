package de.fkkaiser.model.style.builder;

import de.fkkaiser.model.style.ElementStyle;
import de.fkkaiser.model.style.StyleTargetTypes;
import de.fkkaiser.model.style.TextRunStyleProperties;

public class TextRunStyleBuilder {

    private final String name;
    private final TextRunStyleProperties properties;

    public TextRunStyleBuilder(String name){
        this.name = name;
        this.properties = new TextRunStyleProperties();
    }

    /**
     * Sets the name of the text style to apply to this inline element.
     *
     * @param textStyleName name of the text style
     */
    @SuppressWarnings("unused")
    public TextRunStyleBuilder withTextStyleName(String textStyleName){
        properties.setTextStyleName(textStyleName);
        return this;
    }

    /**
     * Sets the text color for this inline element.
     *
     * @param textColor the color to apply (e.g., {@code "#FF0000"}, {@code "rgb(255,0,0)"});
     *
     */
    @SuppressWarnings("unused")
    public TextRunStyleBuilder withTextColor(String textColor){
        properties.setTextColor(textColor);
        return this;
    }

    /**
     * Sets the text decoration for this inline element.
     *
     * @param textDecoration the decoration to apply (e.g., {@code "underline"}, {@code "line-through"});
     *
     */
    @SuppressWarnings("unused")
    public TextRunStyleBuilder withTextDecoration(String textDecoration){
        properties.setTextDecoration(textDecoration);
        return this;
    }

    /**
     * Sets the background color for this inline element.
     *
     * @param backgroundColor the color to apply (e.g., {@code "#FF0000"}, {@code "rgb(255,0,0)"});
     *
     */
    @SuppressWarnings("unused")
    public TextRunStyleBuilder withBackgroundColor(String backgroundColor){
        properties.setBackgroundColor(backgroundColor);
        return this;
    }

    @SuppressWarnings("unused")
    public TextRunStyleBuilder withBaselineShift(String baselineShift){
        properties.setBaselineShift(baselineShift);
        return this;
    }

    public ElementStyle build(){
        return new ElementStyle(this.name, StyleTargetTypes.TEXT_RUN,this.properties);
    }
}
