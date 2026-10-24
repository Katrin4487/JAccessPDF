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

import java.util.Objects;

/**
 * Configuration record for JAccessPDF. Contains the default language and strictness mode for PDF generation.
 *
 * @param defaultLanguage The default language to be used in the PDF if not specified in the document metadata.
 * @param defaultTitle The default title to be used in the PDF if not specified in the document metadata.
 * @param strictnessMode The strictness mode for PDF generation, which can be STRICT or WARN.
 */
public record JAccessConfig(String defaultLanguage, String defaultTitle, StrictnessMode strictnessMode) {

    /**
     * Rejects incomplete configurations at construction time.
     *
     * <p>Both fields are load-bearing in ways that fail quietly if left unset:
     * a {@code null} strictnessMode compares unequal to every enum constant, so
     * every check would silently behave as {@link StrictnessMode#LENIENT} - the
     * exact opposite of what someone configuring strictness intends. A
     * {@code null} or blank defaultLanguage ends up in the document's
     * {@code xml:lang}, breaking the PDF/UA conformance this configuration
     * exists to protect.</p>
     *
     * @throws NullPointerException if either argument is {@code null}
     * @throws IllegalArgumentException if defaultLanguage or defaultTitle is blank
     */
    public JAccessConfig {
        Objects.requireNonNull(defaultLanguage,
                "defaultLanguage must not be null - use JAccessConfig.defaultConfig() for the defaults");
        Objects.requireNonNull(strictnessMode,
                "strictnessMode must not be null - a null mode would silently behave like LENIENT");
        if (defaultLanguage.isBlank()) {
            throw new IllegalArgumentException(
                    "defaultLanguage must not be blank - it is written to the document's xml:lang");
        }
        defaultLanguage = defaultLanguage.trim();
        Objects.requireNonNull(defaultTitle,
                "defaultTitle must not be null - use JAccessConfig.defaultConfig() for the defaults");
        if (defaultTitle.isBlank()) {
            throw new IllegalArgumentException(
                    "defaultTitle must not be blank - it is written to the document's title");
        }
        defaultTitle = defaultTitle.trim();
    }

    /**
     * Returns a default configuration with English as the default language and WARN as the strictness mode.
     * @return A default {@link JAccessConfig} instance.
     */
    public static JAccessConfig defaultConfig() {
        return new JAccessConfig("en", "PDF Document", StrictnessMode.WARN);
    }

}
