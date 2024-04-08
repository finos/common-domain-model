# _Product Model - FpML Mapping - Commodity Swaps_

_Background_

Commodity swaps with a physical leg and a fixed/floating leg are incorrectly mapped from FpML. This is described in more detail in issue [#2837](https://github.com/finos/common-domain-model/issues/2837).

_What is being released?_

This release fixes the mapping for Commodity Swaps from FpML as listed below.

- Commodity swap samples with a physical leg and a fixed leg are now only mapped to these two payouts: `ForwardPayout` and `FixedPricePayout`
- Commodity swap samples with a physical leg and a floating leg no longer have a settlement type defaulted to cash

_Review directions_

In Rosetta, open the Translate tab and review the `fpml-5-10 > incomplete-products > commodity-derivatives` test pack samples:

- com-ex10-physical-oil-pipeline-crude-wti-floating-price.xml
- com-ex11-physical-oil-pipeline-heating-oil-fixed-price.xml

The changes can be reviewed in PR: [#2846](https://github.com/finos/common-domain-model/pull/2846)