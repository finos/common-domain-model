# *Model Optimisation: Extract Party References from ContractualProduct*

_What is being released_

Following the recent model refactoring to extract `Party` references from `ContractualProduct`, the regulatory reporting model in `model-reg-reporting` was updated to look up the `Party` based on the `CounterpartyEnum`.

_Review direction_

In the Textual Browser, review reporting rules:

- `FixedFloatBuyerSeller`
- `FixedFixedBuyerSeller`
- `SingleCurrencyBasisSwap`
- `CrossCurrencySwapBuyerSeller`
- `CreditDefaultSwapBuyerSeller`
