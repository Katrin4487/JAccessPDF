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
package de.fkkaiser.bundle.validation;

import de.fkkaiser.model.font.FontFamilyList;
import de.fkkaiser.model.font.FontStyleValue;
import de.fkkaiser.model.style.StyleSheet;
import de.fkkaiser.model.style.TextStyle;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FontReferenceValidatorTest {

    private static StyleSheet styleSheetWith(TextStyle... textStyles) {
        return new StyleSheet(List.of(textStyles), List.of(), List.of(), null);
    }

    private static final FontFamilyList ROBOTO_REGULAR_AND_BOLD = FontFamilyList.builder()
            .addFontFamily("Roboto")
            .addFont("fonts/Roboto-Regular.ttf", FontStyleValue.NORMAL, "400")
            .addFont("fonts/Roboto-Bold.ttf", FontStyleValue.NORMAL, "700")
            .endFontFamily()
            .build();

    @Test
    void shouldReturnEmptyListWhenAllReferencesAreValid() {
        TextStyle body = new TextStyle("body", "12pt", "Roboto", "400", "normal");
        TextStyle heading = new TextStyle("heading", "16pt", "Roboto", "700", "normal");

        List<String> missing = FontReferenceValidator.findMissingFontReferences(
                styleSheetWith(body, heading), ROBOTO_REGULAR_AND_BOLD);

        assertTrue(missing.isEmpty());
    }

    @Test
    void shouldReportUnknownFontFamily() {
        TextStyle body = new TextStyle("body", "12pt", "Arial", "400", "normal");

        List<String> missing = FontReferenceValidator.findMissingFontReferences(
                styleSheetWith(body), ROBOTO_REGULAR_AND_BOLD);

        assertEquals(1, missing.size());
    }

    @Test
    void shouldReportKnownFamilyWithMissingWeightStyleCombination() {
        // Roboto exists, but no italic variant was uploaded
        TextStyle emphasis = new TextStyle("emphasis", "12pt", "Roboto", "400", "italic");

        List<String> missing = FontReferenceValidator.findMissingFontReferences(
                styleSheetWith(emphasis), ROBOTO_REGULAR_AND_BOLD);

        assertEquals(1, missing.size());
    }

    @Test
    void shouldCollectAllProblemsInOneCall() {
        TextStyle unknownFamily = new TextStyle("body", "12pt", "Arial", "400", "normal");
        TextStyle unknownCombo = new TextStyle("emphasis", "12pt", "Roboto", "400", "italic");
        TextStyle valid = new TextStyle("heading", "16pt", "Roboto", "700", "normal");

        List<String> missing = FontReferenceValidator.findMissingFontReferences(
                styleSheetWith(unknownFamily, unknownCombo, valid), ROBOTO_REGULAR_AND_BOLD);

        assertEquals(2, missing.size());
    }

    @Test
    void shouldReturnEmptyListWhenNoTextStylesDefined() {
        List<String> missing = FontReferenceValidator.findMissingFontReferences(
                styleSheetWith(), ROBOTO_REGULAR_AND_BOLD);

        assertTrue(missing.isEmpty());
    }
}
