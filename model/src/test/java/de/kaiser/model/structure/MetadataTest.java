package de.kaiser.model.structure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MetadataTest {

    private static final String PRODUCER = "de.kaiser.JAccessPDF v1.0";
    private static final String DEFAULT_TITLE = "PDF Dokument";
    private static final String DEFAULT_LANGUAGE = "de-DE";

    @Nested
    @DisplayName("Canonical Constructor Tests")
    class CanonicalConstructorTests {

        @Test
        @DisplayName("Should apply all default values when null is passed")
        void shouldApplyDefaultsWhenFieldsAreNull() {
            Metadata metadata = new Metadata(null, null, null, null, null, null, null, null);

            assertNotNull(metadata.creationDate());
            assertTrue(metadata.displayDocTitle());
            assertNotNull(metadata.keywords());
            assertTrue(metadata.keywords().isEmpty());
            assertEquals(DEFAULT_TITLE, metadata.title());
            assertEquals(DEFAULT_LANGUAGE, metadata.language());
            assertEquals(PRODUCER, metadata.producer());
        }

        @Test
        @DisplayName("Should keep explicitly provided values")
        void shouldKeepExplicitlyProvidedValues() {
            LocalDateTime specificDate = LocalDateTime.of(2025, 1, 1, 12, 0);
            List<String> specificKeywords = List.of("test", "junit");

            Metadata metadata = new Metadata(
                    "Explicit Title", "Explicit Author", "Explicit Subject",
                    specificKeywords, "en-US", "ignored-producer", specificDate, false
            );

            assertEquals("Explicit Title", metadata.title());
            assertEquals("en-US", metadata.language());
            assertEquals(specificDate, metadata.creationDate());
            assertFalse(metadata.displayDocTitle());
            assertEquals(specificKeywords, metadata.keywords());
            assertEquals(PRODUCER, metadata.producer(), "Producer should always be set to the constant");
        }
    }

    @Nested
    @DisplayName("Additional Constructor Tests")
    class AdditionalConstructorTests {

        @Test
        @DisplayName("Should correctly apply defaults for title-only constructor")
        void shouldWorkForTitleOnlyConstructor() {
            Metadata metadata = new Metadata("My Title");

            assertEquals("My Title", metadata.title());
            assertEquals(DEFAULT_LANGUAGE, metadata.language());
            assertTrue(metadata.displayDocTitle());
            assertNotNull(metadata.creationDate());
        }

        @Test
        @DisplayName("Should correctly apply defaults for title-and-language constructor")
        void shouldWorkForTitleAndLanguageConstructor() {

            Metadata metadata = new Metadata("My Title", "fr-FR");

            assertEquals("My Title", metadata.title());
            assertEquals("fr-FR", metadata.language());
            assertTrue(metadata.displayDocTitle());
        }
    }

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build with minimal properties and apply defaults")
        void shouldBuildWithMinimalProperties() {

            Metadata metadata = Metadata.builder("Builder Title").build();

            assertEquals("Builder Title", metadata.title());
            assertEquals(DEFAULT_LANGUAGE, metadata.language());
            assertTrue(metadata.displayDocTitle());
            assertNull(metadata.author());
        }

        @Test
        @DisplayName("Should build with all properties set via builder")
        void shouldBuildWithAllProperties() {
            LocalDateTime specificDate = LocalDateTime.of(2024, 5, 20, 10, 0);

            Metadata metadata = Metadata.builder("Fully Built")
                    .author("Builder Author")
                    .subject("Builder Subject")
                    .keywords(List.of("builder", "pattern"))
                    .language("es-ES")
                    .creationDate(specificDate)
                    .displayDocTitle(false)
                    .build();

            assertEquals("Fully Built", metadata.title());
            assertEquals("Builder Author", metadata.author());
            assertEquals("es-ES", metadata.language());
            assertEquals(specificDate, metadata.creationDate());
            assertFalse(metadata.displayDocTitle());
            assertEquals(2, metadata.keywords().size());
        }
    }
}