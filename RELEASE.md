# *Legal Agreement Model - Eligible Collateral Schedule: Description Updates and Refactored Criteria *

_What is being released_

The eligible collateral schedules (ECS) representation, related data types and associated descriptions have been enhanced or refined with additional features: In addition the higher level ECS data structure has been refactored.

1.	ECS representation and related data missing descriptions added 
2.	Description of the ECS related data types and attributes updated for consistency.
3.	All descriptions across ECS representation related data reviewed and corrected as per style guide
4.	Data attribute within `DebtInterestEnum` realigned as not correctly positions in data list 
5.	New attribute `otherAssetType` to data type `AssetType` for specifying other asset types, with a condition to ensure it exists if other asset types are specified under   AssetTypeEnum 
6.	Changes to the higher-level data structure `EligibleCollateralCriteria` and `ConcentrationLimitCriteria`: 

    •	`EligibleCollateralCriteria` becomes an extension of newly named `CollateralCriteriaBase`

    •	`CollateralCriteriaBase` contains `IssuerCriteria` and `AssetCriteria`

    •	`EligibleCollateralCriteria` still connects to `IssuerCriteria` and `AssetCriteria` but additionally has `CollateralTreatment`

    •	Within `CollateralTreatment`, `ConcentrationLimitType` has been moved to sit within `ConcentrationLimitCriteria` which also references `CollateralCriteriaBase`

    •	Amendments have been made to the related condition `ConcentrationLimitTypeChoice`

_Review Directions_

In the CDM Portal, select the Textual Browser and search for the updated descriptions related to the ECS data CDM model mentioned above. Review and inspect all updated descriptions, these include missing, re written descriptions and any updates needed to be in line with the Rosetta style guide. These changes span across the following namespaces: 
  
    •	base-datetime-type
    
    •	base-math-enum
  
    •	base-staticdata-asset-common-enum
  
    •	base-staticdata-asset-common-type
  
    •	legalagreement-csa-enum
  
    •	legalagreement-csa-type
  
    •	observable-asset-enum
  
    •	observable-asset-type

Search for the `DebtInterestEnum` list, please inspect the re alignment of  `OtherStructured` which was originally not properly listed and appeared to follow on from the line above `InterestOnly` this has now been correctly positioned beneath.

Search for data type `AssetType` and inspect the changes, an additional attribute has been added to this type called `otherAssetType` this is represented as a data `string` with an unlimited cardinality to allow you to represent various other asset types if needed. A related condition is also added beneath that so that if you identify `other` from the `AssetTypeEnum` options `otherAssetType` must exist.

Search for the data type `CollateralCriteriaBase` and inspect the changes, it has been modified so that `EligibleCollateralCriteria` extends `CollateralCriteriaBase` and now only contains the attributes `treatment`. `CollateralCriteriaBase` now only houses attributes `issuer` and `asset`.

Search for the data type `ConcentrationLimit`, and inspect the changes. The attribute `ConcentrationLimitCriteria` has been refactored to extend `CollateralCriteriaBase` that houses the relevant attributes to define asset and issuer criteria specifically. The data type still contains the attribute `ConcentrationLimitType` to allow a more generic specification of concentration limits. The related conditions have also been updated to reflect these changes. 

