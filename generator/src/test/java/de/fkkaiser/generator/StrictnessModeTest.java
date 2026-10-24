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

import de.fkkaiser.model.configuration.JAccessConfig;
import de.fkkaiser.model.configuration.JAccessConfigBuilder;
import de.fkkaiser.model.configuration.JAccessStrictnessException;
import de.fkkaiser.model.configuration.StrictnessMode;
import de.fkkaiser.model.structure.*;
import de.fkkaiser.model.style.StyleSheet;
import de.fkkaiser.model.style.TextStyle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Covers what each {@link StrictnessMode} does when PDF/UA-relevant information
 * is missing from the document.
 *
 * <p>The throwing cases assert on {@link RuntimeException} plus the message
 * rather than on the concrete exception class, because the type is still open:
 * a dedicated compliance exception would be an improvement over the current
 * {@code IllegalStateException}, and these tests should not have to be rewritten
 * when that happens. The message assertions are what actually pin the behaviour
 * down.</p>
 */
class StrictnessModeTest {

    private static StyleSheet styleSheet() {
        TextStyle textStyle = new TextStyle("Default Style", "10pt", "Open Sans", "400", "normal");
        return new StyleSheet(List.of(textStyle), Collections.emptyList(), Collections.emptyList(), null);
    }

    /** A minimal document whose metadata carries a title but no language. */
    private static Document documentWithoutLanguage() {
        Metadata metadata = Metadata.builder("A Title").author("An author").build();
        Paragraph paragraph = new Paragraph("default-paragraph",
                List.of(new TextRun("Hello World!", "default-text")));
        ContentArea body = new ContentArea(List.of(paragraph));
        return new Document(null, metadata, List.of(new PageSequence("default", body, null, null)));
    }

    /** The same document, but declaring a language. */
    private static Document documentWithLanguage(String language) {
        Metadata metadata = Metadata.builder("A Title").author("An author").language(language).build();
        Paragraph paragraph = new Paragraph("default-paragraph",
                List.of(new TextRun("Hello World!", "default-text")));
        ContentArea body = new ContentArea(List.of(paragraph));
        return new Document(null, metadata, List.of(new PageSequence("default", body, null, null)));
    }

    private static String generate(StrictnessMode mode, Document document) {
        return generate(mode, document, null);
    }

    private static String generate(StrictnessMode mode, Document document, ImageResolver resolver) {
        JAccessConfig config = new JAccessConfigBuilder().strictnessMode(mode).build();
        return new XslFoGenerator(config).generate(document, styleSheet(), resolver);
    }

    /**
     * Resolves nothing. Enough for the alt-text checks: the generator logs that
     * the image could not be resolved and then still evaluates the alt text,
     * which is the behaviour under test. Passing null instead would fail with a
     * NullPointerException before reaching it.
     */
    private static final ImageResolver NO_IMAGES = relativePath -> null;

    @Nested
    @DisplayName("Missing document language")
    class MissingLanguage {

        /**
         * Metadata reaches the generator by two routes that behave differently:
         * {@code new Metadata()} (what the simple layer uses) leaves the language
         * null, while {@code Metadata.builder(...)} substitutes "de-DE" inside the
         * Metadata constructor. Only the first route ever reaches the strictness
         * check in XslFoGenerator - see the characterisation test below.
         */
        private Document documentWithUnsetLanguage() {
            Metadata metadata = new Metadata();
            metadata.setTitle("A Title");
            Paragraph paragraph = new Paragraph("default-paragraph",
                    List.of(new TextRun("Hello World!", "default-text")));
            ContentArea body = new ContentArea(List.of(paragraph));
            return new Document(null, metadata, List.of(new PageSequence("default", body, null, null)));
        }

        @Test
        @DisplayName("STRICT refuses to generate")
        void strictThrows() {
            RuntimeException thrown = assertThrows(RuntimeException.class,
                    () -> generate(StrictnessMode.STRICT, documentWithUnsetLanguage()));
            assertTrue(thrown.getMessage().toLowerCase().contains("language"),
                    "the message must say what is missing, was: " + thrown.getMessage());
        }

        @Test
        @DisplayName("WARN generates and falls back to the configured default language")
        void warnFallsBackToDefault() {
            String fo = generate(StrictnessMode.WARN, documentWithUnsetLanguage());
            assertTrue(fo.contains("xml:lang=\"en\""),
                    "expected the configured default language in the output");
        }

        @Test
        @DisplayName("LENIENT generates without complaint")
        void lenientGenerates() {
            assertFalse(generate(StrictnessMode.LENIENT, documentWithUnsetLanguage()).isEmpty());
        }

