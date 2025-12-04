/*
 * Copyright 2025 Katrin Kaiser
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.fkkaiser.generator;

import de.fkkaiser.model.structure.Headline;
import de.fkkaiser.model.structure.InlineElement;
import de.fkkaiser.model.structure.TextRun;
import java.util.List;

/**
 * Generates the fo:bookmark-tree XML structure for a list of headlines.
 *
 * @author Katrin Kaiser
 * @version 1.1.0
 */
public class BookmarkGenerator {


    private static final String OPEN_BOOKMARK_TREE = "<fo:bookmark-tree>";
    private static final String CLOSE_BOOKMARK_TREE = "</fo:bookmark-tree>";
    private static final String OPEN_BOOKMARK = "<fo:bookmark";
    private static final String CLOSE_BOOKMARK = "</fo:bookmark>";
    private static final String INTERNAL_DESTINATION = "internal-destination";
    private static final String BOOKMARK_TITLE_TAG = "bookmark-title";

    /**
     * Generates the complete fo:bookmark-tree from a list of headlines.
     *
     * @param headlines The list of headlines.
     * @return The finished XML string for the bookmarks.
     */
    public String generateBookmarkTree(List<Headline> headlines) {
        if (headlines == null || headlines.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(OPEN_BOOKMARK_TREE);

        int lastLevel = 0;
        for (int i = 0; i < headlines.size(); i++) {
            Headline headline = headlines.get(i);
            int currentLevel = headline.getLevel();
            String id = headline.getId();
            String title = getTitleFrom(headline);

            if (i > 0) {
                if (currentLevel == lastLevel) {
                    // Sibling element, close the previous one.
                    builder.append(GenerateConst.SPACE.repeat(lastLevel + 2)).append(CLOSE_BOOKMARK);
                } else if(currentLevel < lastLevel) { // currentLevel < lastLevel
                    // Moving up, close all necessary levels.
                    int levelsToClose = lastLevel - currentLevel + 1;
                    for (int j = 0; j < levelsToClose; j++) {
                        builder.append(GenerateConst.SPACE.repeat(lastLevel - j + 2)).append(CLOSE_BOOKMARK);
                    }
                }
            }

            // Open the current bookmark.
            builder.append(GenerateConst.SPACE.repeat(currentLevel + 1)).append(getBookmarkOpener(id));
            builder.append(GenerateConst.SPACE.repeat(currentLevel + 2)).append(getBookmarkTitle(title));

            lastLevel = currentLevel;
        }

        // Close all remaining open tags after the loop.
        for (int level = lastLevel; level > 0; level--) {
            builder.append(GenerateConst.SPACE.repeat(level + 1)).append(CLOSE_BOOKMARK);
        }

        builder.append(CLOSE_BOOKMARK_TREE);
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
                if (textRun.getText() != null) {
                    titleBuilder.append(textRun.getText());
                }
            }
        }
        return titleBuilder.toString();
    }

    /**
     * Constructs the opening tag for a bookmark with the given ID.
     *
     * @param id The ID of the bookmark.
     * @return The opening tag string.
     */
    private String getBookmarkOpener(String id) {
        return OPEN_BOOKMARK +
                GenerateConst.SPACE +
                INTERNAL_DESTINATION +
                GenerateConst.EQUALS +
                GenerateConst.GQQ +
                GenerateUtils.escapeXml(id) +
                GenerateConst.GQQ +
                GenerateConst.CLOSER;
    }

    /**
     * Constructs the bookmark title XML element.
     *
     * @param title The title text.
     * @return The bookmark title XML string.
     */
    private String getBookmarkTitle(String title) {
        return GenerateUtils.tagBuilder(BOOKMARK_TITLE_TAG)
                .addContent(title)
                .build();
    }
}
