# *CDM Model - Change to CDM 5.x.x for Equity Products*

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
        if compoundingMethod exists then resetDates exists

    condition FpML_ird_29: <"FpML validation rule ird-29 - If compoundingMethod exists, then fixedRateSchedule must not exist.">
        if compoundingMethod exists
        then rateSpecification -> fixedRate is absent
```
- relaxation of cardinality rule for `expirationTime`
```
expirationTime BusinessCenterTime (0..1) <"The latest time for exercise on expirationDate.">

condition ExpirationTimeChoice: <"Condition to validate the existence of correlation between expirationTime and expirationTimeType">
    ExpirationTimeType(expirationTime, expirationTimeType)

func ExpirationTimeType: <"Conditional Validation function to check the existence of correlation between expiration Time and expiration Time Type.">
    inputs: 
        expirationTime BusinessCenterTime (0..1)
        expirationTimeType ExpirationTimeTypeEnum (0..1)
    output: 
        success boolean (1..1)
    
    set success:
        (if expirationTime exists and expirationTimeType exists then expirationTimeType = ExpirationTimeTypeEnum -> SpecificTime)
        and 
        (if expirationTimeType exists and expirationTimeType = ExpirationTimeTypeEnum -> SpecificTime then expirationTime exists) 

```
- modification to `Qualify_AssetClass_Commodity`
```
addition of clause:
or (economicTerms -> payout -> forwardPayout, economicTerms -> payout -> commodityPayout) only exists
or economicTerms -> payout -> forwardPayout only exists)
```
_Review Directions_

In Rosetta, select the Textual Browser and inspect the changes identified above.

Changes can be reviewed in PR: [#3163](https://github.com/finos/common-domain-model/issues/3163)

