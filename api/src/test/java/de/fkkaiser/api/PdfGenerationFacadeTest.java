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

        InputStream structureStream = getResourceAsStream("jsons/structure.json");
        InputStream styleStream = getResourceAsStream("jsons/style.json");
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