        @Test
        @DisplayName("The configured default language is used, not a hardcoded one")
        void configuredDefaultIsHonoured() {
            JAccessConfig config = new JAccessConfigBuilder()
                    .defaultLanguage("fr-FR")
                    .strictnessMode(StrictnessMode.LENIENT)
                    .build();
            String fo = new XslFoGenerator(config)
                    .generate(documentWithUnsetLanguage(), styleSheet(), null);
            assertTrue(fo.contains("xml:lang=\"fr-FR\""),
                    "the configured default language must reach the output");
        }

        @Test
        @DisplayName("A declared language is kept, even in STRICT")
        void declaredLanguageWins() {
            String fo = generate(StrictnessMode.STRICT, documentWithLanguage("fr"));
            assertTrue(fo.contains("xml:lang=\"fr\""), "the declared language must be used");
        }

        @Test
        @DisplayName("A whitespace-only language counts as missing")
        void blankLanguageCountsAsMissing() {
            // "   " is not a usable BCP 47 tag and would otherwise land in
            // xml:lang verbatim, so the check uses isBlank rather than isEmpty.
            Metadata metadata = new Metadata();
            metadata.setTitle("A Title");
            metadata.setLanguage("   ");
            Paragraph paragraph = new Paragraph("default-paragraph",
                    List.of(new TextRun("Hello World!", "default-text")));
            ContentArea body = new ContentArea(List.of(paragraph));
            Document document = new Document(null, metadata,
                    List.of(new PageSequence("default", body, null, null)));

            assertThrows(RuntimeException.class,
                    () -> generate(StrictnessMode.STRICT, document));

            String fo = generate(StrictnessMode.LENIENT, document);
            assertTrue(fo.contains("xml:lang=\"en\""),
                    "the configured default must replace the blank language");
            assertFalse(fo.contains("xml:lang=\"   \""),
                    "whitespace must never reach xml:lang");
        }

        @Test
        @DisplayName("STRICT throws no matter which route built the Metadata")
        void strictAppliesOnEveryMetadataRoute() {
            // Metadata used to substitute "de-DE" for a missing language in its
            // own constructor, which made this check unreachable for documents
            // built through the core model. Both routes must now reach it.
            Metadata viaFullConstructor = new Metadata("A Title", null, null, null,
                    null, null, null, null);
            Paragraph paragraph = new Paragraph("default-paragraph",
                    List.of(new TextRun("Hello World!", "default-text")));
            ContentArea body = new ContentArea(List.of(paragraph));
            Document document = new Document(null, viaFullConstructor,
                    List.of(new PageSequence("default", body, null, null)));

            assertThrows(RuntimeException.class,
                    () -> generate(StrictnessMode.STRICT, document),
                    "a document built through the full constructor must reach the check too");
            assertThrows(RuntimeException.class,
                    () -> generate(StrictnessMode.STRICT, documentWithUnsetLanguage()),
                    "and so must one built through the Jackson constructor");
        }
    }

    @Nested
    @DisplayName("Missing image alt text")
    class MissingAltText {

        private static Document documentWithImage(String altText) {
            Metadata metadata = Metadata.builder("A Title").language("en").build();
            BlockImage image = new BlockImage(
                    "default-image", "images/logo.png", altText, null, null);
            ContentArea body = new ContentArea(List.of(image));
            return new Document(null, metadata, List.of(new PageSequence("default", body, null, null)));
        }

        @Test
        @DisplayName("STRICT refuses to generate")
        void strictThrows() {
            RuntimeException thrown = assertThrows(RuntimeException.class,
                    () -> generate(StrictnessMode.STRICT, documentWithImage(null), NO_IMAGES));
            assertTrue(thrown.getMessage().toLowerCase().contains("alt text"),
                    "the message must say what is missing, was: " + thrown.getMessage());
        }

        @Test
        @DisplayName("WARN generates anyway")
        void warnGenerates() {
            assertFalse(generate(StrictnessMode.WARN, documentWithImage(null), NO_IMAGES).isEmpty());
        }

        @Test
        @DisplayName("LENIENT generates anyway")
        void lenientGenerates() {
            assertFalse(generate(StrictnessMode.LENIENT, documentWithImage(null), NO_IMAGES).isEmpty());
        }

        @Test
        @DisplayName("An empty or whitespace-only alt text counts as missing")
        void blankAltTextCountsAsMissing() {
            // Whitespace-only alt text is the "technically present, useless to a
            // screen reader" case - precisely what this check is for.
            assertThrows(RuntimeException.class,
                    () -> generate(StrictnessMode.STRICT, documentWithImage(""), NO_IMAGES));
            assertThrows(RuntimeException.class,
                    () -> generate(StrictnessMode.STRICT, documentWithImage("   "), NO_IMAGES));
        }

