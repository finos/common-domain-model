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

PR https://github.com/finos/common-domain-model/pull/2472