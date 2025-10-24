/**
 * Contains data models that define the logical structure and content of a document.
 * <p>
 * This package forms the core domain model for the content to be represented in a PDF file.
 * The classes here represent the hierarchical structure of a document, from the top level
 * down to individual text sections. They are designed to be directly deserialized from a
 * {@code structure.json} file using the Jackson library.
 *
 * <h2>Architecture and Hierarchy</h2>
 * The structure follows a clear hierarchy similar to a tree:
 * <ol>
 * <li>
 * <b>{@link de.fkkaiser.model.structure.Document}</b>:
 * The root object of the entire document. It contains the metadata and a list of page sequences.
 * </li>
 * <li>
 * <b>{@link de.fkkaiser.model.structure.PageSequence}</b>:
 * Defines a sequence of pages with a common layout (e.g. title pages, main body).
 * Each sequence has a header, a footer, and a body region.
 * </li>
 * <li>
 * <b>{@link de.fkkaiser.model.structure.ContentArea}</b>:
 * A simple container that holds a list of block-level elements for the header, body, or footer.
 * </li>
 * <li>
 * <b>{@link de.fkkaiser.model.structure.Element}</b>:
 * The central interface for all **block-level elements** such as paragraphs ({@link de.fkkaiser.model.structure.Paragraph}),
 * headers ({@link de.fkkaiser.model.structure.Headline}), lists ({@link de.fkkaiser.model.structure.SimpleList}), or
 * tables ({@link de.fkkaiser.model.structure.Table}).
 * </li>
 * <li>
 * <b>{@link de.fkkaiser.model.structure.InlineElement}</b>:
 * A specialized interface for all **inline elements** that can appear within a block element
 * (e.g. in a paragraph). Examples are simple text ({@link de.fkkaiser.model.structure.TextRun}),
 * links ({@link de.fkkaiser.model.structure.Hyperlink}), or footnotes ({@link de.fkkaiser.model.structure.Footnote}).
 * </li>
 * </ol>
 *
 * <h2>Polymorphism with Jackson</h2>
 * The system makes extensive use of Jackson's polymorphism mechanism. The {@code type} field in the JSON file
 * controls which concrete Java class (e.g., 'Paragraph' or 'List') is instantiated.
 * The mappings are defined in the `@JsonSubTypes` annotations of the interfaces {@code EElement} and
 * {@code EInlineElement}.
 *
 * @since 1.0
 * @author Katrin Kaiser
 */
package de.fkkaiser.model.structure;