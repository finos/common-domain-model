# _Event Model - Repo partial delivery, reprice, adjustment, and substitution._

_Background_

This release includes event functions and qualifications to support repo partial delivery, reprice, adjustment and substitution. 

_Partial Delivery

In the repo market it is a recommended best practice that if a seller fails to deliver the full amount of collateral
on settlement date, that the buyer agrees to accept the amount of collateral delivered and agree on the remaining amount and when
it will be delivered.

_What is being released?_

A new function `Create_PartialDeliveryPrimitiveInstruction` is added that accepts inputs of the delivered amount and undelivered amount. 
The function closes the existing trade and opens two new trades, one based on the delivered quantity and one based on the undelivered
quantity. The function returns a primitive instruction that can be used to execute a business event. 
There is a also a new qualification function `Qualify_PartialDelivery` that validates the results meet the partial delivery criteria.

_Reprice and Adjustment

Repricing and Adjustment are methods used to eliminate net exposures between counterparties. In both methods the original transaction is terminated
and a new transaction is opened for the remaining term. In repricing, the purchase price of the new transaction is set equal to the market value of the
collateral. The difference between the repurchase price of the terminated transaction and the purchase price of the new transaction is paid net. 

In an adjustment the nominal value of the collateral is changed to bring the market value at the new market price equal to the purchase price of the 
original transaction. The difference between the new nominal collateral amount and orginal nominal collateral amount should be delivered net.
 

_What is being released?_

A new function `Create_RepricePrimitiveInstruction` is added that accepts inputs of a new collateral price and cash
amount. The new function adjusts the amounts and returns a primitive instruction that is used together with a business event to
modify the trade and trade state.

A new function `Create_AdjustmentPrimitiveInstruction` is added that accepts inputs of a new collateral price and collateral amount.
The new function adjusts the amounts and returns a primitive instruction that is used together with a business event to
modify the trade and trade state.

Net cash or securities transfers must be processed separately.

_Substitution

In the repo market, if agreed by the counterparties on the contract, it is possible for the seller to replace collateral that was originally
delivered at settlement with different collateral. The new collateral will most likely have a new quantity and price. 

_What is being released?_

A new function `Create_SubstitutionPrimitiveInstruction` is added that accepts inputs of a new payout that include new cash and collateral
amounts and price, and returns a new primitive instruction that can be used to execute a business event. In the case where
there was a contractual agreement to substitute collateral the original trade remains with reference to it's contract, identifiers, and 
other details. There is a also a new qualification function `Qualify_Substitution` that validates the results meet the substitution criteria.

[PR #2529](https://github.com/finos/common-domain-model/pull/2529)

# *Infrastructure - Dependency Update*

_What is being released?_

This release updates the `rosetta-dsl` dependency.

Version updates include:
- `rosetta-dsl` 9.1.2: this release fixes DSL issues [#670](https://github.com/REGnosys/rosetta-dsl/issues/670) and [#653](https://github.com/REGnosys/rosetta-dsl/issues/653). For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.1.2.
- `rosetta-dsl` 9.1.3: this release fixes an issue related to the generated Java `process` method. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.1.3.

To ease the process of reviewing expectation changes,
the formatting of the following expectation files has also been improved:
- `cdm-sample-files/functions/repo-and-bond/repo-adjustment-input.json`
- `cdm-sample-files/functions/repo-and-bond/repo-reprice-input.json`
- `cdm-sample-files/functions/repo-and-bond/repo-substitution-input.json`

_Review directions_

Inspect formatting changes in the files listed above.

There are no functional changes to the model. In the expectation files, global keys and references have been updated due
to a bug fix, but they remain semantically the same.

The changes can be reviewed in PR [#2550](https://github.com/finos/common-domain-model/pull/2550).

# _Event Model - Valuation Update_

_Background_

This release introduces new types and functions to allow the native representation and qualification of an update of the valuation with the ability to replace the existing historical valuation data.

_What is being released?_

The following support have been added: 

- Representation the valuation PrimitiveInstruction.
- Application the valuation PrimitiveInstruction.
- Qualification of a valuation update event.

_Data types_

- Added new `ValuationInstruction` type.
- `valuation` attribute of type `Valuation` added to `ValuationInstruction`
- `replace` attribute of type `boolean` added to `ValuationInstruction`
- `valuation` attribute of type `ValuationInstruction` added to `PrimitiveInstruction`.

_Functions_

- Added new `Create_Valuation` function.
- Updated `Create_TradeState` function to support `valuation`.

_Qualification_

- Added new `Qualify_ValuationUpdate` function.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

PR: [#2552](https://github.com/finos/common-domain-model/pull/2552)
