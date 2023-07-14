# _CDM - Zero-Strike equity swaps Release Notes_

_Background_

The Zero-Strike equity swaps are not currently qualified in CDM, but these trades are included in FpML documentation and should be reportable.
This release introduces a qualifying function for these equity products,
along with the expansion and update of the `PrincipalPayments`, `PrincipalPayment` and `PrincipalPaymentSchedule` types
to cover for the principal payments of the zero-strike equity swaps inside a single performancePayout.


_What is being released?_


_Data types_

- `PrincipalPaymentDescriptions` attribute of type `PrincipalPaymentSchedule` added to `PrincipalPayments`.
- `principalPaymentDescriptionDate` attribute of type `AdjustableOrRelativeDate` added to `PrincipalPayment`.
- `determinationMethod` attribute of type `DeterminationMethodEnum` added to `PrincipalPayment`.
- `amountRelativeTo` attribute of type `Money` added to `PrincipalPayment`.
- Type of `intermediatePrincipalPayment` in `PrincipalPaymentSchedule` changed to `PrincipalPayment`, cardinality changed from (0..1) to (0..*).

_Qualification_

- Added new `Qualify_EquitySwap_ZeroStrike` function.

_Translate_

Added mapping coverage for Zero-Strike swaps.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.
In the CDM Portal, select Ingestion and review the following samples:

fpml-5-12/products/equity

- eqs ex04 zero strike long form
