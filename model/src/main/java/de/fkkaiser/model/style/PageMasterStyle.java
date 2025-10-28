package de.fkkaiser.model.style;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the styling properties for a page layout in a document.
 * This class maps to the fo:simple-page-master element in XSL-FO and provides
 * flexible configuration for page dimensions, margins, headers, footers, and columns.
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * // Create a simple A4 portrait page with default settings
 * PageMasterStyle simplePage = new PageMasterStyle("main-page");
 *
 * // Create a custom page layout
 * PageMasterStyle customPage = new PageMasterStyle(
 *     "custom-page",
 *     PageSize.A4_LANDSCAPE,
 *     "3cm",    // marginTop
 *     "3cm",    // marginBottom
 *     "2cm",    // marginLeft
 *     "2cm",    // marginRight
 *     "2cm",    // headerExtent
 *     "2cm",    // footerExtent
 *     "2",      // columnCount
 *     "1cm"     // columnGap
 * );
 *
 * // Modify an existing page style
 * simplePage.setPageSize(PageSize.A3_PORTRAIT);
 * simplePage.setMarginTop("4cm");
 * }</pre>
 *
 * @author FK Kaiser
 * @version 1.0
 * @see PageSize
 */
public class PageMasterStyle {

    /**
     * Defines standard ISO page sizes with their dimensions.
     * Each page size constant provides both portrait and landscape orientations
     * with accurate dimensions in centimeters.
     *
     * <p><b>Note:</b> Width is stored first, then height.</p>
     *
     * <p><b>Available Sizes:</b></p>
     * <ul>
     *   <li>A4: 21cm × 29.7cm (Portrait) or 29.7cm × 21cm (Landscape)</li>
     *   <li>A3: 29.7cm × 42cm (Portrait) or 42cm × 29.7cm (Landscape)</li>
     *   <li>A5: 14.8cm × 21cm (Portrait) or 21cm × 14.8cm (Landscape)</li>
     * </ul>
     */
    public enum PageSize {
        /**
         * A4 portrait orientation: 21cm width × 29.7cm height
         */
        A4_PORTRAIT("21cm", "29.7cm"),

        /**
         * A4 landscape orientation: 29.7cm width × 21cm height
         */
        A4_LANDSCAPE("29.7cm", "21cm"),

        /**
         * A3 portrait orientation: 29.7cm width × 42cm height
         */
        A3_PORTRAIT("29.7cm", "42cm"),

        /**
         * A3 landscape orientation: 42cm width × 29.7cm height
         */
        A3_LANDSCAPE("42cm", "29.7cm"),

        /**
         * A5 portrait orientation: 14.8cm width × 21cm height
         */
        A5_PORTRAIT("14.8cm", "21cm"),

        /**
         * A5 landscape orientation: 21cm width × 14.8cm height
         */
        A5_LANDSCAPE("21cm", "14.8cm");

        private final String width;
        private final String height;

        /**
         * Constructs a PageSize with the specified dimensions.
         *
         * @param width  the width of the page including unit (e.g., "21cm")
         * @param height the height of the page including unit (e.g., "29.7cm")
         */
        PageSize(String width, String height) {
            this.width = width;
            this.height = height;
        }

        /**
         * Returns the width of the page size.
         *
         * @return the width as a string with unit (e.g., "21cm" for A4_PORTRAIT)
         */
        public String getWidth() {
            return width;
        }

        /**
         * Returns the height of the page size.
         *
         * @return the height as a string with unit (e.g., "29.7cm" for A4_PORTRAIT)
         */
        public String getHeight() {
            return height;
        }
    }

    /**
     * A unique identifier for the page master style.
     * This name is used to reference the style in page sequences.
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
     * Top margin of the page.
     * The value must include a unit (e.g., "1cm", "0.5in").
     */
    @JsonProperty("margin-top")
    private String marginTop;

    /**
     * Bottom margin of the page.
     * The value must include a unit (e.g., "1cm", "0.5in").
     */
    @JsonProperty("margin-bottom")
    private String marginBottom;

