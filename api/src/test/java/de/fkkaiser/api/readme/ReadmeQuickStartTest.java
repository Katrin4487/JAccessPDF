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
package de.fkkaiser.api.readme;

import de.fkkaiser.api.simplelayer.SimpleDocumentBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ReadmeQuickStartTest {

    @Test
    void quickStartFromReadmeProducesAPdf(@TempDir Path tmp) throws Exception {
        Path out = tmp.resolve("hello.pdf");

        SimpleDocumentBuilder.create("My First Accessible PDF")
                .withLanguage("en")
                .addHeading("Hello World", 1)
                .addParagraph("This document is PDF/UA compliant out of the box.")
                .addUnorderedList(List.of("Tagged structure", "Document language", "Alt texts for images"))
                .build()
                .saveAs(out.toString());

        assertTrue(Files.size(out) > 0, "PDF should not be empty");
        assertTrue(new String(Files.readAllBytes(out)).startsWith("%PDF-"), "should be a PDF");
    }
}
