# _Product Model - FpML Synonym Mappings for Commodity Spread_

_Background_

Currently, the CDM attribute `priceType` is incorrectly being populated with `INTEREST_RATE` for Commodity spreads. This is inadequate since the spread is an asset price.

_What is being released?_

This release updates and extends the FpML mapping coverage for the product model to map the correct price. 

- Mappings added to populate CDM attribute `priceType` with `ASSET_PRICE` for commodity spreads.

_Review directions_

- In the CDM Portal, select the Textual Browser and inspect the change listed above.
