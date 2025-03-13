# *CDM Model - Change to CDM 5.x.x for Equity Products*

_Background_

This release contains modifications required to accomodate Equity and Exotic Products under individual Asset Classes

_What is being released?_

This release creates following modifications:
- following new qualification functions are added
  - `Qualify_Equity_OtherSwap` - This function qualifies a product as an Equity Swap (Other) where the base product qualifies as Equity Swap with non standard terms.
  - `Qualify_Credit_OptionOther` - This function qualifies a product as a Credit Option (Other) where the base product qualifies as Credit Option with non standard terms.
  - `Qualify_Commodity_OptionOther` - This function qualifies a product as an Commodity Option (Other) where the base product qualifies as Commodity Option with non standard terms.
  
```
func Qualify_Equity_OtherSwap: <"Qualifies a product as an Equity Swap (Other) with properties of an Equity Swap with non standard terms">
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

func Qualify_Credit_OptionOther: <"Qualifies a product as an Credit Option (Other) with properties of an Credit Option with non standard terms">
    [qualification Product]
    inputs:
        economicTerms EconomicTerms (1..1)
    output:
        is_product boolean (1..1)
    set is_product:
        economicTerms -> payout -> optionPayout only exists
            and Qualify_AssetClass_Credit(economicTerms) = True
            and economicTerms -> nonStandardisedTerms = True

func Qualify_Commodity_OptionOther: <"Qualifies a product as an Commodity Option (Other) with properties of an Commodity Option with non standard terms">
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

Original Issue: [#3476](https://github.com/finos/common-domain-model/issues/3476)
Changes can be reviewed in PR: [#3511](https://github.com/finos/common-domain-model/pull/3511)