    /**
     * Left margin of the page.
     * The value must include a unit (e.g., "1cm", "0.5in").
     */
    @JsonProperty("margin-left")
    private String marginLeft;

    /**
     * Right margin of the page.
     * The value must include a unit (e.g., "1cm", "0.5in").
     */
    @JsonProperty("margin-right")
    private String marginRight;

    /**
     * Space reserved for the header region at the top of the page.
     * The value must include a unit (e.g., "2cm", "1in").
     */
    @JsonProperty("header-extent")
    private String headerExtent;

    /**
     * Space reserved for the footer region at the bottom of the page.
     * The value must include a unit (e.g., "2cm", "1in").
     */
    @JsonProperty("footer-extent")
    private String footerExtent;

    /**
     * The number of columns on the page.
     * Typical values are "1" for single-column layout or "2" for two-column layout.
     */
    @JsonProperty("column-count")
    private String columnCount;

    /**
     * Space between columns when using a multi-column layout.
     * The value must include a unit (e.g., "0.5cm", "0.25in").
     * This property is only relevant when columnCount is greater than 1.
     */
    @JsonProperty("column-gap")
    private String columnGap;

    // --- Constructors ---

    /**
     * Default constructor with no arguments.
     * Primarily used for deserialization (e.g., by Jackson or similar frameworks).
     * All properties will be null until explicitly set.
     */
    public PageMasterStyle() {}

