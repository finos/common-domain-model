# *Product Model – Price and Quantity including Settlement Instructions*

_What is being released?_

The structural definition of the tradable product has been rationalised, with a merged price/quantity structure that now incorporates settlement instructions (i.e. the settlement terms and the buyer/seller direction). Mappings have not been modified in this release, so attributes related to price/quantity settlement (e.g. option premium or upfront fee, foreign exchange etc.) are still mapped to the product and will be remapped at a later stage.

_Background_

Multiple inconsistencies have been identified in the current modelling of settlement terms. This leads to inefficiency in the product model and in the ability to represent functional rules for digital regulatory reporting.

This release focuses on creating the baseline of an atomic settlement structure from which a product- and asset-agnostic functional settlement model can be built. This structure is based on the principle that a settlement consists of an exchange of a given quantity against a given price between two parties. The existing `PriceQuantity` structure already captures the price and quantity components of that exchange. It is now extended to include the required settlement attributes such as settlement date, direction etc. These settlement attributes are optional because some price/quantity may not define a straight settlement: instead, such price/quantity may be inputs into a product that will further specify the mechanics of those settlements.

_Details_

- The `PriceQuantity` data type has been extended to describe a harmonised settlement structure that now also includes:

  - a `settlementTerms` attribute, that describes the settlement method (cash or physical)
  - a `buyerSeller` attribute, that handles the direction of the settlement

- The `SettlementInstructions` data type has been retired, as its attributes have now been merged into the unique `PriceQuantity`, and the `settlementInstructions` attribute has been removed from `TradableProduct`.
- Each trade lot now describes its own settlement attributes independently, which can be found in `tradeLot -> priceQuantity`. All functional expressions have been updated to fetch the settlement attributes of `TradableProduct` from `tradeLot -> priceQuantity` instead of `settlementTerms` and `settlementInstructions`.
- The `settlementCurrency` attribute has been moved out of `SettlementBase` and into `CashSettlementTerms`: specifying a unique settlement currency only makes sense in the context of cash settlement.
- Some static (java) code has had to be adjusted to work with the restructured model.

_Review Directions_

In the CDM Portal, select the Textual Browser and search for the relevant data types specified above.

In the Ingestion panel, verify that samples ingestion output has not been altered. The re-mapping of attributes related to the settlement of price/quantity (e.g. option premium) will occur at a later stage.
