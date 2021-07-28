# *Product Model – New 2021 Cash Settlement Terms*

_What is being released?_

New cash-settlement terms for mid-market valuation and replacement value methods have been added in accordance with the 2021 definitions. Synonym mappings have been extended using two new FpML 5.12 examples containing optional early termination terms according to these new valuation methods.

_Details_

- `CashSettlementMethodEnum` includes 5 new cash-settlement methods for mid-market valuation and replacement value:

  - `MidMarketIndicativeQuotations`
  - `MidMarketIndicativeQuotationsAlternate`
  - `MidMarketCalculationAgentDetermination`
  - `ReplacementValueFirmQuotations`
  - `ReplacementValueCalculationAgentDetermination`
  
- A new `CashCollateralValuationMethod` data type has been added that includes the parameters required for the new valuation methods.
- The harmonised `CashSettlementTerms` data type has been extended to include an optional `cashCollateralValuationMethod` attribute.
- Conditions have been added to `CashSettlementTerms` to ensure data integrity against the new cash settlement method types.
- `CalculationAgentPartyEnum` has been renamed more generically as `PartyDeterminationEnum`, as it now also applies to the protected party determination in the replacement value method.
- 2 new samples including optional early termination clauses have been added to the FpML 5.12 group:

  - `ird-ex47-rfr-compound-swap-lookback-oet-rvfq-FpML_5_12`
  - `ird-ex46-rfr-compound-swap-lookback-oet-mmviq`

- Their valuation method attributes have been mapped to the extended `CashSettlementTerms` data type.

_Review Directions_

In the CDM Portal, select the Textual Browser and search for the relevant data types.

In the Ingestion tile, search for the samples specified above.
