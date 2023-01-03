# *Product Model - FpML Mappings - Credit Default Swaps*

_What is being released?_

This release updates and extends the FpML mapping coverage for Credit Default Swap products.

- Mappings added to populate CDM attribute `creditDefaultPayout->settlementTerms->settlementType`
- Mappings updated for FpML component `creditDefaultSwap->feeLeg` to correctly populate CDM attributes `InterestRatePayout->calculationPeriodDates` and `InterestRatePayout->paymentDates`. 

_Review Directions_

In the CDM Portal, select Ingestion and review examples in the folder `fpml-5-10 > products > credit`
- fpml-5-10 > products > credit > cd-ex01-long-asia-corp-fixreg-versioned
- fpml-5-10 > products > credit > cd-ex16-short-us-corp-fixreg-recovery-factor-versioned
