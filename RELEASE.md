## Ingest - Fix use of empty in conditions and qualification functions

*Background*

An upcoming DSL release has found a number of areas where the use of empty in qualification functions and conditions was not being handled correctly. This change contains fixes that readying the model for the DSL release. 

*What is being released?*

The following functions have been updated:

- Qualify_Substitution
  - Check for existence of `beforeEconomicterms -> terminationDate` instead of just `beforeEconomicterms` existence.
  - Check for existence of `openEconomicTerms -> effectiveDate` and `openEconomicTerms -> terminationDate` instead of just `openEconomicTerms` existence.

- Qualify_Roll
  - Check for existence of `beforeEconomicterms -> collateral` instead of just `beforeEconomicterms` existence.
  - Check for existence of `openEconomicTerms -> collateral` instead of just `openEconomicTerms` existence.

- UnderlierQualification
  - Check `securityType` exists before comparing to `instrumentType`.

- ObservableQualification
  - Check `securityType` exists before comparing to `instrumentType`.
  - Check `assetClass` exists before comparing to `Index ->> assetClass`.


The following types have been updated:

- NonNegativeQuantitySchedule
    - Split condition `NonNegativeQuantity_value` into two conditions `NonNegativeQuantity_value` and `NonNegativeQuantity_datedValue`.

- ValuationMethod
  - Check for existence of `quotationAmount -> unit -> currency` instead of just `quotationAmount` existence.
  - Check for existence of `minimumQuotationAmount -> unit -> currency` instead of just `minimumQuotationAmount` existence.

- FixedPrice
  - Split condition `NonNegativePrice_amount` into two conditions `NonNegativePrice_amount` and `NonNegativePrice_datedValue`.

- OptionPayout
  - Split condition `AsianOptionChoice` into two conditions `AsianOptionChoice_averagingStrikeFeature` and `AsianOptionChoice_averagingFeature`.

*Review Directions*

Changes can be reviewed in PR: [#4113](https://github.com/finos/common-domain-model/pull/4113)
