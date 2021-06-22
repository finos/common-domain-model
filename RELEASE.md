# *DSL Syntax - New keywords to compare a list to a single data object*

_What is being released?_

New keywords have been introduced in the DSL syntax to compare a list of items to a single data object. Currently comparing a list to single data object will only result to true if all the items of the list match the single data object.  The `all` and `any` keywords can now be used to extend the comparison outcome with any equality operators: `=`, `<>`, `>`, `>=`, `<`, `<=`.

In the examples below, `payout -> interestRatePayout` is a list according to the cardinality of its definition in the model.

The `all` keyword will be used to specify that *all* items in the list must match the single data object. Accordingly, the statement will evaluate to true if each `paymentDates -> paymentFrequency -> period` for every item of the list `interestRatePayout` is equal to `T`.

- `economicTerms -> payout -> interestRatePayout -> paymentDates -> paymentFrequency -> period all = PeriodExtendedEnum -> T`

The `any` keyword will be used to specify that *any* item in the list must match the single data object. Accordingly, the statement will evaluate to true if at least one `paymentDates -> paymentFrequency -> period` for any item of the list `interestRatePayout` is equal to `T`.

- `economicTerms -> payout -> interestRatePayout -> paymentDates -> paymentFrequency -> period any = PeriodExtendedEnum -> T`

All list comparisons in the model have been updated to use the `all` or `any` keywords whilst retaining the original expected logical outcome.

In the CDM Documentation, review the following sections:

- [List comparison operators](https://docs.rosetta-technology.io/dsl/expressions.html#list-comparison-operators)

_Review Directions_

In the CDM Portal, use the Textual Browser to review the list comparisons in the model, including the following examples:

- Event qualification functions - `Qualify_CashTransfer`, `Qualify_Novation`
- Product qualification functions - `Qualify_InterestRate_IRSwap_FixedFloat`, `Qualify_InterestRate_IRSwap_FixedFloat_OIS`
- Conditions - `PriceQuantity -> NonNegativeQuantity`, `CreditDefaultPayout -> FpML_cd_13`, `CreditDefaultPayout -> FpML_cd_14`
