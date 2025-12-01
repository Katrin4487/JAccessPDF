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
package de.fkkaiser.model.style;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SectionStylePropertiesTest {

    @Nested
    @DisplayName("mergeWith Tests - Annotation-Based Inheritance")
    class MergeWithTests {

        @Test
        @DisplayName("Should inherit section-specific properties from SectionStyleProperties")
        void shouldInheritSectionProperties() {
            SectionStyleProperties base = new SectionStyleProperties();
            base.setSectionMarker("§");
            base.setKeepTogether(true);
            base.setOrphans(2);
            base.setWidows(3);
            base.setTextStyleName("marker-style");

            SectionStyleProperties child = new SectionStyleProperties();
            // All section properties are null

            child.mergeWith(base);

            // Section-specific properties WITHOUT @Inheritable should still be inherited
            // because they're manually merged in the overridden mergeWith() method
            assertEquals("§", child.getSectionMarker());
            assertTrue(child.getKeepTogether());
            assertEquals(2, child.getOrphans());
            assertEquals(3, child.getWidows());
            assertEquals("marker-style", child.getTextStyleName());
        }

        @Test
        @DisplayName("Should NOT override non-null section properties")
        void shouldNotOverrideNonNullProperties() {
            SectionStyleProperties base = new SectionStyleProperties();
            base.setSectionMarker("§");
            base.setKeepTogether(true);
            base.setOrphans(2);
            base.setWidows(2);

            SectionStyleProperties child = new SectionStyleProperties();
            child.setSectionMarker("⚠️"); // Already set
            child.setKeepTogether(false); // Already set
            child.setOrphans(5); // Already set
            child.setWidows(5); // Already set

            child.mergeWith(base);

            // Should keep child's values
            assertEquals("⚠️", child.getSectionMarker());
            assertFalse(child.getKeepTogether());
            assertEquals(5, child.getOrphans());
            assertEquals(5, child.getWidows());
        }

        @Test
        @DisplayName("Should inherit backgroundColor from parent (@Inheritable)")
        void shouldInheritInheritableProperties() {
            SectionStyleProperties base = new SectionStyleProperties();
            base.setBackgroundColor("#ff0000");

            SectionStyleProperties child = new SectionStyleProperties();
            // backgroundColor is null

            child.mergeWith(base);

            // backgroundColor has @Inheritable annotation -> should be inherited
            assertEquals("#ff0000", child.getBackgroundColor());
        }

        @Test
        @DisplayName("Should NOT inherit padding/border from parent (no @Inheritable)")
        void shouldNotInheritNonInheritableProperties() {
            SectionStyleProperties base = new SectionStyleProperties();
            base.setPadding("1cm");
            base.setBorder("1pt solid black");
            base.setSpaceBefore("2cm");
            base.setBreakBefore(PageBreakVariant.PAGE);

            SectionStyleProperties child = new SectionStyleProperties();
            // All properties are null

            child.mergeWith(base);

            // These properties have NO @Inheritable annotation -> should NOT be inherited
            assertNull(child.getPadding());
            assertNull(child.getBorder());
            assertNull(child.getSpaceBefore());
            assertNull(child.getBreakBefore());
        }

        @Test
        @DisplayName("Should NOT inherit from incompatible type (ElementBlockStyleProperties)")
        void shouldNotInheritFromDifferentType() {
            ElementBlockStyleProperties base = new ElementBlockStyleProperties();
            base.setPadding("1cm");
            base.setBorder("1pt solid black");
            base.setBackgroundColor("#ff0000");

            SectionStyleProperties child = new SectionStyleProperties();
            child.setBackgroundColor(null); // Explicitly null

            child.mergeWith(base);

            // Different class types -> mergeWith() should skip
            // (class equality check in ElementStyleProperties.mergeWith())
            assertNull(child.getBackgroundColor());
            assertNull(child.getPadding());
            assertNull(child.getBorder());

            // Section-specific properties should remain null
            assertNull(child.getSectionMarker());
            assertNull(child.getKeepTogether());
        }

        @Test
        @DisplayName("Should handle partial merge correctly")
        void shouldHandlePartialMerge() {
            SectionStyleProperties base = new SectionStyleProperties();
            base.setSectionMarker("§");
            base.setOrphans(2);
            base.setBackgroundColor("#efefef");
            // Other properties null

            SectionStyleProperties child = new SectionStyleProperties();
            child.setKeepTogether(true);
            child.setWidows(3);
            // Other properties null

            child.mergeWith(base);

            // Should have combination
            assertEquals("§", child.getSectionMarker()); // from base (manual merge)
            assertTrue(child.getKeepTogether()); // from child (kept)
            assertEquals(2, child.getOrphans()); // from base (manual merge)
            assertEquals(3, child.getWidows()); // from child (kept)
            assertEquals("#efefef", child.getBackgroundColor()); // from base (@Inheritable)
            assertNull(child.getBreakBefore()); // still null
        }
    }

    @Nested
    @DisplayName("Copy Functionality Tests")
    class CopyTests {

        @Test
        @DisplayName("Should create deep copy with all properties")
        void shouldCreateDeepCopy() {
            SectionStyleProperties original = new SectionStyleProperties();
            original.setSectionMarker("⚠️");
            original.setTextStyleName("warning-marker");
            original.setKeepTogether(true);
            original.setBreakBefore(PageBreakVariant.EVEN_PAGE);
            original.setBreakAfter(PageBreakVariant.ODD_PAGE);
            original.setKeepWithNext(false);
            original.setOrphans(3);
            original.setWidows(3);
            original.setPadding("1.5cm");
            original.setBorder("2pt solid red");
            original.setBackgroundColor("#fff3cd");

            SectionStyleProperties copy = original.copy();

            // Verify all properties are copied
            assertEquals(original.getSectionMarker(), copy.getSectionMarker());
            assertEquals(original.getTextStyleName(), copy.getTextStyleName());
            assertEquals(original.getKeepTogether(), copy.getKeepTogether());
            assertEquals(original.getBreakBefore(), copy.getBreakBefore());
            assertEquals(original.getBreakAfter(), copy.getBreakAfter());
            assertEquals(original.getKeepWithNext(), copy.getKeepWithNext());
            assertEquals(original.getOrphans(), copy.getOrphans());
            assertEquals(original.getWidows(), copy.getWidows());
            assertEquals(original.getPadding(), copy.getPadding());
            assertEquals(original.getBorder(), copy.getBorder());
            assertEquals(original.getBackgroundColor(), copy.getBackgroundColor());

            // Verify it's a different instance
            assertNotSame(original, copy);

            // Modify copy
            copy.setSectionMarker("►");
            copy.setKeepTogether(false);
            copy.setOrphans(5);
            copy.setPadding("3cm");
            copy.setBackgroundColor("#000000");

            // Original should remain unchanged
            assertEquals("⚠️", original.getSectionMarker());
            assertTrue(original.getKeepTogether());
            assertEquals(3, original.getOrphans());
            assertEquals("1.5cm", original.getPadding());
            assertEquals("#fff3cd", original.getBackgroundColor());
        }

        @Test
        @DisplayName("Should copy null properties correctly")
        void shouldCopyNullProperties() {
            SectionStyleProperties original = new SectionStyleProperties();
            // All properties are null

            SectionStyleProperties copy = original.copy();

            assertNull(copy.getSectionMarker());
            assertNull(copy.getTextStyleName());
            assertNull(copy.getKeepTogether());
            assertNull(copy.getBreakBefore());
            assertNull(copy.getBreakAfter());
            assertNull(copy.getKeepWithNext());
            assertNull(copy.getOrphans());
            assertNull(copy.getWidows());
            assertNull(copy.getPadding());
            assertNull(copy.getBorder());
            assertNull(copy.getBackgroundColor());
        }
    }

    @Nested
    @DisplayName("Property Getter/Setter Tests")
    class PropertyTests {

        @Test
        @DisplayName("Should set and get all properties correctly")
        void shouldSetAndGetAllProperties() {
            SectionStyleProperties style = new SectionStyleProperties();

            style.setSectionMarker("•");
            style.setTextStyleName("bullet-style");
            style.setKeepTogether(true);
            style.setBreakBefore(PageBreakVariant.ODD_PAGE);
            style.setBreakAfter(PageBreakVariant.AUTO);
            style.setKeepWithNext(false);
            style.setOrphans(4);
            style.setWidows(4);
            style.setBackgroundColor("#f0f0f0");

            assertEquals("•", style.getSectionMarker());
            assertEquals("bullet-style", style.getTextStyleName());
            assertTrue(style.getKeepTogether());
            assertEquals(PageBreakVariant.ODD_PAGE, style.getBreakBefore());
            assertEquals("auto", style.getBreakAfter().getFoValue());
            assertFalse(style.getKeepWithNext());
            assertEquals(4, style.getOrphans());
            assertEquals(4, style.getWidows());
            assertEquals("#f0f0f0", style.getBackgroundColor());
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should return error when section marker is set without text style")
        void shouldReturnErrorForMarkerWithoutTextStyle() {
            SectionStyleProperties style = new SectionStyleProperties();
            style.setSectionMarker("§");
            // textStyleName is null

            List<String> errors = style.validate();

            assertFalse(errors.isEmpty());
            assertTrue(errors.get(0).contains("Section marker is set without setting text style name"));
        }

        @Test
        @DisplayName("Should pass validation when section marker has text style")
        void shouldPassValidationWithMarkerAndTextStyle() {
            SectionStyleProperties style = new SectionStyleProperties();
            style.setSectionMarker("§");
            style.setTextStyleName("marker-style");

            List<String> errors = style.validate();

            assertTrue(errors.isEmpty());
        }

        @Test
        @DisplayName("Should pass validation when section marker is not set")
        void shouldPassValidationWithoutMarker() {
            SectionStyleProperties style = new SectionStyleProperties();
            // sectionMarker is null
            style.setKeepTogether(true);

            List<String> errors = style.validate();

            assertTrue(errors.isEmpty());
        }

        @Test
        @DisplayName("Should pass validation when all properties are null")
        void shouldPassValidationWithAllNull() {
            SectionStyleProperties style = new SectionStyleProperties();
            // All properties null

            List<String> errors = style.validate();

            assertTrue(errors.isEmpty());
        }
    }
}