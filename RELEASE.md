# *CDM Model: Collateral Eligibility Schedules*

_What is being released_

CDM model representation of data relevant for collateral eligibility schedules has been enhanced. This is found under `EligibleCollateral`. 

There are 6 key attributes relevant for this feature of collateral documentation:
- issueInformation `CollateralIssueInformation` 
- underlyingCurrency `UnderlyingCurrency`
- maturityRange `MaturityRange` 
- issuerAgencyRating `IssuerAgencyRating` 
- issueAgencyRating `IssueAgencyRating`
- haircut `CollateralHaircut` 
    
For reference purposes, the previous model have has been renamed to `EligibleCollateralFpMLMapped`.  This will be removed once the new model is complete.

_Review Directions_

- Review enhancements to `EligibleCollateral` and associated types and enums. 
- Review new values in enum ProductIdSourceEnum (added under "//added new").
- Review additional text to reference `EligibleCollateralFpMLMapped`.
