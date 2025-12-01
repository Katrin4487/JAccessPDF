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

/**
 * Unit tests for the InlineElementStyleProperties class.
 */
class InlineElementStylePropertiesTest {

    private InlineElementStyleProperties specificStyle;

    @BeforeEach
    void setUp() {
        // Create a fresh instance for each test
        specificStyle = new InlineElementStyleProperties();
    }

    @Nested
    @DisplayName("mergeWith Logic - Annotation-Based Inheritance")
    class MergeWithTests {

        @Test
        @DisplayName("Should inherit backgroundColor from compatible InlineElementStyleProperties (same type)")
        void shouldInheritFromSameType() {
            // backgroundColor has @Inheritable annotation
            specificStyle.setBackgroundColor(null);

            InlineElementStyleProperties baseStyle = new InlineElementStyleProperties();
            baseStyle.setBackgroundColor("#FF0000");

            specificStyle.mergeWith(baseStyle);

            assertEquals("#FF0000", specificStyle.getBackgroundColor(),
                    "backgroundColor should be inherited from same type (has @Inheritable)");
        }

        @Test
        @DisplayName("Should NOT inherit from incompatible type (ElementBlockStyleProperties)")
        void shouldNotInheritFromDifferentType() {
            // mergeWith() only works with same class type
            specificStyle.setBackgroundColor(null);

            ElementBlockStyleProperties blockBaseStyle = new ElementBlockStyleProperties();
            blockBaseStyle.setBackgroundColor("#FF0000");

            specificStyle.mergeWith(blockBaseStyle);

            assertNull(specificStyle.getBackgroundColor(),
                    "Should NOT inherit from different type (class mismatch check)");
        }

        @Test
        @DisplayName("Should NOT override existing backgroundColor")
        void shouldNotOverrideExistingValue() {
            specificStyle.setBackgroundColor("#00FF00");

            InlineElementStyleProperties baseStyle = new InlineElementStyleProperties();
            baseStyle.setBackgroundColor("#FF0000");

            specificStyle.mergeWith(baseStyle);

            assertEquals("#00FF00", specificStyle.getBackgroundColor(),
                    "Existing value should not be overridden");
        }

        @Test
        @DisplayName("Should skip merge from an incompatible style type")
        void shouldSkipMergeFromIncompatibleType() {
            // Create a completely different ElementStyleProperties type
            ElementStyleProperties incompatibleBase = new ElementStyleProperties() {
                @Override
                public ElementStyleProperties copy() {
                    return null;
                }
            };

            specificStyle.setBackgroundColor(null);

            specificStyle.mergeWith(incompatibleBase);

            assertNull(specificStyle.getBackgroundColor(),
                    "backgroundColor should still be null (incompatible type)");
        }

        @Test
        @DisplayName("Should handle null base style gracefully")
        void shouldHandleNullBaseStyle() {
            specificStyle.setBackgroundColor("#123456");

            // Should not throw exception
            assertDoesNotThrow(() -> specificStyle.mergeWith(null));

            // Original value should remain unchanged
            assertEquals("#123456", specificStyle.getBackgroundColor());
        }

        @Test
        @DisplayName("Should remain null when both properties are null")
        void shouldRemainNullWhenBothAreNull() {
            specificStyle.setBackgroundColor(null);

            InlineElementStyleProperties baseStyle = new InlineElementStyleProperties();
            baseStyle.setBackgroundColor(null);

            specificStyle.mergeWith(baseStyle);

            assertNull(specificStyle.getBackgroundColor());
        }
    }

    @Nested
    @DisplayName("Copy Functionality Tests")
    class CopyTests {

        @Test
        @DisplayName("copy() should create a new instance with identical values")
        void copyShouldCreateNewInstanceWithIdenticalValues() {
            specificStyle.setBackgroundColor("#123456");

            InlineElementStyleProperties copiedStyle =
                    (InlineElementStyleProperties) specificStyle.copy();

            // Verify it's a different instance
            assertNotSame(specificStyle, copiedStyle,
                    "Copy should be a new instance");

            // Verify value is copied
            assertEquals("#123456", copiedStyle.getBackgroundColor());

            // Verify deep copy (modifications don't affect original)
            copiedStyle.setBackgroundColor("#FFFFFF");
            assertEquals("#123456", specificStyle.getBackgroundColor(),
                    "Original should not be modified");
        }

        @Test
        @DisplayName("copy() should handle null properties correctly")
        void copyShouldHandleNullProperties() {
            // Leave backgroundColor as null
            specificStyle.setBackgroundColor(null);

            InlineElementStyleProperties copiedStyle =
                    (InlineElementStyleProperties) specificStyle.copy();

            assertNull(copiedStyle.getBackgroundColor());
        }

        @Test
        @DisplayName("applyPropertiesTo() should copy all properties")
        void applyPropertiesToShouldCopyAllProperties() {
            specificStyle.setBackgroundColor("#ABCDEF");

            InlineElementStyleProperties target = new InlineElementStyleProperties();
            specificStyle.applyPropertiesTo(target);

            assertEquals("#ABCDEF", target.getBackgroundColor());
        }
    }

    @Nested
    @DisplayName("Property Setter/Getter Tests")
    class PropertyTests {

        @Test
        @DisplayName("Should set and get backgroundColor correctly")
        void shouldSetAndGetBackgroundColor() {
            specificStyle.setBackgroundColor("#FF5733");
            assertEquals("#FF5733", specificStyle.getBackgroundColor());
        }

        @Test
        @DisplayName("Should handle null backgroundColor")
        void shouldHandleNullBackgroundColor() {
            specificStyle.setBackgroundColor(null);
            assertNull(specificStyle.getBackgroundColor());
        }

        @Test
        @DisplayName("Should overwrite backgroundColor with new value")
        void shouldOverwriteBackgroundColor() {
            specificStyle.setBackgroundColor("#000000");
            assertEquals("#000000", specificStyle.getBackgroundColor());

            specificStyle.setBackgroundColor("#FFFFFF");
            assertEquals("#FFFFFF", specificStyle.getBackgroundColor());
        }
    }
}