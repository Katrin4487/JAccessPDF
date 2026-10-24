# Changelog

## [0.12.0] - 2026-10-24

### Added
- New module `jaccess-pdf-validation`: validates generated PDFs against PDF/UA-1 using veraPDF.
  It is a deliberate opt-in - `jaccess-pdf-api` does **not** depend on it, so veraPDF and its
  18 transitive artifacts (~16 MB, GPLv3/MPLv2 dual-licensed) stay out of the dependency tree
  of everyone who does not want them. Add it explicitly to use validation.
- `PdfGenerationFacade.generateAndValidatePDF(...)`: generates a PDF and validates it before
  returning. The validator is discovered through `ServiceLoader`; without
  `jaccess-pdf-validation` on the class path the method throws `PdfValidationException` naming
  the artifact to add, rather than silently skipping the check. Validation is expensive - use
  it in tests, in staging, or on samples, not on every request.
- `JAccessConfig` with `JAccessConfigBuilder`, for configuring the default language, the
  default title and the strictness mode. Passed to `PdfGenerationFacade`; the single-argument
  constructor keeps working and uses `JAccessConfig.defaultConfig()`.
- `StrictnessMode` with three levels, deciding what happens when PDF/UA-relevant information
  is missing from a document:
  - `LENIENT` - fill the gap from the configuration, silently
  - `WARN` - fill the gap and log a warning naming the value used (**the default**)
  - `STRICT` - refuse to generate, throwing `JAccessStrictnessException`

  Covers a missing document language, a missing title, and images without alt text.

  **Please note:** in 1.x the default will become `STRICT`. Builds that rely on gaps being
  filled silently will then fail instead. Set the mode explicitly if you want today's
  behaviour to survive that change.
- `Metadata.builder()` without a title, as a counterpart to `Metadata.builder(String)`. Set
  one later with `MetadataBuilder.title(String)`, or leave it unset and let the strictness
  mode decide.

### Changed
- **`Metadata` no longer substitutes defaults for a missing language or title.** Its
  constructor used to replace a missing language with `"de-DE"` and a missing title with
  `"PDF Document"`, so `getLanguage()` and `getTitle()` can now return `null`. The defaults
  moved to `JAccessConfig` and are applied at generation time. This is what makes the
  strictness modes work at all: as long as `Metadata` quietly filled the gaps itself, nothing
  was ever missing by the time the generator looked, and `STRICT` could never fire.
- **The effective default language changed for documents built through the core model**, from
  `"de-DE"` to the configured `defaultLanguage`, which is `"en"` out of the box. Set
  `defaultLanguage("de-DE")` on the configuration to keep the previous behaviour.
- **A missing title is no longer rejected at construction.** Neither `MetadataBuilder(String)`
  (previously `IllegalArgumentException`) nor the `Metadata` constructor (previously
  `NullPointerException`) refuses one any more, and `setTitle(null)` is allowed. Whether a
  missing title is an error is now decided by the strictness mode - the only place that knows
  whether the caller wanted it enforced.
- **Generating a PDF no longer modifies the document it was given.** The fallback title used
  to be written back into the caller's `Metadata`. That made a second generation of the same
  document stop warning, and because `Metadata` is Jackson-serializable, the placeholder could
  end up stored as though the author had set it.
- `XslFoGenerator`'s constructor now takes a `JAccessConfig`. The class is marked `@Internal`,
  but it is public and exported, so this is listed for completeness.

### Fixed
- Missing language, title and image alt text are now also detected when they consist only of
  whitespace. A language of `"   "` used to end up verbatim in the document's `xml:lang`, and
  a whitespace-only alt text counted as present - the "technically there, useless to a screen
  reader" case the strictness modes exist to catch.
- Log messages about images without alt text now name base64 and inline SVG images instead of
  reporting `path 'null'`.

## [0.11.3] - 2026-10-24
### Added
- GitHub Actions build on JDK 21 and 25 (`.github/workflows/build.yml`), running
  `mvn verify` - which covers the tests, the license-header check and the
  javadoc/sources jars.
- `ReadmeQuickStartTest` compiles and runs the Quick Start snippet from the
  README on every build, so the documented example cannot go stale.

### Fixed
- Removed a leftover `System.out.println` in `ListItem` that printed
  `LIS <styleClass>` to stdout on every list item rendered.

