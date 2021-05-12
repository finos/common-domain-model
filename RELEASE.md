# *Event Model - Reset: Fallback Rates*

_What is being released?_

This release adjusts the `Reset` type and related functions to support ISDA/Bloomberg Fallback Rates.  

The Fallback Rates are used as a substitute for LIBOR on legacy LIBOR contracts that have not been amended to an Adjusted Reference Rate (ARR), and where Fallback contractually applies.  

When the Fallback applies, the reset process changes to calculate the reset rate in arrears rather than in advance.  For example, given a trade with 3 month floating rate where the fallback applies, for a reset date (defined in the model as existing attribute `Reset->resetDate`) of 20th April, the rate record date would be 3 months earlier, 19th Jan (e.g. the accrual period start date because reset rate is calculated in arrears, defined in the model as new attribute `Reset->rateRecordDate`).  If no rate is available for the 19th Jan, the observed rate will be taken from the nearest previous available date (defined in the model as existing attribute `Reset->observations->observationIdentifier->observationDate`).

The related functions `Create_Reset`, `Create_ResetPrimitive` and `ResolveInterestRateReset` have been adjusted to use the new attribute rateRecordDate when specified.

Note that the reset process for equities is unaffected.

_Review directions_

In the CDM Portal, select the Textual Browser, and review the following types and functions:

- `Reset`, `ResetInstruction`
- `Create_Reset`, `Create_ResetPrimitive` and `ResolveInterestRateReset`



