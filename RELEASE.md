# *Infrastructure - Dependency Update*

## Changes Made:
#### Updated Readme.md and fixed issues in cdm.finos.org.
- Updated Governance.md and Code of Conduct with links replaced by Terms of Reference PDFs, now located in the "docs" directory.
- Added relative links in the readme to ensure they work seamlessly after merging.
- Included Common Domain Model in the Open Source at FINOS PDF in the Readme.md section.
- Added a roadmap section for upcoming releases.
- Edited the "Getting Involved" section.
- Updated icons for cdm.finos.org.
- Updated the banner for cdm.finos.org.
- Refined the text to explain what Common Domain Model (CDM) is and its benefits.
- Included a PDF document explaining what CDM is.

This release updates the `rosetta-dsl` dependency.

Version updates include:
- `rosetta-dsl` 8.8.1: Changes to support serialisation to XML. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/8.8.1.

There are no changes to the model. Test expectations have not changed functionally, but
the order of the JSON attributes now follows the order of the Rosetta model. Additionally,
enum values in expectations now respect the naming convention of the Rosetta model.

