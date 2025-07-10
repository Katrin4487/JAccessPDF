package de.kaiser.model.style;

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
    @DisplayName("mergeWith Logic")
    class MergeWithTests {

        @Test
        @DisplayName("Should inherit background color from a compatible BlockStyle")
        void shouldInheritFromCompatibleType() {
            specificStyle.setBackgroundColor(null);
            ElementBlockStyleProperties baseStyle = new ElementBlockStyleProperties();
            baseStyle.setBackgroundColor("#FF0000");
            specificStyle.mergeWith(baseStyle);
            assertEquals("#FF0000", specificStyle.getBackgroundColor(),"Background color should match");
        }

        @Test
        @DisplayName("Should NOT override existing background color")
        void shouldNotOverrideExistingValue() {

            specificStyle.setBackgroundColor("#00FF00");
            ElementBlockStyleProperties baseStyle = new ElementBlockStyleProperties();
            baseStyle.setBackgroundColor("#FF0000");
            specificStyle.mergeWith(baseStyle);
            assertEquals("#00FF00", specificStyle.getBackgroundColor(),"Specific value should remain unchanged");
        }

        @Test
        @DisplayName("Should skip merge from an incompatible style type")
        void shouldSkipMergeFromIncompatibleType() {
            ElementStyleProperties incompatibleBase = new ElementStyleProperties() {
                @Override
                public void mergeWith(ElementStyleProperties elemBase) {
                }

                @Override
                public ElementStyleProperties copy() {
                    return null;
                }
            };
            specificStyle.setBackgroundColor(null);

            specificStyle.mergeWith(incompatibleBase);

            assertNull(specificStyle.getBackgroundColor(),"Background color should still be null");
        }
    }

    @Test
    @DisplayName("copy() should create a new instance with identical values")
    void copyShouldCreateNewInstanceWithIdenticalValues() {

        specificStyle.setBackgroundColor("#123456");

        InlineElementStyleProperties copiedStyle = (InlineElementStyleProperties) specificStyle.copy();

        assertNotSame(specificStyle, copiedStyle, "Copy should be a new instance.");

        assertEquals("#123456", copiedStyle.getBackgroundColor());

        copiedStyle.setBackgroundColor("#FFFFFF");
        assertEquals("#123456", specificStyle.getBackgroundColor(), "Original should not be modified.");
    }
}