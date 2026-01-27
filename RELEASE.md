# *Ingestion mappings and CDM model validation rules. - Updates CDM model rules and FpML mappings to resolve validation issues and inconsistencies.*

_Background_

This PR aims to improve the CDM model and mappings to achieve better validation results.

_What is being released?_

**Overview**
This release focuses on improving FpML ingestion mappings and CDM model alignment to enhance validation accuracy and ensure consistent trade representation.

**Summary of Changes**
The following updates have been included in this pull request:

1. `ingest-fpml-confirmation-other-func.rosetta`
    - **MapWeeklyRollConventionEnum**: Completed the implementation by adding missing roll convention mappings.
2. `ingest-fpml-confirmation-party-func.rosetta`
    - **MapRelatedPartyToPartyRole**: Added support for **ClearingBroker** as a synonym of **ClearingFirm**.
3. `ingest-fpml-confirmation-product-fra-func.rosetta`
   - **MapFraToFixedInterestRatePayout**: Removed the **paymentDates** mapping for FRA products, as FRAs support only a single payment and should use **paymentDate** instead.
4. `ingest-fpml-confirmation-product-swap-func.rosetta`
   - **MapOptionalEarlyTermination**: Implemented missing **exerciseNotice** mapping.
   - **MapCalculationPeriodAmountToPriceList**: Updated **FixedRateSpecification** mapping to include the **IRD_29** condition (via `MapSwapPayout`).
5. `product-common-settlement-type.rosetta`
   - **FxFixingDate**: Aligned with FpML by enforcing a choice between **dateRelativeToPaymentDates** and **dateRelativeToCalculationPeriodDates**.
6. `product-template-type.rosetta`
   - **MultipleExercise** / **PartialExercise**: Updated cardinality to make **notionalReference** optional, aligning with the FpML specification.

**Impact**

These changes improve mapping completeness, reduce validation errors, and strengthen alignment between FpML samples and the CDM model.

_Review Directions_

Changes can be reviewed in PR: [#4394](https://github.com/finos/common-domain-model/pull/4394).
