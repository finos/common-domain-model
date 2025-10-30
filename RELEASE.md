# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` dependency.

Version updates include:
- `DSL` `9.68.0` Allow usage of the `switch` operator on types which exist in other namespaces. See DSL release notes: [DSL 9.68.0](https://github.com/finos/rune-dsl/releases/tag/9.68.0)

No expectations are updated as part of this release.

_Review Directions_

The changes can be reviewed in PR: [#4134](https://github.com/finos/common-domain-model/pull/4134)

# Qualification and Validation - Fix use of empty in conditions and qualification functions

*Background*

An upcoming DSL release has found a number of areas where the use of `empty` in qualification functions and conditions was not being handled correctly. This change contains fixes that prepare the model for the upcoming DSL release.

*What is being released?*

The following functions have been updated:

- Qualify_Substitution
    - Check for existence of `beforeEconomicterms -> terminationDate` instead of the existence of `beforeEconomicterms`.
    - Check for existence of `openEconomicTerms -> effectiveDate` and `openEconomicTerms -> terminationDate` instead of the existence of `openEconomicTerms`.

- Qualify_Roll
    - Check for existence of `beforeEconomicterms -> collateral` instead of the existence of `beforeEconomicterms`.
    - Check for existence of `openEconomicTerms -> collateral` instead of the existence of `openEconomicTerms`.

- UnderlierQualification
    - Check `securityType` exists before comparing to `instrumentType`.

- ObservableQualification
    - Check `securityType` exists before comparing to `instrumentType`.
    - Check `assetClass` exists before comparing to `Index ->> assetClass`.


The following types have been updated:

- NonNegativeQuantitySchedule
    - Split condition `NonNegativeQuantity_value` into two conditions `NonNegativeQuantity_value` and `NonNegativeQuantity_datedValue`.

- ValuationMethod
    - Check for existence of `quotationAmount -> unit -> currency` instead of the existence of `quotationAmount`.
    - Check for existence of `minimumQuotationAmount -> unit -> currency` instead of the existence of `minimumQuotationAmount`.

- FixedPrice
    - Split condition `NonNegativePrice_amount` into two conditions `NonNegativePrice_amount` and `NonNegativePrice_datedValue`.

*Review Directions*

Changes can be reviewed in PR: [#4118](https://github.com/finos/common-domain-model/pull/4118)

# Product Taxonomy Model - Adding "CFTC" value in TaxonomySourceEnum

_Background_

A gap has been identified in the model when capturing taxonomy values for commodity underlyer assets as defined by CFTC regulation. Introducing `CFTC` as a taxonomy source is necessary to properly map these values within the model and to support population of the **"Commodity Underlyer ID"** fields in DRR.

_What is being released?_

The contribution is the addition of a new `CFTC` value to the `TaxonomySourceEnum` in order to represent the Commodity Futures Trading Commission as a taxonomy source, enabling support for **Commodity Underlyer ID** rules under CFTC jurisdiction in DRR.

_Review Directions_

Changes can be reviewed in PR: [#4111](https://github.com/finos/common-domain-model/pull/4111)
