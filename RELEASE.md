# *CDM Model - Equity Products*

_Background_

This release contains modifications required to accomodate Equity and Exotic Products under individual Asset Classes

_What is being released?_

This release creates following modifications:
- a new qualification function `Qualify_Equity_OtherOption` to add qualification for Exotic Options
```
func Qualify_Equity_OtherOption: <"Qualifies a product with properties of an Exotic Option as Equity Option (Other)">
    [qualification Product]
    inputs:
        economicTerms EconomicTerms (1..1)
    output:
        is_product boolean (1..1)
            [synonym ISDA_Taxonomy_v1 value "Qualify_EquityOther"]
            [synonym ISDA_Taxonomy_v2 value "Qualify_EquityOther"]
    set is_product:
        Qualify_AssetClass_Equity(economicTerms) = True
            and economicTerms -> payout -> optionPayout only exists
            and economicTerms -> nonStandardisedTerms = True
```
- changes in existing options to avoid duplicate qualification of options
```
e.g.
func Qualify_EquityOption_PriceReturnBasicPerformance_SingleName
now contains additional clause
and (if economicTerms -> nonStandardisedTerms exists then economicTerms -> nonStandardisedTerms = False else True)
```

- modification in FpML conditions `FpML_ird_9` and `FpML_ird_29`
```
    condition FpML_ird_9: <"FpML validation rule ird-9 - If calculationPeriodAmount/calculation/compoundingMethod exists, then resetDates must exist.">
        if compoundingMethod exists and compoundingMethod <> CompoundingMethodEnum -> None then resetDates exists
    condition FpML_ird_29: <"FpML validation rule ird-29 - If compoundingMethod exists, then fixedRateSchedule must not exist.">
        if compoundingMethod exists and compoundingMethod <> CompoundingMethodEnum -> None
        then rateSpecification -> FixedRateSpecification is absent
```
- relaxation of cardinality rule for `expirationTime`
```
    expirationTime BusinessCenterTime (0..1) <"The latest time for exercise on expirationDate. It is made mandatory given that for all option styles, this field is required.">
    expirationTimeType ExpirationTimeTypeEnum (1..1) <"The time of day at which the equity option expires, for example the official closing time of the exchange.">

condition ExpirationTimeChoice: <"Condition to validate the correlation between expirationTime and expirationTimeType">
        (if expirationTime exists then expirationTimeType = ExpirationTimeTypeEnum -> SpecificTime and 
        if expirationTimeType = ExpirationTimeTypeEnum -> SpecificTime then expirationTime exists)
```
- modification to `Qualify_AssetClass_Commodity`
```
addition of clause:
        or economicTerms -> payout -> settlementPayout only exists)
```
_Review Directions_

In Rosetta, select the Textual Browser and inspect the changes identified above.

- modification to `Qualify_Commodity_Forward`
```
addition of clause:
        or // Price Return Vanilla Forward
        (economicTerms -> payout -> settlementPayout only exists))
```
_Review Directions_

In Rosetta, select the Textual Browser and inspect the changes identified above.

The changes can be reviewed in PR: [#3278](https://github.com/finos/common-domain-model/pull/3278).
