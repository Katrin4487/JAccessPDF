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
    @DisplayName("mergeWith Tests - Annotation-Based Inheritance")
    class MergeWithTests {

        @Nested
        @DisplayName("Inheritable Properties (@Inheritable)")
        class InheritablePropertiesTests {

            @Test
            @DisplayName("Should inherit backgroundColor if specific is null")
            void shouldInheritBackgroundColor() {
                // backgroundColor has @Inheritable annotation
                specificStyle.setBackgroundColor(null);
                baseStyle.setBackgroundColor("#ff0000");

                specificStyle.mergeWith(baseStyle);

                assertEquals("#ff0000", specificStyle.getBackgroundColor(),
                        "backgroundColor should be inherited (has @Inheritable)");
            }

            @Test
            @DisplayName("Should NOT override existing backgroundColor")
            void shouldNotOverrideExistingBackgroundColor() {
                specificStyle.setBackgroundColor("#00ff00");
                baseStyle.setBackgroundColor("#ff0000");

                specificStyle.mergeWith(baseStyle);

                assertEquals("#00ff00", specificStyle.getBackgroundColor(),
                        "Existing value should not be overridden");
            }
        }

        @Nested
        @DisplayName("Non-Inheritable Properties (no @Inheritable)")
        class NonInheritablePropertiesTests {

            @Test
            @DisplayName("Should NOT inherit spaceBefore (no @Inheritable)")
            void shouldNotInheritSpaceBefore() {
                // spaceBefore has NO @Inheritable annotation
                specificStyle.setSpaceBefore(null);
                baseStyle.setSpaceBefore("10pt");

                specificStyle.mergeWith(baseStyle);

                assertNull(specificStyle.getSpaceBefore(),
                        "spaceBefore should NOT be inherited (no @Inheritable annotation)");
            }

            @Test
            @DisplayName("Should NOT inherit spaceAfter (no @Inheritable)")
            void shouldNotInheritSpaceAfter() {
                specificStyle.setSpaceAfter(null);
                baseStyle.setSpaceAfter("20pt");

                specificStyle.mergeWith(baseStyle);

                assertNull(specificStyle.getSpaceAfter(),
                        "spaceAfter should NOT be inherited (no @Inheritable annotation)");
            }

            @Test
            @DisplayName("Should NOT inherit padding properties (no @Inheritable)")
            void shouldNotInheritPadding() {
                specificStyle.setPadding(null);
                specificStyle.setPaddingLeft(null);
                baseStyle.setPadding("10pt");
                baseStyle.setPaddingLeft("5pt");

                specificStyle.mergeWith(baseStyle);

                assertNull(specificStyle.getPadding(),
                        "padding should NOT be inherited (no @Inheritable annotation)");
                assertNull(specificStyle.getPaddingLeft(),
                        "paddingLeft should NOT be inherited (no @Inheritable annotation)");
            }

            @Test
            @DisplayName("Should NOT inherit border properties (no @Inheritable)")
            void shouldNotInheritBorder() {
                specificStyle.setBorder(null);
                baseStyle.setBorder("1px solid black");

                specificStyle.mergeWith(baseStyle);

                assertNull(specificStyle.getBorder(),
                        "border should NOT be inherited (no @Inheritable annotation)");
            }

            @Test
            @DisplayName("Should NOT inherit layout control properties (no @Inheritable)")
            void shouldNotInheritLayoutControls() {
                specificStyle.setKeepWithNext(null);
                specificStyle.setBreakBefore(null);
                specificStyle.setBreakAfter(null);

                baseStyle.setKeepWithNext(true);
                baseStyle.setBreakBefore(PageBreakVariant.PAGE);
                baseStyle.setBreakAfter(PageBreakVariant.COLUMN);

                specificStyle.mergeWith(baseStyle);

                assertNull(specificStyle.getKeepWithNext(),
                        "keepWithNext should NOT be inherited (no @Inheritable annotation)");
                assertNull(specificStyle.getBreakBefore(),
                        "breakBefore should NOT be inherited (no @Inheritable annotation)");
                assertNull(specificStyle.getBreakAfter(),
                        "breakAfter should NOT be inherited (no @Inheritable annotation)");
            }

            @Test
            @DisplayName("Should NOT inherit indent properties (no @Inheritable)")
            void shouldNotInheritIndent() {
                specificStyle.setStartIndent(null);
                specificStyle.setEndIndent(null);
                baseStyle.setStartIndent("2em");
                baseStyle.setEndIndent("1em");

                specificStyle.mergeWith(baseStyle);

                assertNull(specificStyle.getStartIndent(),
                        "startIndent should NOT be inherited (no @Inheritable annotation)");
                assertNull(specificStyle.getEndIndent(),
                        "endIndent should NOT be inherited (no @Inheritable annotation)");
            }
        }

        @Test
        @DisplayName("Should remain null if both properties are null")
        void shouldRemainNullWhenBothAreNull() {
            specificStyle.setBackgroundColor(null);
            baseStyle.setBackgroundColor(null);

            specificStyle.mergeWith(baseStyle);

            assertNull(specificStyle.getBackgroundColor());
        }

        @Test
        @DisplayName("Should not merge from incompatible type")
        void shouldNotMergeFromIncompatibleType() {
            // Create incompatible type
            ElementStyleProperties incompatibleBase = new ElementStyleProperties() {
                @Override
                public ElementStyleProperties copy() {
                    return null;
                }
            };

            specificStyle.setBackgroundColor("#123456");

            // Merge should be skipped for incompatible types
            specificStyle.mergeWith(incompatibleBase);

            // Original value should remain unchanged
            assertEquals("#123456", specificStyle.getBackgroundColor());
        }

        @Test
        @DisplayName("Should handle null base style gracefully")
        void shouldHandleNullBaseStyle() {
            specificStyle.setBackgroundColor("#abcdef");

            // Should not throw exception
            assertDoesNotThrow(() -> specificStyle.mergeWith(null));

            // Original value should remain unchanged
            assertEquals("#abcdef", specificStyle.getBackgroundColor());
        }
    }

    @Nested
    @DisplayName("Copy Functionality Tests")
    class CopyTests {

        @Test
        @DisplayName("applyPropertiesTo should copy all properties to target")
        void shouldApplyAllPropertiesToTarget() {
            specificStyle.setSpaceBefore("10pt");
            specificStyle.setPadding("5px");
            specificStyle.setBorder("1px solid green");
            specificStyle.setKeepWithNext(true);
            specificStyle.setBackgroundColor("#efefef");
            specificStyle.setBreakBefore(PageBreakVariant.PAGE);

            specificStyle.applyPropertiesTo(baseStyle);

            assertEquals("10pt", baseStyle.getSpaceBefore());
            assertEquals("5px", baseStyle.getPadding());
            assertEquals("1px solid green", baseStyle.getBorder());
            assertTrue(baseStyle.getKeepWithNext());
            assertEquals("#efefef", baseStyle.getBackgroundColor());
            assertEquals(PageBreakVariant.PAGE, baseStyle.getBreakBefore());
        }

        @Test
        @DisplayName("copy should create a new instance with identical values")
        void shouldCreateDeepCopy() {
            specificStyle.setSpaceBefore("15pt");
            specificStyle.setBackgroundColor("#efefef");
            specificStyle.setKeepWithNext(true);
            specificStyle.setBorder("2px solid blue");

            ElementBlockStyleProperties copiedStyle = specificStyle.copy();

            // Verify it's a different instance
            assertNotSame(specificStyle, copiedStyle,
                    "Copy should be a new instance");

            // Verify all values are copied
            assertEquals("15pt", copiedStyle.getSpaceBefore());
            assertEquals("#efefef", copiedStyle.getBackgroundColor());
            assertTrue(copiedStyle.getKeepWithNext());
            assertEquals("2px solid blue", copiedStyle.getBorder());

            // Verify deep copy (modifications don't affect original)
            copiedStyle.setSpaceBefore("99pt");
            copiedStyle.setBackgroundColor("#ffffff");

            assertEquals("15pt", specificStyle.getSpaceBefore(),
                    "Original spaceBefore should not be modified");
            assertEquals("#efefef", specificStyle.getBackgroundColor(),
                    "Original backgroundColor should not be modified");
        }

        @Test
        @DisplayName("copy should handle null properties correctly")
        void shouldCopyWithNullProperties() {
            // Set only some properties, leave others null
            specificStyle.setSpaceBefore("10pt");
            // backgroundColor, padding, etc. remain null

            ElementBlockStyleProperties copiedStyle = specificStyle.copy();

            assertEquals("10pt", copiedStyle.getSpaceBefore());
            assertNull(copiedStyle.getBackgroundColor());
            assertNull(copiedStyle.getPadding());
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("validate should return empty list for valid properties")
        void shouldReturnEmptyValidationList() {
            specificStyle.setSpaceBefore("10pt");
            specificStyle.setBackgroundColor("#ff0000");

            var validationErrors = specificStyle.validate();

            assertNotNull(validationErrors);
            assertTrue(validationErrors.isEmpty());
        }
    }

    @Nested
    @DisplayName("Dimension Validation Tests")
    class DimensionValidationTests {

        @Test
        @DisplayName("Should accept valid dimension values")
        void shouldAcceptValidDimensions() {
            assertDoesNotThrow(() -> {
                specificStyle.setSpaceBefore("10pt");
                specificStyle.setSpaceAfter("2cm");
                specificStyle.setPadding("5mm");
                specificStyle.setStartIndent("1in");
            });

            assertEquals("10pt", specificStyle.getSpaceBefore());
            assertEquals("2cm", specificStyle.getSpaceAfter());
            assertEquals("5mm", specificStyle.getPadding());
            assertEquals("1in", specificStyle.getStartIndent());
        }

        @Test
        @DisplayName("Should handle null dimension values")
        void shouldHandleNullDimensions() {
            specificStyle.setSpaceBefore(null);
            specificStyle.setPadding(null);

            assertNull(specificStyle.getSpaceBefore());
            assertNull(specificStyle.getPadding());
        }
    }

    @Nested
    @DisplayName("Border Validation Tests")
    class BorderValidationTests {

        @Test
        @DisplayName("Should accept valid border values")
        void shouldAcceptValidBorders() {
            specificStyle.setBorder("1px solid black");
            specificStyle.setBorderTop("2pt dashed red");

            assertEquals("1px solid black", specificStyle.getBorder());
            assertEquals("2pt dashed red", specificStyle.getBorderTop());
        }

        @Test
        @DisplayName("Should reject invalid border and set to null")
        void shouldRejectInvalidBorder() {
            specificStyle.setBorder("invalid-border");

            assertNull(specificStyle.getBorder(),
                    "Invalid border should be set to null");
        }

        @Test
        @DisplayName("Should handle null border values")
        void shouldHandleNullBorders() {
            specificStyle.setBorder(null);
            specificStyle.setBorderLeft(null);

            assertNull(specificStyle.getBorder());
            assertNull(specificStyle.getBorderLeft());
        }
    }
}