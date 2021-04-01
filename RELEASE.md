# *Model Change - Modified Money data type to use the new standard Quantity data type*
_What is being released?_

This release modifies the `Money` type by making it an extension of `Quantity`, removing the existing attributes in 'Money', and adding a condition that requires the `unitOfAmount` to be a `currency`.

The following functions required minor modifications to point-to/use the new location of currency in the 'Money' data type:
- `Create_Transfer`
- `CreditSupportAmount`
- `DeliveryAmount`
- `EquityCashSettlementAmount`
- `PostedCreditSupportItemAmount`
- `ReturnAmount`
- `UndisputedAdjustedPostedCreditSupportAmount`

The following function required no change after the `Money` data type was changed:
- `SumPostedCreditSupportItemAmounts`

The data type for the following attribute was changed from `Money` to `Price`:
- `UnitContractValuationModel`->`unitPrice`

The following data types required no change after the `Money` data type was changed
- `BondPriceAndYieldModel`
- `CalculationPeriod`
- `Cashflow`
- `CashSettlementTerms`
- `CashTransferBreakdown`
- `CashTransferComponent`
- `ConcentrationLimit`
- `CreditEvents`
- `CustodianTerms`
- `ElectiveAmountElection`
- `ExerciseFee`
- `ExerciseFeeSchedule`
- `FailureToPay`
- `InitialMargin`
- `PartialExercise`
- `PaymentCalculationPeriod`
- `PaymentDetail`
- `PaymentDiscounting`
- `PercentageRule`
- `Position`
- `PostedCreditSupportItem`
- `PremiumExpression`
- `PrincipalExchange`
- `SecurityLeg`
- `SimplePayment`
- `StubValue`

_Review directions_

In the CDM Portal, select the Textual Browser and search for any of the changes specified above. 
