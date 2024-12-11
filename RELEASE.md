# *CDM Model - Equity Products*

_Background_

This release contains modifications required to accomodate Equity and Exotic Products under individual Asset Classes.

_What is being released?_

This release creates the following modifications:

**Qualification functions**
- Added a new qualification function for Equity Exotic Options: `Qualify_Equity_OtherOption`, that uses `nonStandardisedTerms` attribute to identify when an option is Exotic.
- For existing Equity Option qualifications functions, `nonStandardisedTerms` is negatively tested to prevent redundant qualification.

**Validation conditions**
- `InterestRatePayout`:
  - FpML conditions `FpML_ird_9` and `FpML_ird_29` are relaxed when `compoundingMethod` is `None` (instead of just when absent).
- `ExerciseTerms`:
  - Attribute `expirationTime` relaxed to be optional (previously mandatory).
  - Attribute `expirationTimeType` tightened to be mandatory (previously optional).
  - Addition of validation condition `ExpirationTimeChoice` to establish the correlation between `expirationTime` and `expirationTimeType`: `expirationTimeType` must be set to `SpecificTime` when `expirationTime` is specified (and conversely).

_Backward incompatible changes_

The `ExerciseTerms` validation change is backward incompatible and all affected samples have been updated to ensure that `expirationTimeType` is populated as `SpecificTime` when the `expirationTime` attribute is populated.

See for example: [`fpml-5-13 > fx-ex09-euro-opt`](https://github.com/finos/common-domain-model/blob/master/rosetta-source/src/main/resources/cdm-sample-files/fpml-5-13/products/fx-derivatives/fx-ex09-euro-opt.xml)

_Review Directions_

Please inspect the changes identified above for the functions and types in the Textual Viewer Rosetta.

Please inspect the changes to option samples using the Ingestion Panel in Rosetta.

The changes can also be reviewed in PR: [#3278](https://github.com/finos/common-domain-model/pull/3278).
