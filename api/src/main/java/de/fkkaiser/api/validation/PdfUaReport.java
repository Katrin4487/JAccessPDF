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

import java.util.List;

/** Outcome of a PDF/UA validation run. Free of any validator-specific types. */
public record PdfUaReport(boolean compliant, int totalChecks,
                          List<PdfUaViolation> violations) {

    public PdfUaReport {
        violations = List.copyOf(violations);
    }

    public String toSummary() {
        if (compliant) {
            return "PDF/UA-1 compliant (" + totalChecks + " checks)";
        }
        return "PDF/UA-1 violations: " + violations.size() + " of " + totalChecks + " checks failed";
    }
}