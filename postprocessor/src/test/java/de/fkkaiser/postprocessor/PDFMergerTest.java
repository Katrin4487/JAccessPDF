package de.fkkaiser.postprocessor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PDFMergerTest {

    @Test
    @DisplayName("Should merge two PDF documents correctly")
    public void testMergePDFs() throws IOException {
        ByteArrayOutputStream out = PDFMerger.builder()
                .addDocument("src/test/resources/core-example.pdf")
                .addDocument("src/test/resources/helloWord.pdf")
                .merge();

        assertNotNull(out);
    }
}
