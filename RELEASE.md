# *Product Model - Option Denomination Deprecation*

_What is being released?_

As part of the price/quantity normalisation, the option payout structure has been further cleaned-up and the specialised `OptionDenomination` type and attribute have been retired.

This contained three basic attributes: `numberOfOptions` (number), `optionEntitlement` (number) and `entitlementCurrency` (string), used for Equity and Bond Option products and all inherited from FpML. Those attributes are effectively captured by the `Quantity` structure as the `amount`, `multiplier` and `multiplierUnit` attributes, respectively. Synonym mappings have been adjusted so that the corresponding values from the FpML samples are populated in the `Quantity` structure of the CDM output.

_Review Directions_

In the CDM Portal, use the Textual Browser to review `OptionPayout`, where the `optionDenomination` attribute has been retired

In the Ingestion Panel, try the following samples:

- products > rates > `bond option uti`
- products > rates > `cb option usi`
- products > equity > `eqd ex01 american call stock long form`
