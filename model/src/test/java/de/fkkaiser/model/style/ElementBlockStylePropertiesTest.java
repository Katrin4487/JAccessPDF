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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ElementBlockStylePropertiesTest {


    private ElementBlockStyleProperties specificStyle;
    private ElementBlockStyleProperties baseStyle;

    @BeforeEach
    void setUp() {
        // Create fresh instances for each test to ensure isolation
        specificStyle = new ElementBlockStyleProperties();
        baseStyle = new ElementBlockStyleProperties();
    }

    @Nested
    @DisplayName("mergeWith Tests")
    class MergeWithTests {

        @Test
        @DisplayName("Should inherit property if specific is null")
        void shouldInheritNullProperties() {

            specificStyle.setSpaceBefore(null);
            baseStyle.setSpaceBefore("10pt");

            // Action
            specificStyle.mergeWith(baseStyle);

            // Assertion: The value should be inherited from the base style
            assertEquals("10pt", specificStyle.getSpaceBefore());
        }

        @Test
        @DisplayName("Should NOT override existing property")
        void shouldNotOverrideExistingProperties() {
            specificStyle.setSpaceAfter("20pt");
            baseStyle.setSpaceAfter("12pt");

            specificStyle.mergeWith(baseStyle);
            assertEquals("20pt", specificStyle.getSpaceAfter());
        }

        @Test
        @DisplayName("Should correctly merge boolean properties")
        void shouldMergeBooleanProperties() {
            specificStyle.setKeepWithNext(null);
            baseStyle.setKeepWithNext(true);
            specificStyle.mergeWith(baseStyle);
            assertTrue(specificStyle.getKeepWithNext());
        }

        @Test
        @DisplayName("Should remain null if both properties are null")
        void shouldRemainNullWhenBothAreNull() {
            specificStyle.setBorder(null);
            baseStyle.setBorder(null);
            specificStyle.mergeWith(baseStyle);
            assertNull(specificStyle.getBorder());
        }

        @Test
        @DisplayName("Should not merge from incompatible type")
        void shouldNotMergeFromIncompatibleType() {
            ElementStyleProperties incompatibleBase = new ElementStyleProperties() {
                @Override
                public void mergeWith(ElementStyleProperties elemBase) {

                }

                @Override
                public ElementStyleProperties copy() {
                    return null;
                }
                // Anonymous empty class
            };
            specificStyle.setPadding("5px");

            specificStyle.mergeWith(incompatibleBase);

            assertEquals("5px", specificStyle.getPadding());
        }
    }

    @Test
    @DisplayName("applyPropertiesTo should copy all properties to target")
    void shouldApplyAllPropertiesToTarget() {
        specificStyle.setSpaceBefore("10pt");
        specificStyle.setPadding("5px");
        specificStyle.setBorder("1px solid green");
        specificStyle.setKeepWithNext(true);

        specificStyle.applyPropertiesTo(baseStyle);

        assertEquals("10pt", baseStyle.getSpaceBefore());
        assertEquals("5px", baseStyle.getPadding());
        assertEquals("1px solid green", baseStyle.getBorder());
        assertTrue(baseStyle.getKeepWithNext());
    }

    @Test
    @DisplayName("copy should create a new instance with identical values")
    void shouldCreateDeepCopy() {
        specificStyle.setSpaceBefore("15pt");
        specificStyle.setBackgroundColor("#efefef");

        ElementBlockStyleProperties copiedStyle = (ElementBlockStyleProperties) specificStyle.copy();

        assertNotSame(specificStyle, copiedStyle, "Copy should be a new instance.");

        assertEquals("15pt", copiedStyle.getSpaceBefore());
        assertEquals("#efefef", copiedStyle.getBackgroundColor());

        copiedStyle.setSpaceBefore("99pt");
        assertEquals("15pt", specificStyle.getSpaceBefore(), "Original should not be modified.");
    }

}