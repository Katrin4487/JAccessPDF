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
 *
 * @author Katrin Kaiser
 * @version 1.2.0
 */
@Internal
public class SectionFoGenerator extends BlockElementFoGenerator {

    @Internal
    public SectionFoGenerator(XslFoGenerator mainGenerator) {
        super(mainGenerator);
    }

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

        // Section marker
        if (style != null && style.getSectionMarker() != null && !style.getSectionMarker().isEmpty()) {
            builder.append("<fo:inline>");
            builder.append(GenerateUtils.escapeXml(style.getSectionMarker()));
            builder.append(" </fo:inline>");
        }

        mainGenerator.generateBlockElements(section.getElements(), styleSheet, builder, headlines, resolver, isExternalArtefact);

        builder.append("      </fo:block>");
    }

    private void appendSectionAttributes(StringBuilder builder, Section section,
                                         SectionStyleProperties style, StyleSheet styleSheet) {
        // PDF/UA role
        String role = section.getVariant() != null
                ? section.getVariant().getPdfRole()
                : SectionVariant.SECTION.getPdfRole();
        builder.append(" role=\"").append(role).append("\"");

        // Alt-text
        if (section.getAltText() != null && !section.getAltText().isEmpty()) {
            builder.append(" fox:alt-text=\"")
                    .append(GenerateUtils.escapeXml(section.getAltText()))
                    .append("\"");
        }

        if (style == null) return;

        // Section-specific properties
        appendSectionSpecificAttributes(builder, style);

        // Common block attributes (from parent class)
        appendBlockAttributes(builder, style, styleSheet);
    }

    private void appendSectionSpecificAttributes(StringBuilder builder, SectionStyleProperties style) {
        // Keep together
        if (Boolean.TRUE.equals(style.getKeepTogether())) {
            builder.append(" keep-together.within-page=\"always\"");
        }

        // Orphans and widows
        if (style.getOrphans() != null) {
            builder.append(" orphans=\"").append(style.getOrphans()).append("\"");
        }

        if (style.getWidows() != null) {
            builder.append(" widows=\"").append(style.getWidows()).append("\"");
        }
    }
}