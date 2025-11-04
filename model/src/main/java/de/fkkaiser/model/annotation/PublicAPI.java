package de.fkkaiser.model.annotation;

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
