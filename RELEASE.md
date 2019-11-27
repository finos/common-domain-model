# Quantity and Price Refactoring - Initial Use Case: FX

_What is being released_

This is a follow-on from the quantity refactoring stream, which is now supplemented by a refactoring of the price concept too. In essence both the quantity and price are abstracted away from the contractual product definition, and positioned alongside it in either the contract and execution.

The refactoring is driven from the initial use case of representing FX products, starting with simple FX (spot and forward).

The changes aim at introducing the new price concept as well as furthering the clean-up of the quantity concept, following feedbacks received in the WG:

- Introducing the `PriceNotation` type, similar to `QuantityNotation`, which is a generic type designed to capture different styles of prices. At this point only the `CashPrice` and `ExchangeRate` price styles have been represented, with a view to augment that list over time.
- Introducing the `AssetIdentifier` type, which is both used in `QuantityNotation` and `PriceNotation` to uniquely identify the asset being quantified or priced.
- Removing the `QuantityTypeEnum` and `PriceTypeEnum` types and corresponding `quantityNotationTag` attribute, which are now redundant since the type can be inferred from the `assetIdentifier` attribute.
- Mapping of `assetIdentifier` attribute for currency and equity types, both outside the product (in the `QuantityNotation`) and inside the product (in the `ResolvablePayoutQuantity`). This allows, for instance, to  uniquely identify where to position quantities in the legs of a XC Swap or in the underlyer of an Equity Swap.
- Mapping of `exchangeRate` attribute in the `PriceNotation` object for (simple) FX products. The price attributes have now been extracted away from the FX product definition.
- Mapping of options premium and other cash payment components as part of the `cashPrice` for contractual products.
- Positioning of the `price` and `contractualPrice` attributes under the `Execution` / `Contract` objects, and unification of the `price` attribute in the former for both cash and contractual products, with the specialisation handled by the `PriceNotation`.
- Plus some clean-up related to those changes.

This is only an initial implementation of the price refactoring and further work will be required to solidify the implementation for simple FX products scope first, then tackle more complex FX product (like options), and then move on to other product types.

_Review Directions_

In the textual Browser in the CDM Portal, see:

- `QuantityNotation` and `PriceNotation` types and their use of the `assetIdentifier` attribute
- `Contract` now shows all three `contractualProduct`, `contractualQuantity` and `contractualPrice` positioned side by side.
- `Execution` shows the `price` and `product` attributes, generically across product types. Note that the `quantity` and `executionQuantity` still need to be unified.

In the Ingestion Panel, see:

- Products > Rates > any XC Swap, and see that the currency has been kept as `assetIdentifier` as part of the quantity at the individual leg level
- Products > Equity, and see that the asset identifier which is now positioned in the `contractualQuantity`
- Products > Rates > any Swaption, and see that the premium is now positioned as part of the `contractualPrice`.
