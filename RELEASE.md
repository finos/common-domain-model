# *Changes to Position - Allow a Position to hold a Product or an Asset*

_Background_

This change allows a `Position` to now either be based upon a `Product` or an `Asset`.

The reason for this change was to allow `CollateralPosition` (which extended `Position`) to represent a list of `PriceQuantity` by `Asset` or by `Product`. 
This is required for securities lending trades that are collateralised by other securities, where the securities being used as collateral may need to defined on the trade too.

_What is being released?_

The following changes have been made:

- A new `PositionBase` type has been introduced.
- The attributes in `PositionBase` are the same as from the original `Position` with the addition of an `Asset`.
- The cardinality of `Product` and `Asset` are set as optional.
- A condition has been added to `PositionBase` to ensure either a `Product` or an `Asset` are defined.
- Existing type `CollateralPosition` now extends `PositionBase`.
- Existing type `Position` now extends `PositionBase`.
- `Position` overrides the cardinality of `product` from `PositionBase` to make it mandatory. This retains the original requirement for a position to contain a product.

_Review Directions_

Changes can be reviewed in PR: [#3994](https://github.com/finos/common-domain-model/pull/3994)
