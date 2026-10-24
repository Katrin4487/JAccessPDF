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

/**
 * Enum representing the strictness mode for PDF generation.
 * STRICT: Enforces strict compliance with PDF/UA-1 standards. Violations will throw exceptions.
 * WARN: Logs warnings for violations but continues processing.
 * LENIENT: Ignores violations and continues processing without logging warnings.
 *
 * @author Katrin Kaiser
 * @version 1.0
 */
public enum StrictnessMode {
    /**
     * Enforces strict compliance with PDF/UA-1 standards. Violations will throw exceptions.
     */
    STRICT,
    /**
     * Logs warnings for violations but continues processing.
     */
    WARN,
    /**
     * Ignores violations and continues processing without logging warnings.
     */
    LENIENT;

}
