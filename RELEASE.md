# Product Model - Commodity Physical Options

_Background_

This release provides qualification support for Commodity Physical Option products, which were not supported until now. These kind of products will be represented similarly to cash settled options: an `optionPayout` with a strike that can be fixed or floating, a commodity `underlier` and the delivery information specified in the attribute `delivery`. The only difference will be that `settlementType` will be `Physical`.

_What is being released?_

* Added two qualifying functions: `Qualify_Commodity_Option_Cash` and `Qualify_Commodity_Option_Physical`, which use the function `Qualify_Commodity_Option` and check the value in `settlementType`.

* Updated the qualifying functions `Qualify_Commodity_Swap_FixedFloat` and `Qualify_Commodity_Swap_Basis` so they do not check that the underlier is a commodity. That is redundant information since it is already been checked in `Qualify_AssetClass_Commodity`.

_Review directions_

* In the Rosetta platform, select the Textual Browser and inspect each of the changes identified above.

The changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2749
