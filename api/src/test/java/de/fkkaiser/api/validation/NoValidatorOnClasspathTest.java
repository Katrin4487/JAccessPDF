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
package de.fkkaiser.api.validation;

import de.fkkaiser.api.PdfGenerationFacade;
import de.fkkaiser.api.utils.EClasspathResourceProvider;
import de.fkkaiser.model.font.FontFamilyList;
import de.fkkaiser.model.structure.Document;
import de.fkkaiser.model.style.StyleSheet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The api module deliberately does NOT depend on jaccess-pdf-validation, so no
 * implementation is on this module's test class path. That is what makes this
 * the test for the optional wiring: it proves the add-on really is optional,
 * and that leaving it out produces a clear error instead of a silently skipped
 * validation or a NoClassDefFoundError.
 */
class NoValidatorOnClasspathTest {

    @Test
    @DisplayName("No PdfValidationService is on the api module's class path")
    void noServiceIsPresent() {
        assertTrue(ServiceLoader.load(PdfValidationService.class).findFirst().isEmpty(),
                "api must not pull in a validator - that would drag veraPDF into "
                        + "the dependency tree of every consumer");
    }

    @Test
    @DisplayName("generateAndValidatePDF fails fast and names the artifact to add")
    void generateAndValidateFailsWithAnActionableMessage() {
        PdfGenerationFacade facade = new PdfGenerationFacade(new EClasspathResourceProvider());

        // Casts only to pick the overload - there is a second
        // generateAndValidatePDF(MasterBundle, ...) that delegates here.
        // Nulls are deliberate: the missing validator has to be detected before
        // the arguments are ever touched, so that no PDF is rendered just to be
        // discarded. A NullPointerException here would mean the check moved back
        // behind the generation step.
        PdfValidationException thrown = assertThrows(PdfValidationException.class, () ->
                facade.generateAndValidatePDF((Document) null, (StyleSheet) null,
                        (FontFamilyList) null));

        assertTrue(thrown.getMessage().contains("jaccess-pdf-validation"),
                "the message must name the artifact to add, was: " + thrown.getMessage());
    }
}
