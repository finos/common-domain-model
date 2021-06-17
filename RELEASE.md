# *DSL Syntax - List comparison keywords*

_What is being released?_

New keywords have been introduced to be used when comparing a list to a single value. Currently a list to single value comparison will only evaluate to true if all the list items match the single value.  The `all` and `any` keywords are used with equality operators; `=`, `<>`, `>`, `>=` etc.

The `all` keyword is used to specify that *all* list items must match the given value.  In the example below, `payout -> interestRatePayout` has multiple cardinality, so for the statement to evaluate to true, the `interestRatePayout -> paymentDates -> paymentFrequency -> period` must equal `T` on each and every `interestRatePayout`.

- `economicTerms -> payout -> interestRatePayout -> paymentDates -> paymentFrequency -> period all = PeriodExtendedEnum -> T`

The `any` keyword is used to specify that *any* list item must match the given value.  In the example below, for the statement to evaluate to true, the `interestRatePayout -> paymentDates -> paymentFrequency -> period` must equal `T` on at least one of the `interestRatePayouts`.

- `economicTerms -> payout -> interestRatePayout -> paymentDates -> paymentFrequency -> period any = PeriodExtendedEnum -> T`
  
All list comparisons in the model have been updated to use `all` or `any` keywords.

In the CDM Documentation, review the following sections:

- [List comparison operators](https://docs.rosetta-technology.io/dsl/expressions.html#list-comparison-operators)

_Review Directions_

In the CDM Portal, use the Textual Browser to review the list comparisons in the model, including the following examples:

- Event qualification functions - `Qualify_CashTransfer`, `Qualify_Novation`
- Product qualification functions - `Qualify_InterestRate_IRSwap_FixedFloat`, `Qualify_InterestRate_IRSwap_FixedFloat_OIS`
- Conditions - `PriceQuantity -> NonNegativeQuantity`, `CreditDefaultPayout -> FpML_cd_13`, `CreditDefaultPayout -> FpML_cd_14`
