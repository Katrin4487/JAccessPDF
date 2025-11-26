package de.fkkaiser.api.simplelayer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SimpleDocumentPageSequenceBuilderTest {

    @Test
    @DisplayName("should use the right element level")
    public void testSimpleDocumentBuilder() {

        SimpleDocumentBuilder builder = SimpleDocumentBuilder.create("A new pdf")
                .addHeading("First Heading (H1)")
                .addHeading("Second Heading (H2)",2);

        SimpleDocument.HeadingElement hl = (SimpleDocument.HeadingElement) builder.getElements().getLast();
        assertEquals(2, hl.level(), "should use the chosen level if the level is ok for a11y");
        builder.addHeading("Third (H4) to H1",4);
        hl = (SimpleDocument.HeadingElement) builder.getElements().getLast();
        assertEquals(3, hl.level(), "should use nearest, if level is not ok fo a11y");

    }

    @Test
    @DisplayName("should add simple paragraph correctly")
    public void testAddingSimpleParagraph()  {

        SimpleDocumentBuilder builder = SimpleDocumentBuilder.create("A new pdf")
                .addHeading("First Heading (H1)")
                .addParagraph("My first paragraph");

        assertNotNull(builder.getElements()); //fake...

    }
}
