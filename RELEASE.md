# *CDM support for increase on existing Trade Lot*

_Background_

CDM does not allow for an existing tradeLot to be increased by a delta amount, however it does allow decrease via a delta amount. 
Using `replace` as a quantityChange direction inherently forces the producer to calculate the position, which may not match the underlying granularity of the RMS.
The ability to perform an increase to an existing tradeLot via summation (delay of increase plus existing lot quantity) is critical.

_What is being released?_


The `Create_Quantity_Change` function has been updated to enable the user to either create a new trade lot by populating a new lot identifier for any prior tradeLot not linked to the parent trade, 
or, if the tradeLot identifier on an increase or decrease matches an existing tradeLot identifier, to apply the quantityChange to the existing tradeLot.

_Review Directions_

In Rosetta, open the contribution and view the changes listed above and inspect each of them.

Changes can be reviewed in PR [#2961](https://github.com/finos/common-domain-model/pull/2961)
