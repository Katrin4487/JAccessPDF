/**
 * Contains classes responsible for reading and parsing external configuration files.
 * <p>
 * These classes act as the primary input layer for the application. They use the
 * Jackson library to deserialize JSON streams into the application's internal
 * data models, such as {@link de.fkkaiser.model.structure.Document} or
 * {@link de.fkkaiser.model.style.StyleSheet}. This package also defines the common
 * {@link de.fkkaiser.processor.reader.JsonReadException} for consistent error
 * handling during these operations.
 */
package de.fkkaiser.processor.reader;