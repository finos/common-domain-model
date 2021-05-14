# *Event Model - Rate Reset: Fallback Rates*

_What is being released?_

This release adjusts the `Reset` type and related functions to support Fallback Rates.  

Fallback Rates are used, for example, as a substitute for LIBOR on legacy LIBOR contracts that have not been amended to an Adjusted Reference Rate (ARR), and where Fallback contractually applies.  

Often in a fallback reset process the reset rate is set in arrears rather than in advance as is the case with rates like LIBOR today, where the rate is reset for the subsequent accrual period.  For example, given a trade with 3 month floating rate, and where such as fallback applies, the reset date (defined in the model with existing attribute `Reset->resetDate`) of 20th April is at the end of the interest accrual period, the Rate Record Date would be 3 months earlier, 19th Jan (e.g. near start of the accrual period because reset rate is set in arrears, defined in the model with new attribute `Reset->rateRecordDate`).

Typically, if no rate is available on the Rate Record Date of 19th Jan, the observed rate will be taken on the nearest previous date where a rate is available (defined in the model with the existing attribute `Reset->observations->observationIdentifier->observationDate`).

The related functions `Create_Reset`, `Create_ResetPrimitive` and `ResolveInterestRateReset` have been adjusted to use the new attribute rateRecordDate when specified.

Note that the reset process for equities is unaffected.

_Review directions_

In the CDM Portal, select the Textual Browser, and review the following types and functions:

- `Reset`, `ResetInstruction`
- `Create_Reset`, `Create_ResetPrimitive` and `ResolveInterestRateReset`



