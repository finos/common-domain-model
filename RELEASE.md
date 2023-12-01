# *Infrastructure - Dependency Update*

_What is being released?_

This release updates the `rosetta-dsl` dependency.

Version updates include:
- `rosetta-dsl` 9.1.2: this release fixes DSL issues [#670](https://github.com/REGnosys/rosetta-dsl/issues/670) and [#653](https://github.com/REGnosys/rosetta-dsl/issues/653). For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.1.2.
- `rosetta-dsl` 9.1.3: this release fixes an issue related to the generated Java `process` method. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.1.3.

To ease the process of reviewing expectation changes,
the formatting of the following expectation files has also been improved:
- `cdm-sample-files/functions/repo-and-bond/repo-adjustment-input.json`
- `cdm-sample-files/functions/repo-and-bond/repo-reprice-input.json`
- `cdm-sample-files/functions/repo-and-bond/repo-substitution-input.json`

_Review directions_

Inspect formatting changes in the files listed above.

There are no functional changes to the model. In the expectation files, global keys and references have been updated due to a bug fix, but they remain semantically the same.

The changes can be reviewed in PR [#2550](https://github.com/finos/common-domain-model/pull/2550).

# _Event Model - Valuation Update_

_Background_

This release introduces new types and functions to support the native representation and qualification of a valuation update. The valuation update can add to or replace the existing valuation history.

_What is being released?_

The following features have been added: 

- Representation of the primitive instruction for a valuation event.
- Application of the valuation primitive instruction to change the trade state.
- Qualification of a valuation update event.

_Data types and attributes_

- Added new `ValuationInstruction` type with the following attributes:
  - `valuation` attribute of type `Valuation`
  - `replace` attribute of type `boolean`
- `valuation` attribute of type `ValuationInstruction` added to `PrimitiveInstruction`.

_Functions_

- Added new `Create_Valuation` function.
- Updated `Create_TradeState` function to include a valuation update step using `Create_Valuation`.

_Qualification_

- Added new `Qualify_ValuationUpdate` function.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

PR: [#2552](https://github.com/finos/common-domain-model/pull/2552)

# *Product Model - Qualification Functions for Zero-coupon Swaps*

_Background_

The qualification function for a zero-coupon swap is too restrictive in CDM, since it requires that all the payout legs should feature one unique payment at Term. Normally a zero coupon only points out that at least one leg has a unique payment made at term.

This release fixes this, along with some inaccurate provisions of qualifying functions regarding zero-coupon swaps. In addition, a new `Qualify_Transaction_ZeroCoupon_KnownAmount` function was added to facilitate the classification of Zero Coupon swaps with a Known Amount.

This release also removes the use of conditional synonym mappings from FpML to CDM of the `PrincipalPayments` element for Zero-Coupon Swaps with Known Amount cases. The `Qualify_SubProduct_FixedFloat` qualification function has also been updated to not require the use of this `PrincipalPayments` element.

_What is being released?_

- Minor changes to existing Zero-Coupon Swap qualification functions
- Addition of a new Zero-Coupon Swap Known Amount qualification function
- Removed the conditional synonym mappings from FpML to CDM for the PrincipalPayments element in scenarios with Zero-Coupon Swaps with Known Amount.

_Qualification_

- Updated `Qualify_Transaction_ZeroCoupon` function to accurately qualify Zero-Coupon swaps.
- Updated `Qualify_SubProduct_FixedFloat` function to include Zero-Coupon swaps with Known Amount.
- Updated `Qualify_InterestRate_IRSwap_FixedFloat_ZeroCoupon` function to include Zero-Coupon swaps with Known Amount.
- Updated `Qualify_InterestRate_InflationSwap_FixedFloat_ZeroCoupon` function to include Zero-Coupon swaps with Known Amount.
- Created new `Qualify_Transaction_ZeroCoupon_KnownAmount` function.
- Updated the annotations of `Qualify_InterestRate_IRSwap_FixedFloat_ZeroCoupon`, `Qualify_InterestRate_InflationSwap_FixedFloat_ZeroCoupon` and `Qualify_InterestRate_InflationSwap_Basis_ZeroCoupon` functions to accurately describe the qualified products.

_Translate_

- Deprecated use of synonym mapping from FpML to CDM for the `PrincipalPayments` element for Zero-Coupon Swaps with Known Amount cases.

Review directions

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

Inspect Pull Request: [#2571](https://github.com/finos/common-domain-model/pull/2571)
