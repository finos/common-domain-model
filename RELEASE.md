# *Updates to Zero Coupon Swaps Qualification Functions*

_What is being released_

_Background:_
- The Qualification functions in CDM classify financial products.
- Qualifying functions `Qualify_InterestRate_IRSwap_FixedFloat_ZeroCoupon` and `Qualify_InterestRate_InflationSwap_FixedFloat_ZeroCoupon` don't need the reference to `Qualify_Transaction_ZeroCoupon_KnownAmount` since it is redundant. The first two functions use `Qualify_Transaction_ZeroCoupon` and that means that the reference to the KnownAmount function is not needed.

_Goal:_
- Improve the existing qualifying functions `Qualify_InterestRate_IRSwap_FixedFloat_ZeroCoupon` and `Qualify_InterestRate_InflationSwap_FixedFloat_ZeroCoupon`.

_Qualification functions_

Updated the `Qualify_InterestRate_IRSwap_FixedFloat_ZeroCoupon` and `Qualify_InterestRate_InflationSwap_FixedFloat_ZeroCoupon` functions:

- Removed the OR statement checking the applicability of `Qualify_Transaction_ZeroCoupon_KnownAmount` in both functions.

Corrected one of the `InterestRatePayout` type conditions:
- Updated the `RateSpecification` condition to check the presence of `priceQuantity` instead of `principalPayment`.


_Review Directions_

In the CDM Portal, select the Textual Browser and search and inspect the `Qualify_InterestRate_IRSwap_FixedFloat_ZeroCoupon` and `Qualify_InterestRate_InflationSwap_FixedFloat_ZeroCoupon` functions and the `InterestRatePayout` type.

Changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2648
