# *CDM Model - Equity Products*

_Background_

This release contains modifications required to accomodate Equity and Exotic Products under individual Asset Classes

_What is being released?_

This release creates following modifications:
- a new qualification function `Qualify_Equity_OtherOption` to add qualification for Exotic Options. The function uses `nonStandardisedTerms` attribute to differentiate between `Exotic Options` and Regular Options. For the existing option qualifications functions, `nonStandardisedTerms` is negatively tested to prevent multiple qualification


- FpML conditions `FpML_ird_9` and `FpML_ird_29` are relaxed when `compoundingMethod` is not populated 

- relaxation of cardinality rule for `expirationTime`. The attribute is now optional.
- attribute `expirationTimeType` is now mandatory.
- addition of validation condition to establish the correlation between `expirationTime` and `expirationTimeType` 

_Review Directions_

In Rosetta, select the Textual Browser and inspect the changes identified above.

The changes can be reviewed in PR: [#3278](https://github.com/finos/common-domain-model/pull/3278).
