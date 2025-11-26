package de.fkkaiser.api;

/**
 * Custom exception thrown when an error occurs during PDF generation.
 * This exception wraps lower-level exceptions (such as IOException, TransformerException,
 * or FOP-related exceptions) and provides a unified error handling mechanism for the
 * PDF generation process.
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * try {
 *     ByteArrayOutputStream pdf = facade.generatePDF(doc, styleSheet, fontList);
 * } catch (PdfGenerationException e) {
 *     log.error("Failed to generate PDF: {}", e.getMessage(), e);
 *     // Handle error appropriately
 * }
 * }</pre>
 *
 * @author Katrin Kaiser
 * @version 1.0.0
 * @see PdfGenerationFacade
 */
public class PdfGenerationException extends Exception {

    /**
     * Constructs a new PdfGenerationException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    @SuppressWarnings("unused")
    public PdfGenerationException(String message) {
        super(message);
    }

    /**
     * Constructs a new PdfGenerationException with the specified detail message and cause.
     * This constructor is typically used to wrap lower-level exceptions that occurred
     * during PDF generation.
     *
     * @param message the detail message explaining the reason for the exception
     * @param cause   the underlying cause of the exception (may be {@code null})
     */
    public PdfGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new PdfGenerationException with the specified cause.
     * The detail message is automatically derived from the cause's message.
     *
     * @param cause the underlying cause of the exception
     */
    @SuppressWarnings("unused")
    public PdfGenerationException(Throwable cause) {
        super(cause);
    }
}