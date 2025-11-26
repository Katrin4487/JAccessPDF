package de.fkkaiser.model.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the visibility of a type or member has been relaxed
 * to make the code testable.
 * <p>
 * Elements annotated with this should <strong>not</strong> be used outside
 * of test code, even if their visibility permits it.
 * </p>
 *
 * <p><strong>Example:</strong></p>
 * <pre>{@code
 * @VisibleForTesting
 * List<ContentElement> getElements() {
 *     return elements;
 * }
 * }</pre>
 *
 * @author FK Kaiser
 * @version 1.0.0
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD})
public @interface VisibleForTesting {
}