# _Product Qualification - Zero Coupon Swaps_

_Background_

The qualification function for a zero-coupon swap is too restrictive in CDM, since it requires that all the `interestRate` Payout legs should feature one unique payment at Term. Normally a zero coupon only points out that at least one leg has a unique payment made at term.
This release fixes this, along with some inaccurate provisions of qualifying functions regarding zero-coupon swaps. This release also removes the use of conditional synonym mappings from FpML to CDM of the `PrincipalPayments` element for Zero-Coupon Swaps with Known Amount cases, which has been deprecated.

_What is being released?_

- Minor changes to Zero-Coupons Swap qualification functions
- Removed the conditional synonym mappings from FpML to CDM for the `PrincipalPayments` element in scenarios with Zero-Coupon Swaps with Known Amount.

_Qualification_

- Updated `Qualify_Transaction_ZeroCoupon` function to accurately qualify Zero-Coupon swaps.
- Updated the provision of `Qualify_InterestRate_IRSwap_FixedFloat_ZeroCoupon`, `Qualify_InterestRate_InflationSwap_FixedFloat_ZeroCoupon` and `Qualify_InterestRate_InflationSwap_Basis_ZeroCoupon` functions to accurately describe the qualified products.

_Translate_

Deprecated use of synonym mapping from FpML to CDM for the `PrincipalPayments` element for Zero-Coupon Swaps with Known Amount cases.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

Inspect Pull Request: [#2390](https://github.com/finos/common-domain-model/pull/2390)
