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

import de.fkkaiser.api.validation.PdfUaReport;
import de.fkkaiser.api.validation.PdfUaViolation;
import de.fkkaiser.api.validation.PdfValidationException;
import de.fkkaiser.api.validation.PdfValidationService;
import org.verapdf.gf.foundry.VeraGreenfieldFoundryProvider;
import org.verapdf.pdfa.*;
import org.verapdf.pdfa.flavours.PDFAFlavour;
import org.verapdf.pdfa.results.TestAssertion;
import org.verapdf.pdfa.results.ValidationResult;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public final class VeraPdfValidationService implements PdfValidationService {

    static {
        // Once per JVM, not per call.
        VeraGreenfieldFoundryProvider.initialise();
    }

    @Override
    public PdfUaReport validate(byte[] pdf) {
        try (InputStream in = new ByteArrayInputStream(pdf);
             // PDFUA_1 explicitly: relying on the flavour declared in the file
             // means a PDF that declares nothing gets validated against nothing
             // and reports as compliant.
             PDFAParser parser = Foundries.defaultInstance()
                     .createParser(in, PDFAFlavour.PDFUA_1);
             PDFAValidator validator = Foundries.defaultInstance()
                     .createValidator(PDFAFlavour.PDFUA_1, false)) {

            ValidationResult result = validator.validate(parser);

            List<PdfUaViolation> violations = result.getTestAssertions().stream()
                    .filter(a -> a.getStatus() == TestAssertion.Status.FAILED)
                    .map(a -> new PdfUaViolation(
                            a.getRuleId().getClause(),
                            a.getRuleId().getTestNumber(),
                            a.getMessage(),
                            a.getLocationContext()))
                    .toList();

            return new PdfUaReport(result.isCompliant(),
                    result.getTotalAssertions(),
                    violations);

        } catch (Exception e) {
            throw new PdfValidationException("Could not validate PDF", e);
        }
    }
}