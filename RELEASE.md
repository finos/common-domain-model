# *CDM Model: Collateral Eligibility Schedules*

_What is being released_

Further changes to the collateral eligibility schedules following a workshop with Acadia. 

- New enum values added to `CollateralIssuerTypeClassification` and `CollateralType`, and new enum `CollateralSubType` added.
- `EligibleCollateral` attribute of type `UnderlyingCurrency` removed and replaced by `denominatedCurrency`.
- Existing enum `ProductIdSourceEnum` value `ICAD` has moved to `TaxonomySourceEnum`.
- `MultipleCreditNotations` has attributes `mismatchResolution` and `referenceAgency` added to specify how to resolve mismatches between ratings. 

The type `EligibleCollateral` now has 5 attributes:

```
    issueInformation CollateralIssueInformation (1..1)
    denominatedCurrency string (1..*)
        [metadata scheme]
    maturityRange MaturityRange (1..1)
    agencyRating AgencyRating (0..1)
    valuationPercentage CollateralValuationPercentage (1..1)
```

_Review Directions_

In the Textual Browser, review the following:

- types: `EligibleCollateral`, `MultipleCreditNotations`, `CollateralIssueInformation`, `CollateralType`, `AgencyRating` and `CollateralValuationPercentage`.
- enums: `CollateralIssuerTypeClassificationEnum`, `CollateralTypeEnum` and `CollateralSubTypeEnum`.
