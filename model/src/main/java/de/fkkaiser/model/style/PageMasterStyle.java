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
package de.fkkaiser.model.style;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fkkaiser.model.annotation.Internal;
import de.fkkaiser.model.annotation.PublicAPI;
import de.fkkaiser.model.util.DimensionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

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
 * @author Katrin Kaiser
 * @version 1.1.0
 * @see PageSize
 */
public class PageMasterStyle {

    private static final Logger log = LoggerFactory.getLogger(PageMasterStyle.class);

    //for validation messages
    private static final String PARAM_PAGE_HEIGHT = "pageHeight";
    private static final String PARAM_PAGE_WIDTH = "pageWidth";
    private static final String PARAM_MARGIN_TOP = "marginTop";
    private static final String PARAM_MARGIN_BOTTOM = "marginBottom";
    private static final String PARAM_HEADER_EXTENT = "headerExtent";
    private static final String PARAM_FOOTER_EXTENT = "footerExtent";
    private static final String PARAM_COLUMN_GAP = "columnGap";
    private static final String PARAM_MARGIN = "margin";
    private static final String PARAM_MARGIN_LEFT = "marginLeft";
    private static final String PARAM_MARGIN_RIGHT = "marginRight";

    /**
     * A unique identifier for the page master style.
     * This name is used to reference the style in page sequences.
     */
    @JsonProperty("name")
    private String name;

    /**
     * The total height of the page.
     * The value must include a unit (e.g., "29.7cm", "11in").
     */
    @JsonProperty("page-height")
    private String pageHeight = "29.7cm"; // default A4 height

    /**
     * The total width of the page.
     * The value must include a unit (e.g., "21cm", "8.5in").
     */
    @JsonProperty("page-width")
    private String pageWidth = "21cm"; // default A4 width

    /**
     * Shorthand for setting all four margins (top, bottom, left, right) at once.
     * If set, this overrides the individual margin properties.
     * The value must include a unit (e.g., "2cm").
     */
    @JsonProperty("margin")
    private String margin ;

    /**
     * Top margin of the page.
     * The value must include a unit (e.g., "1cm", "0.5in").
     */
    @JsonProperty("margin-top")
    private String marginTop = "2cm";// default margin top

    /**
     * Bottom margin of the page.
     * The value must include a unit (e.g., "1cm", "0.5in").
     */
    @JsonProperty("margin-bottom")
    private String marginBottom = "2cm";// default margin bottom

    /**
     * Left margin of the page.
     * The value must include a unit (e.g., "1cm", "0.5in").
     */
    @JsonProperty("margin-left")
    private String marginLeft = "2.5cm";// default margin left

    /**
     * Right margin of the page.
     * The value must include a unit (e.g., "1cm", "0.5in").
     */
    @JsonProperty("margin-right")
    private String marginRight = "2.5cm";// default margin right


    /**
     * Flag indicating whether margins should be automatically adjusted
     * to prevent overlap with header/footer regions.
     * Default is true.
     */
    @JsonProperty("auto-adjust-margins")
    private boolean autoAdjustMargins = true;

    /**
     * Space reserved for the header region at the top of the page.
     * The value must include a unit (e.g., "2cm", "1in").
     */
    @JsonProperty("header-extent")
    private String headerExtent = "0cm"; // default no header extent

    /**
     * Space reserved for the footer region at the bottom of the page.
     * The value must include a unit (e.g., "2cm", "1in").
     */
    @JsonProperty("footer-extent")
    private String footerExtent = "0cm"; // default no footer extent

    /**
     * The number of columns on the page.
     * Typical values are "1" for single-column layout or "2" for two-column layout.
     */
    @JsonProperty("column-count")
    private String columnCount = "1";

    /**
     * Space between columns when using a multi-column layout.
     * The value must include a unit (e.g., "0.5cm", "0.25in").
     * This property is only relevant when columnCount is greater than 1.
     */
    @JsonProperty("column-gap")
    private String columnGap = "0cm"; // default no column gap

    // === Constructors ===

    /**
     * Default constructor with no arguments.
     * Primarily used for deserialization (e.g., by Jackson or similar frameworks).
     * All properties will be null until explicitly set.
     */
    @PublicAPI
    public PageMasterStyle() {}

