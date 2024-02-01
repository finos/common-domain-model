# _Product Model - FpML synonym mappings for Price KnownAmountSchedule_

_Background_

Previously, the `PriceQuantity>priceSchedule` component lacked representation in the InterestRatePayout, specifically when dealing with samples featuring a `knownAmountSchedule`. This gap existed because we were only capturing knownAmountSchedules within the `TradeLot` and not extending that representation to the corresponding `Payout`.

_What is being released?_

In this release, we address the FpML mappings for the `KnownAmountSchedule` component, which was originally mapped to the `TradeLot` but not adequately reflected in the PriceSchedule of the corresponding Payout. The update ensures that the knownAmountSchedule is now appropriately mapped to the `priceSchedule` component.

- Enhanced synonym mappings for `PriceSchedule` have been updated to align with FpML  `knownAmountSchedule` for Rates.

_Review directions_

To assess these changes, navigate to the CDM portal and select the following samples:

fpml-5-10 / products / rates
- IR IRS FixedFloat ZC KnownAmount

In Rosetta, select the Textual Browser and inspect each of the changes identified above.

Changes can be reviewed in PR [#2644](https://github.com/finos/common-domain-model/pull/2644)

# *Updates to Zero Coupon Swaps Qualification Functions*

_Background:_
- The Qualification functions in CDM classify financial products.
- The goal of this update is to improve the existing qualifying functions `Qualify_InterestRate_IRSwap_FixedFloat_ZeroCoupon` and `Qualify_InterestRate_InflationSwap_FixedFloat_ZeroCoupon`.
- Qualifying functions `Qualify_InterestRate_IRSwap_FixedFloat_ZeroCoupon` and `Qualify_InterestRate_InflationSwap_FixedFloat_ZeroCoupon` don't need the reference to `Qualify_Transaction_ZeroCoupon_KnownAmount` since it is redundant. The first two functions use `Qualify_Transaction_ZeroCoupon` and that means that the reference to the `KnownAmount` function is not needed.


_What is being released?_

- Updated the `Qualify_InterestRate_IRSwap_FixedFloat_ZeroCoupon` and `Qualify_InterestRate_InflationSwap_FixedFloat_ZeroCoupon` functions. Removed the OR statement checking the applicability of `Qualify_Transaction_ZeroCoupon_KnownAmount` in both functions.

- Corrected one of the `InterestRatePayout` type conditions. Updated the `RateSpecification` condition to check the presence of `priceQuantity` instead of `principalPayment`.

       
_Review Directions_

In the CDM Portal, select the Textual Browser and inspect the `Qualify_InterestRate_IRSwap_FixedFloat_ZeroCoupon` and `Qualify_InterestRate_InflationSwap_FixedFloat_ZeroCoupon` functions, and the `InterestRatePayout` type.

# *Event Model - PartyRoleEnum including PTRRServiceProvider role*

_Background_

In order to report under EMIR, a party needs to be identified as a portfolio compression or a portfolio rebalancing service provider. These roles can be unified in a more generic role: PTRR Service Provider. The current CompressionServiceProvider code will be replaced by PTRRServiceProvider.

_What is being released?_

- CDM enum `PartyRoleEnum` has been modified in the following way: code `CompressionServiceProvider` has been marked as `[deprecated]` and a more generic code `PTRRServiceProvider` has been added.
- Synonym mappings have been added to populate the `PartyRoleEnum` with `PTRRServiceProvider` whenever the FpML is populated with `PTRRCompressionProvider` or `PTRRRebalancingProvider`

_Review directions_

- In the CDM Portal, select the Textual Browser and inspect the change identified above.

The changes can be reviewed in PR [#2651](https://github.com/finos/common-domain-model/pull/2651)

# _Product Model - FpML Mappings - Bond Forwards_

_What is being released?_

This release fixes FpML mapping issues related to bond forward samples.

_Review directions_

In Rosetta, open the Common Domain Model workspace, select the Translate tab and review the following samples:

* fpml-5-10 > products > rates > bond-fwd-generic-ex01.xml
* fpml-5-10 > products > rates > bond-fwd-generic-ex02.xml

Changes can be reviewed in PR [#2656](https://github.com/finos/common-domain-model/pull/2656)

# _Event Model - Trade Lot Identifier added to Execution Instruction_

_Background_

In order for quantityChange instructions to impact an existing tradeLot, the executionInstruction requires a tradeLot identifer to be present.

_What is being released?_

- Added `lotIdentifier` attribute (optional) to `ExecutionInstruction`
- In `Create_Execution` function, the `lotIdentifier` attribute is used when creating the execution's `TradeLot` object

_Backward-Incompatible Changes_

None

_Review directions_

In the CDM Portal, select the Textual Browser and inspect the change identified above.

The changes can be reviewed in PR [#2649](https://github.com/finos/common-domain-model/pull/2649).

# *Infrastructure - Dependency Update*

_What is being released?_

This release updates the `rosetta-dsl` dependency.

Version updates include:
- `rosetta-dsl` 9.4.0: this release improves performance of validating Rosetta code and of generating code. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.4.0.

There are no changes to the model or test expectations.

_Review directions_

The changes can be reviewed in PR [#2645](https://github.com/finos/common-domain-model/pull/2645).
