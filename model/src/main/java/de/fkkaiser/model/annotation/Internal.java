package de.fkkaiser.model.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks APIs that are internal to JAccessPDF and should not be used
 * by library consumers, even if they are technically accessible.
 * <p>
 * Internal APIs may change or be removed without notice in any version.
 * </p>
 *
 * @author FK Kaiser
 * @version 1.0.0
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.PACKAGE})
public @interface Internal {
    /**
     * Optional explanation why this is internal or what to use instead.
     *
     * @return explanation or empty string
     */
    String value() default "";
}