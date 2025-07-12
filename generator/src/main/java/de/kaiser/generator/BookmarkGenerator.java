package de.kaiser.generator;

import de.kaiser.model.structure.Headline;
import de.kaiser.model.structure.InlineElement;
import de.kaiser.model.structure.TextRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BookmarkGenerator {

    private static final Logger log = LoggerFactory.getLogger(BookmarkGenerator.class);

    /**
     * Generates the complete <fo:bookmark-tree> from a list of headlines.
     * @param headlines The list of headlines.
     * @return The finished XML string for the bookmarks.
     */
    public String generateBookmarkTree(List<Headline> headlines) {
        if (headlines == null || headlines.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        builder.append("  <fo:bookmark-tree>\n");

        int currentLevel = 0;

        for (int i = 0; i < headlines.size(); i++) {
            Headline headline = headlines.get(i);
            int newLevel = headline.getLevel();
            String id = "headline" + i;
            String title = getTitleFrom(headline);

            // Close tags if moving up in the hierarchy
            while (newLevel < currentLevel) {
                builder.append("  ".repeat(currentLevel + 1)).append("</fo:bookmark>\n");
                currentLevel--;
            }

            // Open the new bookmark for the current headline
            builder.append("  ".repeat(newLevel + 1)).append("<fo:bookmark internal-destination=\"").append(escapeXml(id)).append("\">\n");
            builder.append("  ".repeat(newLevel + 2)).append("<fo:bookmark-title>").append(escapeXml(title)).append("</fo:bookmark-title>\n");

            currentLevel = newLevel;
        }

        // Close any remaining open tags after the loop
        while (currentLevel > 0) {
            builder.append("  ".repeat(currentLevel + 1)).append("</fo:bookmark>\n");
            currentLevel--;
        }

        builder.append("  </fo:bookmark-tree>\n");
        return builder.toString();
    }

    /**
     * Extracts the pure text content from a headline by concatenating all its text-based inline elements.
     * @param headline The headline to extract text from.
     * @return The concatenated text content.
     */
    private String getTitleFrom(Headline headline) {
        if (headline.getInlineElements() == null) {
            return "";
        }
        StringBuilder titleBuilder = new StringBuilder();
        for (InlineElement elem : headline.getInlineElements()) {
            if (elem instanceof TextRun textRun) {
                titleBuilder.append((textRun.getText()));
            }
        }
        return titleBuilder.toString();
    }

    private String escapeXml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