### Changed
- Updated FOP to 2.12, which is compatible with JDK 25 and fixes [FOP-3275](https://issues.apache.org/jira/browse/FOP-3275).
- Deleted Saxon-Dependency (`net.sf.saxon:Saxon-HE`) that was a workaround for FOP-3275, no longer needed with FOP 2.12.
- Updated other dependencies to the latest versions (Jackson, Commonmark, JUnit Jupiter, license-maven-plugin).
- README: the Quick Start now contains the actual code instead of only linking
  to the wiki, and names the concrete PDF/UA features of the output
  (`/StructTreeRoot`, `/MarkInfo /Marked true`, `/Lang`, PDF/UA-1 in XMP).

## [0.11.2] - 2026-10-24
### Changed
- **Breaking: all module artifactIds are now prefixed with `jaccess-pdf-`**
  (`api` -> `jaccess-pdf-api`, `model` -> `jaccess-pdf-model`, and so on for
  `generator`, `processor`, `postprocessor`, `bundle` and `markdown`). The
  parent artifactId `jaccess-pdf` is unchanged. Consumers must update their
  dependency declarations. Done ahead of the first Maven Central release,
  where coordinates become permanent.
- Every module now declares its own `name`, `description` and `url` - required
  by Maven Central for each published artifact (`name` is not inherited from
  the parent).

### Added
- Maven Central publishing via a `release` profile: GPG signing and the
  central-publishing-maven-plugin. GitHub Packages stays the target of a plain
  `mvn deploy` for snapshots and releases; Central is additional and reached
  only with `-Prelease`. `release.sh` gained an optional deploy step that
  uploads to both (`--no-deploy` to skip it).

### Fixed
- Removed duplicate `jackson-databind` declarations in the `model` and
  `bundle` modules that each shadowed a test-scoped entry with a compile-scoped
  one and produced a "must be unique" warning on every build.

## [0.11.1] - 2026-10-23
### Fixed
- Added missing ElementStyle for Hyperlink

## [0.11.0] - 2026-10-23
### Added 
- The Markdown method is available in the SimpleDocumentBuilder API now.
- Block Converter for Markdown available (lists, headings, etc.)
- New module for parsing Markdown (inline) text and converting them to the internal document model
- Added standard TextStyles that are available from outside the library
- Added standard ElementStyles that are available from outside the library (StyleSheet)

### Fixed
- License housekeeping: Added missing license headers to all source files
- Added missing bold default font and missing space in generated XML (using default font)
- Corrected naming for default styles
- Added Forgotten @JsonIgnore to some getters that are not mirroring fields
- Headers in StandardElementStyles are now created with the correct HeaderProperties
- added missing JavaTimeModule to Facade's internal ObjectMapper (LocalDateTime fields like Metadata.creationDate could not be serialized)

## [0.10.5] - 2026-08-24
### Added
- New FontReferenceValidator for validating the references of TextStyles to FontFamilies and FontTypes in the StyleSheet
- Add new method in TextStyleFactory to use custom units for font sizes (e.g. pt, px, em, rem, etc.)

### Fixed
- Added Saxon-HE-Workaround for FOP-3275 (compatibility with JDK 25)

## [0.10.4]

### Added
- New enumeration for StyleListKey for JSON representation (makes the generation of JSON files easier)

### Fixed
- FallBack Mechanisms for Missing / Not Working RessourceProviders 

### Added
- New functionalities using Templates (MasterBundles) with Placeholders and Visibility Condition
- ZipBundles for Templates (Im/Export) available
## [0.10.3]
### Fixed
- Updated Mockito for Java 25 compatibility

### Changed
- Plugin Architecture for SVG added: SVG images are converted to PNG using Apache Batik now (if available in clasthpath (currently included in FOP dependencies))

### Added
- .svg is supported as image format (also for list-items) now (converted to .png using Apache Batik)
- Builder chain methods for creating Tables added
- Adding option adding Default Styles for different elements in Stylesheet / style.json
- It's possible to add svg's and base64 strings directly (no path to file needed)
- BlockImageBuilder added


### Fixed
- Fixed issue with incorrect value for text-align in TextBlocks

## [0.10.2] - 25-12-05

### Changed
- ParagraphBuilder has its own class now
- TextAlign is now an enum instead of a string
- Span is now an enum instead of a string
- Widows and Orphans are integers in Paragraphs now (no booleans)
- TextAlignLast in Paragraph can be used with TextAlign enum now
- ElementTypes and InlineElementTypes is replaced wit ElementTargetTypes (enum instead of strings)
- ListStyleTypes are enums now (String is not allowed anymore) and only bullet, small dot, hyphen and none

### Added
- SimpleListBuilder for easier creation of SimpleLists
- ListStyleType enum for easier creation of SimpleLists

### Fixed
- Better title handling in metadata -> title is set as title with default language (so that veraPDF check not fails)
- to prevent errors because of subsetting -> activated subsetting always for fonts
- Fixed issue (FOP Bug?): Error (not in Struture Tree) when using note: replaced Note-Role in Section with Div
- Keep with next works with headlines now
- Linefeed Treatment has the correct value now

## [0.10.1] - 2025-12-02
### Changed
- New Inheritance logic for styles: Only if annotation is set to INHERIT, styles are inherited from parent elements. Otherwise, only the explicitly set style parameters are used.
- Only LinefeedTreatment is addable with method setLinefeedTreatment now.

### Added
- Validation integrated: if a section-marker is set, you have to add a text-style-name (see Fixed)
- JavaDocs updated and missing comments added (work in progress)
- Enum LinefeedTreatment added for handling linefeeds in text elements

### Fixed
- Section Params are not ignored anymore
- section-marker needs a text style: You can add a text-style-name to section now.
- Bookmark generation fixed: Bookmarks are generated correctly now


## [0.10.0] - 2025-12-01

### Changed
- PageMasterStyle now has sensible defaults (A4 Portrait, 2cm margins)
- Section can be Note or Aside now (reading chosen variant from the schema)
- Part can be Part or Article now (reading chosen variant from the schema)
- Part can be used as top-level container now
- heading ids are unique now (based on the document structure)

### Added
- Copyright headers
- Checks and normalizers regarding the dimension values in the PageMasterStyle
- Section can have a altText 
- Enum SectionVariant added (Note, Aside)
- More parameters for SectionStyle available
- PageBreakVariant enum added (Always, Avoid, Even, Odd)
- Checks for border styles and dimensions in Section and Part styles
- New module postprocessor for merging multiple PDF documents into one (experimental)
- PDFEncryptor in postprocessor module for adding password protection to PDFs (experimental)
- PDFFormFiller in postprocessor module for filling PDF forms (experimental)


## [0.9.2] - 2025-11-29
- Apache License 2.0 license added
## [0.9.1] - 2025-11-29
### Added
- LICENSE and NOTICE files
- 
## [0.9.0] - 2025-11-28
### Added
- Initial public release
- Core Model layer
- Simple Document layer
- PDF/UA compliance