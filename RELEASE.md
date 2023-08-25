# _Event Model - Add Repo Partial Delivery._

_Background_

In the repo market it is a recommended best practice that if a seller fails to deliver the full amount of collateral
on settlement date, that the buyer agrees to accept the amount of collateral delivered and agree on the remaining amount and when
it will be delivered.

_What is being released?_

A new function `Create_PartialDeliveryPrimitivenstruction` is added that accepts inputs of the delivered amount and undelivered amount. 
The functions closes the existing trade and opens two new trades, one based on the delivered quantity and one based on the undelivered
quantity. The function returns a primitive instruction that can be used to execute a business event. 
There is a also a new qualification function `Qualify_PartialDelivery` that validates the results meet the partial delivery criteria.