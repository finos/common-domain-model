# *CDM Product Model - Add Price to Payouts*

_Background_

This is an additional enhancement following the Asset Refactoring initiative by improving the consistency
of the modelling of prices on payouts.

_What is being released?_

The new attribute `fixedPrice` of type `PriceSchedule` has been added to the `OptionPayout` and to the
`SettlementPayout`.  Both additionally have a `metadata` address link, pointing to the `PriceQuantity`
in the `TradeLot`.

_Review Directions_

In Rosetta, select the Textual Browser and inspect the changes to the two payouts identified above.

Changes can be reviewed in PR [#3184](https://github.com/finos/common-domain-model/pull/3190)
