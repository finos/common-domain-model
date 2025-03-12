# *CDM Model - Change to CDM 5.x.x for Equity Products*

_Background_

This release contains modifications required to accomodate Equity and Exotic Products under individual Asset Classes

_What is being released?_

This release creates following modifications:
- following new qualification functions are added
  - `Qualify_Equity_OtherSwap`
  - `Qualify_Credit_OptionOther`
  - `Qualify_Commodity_OptionOther`
```
func Qualify_Equity_OtherSwap: <"Qualifies a product as an Equity Swap for which the performance is based on the price changes and dividend returns on an index.  The determination of the qualification is based on the economic terms and the following criteria: 1) An equity product with one performance leg and one interest leg 2) with the former featuring priceReturnTerms and dividendReturnTerms, 3) the underlier is an index, and 4) there are no option features.">
    [qualification Product]
    inputs:
        economicTerms EconomicTerms (1..1)
    output:
        is_product boolean (1..1)

    set is_product:
        // qualifies that the Base Product is an Equity Swap (i.e.: only performance, interest rate or fixed price payouts)
        Qualify_BaseProduct_EquitySwap(economicTerms) = True
            and 
            ((economicTerms -> payout -> interestRatePayout, economicTerms -> payout -> performancePayout) only exists
                    and economicTerms -> payout -> interestRatePayout count = 1
                    and economicTerms -> payout -> performancePayout count = 1)
            and economicTerms -> nonStandardisedTerms = True

func Qualify_Credit_OptionOther: <"This product qualification is temporary until such time that the ISDA Credit Group specifies a proper taxonomy for credit derivatives that is based upon economic terms.">
    [qualification Product]
    inputs:
        economicTerms EconomicTerms (1..1)
    output:
        is_product boolean (1..1)
    set is_product:
        economicTerms -> payout -> optionPayout only exists
            and Qualify_AssetClass_Credit(economicTerms) = True
            and economicTerms -> nonStandardisedTerms = True

func Qualify_Commodity_OptionOther: <"Qualifies a product as a Option that can be exercised into an Commodity.">
    [qualification Product]
    inputs:
        economicTerms EconomicTerms (1..1)
    output:
        is_product boolean (1..1)
            [synonym ISDA_Taxonomy_v1 value "Commodity_Option"]
            [synonym ISDA_Taxonomy_v2 value "Commodity_Option"]
    set is_product:
        Qualify_AssetClass_Commodity(economicTerms) = True
            and economicTerms -> payout -> optionPayout only exists
            and economicTerms -> nonStandardisedTerms = True
```

- changes in existing options to avoid duplicate qualification of options
```
1. func Qualify_AssetClass_InterestRate
```
existing clause
```
or (economicTerms -> payout -> forwardPayout only exists
```
is now modified to
```
or ((economicTerms -> payout -> forwardPayout only exists or 
                (economicTerms -> payout -> forwardPayout exists
                and (economicTerms -> payout -> interestRatePayout exists
                    or economicTerms -> payout -> cashflow exists)))
```
```
2. func Qualify_AssetClass_Equity
```
existing clause
```
or (economicTerms -> payout -> forwardPayout only exists
```
is now modified to
```
or ((economicTerms -> payout -> forwardPayout only exists
                    or (economicTerms -> payout -> forwardPayout exists
                        and (economicTerms -> payout -> interestRatePayout exists or economicTerms -> payout -> cashflow exists)))
```


Changes can be reviewed in PR: [#3163](https://github.com/finos/common-domain-model/issues/3163)
