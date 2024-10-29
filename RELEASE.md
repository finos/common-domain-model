# *CDM Product Model* - Underlier in Corporate Action

_Background_

In an earlier Asset Refactoring release, an unintending defect was introduced on the `CorporateAction` data type.
This release corrects that.

_What is being released?_

The data type of the `underlier` attribute within `CorporateAction` has been undated to be `Underlier` rather than
`Instrument`.  `Instrument` is too restrictive as a broader range of assets can be the subject of corporate actions
and this is best represented by the `Underlier` data type.

_Review Directions_

In Rosetta, select the Textual Browser and inspect the changes to the data type identified above.

Changes can be reviewed in PR [#3201](https://github.com/finos/common-domain-model/pull/3201)

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

Changes can be reviewed in PR [#3202](https://github.com/finos/common-domain-model/pull/3202)
