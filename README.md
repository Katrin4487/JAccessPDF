# JAccessPDF

[![Maven Central](https://img.shields.io/maven-central/v/de.fkkaiser/jaccess-pdf-api)](https://central.sonatype.com/artifact/de.fkkaiser/jaccess-pdf-api)
[![Status](https://img.shields.io/badge/status-beta-yellow)]()
[![License](https://img.shields.io/badge/license-Apache--2.0-blue)](LICENSE)

A Java library for creating PDF/UA compliant PDFs with Apache FOP.

## Installation

Add the `jaccess-pdf-api` module - it transitively pulls in the model, processor, generator, markdown and bundle modules:

```xml
<dependency>
    <groupId>de.fkkaiser</groupId>
    <artifactId>jaccess-pdf-api</artifactId>
    <version>0.11.2</version>
</dependency>
```

Gradle:

```kotlin
implementation("de.fkkaiser:jaccess-pdf-api:0.11.2")
```

<details>
<summary>Individual modules</summary>

`jaccess-pdf-api` already depends on all of these except `jaccess-pdf-postprocessor`, which you add separately if you need PDF merging, encryption or form filling:

| Artifact | Contents |
|---|---|
| `jaccess-pdf-api` | Public entry point: `PdfGenerationFacade`, `SimpleDocumentBuilder`, resource providers |
| `jaccess-pdf-model` | Document model: elements, styles, fonts |
| `jaccess-pdf-generator` | Document model to tagged XSL-FO |
| `jaccess-pdf-processor` | Reads documents, style sheets and fonts from JSON |
| `jaccess-pdf-postprocessor` | Merging, encryption, form filling |
| `jaccess-pdf-markdown` | Markdown to document model |
| `jaccess-pdf-bundle` | Externalized text bundles and templating |

</details>


## ⚠️ Current Status: Beta

This library is functional and being used in production, but:
- Some features are still being finalized
- API may change in minor versions (following semantic versioning)
- Documentation is actively being improved

## Requirements

- Java 21 or higher (tested with JDK 21 and 25)

## Features
- ✅ PDF/UA compliance out of the box
- ✅ Accessible PDFs
- ✅ Simple API and advanced Core Model
- ✅Dynamic content handling and Templating
- ✅ Extended error handling 

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
