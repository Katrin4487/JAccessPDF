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
     * Generates the complete fo:bookmark-tree from a list of headlines.
     * This version uses a more robust logic to handle hierarchy changes.
     * @param headlines The list of headlines.
     * @return The finished XML string for the bookmarks.
     */
    public String generateBookmarkTree(List<Headline> headlines) {
        if (headlines == null || headlines.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        builder.append("  <fo:bookmark-tree>\n");

        int lastLevel = 0;
        for (int i = 0; i < headlines.size(); i++) {
            Headline headline = headlines.get(i);
            int currentLevel = headline.getLevel();
            String id = "headline" + i;
            String title = getTitleFrom(headline);

            // Before opening a new bookmark, close the previous one(s) if necessary.
            if (i > 0) {
                if (currentLevel > lastLevel) {
                    // Going deeper, do nothing.
                } else if (currentLevel == lastLevel) {
                    // Sibling element, close the previous one.
                    builder.append("  ".repeat(lastLevel + 2)).append("</fo:bookmark>\n");
                } else { // currentLevel < lastLevel
                    // Moving up, close all necessary levels.
                    int levelsToClose = lastLevel - currentLevel + 1;
                    for (int j = 0; j < levelsToClose; j++) {
                        builder.append("  ".repeat(lastLevel - j + 2)).append("</fo:bookmark>\n");
                    }
                }
            }

            // Open the current bookmark.
            builder.append("  ".repeat(currentLevel + 1)).append("<fo:bookmark internal-destination=\"").append(escapeXml(id)).append("\">\n");
            builder.append("  ".repeat(currentLevel + 2)).append("<fo:bookmark-title>").append(escapeXml(title)).append("</fo:bookmark-title>\n");

            lastLevel = currentLevel;
        }

        // Close all remaining open tags after the loop.
        for (int level = lastLevel; level > 0; level--) {
            builder.append("  ".repeat(level + 1)).append("</fo:bookmark>\n");
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
            if(elem instanceof TextRun textRun){
                if (textRun.getText() != null) { // More robust check
                    titleBuilder.append(textRun.getText());
                }
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
