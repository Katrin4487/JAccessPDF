package de.fkkaiser.api.simplelayer;

import de.fkkaiser.model.style.*;
import de.fkkaiser.model.style.ElementStyle;
import de.fkkaiser.model.style.PageMasterStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages default styles for SimpleDocument.
 * Uses Open Sans as default font (PDF/UA compliant).
 */
class SimpleStyleManager {


    private static final String PAGE_MASTER_NAME = "simple-a4-portrait";
    private String fontFamily = "Open Sans"; // PDF/UA: embedded font required

    String getDefaultPageMasterName() {
        return PAGE_MASTER_NAME;
    }


    void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    String getParagraphStyleName(String name) {
        if ("default".equals(name)) {
            return "paragraph-default";
        }
        return "paragraph-" + name;
    }

    String getHeadingStyleName(int level) {
        return "heading-" + level;
    }

    String getUnorderedListName(){
        return "list-style-unordered";
    }

    String getOrderedListName(){
        return "list-style-ordered";
    }

    String getDefaultImageName() {
        return "image-default";
    }

    String getDefaultCellStyleName(){
        return "cell-default";
    }

    /**
     * Builds a StyleSheet with PDF/UA compliant defaults.
     * Uses Open Sans (embedded) by default.
     */
    StyleSheet buildStyleSheet() {
        List<TextStyle> textStyles = new ArrayList<>();
        List<ElementStyle> elementStyles = new ArrayList<>();
        List<PageMasterStyle> pageMasterStyles = new ArrayList<>();

        // Default text style - uses embedded Open Sans
        textStyles.add(new TextStyle(
                "text-default",
                "12px",
                fontFamily, // "Open Sans"
                "400",
                "normal"
        ));

        // Heading text styles
        for (int level = 1; level <= 6; level++) {
            int fontSize = 24 - (level - 1) * 2; // 24px, 22px, 20px, ...
            textStyles.add(new TextStyle(
                    "text-heading-" + level,
                    fontSize + "px",
                    fontFamily, // "Open Sans"
                    "700", // bold
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

        // Heading element styles
        for (int level = 1; level <= 6; level++) {
            ParagraphStyleProperties headingProps = new ParagraphStyleProperties();
            headingProps.setTextStyleName("text-heading-" + level);
            elementStyles.add(new ElementStyle(
                    "heading-" + level,
                    StyleTargetTypes.HEADLINE,
                    headingProps
            ));
        }

        // Style for unordered list
        ListStyleProperties listPropsUnordered = new ListStyleProperties();
        listPropsUnordered.setTextStyleName("text-default");
        listPropsUnordered.setListStyleType("disc");
        listPropsUnordered.setListStylePosition("inside");
        elementStyles.add(new ElementStyle(
                "list-style-unordered",
                StyleTargetTypes.LIST,
                listPropsUnordered
        ));

        // Style for ordered list
        ListStyleProperties listPropsOrdered = new ListStyleProperties();
        listPropsOrdered.setTextStyleName("text-default");
        listPropsOrdered.setListStyleType("decimal");
        listPropsOrdered.setListStylePosition("inside");
        elementStyles.add(new ElementStyle(
                "list-style-ordered",
                StyleTargetTypes.LIST,
                listPropsOrdered
        ));

        // Default image style
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

        TableStyleProperties tableProp = new TableStyleProperties();
        tableProp.setTextStyleName("text-default");
        elementStyles.add(new ElementStyle("table-default", StyleTargetTypes.TABLE, tableProp));

        TableCellStyleProperties tableCellStyleProperties = new TableCellStyleProperties();
        elementStyles.add(new ElementStyle("cell-default", StyleTargetTypes.TABLE_CELL, tableCellStyleProperties));

        // Default page master (A4 portrait)
        PageMasterStyle pageMaster = new PageMasterStyle(PAGE_MASTER_NAME);
        pageMasterStyles.add(pageMaster);

        return new StyleSheet(textStyles, elementStyles, pageMasterStyles);
    }

}

