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

import de.fkkaiser.model.annotation.PublicAPI;

/**
 * Builder class for creating instances of {@link JAccessConfig}.
 * This class provides a fluent API for setting configuration options.
 * It allows you to specify the default language and strictness mode for the configuration.
 *
 * @author Katrin Kaiser
 * @version 1.0
 */
public class JAccessConfigBuilder {

    // Pre-set to the same values as JAccessConfig.defaultConfig(), so that
    // setting only one option leaves the other at its documented default
    // instead of null. Without this, configuring just the language would turn
    // strictness off without saying so.
    private String defaultLanguage = "en";
    private StrictnessMode strictnessMode = StrictnessMode.WARN;
    private String defaultTitle = "PDF Document";

    /**
     * Sets the language written to the document when its metadata declares none.
     *
     * @param defaultLanguage language code in BCP 47 format, e.g. "de" or "de-DE";
     *                        must not be {@code null} or blank
     * @return this builder instance
     */
    @PublicAPI
    public JAccessConfigBuilder defaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
        return this;
    }

    /**
     * Sets the title written to the document when its metadata declares none.
     *
     * @param defaultTitle the title; must not be {@code null} or blank
     * @return this builder instance
     */
    @PublicAPI
    public JAccessConfigBuilder defaultTitle(String defaultTitle) {
        this.defaultTitle = defaultTitle;
        return this;
    }

    /**
     * Sets how missing PDF/UA-relevant information is handled during generation.
     *
     * @param strictnessMode the mode to use; must not be {@code null}
     * @return this builder instance
     */
    @PublicAPI
    public JAccessConfigBuilder strictnessMode(StrictnessMode strictnessMode) {
        this.strictnessMode = strictnessMode;
        return this;
    }

    /**
     * Builds the configuration.
     *
     * @return a validated {@link JAccessConfig}
     * @throws NullPointerException if an option was explicitly set to {@code null}
     * @throws IllegalArgumentException if defaultLanguage was set to a blank value
     */
    @PublicAPI
    public JAccessConfig build() {
        return new JAccessConfig(defaultLanguage, defaultTitle,strictnessMode);
    }
}