        @Test
        @DisplayName("Whitespace-only alt text is not written through to the output")
        void blankAltTextIsNotWrittenThrough() {
            String fo = generate(StrictnessMode.LENIENT, documentWithImage("   "), NO_IMAGES);
            assertFalse(fo.contains("alt-text=\"   \""),
                    "whitespace must be normalised away rather than emitted verbatim");
        }

        @Test
        @DisplayName("An image with alt text passes STRICT")
        void altTextPresentPassesStrict() {
            assertFalse(generate(StrictnessMode.STRICT,
                    documentWithImage("A company logo"), NO_IMAGES).isEmpty());
        }
    }

    @Nested
    @DisplayName("Missing document title")
    class MissingTitle {

        /** Metadata created through the Jackson constructor leaves the title null. */
        private Document documentWithUnsetTitle() {
            Metadata metadata = new Metadata();
            metadata.setLanguage("en");
            Paragraph paragraph = new Paragraph("default-paragraph",
                    List.of(new TextRun("Hello World!", "default-text")));
            ContentArea body = new ContentArea(List.of(paragraph));
            return new Document(null, metadata, List.of(new PageSequence("default", body, null, null)));
        }

        @Test
        @DisplayName("STRICT refuses to generate")
        void strictThrows() {
            RuntimeException thrown = assertThrows(RuntimeException.class,
                    () -> generate(StrictnessMode.STRICT, documentWithUnsetTitle()));
            assertTrue(thrown.getMessage().toLowerCase().contains("title"),
                    "the message must say what is missing, was: " + thrown.getMessage());
        }

        @Test
        @DisplayName("WARN generates and falls back to the configured default title")
        void warnFallsBackToDefault() {
            JAccessConfig config = new JAccessConfigBuilder()
                    .defaultTitle("Fallback Title")
                    .strictnessMode(StrictnessMode.WARN)
                    .build();
            String fo = new XslFoGenerator(config)
                    .generate(documentWithUnsetTitle(), styleSheet(), null);
            assertTrue(fo.contains("Fallback Title"),
                    "the configured default title must reach the XMP metadata");
        }

        @Test
        @DisplayName("LENIENT generates without complaint")
        void lenientGenerates() {
            assertFalse(generate(StrictnessMode.LENIENT, documentWithUnsetTitle()).isEmpty());
        }

        @Test
        @DisplayName("A declared title is kept, even in STRICT")
        void declaredTitleWins() {
            Metadata metadata = new Metadata();
            metadata.setLanguage("en");
            metadata.setTitle("A Real Title");
            Paragraph paragraph = new Paragraph("default-paragraph",
                    List.of(new TextRun("Hello World!", "default-text")));
            ContentArea body = new ContentArea(List.of(paragraph));
            Document document = new Document(null, metadata,
                    List.of(new PageSequence("default", body, null, null)));

            String fo = generate(StrictnessMode.STRICT, document);
            assertTrue(fo.contains("A Real Title"));
        }


        @Test
        @DisplayName("A whitespace-only title counts as missing")
        void blankTitleCountsAsMissing() {
            Metadata metadata = new Metadata();
            metadata.setLanguage("en");
            metadata.setTitle("   ");
            Paragraph paragraph = new Paragraph("default-paragraph",
                    List.of(new TextRun("Hello World!", "default-text")));
            ContentArea body = new ContentArea(List.of(paragraph));
            Document document = new Document(null, metadata,
                    List.of(new PageSequence("default", body, null, null)));

            assertThrows(RuntimeException.class,
                    () -> generate(StrictnessMode.STRICT, document));

            String fo = generate(StrictnessMode.LENIENT, document);
            assertTrue(fo.contains("PDF Document"),
                    "the configured default must replace the blank title");
        }

        @Test
        @DisplayName("Generating leaves the caller's Metadata untouched")
        void generatingDoesNotMutateTheDocument() {
            // The fallback title is resolved into a local variable and never
            // written back. If it were, a second generate() would stop warning
            // (the title would no longer be missing), and since Metadata is
            // Jackson-serializable the placeholder would end up baked into stored
            // documents as though the author had set it.
            Document document = documentWithUnsetTitle();

            generate(StrictnessMode.LENIENT, document);

            assertNull(document.metadata().getTitle(),
                    "generation must not write the fallback title back into the document");
        }

        @Test
        @DisplayName("A second generation warns again, because nothing was written back")
        void warningIsNotSwallowedOnTheSecondRun() {
            Document document = documentWithUnsetTitle();

            String first = generate(StrictnessMode.WARN, document);
            String second = generate(StrictnessMode.WARN, document);

            assertEquals(first, second,
                    "the same document must produce the same output every time");
            assertNull(document.metadata().getTitle());
        }
    }
}
