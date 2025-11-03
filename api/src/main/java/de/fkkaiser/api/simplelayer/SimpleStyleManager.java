package de.fkkaiser.api.simplelayer;

import de.fkkaiser.model.style.*;
import de.fkkaiser.model.style.ElementStyle;
import de.fkkaiser.model.style.PageMasterStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages default styles for SimpleDocument.
 * <p>
 * This class provides a centralized style management system that creates
 * PDF/UA compliant styles using Open Sans as the default font family.
 * It handles text styles, element styles, and page master configurations.
 * </p>
 */
class SimpleStyleManager {

    private static final String PAGE_MASTER_NAME = "simple-a4-portrait";

    /**
     * Default font family used for all text styles.
     * PDF/UA requires embedded fonts for accessibility compliance.
     */
    private String fontFamily = "Open Sans";

    /**
     * Returns the name of the default page master style.
     *
     * @return the page master name constant
     */
    String getDefaultPageMasterName() {
        return PAGE_MASTER_NAME;
    }

    /**
     * Sets the font family to be used for all text styles.
     *
     * @param fontFamily the font family name (must be embedded for PDF/UA compliance)
     */
    void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    /**
     * Generates the style name for a paragraph with the given identifier.
     *
     * @param name the paragraph identifier (use "default" for default paragraph style)
     * @return the fully qualified paragraph style name
     */
    String getParagraphStyleName(String name) {
        if ("default".equals(name)) {
            return "paragraph-default";
        }
        return "paragraph-" + name;
    }

    /**
     * Generates the style name for a heading of the specified level.
     *
     * @param level the heading level (1-6)
     * @return the heading style name
     */
    String getHeadingStyleName(int level) {
        return "heading-" + level;
    }

    /**
     * Returns the style name for unordered lists.
     *
     * @return the unordered list style name
     */
    String getUnorderedListName() {
        return "list-style-unordered";
    }

    /**
     * Returns the style name for ordered lists.
     *
     * @return the ordered list style name
     */
    String getOrderedListName() {
        return "list-style-ordered";
    }

    /**
     * Returns the default style name for images.
     *
     * @return the default image style name
     */
    String getDefaultImageName() {
        return "image-default";
    }

    /**
     * Returns the default style name for table cells.
     *
     * @return the default cell style name
     */
    String getDefaultCellStyleName() {
        return "cell-default";
    }

    /**
     * Builds a complete StyleSheet with PDF/UA compliant default styles.
     * <p>
     * The stylesheet includes:
     * <ul>
     *   <li>Text styles for default text and headings (levels 1-6)</li>
     *   <li>Element styles for paragraphs, headings, lists, images, and tables</li>
     *   <li>Page master style for A4 portrait layout</li>
     * </ul>
     * All styles use the configured font family (Open Sans by default) which must
     * be embedded for PDF/UA accessibility compliance.
     * </p>
     *
     * @return a fully configured StyleSheet with all default styles
     */
    StyleSheet buildStyleSheet() {
        List<TextStyle> textStyles = new ArrayList<>();
        List<ElementStyle> elementStyles = new ArrayList<>();
        List<PageMasterStyle> pageMasterStyles = new ArrayList<>();

        // Create default text style with base font settings
        textStyles.add(new TextStyle(
                "text-default",
                "12px",
                fontFamily,
                "400",      // normal weight
                "normal"    // normal style
        ));

        // Create heading text styles with decreasing font sizes
        // Level 1: 24px, Level 2: 22px, Level 3: 20px, etc.
        for (int level = 1; level <= 6; level++) {
            int fontSize = 24 - (level - 1) * 2;
            textStyles.add(new TextStyle(
                    "text-heading-" + level,
                    fontSize + "px",
                    fontFamily,
                    "700",      // bold weight
                    "normal"
            ));
        }

        // Default paragraph element style
        ParagraphStyleProperties defaultProps = new ParagraphStyleProperties();
        defaultProps.setTextStyleName("text-default");
        elementStyles.add(new ElementStyle(
                "paragraph-default",
                StyleTargetTypes.PARAGRAPH,
                defaultProps
        ));

        // Create element styles for each heading level
        for (int level = 1; level <= 6; level++) {
            ParagraphStyleProperties headingProps = new ParagraphStyleProperties();
            headingProps.setTextStyleName("text-heading-" + level);
            elementStyles.add(new ElementStyle(
                    "heading-" + level,
                    StyleTargetTypes.HEADLINE,
                    headingProps
            ));
        }

        // Unordered list style with disc bullets
        ListStyleProperties listPropsUnordered = new ListStyleProperties();
        listPropsUnordered.setTextStyleName("text-default");
        listPropsUnordered.setListStyleType("disc");
        elementStyles.add(new ElementStyle(
                "list-style-unordered",
                StyleTargetTypes.LIST,
                listPropsUnordered
        ));

        // Ordered list style with decimal numbering
        ListStyleProperties listPropsOrdered = new ListStyleProperties();
        listPropsOrdered.setTextStyleName("text-default");
        listPropsOrdered.setListStyleType("decimal");
        elementStyles.add(new ElementStyle(
                "list-style-ordered",
                StyleTargetTypes.LIST,
                listPropsOrdered
        ));

        // Default image style with centered alignment and uniform scaling
        BlockImageStyleProperties imagePropsDefault = new BlockImageStyleProperties();
        imagePropsDefault.setAlignment("center");
        imagePropsDefault.setScaling("uniform");
        imagePropsDefault.setContentWidth("100%");
        imagePropsDefault.setBlockWidth("100%");
        elementStyles.add(new ElementStyle(
                "image-default",
                StyleTargetTypes.BLOCK_IMAGE,
                imagePropsDefault
        ));

        // Default table style
        TableStyleProperties tableProp = new TableStyleProperties();
        tableProp.setTextStyleName("text-default");
        elementStyles.add(new ElementStyle(
                "table-default",
                StyleTargetTypes.TABLE,
                tableProp
        ));

        // Default table cell style
        TableCellStyleProperties tableCellStyleProperties = new TableCellStyleProperties();
        elementStyles.add(new ElementStyle(
                "cell-default",
                StyleTargetTypes.TABLE_CELL,
                tableCellStyleProperties
        ));

        // A4 portrait page master
        PageMasterStyle pageMaster = new PageMasterStyle(PAGE_MASTER_NAME);
        pageMasterStyles.add(pageMaster);

        return new StyleSheet(textStyles, elementStyles, pageMasterStyles);
    }
}