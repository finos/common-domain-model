# _Product Model - FpML synonym mappings for commodity spread_

_Background_

This release updates and extends the FpML mapping coverage for the product model. Currently, the CDM attribute `priceType` is being populated with `INTEREST_RATE` for Commodity spreads. That is wrong since the spread is an asset price.
_What is being released?_

- Mappings added to populate CDM attribute `priceType` with `ASSET_PRICE` for commodity spreads.

_Review directions_

- In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.