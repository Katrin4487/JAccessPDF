# Changelog

## [Unreleased]
### Changed
-- nothing yet --

### Added
- Validation integrated: if a section-marker is set, yo have to add a text-style-name (see Fixed)

### Fixed
- Section Params are not ignored anymore
- section-marker needs a text style: You can add a text-style-name to section now.


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