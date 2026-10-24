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
package de.fkkaiser.model.configuration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JAccessConfigTest {

    @Test
    @DisplayName("defaultConfig is English, a generic title and WARN")
    void defaultConfigValues() {
        JAccessConfig config = JAccessConfig.defaultConfig();
        assertEquals("en", config.defaultLanguage());
        assertEquals("PDF Document", config.defaultTitle());
        assertEquals(StrictnessMode.WARN, config.strictnessMode());
    }

    @Test
    @DisplayName("A null or blank default title is rejected")
    void invalidDefaultTitleIsRejected() {
        assertThrows(NullPointerException.class,
                () -> new JAccessConfig("en", null, StrictnessMode.WARN));
        assertThrows(IllegalArgumentException.class,
                () -> new JAccessConfig("en", "", StrictnessMode.WARN));
        assertThrows(IllegalArgumentException.class,
                () -> new JAccessConfig("en", "   ", StrictnessMode.WARN));
    }

    @Test
    @DisplayName("The default title is trimmed")
    void defaultTitleIsTrimmed() {
        assertEquals("Report",
                new JAccessConfig("en", "  Report  ", StrictnessMode.WARN).defaultTitle());
    }

    @Test
    @DisplayName("Setting only the title leaves the other options at their defaults")
    void settingOnlyTitleKeepsTheRest() {
        JAccessConfig config = new JAccessConfigBuilder().defaultTitle("Invoice").build();
        assertEquals("Invoice", config.defaultTitle());
        assertEquals("en", config.defaultLanguage());
        assertEquals(StrictnessMode.WARN, config.strictnessMode());
    }

    @Test
    @DisplayName("Explicitly setting a null title on the builder still fails at build time")
    void explicitNullTitleOnBuilderFails() {
        assertThrows(NullPointerException.class,
                () -> new JAccessConfigBuilder().defaultTitle(null).build());
    }

    @Test
    @DisplayName("A null strictness mode is rejected rather than behaving like LENIENT")
    void nullStrictnessModeIsRejected() {
        // A null mode compares unequal to every enum constant, so every check
        // would pass silently and strictness would be off without any sign of it.
        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> new JAccessConfig("en", "PDF Document", null));
        assertTrue(thrown.getMessage().contains("LENIENT"),
                "the message should explain why null is dangerous here");
    }

    @Test
    @DisplayName("A null or blank default language is rejected")
    void invalidDefaultLanguageIsRejected() {
        assertThrows(NullPointerException.class, () -> new JAccessConfig(null, "PDF Document", StrictnessMode.WARN));
        assertThrows(IllegalArgumentException.class, () -> new JAccessConfig("", "PDF Document", StrictnessMode.WARN));
        assertThrows(IllegalArgumentException.class, () -> new JAccessConfig("   ", "PDF Document", StrictnessMode.WARN));
    }

    @Test
    @DisplayName("The default language is trimmed")
    void defaultLanguageIsTrimmed() {
        assertEquals("de-DE", new JAccessConfig("  de-DE  ", "PDF Document", StrictnessMode.STRICT).defaultLanguage());
    }

    @Test
    @DisplayName("An empty builder produces the documented defaults")
    void emptyBuilderMatchesDefaultConfig() {
        assertEquals(JAccessConfig.defaultConfig(), new JAccessConfigBuilder().build());
    }

    @Test
    @DisplayName("Setting only the language leaves strictness at WARN")
    void settingOneOptionKeepsTheOtherDefault() {
        // The trap this guards against: configuring just the language used to
        // leave strictnessMode null, which turned every check off.
        JAccessConfig config = new JAccessConfigBuilder().defaultLanguage("de").build();
        assertEquals("de", config.defaultLanguage());
        assertEquals(StrictnessMode.WARN, config.strictnessMode());
    }

    @Test
    @DisplayName("Setting only strictness leaves the language at en")
    void settingOnlyStrictnessKeepsLanguage() {
        JAccessConfig config = new JAccessConfigBuilder().strictnessMode(StrictnessMode.STRICT).build();
        assertEquals("en", config.defaultLanguage());
        assertEquals(StrictnessMode.STRICT, config.strictnessMode());
    }

    @Test
    @DisplayName("Explicitly setting null on the builder still fails at build time")
    void explicitNullOnBuilderStillFails() {
        assertThrows(NullPointerException.class,
                () -> new JAccessConfigBuilder().strictnessMode(null).build());
        assertThrows(NullPointerException.class,
                () -> new JAccessConfigBuilder().defaultLanguage(null).build());
    }
}
