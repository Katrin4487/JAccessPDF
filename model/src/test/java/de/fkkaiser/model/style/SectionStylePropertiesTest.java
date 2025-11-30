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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SectionStylePropertiesTest {

    @Test
    void shouldMergeSectionSpecificProperties() {
        SectionStyleProperties base = new SectionStyleProperties();
        base.setSectionMarker("§");
        base.setKeepTogether(true);
        base.setBreakBefore(PageBreakVariant.PAGE);
        base.setBreakAfter(PageBreakVariant.COLUMN);
        base.setKeepWithNext(true);
        base.setOrphans(2);
        base.setWidows(2);
        base.setPadding("1cm");

        SectionStyleProperties child = new SectionStyleProperties();
        child.setPadding("2cm"); // Override
        // All section-specific properties remain null

        child.mergeWith(base);

        // Section-specific properties should be inherited
        assertEquals("§", child.getSectionMarker());
        assertTrue(child.getKeepTogether());
        assertEquals(PageBreakVariant.PAGE, child.getBreakBefore());
        assertEquals("column", child.getBreakAfter().getFoValue());
        assertTrue(child.getKeepWithNext());
        assertEquals(2, child.getOrphans());
        assertEquals(2, child.getWidows());

        // Overridden property should keep its value
        assertEquals("2cm", child.getPadding());
    }

    @Test
    void shouldNotOverrideNonNullProperties() {
        SectionStyleProperties base = new SectionStyleProperties();
        base.setSectionMarker("§");
        base.setKeepTogether(true);

        SectionStyleProperties child = new SectionStyleProperties();
        child.setSectionMarker("⚠️"); // Already set
        child.setKeepTogether(false); // Already set

        child.mergeWith(base);

        // Should keep child's values
        assertEquals("⚠️", child.getSectionMarker());
        assertFalse(child.getKeepTogether());
    }

    @Test
    void shouldMergeWithNonSectionStyleProperties() {
        ElementBlockStyleProperties base = new ElementBlockStyleProperties();
        base.setPadding("1cm");
        base.setBorder("1pt solid black");

        SectionStyleProperties child = new SectionStyleProperties();
        child.setBackgroundColor("#fff");

        child.mergeWith(base);

        // Should inherit from ElementBlockStyleProperties
        assertEquals("1cm", child.getPadding());
        assertEquals("1pt solid black", child.getBorder());
        assertEquals("#fff", child.getBackgroundColor());

        // Section-specific properties should remain null (no merge from non-SectionStyleProperties)
        assertNull(child.getSectionMarker());
        assertNull(child.getKeepTogether());
    }

    @Test
    void shouldCreateDeepCopy() {
        SectionStyleProperties original = new SectionStyleProperties();
        original.setSectionMarker("⚠️");
        original.setKeepTogether(true);
        original.setBreakBefore(PageBreakVariant.EVEN_PAGE);
        original.setBreakAfter(PageBreakVariant.ODD_PAGE);
        original.setKeepWithNext(false);
        original.setOrphans(3);
        original.setWidows(3);
        original.setPadding("1.5cm");
        original.setBorder("2pt solid red");

        SectionStyleProperties copy = original.copy();

        // All properties should be copied
        assertEquals(original.getSectionMarker(), copy.getSectionMarker());
        assertEquals(original.getKeepTogether(), copy.getKeepTogether());
        assertEquals(original.getBreakBefore(), copy.getBreakBefore());
        assertEquals(original.getBreakAfter(), copy.getBreakAfter());
        assertEquals(original.getKeepWithNext(), copy.getKeepWithNext());
        assertEquals(original.getOrphans(), copy.getOrphans());
        assertEquals(original.getWidows(), copy.getWidows());
        assertEquals(original.getPadding(), copy.getPadding());
        assertEquals(original.getBorder(), copy.getBorder());

        // Modify copy
        copy.setSectionMarker("►");
        copy.setKeepTogether(false);
        copy.setOrphans(5);
        copy.setPadding("3cm");

        // Original should remain unchanged
        assertEquals("⚠️", original.getSectionMarker());
        assertTrue(original.getKeepTogether());
        assertEquals(3, original.getOrphans());
        assertEquals("1.5cm", original.getPadding());
    }

    @Test
    void shouldCopyNullProperties() {
        SectionStyleProperties original = new SectionStyleProperties();
        // All properties are null

        SectionStyleProperties copy = original.copy();

        assertNull(copy.getSectionMarker());
        assertNull(copy.getKeepTogether());
        assertNull(copy.getBreakBefore());
        assertNull(copy.getBreakAfter());
        assertNull(copy.getKeepWithNext());
        assertNull(copy.getOrphans());
        assertNull(copy.getWidows());
    }

    @Test
    void shouldHandlePartialMerge() {
        SectionStyleProperties base = new SectionStyleProperties();
        base.setSectionMarker("§");
        base.setOrphans(2);
        // Other properties null

        SectionStyleProperties child = new SectionStyleProperties();
        child.setKeepTogether(true);
        child.setWidows(3);
        // Other properties null

        child.mergeWith(base);

        // Should have combination
        assertEquals("§", child.getSectionMarker()); // from base
        assertTrue(child.getKeepTogether()); // from child
        assertEquals(2, child.getOrphans()); // from base
        assertEquals(3, child.getWidows()); // from child
        assertNull(child.getBreakBefore()); // still null
    }

    @Test
    void shouldSetAndGetAllProperties() {
        SectionStyleProperties style = new SectionStyleProperties();

        style.setSectionMarker("•");
        style.setKeepTogether(true);
        style.setBreakBefore(PageBreakVariant.ODD_PAGE);
        style.setBreakAfter(PageBreakVariant.AUTO);
        style.setKeepWithNext(false);
        style.setOrphans(4);
        style.setWidows(4);

        assertEquals("•", style.getSectionMarker());
        assertTrue(style.getKeepTogether());
        assertEquals(PageBreakVariant.ODD_PAGE, style.getBreakBefore());
        assertEquals("auto", style.getBreakAfter().getFoValue());
        assertFalse(style.getKeepWithNext());
        assertEquals(4, style.getOrphans());
        assertEquals(4, style.getWidows());
    }
}