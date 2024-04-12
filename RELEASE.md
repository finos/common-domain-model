# _Python Generator v2_

_What is being released?_
This release uses the new version of the Python generator (v2) which includes the following changes:

- Migration to Pydantic 2.x
- More comprehensive support for Rosetta's operators
- Resolves the defect exposed by [PR 2766](https://github.com/finos/common-domain-model/pull/2766)
- Includes an update to the Python Rosetta runtime library used to encapsulate the Pydantic support (now version 2.0.0)

# _Product Model - Synonym mappings for BusinessCenterEnum_

_Background_

The version 2-17 of the FpML coding schemes was recently published. This new version included some changes that are already present in the corresponding enumerations of the CDM model, but the synonym mappings from FpML to CDM have not been updated to cover the latest changes.
This release introduces support for the synonym mappings, to cover the changes in v2-17 of FpML coding schemes for `BusinessCenterEnum`.

_What is being released?_

- Added mapping coverage for all missing values of `BusinessCenterEnum`.

_Review directions_

In Rosetta, select the Textual View and inspect the change identified above.

PR: [#2857](https://github.com/finos/common-domain-model/pull/2857)
