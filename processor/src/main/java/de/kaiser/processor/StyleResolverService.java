package de.kaiser.processor;

import de.kaiser.model.structure.ContentArea;
import de.kaiser.model.structure.Document;
import de.kaiser.model.structure.PageSequence;
import de.kaiser.model.style.ElementStyle;
import de.kaiser.model.style.StyleResolverContext;
import de.kaiser.model.style.StyleSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The StyleResolverService is the logical heart of processing.
 * It orchestrates the recursive style resolution for all elements in a document.
 */
public class StyleResolverService {

    private static final Logger log = LoggerFactory.getLogger(StyleResolverService.class);

    /**
     * Resolves the styles for all elements in the given document using the provided style sheet.
     *
     * @param document   The document to resolve styles for.
     * @param styleSheet The style sheet containing element styles for resolution.
     */
    public void resolve(Document document, StyleSheet styleSheet) {
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
        }
    }

    /**
     * Sets resolved styles for all elements within a given page area by delegating
     * to each element's own resolveStyles method.
     *
     * @param area    The page area containing elements to process.
     * @param context The current style resolver context.
     */
    private void setResolvedStylesForElements(ContentArea area, StyleResolverContext context) {
        if (area != null && area.elements() != null) {
            area.elements().forEach(element -> element.resolveStyles(context));
        }
    }
}
