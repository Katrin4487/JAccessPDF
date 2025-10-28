package de.fkkaiser.api;

import de.fkkaiser.api.utils.EFopResourceResolver;
import de.fkkaiser.api.utils.EFopURIResolver;
import de.fkkaiser.api.utils.EResourceProvider;
import de.fkkaiser.generator.ImageResolver;
import de.fkkaiser.generator.XslFoGenerator;
import de.fkkaiser.model.font.FontFamily;
import de.fkkaiser.model.font.FontFamilyList;
import de.fkkaiser.model.font.FontStyleValue;
import de.fkkaiser.model.font.FontType;
import de.fkkaiser.model.structure.Document;
import de.fkkaiser.model.style.StyleSheet;
import de.fkkaiser.processor.StyleResolverService;
import de.fkkaiser.processor.reader.DocumentReader;
import de.fkkaiser.processor.reader.FontFamilyListReader;
import de.fkkaiser.processor.reader.StyleSheetReader;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;
import org.apache.fop.apps.MimeConstants;
import org.apache.fop.configuration.Configuration;
import org.apache.fop.configuration.DefaultConfigurationBuilder;
import org.apache.xmlgraphics.io.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The central facade for PDF generation from structured document models.
 * This class orchestrates the entire PDF generation process, from reading input data
 * to producing the final PDF output, and provides a simplified API for clients.
 *
 * <p><b>Design Philosophy:</b></p>
 * This facade follows the Facade design pattern to hide the complexity of the underlying
 * PDF generation pipeline, which involves:
 * <ul>
 *   <li>Parsing JSON input or accepting Java model objects</li>
 *   <li>Resolving styles and linking them to document elements</li>
 *   <li>Managing font configurations and registrations</li>
 *   <li>Generating XSL-FO (Formatting Objects) from the document model</li>
 *   <li>Configuring and running Apache FOP to produce PDF output</li>
 * </ul>
 *
 * <p><b>Stateless Design:</b></p>
 * This facade is designed to be stateless and thread-safe. Each method call is independent,
 * and instances can be safely reused across multiple PDF generation requests. All necessary
 * state is passed as method parameters.
 *
 * <p><b>Two API Entry Points:</b></p>
 * The facade provides two complementary APIs to accommodate different use cases:
 * <ol>
 *   <li><b>Stream-based API:</b> {@link #generatePDF(InputStream, InputStream, InputStream)}
 *       - Ideal for web services, REST APIs, or data-driven applications
 *       - Accepts JSON input streams for structure, styles, and fonts
 *   </li>
 *   <li><b>Object-based API:</b> {@link #generatePDF(Document, StyleSheet, FontFamilyList)}
 *       - Ideal for programmatic PDF creation within Java applications
 *       - Accepts pre-built model objects
 *   </li>
 * </ol>
 *
 * <p><b>Usage Example (Stream-based):</b></p>
 * <pre>{@code
 * EResourceProvider provider = new EClasspathResourceProvider();
 * PdfGenerationFacade facade = new PdfGenerationFacade(provider);
 *
 * try (InputStream structure = new FileInputStream("document.json");
 *      InputStream styles = new FileInputStream("styles.json");
 *      InputStream fonts = new FileInputStream("fonts.json")) {
 *
 *     ByteArrayOutputStream pdf = facade.generatePDF(structure, styles, fonts);
 *     Files.write(Paths.get("output.pdf"), pdf.toByteArray());
 * } catch (PdfGenerationException e) {
 *     log.error("PDF generation failed", e);
 * }
 * }</pre>
 *
 * <p><b>Usage Example (Object-based):</b></p>
 * <pre>{@code
 * Document document = new Document();
 * // ... build document structure
 *
 * StyleSheet styleSheet = new StyleSheet();
 * // ... configure styles
 *
 * FontFamilyList fonts = new FontFamilyList();
 * // ... add fonts
 *
 * try {
 *     ByteArrayOutputStream pdf = facade.generatePDF(document, styleSheet, fonts);
 *     // ... use PDF
 * } catch (PdfGenerationException e) {
 *     log.error("PDF generation failed", e);
 * }
 * }</pre>
 *
 * <p><b>Default Font Fallback:</b></p>
 * If no valid fonts are provided (null, empty, or all fonts are invalid), the facade
 * automatically adds a default font (Open Sans Regular) to ensure PDF generation can proceed.
 *
 * <p><b>Thread Safety:</b></p>
 * This class is thread-safe. Multiple threads can safely share a single instance and
 * call its methods concurrently.
 *
 * @author FK Kaiser
 * @version 1.1
 * @see Document
 * @see StyleSheet
 * @see FontFamilyList
 * @see EResourceProvider
 * @see PdfGenerationException
 */
public final class PdfGenerationFacade {

    private static final Logger log = LoggerFactory.getLogger(PdfGenerationFacade.class);

    // Default font configuration
    private static final String DEFAULT_FONT_PATH = "fonts/OpenSans-Regular.ttf";
    private static final String DEFAULT_FONT_NAME = "Open Sans";
    private static final String DEFAULT_FONT_WEIGHT = "400";
    private static final FontStyleValue DEFAULT_FONT_STYLE = FontStyleValue.NORMAL;

    // FOP configuration constants
    private static final String FOP_VERSION = "1.0";
    private static final boolean FOP_STRICT_VALIDATION = false;
    private static final boolean FOP_ACCESSIBILITY_ENABLED = true;
    private static final String FOP_PDF_UA_MODE = "PDF/UA-1";
    private static final String FOP_MIME_TYPE = MimeConstants.MIME_PDF;

    // FOP configuration XML template
    private static final String FOP_CONFIG_TEMPLATE =
            """
                    <?xml version="1.0"?>
                    <fop version="%s">
                      <strict-validation>%s</strict-validation>
                      <accessibility>%s</accessibility>
                      <renderers>
                        <renderer mime="application/pdf">
                          <pdf-ua-mode>%s</pdf-ua-mode>
                    %s\
                        </renderer>
                      </renderers>
                    </fop>""";

    // Service dependencies (final for immutability and thread-safety)
    private final DocumentReader documentReader;
    private final StyleSheetReader styleSheetReader;
    private final FontFamilyListReader fontListReader;
    private final XslFoGenerator foGenerator;
    private final EResourceProvider resourceProvider;

    /**
     * Constructs a new PdfGenerationFacade with the specified resource provider.
     * The resource provider is used throughout the PDF generation process to locate
     * and load external resources such as fonts, images, stylesheets, and other assets.
     *
     * <p>This constructor initializes all necessary service components (readers, generators)
     * that will be used for PDF generation. These components are reusable across multiple
     * PDF generation requests.</p>
     *
     * @param provider the resource provider for resolving external resources;
     *                 must not be {@code null}
     * @throws IllegalArgumentException if provider is {@code null}
     */
    public PdfGenerationFacade(EResourceProvider provider) {
        if (provider == null) {
            log.error("Attempted to create PdfGenerationFacade with null resource provider");
            throw new IllegalArgumentException("EResourceProvider cannot be null");
        }

        this.resourceProvider = provider;
        this.documentReader = new DocumentReader();
        this.styleSheetReader = new StyleSheetReader();
        this.fontListReader = new FontFamilyListReader();
        this.foGenerator = new XslFoGenerator();

        log.debug("PdfGenerationFacade initialized successfully");
    }

    /**
     * Generates a PDF from JSON input streams.
     * This method is ideal for web services, REST APIs, or any scenario where the document
     * structure, styles, and fonts are provided as JSON data streams.
     *
     * <p><b>Process:</b></p>
     * <ol>
     *   <li>Parses the JSON streams into model objects (Document, StyleSheet, FontFamilyList)</li>
     *   <li>Delegates to {@link #generatePDF(Document, StyleSheet, FontFamilyList)} for PDF generation</li>
     * </ol>
     *
     * <p><b>Input Format:</b></p>
     * All three input streams should contain valid JSON data conforming to the respective
     * model schemas. See the documentation for {@link Document}, {@link StyleSheet}, and
     * {@link FontFamilyList} for details on the expected JSON structure.
     *
     * @param structureJson an InputStream containing the document structure as JSON;
     *                      must not be {@code null}
     * @param styleJson     an InputStream containing the style definitions as JSON;
     *                      must not be {@code null}
     * @param fontListJson  an InputStream containing the font configurations as JSON;
     *                      must not be {@code null}
     * @return a ByteArrayOutputStream containing the generated PDF data
     * @throws PdfGenerationException if an error occurs during JSON parsing or PDF generation
     */
    public ByteArrayOutputStream generatePDF(InputStream structureJson,
                                             InputStream styleJson,
                                             InputStream fontListJson) throws PdfGenerationException {
        log.debug("Starting PDF generation from JSON input streams");

        try {
            Document doc = documentReader.readJson(structureJson);
            StyleSheet styleSheet = styleSheetReader.readJson(styleJson);
            FontFamilyList fontFamilyList = fontListReader.readJson(fontListJson);

            log.debug("Successfully parsed JSON input streams");

            // Delegate to the object-based method
            return generatePDF(doc, styleSheet, fontFamilyList);

        } catch (Exception e) {
            log.error("Failed to generate PDF from JSON input streams", e);
            throw new PdfGenerationException("Failed to parse JSON input or generate PDF", e);
        }
    }

    /**
     * Generates a PDF from pre-built Java model objects.
     * This method is ideal for programmatic PDF creation where the document structure,
     * styles, and fonts are constructed directly in Java code.
     *
     * <p><b>Process Overview:</b></p>
     * <ol>
     *   <li>Resolves and links styles to document elements</li>
     *   <li>Validates and prepares the font family list (adds default font if necessary)</li>
     *   <li>Creates the Apache FOP factory with font configuration</li>
     *   <li>Generates XSL-FO (Formatting Objects) from the document model</li>
     *   <li>Transforms XSL-FO to PDF using Apache FOP</li>
     * </ol>
     *
     * <p><b>Font Handling:</b></p>
     * If the provided {@code fontFamilyList} is null, empty, or contains only invalid fonts,
     * a default font (Open Sans Regular) is automatically added to ensure the PDF can be
     * generated successfully. A warning is logged when this occurs.
     *
     * <p><b>Style Resolution:</b></p>
     * The method automatically resolves style references in the document, linking style
     * definitions from the style sheet to the appropriate document elements.
     *
     * @param document       the document structure to be rendered as PDF;
     *                       must not be {@code null}
     * @param styleSheet     the style definitions to be applied to the document;
     *                       must not be {@code null}
     * @param fontFamilyList the list of font families to be used in the PDF;
     *                       may be {@code null} (default font will be used)
     * @return a ByteArrayOutputStream containing the generated PDF data
     * @throws PdfGenerationException if an error occurs during PDF generation
     */
    public ByteArrayOutputStream generatePDF(Document document,
                                             StyleSheet styleSheet,
                                             FontFamilyList fontFamilyList) throws PdfGenerationException {
        log.debug("Starting PDF generation from model objects");

        try {
            // Step 1: Resolve styles and link them to document elements
            StyleResolverService.resolve(document, styleSheet);
            log.debug("Style resolution completed");

            // Step 2: Ensure we have a valid font list (with fallback to default)
            FontFamilyList validatedFonts = ensureValidFontList(fontFamilyList);

            // Step 3: Create FOP factory with font configuration
            FopFactory fopFactory = createFopFactory(validatedFonts);
            log.debug("FOP factory created successfully");

            // Step 4: Generate XSL-FO from document model
            String xslFoString = generateXslFo(document, styleSheet);
            log.debug("XSL-FO generation completed, length: {} characters", xslFoString.length());

            // Step 5: Transform XSL-FO to PDF
            ByteArrayOutputStream pdfOutput = transformToPdf(fopFactory, xslFoString);
            log.debug("PDF generation completed successfully, size: {} bytes", pdfOutput.size());

            return pdfOutput;

        } catch (Exception e) {
            log.error("Failed to generate PDF from model objects", e);
            throw new PdfGenerationException("PDF generation failed", e);
        }
    }

    /**
     * Ensures that a valid font family list is available for PDF generation.
     * This method validates the provided font list, removes invalid entries,
     * and adds a default font if the list is empty or null.
     *
     * <p><b>Validation Steps:</b></p>
     * <ol>
     *   <li>If fontFamilyList is null, creates a new empty list</li>
     *   <li>Removes font families that have null or empty font types</li>
     *   <li>If the resulting list is empty, adds the default font (Open Sans Regular)</li>
     * </ol>
     *
     * <p><b>Default Font:</b></p>
     * The default font used is Open Sans Regular (weight 400), which is expected to be
     * available in the classpath at {@value #DEFAULT_FONT_PATH}.
     *
     * @param fontFamilyList the font family list to validate; may be {@code null}
     * @return a validated FontFamilyList with at least one valid font
     */
    private FontFamilyList ensureValidFontList(FontFamilyList fontFamilyList) {
        // Create new list if null
        if (fontFamilyList == null) {
            log.debug("Font family list is null, creating new empty list");
            fontFamilyList = new FontFamilyList();
        }

        // Clean up invalid font families
        if (fontFamilyList.getFontFamilyList() != null) {
            List<FontFamily> mutableList = new ArrayList<>(fontFamilyList.getFontFamilyList());
            int originalSize = mutableList.size();

            mutableList.removeIf(fontFamily ->
                    fontFamily.fontTypes() == null || fontFamily.fontTypes().isEmpty()
            );

            int removedCount = originalSize - mutableList.size();
            if (removedCount > 0) {
                log.debug("Removed {} invalid font families (null or empty font types)", removedCount);
            }

            fontFamilyList.setFontFamilyList(mutableList);
        }

        // Add default font if list is empty
        if (fontFamilyList.getFontFamilyList() == null || fontFamilyList.getFontFamilyList().isEmpty()) {
            log.info("Font family list is empty, adding default font: {} from {}",
                    DEFAULT_FONT_NAME, DEFAULT_FONT_PATH);

            FontType defaultFontType = new FontType(
                    DEFAULT_FONT_PATH,
                    DEFAULT_FONT_STYLE,
                    DEFAULT_FONT_WEIGHT
            );
            FontFamily defaultFontFamily = new FontFamily(
                    DEFAULT_FONT_NAME,
                    Collections.singletonList(defaultFontType)
            );
            fontFamilyList.setFontFamilyList(Collections.singletonList(defaultFontFamily));
        }

        return fontFamilyList;
    }

    /**
     * Creates and configures an Apache FOP factory with the specified font configuration.
     * The factory is responsible for creating FOP instances that can transform XSL-FO
     * into PDF documents.
     *
     * <p><b>Configuration:</b></p>
     * The FOP factory is configured with:
     * <ul>
     *   <li>Custom resource resolver for loading external resources</li>
     *   <li>Font configuration based on the provided font family list</li>
     *   <li>PDF/UA-1 accessibility mode</li>
     *   <li>Configurable validation settings</li>
     * </ul>
     *
     * @param fontFamilyList the list of font families to register with FOP;
     *                       must not be {@code null} or empty
     * @return a configured FopFactory instance ready for PDF generation
     * @throws Exception if FOP factory creation or configuration fails
     */
    private FopFactory createFopFactory(FontFamilyList fontFamilyList) throws Exception {
        // Build font configuration XML
        EFontFamilyLoader fontLoader = new EFontFamilyLoader(resourceProvider, fontFamilyList);
        String fontConfigXml = fontLoader.getFontListString();

        // Build complete FOP configuration
        String fopConfigXml = buildFopConfigXml(fontConfigXml);
        InputStream fopConfigStream = new ByteArrayInputStream(fopConfigXml.getBytes());

        // Create resource resolver for FOP
        ResourceResolver fopResourceResolver = new EFopResourceResolver(resourceProvider);

        // Build FOP factory
        FopFactoryBuilder fopFactoryBuilder = new FopFactoryBuilder(
                new File(".").toURI(),
                fopResourceResolver
        );

        // Parse and apply configuration
        DefaultConfigurationBuilder cfgBuilder = new DefaultConfigurationBuilder();
        Configuration cfg = cfgBuilder.build(fopConfigStream);
        fopFactoryBuilder.setConfiguration(cfg);

        return fopFactoryBuilder.build();
    }

    /**
     * Generates XSL-FO (Extensible Stylesheet Language Formatting Objects) from the
     * document model and style sheet. XSL-FO is an XML-based markup language that
     * describes the layout and formatting of the document for rendering.
     *
     * <p>The generated XSL-FO includes all document content, applied styles, and
     * references to external resources such as images.</p>
     *
     * @param document   the document structure to convert to XSL-FO
     * @param styleSheet the style definitions to apply
     * @return a String containing the complete XSL-FO document
     */
    private String generateXslFo(Document document, StyleSheet styleSheet) {
        ImageResolver imageResolver = resourceProvider::getResource;
        return foGenerator.generate(document, styleSheet, imageResolver);
    }

    /**
     * Transforms XSL-FO content to PDF format using Apache FOP.
     * This method configures the XSLT transformer with the necessary URI resolver,
     * creates a FOP instance for PDF output, and performs the transformation.
     *
     * <p><b>Process:</b></p>
     * <ol>
     *   <li>Creates a FOP instance configured for PDF output</li>
     *   <li>Sets up an XSLT transformer with custom URI resolver</li>
     *   <li>Transforms the XSL-FO source to PDF via SAX events</li>
     *   <li>Returns the PDF data as a ByteArrayOutputStream</li>
     * </ol>
     *
     * @param fopFactory the configured FOP factory
     * @param xslFoString the XSL-FO content to transform
     * @return a ByteArrayOutputStream containing the generated PDF
     * @throws Exception if transformation fails
     */
    private ByteArrayOutputStream transformToPdf(FopFactory fopFactory, String xslFoString)
            throws Exception {
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();

        try {
            // Create FOP instance for PDF generation
            Fop fop = fopFactory.newFop(FOP_MIME_TYPE, pdfOutputStream);

            // Set up transformer
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            // Set URI resolver for external resource resolution
            transformer.setURIResolver(new EFopURIResolver(resourceProvider));

            // Prepare source and result
            InputStream xslFoStream = new ByteArrayInputStream(xslFoString.getBytes());
            Source source = new StreamSource(xslFoStream);
            Result result = new SAXResult(fop.getDefaultHandler());

            // Perform transformation
            transformer.transform(source, result);

            return pdfOutputStream;

        } catch (Exception e) {
            // Ensure output stream is closed on error
            try {
                pdfOutputStream.close();
            } catch (Exception closeEx) {
                log.warn("Failed to close PDF output stream after error", closeEx);
            }
            throw e;
        }
    }

    /**
     * Builds the complete FOP configuration XML by inserting the font configuration
     * into the configuration template.
     *
     * <p>The configuration includes settings for:</p>
     * <ul>
     *   <li>Validation strictness</li>
     *   <li>Accessibility features (PDF/UA-1)</li>
     *   <li>Font registrations</li>
     *   <li>Renderer-specific settings</li>
     * </ul>
     *
     * @param fontListXml the XML fragment containing font definitions
     * @return a complete FOP configuration XML string
     */
    private String buildFopConfigXml(String fontListXml) {
        return String.format(
                FOP_CONFIG_TEMPLATE,
                FOP_VERSION,
                FOP_STRICT_VALIDATION,
                FOP_ACCESSIBILITY_ENABLED,
                FOP_PDF_UA_MODE,
                fontListXml
        );
    }
}