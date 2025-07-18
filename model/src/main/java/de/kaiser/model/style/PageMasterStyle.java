package de.kaiser.model.style;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the styling properties for a page layout in the document.
 */
public class PageMasterStyle {

    private String name;

    @JsonProperty("page-height")
    private String pageHeight;

    @JsonProperty("page-width")
    private String pageWidth;

    private String margin;

    @JsonProperty("margin-top")
    private String marginTop;

    @JsonProperty("margin-bottom")
    private String marginBottom;

    @JsonProperty("margin-left")
    private String marginLeft;

    @JsonProperty("margin-right")
    private String marginRight;

    @JsonProperty("header-extent")
    private String headerExtent; // z.B. "1.5cm"

    @JsonProperty("footer-extent")
    private String footerExtent; // z.B. "1.5cm"

    @JsonProperty("column-count")
    private String columnCount;

    @JsonProperty("column-gap")
    private String columnGap;

    //-- Constructor ---

    public PageMasterStyle() {}

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

