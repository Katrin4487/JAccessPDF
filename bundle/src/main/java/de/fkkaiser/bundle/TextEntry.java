package de.fkkaiser.bundle;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a text entry in a bundle.
 * This class encapsulates a rich text string and an optional description.
 * Placeholder syntax is supported to allow for dynamic text replacement.
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 */
public class TextEntry {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\{(.*?)\\}\\}");

    private final String richText;
    private String description;


    @JsonCreator
    public TextEntry(
            @JsonProperty("rich-text") String richText,
            @JsonProperty("description") String description) {
        this.richText = richText;
        this.description = description;
    }
    public TextEntry(String richText) {
        this(richText,null);
    }

    public String getRichText() {
        return richText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String descrption) {
        this.description = descrption;
    }

    public boolean hasPlaceholders() {
        return PLACEHOLDER_PATTERN.matcher(this.richText).find();
    }


    public List<String> extractPlaceholders() {
        List<String> placeholders = new ArrayList<>();
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(this.richText);

        while (matcher.find()) {
            placeholders.add(matcher.group(1));
        }

        return placeholders;
    }
}
