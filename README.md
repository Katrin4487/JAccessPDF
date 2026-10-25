# JAccessPDF

[![Build](https://github.com/Katrin4487/JAccessPDF/actions/workflows/build.yml/badge.svg)](https://github.com/Katrin4487/JAccessPDF/actions/workflows/build.yml)
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
    <version>0.12.0</version>
</dependency>
```

Gradle:

```kotlin
implementation("de.fkkaiser:jaccess-pdf-api:0.12.0")
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

### Optional: PDF/UA validation

`jaccess-pdf-validation` checks generated PDFs against PDF/UA-1 using
[veraPDF](https://verapdf.org), the reference validator:

```xml
<dependency>
    <groupId>de.fkkaiser</groupId>
    <artifactId>jaccess-pdf-validation</artifactId>
    <version>0.12.0</version>
</dependency>
```

It is kept separate on purpose. veraPDF brings 18 transitive artifacts (~16 MB)
and is GPLv3/MPLv2 dual-licensed, so nothing of it reaches the dependency tree
of anyone who does not ask for it - JAccessPDF itself stays Apache-2.0 with no
copyleft attached. With the module on the class path,
`PdfGenerationFacade.generateAndValidatePDF(...)` validates before returning;
without it, that method tells you which artifact to add rather than silently
skipping the check.

Validation is not cheap. Use it in tests, in staging or on samples - not on
every request of a high-throughput service.


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

```java
import de.fkkaiser.api.simplelayer.SimpleDocumentBuilder;
import java.util.List;

SimpleDocumentBuilder.create("My First Accessible PDF")
        .withLanguage("en")
        .addHeading("Hello World", 1)
        .addParagraph("This document is PDF/UA compliant out of the box.")
        .addUnorderedList(List.of("Tagged structure", "Document language", "Alt texts for images"))
        .build()
        .saveAs("hello.pdf");
```

That is the whole program. No FOP configuration, no XSL-FO, no tagging by hand.
The resulting PDF carries a `/StructTreeRoot` with `/MarkInfo /Marked true`, a
document title, `/Lang (en)` from `withLanguage`, and the PDF/UA-1 identifier
in its XMP metadata - the structure screen readers rely on and PDF/UA demands.

The builder also offers `addOrderedList`, `addImage` (with alt text),
`addTable`, `addMarkdown` and `addParagraph(text, styleName)`. Use
`toStream()` instead of `saveAs(...)` to get a `ByteArrayOutputStream`, for
instance to serve the PDF from a web application.

For full control over styles, fonts and page sequences there is the Core Model
behind `PdfGenerationFacade` - see the
[wiki](https://github.com/Katrin4487/JAccessPDF/wiki/create-first-pdf).

or the my new documentation [JAccessPDF Documentation](https://jaccesspdf.fkkaiser.de)

> The snippet above is verified by `ReadmeQuickStartTest`, so it compiles and
> produces a real PDF on every build.

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