    /**
     * Full constructor for creating a page master style with all properties specified.
     * This constructor provides complete control over every aspect of the page layout.
     *
     * @param name         a unique identifier for this page master style
     * @param pageHeight   the total page height, including unit (e.g., "29.7cm")
     * @param pageWidth    the total page width, including unit (e.g., "21cm")
     * @param marginTop    the top margin, including unit (e.g., "2cm")
     * @param marginBottom the bottom margin, including unit (e.g., "2cm")
     * @param marginLeft   the left margin, including unit (e.g., "2.5cm")
     * @param marginRight  the right margin, including unit (e.g., "2.5cm")
     * @param headerExtent the space reserved for the header, including unit (e.g., "1.5cm")
     * @param footerExtent the space reserved for the footer, including unit (e.g., "1.5cm")
     * @param columnCount  the number of columns (e.g., "1" or "2")
     * @param columnGap    the space between columns, including unit (e.g., "0.5cm")
     */
    public PageMasterStyle(String name, String pageHeight, String pageWidth,
                           String marginTop, String marginBottom,
                           String marginLeft, String marginRight, String headerExtent,
                           String footerExtent, String columnCount, String columnGap) {
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
     * Constructor using a predefined PageSize enum and specific layout properties.
     * This is a convenient way to create standard-sized pages (A4, A3, A5) without
     * manually specifying dimensions.
     *
     * @param name         a unique identifier for this page master style
     * @param pageSize     the predefined page size (e.g., {@link PageSize#A4_PORTRAIT})
     * @param marginTop    the top margin, including unit (e.g., "2cm")
     * @param marginBottom the bottom margin, including unit (e.g., "2cm")
     * @param marginLeft   the left margin, including unit (e.g., "2.5cm")
     * @param marginRight  the right margin, including unit (e.g., "2.5cm")
     * @param headerExtent the space reserved for the header, including unit (e.g., "1.5cm")
     * @param footerExtent the space reserved for the footer, including unit (e.g., "1.5cm")
     * @param columnCount  the number of columns (e.g., "1" or "2")
     * @param columnGap    the space between columns, including unit (e.g., "0.5cm")
     */
    public PageMasterStyle(String name, PageSize pageSize,
                           String marginTop, String marginBottom,
                           String marginLeft, String marginRight, String headerExtent,
                           String footerExtent, String columnCount, String columnGap) {
        this(name,
                pageSize.getHeight(),
                pageSize.getWidth(),
                marginTop, marginBottom, marginLeft, marginRight,
                headerExtent, footerExtent, columnCount, columnGap);
    }

    /**
     * Simplified constructor that creates a default A4 Portrait page layout with standard margins.
     * This is the most convenient way to create a standard page when you only need to specify a name.
     *
     * <p><b>Default Values:</b></p>
     * <ul>
     *   <li>Page Size: A4 Portrait (21cm × 29.7cm)</li>
     *   <li>Top Margin: 2cm</li>
     *   <li>Bottom Margin: 2cm</li>
     *   <li>Left Margin: 2.5cm</li>
     *   <li>Right Margin: 2.5cm</li>
     *   <li>Header Extent: 1.5cm</li>
     *   <li>Footer Extent: 1.5cm</li>
     *   <li>Column Count: 1 (single column)</li>
     *   <li>Column Gap: 0cm</li>
     * </ul>
     *
     * @param name a unique identifier for this page master style
     */
    public PageMasterStyle(String name) {
        this(name, PageSize.A4_PORTRAIT,
                "2cm",
                "2cm",
                "2.5cm",
                "2.5cm",
                "1.5cm",
                "1.5cm",
                "1",
                "0cm");
    }

    // --- Getters and Setters ---

    /**
     * Returns the unique identifier of this page master style.
     *
     * @return the name of this page master style
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the unique identifier of this page master style.
     *
     * @param name the name to set for this page master style
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the total height of the page.
     *
     * @return the page height as a string with unit (e.g., "29.7cm")
     */
    public String getPageHeight() {
        return pageHeight;
    }

    /**
     * Sets the total height of the page.
     *
     * @param pageHeight the page height as a string including value and unit (e.g., "29.7cm", "11in")
     */
    @SuppressWarnings("unused")
    public void setPageHeight(String pageHeight) {
        this.pageHeight = pageHeight;
    }

    /**
     * Returns the total width of the page.
     *
     * @return the page width as a string with unit (e.g., "21cm")
     */
    @SuppressWarnings("unused")
    public String getPageWidth() {
        return pageWidth;
    }

    /**
     * Sets the total width of the page.
     *
     * @param pageWidth the page width as a string including value and unit (e.g., "21cm", "8.5in")
     */
    @SuppressWarnings("unused")
    public void setPageWidth(String pageWidth) {
        this.pageWidth = pageWidth;
    }

    /**
     * Returns the shorthand margin value that applies to all four sides.
     * If this value is set, it overrides individual margin properties (top, bottom, left, right).
     *
     * @return the margin as a string with unit (e.g., "2cm"), or null if not set
     */
    @SuppressWarnings("unused")
    public String getMargin() {
        return margin;
    }

    /**
     * Sets a shorthand margin value that applies to all four sides of the page.
     * Setting this value will override any individual margin properties.
     *
     * @param margin the margin as a string including value and unit (e.g., "2cm", "1in")
     */
    @SuppressWarnings("unused")
    public void setMargin(String margin) {
        this.margin = margin;
    }

    /**
     * Returns the top margin of the page.
     * This can be set individually or inherited from the general margin property.
     *
     * @return the top margin as a string with unit (e.g., "2cm")
     */
    @SuppressWarnings("unused")
    public String getMarginTop() {
        return marginTop;
    }

    /**
     * Sets the top margin of the page.
     *
     * @param marginTop the top margin as a string including value and unit (e.g., "2cm", "1in")
     */
    @SuppressWarnings("unused")
    public void setMarginTop(String marginTop) {
        this.marginTop = marginTop;
    }

    /**
     * Returns the bottom margin of the page.
     * Note: the effective margin can be set inherited from the general margin property.
     * In this case this getter returns null although a margin is set.
     *
     * @return the bottom margin as a string with unit (e.g., "2cm")
     */
    @SuppressWarnings("unused")
    public String getMarginBottom() {
        return marginBottom;
    }

    /**
     * Sets the bottom margin of the page.
     *  Note: the effective margin can be set inherited from the general margin property.
     * In this case this getter returns null although a margin is set.
     *
     * @param marginBottom the bottom margin as a string including value and unit (e.g., "2cm", "1in")
     */
    @SuppressWarnings("unused")
    public void setMarginBottom(String marginBottom) {
        this.marginBottom = marginBottom;
    }

    /**
     * Returns the left margin of the page.
     * Note: the effective margin can be set inherited from the general margin property.
     * In this case this getter returns null although a margin is set.
     *
     * @return the left margin as a string with unit (e.g., "2.5cm")
     */
    public String getMarginLeft() {
        return marginLeft;
    }

    /**
     * Sets the left margin of the page.
     *
     * @param marginLeft the left margin as a string including value and unit (e.g., "2.5cm", "1in")
     */
    @SuppressWarnings("unused")
    public void setMarginLeft(String marginLeft) {
        this.marginLeft = marginLeft;
    }

    /**
     * Returns the right margin of the page.
     * This can be set individually or inherited from the general margin property.
     *
     * @return the right margin as a string with unit (e.g., "2.5cm")
     */
    public String getMarginRight() {
        return marginRight;
    }

    /**
     * Sets the right margin of the page.
     *
     * @param marginRight the right margin as a string including value and unit (e.g., "2.5cm", "1in")
     */
    @SuppressWarnings("unused")
    public void setMarginRight(String marginRight) {
        this.marginRight = marginRight;
    }

    /**
     * Returns the space reserved for the header region.
     *
     * @return the header extent as a string with unit (e.g., "1.5cm")
     */
    public String getHeaderExtent() {
        return headerExtent;
    }

    /**
     * Sets the space reserved for the header region.
     *
     * @param headerExtent the header extent as a string including value and unit (e.g., "1.5cm", "1in")
     */
    @SuppressWarnings("unused")
    public void setHeaderExtent(String headerExtent) {
        this.headerExtent = headerExtent;
    }

    /**
     * Returns the space reserved for the footer region.
     *
     * @return the footer extent as a string with unit (e.g., "1.5cm")
     */
    public String getFooterExtent() {
        return footerExtent;
    }

    /**
     * Sets the space reserved for the footer region.
     *
     * @param footerExtent the footer extent as a string including value and unit (e.g., "1.5cm", "1in")
     */
    @SuppressWarnings("unused")
    public void setFooterExtent(String footerExtent) {
        this.footerExtent = footerExtent;
    }

    /**
     * Returns the number of columns for the page layout.
     *
     * @return the column count as a string (e.g., "1", "2", "3")
     */
    public String getColumnCount() {
        return columnCount;
    }

    /**
     * Sets the number of columns for the page layout.
     *
     * @param columnCount the column count as a string (e.g., "1" for single column, "2" for two columns)
     */
    @SuppressWarnings("unused")
    public void setColumnCount(String columnCount) {
        this.columnCount = columnCount;
    }

    /**
     * Returns the space between columns in a multi-column layout.
     *
     * @return the column gap as a string with unit (e.g., "0.5cm")
     */
    public String getColumnGap() {
        return columnGap;
    }

    /**
     * Sets the space between columns in a multi-column layout.
     * This property is only relevant when the column count is greater than 1.
     *
     * @param columnGap the column gap as a string including value and unit (e.g., "0.5cm", "0.25in")
     */
    @SuppressWarnings("unused")
    public void setColumnGap(String columnGap) {
        this.columnGap = columnGap;
    }

    /**
     * Sets the page dimensions using a predefined PageSize enum.
     * This is a convenient method to change the page size to a standard format
     * (such as A4, A3, or A5 in portrait or landscape orientation).
     *
     * @param pageSize the predefined page size (e.g., {@link PageSize#A4_PORTRAIT})
     */
    @SuppressWarnings("unused")
    public void setPageSize(PageSize pageSize) {
        this.pageHeight = pageSize.getHeight();
        this.pageWidth = pageSize.getWidth();
    }
}