    /**
     * Full constructor for creating a page master style with all properties specified.
     * This constructor provides complete control over every aspect of the page layout.
     *
     * @param name         a unique identifier for this page master style. Must not be {@code null}.
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
     * @param autoAdjustMargins flag to enable/disable automatic margin adjustment to prevent overlap with header/footer
     */
    @PublicAPI
    public PageMasterStyle(String name, String pageHeight, String pageWidth,
                           String marginTop, String marginBottom,
                           String marginLeft, String marginRight, String headerExtent,
                           String footerExtent, String columnCount, String columnGap,boolean autoAdjustMargins) {

        Objects.requireNonNull(name, "PageMasterStyle name must not be null");
        this.name = name;
        this.pageHeight = validatedDimension(pageHeight,PARAM_PAGE_HEIGHT);
        this.pageWidth = validatedDimension(pageWidth,PARAM_PAGE_WIDTH);
        this.marginTop = validatedDimension(marginTop,PARAM_MARGIN_TOP);
        this.marginBottom = validatedDimension(marginBottom,PARAM_MARGIN_BOTTOM);
        this.marginLeft = validatedDimension(marginLeft,PARAM_MARGIN_LEFT);
        this.marginRight = validatedDimension(marginRight,PARAM_MARGIN_RIGHT);
        this.headerExtent = validatedDimension(headerExtent,PARAM_HEADER_EXTENT);
        this.footerExtent = validatedDimension(footerExtent,PARAM_FOOTER_EXTENT);
        this.columnCount = columnCount;
        this.columnGap = validatedDimension(columnGap,PARAM_COLUMN_GAP);
        this.autoAdjustMargins = autoAdjustMargins;
        if(autoAdjustMargins) {
            ensureNoHeaderOverlap();
        }
        proofGapConsistency();
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
     * @param autoAdjustMargins flag to enable/disable automatic margin adjustment to prevent overlap with header/footer
     */
    @PublicAPI
    public PageMasterStyle(String name, PageSize pageSize,
                           String marginTop, String marginBottom,
                           String marginLeft, String marginRight, String headerExtent,
                           String footerExtent, String columnCount, String columnGap, boolean autoAdjustMargins) {
        this(name,
                pageSize.getHeight(),
                pageSize.getWidth(),
                marginTop, marginBottom, marginLeft, marginRight,
                headerExtent, footerExtent, columnCount, columnGap,autoAdjustMargins);
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
    @PublicAPI
    public PageMasterStyle(String name) {
        this(name, PageSize.A4_PORTRAIT,
                "2cm",
                "2cm",
                "2.5cm",
                "2.5cm",
                "1.5cm",
                "1.5cm",
                "1",
                "0cm",
                true);
    }

    // --- Getters and Setters ---

    /**
     * Returns the unique identifier of this page master style.
     *
     * @return the name of this page master style
     */
    @PublicAPI
    public String getName() {
        return name;
    }

    /**
     * Sets the unique identifier of this page master style.
     *
     * @param name the name to set for this page master style
     */
    @PublicAPI
    public void setName(String name) {
        Objects.requireNonNull(name, "PageMasterStyle name must not be null");
        this.name = name;
    }

    /**
     * Returns the total height of the page.
     *
     * @return the page height as a string with unit (e.g., "29.7cm")
     */
    @PublicAPI
    public String getPageHeight() {
        return pageHeight;
    }

    /**
     * Sets the total height of the page.
     *
     * @param pageHeight the page height as a string including value and unit (e.g., "29.7cm", "11in")
     */
    @PublicAPI
    public void setPageHeight(String pageHeight) {
        this.pageHeight = validatedDimension(pageHeight,PARAM_PAGE_HEIGHT);
    }

    /**
     * Returns the total width of the page.
     *
     * @return the page width as a string with unit (e.g., "21cm")
     */
    @PublicAPI
    public String getPageWidth() {
        return pageWidth;
    }

    /**
     * Sets the total width of the page.
     *
     * @param pageWidth the page width as a string including value and unit (e.g., "21cm", "8.5in")
     */
    @PublicAPI
    public void setPageWidth(String pageWidth) {
        this.pageWidth = validatedDimension(pageWidth,PARAM_PAGE_WIDTH);
    }

    /**
     * Sets whether margins should be automatically adjusted to prevent
     * overlap with header/footer regions. Default is {@code true}.
     * @param autoAdjustMargins true to enable automatic adjustment, false to disable
     */
    @PublicAPI
    public void setAutoAdjustMargins(boolean autoAdjustMargins) {
        this.autoAdjustMargins = autoAdjustMargins;
    }

    /**
     * Returns whether margins are automatically adjusted to prevent
     * overlap with header/footer regions.
     * @return true if automatic adjustment is enabled, false otherwise
     */
    @PublicAPI
    public boolean isAutoAdjustMargins() {
        return autoAdjustMargins;
    }

    /**
     * Returns the shorthand margin value that applies to all four sides.
     * If this value is set, it overrides individual margin properties (top, bottom, left, right).
     *
     * @return the margin as a string with unit (e.g., "2cm"), or null if not set
     */
    @PublicAPI
    public String getMargin() {
        return margin;
    }

    /**
     * Sets a shorthand margin value that applies to all four sides of the page.
     * Setting this value will override any individual margin properties.
     *
     * @param margin the margin as a string including value and unit (e.g., "2cm", "1in")
     */
    @PublicAPI
    public void setMargin(String margin) {
        this.margin = validatedDimension(margin,PARAM_MARGIN);
        this.marginTop = this.margin; // to make sure that overlap mechanism works
        this.marginBottom = this.margin;
        if(autoAdjustMargins){
            ensureNoHeaderOverlap();
        }
    }

    /**
     * Returns the top margin of the page.
     * This can be set individually or inherited from the general margin property.
     *
     * @return the top margin as a string with unit (e.g., "2cm")
     */
    @PublicAPI
    public String getMarginTop() {
        return marginTop;
    }

    /**
     * Sets the top margin of the page.
     * Please note that if headerExtent is set, marginTop will be automatically adjusted.
     *
     * @param marginTop the top margin as a string including value and unit (e.g., "2cm", "1in")
     */
    @PublicAPI
    public void setMarginTop(String marginTop) {
        this.marginTop = validatedDimension(marginTop,PARAM_MARGIN_TOP);
        if (autoAdjustMargins){
            if(this.marginTop==null){
                this.marginTop = headerExtent;
            }else{
                ensureNoHeaderOverlap();
            }
        }
    }

    /**
     * Forces the top margin of the page without adjusting for header overlap.
     * Use this method with caution, as it may result in content overlapping with the header region.
     * Note: Using this method disables automatic margin adjustment.
     *
     * @param marginTop the top margin as a string including value and unit (e.g., "2cm", "1in")
     */
    @PublicAPI
    public void forceMarginTop(String marginTop) {
        this.marginTop = validatedDimension(marginTop,PARAM_MARGIN_TOP);
        this.autoAdjustMargins = false;
    }

    /**
     * Returns the bottom margin of the page.
     * Note: the effective margin can be set inherited from the general margin property.
     * In this case this getter returns null although a margin is set.
     *
     * @return the bottom margin as a string with unit (e.g., "2cm")
     */
    @PublicAPI
    public String getMarginBottom() {
        return marginBottom;
    }

    /**
     * Sets the bottom margin of the page.
     *  Note: the effective margin can be set inherited from the general margin property.
     * In this case this getter returns null although a margin is set.
     * Note: if footerExtent is set, marginBottom will be automatically adjusted to prevent overlap.
     *
     * @param marginBottom the bottom margin as a string including value and unit (e.g., "2cm", "1in")
     */
    @PublicAPI
    public void setMarginBottom(String marginBottom) {
        this.marginBottom = validatedDimension(marginBottom,PARAM_MARGIN_BOTTOM);
        if(autoAdjustMargins) {
            if(this.marginBottom == null) {
                this.marginBottom = footerExtent;
            }else{
                ensureNoFooterOverlap();
            }
        }
    }

    /**
     * Forces the bottom margin of the page without adjusting for footer overlap.
     * Use this method with caution, as it may result in content overlapping with the footer region.
     * Note: Using this method disables automatic margin adjustment.
     *
     * @param marginBottom the bottom margin as a string including value and unit (e.g., "2cm", "1in")
     */
    @PublicAPI
    public void forceMarginBottom(String marginBottom) {
        this.marginBottom = validatedDimension(marginBottom,PARAM_MARGIN_BOTTOM);
        this.autoAdjustMargins = false;
    }

    /**
     * Returns the left margin of the page.
     * Note: the effective margin can be set inherited from the general margin property.
     * In this case this getter returns null although a margin is set.
     *
     * @return the left margin as a string with unit (e.g., "2.5cm")
     */
    @PublicAPI
    public String getMarginLeft() {
        return marginLeft;
    }

    /**
     * Sets the left margin of the page.
     *
     * @param marginLeft the left margin as a string including value and unit (e.g., "2.5cm", "1in")
     */
    @PublicAPI
    public void setMarginLeft(String marginLeft) {
        this.marginLeft = validatedDimension(marginLeft,PARAM_MARGIN_LEFT);
    }

    /**
     * Returns the right margin of the page.
     * This can be set individually or inherited from the general margin property.
     *
     * @return the right margin as a string with unit (e.g., "2.5cm")
     */
    @PublicAPI
    public String getMarginRight() {
        return marginRight;
    }

    /**
     * Sets the right margin of the page.
     *
     * @param marginRight the right margin as a string including value and unit (e.g., "2.5cm", "1in")
     */
    @PublicAPI
    public void setMarginRight(String marginRight) {
        this.marginRight = validatedDimension(marginRight,PARAM_MARGIN_RIGHT);
    }

    /**
     * Returns the space reserved for the header region.
     *
     * @return the header extent as a string with unit (e.g., "1.5cm")
     */
    @PublicAPI
    public String getHeaderExtent() {
        return headerExtent;
    }

    /**
     * Sets the space reserved for the header region.
     * This will automatically adjust marginTop if autoAdjustMargins is enabled.
     *
     * @param headerExtent the header extent as a string including value and unit (e.g., "1.5cm", "1in")
     */
    @PublicAPI
    public void setHeaderExtent(String headerExtent) {
        this.headerExtent = validatedDimension(headerExtent,PARAM_HEADER_EXTENT);
        if(autoAdjustMargins) {
            if(marginTop==null) {
                marginTop = this.headerExtent;
                ensureNoHeaderOverlap();
            }
        }
    }

    /**
     * Returns the space reserved for the footer region.
     *
     * @return the footer extent as a string with unit (e.g., "1.5cm")
     */
    @PublicAPI
    public String getFooterExtent() {
        return footerExtent;
    }

    /**
     * Sets the space reserved for the footer region.
     *
     * @param footerExtent the footer extent as a string including value and unit (e.g., "1.5cm", "1in")
     */
    @PublicAPI
    public void setFooterExtent(String footerExtent) {
        this.footerExtent = validatedDimension(footerExtent,PARAM_FOOTER_EXTENT);
        if(autoAdjustMargins) {
            if(marginBottom==null){
                marginBottom = this.footerExtent;
            }else{
                ensureNoFooterOverlap();
            }
        }
    }

    /**
     * Returns the number of columns for the page layout.
     *
     * @return the column count as a string (e.g., "1", "2", "3")
     */
    @PublicAPI
    public String getColumnCount() {
        return columnCount;
    }

    /**
     * Sets the number of columns for the page layout.
     *
     * @param columnCount the column count as a string (e.g., "1" for single column, "2" for two columns)
     */
    @PublicAPI
    public void setColumnCount(String columnCount) {
        this.columnCount = columnCount;
        proofGapConsistency();
    }

    /**
     * Returns the space between columns in a multi-column layout.
     *
     * @return the column gap as a string with unit (e.g., "0.5cm")
     */
    @PublicAPI
    public String getColumnGap() {
        return columnGap;
    }

    /**
     * Sets the space between columns in a multi-column layout.
     * This property is only relevant when the column count is greater than 1.
     *
     * @param columnGap the column gap as a string including value and unit (e.g., "0.5cm", "0.25in")
     */
    @PublicAPI
    public void setColumnGap(String columnGap) {
        this.columnGap = validatedDimension(columnGap,PARAM_COLUMN_GAP);
        proofGapConsistency();
    }

    /**
     * Sets the page dimensions using a predefined PageSize enum.
     * This is a convenient method to change the page size to a standard format
     * (such as A4, A3, or A5 in portrait or landscape orientation).
     *
     * @param pageSize the predefined page size (e.g., {@link PageSize#A4_PORTRAIT})
     */
    @PublicAPI
    public void setPageSize(PageSize pageSize) {
        this.pageHeight = pageSize.getHeight();
        this.pageWidth = pageSize.getWidth();
    }

    /**
     * Validates the PageMasterStyle instance to ensure required properties are set.
     * This method checks that the 'name' property is not null or empty.
     *
     * @throws IllegalStateException if any required property is missing or invalid
     * @throws NullPointerException if 'name' is null
     */
    @Internal
    public void validate() {
        Objects.requireNonNull(name, "PageMasterStyle name must not be null");

        if (name.trim().isEmpty()) {
            throw new IllegalStateException(
                    "PageMasterStyle 'name' is required");
        }
        validatedDimension(pageHeight,PARAM_PAGE_HEIGHT);
        validatedDimension(pageWidth,PARAM_PAGE_WIDTH);
        validatedDimension(marginTop,PARAM_MARGIN_TOP);
        validatedDimension(marginBottom,PARAM_MARGIN_BOTTOM);
        validatedDimension(headerExtent,PARAM_HEADER_EXTENT);
        validatedDimension(footerExtent,PARAM_FOOTER_EXTENT);
        validatedDimension(columnGap,PARAM_COLUMN_GAP);
        validatedDimension(margin,PARAM_MARGIN);
        validatedDimension(marginLeft,PARAM_MARGIN_LEFT);
        validatedDimension(marginRight,PARAM_MARGIN_RIGHT);

    }


    // ==== Private Helper Methods ====

    /**
     * Ensures that marginTop is at least as large as headerExtent.
     * If headerExtent is set but marginTop is smaller, marginTop is automatically
     * adjusted to match headerExtent.
     */
    private void ensureNoHeaderOverlap() {
        if (headerExtent != null && marginTop != null) {
            double headerValue = parseValue(headerExtent);
            double marginValue = parseValue(marginTop);

            if (marginValue < headerValue) {
                log.warn("marginTop ({}) is smaller than headerExtent ({}). Adjusting marginTop to avoid overlap.If you want to force this overlap please use forceMarginTop.",
                        marginTop, headerExtent);
                this.marginTop = headerExtent;
            }
        }
    }

    /**
     * Ensures that marginBottom is at least as large as footerExtent.
     * If footerExtent is set but marginBottom is smaller, marginBottom is automatically
     * adjusted to match footerExtent.
     */
    private void ensureNoFooterOverlap() {
        if (footerExtent != null && marginBottom != null) {
            double footerValue = parseValue(footerExtent);
            double marginValue = parseValue(marginBottom);

            if (marginValue < footerValue) {
                log.warn("marginBottom ({}) is smaller than footerExtent ({}). Adjusting marginBottom to avoid overlap.If you want to force this overlap please use forceMarginBottom.",
                        marginBottom, footerExtent);
                this.marginBottom = footerExtent;
            }
        }
    }

    /**
     * Parses a dimension string to a double value in cm.
     * Supports "cm", "mm", "in", "pt" units.
     */
    private double parseValue(String dimension) {
        if (dimension == null) return 0;

        String value = dimension.replaceAll("[^0-9.]", "");
        String unit = dimension.replaceAll("[0-9.]", "");

        double num = Double.parseDouble(value);

        return switch (unit) {
            case "cm" -> num;
            case "mm" -> num / 10.0;
            case "in" -> num * 2.54;
            case "pt" -> num * 0.0353;
            default -> num; // assume cm
        };
    }

    private void proofGapConsistency() {
        if (columnCount == null) {
            this.columnCount = "1";
            this.columnGap = "0cm";
            return;
        }

        try {
            int count = Integer.parseInt(columnCount);
            if (count <= 1) {
                this.columnGap = "0cm";
                this.columnCount = "1";
            }
        } catch (NumberFormatException e) {
            log.warn("Invalid columnCount '{}'. Must be an integer. Setting to '1' with no gap.", columnCount);
            this.columnCount = "1";
            this.columnGap = "0cm";
        }
    }

    /**
     * Validates and normalizes dimension value.
     *
     * @param paramValue the value to validate
     * @param paramName the name of the parameter for logging purposes
     * @return the normalized page height
     */
    private String validatedDimension(String paramValue,String paramName) {
        if (paramValue == null) {
            return null;
        }
        DimensionUtil.ValidationResult result = DimensionUtil.validateAndNormalize(paramValue);
        if (result.hasWarning()) {
            log.warn("PageMasterStyle[{}].{}: {}", name, paramName, result.warningMessage());
        }
        return result.normalized();
    }


    // ==== PageSize Enum ====

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
}