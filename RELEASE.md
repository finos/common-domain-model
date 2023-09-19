# _Infrastructure - Dependency Update_

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

This release updates the rosetta-dsl dependency.

Version updates include:
- 8.6.0: Adds annotations to the generated Java code that capture information to better serialise from and to the CDM. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/8.6.0.
- 8.6.1: Fixes parsing bugs, one related to the `only exists` operation, one responsible for making validation of Rosetta files order dependent. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/8.6.1.
- 8.6.2: Adds the display name to the annotations of enum values in the generated Java code. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/8.6.2.

There are no changes to the model, so the test expectations remain the same.

The changes can be reviewed in PR [#2160](https://github.com/finos/common-domain-model/pull/2160).