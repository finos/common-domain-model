# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` dependency.

Version updates include:
- `DSL` `9.68.0` Fix issue that prevents switching over types which exist in other namespaces. See DSL release notes: [DSL 9.68.0](https://github.com/finos/rune-dsl/releases/tag/9.68.0)

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

- ExpirationTimeType
  - Make function only applicable when `expirationTime` and `expirationTimeType` are both present.

The following types have been updated:

- NonNegativeQuantitySchedule
  - Split condition `NonNegativeQuantity_value` into two conditions `NonNegativeQuantity_value` and `NonNegativeQuantity_datedValue`.

- ValuationMethod
  - Check for existence of `quotationAmount -> unit -> currency` instead of the existence of `quotationAmount`.
  - Check for existence of `minimumQuotationAmount -> unit -> currency` instead of the existence of `minimumQuotationAmount`.

- FixedPrice
  - Split condition `NonNegativePrice_amount` into two conditions `NonNegativePrice_amount` and `NonNegativePrice_datedValue`.

*Review Directions*

Changes can be reviewed in PR: [#4121](https://github.com/finos/common-domain-model/pull/4121)
