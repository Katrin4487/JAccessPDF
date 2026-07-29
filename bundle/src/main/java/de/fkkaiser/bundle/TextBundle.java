package de.fkkaiser.bundle;

import java.util.Map;


/**
 * Represents a text bundle containing text content.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
public class TextBundle {

    Map<String,TextEntry> textContent;

    public TextBundle(Map<String,TextEntry> textContent) {
        this.textContent = textContent;
    }

    public Map<String, TextEntry> getTextContent() {
        return textContent;
    }

}
