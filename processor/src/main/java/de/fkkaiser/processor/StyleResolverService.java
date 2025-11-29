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
package de.fkkaiser.processor;

import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.structure.ContentArea;
import de.fkkaiser.model.structure.Document;
import de.fkkaiser.model.structure.PageSequence;
import de.fkkaiser.model.style.ElementStyle;
import de.fkkaiser.model.style.StyleResolverContext;
import de.fkkaiser.model.style.StyleSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The StyleResolverService is the logical heart of processing.
 * It orchestrates the recursive style resolution for all elements in a document.
 *
 * @author Katrin Kaiser
 * @version 1.1.0
 */
@Internal
public final class StyleResolverService {

    private static final Logger log = LoggerFactory.getLogger(StyleResolverService.class);

    /**
     * Resolves the styles for all elements in the given document using the provided style sheet.
     *
     * @param document   The document to resolve styles for.
     * @param styleSheet The style sheet containing element styles for resolution.
     */
    public static void resolve(Document document, StyleSheet styleSheet) {
        if (document == null || styleSheet == null || styleSheet.elementStyles() == null) {
            log.warn("Document or StyleSheet is null or empty, aborting style resolution.");
            return;
        }

        Map<String, ElementStyle> styleMap = styleSheet.elementStyles().stream()
                .collect(Collectors.toMap(ElementStyle::name, Function.identity()));

        // Create the initial context. Initial parent style is null
        StyleResolverContext initialContext = new StyleResolverContext(styleMap, null);

        // Start the recursive process for all elements at the top level.
        if (document.pageSequences() != null) {
            for (PageSequence sequence : document.pageSequences()) {
                // The top-level elements in header, footer, and body will receive
                // a context with a null parent style. Their 'resolveStyles' methods
                // must be able to handle this gracefully.
                setResolvedStylesForElements(sequence.header(), initialContext);
                setResolvedStylesForElements(sequence.body(), initialContext);
                setResolvedStylesForElements(sequence.footer(), initialContext);
            }
        }else {
            log.warn("Document contains no page sequences, nothing to resolve.");
        }
    }

    /**
     * Sets resolved styles for all elements within a given page area by delegating
     * to each element's own resolveStyles method. The provided context is used and passed down.
     * So that the method works recursively through all child elements.
     *
     * @param area    The page area containing elements to process.
     * @param context The current style resolver context.
     */
    private static void setResolvedStylesForElements(ContentArea area, StyleResolverContext context) {
        if (area != null && area.elements() != null) {
            area.elements().forEach(element -> element.resolveStyles(context));
        }else  {
            log.info("Element area is null or empty, aborting style resolution.");
        }
    }
}
