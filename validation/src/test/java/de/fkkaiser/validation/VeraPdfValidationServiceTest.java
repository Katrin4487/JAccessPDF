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
import de.fkkaiser.api.validation.PdfUaReport;
import de.fkkaiser.api.validation.PdfValidationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.*;

class VeraPdfValidationServiceTest {

    private static PdfValidationService service() {
        return ServiceLoader.load(PdfValidationService.class).findFirst()
                .orElseThrow(() -> new AssertionError(
                        "ServiceLoader found no PdfValidationService. On the class path this "
                                + "needs META-INF/services, not just the provides directive in "
                                + "module-info - surefire runs with useModulePath=false."));
    }

    @Test
    @DisplayName("The implementation is discoverable through ServiceLoader")
    void serviceIsDiscoverable() {
        assertInstanceOf(VeraPdfValidationService.class, service());
    }

    @Test
    @DisplayName("A generated document passes PDF/UA-1 validation")
    void generatedDocumentIsCompliant() throws Exception {
        ByteArrayOutputStream pdf = SimpleDocumentBuilder.create("Compliant Document")
                .withLanguage("en")
                .addHeading("Hello World", 1)
                .addParagraph("A paragraph.")
                .addUnorderedList(List.of("one", "two"))
                .build()
                .toStream();

        PdfUaReport report = service().validate(pdf.toByteArray());

        assertTrue(report.compliant(), report.toSummary());
        assertTrue(report.violations().isEmpty());
        assertTrue(report.totalChecks() > 100,
                "expected a substantial number of checks, got " + report.totalChecks());
    }

    @Test
    @DisplayName("An untagged PDF is reported as non-compliant, with usable detail")
    void untaggedPdfIsRejected() throws Exception {
        // The test that proves this validator can fail at all. Without it, a
        // green suite cannot be told apart from a validator that approves
        // everything. untagged.pdf is a bare one-page PDF with no structure
        // tree, no title and no metadata.
        byte[] pdf;
        try (InputStream in = getClass().getResourceAsStream("/untagged.pdf")) {
            assertNotNull(in, "test resource untagged.pdf is missing");
            pdf = in.readAllBytes();
        }

        PdfUaReport report = service().validate(pdf);

        assertFalse(report.compliant(), "an untagged PDF must not pass PDF/UA-1");
        assertFalse(report.violations().isEmpty(), "violations must be reported, not just a false flag");

        // Every violation must carry enough detail to act on.
        report.violations().forEach(v -> {
            assertNotNull(v.clause(), "clause missing");
            assertFalse(v.clause().isBlank(), "clause blank");
            assertNotNull(v.message(), "message missing");
            assertFalse(v.message().isBlank(), "message blank");
        });

        // The missing structure tree is clause 7.1 of PDF/UA-1.
        assertTrue(report.violations().stream().anyMatch(v -> v.clause().startsWith("7.1")),
                "expected a clause 7.1 violation for the missing structure tree, got: "
                        + report.violations());
    }

    @Test
    @DisplayName("Characterisation: defaults that make documents pass are applied silently")
    void defaultsAreAppliedSilently() throws Exception {
        // These two cases pass validation only because the library fills gaps in
        // for the caller. Locked in here so a change in that behaviour shows up
        // as a failing test rather than as a silent change in output.

        // No withLanguage() call - a default language is written anyway.
        ByteArrayOutputStream noLanguage = SimpleDocumentBuilder.create("No Language")
                .addParagraph("This document declares no language.")
                .build()
                .toStream();
        assertTrue(service().validate(noLanguage.toByteArray()).compliant(),
                "a default language is currently written when none is set");

        // addImage without alt text - a placeholder alt text is written anyway.
        ByteArrayOutputStream noAltText = SimpleDocumentBuilder.create("No Alt Text")
                .withLanguage("en")
                .addParagraph("Followed by an image without alt text.")
                .addImage("images/logo.png")
                .build()
                .toStream();
        assertTrue(service().validate(noAltText.toByteArray()).compliant(),
                "a placeholder alt text is currently written when none is given");
    }
}
