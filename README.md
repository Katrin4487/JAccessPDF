# JAccessPDF

[![Status](https://img.shields.io/badge/status-beta-yellow)]()

A Java library for creating PDF/UA compliant PDFs with Apache FOP.

## ⚠️ Current Status: Beta

This library is functional and being used in production, but:
- Some features are still being finalized
- API may change in minor versions (following semantic versioning)
- Documentation is actively being improved

## Features
- ✅ PDF/UA compliance out of the box
- ✅ Accessible PDFs
- ✅ Simple API and advanced Core Model
- 🚧 Dynamic content handling (in progress)
- 🚧 Extended error handling (in progress)

## Quick Start
see [Hello World Example](https://github.com/Katrin4487/JAccessPDF/wiki/create-first-pdf)

## Image Support

JAccessPDF supports **PNG, JPEG, GIF, WebP, and SVG** images out of the box.

### SVG Support

SVG images are automatically converted to PNG for PDF embedding. This approach:
- ✅ Works reliably across all PDF viewers
- ✅ Requires no additional dependencies (uses Apache Batik included with FOP)
- ✅ Produces high-quality output for logos and icons at 96 DPI

**No configuration needed** - just use SVG images like any other format:
```java
BlockImage logo = new BlockImage()
    .setPath("company-logo.svg")
    .setAltText("Company Logo");
```

### Technical Details

SVG support is provided by Apache Batik, which is included as a transitive
dependency of Apache FOP. JAccessPDF automatically detects Batik and uses it
for SVG→PNG conversion when needed.

**Limitations:**
- Complex SVG features (filters, animations, scripts) may not render correctly
- External references in SVG files are not supported
- Self-contained SVG files work bes
