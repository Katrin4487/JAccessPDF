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
package de.fkkaiser.generator.element;

import de.fkkaiser.generator.GenerateUtils;
import de.fkkaiser.generator.ImageResolver;
import de.fkkaiser.generator.TagBuilder;
import de.fkkaiser.generator.XslFoGenerator;
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.structure.Element;
import de.fkkaiser.model.structure.Headline;
import de.fkkaiser.model.structure.Section;
import de.fkkaiser.model.structure.SectionVariant;
import de.fkkaiser.model.style.SectionStyleProperties;
import de.fkkaiser.model.style.StyleSheet;
import de.fkkaiser.model.style.TextStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Generates the XSL-FO structure for a Section element.
 * <p>
 * This class is responsible for creating the XSL-FO representation of a Section,
 * including its attributes, styles, and child elements.
 * It extends the functionality of `BlockElementFoGenerator` to handle section-specific
 * properties and behaviors.
 *
 * @author Katrin Kaiser
 * @version 1.2.1
 */
@Internal
public class SectionFoGenerator extends BlockElementFoGenerator {

    private static final Logger log = LoggerFactory.getLogger(SectionFoGenerator.class);

    /**
     * Constructs a new `SectionFoGenerator` instance.
     *
     * @param mainGenerator the main `XslFoGenerator` instance used for generating block elements
     */
    @Internal
    public SectionFoGenerator(XslFoGenerator mainGenerator) {
        super(mainGenerator);
    }

    /**
     * Generates the XSL-FO structure for the given `Section` element.
     * <p>
     * This method appends the XSL-FO representation of the section to the provided
     * `StringBuilder`. It includes section attributes, markers, and child elements.
     *
     * @param element            the `Element` to generate (expected to be a `Section`)
     * @param styleSheet         the `StyleSheet` containing style definitions
     * @param builder            the `StringBuilder` to append the generated XSL-FO to
     * @param headlines          a list of `Headline` elements for the document
     * @param resolver           the `ImageResolver` for resolving image paths
     * @param isExternalArtefact a flag indicating if the section is part of an external artefact
     */
    @Internal
    @Override
    public void generate(Element element,
                         StyleSheet styleSheet,
                         StringBuilder builder,
                         List<Headline> headlines,
                         ImageResolver resolver,
                         boolean isExternalArtefact) {
        Section section = (Section) element;
        SectionStyleProperties style = section.getResolvedStyle();

        TagBuilder blockBuilder = GenerateUtils.tagBuilder("block");
        appendSectionAttributes(blockBuilder, section, style, styleSheet);

        // Build the section content
        StringBuilder content = new StringBuilder();

        // Section marker (only if style is not null)
        if (style != null &&
                style.getSectionMarker() != null &&
                style.getTextStyleName() != null &&
                !style.getSectionMarker().isEmpty()) {

            Optional<TextStyle> textStyleOpt = styleSheet.findFontStyleByName(style.getTextStyleName());

            TagBuilder inlineBuilder = GenerateUtils.tagBuilder("inline");
            textStyleOpt.ifPresent(ts -> GenerateUtils.appendTextStyleTags(inlineBuilder, ts));
            inlineBuilder.addContent(style.getSectionMarker() + " ");
            inlineBuilder.buildInto(content);
        }

        // Generate child elements
        mainGenerator.generateBlockElements(section.getElements(), styleSheet, content, headlines, resolver, isExternalArtefact);

        blockBuilder.addNestedContent(content.toString());
        blockBuilder.buildInto(builder);
    }

    /**
     * Appends section-specific attributes to the block builder.
     *
     * @param builder    the TagBuilder to add attributes to
     * @param section    the section element
     * @param style      the section style properties
     * @param styleSheet the stylesheet
     */
    private void appendSectionAttributes(TagBuilder builder, Section section,
                                         SectionStyleProperties style, StyleSheet styleSheet) {
        // PDF/UA role
        String role = section.getVariant() != null
                ? section.getVariant().getPdfRole()
                : SectionVariant.SECTION.getPdfRole();

        if (role.equals(SectionVariant.NOTE.getPdfRole())) {
            // Needs unique ID for accessibility
            builder
                    .addAttribute("role", "Div")
                    .addAttribute("id", "note-" + UUID.randomUUID());

            log.warn("Section with variant NOTE detected. Note is not correctly written in Structure Tree with FOP. Using DIV instead");
        } else {
            builder.addAttribute("role", role);
        }

        // Alt-text
        builder.addAttribute("fox:alt-text", section.getAltText());

        if (style == null) return;

        // Section-specific properties
        appendSectionSpecificAttributes(builder, style);

        // Common block attributes (from parent class)
        appendBlockAttributes(builder, style, styleSheet);
    }

    /**
     * Appends section-specific style attributes.
     *
     * @param builder the TagBuilder to add attributes to
     * @param style   the section style properties
     */
    private void appendSectionSpecificAttributes(TagBuilder builder, SectionStyleProperties style) {
        // Keep together
        if (Boolean.TRUE.equals(style.getKeepTogether())) {
            builder.addAttribute("keep-together.within-page", "always");
        }

        // Orphans and widows
        if (style.getOrphans() != null) {
            builder.addAttribute("orphans", String.valueOf(style.getOrphans()));
        }

        if (style.getWidows() != null) {
            builder.addAttribute("widows", String.valueOf(style.getWidows()));
        }
    }
}