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
import de.fkkaiser.generator.XslFoGenerator;
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.structure.Element;
import de.fkkaiser.model.structure.Headline;
import de.fkkaiser.model.structure.Section;
import de.fkkaiser.model.structure.SectionVariant;
import de.fkkaiser.model.style.SectionStyleProperties;
import de.fkkaiser.model.style.StyleSheet;
import java.util.List;

/**
 * Generates the XSL-FO structure for a Section element.
 * A Section is rendered as a fo:block with appropriate PDF/UA
 * role based on its variant.
 *
 * @author Katrin Kaiser
 * @version 1.1.0
 */
@Internal
public class SectionFoGenerator extends ElementFoGenerator {

    private final XslFoGenerator mainGenerator;

    /**
     * Constructor for SectionFoGenerator.
     *
     * @param mainGenerator The main generator used for delegating content generation.
     */
    @Internal
    public SectionFoGenerator(XslFoGenerator mainGenerator) {
        this.mainGenerator = mainGenerator;
    }

    /**
     * Generates the XSL-FO string for a section element.
     *
     * @param element    The section element to be processed.
     * @param styleSheet The entire StyleSheet for accessing style information.
     * @param builder    The StringBuilder to which the generated string is appended.
     * @param headlines  The list of headlines for bookmark generation.
     */
    @Override
    public void generate(Element element,
                         StyleSheet styleSheet,
                         StringBuilder builder,
                         List<Headline> headlines,
                         ImageResolver resolver,
                         boolean isExternalArtefact) {
        Section section = (Section) element;
        SectionStyleProperties style = section.getResolvedStyle();

        builder.append("      <fo:block");
        appendSectionAttributes(builder, section, style, styleSheet);
        builder.append(">");

        // Add section marker if specified
        if (style != null && style.getSectionMarker() != null && !style.getSectionMarker().isEmpty()) {
            builder.append("<fo:inline");
            // Optional: Style the marker differently
            builder.append(" font-weight=\"bold\"");
            builder.append(">");
            builder.append(GenerateUtils.escapeXml(style.getSectionMarker()));
            builder.append(" </fo:inline>");
        }

        // Generate child elements
        mainGenerator.generateBlockElements(section.getElements(), styleSheet, builder, headlines, resolver, isExternalArtefact);

        builder.append("      </fo:block>");
    }

    /**
     * Appends section-specific attributes to the fo:block tag.
     *
     * @param builder    The StringBuilder to append to.
     * @param section    The section element.
     * @param style      The resolved style properties for the section.
     * @param styleSheet The stylesheet for font lookups.
     */
    private void appendSectionAttributes(StringBuilder builder, Section section,
                                         SectionStyleProperties style, StyleSheet styleSheet) {
        // Determine PDF/UA role based on variant
        String role = section.getVariant() != null
                ? section.getVariant().getPdfRole()
                : SectionVariant.SECTION.getPdfRole();

        builder.append(" role=\"").append(role).append("\"");

        // Add alt-text if provided
        if (section.getAltText() != null && !section.getAltText().isEmpty()) {
            builder.append(" fox:alt-text=\"")
                    .append(GenerateUtils.escapeXml(section.getAltText()))
                    .append("\"");
        }

        // Add style attributes
        if (style == null) return;

        // Section-specific properties
        if (Boolean.TRUE.equals(style.getKeepTogether())) {
            builder.append(" keep-together.within-page=\"always\"");
        }

        if (style.getBreakBefore() != null) {
            builder.append(" break-before=\"")
                    .append(GenerateUtils.escapeXml(style.getBreakBefore().getFoValue()))
                    .append("\"");
        }

        if (style.getBreakAfter() != null && !style.getBreakAfter().equals("auto")) {
            builder.append(" break-after=\"")
                    .append(GenerateUtils.escapeXml(style.getBreakAfter().getFoValue()))
                    .append("\"");
        }

        if (Boolean.TRUE.equals(style.getKeepWithNext())) {
            builder.append(" keep-with-next.within-page=\"always\"");
        }

        if (style.getOrphans() != null) {
            builder.append(" orphans=\"")
                    .append(style.getOrphans())
                    .append("\"");
        }

        if (style.getWidows() != null) {
            builder.append(" widows=\"")
                    .append(style.getWidows())
                    .append("\"");
        }

        // Font style
        setFontStyle(styleSheet, style, builder);

        // Layout properties
        if (style.getPadding() != null) {
            builder.append(" padding=\"").append(GenerateUtils.escapeXml(style.getPadding())).append("\"");
        }
        if (style.getPaddingBottom() != null) {
            builder.append(" padding-bottom=\"").append(GenerateUtils.escapeXml(style.getPaddingBottom())).append("\"");
        }
        if (style.getBorder() != null) {
            builder.append(" border=\"").append(GenerateUtils.escapeXml(style.getBorder())).append("\"");
        }
        if (style.getBackgroundColor() != null) {
            builder.append(" background-color=\"").append(GenerateUtils.escapeXml(style.getBackgroundColor())).append("\"");
        }
        if (style.getStartIndent() != null) {
            builder.append(" start-indent=\"").append(GenerateUtils.escapeXml(style.getStartIndent())).append("\"");
        }
        if (style.getEndIndent() != null) {
            builder.append(" end-indent=\"").append(GenerateUtils.escapeXml(style.getEndIndent())).append("\"");
        }
        if (style.getSpaceBefore() != null) {
            builder.append(" space-before=\"").append(GenerateUtils.escapeXml(style.getSpaceBefore())).append("\"");
        }
        if (style.getSpaceAfter() != null) {
            builder.append(" space-after=\"").append(GenerateUtils.escapeXml(style.getSpaceAfter())).append("\"");
        }
    }
}