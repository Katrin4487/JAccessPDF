package de.kaiser.api;

import de.kaiser.api.utils.EFopResourceResolver;
import de.kaiser.api.utils.EFopURIResolver;
import de.kaiser.api.utils.EResourceProvider;

import de.kaiser.generator.*;

import de.kaiser.model.font.FontFamilyList;
import de.kaiser.model.structure.Document;
import de.kaiser.model.style.StyleSheet;
import de.kaiser.processor.StyleResolverService;
import de.kaiser.processor.reader.DocumentReader;
import de.kaiser.processor.reader.FontFamilyListReader;
import de.kaiser.processor.reader.StyleSheetReader;
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
import java.io.*;
import java.net.URL;

/**
 * The facade is the central entry point for the creation of a PDF.
 * It orchestrates the entire process and is designed to be stateless.
 */
public final class PdfGenerationFacade {

    private static final Logger log = LoggerFactory.getLogger(PdfGenerationFacade.class);

    // Services are now final fields for better testability and reusability.
    private final DocumentReader documentReader;
    private final StyleSheetReader styleSheetReader;
    private final FontFamilyListReader fontListReader;
    private final XslFoGenerator foGenerator;

    public PdfGenerationFacade() {
        this.documentReader = new DocumentReader();
        this.styleSheetReader = new StyleSheetReader();
        this.fontListReader = new FontFamilyListReader();
        this.foGenerator = new XslFoGenerator();
    }

    /**
     * API Facade 1: Generates a PDF from JSON input streams.
     * Ideal for web services or data-driven applications.
     */
    public ByteArrayOutputStream generatePDF(InputStream structureJson, InputStream styleJson, InputStream fontListJson, EResourceProvider provider) throws Exception {
        log.debug("Reading models from input streams...");
        Document doc = documentReader.readJson(structureJson);
        StyleSheet styleSheet = styleSheetReader.readJson(styleJson);
        FontFamilyList fontFamilyList = fontListReader.readJson(fontListJson);

        // Delegate to the object-based method
        return generatePDF(doc, styleSheet, fontFamilyList, provider);
    }

    /**
     * API Facade 2: Generates a PDF from pre-built Java model objects.
     * Ideal for programmatic PDF creation within a Java application.
     */
    public ByteArrayOutputStream generatePDF(Document document, StyleSheet styleSheet, FontFamilyList fontFamilyList, EResourceProvider provider) throws Exception {
        log.debug("Generating PDF from model objects...");

        // 1. Link styles with content
        StyleResolverService.resolve(document, styleSheet);

        // 2. Build FOP configuration dynamically
        EFontFamilyLoader fontLoader = new EFontFamilyLoader(provider, fontFamilyList);
        String fopConfigXml = buildFopConfig(fontLoader);
        InputStream fopConfigStream = new ByteArrayInputStream(fopConfigXml.getBytes());

        URL imageUrl = null;
        if(document.internalAddresses()!=null && document.internalAddresses().imageDictionary()!=null){
            imageUrl = provider.getResource(document.internalAddresses().imageDictionary());
        }


        // 3. Generate XSL-FO string from the document model
        String xslFoString = foGenerator.generate(document, styleSheet,imageUrl);
        //System.out.println("######### OUT ####\n"+xslFoString);
        InputStream xslFoStream = new ByteArrayInputStream(xslFoString.getBytes());

        // 4. Use FOP to transform XSL-FO to PDF
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();

        // Create the correct ResourceResolver for FOP
        ResourceResolver fopResourceResolver = new EFopResourceResolver(provider);
        FopFactoryBuilder fopFactoryBuilder = new FopFactoryBuilder(new File(".").toURI(), fopResourceResolver);

        DefaultConfigurationBuilder cfgBuilder = new DefaultConfigurationBuilder();
        Configuration cfg = cfgBuilder.build(fopConfigStream);
        fopFactoryBuilder.setConfiguration(cfg);
        FopFactory fopFactory = fopFactoryBuilder.build();

        Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, pdfOutputStream);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        // The URI resolver is crucial for the Transformer to find resources
        transformer.setURIResolver(new EFopURIResolver(provider));

        Source src = new StreamSource(xslFoStream);
        Result res = new SAXResult(fop.getDefaultHandler());
        transformer.transform(src, res);

        return pdfOutputStream;
    }

    /**
     * Dynamically generates the FOP configuration XML to register fonts.
     */
    private String buildFopConfig(EFontFamilyLoader fontLoader) throws IOException {
        String fontListXml = fontLoader.getFontListString();
        String out= "<?xml version=\"1.0\"?>\n" +
                "<fop version=\"1.0\">\n" +
                "  <strict-validation>false</strict-validation>\n" +
                "  <accessibility>true</accessibility>\n" +
                "  <renderers>\n" +
                "    <renderer mime=\"application/pdf\">\n" +
                "      <pdf-ua-mode>PDF/UA-1</pdf-ua-mode>\n" +
                fontListXml +
                "    </renderer>\n" +
                "  </renderers>\n" +
                "</fop>";
        //System.out.println("\n######### FOP #############\n"+out);
        return out;
    }
}
