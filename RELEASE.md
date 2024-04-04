# _Product Model - Cumulation Features_

_Background_

Some products may contain provisions upon which payout calculation and settlement apply to a core quantity that is incremented/cumulated over time. The cumulation feature is agnostic to the type of payout, as well as to the asset class involved, thus belongs to the quantity defined in the payout base of the product.
Besides, the cumulation rule may be either straight or involve more sophisticated features such as barriers or levers, as well as multiple cumulation periods to be taken into account when resolving the final multiplier to be applied to the core quantity.
However, it is assumed that the simple existence of cumulation feature per se, without any further details, is sufficient for defining the qualifiers required for Digital Regulatory Reporting (DRR).

_What is being released?_

- Created new type `CumulationFeature` with no atttributes
- Listed the new type `CumulationFeature` as quantityCumulation attribute of type `ResolvablePriceQuantity` with cardinality (0..*)

_Review directions_

In the Rosetta platform, select the Textual Browser and inspect the change identified above.

The changes can be reviewed in  PR: [#2830](https://github.com/finos/common-domain-model/pull/2830)
