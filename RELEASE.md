<<<<<<< HEAD
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
 
=======
<<<<<<< HEAD
# *Product Model - FpML Synonym Mappings for Unique Product Identifiers*
>>>>>>> 15ba0f754b100be6c1ea7cc264a252d181e08d45

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

<<<<<<< HEAD
[PR #2529](https://github.com/finos/common-domain-model/pull/2529)
=======
Changes can be reviewed in PR [#2521](https://github.com/finos/common-domain-model/pull/2521)
=======
# _Product Qualification - Zero Coupon Swaps_

_Background_

The qualification function for a zero-coupon swap is too restrictive in CDM, since it requires that all the `interestRate` Payout legs should feature one unique payment at Term. Normally a zero coupon only points out that at least one leg has a unique payment made at term.
This release fixes this, along with some inaccurate provisions of qualifying functions regarding zero-coupon swaps. This release also removes the use of conditional synonym mappings from FpML to CDM of the `PrincipalPayments` element for Zero-Coupon Swaps with Known Amount cases, which has been deprecated. The `Qualify_SubProduct_FixedFloat` qualification function has also been updated to not require the use of this `PrincipalPayments` element.

_What is being released?_

- Minor changes to Zero-Coupons Swap qualification functions
- Removed the conditional synonym mappings from FpML to CDM for the `PrincipalPayments` element in scenarios with Zero-Coupon Swaps with Known Amount.

_Qualification_

- Updated `Qualify_Transaction_ZeroCoupon` function to accurately qualify Zero-Coupon swaps.
- Updated `Qualify_SubProduct_FixedFloat` function to accurately qualify Zero-Coupon swaps with Known Amount.
- Updated the provision of `Qualify_InterestRate_IRSwap_FixedFloat_ZeroCoupon`, `Qualify_InterestRate_InflationSwap_FixedFloat_ZeroCoupon` and `Qualify_InterestRate_InflationSwap_Basis_ZeroCoupon` functions to accurately describe the qualified products.

_Translate_

Deprecated use of synonym mapping from FpML to CDM for the `PrincipalPayments` element for Zero-Coupon Swaps with Known Amount cases.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

Inspect Pull Request: [#2390](https://github.com/finos/common-domain-model/pull/2390)
>>>>>>> befeacbef6f6d84d22407c32ddef254fa919bfa5
>>>>>>> 15ba0f754b100be6c1ea7cc264a252d181e08d45
