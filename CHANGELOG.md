# Changelog

## [Unreleased]

### Changed
- ParagraphBuilder has its own class now
- TextAlign is now an enum instead of a string
- Span is now an enum instead of a string
- Widows and Orphans are integers in Paragraphs now (no booleans)
- TextAlignLast in Paragraph can be used with TextAlign enum now

### Added
- SimpleListBuilder for easier creation of SimpleLists

### Fixed
- Better title handling in metadata -> title is set as title with default language (so that veraPDF check not fails)
- to prevent errors because of subsetting -> activated subsetting always for fonts
- Fixed issue (FOP Bug?): Error (not in Struture Tree) when using note: replaced Note-Role in Section with Div

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