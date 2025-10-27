package de.fkkaiser.api;


import de.fkkaiser.api.utils.EClasspathResourceProvider;
import de.fkkaiser.api.utils.EResourceProvider;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PdfGenerationFacadeTest {

    @Test
    public void shouldGeneratePdfFromJsons() throws Exception {


        EResourceProvider provider = new EClasspathResourceProvider();
        PdfGenerationFacade pdfGenerationFacade = new PdfGenerationFacade(provider);

        InputStream structureStream = getResourceAsStream("jsons/structure2.json");
        InputStream styleStream = getResourceAsStream("jsons/style2.json");
        InputStream fontStream = getResourceAsStream("jsons/font-families.json"); // Using the name from the previous example

        ByteArrayOutputStream out = pdfGenerationFacade.generatePDF(structureStream,styleStream,fontStream);

        writeOutputStreamToFile(out, "output.pdf");


    }

    private void writeOutputStreamToFile(ByteArrayOutputStream stream, String fileName) {
        System.out.println("Attempting to write test PDF to file...");
        try {
            Path outputPath = Paths.get(fileName);
            try (OutputStream fileOutputStream = Files.newOutputStream(outputPath)) {
                stream.writeTo(fileOutputStream);
                System.out.println("Successfully wrote test PDF to: " + outputPath.toAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("Failed to write test PDF to file: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private InputStream getResourceAsStream(String fileName) {
        return getClass().getClassLoader().getResourceAsStream(fileName);
    }
}
