package de.fkkaiser.model.structure;

import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;

/**
 * Enumeration representing the variants of a Part element.
 */
@PublicAPI
public enum PartVariant {
    PART("Part"),
    ARTICLE("Article");

    private final String pdfRole;

    /**
     * Constructor for PartVariant.
     * @param pdfRole The corresponding PDF/UA role for the variant.
     */
    PartVariant(String pdfRole) {
        this.pdfRole = pdfRole;
    }

    /**
     * Returns the corresponding PDF/UA structure role.
     * @return the PDF/UA role name (e.g., "Part", "Article")
     */
     @Internal
    public String getPdfRole() {
        return pdfRole;
    }

    /**
     * Returns the lowercase variant name for style resolution.
     * For example: PART → "part", ARTICLE → "article"
     * @return the lowercase variant name
     */
    public String getStyleName() {
        return name().toLowerCase();
    }

}
