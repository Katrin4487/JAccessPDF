package de.fkkaiser.model.structure;

import de.fkkaiser.model.annotation.PublicAPI;

/**
 * Defines the semantic variants for sections that map to PDF/UA structure types.
 * Each variant determines both the visual styling and the accessibility role.
 */
@PublicAPI
public enum SectionVariant {

    /**
     * Standard section (default).
     * Maps to PDF/UA role "Sect".
     * Use for general content grouping.
     */
    SECTION("Sect"),

    /**
     * Note or important information.
     * Maps to PDF/UA role "Note".
     * Use for warnings, errors, important notices, tips.
     */
    NOTE("Note"),

    /**
     * Complementary or aside content.
     * Maps to PDF/UA role "Aside".
     * Use for examples, sidebars, supplementary information.
     */
    ASIDE("Aside");

    private final String pdfRole;

    SectionVariant(String pdfRole) {
        this.pdfRole = pdfRole;
    }

    /**
     * Returns the corresponding PDF/UA structure role.
     *
     * @return the PDF/UA role name (e.g., "Sect", "Note", "Aside")
     */
    public String getPdfRole() {
        return pdfRole;
    }

    /**
     * Returns the lowercase variant name for style resolution.
     * For example: SECTION → "section", NOTE → "note"
     *
     * @return the lowercase variant name
     */
    public String getStyleName() {
        return name().toLowerCase();
    }
}