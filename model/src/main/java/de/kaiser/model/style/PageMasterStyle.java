package de.kaiser.model.style;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the styling properties for a page layout in the document.
 * This maps to the fo:simple-page-master element in XSL-FO.
 */
public class PageMasterStyle {

    /**
     * Defines standard ISO page sizes.
     * Saves width first, then height
     */
    public enum PageSize {
        // Die Namen (z.B. A4_PORTRAIT) sind frei wählbar
        A4_PORTRAIT("21cm", "29.7cm"),
        A4_LANDSCAPE("29.7cm", "21cm"),
        A3_PORTRAIT("29.7cm", "42cm"),
        A3_LANDSCAPE("42cm", "29.7cm"),
        A5_PORTRAIT("14.8cm", "21cm"),
        A5_LANDSCAPE("21cm", "14.8cm");

        private final String width;
        private final String height;

        PageSize(String width, String height) {
            this.width = width;
            this.height = height;
        }

        public String getWidth() { return width; }
        public String getHeight() { return height; }
    }

    /**
     * A unique identifier for the page master style.
     */
    private String name;

    /**
     * The total height of the page.
     * The value must include a unit (e.g., "29.7cm", "11in").
     */
    @JsonProperty("page-height")
    private String pageHeight;

    /**
     * The total width of the page.
     * The value must include a unit (e.g., "21cm", "8.5in").
     */
    @JsonProperty("page-width")
    private String pageWidth;

    /**
     * Shorthand for setting all four margins (top, bottom, left, right) at once.
     * If set, this overrides the individual margin properties.
     * The value must include a unit (e.g., "2cm").
     */
    private String margin;

    /**
     * Top margin, including unit (e.g., 1cm)
     */
    @JsonProperty("margin-top")
    private String marginTop;

    /**
     * Bottom margin, including unit (e.g., 1cm)
     */
    @JsonProperty("margin-bottom")
    private String marginBottom;

    /**
     * Left margin, including unit (e.g., 1cm)
     */
    @JsonProperty("margin-left")
    private String marginLeft;

    /**
     * Right margin, including unit (e.g., 1cm)
     */
    @JsonProperty("margin-right")
    private String marginRight;

    /**
     * Space reserved for the header,  including unit (e.g., "2cm").
     */
    @JsonProperty("header-extent")
    private String headerExtent;

    /**
     * Space reserved for the footer, including unit (e.g., "2cm").
     */
    @JsonProperty("footer-extent")
    private String footerExtent;

    /**
     * The number of columns on the page (e.g., "1", "2").
     */
    @JsonProperty("column-count")
    private String columnCount;

    /**
     * Space between columns, including unit (e.g., "0.5cm").
     */
    @JsonProperty("column-gap")
    private String columnGap;

    //-- Constructor ---

    /**
     * Default constructor. Primarily used for deserialization (e.g., by Jackson).
     */
    public PageMasterStyle() {}

    /**
     * Full constructor setting all possible page layout properties.
     *
     * @param name        A unique identifier for this style.
     * @param pageHeight  The total page height, including unit (e.g., "29.7cm").
     * @param pageWidth   The total page width, including unit (e.g., "21cm").
     * @param marginTop   Top margin, including unit.
     * @param marginBottom Bottom margin, including unit.
     * @param marginLeft  Left margin, including unit.
     * @param marginRight Right margin, including unit.
     * @param headerExtent Space reserved for the header, including unit.
     * @param footerExtent Space reserved for the footer, including unit.
     * @param columnCount Number of columns (e.g., "1").
     * @param columnGap   Space between columns, including unit (e.g., "0.5cm").
     */
    public PageMasterStyle(String name, String pageHeight, String pageWidth,
                           String marginTop, String marginBottom,
                           String marginLeft, String marginRight, String headerExtent,
                           String footerExtent,String columnCount,String columnGap) {
        this.name = name;
        this.pageHeight = pageHeight;
        this.pageWidth = pageWidth;
        this.marginTop = marginTop;
        this.marginBottom = marginBottom;
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
        this.headerExtent = headerExtent;
        this.footerExtent = footerExtent;
        this.columnCount = columnCount;
        this.columnGap = columnGap;
    }


    /**
     * Constructor using a predefined PageSize (e.g., A4_PORTRAIT)
     * and specific layout properties.
     *
     * @param name A unique identifier for this style.
     * @param pageSize The predefined page size (e.g., PageSize.A4_PORTRAIT).
     * @param marginTop   Top margin, including unit.
     * @param marginBottom Bottom margin, including unit.
     * @param marginLeft  Left margin, including unit.
     * @param marginRight Right margin, including unit.
     * @param headerExtent Space reserved for the header, including unit.
     * @param footerExtent Space reserved for the footer, including unit.
     * @param columnCount Number of columns (e.g., "1").
     * @param columnGap   Space between columns, including unit (e.g., "0.5cm").
     */
    public PageMasterStyle(String name, PageSize pageSize,
                           String marginTop, String marginBottom,
                           String marginLeft, String marginRight, String headerExtent,
                           String footerExtent, String columnCount, String columnGap) {
        this(name,
                pageSize.getHeight(), // Höhe
                pageSize.getWidth(),  // Breite
                marginTop, marginBottom, marginLeft, marginRight,
                headerExtent, footerExtent, columnCount, columnGap);
    }


    /**
     * Creates a default A4 Portrait page layout with standard margins.
     * <p>
     * <b>Defaults:</b>
     * <ul>
     * <li>Page Size: A4 Portrait (21cm x 29.7cm)</li>
     * <li>Margins: 2cm (top/bottom), 2.5cm (left/right)</li>
     * <li>Header/Footer: 1.5cm</li>
     * <li>Columns: 1</li>
     * </ul>
     *
     * @param name A unique identifier for this page master style.
     */
    public PageMasterStyle(String name) {
        this(name,PageSize.A4_PORTRAIT,
                "2cm",
                "2cm",
                "2.5cm",
                "2.5cm",
                "1.5cm",
                "1.5cm",
                "1",
                "0cm");
    }

    // --- Getter und Setter ---

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPageHeight() {
        return pageHeight;
    }

    public void setPageHeight(String pageHeight) {
        this.pageHeight = pageHeight;
    }

    public String getPageWidth() {
        return pageWidth;
    }

    public void setPageWidth(String pageWidth) {
        this.pageWidth = pageWidth;
    }

    public String getMargin() {
        return margin;
    }


    public String getMarginTop() {
        return marginTop;
    }


    public String getMarginBottom() {
        return marginBottom;
    }


    public String getMarginLeft() {
        return marginLeft;
    }

    public String getMarginRight() {
        return marginRight;
    }


    public String getHeaderExtent() {
        return headerExtent;
    }


    public String getFooterExtent() {
        return footerExtent;
    }

    public void setMargin(String margin) {
        this.margin = margin;
    }

    public void setMarginTop(String marginTop) {
        this.marginTop = marginTop;
    }

    public void setMarginBottom(String marginBottom) {
        this.marginBottom = marginBottom;
    }

    public void setMarginLeft(String marginLeft) {
        this.marginLeft = marginLeft;
    }

    public void setMarginRight(String marginRight) {
        this.marginRight = marginRight;
    }

    public void setHeaderExtent(String headerExtent) {
        this.headerExtent = headerExtent;
    }

    public void setFooterExtent(String footerExtent) {
        this.footerExtent = footerExtent;
    }

    public String getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(String columnCount) {
        this.columnCount = columnCount;
    }

    public String getColumnGap() {
        return columnGap;
    }

    public void setColumnGap(String columnGap) {
        this.columnGap = columnGap;
    }
}

