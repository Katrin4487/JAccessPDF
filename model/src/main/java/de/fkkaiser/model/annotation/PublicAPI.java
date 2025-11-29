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
package de.fkkaiser.model.annotation;

/**
 * Indicates that methods should be used in the public api.
 *
 * @author FK Kaiser
 * @version 1.0.0
 */
public @interface PublicAPI {

    /**
     * Optional description or note about this API element.
     *
     * @return a description, or empty string if not specified
     */
    String value() default "";

    /**
     * The stability status of this API element.
     *
     * @return the stability status (default: STABLE)
     */
    Status status() default Status.STABLE;

    /**
     * Stability status of API elements.
     */
    enum Status {
        /**
         * Stable API that will not change in a backward-incompatible way
         * within the same major version.
         */
        STABLE,

        /**
         * Experimental API that may change or be removed in future versions.
         * Use with caution in production code.
         */
        EXPERIMENTAL,

        /**
         * Deprecated API that will be removed in a future version.
         * Prefer using the suggested alternative.
         */
        DEPRECATED
    }
}
