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
package de.fkkaiser.validation;

import de.fkkaiser.api.simplelayer.SimpleDocumentBuilder;
import de.fkkaiser.api.simplelayer.SimpleTable;
import de.fkkaiser.api.validation.PdfUaReport;
import de.fkkaiser.api.validation.PdfValidationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Validates one document per supported feature against PDF/UA-1, so that a
 * regression in the tag structure of any single element type is caught instead
 * of being masked by the other elements in a larger document.
 *
 * <p>These check the machine-verifiable PDF/UA-1 clauses. They cannot judge
 * whether an alt text is meaningful, whether the reading order makes sense, or
 * whether a table is a data table needing header association - PDF/UA leaves
 * those to human review on purpose.</p>
 */
class PdfUaFeatureSweepTest {

    private static final PdfValidationService SERVICE =
            ServiceLoader.load(PdfValidationService.class).findFirst()
                    .orElseThrow(() -> new AssertionError("no PdfValidationService on the class path"));

    static Stream<Arguments> features() {
        return Stream.of(
                feature("headings h1-h3", b -> b
                        .addHeading("Level 1", 1)
                        .addHeading("Level 2", 2)
                        .addHeading("Level 3", 3)),
                feature("paragraph", b -> b.addParagraph("Just a paragraph.")),
                feature("unordered list", b -> b.addUnorderedList(List.of("a", "b", "c"))),
                feature("ordered list", b -> b.addOrderedList(List.of("first", "second"))),
                feature("image with alt text", b -> b.addImage("images/logo.png", "A company logo")),
                feature("table with header row", b -> b.addTable(new SimpleTable()
                        .addHeaderRow("Name", "Value")
                        .addBodyRow("rows", "2"))),
                feature("table with column widths", b -> b.addTable(new SimpleTable()
                        .setColumns("40%", "60%")
                        .addHeaderRow("Name", "Value")
                        .addBodyRow("rows", "2"))),
                // Passes validation: veraPDF does not decide whether this is a
                // data table that would need header association, or a layout table.
                feature("table without header row", b -> b.addTable(new SimpleTable()
                        .addBodyRow("just", "body"))),
                feature("markdown inline formatting", b -> b
                        .addMarkdown("# Heading\n\nSome **bold** and *italic* text.")),
                feature("markdown link", b -> b
                        .addMarkdown("A [link to example](https://example.org) inline.")),
                feature("markdown list", b -> b.addMarkdown("- one\n- two\n- three")),
                feature("all features combined", b -> b
                        .addHeading("Report", 1)
                        .addParagraph("Intro.")
                        .addUnorderedList(List.of("x", "y"))
                        .addTable(new SimpleTable().addHeaderRow("K", "V").addBodyRow("a", "1"))
                        .addImage("images/logo.png", "Logo")
                        .addMarkdown("Closing **remark** with a [link](https://example.org).")));
    }

    private static Arguments feature(String name, Consumer<SimpleDocumentBuilder> build) {
        return Arguments.of(name, build);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("features")
    @DisplayName("Each feature produces a PDF/UA-1 compliant document")
    void featureIsCompliant(String name, Consumer<SimpleDocumentBuilder> build) throws Exception {
        SimpleDocumentBuilder builder = SimpleDocumentBuilder.create(name).withLanguage("en");
        build.accept(builder);
        ByteArrayOutputStream pdf = builder.build().toStream();

        PdfUaReport report = SERVICE.validate(pdf.toByteArray());

        // Guards against a vacuous pass: an empty or unreadable document would
        // produce very few checks and still report as compliant.
        assertTrue(report.totalChecks() > 50,
                "suspiciously few checks (" + report.totalChecks() + ") for '" + name
                        + "' - did the document actually contain the element?");

        assertTrue(report.compliant(), () -> "'" + name + "' is not PDF/UA-1 compliant: "
                + report.toSummary() + "\n"
                + report.violations().stream()
                        .map(v -> "  clause " + v.clause() + " test " + v.testNumber() + ": " + v.message())
                        .reduce("", (a, b2) -> a + b2 + "\n"));
    }
}
