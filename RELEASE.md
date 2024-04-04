# _Product Model - Cumulation Features_

_Background_

Some product may contain provisions upon which payout calculation and settlement shall apply to a core quantity that is incremented/cumulated over time. For clarity, this cumulation feature is agnostic to the type of payout at stake, as well as to the asset class involved, thus belongs to the quantity defined in the payout base of the products.
Besides, the cumulation rule may be either straight or involve more sophisticated features such as barriers or levers, as well as multiple cumulation periods to be taken into account when resolving the final multiplier to be applied to the core quantity.
However, it is assumed that the simple existence of cumulation feature per se, without any further details, is sufficient for defining the qualifiers required in regards of DRR.

_What is being released?_

In accordance with the above consideration, that is to short term focus on DRR requirements :

- Creation of new type `CumulationFeature` that is empty i.e. there is no atttribute inside this type
- Added the above as new attribute of `ResolvablePriceQuantity` under name quantityCumulation with card (0..*)

_Review directions_

In the Rosetta platform, select the Textual Browser and inspect the change identified above.

The changes can be reviewed in  PR: [#2830](https://github.com/finos/common-domain-model/pull/2830)