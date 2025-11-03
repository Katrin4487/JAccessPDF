package de.fkkaiser.generator.element;

import de.fkkaiser.generator.ImageResolver;
import de.fkkaiser.generator.XslFoGenerator;
import de.fkkaiser.model.structure.*;
import de.fkkaiser.model.style.ListStyleProperties;
import de.fkkaiser.model.style.StyleSheet;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ListFoGeneratorTest {

    /**
     * Tests the generation of an ordered list with default labels.
     */
    @Test
    void testGenerateOrderedListWithDefaultLabels() {
        // Prepare test data
        SimpleList list = new SimpleList(
                "test-style",
                ListOrdering.ORDERED,
                List.of(new ListItem(null, null,null,new ArrayList<>()), new ListItem(null, null,null,new ArrayList<>()))
        );
        ListStyleProperties styleProperties = new ListStyleProperties();
        list.setResolvedStyle(styleProperties);

        StyleSheet styleSheet = StyleSheet.builder().build();
        StringBuilder builder = new StringBuilder();
        XslFoGenerator mockGenerator = new MockXslFoGenerator();
        ImageResolver mockResolver = relativePath -> new URL("file://test");
        ListFoGenerator generator = new ListFoGenerator(mockGenerator);

        // Execute the method
        generator.generate(list, styleSheet, builder, Collections.emptyList(), mockResolver, false);

        // Assert the output
        String output = builder.toString();
        assertTrue(output.contains("<fo:list-block"), "Generated content must contain list-block tag");
        assertTrue(output.contains("<fo:list-item"), "Generated content must contain list-item tag");
        assertTrue(output.contains("1."), "Generated list items should contain '1.' as default label");
        assertTrue(output.contains("2."), "Generated list items should contain '2.' as default label");
    }

    /**
     * Tests the generation of an unordered list with a custom style type.
     */
    @Test
    void testGenerateUnorderedListWithCustomStyleType() {
        // Prepare test data
        SimpleList list = new SimpleList(
                "test-style",
                ListOrdering.UNORDERED,
                List.of(new ListItem(null, null,null,new ArrayList<>()), new ListItem(null, null,null,new ArrayList<>()))
        );
        ListStyleProperties styleProperties = new ListStyleProperties();
        styleProperties.setListStyleType("circle");
        list.setResolvedStyle(styleProperties);

        StyleSheet styleSheet = StyleSheet.builder().build();
        StringBuilder builder = new StringBuilder();
        XslFoGenerator mockGenerator = new MockXslFoGenerator();
        ImageResolver mockResolver = relativePath -> new URL("file://test");
        ListFoGenerator generator = new ListFoGenerator(mockGenerator);

        // Execute the method
        generator.generate(list, styleSheet, builder, Collections.emptyList(), mockResolver, false);

        // Assert the output
        String output = builder.toString();
        assertTrue(output.contains("<fo:list-block"), "Generated content must contain list-block tag");
        assertTrue(output.contains("&#x25CB;"), "Generated list items should use 'circle' symbol");
    }

    /**
     * Tests the generation of a list with an external artifact attribute.
     */
    @Test
    void testGenerateListWithExternalArtifact() {
        // Prepare test data
        SimpleList list = new SimpleList(
                "test-style",
                ListOrdering.UNORDERED,
                List.of(new ListItem(null, null,null,new ArrayList<>()))
        );
        ListStyleProperties styleProperties = new ListStyleProperties();
        list.setResolvedStyle(styleProperties);

        StyleSheet styleSheet = StyleSheet.builder().build();
        StringBuilder builder = new StringBuilder();
        XslFoGenerator mockGenerator = new MockXslFoGenerator();
        ImageResolver mockResolver = relativePath -> new URL("file://test");
        ListFoGenerator generator = new ListFoGenerator(mockGenerator);

        // Execute the method
        generator.generate(list, styleSheet, builder, Collections.emptyList(), mockResolver, true);

        // Assert the output
        String output = builder.toString();
        assertTrue(output.contains("fox:content-type=\"external-artifact\""),
                "Generated content must contain external artifact attribute");
    }

    /**
     * Helper class: Mock implementation of XslFoGenerator.
     */
    static class MockXslFoGenerator extends XslFoGenerator {
        @Override
        public void generateInlineElement(InlineElement inlineElement, StyleSheet styleSheet, StringBuilder builder) {
            builder.append("Inline content");
        }

        @Override
        public void generateBlockElement(Element element, StyleSheet styleSheet, StringBuilder builder,
                                         List<Headline> headlines, ImageResolver resolver, boolean isExternalArtefact) {
            builder.append("Block content");
        }
    }

}