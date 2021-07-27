# *Legal Agreement Model – Collateral Valuation Treatment/Identification and addition of additional haircuts*

_What is being released?_

The data type `CollateralValuationPercentage` has been renamed to `CollateralValuationTreatment` as more relevant. The corresponding data attribute `valuationPercentage` has been renamed to `haircutPercentage` and its description reflects haircut rather than the full valuation percentage. 

The following functions have been updated with the new names:
- `PostedCreditSupportItemAmount`
- `SecurityFinanceCashSettlementAmount`
- `ResolveSecurityFinanceBillingAmount`

An additional haircut data attribute `additionalHaircutPercentage` with related description has been added along with conditions as `haircutPercentage` and `fxHaircutPercentage`.

_Review Directions_

In the CDM Portal, select the Textual Browser and search for the data types mentioned above.

Search for the data type `CollateralValuationTreatment` and inspect the change from `CollateralValuationPercentage` throughout the model. Inspect the change to data attribute `haircutPercentage` from `valuationPercentage` and changes to the description and related conditions.

Check the addition of data attribute `additionalHaircutPercentage` and inspect the related conditions.

Inspect the addition of condition `HaircutPercentageOrMarginPercentage` which forces a required choice for `haricutpercentage` or `marginpercentage` 
In the legalagreement-csa-func file inspect the changes made to the function for haircut calculation to support the changes made.

# *Credit Notations – Agency Rating Criteria additions and added descriptions*

_What is being released?_

A new enumeration list `CreditNotationBoundaryEnum` will now denote the boundary of a credit agency rating i.e minimum or maximum. It is used now with the 
 `boundary` attribute added  to the `AgencyRatingCriteria` data type. 

Descriptions for attributes of `CreditNotationMismatchResolution` and `AgencyRatingCriteria` have been added.

_Review Directions_

In the CDM Portal, select the Textual Browser and search for the relevant data types and review as per the following instructions:

Search for the data type `AgencyRatingCriteria` and inspect the descriptions added to attributes `mismatchResolution` and `referenceAgency`. Also inspect the new added data attribute `boundary` and its related enumerations `CreditNotationBoundaryEnum` with descriptions.

Search for `CreditNotationMismatchResolutionEnum` and inspect the descriptions now populated that where previously missing in the model.
